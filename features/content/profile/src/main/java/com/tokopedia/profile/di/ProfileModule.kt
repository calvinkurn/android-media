package com.tokopedia.profile.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.listener.ProfileEmptyContract
import com.tokopedia.profile.view.presenter.ProfileEmptyPresenter
import com.tokopedia.profile.view.presenter.ProfilePresenter
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides

/**
 * @author by milhamj on 9/21/18.
 */
@Module(includes = [(ProfileNetworkModule::class)])
class ProfileModule {
    @ProfileScope
    @Provides
    fun provideProfilePresenter(profilePresenter: ProfilePresenter): ProfileContract.Presenter {
        return profilePresenter
    }
    @ProfileScope
    @Provides
    fun provideProfileEmptyPresenter(profileEmptyPresenter: ProfileEmptyPresenter)
            : ProfileEmptyContract.Presenter {
        return profileEmptyPresenter
    }

    @ProfileScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }
}