package com.tokopedia.payment.fingerprint.view;

import android.app.Activity;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.tokopedia.payment.R;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public class FingerPrintUIHelper extends FingerprintManagerCompat.AuthenticationCallback {

    public static final String TAG_FINGERPRINT_DIALOG = "fingerprint_dialog";
    private Activity activity;
    private String transactionId;
    private String ccHashed;
    private Stage stage;
    private String userId;
    private Callback callback;
    private FingerPrintDialogRegister fingerPrintDialogRegister;
    private FingerPrintDialogPayment fingerPrintDialogPayment;
    private String partner;
    private CancellationSignal cancellationSignal;

    public FingerPrintUIHelper(Activity activity, String transactionId, String ccHashed, Stage stage, String userId, String partner) {
        this.activity = activity;
        this.transactionId = transactionId;
        this.ccHashed = ccHashed;
        this.stage = stage;
        this.userId = userId;
        this.partner = partner;
    }

    public void startListening(Callback callback) {
        this.callback = callback;
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(activity);
        if (fingerprintManagerCompat.isHardwareDetected() && fingerprintManagerCompat.hasEnrolledFingerprints()) {
            fingerPrintDialogRegister = new FingerPrintDialogRegister();
            fingerPrintDialogRegister.show(((AppCompatActivity) activity).getSupportFragmentManager(), TAG_FINGERPRINT_DIALOG);
            cancellationSignal = new CancellationSignal();
            fingerprintManagerCompat.authenticate(null, 0, cancellationSignal, this, null);
        }
    }

    public void stopListening() {
        if(cancellationSignal != null){
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        super.onAuthenticationError(errMsgId, errString);
        stopListening();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        super.onAuthenticationHelp(helpMsgId, helpString);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = dateFormat.format(calendar.getTime());
        if (stage == Stage.REGISTER) {
            callback.onRegisterFingerPrint(transactionId, getPublicKey(), date, getAccountSignature(userId, date, getPublicKey(), generatePublicKey()), userId);
        } else if (stage == Stage.PAYMENT) {
            callback.onPaymentFingerPrint(transactionId, partner, getPublicKey(), date,
                    getAccountSignature(userId, date, getPublicKey(), generatePublicKey()), userId);
        }
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
    }


    private PublicKey generatePublicKey() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
                KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder("fingerprint",
                        KeyProperties.PURPOSE_ENCRYPT)
                        .setDigests(KeyProperties.DIGEST_SHA1);
                keyPairGenerator.initialize(builder.build());
                return keyPairGenerator.generateKeyPair().getPublic();
            } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    private String getAccountSignature(String userId, String date, String publicKey, PublicKey key) {
        try {
            Cipher cipher = Cipher.getInstance(key.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedSignature = cipher.doFinal((userId + date).getBytes());
            return Base64.encodeToString(encryptedSignature, 0);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getPublicKey() {
        return Base64.encodeToString(generatePublicKey().getEncoded(), 0);
    }

    public void onErrorPaymentFingerPrint() {
        fingerPrintDialogRegister.updateTitle(activity.getString(R.string.fingerprint_label_failed_scan));
    }

    public void closeBottomSheet() {
        fingerPrintDialogRegister.dismiss();
    }

    public interface Callback {

        void onRegisterFingerPrint(String transactionId, String publicKey, String date, String accountSignature, String userId);

        void onPaymentFingerPrint(String transactionId, String partner, String publicKey, String date, String accountSignature, String userId);
    }

    public enum Stage {
        REGISTER,
        PAYMENT
    }
}
