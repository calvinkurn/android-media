package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordDetail
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsInsightShopKeywordViewHolder
import kotlinx.android.synthetic.main.topads_insight_keyword_recomm_item.view.*

class TopAdsInsightShopKeywordRecommAdapter(
    private val list: List<RecommendedKeywordDetail>,
    private val type: Int,
    private val lstnr: (Int) -> Unit
) : RecyclerView.Adapter<TopAdsInsightShopKeywordViewHolder>() {

    private var selectedItemsCount = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAdsInsightShopKeywordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return TopAdsInsightShopKeywordViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopAdsInsightShopKeywordViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        holder.initView(type,item)

        holder.bindData(type)

        holder.addListeners { checkBox ->
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) selectedItemsCount++ else selectedItemsCount--
                item.isChecked = isChecked
                lstnr.invoke(selectedItemsCount)
            }
        }
    }

    fun checkAllItems() {
        list.forEach { it.isChecked = true }
        notifyDataSetChanged()
    }

    fun unCheckAllItems() {
        list.forEach { it.isChecked = false }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    companion object {
        private val layout = R.layout.topads_insight_keyword_recomm_item

        fun createInstance(
            list: List<RecommendedKeywordDetail>,
            type: Int,
            lstnr: (Int) -> Unit
        ) = TopAdsInsightShopKeywordRecommAdapter(list, type, lstnr)
    }

}