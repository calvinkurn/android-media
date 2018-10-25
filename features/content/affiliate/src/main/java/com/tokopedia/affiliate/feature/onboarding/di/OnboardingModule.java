package com.tokopedia.affiliate.feature.onboarding.di;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliate.feature.onboarding.view.listener.RecommendProductContract;
import com.tokopedia.affiliate.feature.onboarding.view.listener.UsernameInputContract;
import com.tokopedia.affiliate.feature.onboarding.view.presenter.RecommendProductPresenter;
import com.tokopedia.affiliate.feature.onboarding.view.presenter.UsernameInputPresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * @author by milhamj on 10/4/18.
 */
@Module
public class OnboardingModule {
    @OnboardingScope
    @Provides
    UsernameInputContract.Presenter provideUsernameInputPresenter(
            UsernameInputPresenter usernameInputPresenter) {
        return usernameInputPresenter;
    }

    @OnboardingScope
    @Provides
    RecommendProductContract.Presenter provideRecommendProductPresenter(
            RecommendProductPresenter recommendProductPresenter) {
        return recommendProductPresenter;
    }

    @OnboardingScope
    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    Resources provideResources(@ApplicationContext Context context) {
        return context.getResources();
    }
}
