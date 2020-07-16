package com.tokopedia.reviewseller.common.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.reviewseller.common.di.module.ReviewSellerModule
import com.tokopedia.reviewseller.common.di.scope.ReviewSellerScope
import com.tokopedia.reviewseller.feature.inboxreview.presentation.fragment.InboxReviewFragment
import com.tokopedia.reviewseller.feature.reviewlist.view.fragment.RatingProductFragment
import dagger.Component

/**
 * @author by Rafli Syam on 02-27-2020
 */
@ReviewSellerScope
@Component(modules = [ReviewSellerModule::class], dependencies = [BaseAppComponent::class])
interface ReviewSellerComponent {

    fun inject(ratingProductFragment: RatingProductFragment)
    fun inject(inboxReviewFragment: InboxReviewFragment)

}