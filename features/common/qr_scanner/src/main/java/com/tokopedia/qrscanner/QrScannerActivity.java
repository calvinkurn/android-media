package com.tokopedia.qrscanner;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.utils.permission.PermissionCheckerHelper;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSession;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

public class QrScannerActivity extends BaseScannerQRActivity implements QrScannerContract.View,
        HasComponent<QRComponent> {

    public static final int RESULT_CODE_HOME = 1;
    private static final int REQUEST_CODE_NOMINAL = 211;
    private static final int REQUEST_CODE_LOGIN = 3;
    private static final int REQUEST_PAY_WITH_QR = 4;
    private static final String PARAM_NEED_RESULT = "1";

    private static final String QR_DATA = "QR_DATA";
    private static final String IMEI = "IMEI";
    private static final String QR_RESPONSE = "QR_RESPONSE";
    @Inject
    QrScannerPresenter presenter;
    private QRComponent qrComponent;
    private boolean isTorchOn;
    private ProgressBar progressBar;
    private PermissionCheckerHelper permissionCheckerHelper;
    private boolean mNeedResult = false;

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.qr_scanner_layout_scanner_qr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        if (uri != null) {
            List<String> paths = UriUtil.destructureUri(ApplinkConstInternalMarketplace.QR_SCANNEER, uri);
            if (!paths.isEmpty()) {
                String param = paths.get(0);
                mNeedResult = param.equals(PARAM_NEED_RESULT);
            }
        }

        progressBar = findViewById(R.id.progress_bar_scanner);
        permissionCheckerHelper = new PermissionCheckerHelper();
        permissionCheckerHelper.checkPermission(
                this,
                Manifest.permission.CAMERA,
                new PermissionCheckerHelper.PermissionCheckListener() {
                    @Override
                    public void onPermissionDenied(@NotNull String permissionText) {
                        requestCameraPermissionDenied();
                    }

                    @Override
                    public void onNeverAskAgain(@NotNull String permissionText) {
                        requestCameraPermissionNeverAsk();
                    }

                    @Override
                    public void onPermissionGranted() {

                    }
                },
                ""
        );
    }

    @Override
    public void goToPaymentPage(String imeiNumber, JsonObject barcodeData) {
        UserSession session = new UserSession(this);
        if (session.isLoggedIn()) {
            SaveInstanceCacheManager cacheManager = new SaveInstanceCacheManager(this, true);
            cacheManager.put(QR_RESPONSE, barcodeData);
            Intent intent = RouteManager.getIntent(getActivity(),
                    ApplinkConstInternalGlobal.OVO_PAY_WITH_QR_ENTRY);
            intent.putExtra(QR_DATA, cacheManager.getId());
            intent.putExtra(IMEI, imeiNumber);
            startActivity(intent);
            finish();
        } else {
            moveToLoginPage(REQUEST_PAY_WITH_QR);
        }
    }

    @Override
    public boolean getRemoteConfigForOvoPay() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(getApplicationContext());
        return remoteConfig.getBoolean(RemoteConfigKey.OVO_ENABLE_FLAG, false);
    }

    private void moveToLoginPage(int requestCode) {
        startActivityForResult(RouteManager.getIntent(getActivity(), ApplinkConst.LOGIN), requestCode);
    }

    void requestCameraPermissionDenied() {
        Toast.makeText(this, getResources().getString(R.string.qr_scanner_error_actiivty_open_permission), Toast.LENGTH_LONG).show();
        finish();
    }

    void requestCameraPermissionNeverAsk() {
        Toast.makeText(this, getResources().getString(R.string.qr_scanner_error_actiivty_open_permission), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionCheckerHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected int getInflateViewId() {
        return R.layout.qr_scanner_layout_scanner_qr;
    }

    @Override
    protected int getIdDecoratedBarcodeView() {
        return R.id.zxing_barcode_scanner;
    }

    @Override
    protected int getIdScannerLaser() {
        return R.id.scanner_laser;
    }

    @Override
    protected int getColorUpScannerLaser() {
        return R.drawable.qr_gradient_green_up;
    }

    @Override
    protected int getColorDownScannerLaser() {
        return R.drawable.qr_gradient_green_down;
    }

    @Override
    protected void initView() {
        initInjector();
        presenter.attachView(this);
        updateTitle(getString(R.string.qr_scanner_title_scan_qr));

        final ImageView torch = (ImageView) findViewById(R.id.switch_flashlight);
        torch.setVisibility(!hasFlash() ? View.GONE : View.VISIBLE);
        decoratedBarcodeView.setTorchListener(getListener());
        torch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTorchOn) {
                    isTorchOn = false;
                    decoratedBarcodeView.setTorchOff();
                } else {
                    isTorchOn = true;
                    decoratedBarcodeView.setTorchOn();
                }
                torch.setImageDrawable(iconQr(isTorchOn));
            }
        });
    }

    private Drawable iconQr(boolean isTorchOn) {
        return (!isTorchOn) ?
                AppCompatResources.getDrawable(this, R.drawable.qr_ic_flash_turn_on) :
                AppCompatResources.getDrawable(this, R.drawable.qr_ic_flash_turn_off);
    }

    @Override
    protected void findResult(BarcodeResult barcodeResult) {
        if (mNeedResult) {
            Intent intent = new Intent();
            intent.putExtra("scanResult", barcodeResult.getText());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            decoratedBarcodeView.pauseAndWait();
            hideAnimation();
            presenter.onBarCodeScanComplete(barcodeResult.getText());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        decoratedBarcodeView.resume();
        animateScannerLaser();
    }


    private DecoratedBarcodeView.TorchListener getListener() {
        return new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                isTorchOn = true;
            }

            @Override
            public void onTorchOff() {
                isTorchOn = false;
            }
        };
    }

    @Override
    public void showProgressDialog() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressDialog() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showErrorGetInfo(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.qr_scanner_title_dialog_wrong_scan));
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.qr_scanner_btn_dialog_wrong_scan),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        decoratedBarcodeView.resume();
                        animateScannerLaser();
                    }
                }).create().show();
    }

    @Override
    public void showErrorNetwork(Throwable throwable) {
        String message = ErrorHandler.getErrorMessage(getApplicationContext(), throwable);
        NetworkErrorHelper.createSnackbarWithAction(this, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        decoratedBarcodeView.resume();
                        animateScannerLaser();
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void goToEventRedeemPage(String url){
        Intent intent = RouteManager.getIntent(this, ApplinkConstInternalEntertainment.EVENT_REDEEM);
        intent.putExtra("EXTRA_URL_REDEEM", url);
        startActivity(intent);
    }

    @Override
    public void goToLoginByQr(String data) {
        Intent intent = RouteManager.getIntent(this, ApplinkConstInternalGlobal.QR_LOGIN);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, data);
        startActivity(intent);
    }

    @Override
    public QRComponent getComponent() {
        if (qrComponent == null) initInjector();
        return qrComponent;
    }

    private void initInjector() {
        qrComponent = DaggerQRComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
        qrComponent.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroyView();
        presenter.detachView();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_NOMINAL && resultCode == RESULT_CODE_HOME)
                || (resultCode == RESULT_OK && requestCode == REQUEST_PAY_WITH_QR)) {
            finish();
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_LOGIN) {
            decoratedBarcodeView.pause();
            hideAnimation();
            presenter.onScanCompleteAfterLoginQrPayment();
        }
    }
}
