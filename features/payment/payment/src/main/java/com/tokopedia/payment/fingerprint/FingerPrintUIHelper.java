package com.tokopedia.payment.fingerprint;

import android.content.Context;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import com.tokopedia.payment.activity.TopPayActivity;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public class FingerPrintUIHelper extends FingerprintManagerCompat.AuthenticationCallback {

    private Context context;
    private String transactionId;
    private String ccHashed;
    private Stage stage;
    private Callback callback;

    public FingerPrintUIHelper(Context context, String transactionId, String ccHashed, Stage stage) {
        this.context = context;
        this.transactionId = transactionId;
        this.ccHashed = ccHashed;
        this.stage = stage;
    }

    public void startListening(Callback callback) {
        this.callback = callback;
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(context);
        if(fingerprintManagerCompat.isHardwareDetected() && fingerprintManagerCompat.hasEnrolledFingerprints()) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            fingerprintManagerCompat.authenticate(null, 0, cancellationSignal, this, null);
        }
    }

    public void stopListening(){

    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        super.onAuthenticationError(errMsgId, errString);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        super.onAuthenticationHelp(helpMsgId, helpString);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
    }

    public interface Callback{

    }

    public enum Stage {
        REGISTER,
        PAYMENT
    }
}
