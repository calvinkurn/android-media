package com.tokopedia.profile.di

import com.tokopedia.feedcomponent.di.FeedComponentModule
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.listener.ProfileEmptyContract
import com.tokopedia.profile.view.presenter.ProfileEmptyPresenter
import com.tokopedia.profile.view.presenter.ProfilePresenter
import dagger.Module
import dagger.Provides

/**
 * @author by milhamj on 9/21/18.
 */
@Module(includes = [ProfileNetworkModule::class, FeedComponentModule::class])
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

}