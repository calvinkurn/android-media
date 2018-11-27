package com.tokopedia.interestpick.di

import com.tokopedia.interestpick.view.listener.InterestPickContract
import com.tokopedia.interestpick.view.presenter.InterestPickPresenter
import dagger.Binds
import dagger.Module

/**
 * @author by milhamj on 07/09/18.
 */

@Module
abstract class InterestPickModule {
    @InterestPickScope
    @Binds
    abstract fun provideInterestPickPresenter(interestPickPresenter: InterestPickPresenter)
            : InterestPickContract.Presenter
}
