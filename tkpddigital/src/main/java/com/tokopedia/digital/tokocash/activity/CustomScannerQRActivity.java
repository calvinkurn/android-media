package com.tokopedia.digital.tokocash.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.tokopedia.digital.R;
import com.tokopedia.digital.tokocash.compoundview.ScannerLaserView;

import java.util.List;

/**
 * Created by nabillasabbaha on 12/14/17.
 */

public class CustomScannerQRActivity extends BasePresenterActivity {

    private DecoratedBarcodeView decoratedBarcodeView;
    private ToggleButton switchTorch;
    private ScannerLaserView scannerLaser;
    private boolean repeatUp = true;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_scanner_qr;
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
                    if (!result.getText().equals("http://www.tokopedia.com")) {
                        Toast.makeText(getApplicationContext(), "Scan salah", Toast.LENGTH_LONG).show();
                    } else {
                        decoratedBarcodeView.setStatusText(result.getText());
                        startActivity(new Intent(getApplicationContext(), SuccessPaymentQRActivity.class));
//                        Intent intent = new Intent();
//                        intent.putExtra("result", result.getText());
//                        setResult(Activity.RESULT_OK);
                        finish();
                    }
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
}
