package com.tokopedia.payment.fingerprint.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tokopedia.fingerprint.view.FingerPrintDialog;
import com.tokopedia.payment.R;
import com.tokopedia.payment.fingerprint.util.FingerprintConstant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public class FingerPrintUIHelper implements FingerPrintDialog.Callback {

    private static final int MAX_ERROR = 3;

    private final AppCompatActivity activity;
    private final String transactionId;
    private final String userId;
    private Callback callback;
    private FingerPrintDialogPayment fingerPrintDialogPayment;
    private final String partner;
    private String urlOtp;
    private String date;
    private int counterError = 0;

    public FingerPrintUIHelper(AppCompatActivity activity, String transactionId, String userId, String partner, String urlOtp) {
        this.activity = activity;
        this.transactionId = transactionId;
        this.userId = userId;
        this.partner = partner;
        this.urlOtp = urlOtp;
        generateDate();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void startListening(final Callback callback) {
        this.callback = callback;
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(activity);
        if (fingerprintManagerCompat.isHardwareDetected() && fingerprintManagerCompat.hasEnrolledFingerprints()) {
            if (fingerPrintDialogPayment == null) {
                fingerPrintDialogPayment = new FingerPrintDialogPayment();
                fingerPrintDialogPayment.setTextToEncrypt(userId + date);
                fingerPrintDialogPayment.setCallback(this);
                fingerPrintDialogPayment.setContext(activity);
                fingerPrintDialogPayment.setClickListenerButtonOtp(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callback.onGoToOtpPage(urlOtp);
                    }
                });
                fingerPrintDialogPayment.show(activity.getSupportFragmentManager(), FingerprintConstant.TAG_FINGERPRINT_DIALOG);
            }
            fingerPrintDialogPayment.startListening();
        }
    }

    private void generateDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        date = dateFormat.format(calendar.getTime());
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        updateCounterError();
    }

    private boolean updateCounterError() {
        counterError++;
        fingerPrintDialogPayment.updateTitle(activity.getString(R.string.fingerprint_label_failed_scan));
        fingerPrintDialogPayment.setVisibilityContainer(true);
        if (counterError > MAX_ERROR) {
            closeBottomSheet();
            callback.onGoToOtpPage(urlOtp);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {

    }

    public void onAuthenticationSucceeded(String publicKey, String signature) {
        callback.onPaymentFingerPrint(transactionId, partner, publicKey, date,
                signature, userId);
    }

    @Override
    public void onAuthenticationFailed() {
        updateCounterError();
    }

    public void onErrorNetworkPaymentFingerPrint() {
        if (updateCounterError()) {
            startListening(callback);
        }
    }

    public void closeBottomSheet() {
        fingerPrintDialogPayment.dismiss();
    }

    public void stopListening() {
        fingerPrintDialogPayment.stopListening();
    }

    public interface Callback {
        void onPaymentFingerPrint(String transactionId, String partner, String publicKey, String date, String accountSignature, String userId);

        void onGoToOtpPage(String urlOtp);
    }
}
