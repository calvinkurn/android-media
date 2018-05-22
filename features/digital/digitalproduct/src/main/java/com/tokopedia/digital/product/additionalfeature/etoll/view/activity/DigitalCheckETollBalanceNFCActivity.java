package com.tokopedia.digital.product.additionalfeature.etoll.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.product.additionalfeature.etoll.data.mapper.SmartcardInquiryMapper;
import com.tokopedia.digital.product.additionalfeature.etoll.data.repository.ETollRepository;
import com.tokopedia.digital.product.additionalfeature.etoll.data.source.SmartcardCommandDataSource;
import com.tokopedia.digital.product.additionalfeature.etoll.data.source.SmartcardInquiryDataSource;
import com.tokopedia.digital.product.additionalfeature.etoll.domain.interactor.SmartcardInquiryUseCase;
import com.tokopedia.digital.product.additionalfeature.etoll.domain.interactor.SendCommandUseCase;
import com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview.ETollUpdateBalanceResultView;
import com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview.NFCDisabledView;
import com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview.TapETollCardView;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;
import com.tokopedia.digital.product.additionalfeature.etoll.view.presenter.ETollPresenter;
import com.tokopedia.digital.product.view.activity.DigitalProductActivity;
import com.tokopedia.digital.product.view.listener.IEMoneyView;
import com.tokopedia.digital.product.view.model.CardInfo;
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
        implements IEMoneyView {

    private static final String TAG = DigitalCheckETollBalanceNFCActivity.class.getSimpleName();

    private ETollPresenter presenter;

    private CardInfo fetchedCardInfo;

    private TapETollCardView tapETollCardView;
    private NFCDisabledView nfcDisabledView;
    private ETollUpdateBalanceResultView eTollUpdateBalanceResultView;

    private NfcAdapter nfcAdapter;

    private IsoDep isoDep;

    private PendingIntent pendingIntent;
    private IntentFilter [] intentFiltersArray;
    private String [][] techListsArray;

    public static Intent newInstance(Activity activity) {
        return new Intent(activity, DigitalCheckETollBalanceNFCActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateTitle("Cek Saldo");

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter isodep = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

        intentFiltersArray = new IntentFilter [] {isodep, };

        techListsArray = new String[][] { new String[] { IsoDep.class.getName(), NfcA.class.getName()} };

        SmartcardInquiryMapper mapper = new SmartcardInquiryMapper();
        DigitalEndpointService digitalEndpointService = new DigitalEndpointService();
        SmartcardInquiryDataSource smartcardInquiryDataSource = new SmartcardInquiryDataSource(this,
                digitalEndpointService, mapper);
        SmartcardCommandDataSource smartcardCommandDataSource = new SmartcardCommandDataSource(this, mapper);
        ETollRepository eTollRepository = new ETollRepository(smartcardInquiryDataSource, smartcardCommandDataSource);
        SmartcardInquiryUseCase smartcardInquiryUseCase = new SmartcardInquiryUseCase(eTollRepository);
        SendCommandUseCase sendCommandUseCase = new SendCommandUseCase(eTollRepository);

        presenter = new ETollPresenter(this, smartcardInquiryUseCase, sendCommandUseCase);
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
                // navigate to category emoney page
                DigitalCategoryDetailPassData passData = new DigitalCategoryDetailPassData.Builder()
                        .categoryId("34")
                        .operatorId("419")
                        .clientNumber(eTollUpdateBalanceResultView.getCardNumber())
                        .build();

                Intent intent = DigitalProductActivity.newInstance(DigitalCheckETollBalanceNFCActivity.this,
                        passData);

                startActivity(intent);
                finish();
            }
        });

        nfcDisabledView.setListener(new NFCDisabledView.OnActivateNFCClickListener() {
            @Override
            public void onClick() {
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
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (eTollUpdateBalanceResultView.getVisibility() == View.VISIBLE) {
            eTollUpdateBalanceResultView.showLoading();
        } else {
            tapETollCardView.setVisibility(View.VISIBLE);
            tapETollCardView.showLoading(getResources().getString(R.string.reading_card_label));
        }

        final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //do something with tagFromIntent
        isoDep = IsoDep.get(tag);

        try {
            isoDep.connect();
            isoDep.setTimeout(5000); // 5 sec time out

            final byte[] commandSelectEMoney = isoDep.transceive(NFCUtils.hexStringToByteArray("00A40400080000000000000001"));
            final byte[] commandCardAttribute = isoDep.transceive(NFCUtils.hexStringToByteArray("00F210000B"));
            final byte[] commandCardInfo = isoDep.transceive(NFCUtils.hexStringToByteArray("00B300003F"));
            final byte[] commandLastBalance = isoDep.transceive(NFCUtils.hexStringToByteArray("00B500000A"));

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    byte[] cardUID = isoDep.getTag().getId();

                    String responseSelectEMoney = NFCUtils.toHex(commandSelectEMoney);
                    String responseCardAttribute = NFCUtils.toHex(commandCardAttribute);
                    String responseCardUID = NFCUtils.toHex(cardUID);
                    String responseCardInfo = NFCUtils.toHex(commandCardInfo);
                    String responseCardLastBalance = NFCUtils.toHex(commandLastBalance);

//                    Log.d(TAG, "\nSelect eMoney: " + responseSelectEMoney +
//                            "\nCard Attribute: " + responseCardAttribute +
//                            "\nCard UID: " + responseCardUID +
//                            "\nCard Info: " + responseCardInfo +
//                            "\nLast Balance: " + responseCardLastBalance);

                    fetchedCardInfo = new CardInfo(NFCUtils.convertCardUID(responseCardInfo),
                            NFCUtils.convertCardLastBalance(responseCardLastBalance), null);

                    if (responseSelectEMoney.equals("9000")) {
                        presenter.inquiryBalance(1, responseCardAttribute, responseCardInfo,
                                responseCardUID, responseCardLastBalance);
                    } else {
                        // show error reading card
                        if (eTollUpdateBalanceResultView.getVisibility() == View.VISIBLE) {
                            eTollUpdateBalanceResultView.showError(getResources().getString(R.string.card_is_not_supported));
                        } else {
                            tapETollCardView.stopLoading();
                            NetworkErrorHelper.showRedCloseSnackbar(DigitalCheckETollBalanceNFCActivity.this,
                                    getResources().getString(R.string.card_is_not_supported));
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            if (eTollUpdateBalanceResultView.getVisibility() == View.VISIBLE) {
                eTollUpdateBalanceResultView.showError("Gagal membaca kartu");
            } else {
                tapETollCardView.stopLoading();
                NetworkErrorHelper.showRedCloseSnackbar(DigitalCheckETollBalanceNFCActivity.this,
                        "Gagal membaca kartu");
            }
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
                            directToNFCSettingsPage();
                        }
                    })
                    .setNegativeButton("Batalkan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            nfcDisabledView.setVisibility(View.VISIBLE);
                        }
                    }).show();
        } else {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
            nfcDisabledView.setVisibility(View.GONE);

            // prevent tapEMoneyCardView to show up if eMoneyUpdateBalanceResultView presents (visible)
            if (eTollUpdateBalanceResultView.getVisibility() == View.GONE) {
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
        Toast.makeText(this, "Anda tidak memberikan izin akses untuk NFC", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
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
                            presenter.sendCommand();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                showCardLastBalance(inquiryBalanceModel);
            }
        } else {
            showCardLastBalanceWithError(inquiryBalanceModel, inquiryBalanceModel.getErrorMessage());
        }
    }

    @Override
    public void showCardLastBalance(InquiryBalanceModel inquiryBalanceModel) {
        tapETollCardView.setVisibility(View.GONE);
        eTollUpdateBalanceResultView.setVisibility(View.VISIBLE);
        eTollUpdateBalanceResultView.showCardInfoFromApi(inquiryBalanceModel);
    }

    @Override
    public void showCardLastBalanceWithError(InquiryBalanceModel inquiryBalanceModel, String errorMessage) {
        tapETollCardView.setVisibility(View.GONE);
        eTollUpdateBalanceResultView.setVisibility(View.VISIBLE);
        eTollUpdateBalanceResultView.showCardInfoWithError(inquiryBalanceModel, errorMessage);
    }

    @Override
    public void renderLocalCardInfo() {
        tapETollCardView.setVisibility(View.GONE);
        eTollUpdateBalanceResultView.setVisibility(View.VISIBLE);
        eTollUpdateBalanceResultView.showLocalCardInfo(fetchedCardInfo);
    }

}