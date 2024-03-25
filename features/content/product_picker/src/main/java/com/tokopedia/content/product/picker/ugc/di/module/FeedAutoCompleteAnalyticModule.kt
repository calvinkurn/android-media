package com.tokopedia.content.product.picker.ugc.di.module

import com.tokopedia.content.product.picker.ugc.analytic.autocomplete.FeedAutoCompleteAnalytic
import com.tokopedia.content.product.picker.ugc.analytic.autocomplete.FeedAutoCompleteAnalyticImpl
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
