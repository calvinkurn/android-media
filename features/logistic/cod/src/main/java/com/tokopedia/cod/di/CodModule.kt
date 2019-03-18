package com.tokopedia.cod.di

import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import com.tokopedia.cod.view.CodContract
import com.tokopedia.cod.view.CodPresenter
import com.tokopedia.logisticanalytics.CodAnalytics
import dagger.Module
import dagger.Provides

/**
 * Created by fajarnuha on 29/12/18.
 */
@Module
class CodModule{

    @CodScope
    @Provides
    fun providePresenter(presenter: CodPresenter): CodContract.Presenter = presenter

    @CodScope
    @Provides
    fun provideAnalyticsTracker(router: AbstractionRouter): AnalyticTracker = router.analyticTracker

    @CodScope
    @Provides
    fun provideCodAnalytics(): CodAnalytics = CodAnalytics()

}