package com.tokopedia.tkpd.nfc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;

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

    private static final String TAG_EMONEY_TIME_CHECK_LOGIC = "EMONEY_TIME_CHECK_LOGIC";
    private static final String TAG_EMONEY_DEBUG = "EMONEY_DEBUG";
    private static final int DIVIDER = 1000000;

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

            String durationString = getDuration(startTime);
            newIntent.putExtra(TAG_EMONEY_TIME_CHECK_LOGIC, durationString);
            sendLog(durationString);

            startActivity(newIntent);
            finish();
        }
    }

    private String getDuration(long startTime) {
        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/DIVIDER;
        return ""+duration+" ms";
    }

    private void sendLog(String durationString) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put(TAG_EMONEY_TIME_CHECK_LOGIC, durationString);
        ServerLogger.log(Priority.P2, TAG_EMONEY_DEBUG, messageMap);
    }
}

