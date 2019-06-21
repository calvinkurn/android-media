package com.tokopedia.digital.product.additionalfeature.etoll.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.constant.DigitalUrl;
import com.tokopedia.digital.common.di.DigitalComponent;
import com.tokopedia.digital.common.di.DigitalComponentInstance;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.product.additionalfeature.etoll.ETollEventTracking;
import com.tokopedia.digital.product.additionalfeature.etoll.di.DaggerDigitalETollComponent;
import com.tokopedia.digital.product.additionalfeature.etoll.di.DigitalETollComponent;
import com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview.ETollUpdateBalanceResultView;
import com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview.NFCDisabledView;
import com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview.TapETollCardView;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;
import com.tokopedia.digital.product.additionalfeature.etoll.view.presenter.ETollPresenter;
import com.tokopedia.digital.product.view.activity.DigitalProductActivity;
import com.tokopedia.digital.product.view.listener.IETollView;
import com.tokopedia.digital.product.view.model.DigitalCategoryDetailPassData;
import com.tokopedia.digital.utils.NFCUtils;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;

import java.io.IOException;

import javax.inject.Inject;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Rizky on 15/05/18.
 */
public class DigitalCheckETollBalanceNFCActivity extends BaseSimpleActivity
        implements IETollView {

    private static final String ETOLL_CATEGORY_ID = "34";
    private static final String ETOLL_EMONEY_OPERATOR_ID = "578";

    private static final String COMMAND_SELECT_EMONEY = "00A40400080000000000000001";
    private static final String COMMAND_CARD_ATTRIBUTE = "00F210000B";
    private static final String COMMAND_CARD_INFO = "00B300003F";
    private static final String COMMAND_LAST_BALANCE = "00B500000A";

    private static final String COMMAND_SUCCESSFULLY_EXECUTED = "9000";

    private static final int TRANSCEIVE_TIMEOUT_IN_SEC = 5000;

    private static final String DIGITAL_SMARTCARD = "mainapp_digital_smartcard";
    public static final String DIGITAL_NFC_CALLING_TYPE = "calling_page_check_saldo";
    public static final String DIGITAL_NFC_FROM_PDP = "calling_from_pdp";
    private static final String DIGITAL_NFC = "calling_from_nfc";

    private static final String TAG = DigitalCheckETollBalanceNFCActivity.class.getSimpleName();

    @Inject
    ETollPresenter presenter;

    private TapETollCardView tapETollCardView;
    private NFCDisabledView nfcDisabledView;
    private ETollUpdateBalanceResultView eTollUpdateBalanceResultView;

    private NfcAdapter nfcAdapter;

    private IsoDep isoDep;

    private PendingIntent pendingIntent;
    private IntentFilter [] intentFiltersArray;
    private String [][] techListsArray;

    private FirebaseRemoteConfigImpl remoteConfig;
    private PermissionCheckerHelper permissionCheckerHelper;

    public static Intent newInstance(Context context, String typeCallingPage) {
        Intent intent = new Intent(context, DigitalCheckETollBalanceNFCActivity.class);
        intent.putExtra(DIGITAL_NFC_CALLING_TYPE, typeCallingPage);
        return intent;
    }

    @SuppressWarnings("unused")
    @DeepLink({ApplinkConst.DIGITAL_SMARTCARD})
    public static TaskStackBuilder intentForTaskStackBuilderMethods(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent homeIntent = ((DigitalRouter) context.getApplicationContext()).getHomeIntent(context);
        taskStackBuilder.addNextIntent(homeIntent);

        DigitalCategoryDetailPassData passData = new DigitalCategoryDetailPassData.Builder()
                .appLinks(uri.toString())
                .categoryId(ETOLL_CATEGORY_ID)
                .operatorId(ETOLL_EMONEY_OPERATOR_ID)
                .build();
        Intent intentDigitalProduct = DigitalProductActivity.newInstance(context, passData);
        taskStackBuilder.addNextIntent(intentDigitalProduct);

        Intent intentEToll = DigitalCheckETollBalanceNFCActivity.newInstance(context, DIGITAL_NFC);
        taskStackBuilder.addNextIntent(intentEToll);

        return taskStackBuilder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initInjector();
        super.onCreate(savedInstanceState);
        presenter.attachView(this);

        remoteConfig = new FirebaseRemoteConfigImpl(this);

        updateTitle(getResources().getString(R.string.toolbar_title_etoll_check_balance));

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter isodep = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

        intentFiltersArray = new IntentFilter [] {isodep, };

        techListsArray = new String[][] { new String[] { IsoDep.class.getName(), NfcA.class.getName()} };
        handleIntent(getIntent());
    }

    private void initInjector() {
        DigitalComponent digitalComponent = DigitalComponentInstance.getInstance(getApplication());
        DigitalETollComponent component = DaggerDigitalETollComponent.builder().digitalComponent(digitalComponent).build();
        component.inject(this);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);

        eTollUpdateBalanceResultView = findViewById(R.id.view_update_balance_result);
        nfcDisabledView = findViewById(R.id.view_nfc_disabled);
        tapETollCardView = findViewById(R.id.view_tap_emoney_card);

        eTollUpdateBalanceResultView.setListener(() -> {
                TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                                ETollEventTracking.Event.CLICK_NFC,
                                ETollEventTracking.Category.DIGITAL_NFC,
                                ETollEventTracking.Action.CLICK_TOPUP_EMONEY,
                                ETollEventTracking.Label.EMONEY
                        ));

            // navigate to category emoney page
            DigitalCategoryDetailPassData passData = new DigitalCategoryDetailPassData.Builder()
                    .categoryId(ETOLL_CATEGORY_ID)
                    .operatorId(ETOLL_EMONEY_OPERATOR_ID)
                    .clientNumber(eTollUpdateBalanceResultView.getCardNumber())
                    .additionalETollLastBalance(eTollUpdateBalanceResultView.getCardLastBalance())
                    .additionalETollLastUpdatedDate(eTollUpdateBalanceResultView.getCardLastUpdatedDate())
                    .build();

            if (getIntent() != null && getIntent().getStringExtra(DIGITAL_NFC_CALLING_TYPE) != null) {
                if (getIntent().getStringExtra(DIGITAL_NFC_CALLING_TYPE) == DIGITAL_NFC) {
                    navigatePageToDigitalProduct(passData);
                } else {
                    Intent intentReturn = new Intent();
                    intentReturn.putExtra(DigitalProductActivity.EXTRA_CATEGORY_PASS_DATA, passData);
                    setResult(Activity.RESULT_OK, intentReturn);
                }
            } else {
                navigatePageToDigitalProduct(passData);
            }
            finish();
        });

        nfcDisabledView.setListener(() -> {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                            ETollEventTracking.Event.CLICK_NFC,
                            ETollEventTracking.Category.DIGITAL_NFC,
                            ETollEventTracking.Action.CLICK_ACTIVATE,
                            ETollEventTracking.Label.EMONEY
                    ));
            directToNFCSettingsPage();
        });
    }

    private void navigatePageToDigitalProduct(DigitalCategoryDetailPassData passData) {
        Intent intent = DigitalProductActivity.newInstance(
                DigitalCheckETollBalanceNFCActivity.this,
                passData);
        startActivity(intent);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_check_etoll_balance_nfc;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            permissionCheckerHelper = new PermissionCheckerHelper();
            permissionCheckerHelper.checkPermission(this, PermissionCheckerHelper.Companion.PERMISSION_NFC, new PermissionCheckerHelper.PermissionCheckListener() {
                @Override
                public void onPermissionDenied(@NotNull String permissionText) {
                    permissionCheckerHelper.onPermissionDenied(DigitalCheckETollBalanceNFCActivity.this, permissionText);
                }

                @Override
                public void onNeverAskAgain(@NotNull String permissionText) {
                    permissionCheckerHelper.onNeverAskAgain(DigitalCheckETollBalanceNFCActivity.this, permissionText);
                }

                @Override
                public void onPermissionGranted() {
                    detectNFC();
                }
            }, getStringResource(R.string.nfc_permission_rationale_message));
        } else {
            // show webview help page
            Intent intent = RouteManager.getIntent(this, ApplinkConst.CONTACT_US_NATIVE);
            startActivity(intent);
            finish();
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionCheckerHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction()) &&
                intent.getAction().equals(NfcAdapter.ACTION_TECH_DISCOVERED)) {
            if (!isDigitalSmartcardEnabled()) {
                Toast.makeText(this, "Fitur ini belum tersedia", Toast.LENGTH_SHORT).show();
                RouteManager.route(this, ApplinkConst.HOME_FEED);
                finish();
            } else {
                final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                if (tag != null) {
                    handleTagFromIntent(tag);
                }
            }
        }
    }

    private void handleTagFromIntent(Tag tag) {
        showLoading();

        //do something with tagFromIntent
        isoDep = IsoDep.get(tag);

        try {
            isoDep.close();
            isoDep.connect();
            isoDep.setTimeout(TRANSCEIVE_TIMEOUT_IN_SEC); // 5 sec time out

            final byte[] commandSelectEMoney =
                    isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_SELECT_EMONEY));
            final byte[] commandCardAttribute =
                    isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_CARD_ATTRIBUTE));
            final byte[] commandCardInfo =
                    isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_CARD_INFO));
            final byte[] commandLastBalance =
                    isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_LAST_BALANCE));

            runOnUiThread(() -> {
                byte[] cardUID = isoDep.getTag().getId();

                String responseSelectEMoney = NFCUtils.toHex(commandSelectEMoney);
                String responseCardAttribute = NFCUtils.toHex(commandCardAttribute);
                String responseCardUID = NFCUtils.toHex(cardUID);
                String responseCardInfo = NFCUtils.toHex(commandCardInfo);
                String responseCardLastBalance = NFCUtils.toHex(commandLastBalance);

                if (responseSelectEMoney.equals(COMMAND_SUCCESSFULLY_EXECUTED)) {
                    presenter.inquiryBalance(1, responseCardAttribute, responseCardInfo,
                            responseCardUID, responseCardLastBalance);
                } else {
                    // show error reading card
                    if (getApplication() instanceof AbstractionRouter) {
                        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                                        ETollEventTracking.Event.CLICK_NFC,
                                        ETollEventTracking.Category.DIGITAL_NFC,
                                        ETollEventTracking.Action.CARD_IS_NOT_SUPPORTED,
                                        ETollEventTracking.Label.EMONEY
                                ));
                    }
                    showError(getResources().getString(R.string.card_is_not_supported));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            showError(getResources().getString(R.string.failed_read_card));
        }
    }

    private void showLoading() {
        if (eTollUpdateBalanceResultView.getVisibility() == View.VISIBLE) {
            eTollUpdateBalanceResultView.showLoading();
        } else {
            tapETollCardView.setVisibility(View.VISIBLE);
            tapETollCardView.showLoading();
            if (getApplication() instanceof AbstractionRouter) {
                TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                        ETollEventTracking.Event.CLICK_NFC,
                                ETollEventTracking.Category.DIGITAL_NFC,
                                ETollEventTracking.Action.CHECK_STEP_2,
                                ETollEventTracking.Label.EMONEY
                        ));
            }
        }
    }

    @Override
    public void sendCommand(final InquiryBalanceModel inquiryBalanceModel) {
        if (isoDep != null && isoDep.isConnected()) {
            try {
                final byte [] responseInByte = isoDep.transceive(NFCUtils.hexStringToByteArray(inquiryBalanceModel.getCommand()));

                runOnUiThread(() -> {
                    if (responseInByte != null) {
                        String response = NFCUtils.toHex(responseInByte);
                        presenter.sendCommand(response, inquiryBalanceModel.getId(), 1);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                showError(getResources().getString(R.string.update_balance_failed));
            }
        } else {
            showError(getResources().getString(R.string.update_balance_failed));
        }
    }

    @Override
    public void showCardLastBalance(InquiryBalanceModel inquiryBalanceModel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                            ETollEventTracking.Event.CLICK_NFC,
                            ETollEventTracking.Category.DIGITAL_NFC,
                            ETollEventTracking.Action.SUCCESS_CHECK_BALANCE,
                            ETollEventTracking.Label.EMONEY
                    ));
        tapETollCardView.setVisibility(View.GONE);
        eTollUpdateBalanceResultView.setVisibility(View.VISIBLE);
        eTollUpdateBalanceResultView.showCardInfoFromApi(inquiryBalanceModel);
        NetworkErrorHelper.showGreenCloseSnackbar(this, inquiryBalanceModel.getErrorMessage());
    }

    @Override
    public void showError(String errorMessage) {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                            ETollEventTracking.Event.CLICK_NFC,
                            ETollEventTracking.Category.DIGITAL_NFC,
                            ETollEventTracking.Action.FAILED_UPDATE_BALANCE,
                            ETollEventTracking.Label.EMONEY
                    ));
        if (eTollUpdateBalanceResultView.getVisibility() == View.VISIBLE) {
            eTollUpdateBalanceResultView.showError(errorMessage);
        } else {
            tapETollCardView.setVisibility(View.VISIBLE);
            tapETollCardView.showInitialState();
            tapETollCardView.showErrorState(errorMessage);
        }
    }

    void detectNFC() {
        if (!nfcAdapter.isEnabled()) {
            eTollUpdateBalanceResultView.setVisibility(View.GONE);
            tapETollCardView.setVisibility(View.GONE);

            new AlertDialog.Builder(this)
                    .setMessage(getStringResource(R.string.please_activate_nfc_from_settings))
                    .setPositiveButton(getStringResource(R.string.activate), (dialog, which) -> {
                            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                                            ETollEventTracking.Event.CLICK_NFC,
                                            ETollEventTracking.Category.DIGITAL_NFC,
                                            ETollEventTracking.Action.CLICK_ACTIVATE_PROMPT,
                                            ETollEventTracking.Label.EMONEY
                                    ));

                        directToNFCSettingsPage();
                    })
                    .setNegativeButton(getStringResource(R.string.cancel), (dialog, which) -> {
                                TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                                            ETollEventTracking.Event.CLICK_NFC,
                                            ETollEventTracking.Category.DIGITAL_NFC,
                                            ETollEventTracking.Action.CLICK_CANCEL_PROMPT,
                                            ETollEventTracking.Label.EMONEY
                                    ));
                        nfcDisabledView.setVisibility(View.VISIBLE);
                    }).show();
        } else {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
            nfcDisabledView.setVisibility(View.GONE);

            if (eTollUpdateBalanceResultView.getVisibility() == View.GONE) {
                    TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                                    ETollEventTracking.Event.CLICK_NFC,
                                    ETollEventTracking.Category.DIGITAL_NFC,
                                    ETollEventTracking.Action.CHECK_STEP_1,
                                    ETollEventTracking.Label.EMONEY
                            ));

                tapETollCardView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void directToNFCSettingsPage() {
        Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
        startActivity(intent);
    }

    @Override
    public String getStringResource(int stringRes) {
        return getResources().getString(stringRes);
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    private boolean isDigitalSmartcardEnabled() {
        return remoteConfig.getBoolean(DIGITAL_SMARTCARD, false);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
