package com.tokopedia.tkpd.qrscanner;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

import java.util.List;

/**
 * Created by nabillasabbaha on 12/29/17.
 */

public abstract class BaseScannerQRActivity extends BaseSimpleActivity {

    protected DecoratedBarcodeView decoratedBarcodeView;
    protected View scannerLaser;
    private boolean repeatUp = true;
    private TranslateAnimation mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decoratedBarcodeView = (DecoratedBarcodeView) findViewById(getIdDecoratedBarcodeView());
        scannerLaser = (View) findViewById(getIdScannerLaser());
        initView();

        animateScannerLaser();
        decoratedBarcodeView.decodeContinuous(getBarcodeCallback());
    }

    @Override
    protected int getLayoutRes() {
        return getInflateViewId();
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

    /**
     * @return int to inflate scanner view
     */
    protected abstract int getInflateViewId();

    /**
     * @return int to bind view DecoratedBarcodeView
     */
    protected abstract int getIdDecoratedBarcodeView();

    /**
     * @return int to bind view Scanner Laser
     */
    protected abstract int getIdScannerLaser();

    /**
     * to bind additional view
     */
    protected abstract void initView();

    /**
     * @param barcodeResult is result from scanning QR Code
     */
    protected abstract void findResult(BarcodeResult barcodeResult);

    /**
     * @return int to give color when scanner laser going up
     */
    protected abstract int getColorUpScannerLaser();

    /**
     * @return int to give color when scanner laser going down
     */
    protected abstract int getColorDownScannerLaser();

    /**
     * this method for animating scanner lasser, for customize color up and down
     * please use getColorUpScannerLaser() and getColorDownScannerLaser()
     */
    protected void animateScannerLaser() {
        if (mAnimation == null) {
            mAnimation = new TranslateAnimation(
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
                                .getDrawable(getApplicationContext(), getColorUpScannerLaser()));
                        repeatUp = false;
                    } else {
                        scannerLaser.setBackground(ContextCompat
                                .getDrawable(getApplicationContext(), getColorDownScannerLaser()));
                        repeatUp = true;
                    }
                }
            });
            mAnimation.setFillAfter(true);
            mAnimation.setDuration(1500);
            mAnimation.setRepeatCount(-1);
            mAnimation.setRepeatMode(Animation.REVERSE);
            mAnimation.setInterpolator(new LinearInterpolator());
        }
        scannerLaser.startAnimation(mAnimation);
    }

    protected void hideAnimation() {
        if (scannerLaser.getAnimation() != null) {
            scannerLaser.clearAnimation();
        }
    }

    /**
     * @return for checking when device have flash or not
     */
    protected boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    /**
     * @return for getting result when scanner finish
     */
    private BarcodeCallback getBarcodeCallback() {
        return new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() == null) {
                    return;
                } else {
                    findResult(result);
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return decoratedBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}