package com.tokopedia.changephonenumber.di.input;

import com.tokopedia.changephonenumber.domain.interactor.ValidateNumberUseCase;
import com.tokopedia.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;
import com.tokopedia.changephonenumber.view.presenter.ChangePhoneNumberInputPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by alvinatin on 01/10/18.
 */

@Module
public class ChangePhoneNumberInputModule {
    @ChangePhoneNumberInputScope
    @Provides
    ChangePhoneNumberInputFragmentListener.Presenter provideChangePhoneNumberInputPresenter
            (ValidateNumberUseCase validateNumberUseCase) {
        return new ChangePhoneNumberInputPresenter(validateNumberUseCase);
    }
}
