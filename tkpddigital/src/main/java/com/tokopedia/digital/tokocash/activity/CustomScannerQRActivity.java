package com.tokopedia.digital.tokocash.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.digital.R;

import java.util.List;

/**
 * Created by nabillasabbaha on 12/14/17.
 */

public class CustomScannerQRActivity extends BasePresenterActivity {

    private DecoratedBarcodeView decoratedBarcodeView;
    private ToggleButton switchTorch;


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
                        Intent intent = new Intent();
                        intent.putExtra("result", result.getText());
                        setResult(Activity.RESULT_OK);
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
