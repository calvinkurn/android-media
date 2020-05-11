package com.tokopedia.digital.product.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.receiver.USSDBroadcastReceiver;

import java.util.Collections;
import java.util.List;

public class USSDAccessibilityService extends AccessibilityService {
    private final String TAG = USSDAccessibilityService.class.getSimpleName();
    public static final String KEY_START_SERVICE_FROM_APP = "STARTFROMAPP";
    private boolean startFromApp;
    private static final int MIN_MESSAGE_LENGTH = 15;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null)
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
        if (event == null) {
            return;
        }
        AccessibilityNodeInfo source = event.getSource();
        if (source == null) {
            return;
        }
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
        if (eventText == null)
            return;

        String result = processUSSDText(eventText);
        if (TextUtils.isEmpty(result))
            return;
        sendResultToBroadcast(result);
        closeSystemDialog(source);

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
        setInfo();
    }

    private String processUSSDText(List<CharSequence> eventText) {
        for (CharSequence s : eventText) {
            if (s.length() >= MIN_MESSAGE_LENGTH) {
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
        if (source == null) {
            return;
        }
        boolean isClosed = checkAndClosed(source,getString(R.string.label_ussd_cancel));

        if (!isClosed) {
            isClosed = checkAndClosed(source,getString(R.string.ussd_batal_label));
        }

        if (!isClosed) {
            isClosed = checkAndClosed(source,getString(R.string.ok));
        }

        //this block is for in case system language is Indonesia then button text may be "Batal"
        if (!isClosed) {
            for (int i = 0; i < source.getChildCount(); i++) {
                AccessibilityNodeInfo node = source.getChild(i);
                if (node != null && node.getText() != null) {
                    if (getString(R.string.label_ussd_cancel).equalsIgnoreCase(node.getText().toString()) ||
                            getString(R.string.ok).equalsIgnoreCase(node.getText().toString()) ||
                            getString(R.string.ussd_batal_label).equalsIgnoreCase(node.getText().toString())) {
                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        isClosed = true;
                        break;
                    }
                }
            }
        }
        if (!isClosed && startFromApp && android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
            performGlobalAction(GLOBAL_ACTION_BACK); // This works on 4.1+ only
    }

    private boolean checkAndClosed(AccessibilityNodeInfo source,String btnText) {
        List<AccessibilityNodeInfo> list = source
                .findAccessibilityNodeInfosByText(btnText);
        boolean isClosed = false;
        if (list != null) {
            for (AccessibilityNodeInfo node : list) {
                if (node != null) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    isClosed = true;
                }
            }
        }
        return isClosed;
    }
}