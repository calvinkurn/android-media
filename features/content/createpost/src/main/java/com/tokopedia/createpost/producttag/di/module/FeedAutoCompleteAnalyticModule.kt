package com.tokopedia.createpost.producttag.di.module

import com.tokopedia.createpost.producttag.analytic.autocomplete.FeedAutoCompleteAnalytic
import com.tokopedia.createpost.producttag.analytic.autocomplete.FeedAutoCompleteAnalyticImpl
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on May 24, 2022
 */
@Module
abstract class FeedAutoCompleteAnalyticModule {

    @Binds
    abstract fun bindFeedAutoCompleteAnalytic(analytic: FeedAutoCompleteAnalyticImpl): FeedAutoCompleteAnalytic
}