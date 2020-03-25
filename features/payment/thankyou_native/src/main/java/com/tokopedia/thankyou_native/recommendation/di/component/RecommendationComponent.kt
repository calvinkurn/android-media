package com.tokopedia.thankyou_native.recommendation.di.component

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.thankyou_native.recommendation.di.module.DispatcherModule
import com.tokopedia.thankyou_native.recommendation.di.module.GqlQueryModule
import com.tokopedia.thankyou_native.recommendation.di.module.RecommendationModule
import com.tokopedia.thankyou_native.recommendation.di.module.ViewModelModule
import com.tokopedia.thankyou_native.recommendation.di.scope.RecommendationScope
import com.tokopedia.thankyou_native.recommendation.presentation.view.PDPThankYouPageView
import dagger.Component

@RecommendationScope
@Component(modules = [DispatcherModule::class,
    GqlQueryModule::class,
    RecommendationModule::class,
    ViewModelModule::class], dependencies = [BaseAppComponent::class])
interface RecommendationComponent{
    @ApplicationContext
    fun context(): Context

    fun inject(view: PDPThankYouPageView)
}