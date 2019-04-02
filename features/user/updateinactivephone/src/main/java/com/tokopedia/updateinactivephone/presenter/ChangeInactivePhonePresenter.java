package com.tokopedia.updateinactivephone.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.subscriber.CheckPhoneNumberStatusSubscriber;
import com.tokopedia.updateinactivephone.usecase.CheckPhoneNumberStatusUsecase;
import com.tokopedia.updateinactivephone.view.ChangeInactivePhone;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class ChangeInactivePhonePresenter extends BaseDaggerPresenter<ChangeInactivePhone.View>
        implements ChangeInactivePhone.Presenter {

    private final CheckPhoneNumberStatusUsecase checkPhoneNumberStatusUsecase;
    public static final String PHONE_MATCHER = "^(\\+)?+[0-9]*$";

    @Inject
    public ChangeInactivePhonePresenter(CheckPhoneNumberStatusUsecase checkPhoneNumberStatusUsecase) {
        this.checkPhoneNumberStatusUsecase = checkPhoneNumberStatusUsecase;
    }

    @Override
    public void attachView(ChangeInactivePhone.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        checkPhoneNumberStatusUsecase.unsubscribe();
    }


    private boolean isValid(String phoneNumber) {
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
    public void checkPhoneNumberStatus(String phone) {
        if (isValid(phone)) {
            getView().showLoading();
            checkPhoneNumberStatusUsecase.execute(phone,
                    new CheckPhoneNumberStatusSubscriber(getView()));
        }
    }

}