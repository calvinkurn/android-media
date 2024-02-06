package com.tokopedia.buy_more_get_more.olp.presentation.adapter.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemGwpTierListBinding
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.utils.view.binding.viewBinding

class GwpTierListAdapter : RecyclerView.Adapter<GwpTierListAdapter.GwpTierListViewHolder>() {

    var tierList: List<OfferInfoForBuyerUiModel.Offering.Tier> = listOf()
    private var onTierClick: (OfferInfoForBuyerUiModel.Offering.Tier) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GwpTierListViewHolder {
        return GwpTierListViewHolder(parent.inflateLayout(R.layout.item_gwp_tier_list))
    }

    override fun getItemCount(): Int {
        return tierList.size
    }

    override fun onBindViewHolder(holder: GwpTierListViewHolder, position: Int) {
        holder.bindData(tierList[position], position)
    }

    fun setOnTierClick(onTierClick: (OfferInfoForBuyerUiModel.Offering.Tier) -> Unit) {
        this.onTierClick = onTierClick
    }

    inner class GwpTierListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var context: Context

        private val binding: ItemGwpTierListBinding? by viewBinding()
        private val giftImageListAdapter by lazy { GwpGiftImageListAdapter() }

        init {
            context = itemView.context
        }

        fun bindData(tierInfo: OfferInfoForBuyerUiModel.Offering.Tier, position: Int) {
            giftImageListAdapter.giftImageList = tierInfo.benefits.firstOrNull()?.products.orEmpty()
            giftImageListAdapter.setOnTierClick { onTierClick(tierInfo) }

            binding?.run {
                whiteOosOverlay.showWithCondition(tierInfo.isOOS)
                tpgOosGift.showWithCondition(tierInfo.isOOS)
                tpgGiftTier.text = MethodChecker.fromHtml(tierInfo.tierWording)
                rvGiftList.apply {
                    layoutManager = LinearLayoutManager(
                        itemView.context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    adapter = giftImageListAdapter
                }

                cardGiftTier.setOnClickListener { onTierClick(tierInfo) }
            }
        }
    }
}
