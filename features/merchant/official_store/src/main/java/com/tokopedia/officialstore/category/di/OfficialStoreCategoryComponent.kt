package com.tokopedia.officialstore.category.di

import com.tokopedia.officialstore.category.presentation.fragment.OfficialHomeContainerFragment
import com.tokopedia.officialstore.common.di.OfficialStoreComponent
import dagger.Component

@OfficialStoreCategoryScope
@Component(modules = [OfficialStoreCategoryModule::class], dependencies = [OfficialStoreComponent::class])
interface OfficialStoreCategoryComponent {
    fun inject(view: OfficialHomeContainerFragment)
}