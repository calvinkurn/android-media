package com.tokopedia.core.manage.people.profile.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.manage.people.profile.interactor.ManagePeopleProfileInteractor;
import com.tokopedia.core.manage.people.profile.interactor.ManagePeopleProfileInteractorImpl;
import com.tokopedia.core.manage.people.profile.listener.EmailVerificationView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nisie on 9/29/16.
 */

public class EmailVerificationPresenterImpl implements EmailVerificationPresenter {


    private static final String TIMESTAMP = "timestamp";
    private static final String EXPIRE_TIME = "expired_time";
    EmailVerificationView viewListener;
    ManagePeopleProfileInteractor networkInteractor;

    public EmailVerificationPresenterImpl(EmailVerificationView viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = ManagePeopleProfileInteractorImpl.createInstance();
    }

    @Override
    public void changeEmailClicked() {
        if (isValidForm()) {
            viewListener.showLoadingProgress();
            changeEmail();
        }
    }

    private void changeEmail() {

        networkInteractor.editEmail(viewListener.getActivity(), getParam(), new ManagePeopleProfileInteractor.EditEmailListener() {
            @Override
            public void onSuccess() {
                viewListener.finishLoading();
                viewListener.onSuccessChangeEmail();
            }

            @Override
            public void onTimeout() {
                viewListener.finishLoading();
                viewListener.showErrorToast(viewListener.getString(R.string.msg_connection_timeout));
            }

            @Override
            public void onFailAuth() {
                viewListener.finishLoading();
                viewListener.showErrorToast("");
            }

            @Override
            public void onThrowable(Throwable e) {
                if (e.getMessage().contains("timeout")) {
                    onTimeout();
                } else {
                    viewListener.finishLoading();
                    viewListener.showErrorToast(viewListener.getString(R.string.msg_no_connection));
                }
            }

            @Override
            public void onError(String error) {
                viewListener.finishLoading();
                viewListener.showErrorToast(error);

            }

            @Override
            public void onNullData() {
                viewListener.finishLoading();
                viewListener.showErrorToast("");
            }

            @Override
            public void onNoConnection() {
                viewListener.finishLoading();
                viewListener.showErrorToast("");
            }
        });
    }

    private Map<String, String> getParam() {
        Map<String, String> param = new HashMap<>();
        param.put("new_email", viewListener.getEmail().getText().toString());
        param.put("password", viewListener.getPassword().getText().toString());
        param.put("otp_code", viewListener.getOTP().getText().toString());
        return param;
    }

    @Override
    public void onRequestOTP() {
        viewListener.showLoadingProgress();
        sendEmailOTP();
        viewListener.getOTP().setEnabled(true);
    }

    @Override
    public void onDestroyView() {
        networkInteractor.unSubscribe();
    }

    private void sendEmailOTP() {
        networkInteractor.sendOTPEditEmail(viewListener.getActivity(), new HashMap<String, String>(), new ManagePeopleProfileInteractor.RequestOTPListener() {
            @Override
            public void onSuccess() {
                viewListener.finishLoading();
                viewListener.onSuccessRequestOTP();

            }

            @Override
            public void onTimeout() {
                viewListener.finishLoading();
                viewListener.showErrorToast("");
            }

            @Override
            public void onFailAuth() {
                viewListener.finishLoading();
                viewListener.showErrorToast("");
            }

            @Override
            public void onThrowable(Throwable e) {
                viewListener.finishLoading();
                viewListener.showErrorToast("");
            }

            @Override
            public void onError(String error) {
                viewListener.finishLoading();
                viewListener.showErrorToast(error);
            }

            @Override
            public void onNullData() {
                viewListener.finishLoading();
                viewListener.showErrorToast("");
            }

            @Override
            public void onNoConnection() {
                viewListener.finishLoading();
                viewListener.showErrorToast("");
            }
        });
    }

    private boolean isValidForm() {
        boolean isValid = true;

        if (viewListener.getOTP().getText().toString().length() == 0) {
            viewListener.getOTP().setError(viewListener.getString(R.string.error_field_required));
            isValid = false;
        }

        if (viewListener.getPassword().getText().toString().length() == 0) {
            viewListener.getPassword().setError(viewListener.getString(R.string.error_field_required));
            viewListener.getPassword().requestFocus();
            isValid = false;
        }

        if (viewListener.getEmail().getText().toString().trim().length() == 0) {
            viewListener.getEmail().setError(viewListener.getString(R.string.error_field_required));
            viewListener.getEmail().requestFocus();
            isValid = false;
        } else if (!CommonUtils.EmailValidation(viewListener.getEmail().getText().toString())) {
            viewListener.getEmail().setError(viewListener.getString(R.string.error_invalid_email));
            viewListener.getEmail().requestFocus();
            isValid = false;
        }
        return isValid;
    }
}