package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.top_ads_headline.data.TopAdsManageHeadlineInput2
import com.tokopedia.topads.common.data.internal.ParamObject.ACTION_CREATE
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordDetail
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsInsightShopKeywordViewHolder

class TopAdsShopKeywordRecommendationAdapter(
    private var list: MutableList<RecommendedKeywordDetail>,
    private val type: Int,
    private val lstnr: (Int) -> Unit,
    private val error: (Int) -> Unit
) : RecyclerView.Adapter<TopAdsInsightShopKeywordViewHolder>() {

    var selectedItemsCount = list.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopAdsInsightShopKeywordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return TopAdsInsightShopKeywordViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopAdsInsightShopKeywordViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        if (item.priceBid == 0) item.priceBid = item.recommendedBid.toInt()
        holder.initView(type, item)

        holder.bindData(type)

        holder.addListeners { checkBox, edtBid ->
            checkBox.setOnClickListener {
                onCheckBoxClicked(item, holder.adapterPosition,false)
            }
            edtBid.textFieldInput.afterTextChanged {
                val inputBudget = it.toIntOrZero()
                holder.updateRecommBudget(inputBudget)
                item.priceBid = inputBudget
            }
        }
    }

    private fun onCheckBoxClicked(item: RecommendedKeywordDetail, index: Int,isError: Boolean) {
        item.isChecked = !item.isChecked
        if (item.isChecked) selectedItemsCount++ else selectedItemsCount--
        lstnr.invoke(selectedItemsCount)
        notifyItemChanged(index)
        if(isError) error.invoke(index)
    }

    fun getSelectedKeywords(): MutableMap<Pair<Int, String>, MutableList<TopAdsManageHeadlineInput2.Operation.Group.KeywordOperation>>? {
        val groupMap =
            mutableMapOf<Pair<Int, String>, MutableList<TopAdsManageHeadlineInput2.Operation.Group.KeywordOperation>>()
        list.forEachIndexed { index, it ->
            if (it.isChecked) {
                if (it.isError) {
                    onCheckBoxClicked(it, index,true)
                    return null
                }

                val keyword = TopAdsManageHeadlineInput2.Operation.Group.KeywordOperation(
                    action = ACTION_CREATE,
                    TopAdsManageHeadlineInput2.Operation.Group.KeywordOperation.Keyword(
                        type = "positive_phrase",
                        status = "active",
                        tag = it.keywordTag,
                        priceBid = it.priceBid.toLong()
                    )
                )
                val pair = it.groupID to it.groupName
                if (groupMap.containsKey(pair)) {
                    groupMap[pair]!!.add(keyword)
                } else {
                    groupMap[pair] = mutableListOf(keyword)
                }
            }
        }
        return groupMap
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
            list: MutableList<RecommendedKeywordDetail>,
            type: Int,
            lstnr: (Int) -> Unit,
            error: (Int) -> Unit
        ) = TopAdsShopKeywordRecommendationAdapter(list, type, lstnr,error)
    }

}