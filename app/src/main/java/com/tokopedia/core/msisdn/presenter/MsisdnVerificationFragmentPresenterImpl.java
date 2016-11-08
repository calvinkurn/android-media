package com.tokopedia.core.msisdn.presenter;

import com.tokopedia.core.R;
import com.tokopedia.core.msisdn.MSISDNConstant;
import com.tokopedia.core.msisdn.fragment.MsisdnVerificationFragment;
import com.tokopedia.core.msisdn.interactor.MsisdnVerificationRetrofitInteractor;
import com.tokopedia.core.msisdn.interactor.MsisdnVerificationRetrofitInteractorImpl;
import com.tokopedia.core.msisdn.listener.MsisdnVerificationFragmentView;
import com.tokopedia.core.util.SessionHandler;

import java.util.HashMap;

/**
 * Created by Nisie on 7/13/16.
 */
public class MsisdnVerificationFragmentPresenterImpl implements MsisdnVerificationFragmentPresenter, MSISDNConstant {
    MsisdnVerificationFragmentView viewListener;
    MsisdnVerificationRetrofitInteractor networkInteractor;

    public MsisdnVerificationFragmentPresenterImpl(MsisdnVerificationFragment viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new MsisdnVerificationRetrofitInteractorImpl();
    }

    @Override
    public void setReminder(boolean isChecked) {
        SessionHandler.setDontRemindLater(isChecked);
        viewListener.onDismiss();
    }

    @Override
    public void verifyOTP(String otpCode) {
        if (isValidOTP(otpCode)) {
            viewListener.showLoading();
            viewListener.removeError();
            networkInteractor.verifyOTP(viewListener.getActivity(), getVerifyOTPParam(), new MsisdnVerificationRetrofitInteractor.VerifyOTPListener() {
                @Override
                public void onSuccess() {
                    SessionHandler.setIsMSISDNVerified(true);
                    viewListener.onSuccessVerifyOTP();
                }

                @Override
                public void onTimeout() {
                    viewListener.finishLoading();
                    viewListener.showError("");

                }

                @Override
                public void onFailAuth() {
                    viewListener.finishLoading();
                    viewListener.showError("");
                }

                @Override
                public void onThrowable(Throwable e) {
                    viewListener.finishLoading();
                    viewListener.showError("");
                }

                @Override
                public void onError(String error) {
                    viewListener.finishLoading();
                    viewListener.showError(error);
                }

                @Override
                public void onNullData() {
                    viewListener.finishLoading();
                    viewListener.showError("");
                }

                @Override
                public void onNoConnection() {

                }
            });
        }
    }

    private boolean isValidOTP(String otpCode) {
        boolean isValid = true;
        if (otpCode.length() == 0) {
            isValid = false;
            viewListener.setError(viewListener.getOtpEditText(), viewListener.getActivity().getString(R.string.error_field_required));
        } else if (otpCode.length() > 6) {
            viewListener.setError(viewListener.getOtpEditText(), viewListener.getActivity().getString(R.string.error_max_otp));
            isValid = false;
        } else if (otpCode.length() < 6) {
            viewListener.setError(viewListener.getOtpEditText(), viewListener.getActivity().getString(R.string.error_min_otp));
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void requestOTP(String phoneNumber) {
        if (isValidPhoneNumber(phoneNumber)) {
            viewListener.showLoading();
            viewListener.removeError();
            networkInteractor.requestOTP(viewListener.getActivity(), getRequestOTPParam(), new MsisdnVerificationRetrofitInteractor.RequestOTPListener() {
                @Override
                public void onSuccess() {
                    viewListener.onSuccessRequestOTP();
                }

                @Override
                public void onTimeout() {
                    viewListener.finishLoading();
                    viewListener.showError("");

                }

                @Override
                public void onFailAuth() {
                    viewListener.finishLoading();
                    viewListener.showError("");

                }

                @Override
                public void onThrowable(Throwable e) {
                    viewListener.finishLoading();
                    viewListener.showError("");

                }

                @Override
                public void onError(String error) {
                    viewListener.finishLoading();
                    viewListener.showError(error);
                }

                @Override
                public void onNullData() {
                    viewListener.finishLoading();
                    viewListener.showError("");
                }

                @Override
                public void onNoConnection() {
                    viewListener.finishLoading();
                    viewListener.showError("");
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        networkInteractor.unSubscribeObservable();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        boolean isValid = true;
        if (phoneNumber.length() == 0) {
            isValid = false;
            viewListener.setError(viewListener.getPhoneEditText(),
                    viewListener.getActivity().getString(R.string.error_field_required));
        } else if (!phoneNumber.startsWith("0")) {
            isValid = false;
            viewListener.setError(viewListener.getPhoneEditText(),
                    viewListener.getActivity().getString(R.string.error_phone_wrong_format));
        } else if (phoneNumber.length() > 16) {
            isValid = false;
            viewListener.setError(viewListener.getPhoneEditText(),
                    viewListener.getActivity().getString(R.string.error_phone_too_long));
        } else if (phoneNumber.length() < 6) {
            isValid = false;
            viewListener.setError(viewListener.getPhoneEditText(),
                    viewListener.getActivity().getString(R.string.error_phone_too_short));
        }
        return isValid;

    }

    public HashMap<String, String> getVerifyOTPParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_PHONE, viewListener.getPhoneNumber());
        param.put(PARAM_CODE, viewListener.getVerificationCode());
        return param;
    }

    public HashMap<String, String> getRequestOTPParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_PHONE, viewListener.getPhoneNumber());
        param.put(PARAM_UPDATE_FLAG, "0");
        param.put(PARAM_EMAIL_CODE, "");
        return param;
    }
}
