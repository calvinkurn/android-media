package com.tokopedia.topads.dashboard.view.adapter.insight

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.top_ads_headline_usecase.model.TopAdsManageHeadlineInput2
import com.tokopedia.topads.common.data.internal.ParamObject.ACTION_CREATE
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordDetail
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsInsightShopKeywordViewHolder

class TopAdsShopKeywordRecommendationAdapter(
    private val context: Context,
    private var list: MutableList<RecommendedKeywordDetail>,
    private val type: Int,
    private val lstnr: (Int) -> Unit,
    private val error: (RecommendedKeywordDetail) -> Unit
) : RecyclerView.Adapter<TopAdsInsightShopKeywordViewHolder>() {

    var selectedItemsCount = list.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopAdsInsightShopKeywordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        val holder = TopAdsInsightShopKeywordViewHolder(context, view)
        holder.edtBid.textFieldInput.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val item = list[holder.adapterPosition]
                val inputBudget = holder.edtBid.textFieldInput.text.toString().toIntOrZero()
                item.priceBid = inputBudget
                holder.updateRecommBudget(item)
            }
        })
        return holder
    }

    override fun onBindViewHolder(holder: TopAdsInsightShopKeywordViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        if (item.priceBid == 0) item.priceBid = item.recommendedBid.toInt()
        holder.initView(type, item)

        holder.bindData(item)
        holder.checkBox.setOnClickListener {
            onCheckBoxClicked(item, holder.adapterPosition, false)
        }


    }

    private fun onCheckBoxClicked(item: RecommendedKeywordDetail, index: Int, isError: Boolean) {
        item.isChecked = !item.isChecked
        if (item.isChecked) selectedItemsCount++ else selectedItemsCount--
        lstnr.invoke(selectedItemsCount)
        if (isError) error.invoke(list[index])
        notifyItemChanged(index)
    }

    fun getSelectedKeywords(): MutableMap<Pair<Int, String>, MutableList<TopAdsManageHeadlineInput2.Operation.Group.KeywordOperation>>? {
        val groupMap =
            mutableMapOf<Pair<Int, String>, MutableList<TopAdsManageHeadlineInput2.Operation.Group.KeywordOperation>>()
        var isError = false
        list.forEachIndexed { index, it ->
            if (it.isChecked) {
                if (it.isError) {
                    isError = true
                    onCheckBoxClicked(it, index, true)
                } else {
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
        }
        if (isError) return null
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
            context: Context,
            list: MutableList<RecommendedKeywordDetail>,
            type: Int,
            lstnr: (Int) -> Unit,
            error: (RecommendedKeywordDetail) -> Unit
        ) = TopAdsShopKeywordRecommendationAdapter(context, list, type, lstnr, error)
    }

}