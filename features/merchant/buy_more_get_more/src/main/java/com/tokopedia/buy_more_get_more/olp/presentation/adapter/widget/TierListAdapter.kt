package com.tokopedia.buy_more_get_more.olp.presentation.adapter.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemTierListBinding
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.Offering.Tier
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visibleWithCondition
import com.tokopedia.utils.view.binding.viewBinding

class TierListAdapter : RecyclerView.Adapter<TierListAdapter.TierListViewHolder>() {

    var tierList: List<Tier> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TierListViewHolder {
        return TierListViewHolder(parent.inflateLayout(R.layout.item_tier_list))
    }

    override fun getItemCount(): Int {
        return tierList.size
    }

    override fun onBindViewHolder(holder: TierListViewHolder, position: Int) {
        holder.bindData(tierList[position], position)
    }

    inner class TierListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var context: Context

        private val binding: ItemTierListBinding? by viewBinding()

        init {
            context = itemView.context
        }

        fun bindData(tierInfo: Tier, position: Int) {
            binding?.run {
                tpgTierInfo.text =
                    MethodChecker.fromHtml(
                        context.getString(
                            R.string.bmgm_tier_info_placeholder,
                            tierInfo.rules.firstOrNull()?.value,
                            tierInfo.benefits.firstOrNull()?.value
                        )
                    )
                bulletSymbol.visibleWithCondition(tierList.lastIndex != position)
            }
        }
    }
}
