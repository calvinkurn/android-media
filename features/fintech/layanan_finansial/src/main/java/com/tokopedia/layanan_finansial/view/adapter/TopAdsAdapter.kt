package com.tokopedia.layanan_finansial.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.layanan_finansial.R
import com.tokopedia.layanan_finansial.view.models.TopAdsImageModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.widget.TopAdsImageView

class TopAdsAdapter(
    private val topAdsModelList: List<TopAdsImageModel>,
    private val onclick: (appLink: String) -> Unit
) : RecyclerView.Adapter<TopAdsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAdsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_topads_sdk, parent, false)
        return TopAdsViewHolder(view, onclick)
    }

    override fun onBindViewHolder(holder: TopAdsViewHolder, position: Int) {
        holder.bind(topAdsModelList[position] as TopAdsImageViewModel)

    }

    override fun getItemCount(): Int {
        return topAdsModelList.size
    }


}


class TopAdsViewHolder(itemView: View, val onclick: (appLink: String) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    fun bind(topAdsImageViewModel: TopAdsImageViewModel) {
        imageModel.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
            override fun onTopAdsImageViewClicked(applink: String?) {
                applink?.let {
                    onclick(it)
                }

            }

        })
        imageModel.loadImage(topAdsImageViewModel)
    }

    private val imageModel = itemView.findViewById<TopAdsImageView>(R.id.topAdsBanner)


}