package com.tokopedia.fingerprint.view;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.fingerprint.R;
import com.tokopedia.fingerprint.util.FingerprintConstant;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;


/**
 * Created by zulfikarrahman on 3/26/18.
 */

@TargetApi(Build.VERSION_CODES.M)
public class FingerPrintDialog extends BottomSheets {

    private TextView descFingerprint;
    private String textToEncrypt = "";
    private FingerprintManagerCompat.CryptoObject cryptoObject;
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

    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initKeyStore();
        initCryptoObject();
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        startListening();
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        int show = super.show(transaction, tag);
        startListening();
        return show;
    }

    public void startListening() {
        FingerprintManagerCompat fingerprintManagerCompat =  FingerprintManagerCompat.from(context);
        cancellationSignal = new CancellationSignal();
        fingerprintManagerCompat.authenticate(cryptoObject, 0, cancellationSignal, getAuthenticationCallback(), null);
    }

    private FingerprintManagerCompat.AuthenticationCallback getAuthenticationCallback() {
        return new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                callback.onAuthenticationError(errMsgId, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                callback.onAuthenticationHelp(helpMsgId, helpString);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                callback.onAuthenticationSucceeded(getPublicKey(), getSignature());
            }

            @Override
            public void onAuthenticationFailed() {
                callback.onAuthenticationFailed();
                super.onAuthenticationFailed();
            }
        };
    }

    private String getPublicKey(){
        return new String(Base64.encode(generatePublicKey(getContext()).getEncoded(), 0));
    }

    private String getSignature() {
        Signature signature = cryptoObject.getSignature();
        String signText = "";
        try {
            signature.update(textToEncrypt.getBytes());
            signText = Base64.encodeToString(signature.sign(), 0);
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return signText;
    }

    public void stopListening() {
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
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
            cryptoObject = new FingerprintManagerCompat.CryptoObject(signature);
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
                | NoSuchAlgorithmException | InvalidKeyException e) {
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

                return keyStore.getCertificate(FingerprintConstant.FINGERPRINT).getPublicKey();
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
            return publicKey;
        } else {
            return null;
        }
    }

    protected static void generateKeyPair(KeyStore keyStore) throws KeyStoreException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        //check if key is stored already, if null, create new key
//        if (keyStore.getCertificate(FingerprintConstant.FINGERPRINT) == null || keyStore.getCertificate(FingerprintConstant.FINGERPRINT).getPublicKey() == null) {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, FingerprintConstant.ANDROID_KEY_STORE);
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(FingerprintConstant.FINGERPRINT,
                    KeyProperties.PURPOSE_SIGN)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PSS)
                    .setDigests(KeyProperties.DIGEST_SHA1)
                    .setUserAuthenticationRequired(false);
            keyPairGenerator.initialize(builder.build());
            keyPairGenerator.generateKeyPair();
//        }
    }


    public interface Callback {
        void onAuthenticationError(int errMsgId, CharSequence errString);

        void onAuthenticationHelp(int helpMsgId, CharSequence helpString);

        void onAuthenticationSucceeded(String publicKey, String signature);

        void onAuthenticationFailed();
    }
}
