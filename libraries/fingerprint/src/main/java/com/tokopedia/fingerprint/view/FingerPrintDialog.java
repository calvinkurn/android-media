package com.tokopedia.fingerprint.view;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.fingerprint.R;
import com.tokopedia.fingerprint.util.FingerprintConstant;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;


/**
 * Created by zulfikarrahman on 3/26/18.
 */

@TargetApi(Build.VERSION_CODES.M)
public class FingerPrintDialog extends BottomSheets {

    private TextView descFingerprint;
    private String textToEncrypt = "";
    private FingerprintManager.CryptoObject cryptoObject;
    private Signature signature;
    private KeyStore keyStore;
    private CancellationSignal cancellationSignal;
    private Callback callback;
    private Context context;
    private TextView textViewTitle;

    @Override
    public int getLayoutResourceId() {
        return R.layout.bottom_sheet_fingerprint_view;
    }

    public void setTextToEncrypt(String textToEncrypt) {
        this.textToEncrypt = textToEncrypt;
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public Callback getCallback(){
        return callback;
    }

    @Override
    protected void configView(View parentView) {
        super.configView(parentView);
        textViewTitle = parentView.findViewById(com.tokopedia.design.R.id.tv_title);
    }

    @Override
    public void initView(View view) {
        descFingerprint = view.findViewById(R.id.desc_fingerprint);
    }

    @Override
    protected String title() {
        return getString(R.string.fingerprint_label_scan_your_fingerprint);
    }

    public void updateTitle(String title) {
        textViewTitle.setText(title);
    }

    public void updateDesc(String title){
        descFingerprint.setText(title);
    }

    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initKeyStore();
        initCryptoObject();
        return super.onCreateDialog(savedInstanceState);
    }

    public void startListening() {
        FingerprintManager fingerprintManager = context.getSystemService(FingerprintManager.class);
        cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, getAuthenticationCallback(), null);
    }

    private FingerprintManager.AuthenticationCallback getAuthenticationCallback() {
        return new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                getCallback().onAuthenticationError(errMsgId, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                getCallback().onAuthenticationHelp(helpMsgId, helpString);
            }


            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                getCallback().onAuthenticationSucceeded(getPublicKey(), getSignature());
            }

            @Override
            public void onAuthenticationFailed() {
                getCallback().onAuthenticationFailed();
                super.onAuthenticationFailed();
            }
        };
    }

    private String getPublicKey(){
        String encoded = new String(Base64.encode(generatePublicKey(context).getEncoded(), 0));
        String publicKey = "-----BEGIN PUBLIC KEY-----\r\n" + encoded + "\n-----END PUBLIC KEY-----";
        return new String(Base64.encode(publicKey.getBytes(), 0));
    }

    private String getSignature() {
        Signature signature = cryptoObject.getSignature();
        String signText = "";
        try {
//            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
//            crypt.reset();
//            crypt.update(textToEncrypt.getBytes("UTF-8"));
            signature.update(textToEncrypt.getBytes());
            signText = new String(Base64.encode(Base64.encode(signature.sign(),
                    0), 0));
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return signText;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public void stopListening() {
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        stopListening();
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        stopListening();
        super.onDestroy();
    }

    private void initKeyStore() {
        try {
            keyStore = KeyStore.getInstance(FingerprintConstant.ANDROID_KEY_STORE);
            keyStore.load(null);

            generateKeyPair(keyStore);
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initCryptoObject() {
        if (initSignature()) {
            cryptoObject = new FingerprintManager.CryptoObject(signature);
        }
    }

    private boolean initSignature() {
        try {
            PrivateKey key = (PrivateKey) keyStore.getKey(FingerprintConstant.FINGERPRINT, null);
            signature = Signature.getInstance(FingerprintConstant.SHA_1_WITH_RSA);
            signature.initSign(key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | UnrecoverableKeyException
                | NoSuchAlgorithmException | InvalidKeyException  e ) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    public static PublicKey generatePublicKey(Context context) {
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(context);
        if (fingerprintManagerCompat.isHardwareDetected() && fingerprintManagerCompat.hasEnrolledFingerprints()) {
            PublicKey publicKey = null;
            try {
                KeyStore keyStore = KeyStore.getInstance(FingerprintConstant.ANDROID_KEY_STORE);
                keyStore.load(null);
                generateKeyPair(keyStore);

                publicKey = keyStore.getCertificate(FingerprintConstant.FINGERPRINT).getPublicKey();
                KeyFactory factory = KeyFactory.getInstance(publicKey.getAlgorithm());
                X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey.getEncoded());
                return factory.generatePublic(spec);
            } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            return publicKey;
        } else {
            return null;
        }
    }

    protected static void generateKeyPair(KeyStore keyStore) throws KeyStoreException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        //check if key is stored already, if null, create new key
        if (keyStore.getCertificate(FingerprintConstant.FINGERPRINT) == null || keyStore.getCertificate(FingerprintConstant.FINGERPRINT).getPublicKey() == null) {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, FingerprintConstant.ANDROID_KEY_STORE);
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(FingerprintConstant.FINGERPRINT,
                    KeyProperties.PURPOSE_SIGN)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .setKeySize(2048)
                    .setUserAuthenticationRequired(false);
            keyPairGenerator.initialize(builder.build());
            keyPairGenerator.generateKeyPair();
        }
    }


    public interface Callback {
        void onAuthenticationError(int errMsgId, CharSequence errString);

        void onAuthenticationHelp(int helpMsgId, CharSequence helpString);

        void onAuthenticationSucceeded(String publicKey, String signature);

        void onAuthenticationFailed();
    }
}
