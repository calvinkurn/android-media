package com.tokopedia.changephonenumber.di.warning;

import com.tokopedia.changephonenumber.domain.interactor.GetWarningUseCase;
import com.tokopedia.changephonenumber.domain.interactor.ValidateOtpStatusUseCase;
import com.tokopedia.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;
import com.tokopedia.changephonenumber.view.presenter.ChangePhoneNumberWarningPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by alvinatin on 01/10/18.
 */

@Module
public class ChangePhoneNumberWarningModule {
    @ChangePhoneNumberWarningScope
    @Provides
    ChangePhoneNumberWarningFragmentListener.Presenter provideChangePhoneNumberWarningPresenter
            (GetWarningUseCase getWarningUseCase,
             ValidateOtpStatusUseCase validateOtpStatusUseCase) {
        return new ChangePhoneNumberWarningPresenter(getWarningUseCase, validateOtpStatusUseCase);
    }
}
