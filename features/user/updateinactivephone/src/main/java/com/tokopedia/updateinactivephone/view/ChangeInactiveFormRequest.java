package com.tokopedia.updateinactivephone.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public class ChangeInactiveFormRequest {

    public interface View extends CustomerView {

        void dismissLoading();

        void showLoading();

        void onForbidden();

        void showErrorValidateData(String errorMessage);

        void onUserDataValidated();

        void onPhoneTooShort();

        void onPhoneTooLong();

        void onPhoneBlackListed();

        void onWrongUserIDInput();

        void onPhoneDuplicateRequest();

        void onPhoneServerError();

        void onSameMsisdn();

        void onAlreadyRegisteredMsisdn();

        void onEmptyMsisdn();

        void onInvalidPhone();

        void onMaxReachedPhone();

        void showErrorPhoneNumber(int error_field_required);

        void showErrorEmail(int error_invalid_email);

        void onEmailError();

        void onUserNotRegistered();

        void onInvalidFileUploaded();

        void onUpdateDataRequestFailed();

        void onUpdateDataRequestSuccess();
    }

    public interface Presenter extends CustomerPresenter<ChangeInactiveFormRequest.View> {

        void uploadPhotoIdImage(String email, String phone, String userId);
        void validateUserData(String email, String phone, String userId);
    }

}
