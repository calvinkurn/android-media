package com.tokopedia.tkpdreactnative.react.fingerprint.view;

import android.app.Activity;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.tkpdreactnative.R;
import com.tokopedia.tkpdreactnative.react.common.view.DialogPreferenceHide;
import com.tokopedia.tkpdreactnative.react.fingerprint.di.DaggerFingerprintComponent;
import com.tokopedia.tkpdreactnative.react.fingerprint.di.FingerprintModule;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.presenter.FingerprintConfirmationContract;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.presenter.FingerprintConfirmationPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 4/24/18.
 */

public class FingerprintDialogConfirmation extends DialogPreferenceHide implements FingerprintConfirmationContract.View {
    private String transactionId;
    private FingerPrintUIHelper.Callback callback;
    @Inject
    FingerprintConfirmationPresenter fingerprintConfirmationPresenter;
    private boolean showFingerprintAfterSavePref = true;

    public FingerprintDialogConfirmation(Activity context, Type type, String transactionId, FingerPrintUIHelper.Callback callback) {
        super(context, type);
        this.transactionId = transactionId;
        this.callback = callback;
    }

    @Override
    public void initView(View dialogView) {
        super.initView(dialogView);
        setBtnCancel(context.getString(R.string.title_no));
        setBtnOk(context.getString(R.string.dialog_fingerprint_label_register));
        setTitle(context.getString(R.string.dialog_fingerprint_label_auth_fingerprint_title));
        setDesc(context.getString(R.string.dialog_fingerprint_label_desc_auth));

        setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showFingerprintAfterSavePref = false;
                        fingerprintConfirmationPresenter.savePreferenceHide(isCheckedBoxHideDialog());
                    }
                });
            }
        });
        setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showFingerprintAfterSavePref = true;
                        fingerprintConfirmationPresenter.savePreferenceHide(isCheckedBoxHideDialog());
                    }
                });
            }
        });
    }

    @Override
    protected void init() {
        super.init();
        getAlertDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fingerprintConfirmationPresenter.detachView();
                        hideProgressLoading();
                    }
                });
            }
        });
    }

    @Override
    public void show() {
        DaggerFingerprintComponent
                .builder()
                .fingerprintModule(new FingerprintModule())
                .baseAppComponent(((BaseMainApplication) context.getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        fingerprintConfirmationPresenter.attachView(this);
        fingerprintConfirmationPresenter.getPreferenceHide();
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
    public void onSuccessSavePreference() {
        if (context instanceof AppCompatActivity && showFingerprintAfterSavePref) {
            FingerPrintUIHelper fingerPrintUIHelper = new FingerPrintUIHelper((AppCompatActivity) context, transactionId, callback);
            fingerPrintUIHelper.startListening();
        }
        dismiss();
    }

    @Override
    public void onErrorSavePreference() {
        dismiss();
    }

    @Override
    public void onGetPreference(boolean isHide) {
        if (!isHide) {
            super.show();
        }
    }

    @Override
    public void onErrorGetPreference() {

    }
}
