package com.tokopedia.review.feature.reputationhistory.view.adapter

import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.reputationhistory.view.adapter.viewholder.SellerReputationDateViewHolder
import com.tokopedia.review.feature.reputationhistory.view.adapter.viewholder.SellerReputationEmptyViewHolder
import com.tokopedia.review.feature.reputationhistory.view.adapter.viewholder.SellerReputationPenaltyViewHolder
import com.tokopedia.review.feature.reputationhistory.view.adapter.viewholder.SellerReputationShopViewHolder
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationDateUiModel
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationPenaltyUiModel
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationShopUiModel

class ReputationPenaltyAdapterFactory(private val fragment: Fragment,
                                      private val sellerReputationInfoListener: SellerReputationShopViewHolder.SellerReputationInfoListener): BaseAdapterTypeFactory(),
    ReputationPenaltyTypeFactory {

    override fun type(reputationDateUiModel: ReputationDateUiModel): Int {
        return SellerReputationDateViewHolder.LAYOUT
    }

    override fun type(reputationShopUiModel: ReputationShopUiModel): Int {
        return SellerReputationShopViewHolder.LAYOUT
    }

    override fun type(reputationPenaltyUiModel: ReputationPenaltyUiModel): Int {
        return SellerReputationPenaltyViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel?): Int {
        return SellerReputationEmptyViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            SellerReputationPenaltyViewHolder.LAYOUT -> SellerReputationPenaltyViewHolder(parent)
            SellerReputationShopViewHolder.LAYOUT -> SellerReputationShopViewHolder(parent, sellerReputationInfoListener)
            SellerReputationDateViewHolder.LAYOUT -> SellerReputationDateViewHolder(parent, fragment)
            SellerReputationEmptyViewHolder.LAYOUT -> SellerReputationEmptyViewHolder(parent, fragment)
            else -> super.createViewHolder(parent, type)
        }
    }

}