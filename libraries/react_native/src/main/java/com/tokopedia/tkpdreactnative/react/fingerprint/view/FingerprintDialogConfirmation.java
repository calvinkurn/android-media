package com.tokopedia.tkpdreactnative.react.fingerprint.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tkpdreactnative.react.fingerprint.di.DaggerFingerprintComponent;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.tkpdreactnative.R;
import com.tokopedia.tkpdreactnative.react.common.view.DialogPreferenceHide;
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

        getAlertDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                fingerprintConfirmationPresenter.detachView();
            }
        });
        setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fingerprintConfirmationPresenter.savePreferenceHide(isCheckedBoxHideDialog());
                dismiss();
            }
        });
        setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fingerprintConfirmationPresenter.savePreferenceHide(isCheckedBoxHideDialog());
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
        if(context instanceof AppCompatActivity) {
            FingerPrintUIHelper fingerPrintUIHelper = new FingerPrintUIHelper((AppCompatActivity)context, transactionId, callback);
            fingerPrintUIHelper.startListening();
        }
        dismiss();
    }

    @Override
    public void onErrorSavePreference() {
        dismiss();
    }

    @Override
    public void onGetPreference(boolean isShow) {
        if(isShow){
            super.show();
        }
    }

    @Override
    public void onErrorGetPreference() {

    }
}
