package com.tokopedia.tkpd.qrscanner;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.content.res.AppCompatResources;
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
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.di.CampaignComponent;
import com.tokopedia.tkpd.campaign.di.DaggerCampaignComponent;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import rx.Observable;

@RuntimePermissions
public class QrScannerActivity extends BaseScannerQRActivity implements QrScannerContract.View,
        HasComponent<CampaignComponent> {

    public static final int RESULT_CODE_HOME = 1;
    private static final int REQUEST_CODE_NOMINAL = 211;
    private static final int REQUEST_CODE_LOGIN = 3;
    private static final int REQUEST_PAY_WITH_QR = 4;

    private static final String QR_DATA = "QR_DATA";
    private static final String IMEI = "IMEI";
    private static final String QR_RESPONSE = "QR_RESPONSE";

    private CampaignComponent campaignComponent;
    private boolean isTorchOn;
    private ProgressBar progressBar;
    private static String QR_NEED_RESULT = "qr_need_result";

    @Inject
    QrScannerPresenter presenter;

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_scanner_qr;
    }

    public static Intent newInstance(Context context, boolean needResult) {
        Intent intent = new Intent(context, QrScannerActivity.class);
        intent.putExtra(QR_NEED_RESULT, needResult);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = findViewById(R.id.progress_bar_scanner);
        QrScannerActivityPermissionsDispatcher.isCameraPermissionAvailableWithCheck(this);
    }

    @Override
    public Observable<InfoQrTokoCash> getInfoQrTokoCash(RequestParams requestParams) {
        return ((TokoCashRouter) getApplication()).getInfoQrTokoCashUseCase(requestParams);
    }

    @Override
    public Observable<BalanceTokoCash> getBalanceTokoCash() {
        return ((TokoCashRouter) getApplication()).getBalanceTokoCash();
    }

    @Override
    public void navigateToNominalActivityPage(String qrcode, InfoQrTokoCash infoQrTokoCash) {
        Intent intent = ((TokoCashRouter) getApplication()).getNominalActivityIntent(getApplicationContext(), qrcode, infoQrTokoCash);
        startActivityForResult(intent, REQUEST_CODE_NOMINAL);
    }

    @Override
    public void goToPaymentPage(String imeiNumber, JsonObject barcodeData) {
        UserSession session = new UserSession(this);
        if (session.isLoggedIn()) {
            SaveInstanceCacheManager cacheManager = new SaveInstanceCacheManager(this, true);
            cacheManager.put(QR_RESPONSE, barcodeData);
            Intent intent = ((TokoCashRouter) getApplication()).getOvoActivityIntent(getApplicationContext());
            intent.putExtra(QR_DATA, cacheManager.getId());
            intent.putExtra(IMEI, imeiNumber);
            startActivity(intent);
            finish();
        } else {
            moveToLoginPage(REQUEST_PAY_WITH_QR);
        }
    }

    private void moveToLoginPage(int requestCode) {
        startActivityForResult(LoginActivity.getCallingIntent(getApplicationContext()), requestCode);
    }

    @NeedsPermission({Manifest.permission.CAMERA})
    void isCameraPermissionAvailable() {

    }

    @OnPermissionDenied({Manifest.permission.CAMERA})
    void requestCameraPermissionDenied() {
        Toast.makeText(this, getResources().getString(R.string.error_actiivty_open_permission), Toast.LENGTH_LONG).show();
        finish();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA})
    void requestCameraPermissionNeverAsk() {
        Toast.makeText(this, getResources().getString(R.string.error_actiivty_open_permission), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        QrScannerActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected int getInflateViewId() {
        return R.layout.layout_scanner_qr;
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
        updateTitle(getString(R.string.title_scan_qr));

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
        if (getIntent().getBooleanExtra(QR_NEED_RESULT, false)) {
            Intent intent = new Intent();
            intent.putExtra("scanResult", barcodeResult.getText());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            decoratedBarcodeView.pause();
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
        builder.setTitle(getString(R.string.title_dialog_wrong_scan));
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.btn_dialog_wrong_scan),
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
    public CampaignComponent getComponent() {
        if (campaignComponent == null) initInjector();
        return campaignComponent;
    }

    private void initInjector() {
        campaignComponent = DaggerCampaignComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
        campaignComponent.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroyView();
        presenter.detachView();
    }

    @Override
    public void interruptToLoginPage() {
        startActivityForResult(LoginActivity.getCallingIntent(getApplicationContext()), REQUEST_CODE_LOGIN);
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
