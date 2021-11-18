package com.tokopedia.topads.dashboard.view.adapter.insight

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordDetail
import kotlinx.android.synthetic.main.topads_insight_keyword_recomm_item.view.*

class TopAdsInsightRecommKeywordsAdapter(private val list: List<RecommendedKeywordDetail>) :
    RecyclerView.Adapter<TopAdsInsightRecommKeywordsAdapter.Companion.KeywordsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return KeywordsViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeywordsViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        with(holder.view) {
            txtTitle.text = item.keywordTag
            textGroupName.text = item.groupName
            txtCurrentCost.text = String.format(resources.getString(R.string.per_click_value),110)
            txtNewFee.text =
                Html.fromHtml(String.format(resources.getString(R.string.per_click_bold_value),100))
            txtFooter.text =
                Html.fromHtml(String.format(resources.getString(R.string.max_times_month),500))
            btnEditFee.setOnClickListener {
                feeGroup.hide()
                edtNewFee.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    companion object {
        class KeywordsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        }

        private val layout = R.layout.topads_insight_keyword_recomm_item

        fun createInstance(list: List<RecommendedKeywordDetail>) =
            TopAdsInsightRecommKeywordsAdapter(list)
    }
}