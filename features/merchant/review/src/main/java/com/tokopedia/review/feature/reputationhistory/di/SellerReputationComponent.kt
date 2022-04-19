package com.tokopedia.review.feature.reputationhistory.di

import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.reputationhistory.view.fragment.SellerReputationPenaltyFragment
import dagger.Component

/**
 * Created by normansyahputa on 2/13/18.
 */
@SellerReputationScope
@Component(modules = [SellerReputationModule::class], dependencies = [ReviewComponent::class])
interface SellerReputationComponent {
    fun inject(sellerReputationPenaltyFragment: SellerReputationPenaltyFragment?)
}