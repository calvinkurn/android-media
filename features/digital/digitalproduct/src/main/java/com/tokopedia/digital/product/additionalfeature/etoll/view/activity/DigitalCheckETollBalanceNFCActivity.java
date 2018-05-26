package com.tokopedia.digital.product.additionalfeature.etoll.view.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.product.additionalfeature.etoll.ETollEventTracking;
import com.tokopedia.digital.product.additionalfeature.etoll.data.mapper.SmartcardMapper;
import com.tokopedia.digital.product.additionalfeature.etoll.data.repository.ETollRepository;
import com.tokopedia.digital.product.additionalfeature.etoll.data.source.SmartcardCommandDataSource;
import com.tokopedia.digital.product.additionalfeature.etoll.data.source.SmartcardInquiryDataSource;
import com.tokopedia.digital.product.additionalfeature.etoll.domain.interactor.SmartcardCommandUseCase;
import com.tokopedia.digital.product.additionalfeature.etoll.domain.interactor.SmartcardInquiryUseCase;
import com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview.ETollUpdateBalanceResultView;
import com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview.NFCDisabledView;
import com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview.TapETollCardView;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;
import com.tokopedia.digital.product.additionalfeature.etoll.view.presenter.ETollPresenter;
import com.tokopedia.digital.product.view.activity.DigitalProductActivity;
import com.tokopedia.digital.product.view.activity.DigitalWebActivity;
import com.tokopedia.digital.product.view.listener.IETollView;
import com.tokopedia.digital.utils.NFCUtils;

import java.io.IOException;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Rizky on 15/05/18.
 */
