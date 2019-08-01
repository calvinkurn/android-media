package com.tokopedia.logisticcart.cod.di

import com.tokopedia.logisticcart.cod.view.CodContract
import com.tokopedia.logisticcart.cod.view.CodPresenter
import com.tokopedia.logisticdata.data.analytics.CodAnalytics
import dagger.Module
import dagger.Provides

/**
 * Created by fajarnuha on 29/12/18.
 */
@Module
class CodModule {

    @CodScope
    @Provides
    fun providePresenter(presenter: CodPresenter): CodContract.Presenter = presenter

    @CodScope
    @Provides
    fun provideCodAnalytics(): CodAnalytics = CodAnalytics()

}