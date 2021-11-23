package com.tokopedia.topads.dashboard.view.adapter.insight

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordDetail
import kotlinx.android.synthetic.main.topads_insight_keyword_recomm_item.view.*

class TopAdsInsightRecommKeywordsAdapter(
    private val list: List<RecommendedKeywordDetail>,
    private val type: Int
) : RecyclerView.Adapter<TopAdsInsightRecommKeywordsAdapter.Companion.KeywordsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return KeywordsViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeywordsViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        with(holder.view) {
            initView()

            txtTitle.text = item.keywordTag
            textGroupName.text = item.groupName
            txtSubTitle1Value.text =
                String.format(resources.getString(R.string.per_click_value), 110)
            txtSubTitle2Value.text =
                Html.fromHtml(
                    String.format(resources.getString(R.string.per_click_bold_value), 100)
                )
            txtFooter.text =
                Html.fromHtml(String.format(resources.getString(R.string.max_times_month), 500))

            btnEditFee.setOnClickListener {
                openEditTextFee()
            }
            setOnClickListener { closeEditTextFee() }
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                itemSelectedListener?.invoke(item, isChecked)
            }
        }
    }

    private fun View.initView() {
        when (type) {
            TopAdsInsightConstants.BID_KEYWORD -> {
                searchGroup.hide()
            }
            TopAdsInsightConstants.NEW_KEYWORD -> {
                (txtSubTitle2.layoutParams as ConstraintLayout.LayoutParams)
                    .startToEnd = R.id.guideline

                newKeywordGroup.hide()
                searchGroup.show()


                txtNoOfSearches.text =
                    Html.fromHtml(String.format(resources.getString(R.string.no_of_searches), 10))
            }
            TopAdsInsightConstants.NEGATIVE_KEYWORD -> {
                searchGroup.hide()
                btnEditFee.hide()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun View.closeEditTextFee() {
        feeGroup.show()
        edtBid.hide()
    }

    private fun View.openEditTextFee() {
        feeGroup.invisible()
        edtBid.show()
    }

    var itemSelectedListener: ((RecommendedKeywordDetail, Boolean) -> Unit)? = null

    companion object {
        class KeywordsViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        private val layout = R.layout.topads_insight_keyword_recomm_item

        fun createInstance(
            list: List<RecommendedKeywordDetail>,
            type: Int
        ) = TopAdsInsightRecommKeywordsAdapter(list, type)
    }

}