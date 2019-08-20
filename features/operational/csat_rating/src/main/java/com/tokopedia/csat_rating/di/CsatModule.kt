package com.tokopedia.csat_rating.di

import com.tokopedia.csat_rating.ProvideRatingContract
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentPresenter

import dagger.Module
import dagger.Provides

@Module
class CsatModule {
    @Provides
    fun provideRatingPresenter(): ProvideRatingContract.ProvideRatingPresenter {
        return BaseProvideRatingFragmentPresenter()
    }
}
