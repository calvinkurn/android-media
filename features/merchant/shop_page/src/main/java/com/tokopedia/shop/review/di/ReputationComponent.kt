package com.tokopedia.shop.review.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop.review.shop.view.ReviewShopFragment
import dagger.Component

/**
 * @author by nisie on 8/11/17.
 */
@ReputationScope
@Component(modules = [ReputationModule::class], dependencies = [BaseAppComponent::class])
interface ReputationComponent {
    fun inject(shopReviewFragment: ReviewShopFragment?)
}