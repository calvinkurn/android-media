package com.tokopedia.affiliate.feature.onboarding.di;

import com.tokopedia.affiliate.feature.onboarding.domain.usecase.GetUsernameSuggestionUseCase;
import com.tokopedia.affiliate.feature.onboarding.view.listener.UsernameInputContract;
import com.tokopedia.affiliate.feature.onboarding.view.presenter.UsernameInputPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by milhamj on 10/4/18.
 */
@Module
public class OnboardingModule {
    @OnboardingScope
    @Provides
    UsernameInputContract.Presenter
    provideUsernameInputPresenter(GetUsernameSuggestionUseCase getUsernameSuggestionUseCase) {
        return new UsernameInputPresenter(getUsernameSuggestionUseCase);
    }
}
