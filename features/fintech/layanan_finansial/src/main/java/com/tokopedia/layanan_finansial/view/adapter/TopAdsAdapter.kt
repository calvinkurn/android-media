package com.tokopedia.layanan_finansial.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.layanan_finansial.R
import com.tokopedia.layanan_finansial.view.models.TopAdsImageModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.widget.TopAdsImageView

class TopAdsAdapter(private val topAdsModelList: List<TopAdsImageModel>):  RecyclerView.Adapter<TopAdsAdapter.TopAdsViewHolder>()  {
    class TopAdsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageModel = itemView.findViewById<TopAdsImageView>(R.id.topAdsBanner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAdsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_topads_sdk, parent, false)
        return TopAdsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopAdsViewHolder, position: Int) {
       holder.imageModel.loadImage(topAdsModelList[position] as TopAdsImageViewModel)
    }

    override fun getItemCount(): Int {
       return topAdsModelList.size
    }


}