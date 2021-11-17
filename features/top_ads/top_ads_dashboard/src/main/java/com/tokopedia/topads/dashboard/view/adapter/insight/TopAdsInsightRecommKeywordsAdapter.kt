package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.dashboard.R
import kotlinx.android.synthetic.main.topads_insight_keyword_recomm_item.view.*

class TopAdsInsightRecommKeywordsAdapter : RecyclerView.Adapter<TopAdsInsightRecommKeywordsAdapter.Companion.KeywordsViewHolder>() {

    private val list = listOf<Int>(1,2,3)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout,parent,false)
        return KeywordsViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeywordsViewHolder, position: Int) {
        with(holder.view) {
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
        class KeywordsViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        }
        private val layout = R.layout.topads_insight_keyword_recomm_item
    }
}