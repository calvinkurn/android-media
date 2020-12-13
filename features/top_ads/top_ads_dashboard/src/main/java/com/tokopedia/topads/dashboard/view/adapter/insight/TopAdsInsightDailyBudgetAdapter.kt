package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R

/**
 * Created by Pika on 9/12/20.
 */

class TopAdsInsightDailyBudgetAdapter : RecyclerView.Adapter<TopAdsInsightDailyBudgetAdapter.ViewHolder>(){

    class ViewHolder(val view: View):RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topads_dash_insight_pos_key_item_layout, parent, false)
        return TopAdsInsightDailyBudgetAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}