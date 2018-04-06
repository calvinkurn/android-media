package com.tokopedia.tkpdreactnative.react.fingerprint.view;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.fingerprint.view.FingerPrintDialog;
import com.tokopedia.tkpdreactnative.R;
import com.tokopedia.tkpdreactnative.react.fingerprint.di.FingerprintModule;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.presenter.SaveFingerPrintContract;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.presenter.SaveFingerPrintPresenter;
import com.tokopedia.tkpdreactnative.react.fingerprint.di.DaggerFingerprintComponent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public class FingerPrintUIHelper implements FingerPrintDialog.Callback, FingerprintDialogRegister.EventListener, SaveFingerPrintContract.View {

    private static final int MAX_ERROR = 3;

    @Inject
    SaveFingerPrintPresenter fingerPrintPresenter;

    private final AppCompatActivity activity;
    private String transactionId;
    private FingerprintDialogRegister fingerprintDialog;
    private String date;
    private int counterError = 0;
    private ProgressDialog progressDialog;

    public FingerPrintUIHelper(AppCompatActivity activity, String transactionId) {
        this.activity = activity;
        this.transactionId = transactionId;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getString(R.string.title_loading));
        generateDate();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void startListening() {
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(activity);
        if (fingerprintManagerCompat.isHardwareDetected() && fingerprintManagerCompat.hasEnrolledFingerprints()) {
            if (fingerprintDialog == null) {
                fingerprintDialog = new FingerprintDialogRegister();
                if(fingerPrintPresenter != null) {
                    fingerprintDialog.setTextToEncrypt(fingerPrintPresenter.getUserId() + date);
                }
                fingerprintDialog.setCallback(this);
                fingerprintDialog.setContext(activity);
                fingerprintDialog.setEventListener(this);
                fingerprintDialog.show(activity.getSupportFragmentManager(), "fingerprint");
            }
            fingerprintDialog.startListening();
        }
    }

    private void generateDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        date = dateFormat.format(calendar.getTime());
    }

    private boolean updateCounterError() {
        counterError++;
        fingerprintDialog.updateDesc(activity.getString(R.string.fingerprint_label_desc_default));
        fingerprintDialog.updateTitle(activity.getString(R.string.fingerprint_label_try_again));
        if (counterError > MAX_ERROR) {
            closeBottomSheet();
            return false;
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

    }

    public void onAuthenticationSucceeded(String publicKey, String signature) {
        fingerPrintPresenter.registerFingerPrint(transactionId, publicKey, date,
                signature, fingerPrintPresenter.getUserId());
    }

    @Override
    public void onAuthenticationFailed() {
        updateCounterError();
    }

    public void closeBottomSheet() {
        fingerprintDialog.dismiss();
    }

    @Override
    public void onDismissDialog() {
        fingerprintDialog.stopListening();
        fingerPrintPresenter.detachView();
    }

    @Override
    public void onCreate() {
        DaggerFingerprintComponent
                .builder()
                .fingerprintModule(new FingerprintModule())
                .baseAppComponent(((BaseMainApplication) activity.getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        fingerPrintPresenter.attachView(this);

        if(fingerprintDialog != null) {
            fingerprintDialog.setTextToEncrypt("" + date);
        }
    }

    @Override
    public void hideProgressLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showProgressLoading() {
        progressDialog.show();
    }

    @Override
    public void onErrorNetworkRegisterFingerPrint(Throwable e) {
        if (updateCounterError()) {
            startListening();
        }
    }

    @Override
    public void onSuccessRegisterFingerPrint() {

    }
}
