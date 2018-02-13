package com.tokopedia.tkpd.qrscanner;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.di.CampaignComponent;
import com.tokopedia.tkpd.campaign.di.DaggerCampaignComponent;
import com.tokopedia.tokocash.qrpayment.domain.GetInfoQrTokoCashUseCase;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class QrScannerActivity extends BaseScannerQRActivity implements QrScannerContract.View,
        HasComponent<CampaignComponent> {

    public static final int RESULT_CODE_HOME = 1;
    public static final int RESULT_CODE_SCANNER = 2;
    private static final int REQUEST_CODE_NOMINAL = 211;
    private static final int REQUEST_CODE_LOGIN = 3;

    private CampaignComponent campaignComponent;
    private boolean isTorchOn;
    private TkpdProgressDialog progressDialog;

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

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, QrScannerActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QrScannerActivityPermissionsDispatcher.isCameraPermissionAvailableWithCheck(this);
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
        return R.drawable.digital_gradient_green_up;
    }

    @Override
    protected int getColorDownScannerLaser() {
        return R.drawable.digital_gradient_green_down;
    }

    @Override
    protected void initView() {
        initInjector();
        presenter.attachView(this);
        updateTitle(getString(R.string.title_scan_qr));

        final ImageView torch = (ImageView) findViewById(com.tokopedia.tokocash.R.id.switch_flashlight);
        torch.setVisibility(!hasFlash() ? View.GONE : View.VISIBLE);
        decoratedBarcodeView.setTorchListener(getListener());
        torch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTorchOn) {
                    isTorchOn = false;
                    decoratedBarcodeView.setTorchOff();
                    torch.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            com.tokopedia.tokocash.R.drawable.ic_flash_turn_on));
                } else {
                    isTorchOn = true;
                    decoratedBarcodeView.setTorchOn();
                    torch.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            com.tokopedia.tokocash.R.drawable.ic_flash_turn_off));
                }
            }
        });
    }

    @Override
    protected void findResult(BarcodeResult barcodeResult) {
        decoratedBarcodeView.pause();
        hideAnimation();
        presenter.onBarCodeScanComplete(barcodeResult.getText());
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
        if (progressDialog != null) {
            progressDialog.showDialog();
        } else {
            progressDialog = new TkpdProgressDialog(getApplicationContext(), TkpdProgressDialog.NORMAL_PROGRESS);
        }
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void showErrorGetInfo(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(com.tokopedia.tokocash.R.string.title_dialog_wrong_scan));
        builder.setMessage(message);
        builder.setPositiveButton(getString(com.tokopedia.tokocash.R.string.btn_dialog_wrong_scan),
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
    public void showErrorNetwork(String message) {
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
    public int getRequestCodeForQrPayment() {
        return REQUEST_CODE_NOMINAL;
    }

    @Override
    public void interruptToLoginPage() {
        startActivityForResult(LoginActivity.getCallingIntent(getApplicationContext()), REQUEST_CODE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_NOMINAL && resultCode == RESULT_CODE_HOME) {
            finish();
        } else if (requestCode == REQUEST_CODE_LOGIN) {
            LocalCacheHandler localCacheHandler = new LocalCacheHandler(getApplicationContext(), GetInfoQrTokoCashUseCase.IDENTIFIER);
            if (resultCode == RESULT_OK && presenter.isUserLogin()) {
                String qrCode = localCacheHandler.getString(GetInfoQrTokoCashUseCase.IDENTIFIER);
                presenter.onBarCodeScanComplete(qrCode);
            } else {
                localCacheHandler.putString(GetInfoQrTokoCashUseCase.IDENTIFIER, "");
            }
        }
    }
}
