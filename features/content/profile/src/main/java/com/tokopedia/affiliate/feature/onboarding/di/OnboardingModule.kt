package com.tokopedia.affiliate.feature.onboarding.di

import android.content.Context
import android.content.res.Resources

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.affiliate.feature.onboarding.view.listener.UsernameInputContract
import com.tokopedia.affiliate.feature.onboarding.view.presenter.UsernameInputPresenter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

import dagger.Module
import dagger.Provides

/**
 * @author by milhamj on 10/4/18.
 */
@Module
class OnboardingModule {
    @OnboardingScope
    @Provides
    fun provideUsernameInputPresenter(usernameInputPresenter: UsernameInputPresenter)
            : UsernameInputContract.Presenter {
        return usernameInputPresenter
    }

    @OnboardingScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context)
            : UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }
}
