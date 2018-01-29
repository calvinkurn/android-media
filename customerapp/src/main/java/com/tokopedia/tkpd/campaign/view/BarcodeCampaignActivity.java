package com.tokopedia.tkpd.campaign.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.di.CampaignComponent;
import com.tokopedia.tkpd.campaign.di.DaggerCampaignComponent;

import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class BarcodeCampaignActivity extends BasePresenterActivity<BarCodeScannerPresenter> implements BarCodeScannerContract.View {
    private DecoratedBarcodeView decoratedBarcodeView;
    private ToggleButton switchTorch;
    private ScannerLaserView scannerLaser;
    private boolean repeatUp = true;

    @Inject
    BarCodeScannerPresenter mPresenter;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        getComponent();
        presenter = mPresenter;
        presenter.attachView(this);
        BarcodeCampaignActivityPermissionsDispatcher.requestCameraPermissionWithCheck(this);
    }
    public static Intent getBarcodeCamapignIntent(Context context) {
        Intent i = new Intent(context,BarcodeCampaignActivity.class);
        return i;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_compaign_qr_scan;
    }
    @NeedsPermission({Manifest.permission.CAMERA})
    void requestCameraPermission() {

    }

    @OnPermissionDenied({Manifest.permission.CAMERA})
    void requestCameraPermissionDenied() {
        Toast.makeText(this,"Unable to open barcode scanner",Toast.LENGTH_LONG).show();
        finish();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA})
    void requestCameraPermissionNeverAsk() {
        Toast.makeText(this,"Unable to open barcode scanner",Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void initView() {
        decoratedBarcodeView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        switchTorch = (ToggleButton) findViewById(R.id.switch_flashlight);
        scannerLaser = (ScannerLaserView) findViewById(R.id.scanner_laser_view);

        final TranslateAnimation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, -1.0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 1.0f);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                mAnimation.setStartOffset(0);
                if (repeatUp) {
                    scannerLaser.setBackground(ContextCompat
                            .getDrawable(getApplicationContext(), R.drawable.digital_gradient_green_up));
                    repeatUp = false;
                } else {
                    scannerLaser.setBackground(ContextCompat
                            .getDrawable(getApplicationContext(), R.drawable.digital_gradient_green_down));
                    repeatUp = true;
                }
            }
        });
        mAnimation.setFillAfter(true);
        mAnimation.setDuration(1500);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        scannerLaser.setAnimation(mAnimation);

        decoratedBarcodeView.decodeContinuous(getBarcodeCallback());

        switchTorch.setVisibility(!hasFlash() ? View.GONE : View.VISIBLE);
    }

    private BarcodeCallback getBarcodeCallback() {
        return new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() == null) {
                    return;
                } else {
                    barCodeData = result.getText();
                        Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_LONG).show();
                    mPresenter.onBarCodeScanComplete();
                    decoratedBarcodeView.pause();
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        decoratedBarcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        decoratedBarcodeView.pause();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        toolbar.setTitle("Scan QR Code");
    }

    @Override
    protected void setActionVar() {
        decoratedBarcodeView.setTorchListener(getListener());
        switchTorch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    switchTorch.setChecked(true);
                    decoratedBarcodeView.setTorchOn();
                } else {
                    switchTorch.setChecked(false);
                    decoratedBarcodeView.setTorchOff();
                }
            }
        });
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private DecoratedBarcodeView.TorchListener getListener() {
        return new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                switchTorch.setChecked(true);
            }

            @Override
            public void onTorchOff() {
                switchTorch.setChecked(false);
            }
        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return decoratedBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    CampaignComponent campaignComponent;
    public CampaignComponent getComponent() {
        if (campaignComponent == null) initInjector();
        return campaignComponent;
    }

    private void initInjector() {

        CampaignComponent component = DaggerCampaignComponent.builder()
                .appComponent(getApplicationComponent())
                .build();
        component.inject(this);
    }

    String barCodeData;

    @Override
    public String getBarCodeData() {
        return barCodeData;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BarcodeCampaignActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }
}
