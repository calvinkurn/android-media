package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel
import com.tokopedia.unifycomponents.ChipsUnify

class InsightTypeChipsViewHolder(
    val itemView: View,
    private val onInsightTypeChipClick: ((MutableList<InsightListUiModel>?) -> Unit)?
) :
    AbstractViewHolder<InsightTypeChipsUiModel>(itemView) {

    inner class InsightTypeChipsAdapter :
        RecyclerView.Adapter<InsightTypeChipsAdapter.InsightChipsViewHolder>() {
        var insightChipsList: List<String> = mutableListOf()
        var adGroupList: MutableList<InsightListUiModel> = mutableListOf()

        inner class InsightChipsViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val chips: ChipsUnify = itemView.findViewById(R.id.groupInsightChips)
            fun bind(chipName: String) {
                chips.chipText = chipName
                chips.chip_right_icon.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        com.tokopedia.iconunify.R.drawable.iconunify_chevron_down
                    )
                )
                chips.setChevronClickListener {
                    if(chipName == getString(R.string.topads_insight_ad_type_product) || chipName == getString(R.string.topads_insight_ad_type_shop)) {
                        onInsightTypeChipClick?.invoke(null)
                    } else {
                        onInsightTypeChipClick?.invoke(adGroupList)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsightChipsViewHolder {
            return InsightChipsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.top_ads_group_detail_insight_type_list_item_layout,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: InsightChipsViewHolder, position: Int) {
            holder.bind(insightChipsList[position])
        }

        override fun getItemCount(): Int {
            return insightChipsList.count()
        }
    }

    private val adapter by lazy { InsightTypeChipsAdapter() }
    private val insightTypeChipsRv: RecyclerView = itemView.findViewById(R.id.insightTypeChipsRv)

    override fun bind(element: InsightTypeChipsUiModel?) {
        insightTypeChipsRv.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        insightTypeChipsRv.adapter = adapter
        element?.let {
            adapter.insightChipsList = element.chipsList
            adapter.adGroupList = element.adGroupList
        }
    }

    companion object {
        val LAYOUT = R.layout.top_ads_group_detail_insight_type_item_layout
    }
}
