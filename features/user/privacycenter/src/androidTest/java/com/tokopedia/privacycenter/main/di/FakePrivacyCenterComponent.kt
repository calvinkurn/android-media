package com.tokopedia.privacycenter.main.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.privacycenter.common.di.PrivacyCenterComponent
import com.tokopedia.privacycenter.common.di.PrivacyCenterModule
import com.tokopedia.privacycenter.common.di.PrivacyCenterViewModelModule
import com.tokopedia.privacycenter.common.di.RecommendationModule
import dagger.Component


@ActivityScope
@Component(modules = [
    RecommendationModule::class,
    PrivacyCenterModule::class,
    PrivacyCenterViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface FakePrivacyCenterComponent: PrivacyCenterComponent {

}
