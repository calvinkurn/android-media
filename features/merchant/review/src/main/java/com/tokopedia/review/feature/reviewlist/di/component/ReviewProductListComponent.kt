package com.tokopedia.review.feature.reviewlist.di.component

import com.tokopedia.review.common.di.ReviewSellerComponent
import com.tokopedia.review.feature.reviewlist.di.module.ReviewProductListModule
import com.tokopedia.review.feature.reviewlist.di.scope.ReviewProductListScope
import dagger.Component

@ReviewProductListScope
@Component(modules = [ReviewProductListModule::class], dependencies = [ReviewSellerComponent::class])
interface ReviewProductListComponent {}