package com.tokopedia.thankyou_native.recommendationdigital.di.component

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.thankyou_native.di.module.ViewModelModule
import com.tokopedia.thankyou_native.recommendationdigital.di.module.DigitalRecommendationModule
import com.tokopedia.thankyou_native.recommendationdigital.di.scope.DigitalRecommendationScope
import com.tokopedia.thankyou_native.recommendationdigital.di.module.GqlQueryModule
import com.tokopedia.thankyou_native.recommendationdigital.presentation.view.DigitalRecommendation
import dagger.Component

@DigitalRecommendationScope
@Component(modules =
[DigitalRecommendationModule::class,
    ViewModelModule::class,
    GqlQueryModule::class],
        dependencies = [BaseAppComponent::class])
interface ThankYouPageComponent {
    @ApplicationContext
    fun context(): Context

    fun inject(view: DigitalRecommendation)
}