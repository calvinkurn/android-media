package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_EDIT_OPTION
import com.tokopedia.topads.common.data.response.KeywordEditInput
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordDetail
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsInsightShopKeywordViewHolder

class TopAdsShopKeywordRecommendationAdapter(
    private val list: List<RecommendedKeywordDetail>,
    private val type: Int,
    private val lstnr: (Int) -> Unit
) : RecyclerView.Adapter<TopAdsInsightShopKeywordViewHolder>() {

    var selectedItemsCount = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAdsInsightShopKeywordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return TopAdsInsightShopKeywordViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopAdsInsightShopKeywordViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        holder.initView(type, item)

        holder.bindData(type)

        holder.addListeners { checkBox ->
            checkBox.setOnClickListener {
                item.isChecked = !item.isChecked
                if (item.isChecked) selectedItemsCount++ else selectedItemsCount--
                lstnr.invoke(selectedItemsCount)
            }
        }
    }

    fun getSelectedList(): List<KeywordEditInput> {
        val selectedList = mutableListOf<KeywordEditInput>()

        list.forEach {
            if (it.isChecked) {
                selectedList.add(KeywordEditInput(
                    PARAM_EDIT_OPTION,
                    KeywordEditInput.Keyword(
                        it.groupId.toString(),
                        "",
                        "",
                        it.keywordTag,
                        it.priceBid,
                        ""
                    ))
                )
            }
        }

        return selectedList
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
        ) = TopAdsShopKeywordRecommendationAdapter(list, type, lstnr)
    }

}