package com.tokopedia.tkpdreactnative.react.fingerprint.view;

import android.annotation.TargetApi;
import android.os.Build;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tkpdreactnative.R;
import com.tokopedia.tkpdreactnative.react.fingerprint.di.DaggerFingerprintComponent;
import com.tokopedia.tkpdreactnative.react.fingerprint.di.FingerprintModule;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.presenter.SaveFingerPrintContract;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.presenter.SaveFingerPrintPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public class FingerPrintUIHelper implements FingerprintDialogRegister.ListenerRegister, SaveFingerPrintContract.View {

    @Inject
    SaveFingerPrintPresenter fingerPrintPresenter;

    private final AppCompatActivity activity;
    private String transactionId;
    private FingerprintDialogRegister fingerprintDialog;
    private Callback callback;

    public FingerPrintUIHelper(AppCompatActivity activity, String transactionId, Callback callback) {
        this.activity = activity;
        this.transactionId = transactionId;
        this.callback = callback;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void startListening() {
        DaggerFingerprintComponent
                .builder()
                .fingerprintModule(new FingerprintModule())
                .baseAppComponent(((BaseMainApplication) activity.getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(activity);
        if (fingerprintManagerCompat.isHardwareDetected() && fingerprintManagerCompat.hasEnrolledFingerprints()) {
            fingerprintDialog = FingerprintDialogRegister.createInstance(fingerPrintPresenter.getUserId(), transactionId);
            fingerprintDialog.setListenerRegister(this);
            fingerprintDialog.setContext(activity);
            fingerprintDialog.show(activity.getSupportFragmentManager(), "fingerprintRegister");
        }
    }

    @Override
    public void onDismissDialog() {
        fingerPrintPresenter.detachView();
    }

    @Override
    public void onCreate() {
        fingerPrintPresenter.attachView(this);
    }

    @Override
    public void hideProgressLoading() {
        callback.hideProgressDialog();
    }

    @Override
    public void showProgressLoading() {
        callback.showProgressDialog();
    }

    @Override
    public void onRegisterFingerPrint(String transactionId, String publicKey, String date, String signature, String userId) {
        fingerPrintPresenter.registerFingerPrint(transactionId, publicKey, date, signature, userId);
    }

    @Override
    public void showErrorRegisterSnackbar() {
        NetworkErrorHelper.showRedCloseSnackbar(activity, activity.getString(R.string.fingerprint_label_failed_fingerprint));
    }

    @Override
    public void onSuccessRegisterFingerPrint() {
        fingerprintDialog.stopListening();
        fingerprintDialog.dismiss();
        NetworkErrorHelper.showGreenCloseSnackbar(activity, activity.getString(R.string.fingerprint_label_successed_fingerprint));
    }

    @Override
    public void onErrorNetworkRegisterFingerPrint(Throwable e) {
        fingerprintDialog.onErrorRegisterFingerPrint();
    }

    public interface Callback {
        void showProgressDialog();

        void hideProgressDialog();
    }
}
