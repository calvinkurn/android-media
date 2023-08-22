package com.tokopedia.buy_more_get_more.olp.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemOlpOfferingInfoBinding
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.widget.TierListAdapter
import com.tokopedia.buy_more_get_more.olp.presentation.listener.OfferingInfoListener
import com.tokopedia.utils.view.binding.viewBinding

class OfferingInfoViewHolder(
    itemView: View,
    val listener: OfferingInfoListener) :
    AbstractViewHolder<OfferInfoForBuyerUiModel>(itemView) {

    private val binding: ItemOlpOfferingInfoBinding? by viewBinding()
    private val tierListAdapter by lazy { TierListAdapter() }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_olp_offering_info
    }

    override fun bind(data: OfferInfoForBuyerUiModel) {
        binding?.apply {
            tpgShopName.text = data.offerings.firstOrNull()?.offerName.orEmpty()
            ivShopBadge.setImageUrl(data.offerings.firstOrNull()?.shopData?.badge.orEmpty())
            tpgTnc.setOnClickListener { listener.onTncClicked() }
        }
        setupTierList(data)
    }

    private fun setupTierList(data: OfferInfoForBuyerUiModel) {
        tierListAdapter.let { adapter ->
            data.offerings.firstOrNull()?.tierList?.let { adapter.tierList = it }
        }
        binding?.run {
            cardTierInfo.setCardUnifyBackgroundColor(
                MethodChecker.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_TN50
                )
            )
            rvTierList.apply {
                layoutManager = LinearLayoutManager(
                    itemView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = tierListAdapter
            }
        }
    }
}
