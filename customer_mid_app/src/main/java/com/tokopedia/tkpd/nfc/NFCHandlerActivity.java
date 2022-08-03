package com.tokopedia.tkpd.nfc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConsInternalDigital;
import com.tokopedia.common_digital.common.constant.DigitalExtraParam;
import com.tokopedia.common_electronic_money.util.CardUtils;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;

import java.util.HashMap;
import java.util.Map;

public class NFCHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        long startTime = System.nanoTime();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            Intent newIntent;
            if (CardUtils.isEmoneyCard(intent) || CardUtils.isTapcashCard(intent)) {
                newIntent = RouteManager.getIntent(this, ApplinkConsInternalDigital.INTERNAL_SMARTCARD_EMONEY, DigitalExtraParam.EXTRA_NFC);
            } else if ((CardUtils.isBrizziCard(intent))) {
                newIntent = RouteManager.getIntent(this, ApplinkConsInternalDigital.INTERNAL_SMARTCARD_BRIZZI, DigitalExtraParam.EXTRA_NFC);
            } else {
                newIntent = RouteManager.getIntent(this, ApplinkConsInternalDigital.INTERNAL_SMARTCARD_EMONEY, DigitalExtraParam.EXTRA_NFC);
            }
            newIntent.replaceExtras(intent.getExtras());
            newIntent.setAction(intent.getAction());

            long endTime = System.nanoTime();
            long duration = (endTime - startTime)/1000000;
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("duration", ""+duration);
            ServerLogger.log(Priority.P2, "EMONEY_TIME_CHECK_LOGIC", messageMap);
            Log.d("EMONEY_TIME_CHECK_LOGIC", ""+duration+" ms");

            startActivity(newIntent);
            finish();
        }
    }
}

