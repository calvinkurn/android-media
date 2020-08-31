package com.tokopedia.changephonenumber.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.changephonenumber.domain.interactor.GetWarningUseCase;
import com.tokopedia.changephonenumber.domain.interactor.ValidateOtpStatusUseCase;
import com.tokopedia.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;
import com.tokopedia.changephonenumber.view.subscriber.GetWarningSubscriber;
import com.tokopedia.changephonenumber.view.subscriber.ValidateOtpStatusSubscriber;
import com.tokopedia.otp.verification.domain.data.OtpConstant;

/**
 * Created by milhamj on 18/12/17.
 */

public class ChangePhoneNumberWarningPresenter
        extends BaseDaggerPresenter<ChangePhoneNumberWarningFragmentListener.View>
        implements ChangePhoneNumberWarningFragmentListener.Presenter {

    private final GetWarningUseCase getWarningUseCase;
    private final ValidateOtpStatusUseCase getValidateOtpStatusUseCase;
    private ChangePhoneNumberWarningFragmentListener.View view;

    public ChangePhoneNumberWarningPresenter(GetWarningUseCase getWarningUseCase,
                                             ValidateOtpStatusUseCase validateOtpStatusUseCase) {
        this.getWarningUseCase = getWarningUseCase;
        this.getValidateOtpStatusUseCase = validateOtpStatusUseCase;
    }

    @Override
    public void attachView(ChangePhoneNumberWarningFragmentListener.View view) {
        this.view = view;
        super.attachView(view);
    }

    @Override
    public void detachView() {
        getWarningUseCase.unsubscribe();
        getValidateOtpStatusUseCase.unsubscribe();
        super.detachView();
    }

    @Override
    public void initView() {
//        validateOtpStatus(getView().getUserId());
        getWarning();
    }

    @Override
    public void getWarning() {
        view.showLoading();
        getWarningUseCase.execute(GetWarningUseCase.getGetWarningParam(),
                new GetWarningSubscriber(view));
    }

    @Override
    public void validateOtpStatus(int userId) {
        view.showLoading();
        getValidateOtpStatusUseCase.execute(ValidateOtpStatusUseCase.
                getValidateOtpParam(userId, OtpConstant.OtpType.VERIFY_USER_CHANGE_PHONE_NUMBER),
                new ValidateOtpStatusSubscriber(view));
    }
}
