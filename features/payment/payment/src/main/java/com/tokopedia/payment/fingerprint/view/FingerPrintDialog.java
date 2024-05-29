package com.tokopedia.payment.fingerprint.view;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tokopedia.fingerprint.FingerprintUtil;
import com.tokopedia.payment.R;
import com.tokopedia.unifycomponents.BottomSheetUnify;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;


/**
 * Created by zulfikarrahman on 3/26/18.
 */

@TargetApi(Build.VERSION_CODES.M)
public class FingerPrintDialog extends BottomSheetUnify {

    private TextView descFingerprint;
    private String textToEncrypt = "";
    private FingerprintManager.CryptoObject cryptoObject;
    private Signature signature;
    private KeyStore keyStore;
    private CancellationSignal cancellationSignal;
    private Callback callback;
    private Context context;

    protected View inflatedView;

    public void setTextToEncrypt(String textToEncrypt) {
        this.textToEncrypt = textToEncrypt;
    }

    public String getTextToEncrypt() {
        return textToEncrypt;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBottomSheet();
    }

    protected int getLayoutResourceId(){
        return R.layout.bottom_sheet_fingerprint_view;
    }

    private void initBottomSheet() {
        inflatedView = LayoutInflater.from(context).inflate(getLayoutResourceId(), null);
        descFingerprint = inflatedView.findViewById(R.id.desc_fingerprint);
        setTitle(getString(R.string.fingerprint_label_scan_your_fingerprint));
        setChild(inflatedView);
        initView(inflatedView);
        onCloseButtonClick();
    }

    protected void onCloseButtonClick(){}

    protected void initView(View view){

    }

    public void updateTitle(String title) {
        bottomSheetTitle.setText(title);
    }

    public void updateDesc(String title) {
        descFingerprint.setText(title);
    }

    public void setContext(Context context) {
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
                getCallback().onAuthenticationSucceeded(FingerprintUtil.INSTANCE.getPublicKey(FingerprintUtil.INSTANCE.generatePublicKey(context)), getSignature());
            }

            @Override
            public void onAuthenticationFailed() {
                getCallback().onAuthenticationFailed();
                super.onAuthenticationFailed();
            }
        };
    }

    private String getSignature() {
        String signText = "";
        try {
            keyStore.load(null);
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(FingerprintUtil.FINGERPRINT, null);
            Signature signature = Signature.getInstance(FingerprintUtil.SHA_1_WITH_RSA);
            signature.initSign(privateKey);
            signature.update(getTextToEncrypt().getBytes());
            signText = Base64.encodeToString(signature.sign(),
                    Base64.NO_WRAP);
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
            keyStore = KeyStore.getInstance(FingerprintUtil.ANDROID_KEY_STORE);
            keyStore.load(null);

            FingerprintUtil.INSTANCE.generateKeyPair(keyStore);
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
            keyStore.load(null);
            PrivateKey key = (PrivateKey) keyStore.getKey(FingerprintUtil.FINGERPRINT, null);
            signature = Signature.getInstance(FingerprintUtil.SHA_1_WITH_RSA);
            signature.initSign(key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | UnrecoverableKeyException
                | NoSuchAlgorithmException | InvalidKeyException| IOException | CertificateException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @Override
    public void onPause() {
        stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        startListening();
        super.onResume();
    }

    public interface Callback {
        void onAuthenticationError(int errMsgId, CharSequence errString);

        void onAuthenticationHelp(int helpMsgId, CharSequence helpString);

        void onAuthenticationSucceeded(String publicKey, String signature);

        void onAuthenticationFailed();
    }

}
