package com.tokopedia.officialstore.presentation.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.officialstore.presentation.OfficialHomeFragment
import dagger.Component

@Component(modules = [OfficialHomeModule::class], dependencies = [BaseAppComponent::class])
@OfficialHomeScope
interface OfficialHomeComponent {
    fun inject(fragment: OfficialHomeFragment)
}
