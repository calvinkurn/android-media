package com.tokopedia.loginphone.checkregisterphone.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.loginphone.R;
import com.tokopedia.loginphone.checkregisterphone.domain.CheckRegisterPhoneNumberUseCase;
import com.tokopedia.loginphone.checkregisterphone.domain.pojo.CheckRegisterPhoneNumberPojo;
import com.tokopedia.loginphone.checkregisterphone.view.listener.CheckRegisterPhoneNumber;
import com.tokopedia.sessioncommon.ErrorHandlerSession;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by yfsx on 26/2/18.
 */

public class CheckRegisterPhoneNumberPresenter extends BaseDaggerPresenter<CheckRegisterPhoneNumber.View>
        implements CheckRegisterPhoneNumber.Presenter {

    private static final int COUNT_MIN = 8;
    private static final int COUNT_MAX = 15;

    private final CheckRegisterPhoneNumberUseCase checkMsisdnPhoneNumberUseCase;

    @Inject
    public CheckRegisterPhoneNumberPresenter(CheckRegisterPhoneNumberUseCase checkMsisdnPhoneNumberUseCase) {
        this.checkMsisdnPhoneNumberUseCase = checkMsisdnPhoneNumberUseCase;
    }

    @Override
    public void attachView(CheckRegisterPhoneNumber.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        checkMsisdnPhoneNumberUseCase.unsubscribe();
    }


    @Override
    public void checkPhoneNumber(String phoneNumber) {
        getView().showLoading();
        if (isValid(phoneNumber))
            checkMsisdnPhoneNumberUseCase.execute(CheckRegisterPhoneNumberUseCase.getParams
                    (phoneNumber), new Subscriber<CheckRegisterPhoneNumberPojo>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if(getView()!= null) {
                        getView().dismissLoading();
                        getView().showErrorPhoneNumber(ErrorHandlerSession.getErrorMessage(getView()
                                .getContext(), e));
                    }
                }

                @Override
                public void onNext(CheckRegisterPhoneNumberPojo checkMsisdnPojo) {
                    getView().dismissLoading();
                    if (!checkMsisdnPojo.isExist() && getView() != null) {
                        getView().showConfirmationPhoneNumber(checkMsisdnPojo.getPhoneView());
                    } else if (getView() != null) {
                        getView().showAlreadyRegisteredDialog(checkMsisdnPojo.getPhoneView());
                    }
                }
            });
    }


    private boolean isValid(String phoneNumber) {
        if (getView() == null) {
            return false;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            getView().showErrorPhoneNumber(R.string.error_field_required_phone);
            return false;
        }
        if (phoneNumber.length() < COUNT_MIN) {
            getView().showErrorPhoneNumber(getView().getContext().getString(R.string.error_char_count_under));
            return false;
        }

        if (phoneNumber.length() > COUNT_MAX) {
            getView().showErrorPhoneNumber(getView().getContext().getString(R.string.error_char_count_over));
            return false;
        }

        return true;
    }
}
