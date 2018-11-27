package com.tokopedia.updateinactivephone.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.subscriber.UpdatePhoneNumberSubscriber;
import com.tokopedia.updateinactivephone.subscriber.ValidateUserDataSubscriber;
import com.tokopedia.updateinactivephone.usecase.UploadChangePhoneNumberRequestUseCase;
import com.tokopedia.updateinactivephone.usecase.ValidateUserDataUseCase;
import com.tokopedia.updateinactivephone.view.ChangeInactiveFormRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.ID;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.PARAM_BANK_BOOK_IMAGE_PATH;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.PARAM_KTP_IMAGE_PATH;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.RESOLUTION;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.SERVER_ID;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.TOKEN;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.USERID;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.EMAIL;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.PHONE;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.USER_ID;
import static com.tokopedia.updateinactivephone.presenter.ChangeInactivePhonePresenter.PHONE_MATCHER;

public class ChangeInactiveFormRequestPresenter extends BaseDaggerPresenter<ChangeInactiveFormRequest.View>
        implements ChangeInactiveFormRequest.Presenter {

    private String photoIdImagePath;
    private String accountImagePath;

    private ValidateUserDataUseCase validateUserDataUseCase;
    private UploadChangePhoneNumberRequestUseCase uploadChangePhoneNumberRequestUseCase;

    @Inject
    public ChangeInactiveFormRequestPresenter(ValidateUserDataUseCase validateUserDataUseCase,
                                              UploadChangePhoneNumberRequestUseCase uploadChangePhoneNumberRequestUseCase) {
        this.validateUserDataUseCase = validateUserDataUseCase;
        this.uploadChangePhoneNumberRequestUseCase = uploadChangePhoneNumberRequestUseCase;
    }


    public void setPhotoIdImagePath(String imagePath) {
        this.photoIdImagePath = imagePath;
    }

    @Override
    public void uploadPhotoIdImage(String email, String phone, String userId) {
        UpdatePhoneNumberSubscriber updatePhoneNumberSubscriber = new UpdatePhoneNumberSubscriber(getView());
        if (!TextUtils.isEmpty(photoIdImagePath)) {
            uploadChangePhoneNumberRequestUseCase.execute(getUploadChangePhoneNumberRequestParam(email, phone, userId),
                    updatePhoneNumberSubscriber);
        }
    }

    public boolean isValidPhotoIdPath() {
        return !TextUtils.isEmpty(photoIdImagePath);
    }

    public void setAccountPhotoImagePath(String imagePath) {
        this.accountImagePath = imagePath;
    }

    @Override
    public void validateUserData(String email, String phone, String userId) {

        boolean validEmail = isValidEmail(email);
        boolean validPhone = isValidPhone(phone);

        if (validPhone && validEmail) {
            getView().showLoading();
            validateUserDataUseCase.execute(phone, email, userId,
                    new ValidateUserDataSubscriber(getView()));
        }
    }

    private boolean isValidEmail(String email) {
        boolean isValid = true;

        if (TextUtils.isEmpty(email)) {
            getView().showErrorPhoneNumber(R.string.email_field_empty);
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false;
            getView().showErrorEmail(R.string.invalid_email_error);
        }

        return isValid;
    }

    private RequestParams getUploadChangePhoneNumberRequestParam(String email, String phone, String userId) {
        RequestParams params = RequestParams.create();
        params.putString(USERID, userId);
        params.putString(ID, userId);
        params.putInt(SERVER_ID, 49);
        params.putInt(RESOLUTION, 215);
        params.putString(PHONE, phone);
        params.putString(EMAIL, email);
        params.putInt(USER_ID, Integer.valueOf(userId));
        params.putString(TOKEN, "tokopedia-lite-inactive-phone");

        setParamUploadIdImage(params);

        if (!TextUtils.isEmpty(accountImagePath)) {
            setParamUploadBankBookImage(params);
        }
        return params;
    }

    private void setParamUploadBankBookImage(RequestParams params) {
        params.putString(PARAM_BANK_BOOK_IMAGE_PATH, accountImagePath);

    }

    private void setParamUploadIdImage(RequestParams params) {
        params.putString(PARAM_KTP_IMAGE_PATH, photoIdImagePath);
    }

    private boolean isValidPhone(String phoneNumber) {
            boolean isValid = true;
            boolean check;
            Pattern p;
            Matcher m;
            p = Pattern.compile(PHONE_MATCHER);
            m = p.matcher(phoneNumber);
            check = m.matches();

            if (TextUtils.isEmpty(phoneNumber)) {
                getView().showErrorPhoneNumber(R.string.phone_field_empty);
                isValid = false;
            } else if (check && phoneNumber.length() < 8) {
                getView().showErrorPhoneNumber(R.string.phone_number_invalid_min_8);
                isValid = false;
            } else if (check && phoneNumber.length() > 15) {
                getView().showErrorPhoneNumber(R.string.phone_number_invalid_max_15);
                isValid = false;
            } else if (!check) {
                getView().showErrorPhoneNumber(R.string.invalid_phone_number);
                isValid = false;
            }
            return isValid;
    }

    @Override
    public void detachView() {
        super.detachView();
        validateUserDataUseCase.unsubscribe();
        uploadChangePhoneNumberRequestUseCase.unsubscribe();
    }
}
