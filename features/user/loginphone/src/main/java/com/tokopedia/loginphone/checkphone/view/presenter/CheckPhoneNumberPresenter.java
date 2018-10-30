package com.tokopedia.loginphone.checkphone.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.loginphone.R;
import com.tokopedia.loginphone.checkphone.domain.pojo.CheckMsisdnTokoCashPojo;
import com.tokopedia.loginphone.checkphone.domain.usecase.CheckMsisdnTokoCashUseCase;
import com.tokopedia.loginphone.checkphone.view.listener.CheckPhoneNumberContract;
import com.tokopedia.sessioncommon.ErrorHandlerSession;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 11/23/17.
 */

public class CheckPhoneNumberPresenter extends BaseDaggerPresenter<CheckPhoneNumberContract.View>
        implements CheckPhoneNumberContract.Presenter {

    private final CheckMsisdnTokoCashUseCase checkMsisdnTokoCashUseCase;

    @Inject
    public CheckPhoneNumberPresenter(CheckMsisdnTokoCashUseCase checkMsisdnTokoCashUseCase) {
        this.checkMsisdnTokoCashUseCase = checkMsisdnTokoCashUseCase;
    }

    @Override
    public void attachView(CheckPhoneNumberContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        checkMsisdnTokoCashUseCase.unsubscribe();
    }


    @Override
    public void loginWithPhoneNumber(String phoneNumber) {
        if (isValid(phoneNumber)) {
            getView().showLoading();
            checkMsisdnTokoCashUseCase.execute(CheckMsisdnTokoCashUseCase.getParam(phoneNumber),
                    new Subscriber<CheckMsisdnTokoCashPojo>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().dismissLoading();
                            ErrorHandlerSession.getErrorMessage(new ErrorHandlerSession.ErrorForbiddenListener
                                    () {
                                @Override
                                public void onForbidden() {
                                    getView().onForbidden();
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    getView().showErrorPhoneNumber(errorMessage);
                                }
                            }, e, getView().getContext());
                        }

                        @Override
                        public void onNext(CheckMsisdnTokoCashPojo checkMsisdnTokoCashPojo) {
                            getView().dismissLoading();
                            if (checkMsisdnTokoCashPojo.isTokopediaAccountExist()) {
                                getView().goToVerifyAccountPage(phoneNumber);
                            } else {
                                getView().showErrorPhoneNumber(R.string.phone_number_not_registered);
                            }
                        }
                    });
        }
    }

    private boolean isValid(String phoneNumber) {
        boolean isValid = true;
        if (TextUtils.isEmpty(phoneNumber)) {
            getView().showErrorPhoneNumber(R.string.error_field_required);
            isValid = false;
        } else if (phoneNumber.length() < 7 || phoneNumber.length() > 15) {
            getView().showErrorPhoneNumber(R.string.phone_number_invalid);
            isValid = false;
        }
        return isValid;
    }
}
