package com.tokopedia.officialstore.official.di

import com.tokopedia.officialstore.common.di.OfficialStoreComponent
import com.tokopedia.officialstore.official.presentation.OfficialHomeFragment
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import dagger.Component

@OfficialStoreHomeScope
@Component(modules = [OfficialStoreHomeModule::class, RecommendationModule::class, TopAdsWishlistModule::class], dependencies = [OfficialStoreComponent::class])
interface OfficialStoreHomeComponent {
    fun inject(view: OfficialHomeFragment)
}