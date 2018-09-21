package com.tokopedia.profile.di

import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.presenter.ProfilePresenter
import dagger.Binds
import dagger.Module

/**
 * @author by milhamj on 9/21/18.
 */
@Module
abstract class ProfileModule {
    @Binds
    abstract fun providePresenter(profilePresenter: ProfilePresenter): ProfileContract.Presenter
}