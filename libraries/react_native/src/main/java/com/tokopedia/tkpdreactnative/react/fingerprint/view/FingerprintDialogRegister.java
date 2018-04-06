package com.tokopedia.tkpdreactnative.react.fingerprint.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.fingerprint.view.FingerPrintDialog;
import com.tokopedia.tkpdreactnative.R;

/**
 * Created by zulfikarrahman on 4/5/18.
 */

public class FingerprintDialogRegister extends FingerPrintDialog {

    private EventListener eventListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventListener.onCreate();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        updateDesc(getString(R.string.fingerprint_label_register_fingerprint));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        eventListener.onDismissDialog();
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public interface EventListener {
        void onDismissDialog();

        void onCreate();
    }
}
