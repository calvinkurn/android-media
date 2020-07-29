package com.tokopedia.shop.review.di

import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.review.shop.view.ReviewShopFragment
import dagger.Component

/**
 * @author by nisie on 8/11/17.
 */
@ReputationScope
@Component(modules = [ReputationModule::class], dependencies = [ShopComponent::class])
interface ReputationComponent {
    fun inject(shopReviewFragment: ReviewShopFragment?)
}