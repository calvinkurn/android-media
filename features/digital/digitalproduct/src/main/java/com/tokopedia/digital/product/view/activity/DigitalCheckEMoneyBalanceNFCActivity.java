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
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.compoundview.CheckEMoneyBalanceView;
import com.tokopedia.digital.product.view.compoundview.EMoneyCardInfoView;
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

    private EMoneyCardInfoView eMoneyCardInfoView;
    private TapEMoneyCardView tapEMoneyCardView;
    private Button buttonTopup;
    private LinearLayout viewCardInfo;

    private NfcAdapter nfcAdapter;

    private IsoDep isoDep;

    private PendingIntent pendingIntent;
    private IntentFilter [] intentFiltersArray;
    private String [][] techListsArray;

    public static Intent newInstance(Activity activity) {
        return new Intent(activity, DigitalCheckEMoneyBalanceNFCActivity.class);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);

        eMoneyCardInfoView = findViewById(R.id.view_emoney_card_info);
        tapEMoneyCardView = findViewById(R.id.view_tap_emoney_card);
        buttonTopup = findViewById(R.id.button_topup);
        viewCardInfo = findViewById(R.id.view_card_info);

        buttonTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isoDep != null) {
                    if (isoDep.isConnected()) {
                        String command = "0";
                        try {
                            final byte [] responseInByte = isoDep.transceive(NFCUtils.hexStringToByteArray(command));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (responseInByte != null) {
                                        String response = NFCUtils.toHex(responseInByte);
                                        Log.d(TAG, response);
                                    }
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateTitle("Cek Saldo");

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        intentFiltersArray = new IntentFilter [] {};

        techListsArray = new String[][] { new String[] { IsoDep.class.getName() } };

        if (nfcAdapter != null) {
            DigitalCheckEMoneyBalanceNFCActivityPermissionsDispatcher.detectNFCWithCheck(this);
        } else {
            // direct to
            Toast.makeText(this, "Beli hp yang ada NFC ya.. :)", Toast.LENGTH_SHORT).show();
        }
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
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
    }

    @Override
    public void onNewIntent(Intent intent) {
        tapEMoneyCardView.showLoading("Sedang membaca kartu... Jangan mengubah posisi kartu hingga selesai");

        Toast.makeText(this, "NFC Card Detected", Toast.LENGTH_SHORT).show();

        final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //do something with tagFromIntent
        isoDep = IsoDep.get(tag);
        try {
            isoDep.connect();
            isoDep.setTimeout(5000); // 5 sec time out

            final byte [] commandSelectEMoney = isoDep.transceive(NFCUtils.hexStringToByteArray("00A40400080000000000000001"));
            final byte [] commandCardAttribute = isoDep.transceive(NFCUtils.hexStringToByteArray("00F210000B"));
            final byte [] commandCardInfo = isoDep.transceive(NFCUtils.hexStringToByteArray("00B300003F"));
            final byte [] commandLastBalance = isoDep.transceive(NFCUtils.hexStringToByteArray("00B500000A"));

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    byte [] cardUID = isoDep.getTag().getId();

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

                    tapEMoneyCardView.setVisibility(View.GONE);

                    viewCardInfo.setVisibility(View.VISIBLE);

                    CardInfo cardInfo = new CardInfo(responseSelectEMoney, responseCardUID, responseCardAttribute,
                            responseCardInfo, responseLastBalance);

                    if (cardInfo.isValid()) {
                        eMoneyCardInfoView.showCardInfo(cardInfo);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NeedsPermission(Manifest.permission.NFC)
    void detectNFC() {
        if (nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC detected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enable NFC from Settings", Toast.LENGTH_SHORT).show();

            new AlertDialog.Builder(this)
                    .setMessage("Silahkan aktifkan pengaturan NFC pada handphone Anda, untuk melanjutkan")
                    .setPositiveButton("Aktifkan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Batalkan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showNFCDisabledView();
                        }
                    }).show();
        }
    }

    private void showNFCDisabledView() {

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