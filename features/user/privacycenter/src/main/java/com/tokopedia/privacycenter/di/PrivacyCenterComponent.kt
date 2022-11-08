package com.tokopedia.privacycenter.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.privacycenter.PrivacyCenterFragment
import dagger.Component

@ActivityScope
@Component(modules = [
    PrivacyCenterModule::class,
    PrivacyCenterViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface PrivacyCenterComponent {
    fun inject(fragment: PrivacyCenterFragment)
}
