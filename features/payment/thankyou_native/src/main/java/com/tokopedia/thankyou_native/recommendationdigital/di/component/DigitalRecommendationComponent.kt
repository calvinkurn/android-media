package com.tokopedia.thankyou_native.recommendationdigital.di.component

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.digital.digital_recommendation.di.DigitalRecommendationViewModelModule
import com.tokopedia.thankyou_native.recommendationdigital.di.module.DigitalRecommendationModule
import com.tokopedia.thankyou_native.recommendationdigital.di.module.DigitalViewModelModule
import com.tokopedia.thankyou_native.recommendationdigital.di.module.GqlQueryModule
import com.tokopedia.thankyou_native.recommendationdigital.di.scope.DigitalRecommendationScope
import com.tokopedia.thankyou_native.recommendationdigital.presentation.view.DigitalRecommendation
import dagger.Component

@DigitalRecommendationScope
@Component(modules =
[DigitalRecommendationModule::class,
    DigitalViewModelModule::class,
    GqlQueryModule::class, DigitalRecommendationViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface DigitalRecommendationComponent {
    @ApplicationContext
    fun context(): Context
    fun inject(view: DigitalRecommendation)
}