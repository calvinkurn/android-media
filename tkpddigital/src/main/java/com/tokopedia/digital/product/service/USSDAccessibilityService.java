package com.tokopedia.digital.product.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.tokopedia.digital.product.receiver.USSDBroadcastReceiver;

import java.util.Collections;
import java.util.List;

public class USSDAccessibilityService extends AccessibilityService {
    private final String TAG = USSDAccessibilityService.class.getSimpleName();
    public static final String KEY_START_SERVICE_FROM_APP = "STARTFROMAPP";
    private boolean startFromApp = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startFromApp = intent.getBooleanExtra(KEY_START_SERVICE_FROM_APP, false);
        if (startFromApp)
            setInfo();
        else {
            AccessibilityServiceInfo info = new AccessibilityServiceInfo();
            setServiceInfo(info);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent" + event.getPackageName());

        AccessibilityNodeInfo source = event.getSource();
    /* if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && !event.getClassName().equals("android.app.AlertDialog")) { // android.app.AlertDialog is the standard but not for all phones  */
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && !String.valueOf(event.getClassName()).contains("AlertDialog")) {
            return;
        }
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && (source == null || !source.getClassName().equals("android.widget.TextView"))) {
            return;
        }
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && TextUtils.isEmpty(source.getText())) {
            return;
        }

        List<CharSequence> eventText;

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            eventText = event.getText();
        } else {
            eventText = Collections.singletonList(source.getText());
        }

        String result = processUSSDText(eventText);
        //  String result = fetchResponse(source);

        if (TextUtils.isEmpty(result)) return;
        Log.d(TAG, result);
        sendResultToBroadcast(result);
        closeSystemDialog(source);
        // fetchResponse(source);

//
        Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(closeDialog);

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        setServiceInfo(info);
        startFromApp = false;

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");
        setInfo();
    }

    private String processUSSDText(List<CharSequence> eventText) {
        for (CharSequence s : eventText) {
            if (s.length() >= 15) {
                String text = String.valueOf(s);
                return text;
            }

        }

        return null;
    }

    private void sendResultToBroadcast(String result) {
        Intent intent = new Intent(USSDBroadcastReceiver.ACTION_GET_BALANCE_FROM_USSD);
        if (result != null)
            intent.putExtra(USSDBroadcastReceiver.EXTRA_RESULT_USSD_DATA, result);
        sendBroadcast(intent);

    }

    public void setInfo() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.packageNames = new String[]{"com.android.phone"};
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

    private void closeSystemDialog(AccessibilityNodeInfo source) {
        List<AccessibilityNodeInfo> list = source
                .findAccessibilityNodeInfosByText("Cancel");
        boolean isClosed = false;
        for (AccessibilityNodeInfo node : list) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            isClosed = true;

        }
        if (!isClosed) {
            list = source
                    .findAccessibilityNodeInfosByText("OK");
            for (AccessibilityNodeInfo node : list) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                isClosed = true;
            }
        }
        if (!isClosed && startFromApp && android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
            performGlobalAction(GLOBAL_ACTION_BACK); // This works on 4.1+ only


    }
}