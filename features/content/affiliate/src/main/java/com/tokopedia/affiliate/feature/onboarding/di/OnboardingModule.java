package com.tokopedia.affiliate.feature.onboarding.di;

import com.tokopedia.affiliate.feature.onboarding.view.listener.UsernameInputContract;
import com.tokopedia.affiliate.feature.onboarding.view.presenter.UsernameInputPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * @author by milhamj on 10/4/18.
 */
@Module
public abstract class OnboardingModule {
    @OnboardingScope
    @Binds
    abstract UsernameInputContract.Presenter providePresenter(UsernameInputPresenter presenter);
}
