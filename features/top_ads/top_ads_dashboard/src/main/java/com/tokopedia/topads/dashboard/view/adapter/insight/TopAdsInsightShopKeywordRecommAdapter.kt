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
    private val lstnr: (Boolean) -> Unit
) : RecyclerView.Adapter<TopAdsInsightShopKeywordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAdsInsightShopKeywordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return TopAdsInsightShopKeywordViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopAdsInsightShopKeywordViewHolder, position: Int) {
        holder.initView(type)
        val item = list[holder.adapterPosition]

        holder.bindData(item)

        with(holder.view) {
            btnEditFee.setOnClickListener {
                openEditTextFee()
            }
            setOnClickListener {
                closeEditTextFee()
            }
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                lstnr.invoke(isChecked)
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

    private fun View.closeEditTextFee() {
        txtSubTitle2Value.show()
        btnEditFee.show()
        edtBid.hide()
    }

    private fun View.openEditTextFee() {
        txtSubTitle2Value.invisible()
        btnEditFee.invisible()
        edtBid.show()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    companion object {
        private val layout = R.layout.topads_insight_keyword_recomm_item

        fun createInstance(
            list: List<RecommendedKeywordDetail>,
            type: Int,
            lstnr: (Boolean) -> Unit
        ) = TopAdsInsightShopKeywordRecommAdapter(list, type, lstnr)
    }

}