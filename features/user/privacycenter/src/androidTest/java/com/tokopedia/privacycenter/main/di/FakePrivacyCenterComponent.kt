package com.tokopedia.privacycenter.main.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.privacycenter.di.PrivacyCenterComponent
import com.tokopedia.privacycenter.di.PrivacyCenterModule
import com.tokopedia.privacycenter.di.PrivacyCenterViewModelModule
import com.tokopedia.privacycenter.di.RecommendationModule
import dagger.Component

@ActivityScope
@Component(
    modules = [
        RecommendationModule::class,
        PrivacyCenterModule::class,
        PrivacyCenterViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface FakePrivacyCenterComponent : PrivacyCenterComponent
