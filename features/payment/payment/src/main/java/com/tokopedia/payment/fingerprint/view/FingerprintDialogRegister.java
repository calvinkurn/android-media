package com.tokopedia.payment.fingerprint.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.fingerprint.view.FingerPrintDialog;
import com.tokopedia.payment.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by zulfikarrahman on 4/5/18.
 */

public class FingerprintDialogRegister extends FingerPrintDialog implements FingerPrintDialog.Callback {

    private static final int MAX_ERROR = 3;
    public static final String USER_ID = "USER_ID";
    public static final String TRANSACTION_ID = "TRANSACTION_ID";
    public static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ";

    private String userId;
    private String transactionId;
    private int counterError = 0;

    private ListenerRegister listenerRegister;
    private String date;

    public static FingerprintDialogRegister createInstance(String userId, String transactionId) {
        FingerprintDialogRegister fingerprintDialogRegister = new FingerprintDialogRegister();
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, userId);
        bundle.putString(TRANSACTION_ID, transactionId);
        fingerprintDialogRegister.setArguments(bundle);
        return fingerprintDialogRegister;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString(USER_ID, "");
        transactionId = getArguments().getString(TRANSACTION_ID, "");
    }

    public void setListenerRegister(ListenerRegister listenerRegister) {
        this.listenerRegister = listenerRegister;
    }

    @Override
    public String getTextToEncrypt() {
        date = generateDate();
        return userId + date;
    }

    @Override
    public Callback getCallback() {
        return this;
    }

    private String generateDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                DATE_FORMAT, Locale.ENGLISH);
        return dateFormat.format(calendar.getTime());
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        updateDesc(getString(R.string.fingerprint_label_register_fingerprint));
    }

    private boolean updateCounterError() {
        if (isResumed()) {
            counterError++;
            updateDesc(getString(R.string.fingerprint_label_desc_default));
            updateTitle(getString(R.string.fingerprint_label_try_again));
            if (counterError > MAX_ERROR) {
                stopListening();
                listenerRegister.showErrorRegisterSnackbar();
                dismiss();
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        updateCounterError();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        updateCounterError();
    }

    @Override
    public void onAuthenticationSucceeded(String publicKey, String signature) {
        listenerRegister.onRegisterFingerPrint(transactionId, publicKey, date, signature, userId);
    }

    @Override
    public void onAuthenticationFailed() {
        updateCounterError();
    }

    public void onErrorRegisterFingerPrint() {
        if (updateCounterError()) {
            startListening();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public interface ListenerRegister {
        void onRegisterFingerPrint(String transactionId, String publicKey, String date, String signature, String userId);

        void showErrorRegisterSnackbar();
    }
}
