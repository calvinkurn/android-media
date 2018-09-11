package com.tokopedia.events.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.events.R;
import com.tokopedia.events.ScanQrCodeRouter;

public class ScanQRCodeActivity extends TActivity {

    View divider1, divider2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        gotoQrScannerPage();
        setContentView(R.layout.activity_scan_ticket_result);

        divider1 = findViewById(R.id.divider1);
        divider2 = findViewById(R.id.divider2);

        divider1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        divider2.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }


    public void gotoQrScannerPage() {
        startActivity(((ScanQrCodeRouter) this.getApplicationContext())
                .gotoQrScannerPage());
        finish();
    }
}
