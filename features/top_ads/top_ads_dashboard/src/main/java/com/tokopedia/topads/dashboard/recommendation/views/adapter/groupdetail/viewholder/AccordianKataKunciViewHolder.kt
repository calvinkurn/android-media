package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewPositiveKeywordsRecom
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKataKunciUiModel

class AccordianKataKunciViewHolder(private val itemView: View) :
    AbstractViewHolder<AccordianKataKunciUiModel>(itemView) {

    inner class KataKunciItemsAdapter :
        RecyclerView.Adapter<KataKunciItemsAdapter.KataKunciItemsViewHolder>() {
        var kataKunciItemList: List<NewPositiveKeywordsRecom> = mutableListOf()

        inner class KataKunciItemsViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val title : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.title)
            private val searchesCount : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.searches_count_value)
            private val potentialCount : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.show_potential_value)
            fun bind(element: NewPositiveKeywordsRecom) {
                title.text = element.keywordTag
                searchesCount.text = element.totalSearch
                potentialCount.text = element.predictedImpression
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KataKunciItemsViewHolder {
            return KataKunciItemsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.topads_insights_accordian_kata_kunci_item_layout,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: KataKunciItemsViewHolder, position: Int) {
            holder.bind(kataKunciItemList[position])
        }

        override fun getItemCount(): Int {
            return kataKunciItemList.count()
        }
    }

    private val adapter by lazy { KataKunciItemsAdapter() }
    private val kataKunciRv: RecyclerView = itemView.findViewById(R.id.kataKunciRv)

    override fun bind(element: AccordianKataKunciUiModel?) {
        kataKunciRv.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        kataKunciRv.adapter = adapter
        element?.newPositiveKeywordsRecom?.let {
            adapter.kataKunciItemList = it
        }
    }

    companion object {
        val LAYOUT = R.layout.top_ads_accordian_kata_kunci_layout
    }
}