@RuntimePermissions
public class DigitalCheckETollBalanceNFCActivity extends BaseSimpleActivity
        implements IETollView {

    private static final String ETOLL_CATEGORY_ID = "34";
    private static final String ETOLL_EMONEY_OPERATOR_ID = "578";

    private static final String COMMAND_SELECT_EMONEY = "00A40400080000000000000001";
    private static final String COMMAND_CARD_ATTRIBUTE = "00F210000B";
    private static final String COMMAND_CARD_INFO = "00B300003F";
    private static final String COMMAND_LAST_BALANCE = "00B500000A";

    private static final String HELP_PAGE_URL = "https://www.tokopedia.com/bantuan/produk-digital/" +
            "e-money/#cara-update-saldo-kartu";

    private static final String TAG = DigitalCheckETollBalanceNFCActivity.class.getSimpleName();

    private ETollPresenter presenter;

    private TapETollCardView tapETollCardView;
    private NFCDisabledView nfcDisabledView;
    private ETollUpdateBalanceResultView eTollUpdateBalanceResultView;

    private NfcAdapter nfcAdapter;

    private IsoDep isoDep;

    private PendingIntent pendingIntent;
    private IntentFilter [] intentFiltersArray;
    private String [][] techListsArray;

    private AbstractionRouter abstractionRouter;

    public static Intent newInstance(Context context) {
        return new Intent(context, DigitalCheckETollBalanceNFCActivity.class);
    }

    @SuppressWarnings("unused")
    @DeepLink({ApplinkConst.DIGITAL_SMARTCARD})
    public static TaskStackBuilder intentForTaskStackBuilderMethods(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent homeIntent;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivity(context);
        }
        homeIntent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                HomeRouter.INIT_STATE_FRAGMENT_HOME);
        taskStackBuilder.addNextIntent(homeIntent);

        DigitalCategoryDetailPassData passData = new DigitalCategoryDetailPassData.Builder()
                .appLinks(uri.toString())
                .categoryId(ETOLL_CATEGORY_ID)
                .operatorId(ETOLL_EMONEY_OPERATOR_ID)
                .build();
        Intent intentDigitalProduct = DigitalProductActivity.newInstance(context, passData);
        taskStackBuilder.addNextIntent(intentDigitalProduct);

        Intent intentEToll = DigitalCheckETollBalanceNFCActivity.newInstance(context);
        taskStackBuilder.addNextIntent(intentEToll);

        return taskStackBuilder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateTitle(getResources().getString(R.string.toolbar_title_etoll_check_balance));

        abstractionRouter = (AbstractionRouter) getApplication();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter isodep = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

        intentFiltersArray = new IntentFilter [] {isodep, };

        techListsArray = new String[][] { new String[] { IsoDep.class.getName(), NfcA.class.getName()} };

        SmartcardMapper mapper = new SmartcardMapper();
        DigitalEndpointService digitalEndpointService = new DigitalEndpointService();
        SmartcardInquiryDataSource smartcardInquiryDataSource = new SmartcardInquiryDataSource(
                digitalEndpointService, mapper);
        SmartcardCommandDataSource smartcardCommandDataSource = new SmartcardCommandDataSource(
                digitalEndpointService, mapper);
        ETollRepository eTollRepository = new ETollRepository(smartcardInquiryDataSource,
                smartcardCommandDataSource);
        SmartcardInquiryUseCase smartcardInquiryUseCase = new SmartcardInquiryUseCase(eTollRepository);
        SmartcardCommandUseCase smartcardCommandUseCase = new SmartcardCommandUseCase(eTollRepository);

        presenter = new ETollPresenter(this, smartcardInquiryUseCase, smartcardCommandUseCase);

        if (getIntent() != null && !TextUtils.isEmpty(getIntent().getAction()) &&
            getIntent().getAction().equals(NfcAdapter.ACTION_TECH_DISCOVERED)) {
            handleIntent(getIntent());
        }
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);

        eTollUpdateBalanceResultView = findViewById(R.id.view_update_balance_result);
        nfcDisabledView = findViewById(R.id.view_nfc_disabled);
        tapETollCardView = findViewById(R.id.view_tap_emoney_card);

        eTollUpdateBalanceResultView.setListener(new ETollUpdateBalanceResultView.OnTopupETollClickListener() {
            @Override
            public void onClick() {
                if (getApplication() instanceof AbstractionRouter) {
                    abstractionRouter
                            .getAnalyticTracker()
                            .sendEventTracking(
                                    ETollEventTracking.Event.CLICK_NFC,
                                    ETollEventTracking.Category.DIGITAL_NFC,
                                    ETollEventTracking.Action.CLICK_TOPUP_EMONEY,
                                    ETollEventTracking.Label.EMONEY
                            );
                }

                // navigate to category emoney page
                DigitalCategoryDetailPassData passData = new DigitalCategoryDetailPassData.Builder()
                        .categoryId(ETOLL_CATEGORY_ID)
                        .operatorId(ETOLL_EMONEY_OPERATOR_ID)
                        .clientNumber(eTollUpdateBalanceResultView.getCardNumber())
                        .additionalETollLastBalance(eTollUpdateBalanceResultView.getCardLastBalance())
                        .additionalETollLastUpdatedDate(eTollUpdateBalanceResultView.getCardLastUpdatedDate())
                        .build();

                Intent intent = DigitalProductActivity.newInstance(
                        DigitalCheckETollBalanceNFCActivity.this,
                        passData);

                startActivity(intent);
                finish();
            }
        });

        nfcDisabledView.setListener(new NFCDisabledView.OnActivateNFCClickListener() {
            @Override
            public void onClick() {
                if (getApplication() instanceof AbstractionRouter) {
                    abstractionRouter
                            .getAnalyticTracker()
                            .sendEventTracking(
                                    ETollEventTracking.Event.CLICK_NFC,
                                    ETollEventTracking.Category.DIGITAL_NFC,
                                    ETollEventTracking.Action.CLICK_ACTIVATE,
                                    ETollEventTracking.Label.EMONEY
                            );
                }
                directToNFCSettingsPage();
            }
        });
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
            DigitalCheckETollBalanceNFCActivityPermissionsDispatcher.detectNFCWithCheck(this);
        } else {
            // show webview help page
            startActivity(DigitalWebActivity.newInstance(this, HELP_PAGE_URL));
            finish();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (eTollUpdateBalanceResultView.getVisibility() == View.VISIBLE) {
            eTollUpdateBalanceResultView.showLoading();
        } else {
            tapETollCardView.setVisibility(View.VISIBLE);
            tapETollCardView.showLoading();
            if (getApplication() instanceof AbstractionRouter) {
                abstractionRouter
                        .getAnalyticTracker()
                        .sendEventTracking(
                                ETollEventTracking.Event.CLICK_NFC,
                                ETollEventTracking.Category.DIGITAL_NFC,
                                ETollEventTracking.Action.CHECK_STEP_2,
                                ETollEventTracking.Label.EMONEY
                        );
            }
        }

        final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //do something with tagFromIntent
        isoDep = IsoDep.get(tag);

        try {
            isoDep.connect();
            isoDep.setTimeout(5000); // 5 sec time out

            final byte[] commandSelectEMoney =
                    isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_SELECT_EMONEY));
            final byte[] commandCardAttribute =
                    isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_CARD_ATTRIBUTE));
            final byte[] commandCardInfo =
                    isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_CARD_INFO));
            final byte[] commandLastBalance =
                    isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_LAST_BALANCE));

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    byte[] cardUID = isoDep.getTag().getId();

                    String responseSelectEMoney = NFCUtils.toHex(commandSelectEMoney);
                    String responseCardAttribute = NFCUtils.toHex(commandCardAttribute);
                    String responseCardUID = NFCUtils.toHex(cardUID);
                    String responseCardInfo = NFCUtils.toHex(commandCardInfo);
                    String responseCardLastBalance = NFCUtils.toHex(commandLastBalance);

                    if (responseSelectEMoney.equals("9000")) {
                        presenter.inquiryBalance(1, responseCardAttribute, responseCardInfo,
                                responseCardUID, responseCardLastBalance);
                    } else {
                        // show error reading card
                        if (getApplication() instanceof AbstractionRouter) {
                            abstractionRouter
                                    .getAnalyticTracker()
                                    .sendEventTracking(
                                            ETollEventTracking.Event.CLICK_NFC,
                                            ETollEventTracking.Category.DIGITAL_NFC,
                                            ETollEventTracking.Action.CARD_IS_NOT_SUPPORTED,
                                            ETollEventTracking.Label.EMONEY
                                    );
                        }
                        showError(getResources().getString(R.string.card_is_not_supported));
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            showError(getResources().getString(R.string.failed_read_card));
        }
    }

    @Override
    public void sendCommand(final InquiryBalanceModel inquiryBalanceModel) {
        if (isoDep != null && isoDep.isConnected()) {
            try {
                final byte [] responseInByte = isoDep.transceive(NFCUtils.hexStringToByteArray(inquiryBalanceModel.getCommand()));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseInByte != null) {
                            String response = NFCUtils.toHex(responseInByte);
                            Log.d(TAG, response);
                            presenter.sendCommand(response, inquiryBalanceModel.getId(), 1);
                        }
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
        if (getApplication() instanceof AbstractionRouter) {
            abstractionRouter
                    .getAnalyticTracker()
                    .sendEventTracking(
                            ETollEventTracking.Event.CLICK_NFC,
                            ETollEventTracking.Category.DIGITAL_NFC,
                            ETollEventTracking.Action.SUCCESS_CHECK_BALANCE,
                            "emoney"
                    );
        }
        tapETollCardView.setVisibility(View.GONE);
        eTollUpdateBalanceResultView.setVisibility(View.VISIBLE);
        eTollUpdateBalanceResultView.showCardInfoFromApi(inquiryBalanceModel);
        NetworkErrorHelper.showGreenCloseSnackbar(this,
                getResources().getString(R.string.success_update_balance));
    }

    @Override
    public void showError(String errorMessage) {
        if (getApplication() instanceof AbstractionRouter) {
            abstractionRouter
                    .getAnalyticTracker()
                    .sendEventTracking(
                            ETollEventTracking.Event.CLICK_NFC,
                            ETollEventTracking.Category.DIGITAL_NFC,
                            ETollEventTracking.Action.FAILED_UPDATE_BALANCE,
                            ETollEventTracking.Label.EMONEY
                    );
        }
        if (eTollUpdateBalanceResultView.getVisibility() == View.VISIBLE) {
            eTollUpdateBalanceResultView.showError(errorMessage);
        } else {
            tapETollCardView.setVisibility(View.VISIBLE);
            tapETollCardView.showInitialState();
            NetworkErrorHelper.showRedCloseSnackbar(this,
                    errorMessage);
        }
    }

    @NeedsPermission(Manifest.permission.NFC)
    void detectNFC() {
        if (!nfcAdapter.isEnabled()) {
            eTollUpdateBalanceResultView.setVisibility(View.GONE);
            tapETollCardView.setVisibility(View.GONE);

            new AlertDialog.Builder(this)
                    .setMessage("Silahkan aktifkan pengaturan NFC pada handphone Anda, untuk melanjutkan")
                    .setPositiveButton("Aktifkan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (getApplication() instanceof AbstractionRouter) {
                                abstractionRouter
                                        .getAnalyticTracker()
                                        .sendEventTracking(
                                                ETollEventTracking.Event.CLICK_NFC,
                                                ETollEventTracking.Category.DIGITAL_NFC,
                                                ETollEventTracking.Action.CLICK_ACTIVATE_PROMPT,
                                                ETollEventTracking.Label.EMONEY
                                        );
                            }
                            directToNFCSettingsPage();
                        }
                    })
                    .setNegativeButton("Batalkan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (getApplication() instanceof AbstractionRouter) {
                                abstractionRouter
                                        .getAnalyticTracker()
                                        .sendEventTracking(
                                                ETollEventTracking.Event.CLICK_NFC,
                                                ETollEventTracking.Category.DIGITAL_NFC,
                                                ETollEventTracking.Action.CLICK_CANCEL_PROMPT,
                                                ETollEventTracking.Label.EMONEY
                                        );
                            }
                            nfcDisabledView.setVisibility(View.VISIBLE);
                        }
                    }).show();
        } else {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
            nfcDisabledView.setVisibility(View.GONE);

            if (eTollUpdateBalanceResultView.getVisibility() == View.GONE) {
                if (getApplication() instanceof AbstractionRouter) {
                    abstractionRouter
                            .getAnalyticTracker()
                            .sendEventTracking(
                                    ETollEventTracking.Event.CLICK_NFC,
                                    ETollEventTracking.Category.DIGITAL_NFC,
                                    ETollEventTracking.Action.CHECK_STEP_1,
                                    ETollEventTracking.Label.EMONEY
                            );
                }
                tapETollCardView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void directToNFCSettingsPage() {
        Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
        startActivity(intent);
    }

    @OnShowRationale(Manifest.permission.NFC)
    void showRationaleForNFC(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("Aplikasi ini membutuhkan izin untuk mengakses NFC")
                .setPositiveButton("Izinkan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("Tolak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                }).show();
    }

    @OnPermissionDenied(Manifest.permission.NFC)
    void showDeniedForCamera() {
        Toast.makeText(this, "Anda tidak memberikan izin akses untuk NFC",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getStringResource(int stringRes) {
        return getResources().getString(stringRes);
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

}