package com.tokopedia.thankyou_native.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.thankyou_native.R
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.widget.TopAdsImageView
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.thanks_item_top_ads_view.view.*

class TopAdsViewAdapter(val topAdsImageViewModel: ArrayList<TopAdsImageViewModel>,
                        val onclick : (TopAdsImageViewModel)->Unit) :
    RecyclerView.Adapter<TopAdsViewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAdsViewViewHolder {
        val parentWidth = parent.width
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.thanks_item_top_ads_view, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.width = parentWidth - 16.toPx()
        return TopAdsViewViewHolder(view, onclick)
    }

    override fun onBindViewHolder(holder: TopAdsViewViewHolder, position: Int) {
        holder.bind(topAdsImageViewModel[position])
    }

    override fun getItemCount(): Int  = topAdsImageViewModel.size

    fun addItems(topAdsImageViewModelList: ArrayList<TopAdsImageViewModel>) {
        this.topAdsImageViewModel.addAll(topAdsImageViewModelList)
    }

}

class TopAdsViewViewHolder(itemView: View,
                           val onclick : (TopAdsImageViewModel) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val topAdsImageView : TopAdsImageView = itemView.adsTopAdsImageView
    fun bind(topAdsImageViewModel: TopAdsImageViewModel){
        topAdsImageView.loadImage(topAdsImageViewModel,8.toPx())
        topAdsImageView.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
            override fun onTopAdsImageViewClicked(applink: String?) {
                onclick(topAdsImageViewModel)
            }
        })
    }
}

