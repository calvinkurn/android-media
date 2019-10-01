package com.tokopedia.officialstore.official.di

import com.tokopedia.officialstore.common.di.OfficialStoreComponent
import com.tokopedia.officialstore.official.presentation.OfficialHomeFragment
import dagger.Component

@OfficialStoreHomeScope
@Component(modules = [OfficialStoreHomeModule::class], dependencies = [OfficialStoreComponent::class])
interface OfficialStoreHomeComponent {
    fun inject(view: OfficialHomeFragment)
}