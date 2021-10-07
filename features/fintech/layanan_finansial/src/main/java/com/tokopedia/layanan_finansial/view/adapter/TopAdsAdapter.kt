package com.tokopedia.layanan_finansial.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.layanan_finansial.R

class TopAdsAdapter():  RecyclerView.Adapter<TopAdsAdapter.TopAdsViewHolder>()  {
    class TopAdsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAdsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_topads_sdk, parent, false)
        return TopAdsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopAdsViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


}