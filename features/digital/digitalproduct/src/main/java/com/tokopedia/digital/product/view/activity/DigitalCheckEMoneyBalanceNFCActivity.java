package com.tokopedia.digital.product.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.compoundview.EMoneyUpdateBalanceResultView;
import com.tokopedia.digital.product.view.compoundview.NFCDisabledView;
import com.tokopedia.digital.product.view.compoundview.TapEMoneyCardView;
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
public class DigitalCheckEMoneyBalanceNFCActivity extends BaseSimpleActivity {

    private static final String TAG = DigitalCheckEMoneyBalanceNFCActivity.class.getSimpleName();

    private TapEMoneyCardView tapEMoneyCardView;
    private NFCDisabledView nfcDisabledView;
    private EMoneyUpdateBalanceResultView eMoneyUpdateBalanceResultView;

    private NfcAdapter nfcAdapter;

    private IsoDep isoDep;

    private PendingIntent pendingIntent;
    private IntentFilter [] intentFiltersArray;
    private String [][] techListsArray;

    public static Intent newInstance(Activity activity) {
        return new Intent(activity, DigitalCheckEMoneyBalanceNFCActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateTitle("Cek Saldo");

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        intentFiltersArray = new IntentFilter [] {};

        techListsArray = new String[][] { new String[] { IsoDep.class.getName() } };
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);

        eMoneyUpdateBalanceResultView = findViewById(R.id.view_update_balance_result);
        nfcDisabledView = findViewById(R.id.view_nfc_disabled);
        tapEMoneyCardView = findViewById(R.id.view_tap_emoney_card);

        eMoneyUpdateBalanceResultView.setListener(new EMoneyUpdateBalanceResultView.OnTopupEMoneyClickListener() {
            @Override
            public void onClick() {
                // navigate to category emoney page
//                RouteManager.route(DigitalCheckEMoneyBalanceNFCActivity.this,
//                        ApplinkConst.DIGITAL_PRODUCT+"?category_id=34");

                DigitalCategoryDetailPassData passData = new DigitalCategoryDetailPassData.Builder()
                        .categoryId("34")
                        .clientNumber(eMoneyUpdateBalanceResultView.getCardNumber())
                        .build();

                Intent intent = DigitalProductActivity.newInstance(DigitalCheckEMoneyBalanceNFCActivity.this,
                        passData);

                startActivity(intent);
                finish();

//                if (isoDep != null) {
//                    if (isoDep.isConnected()) {
//                        String command = "0";
//                        try {
//                            final byte [] responseInByte = isoDep.transceive(NFCUtils.hexStringToByteArray(command));
//
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (responseInByte != null) {
//                                        String response = NFCUtils.toHex(responseInByte);
//                                        Log.d(TAG, response);
//                                    }
//                                }
//                            });
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
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
        return R.layout.activity_check_emoney_balance_nfc;
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
        } else {
            // show webview help page
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            DigitalCheckEMoneyBalanceNFCActivityPermissionsDispatcher.detectNFCWithCheck(this);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (eMoneyUpdateBalanceResultView.getVisibility() == View.VISIBLE) {
            eMoneyUpdateBalanceResultView.showLoading();
        } else {
            tapEMoneyCardView.showLoading(getResources().getString(R.string.reading_card_label));
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
                    String responseLastBalance = NFCUtils.toHex(commandLastBalance);

                    Log.d(TAG, "\nSelect eMoney: " + responseSelectEMoney +
                            "\nCard Attribute: " + responseCardAttribute +
                            "\nCard UID: " + responseCardUID +
                            "\nCard Info: " + responseCardInfo +
                            "\nLast Balance: " + responseLastBalance);

                    final CardInfo cardInfo = new CardInfo(responseSelectEMoney, responseCardUID, responseCardAttribute,
                            responseCardInfo, responseLastBalance);

                    if (cardInfo.isValid()) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                tapEMoneyCardView.setVisibility(View.GONE);
                                eMoneyUpdateBalanceResultView.showCardInfo(cardInfo);
                            }
                        }, 5000);
                    } else {
                        // show error reading card
                        if (eMoneyUpdateBalanceResultView.getVisibility() == View.VISIBLE) {
                            eMoneyUpdateBalanceResultView.showCardIsNotSupported();
                        } else {
                            tapEMoneyCardView.stopLoading();
                            NetworkErrorHelper.showRedCloseSnackbar(DigitalCheckEMoneyBalanceNFCActivity.this,
                                    getResources().getString(R.string.card_is_not_supported));
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NeedsPermission(Manifest.permission.NFC)
    void detectNFC() {
        if (!nfcAdapter.isEnabled()) {
            eMoneyUpdateBalanceResultView.setVisibility(View.GONE);
            tapEMoneyCardView.setVisibility(View.GONE);

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
            if (eMoneyUpdateBalanceResultView.getVisibility() == View.GONE) {
                tapEMoneyCardView.setVisibility(View.VISIBLE);
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

}