package com.tokopedia.payment.fingerprint.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.tokopedia.fingerprint.view.FingerPrintDialog;
import com.tokopedia.payment.R;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by zulfikarrahman on 4/5/18.
 */

public class FingerprintDialogRegister extends FingerPrintDialog implements FingerPrintDialog.Callback {

    private static final int MAX_ERROR = 3;
    public static final String USER_ID = "USER_ID";
    public static final String TRANSACTION_ID = "TRANSACTION_ID";

    private String userId;
    private String transactionId;
    private int counterError = 0;

    private ListenerRegister listenerRegister;
    private boolean isAttached;
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
    public void startListening() {
        super.startListening();
        date = generateDate();
        setTextToEncrypt(userId + date);
    }

    @Override
    public Callback getCallback() {
        return this;
    }

    private String generateDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss ZZZ", Locale.ENGLISH);
        return dateFormat.format(calendar.getTime());
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        updateDesc(getString(R.string.fingerprint_label_register_fingerprint));
    }

    private boolean updateCounterError() {
        if (isAttached) {
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
    public void onAttach(Context context) {
        super.onAttach(context);
        isAttached = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        isAttached = false;
        super.onDismiss(dialog);
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

    public interface ListenerRegister {
        void onRegisterFingerPrint(String transactionId, String publicKey, String date, String signature, String userId);

        void showErrorRegisterSnackbar();
    }
}
