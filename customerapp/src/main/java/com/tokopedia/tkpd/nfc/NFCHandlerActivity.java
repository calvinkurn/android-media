package com.tokopedia.tkpd.nfc;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConsInternalDigital;
import com.tokopedia.common_digital.common.constant.DigitalExtraParam;

public class NFCHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            Intent newIntent = RouteManager.getIntent(this, ApplinkConsInternalDigital.INTERNAL_SMARTCARD, DigitalExtraParam.EXTRA_NFC);
            newIntent.putExtras(intent.getExtras());
            newIntent.setAction(intent.getAction());
            startActivity(newIntent);
            finish();
        }
    }
}

