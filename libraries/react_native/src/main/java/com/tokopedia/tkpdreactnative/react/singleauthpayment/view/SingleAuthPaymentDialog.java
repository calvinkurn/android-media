package com.tokopedia.tkpdreactnative.react.singleauthpayment.view;

import android.app.Activity;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tkpdreactnative.react.fingerprint.di.DaggerFingerprintComponent;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.tkpdreactnative.R;
import com.tokopedia.tkpdreactnative.react.common.view.DialogPreferenceHide;
import com.tokopedia.tkpdreactnative.react.fingerprint.di.FingerprintModule;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.FingerPrintUIHelper;
import com.tokopedia.tkpdreactnative.react.singleauthpayment.view.presenter.SetSingleAuthPaymentContract;
import com.tokopedia.tkpdreactnative.react.singleauthpayment.view.presenter.SetSingleAuthPaymentPresenter;
import com.tokopedia.tkpdreactnative.react.singleauthpayment.view.presenter.SinglePaymentConfirmationContract;
import com.tokopedia.tkpdreactnative.react.singleauthpayment.view.presenter.SinglePaymentPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 4/24/18.
 */

public class SingleAuthPaymentDialog extends DialogPreferenceHide implements SinglePaymentConfirmationContract.View, SetSingleAuthPaymentContract.View {
    private FingerPrintUIHelper.Callback callback;
    @Inject
    SinglePaymentPresenter singlePaymentPresenter;
    @Inject
    SetSingleAuthPaymentPresenter singleAuthPaymentPresenter;
    private boolean saveSinglePaymentAfterSavePref = true;

    public SingleAuthPaymentDialog(Activity context, Type type, FingerPrintUIHelper.Callback callback) {
        super(context, type);
        this.callback = callback;
    }

    @Override
    public void initView(View dialogView) {
        super.initView(dialogView);
        setBtnCancel(context.getString(R.string.title_no));
        setBtnOk(context.getString(R.string.single_payment_label_activated));
        setTitle(context.getString(R.string.single_payment_label_activated_single));
        setDesc(context.getString(R.string.single_auth_label_desc));

        setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSinglePaymentAfterSavePref = false;
                singlePaymentPresenter.savePreferenceHide(isCheckedBoxHideDialog());
            }
        });
        setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSinglePaymentAfterSavePref = true;
                singlePaymentPresenter.savePreferenceHide(isCheckedBoxHideDialog());
            }
        });
    }

    @Override
    protected void init() {
        super.init();
        getAlertDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                singlePaymentPresenter.detachView();
                singleAuthPaymentPresenter.detachView();
                hideProgressLoading();
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
        singlePaymentPresenter.attachView(this);
        singleAuthPaymentPresenter.attachView(this);
        singlePaymentPresenter.getPreferenceHide();
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
    public void onErrorNetworkSingleAuth(String errorMessage) {
        NetworkErrorHelper.showRedCloseSnackbar(context, errorMessage);
        dismiss();
    }

    @Override
    public void onSuccessSingleAuth(String successMessage) {
        NetworkErrorHelper.showGreenCloseSnackbar(context, successMessage);
        dismiss();
    }

    @Override
    public void onSuccessSavePreference() {
        if(saveSinglePaymentAfterSavePref) {
            if (context instanceof AppCompatActivity) {
                singleAuthPaymentPresenter.setSingleAuthenticationMode();
            }
        }else{
            dismiss();
        }
    }

    @Override
    public void onErrorSavePreference() {
        dismiss();
    }

    @Override
    public void onGetPreference(boolean isHide) {
        if(!isHide){
            super.show();
        }
    }

    @Override
    public void onErrorGetPreference() {

    }
}
