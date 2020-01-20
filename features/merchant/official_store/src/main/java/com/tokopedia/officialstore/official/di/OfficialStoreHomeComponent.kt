package com.tokopedia.officialstore.official.di

import com.tokopedia.officialstore.common.di.OfficialStoreComponent
import com.tokopedia.officialstore.official.presentation.OfficialHomeFragment
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import dagger.Component

@OfficialStoreHomeScope
@Component(modules = [OfficialStoreHomeModule::class, TopAdsWishlistModule::class], dependencies = [OfficialStoreComponent::class])
interface OfficialStoreHomeComponent {
    fun inject(view: OfficialHomeFragment)
}