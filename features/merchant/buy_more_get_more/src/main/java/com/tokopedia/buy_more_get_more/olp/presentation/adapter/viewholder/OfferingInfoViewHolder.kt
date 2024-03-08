package com.tokopedia.buy_more_get_more.olp.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemOlpOfferingInfoBinding
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.widget.GwpTierListAdapter
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.widget.TierListAdapter
import com.tokopedia.buy_more_get_more.olp.presentation.listener.OfferingInfoListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.R.color.Unify_Static_White
import com.tokopedia.utils.view.binding.viewBinding

class OfferingInfoViewHolder(
    itemView: View,
    val listener: OfferingInfoListener
) :
    AbstractViewHolder<OfferInfoForBuyerUiModel>(itemView) {

    private val binding: ItemOlpOfferingInfoBinding? by viewBinding()
    private val pdTierListAdapter by lazy { TierListAdapter() }
    private val gwpTierListAdapter by lazy { GwpTierListAdapter() }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_olp_offering_info
        private const val PD_TIER_ID = 1L
        private const val GWP_TIER_ID = 2L
    }

    override fun bind(data: OfferInfoForBuyerUiModel) {
        binding?.apply {
            tpgShopName.apply {
                text = data.offerings.firstOrNull()?.shopData?.shopName.orEmpty()
                setOnClickListener {
                    data.offerings.firstOrNull()?.shopData?.shopId?.let { shopId ->
                        listener.onShopNameClicked(
                            shopId
                        )
                    }
                }
            }
            if (data.offerings.firstOrNull()?.shopData?.badge?.isNotEmpty() == true) {
                ivShopBadge.apply {
                    visible()
                    setImageUrl(data.offerings.firstOrNull()?.shopData?.badge.orEmpty())
                }
            }
        }

        when (data.offerings.firstOrNull()?.offerTypeId) {
            PD_TIER_ID -> {
                setupTncProgressiveDiscount()
                setupTierListProgressiveDiscount(data)
            }
            GWP_TIER_ID -> {
                setupTncGwp()
                setupTierListGwp(data)
            }
        }
    }

    private fun setupTncProgressiveDiscount() {
        binding?.apply {
            tpgTnc.apply {
                visible()
                setOnClickListener { listener.onTncClicked() }
            }
            btnGwpTnc.gone()
        }
    }

    private fun setupTncGwp() {
        binding?.apply {
            tpgTnc.gone()
            btnGwpTnc.apply {
                visible()
                setOnClickListener { listener.onTncClicked() }
            }
        }
    }

    private fun setupTierListProgressiveDiscount(data: OfferInfoForBuyerUiModel) {
        pdTierListAdapter.let { adapter ->
            data.offerings.firstOrNull()?.tierList?.let { adapter.tierList = it }
        }
        binding?.run {
            cardPdTierInfo.apply {
                setCardUnifyBackgroundColor(
                    MethodChecker.getColor(
                        itemView.context,
                        Unify_Static_White
                    )
                )
                visible()
            }
            rvTierList.apply {
                layoutManager = LinearLayoutManager(
                    itemView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = pdTierListAdapter
            }
            rvGwpTierList.gone()
        }
    }

    private fun setupTierListGwp(data: OfferInfoForBuyerUiModel) {
        gwpTierListAdapter.let { adapter ->
            data.offerings.firstOrNull()?.tierList?.let { adapter.tierList = it }
            adapter.setOnTierClick { selectedTier ->
                listener.onTierClicked(selectedTier, data)
            }
        }
        binding?.run {
            rvGwpTierList.apply {
                layoutManager = LinearLayoutManager(
                    itemView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = gwpTierListAdapter
                visible()
            }
            cardPdTierInfo.gone()
        }
    }
}
