package com.tokopedia.home_page_banner.presenter.adapter.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_page_banner.R
import com.tokopedia.home_page_banner.presenter.widgets.ShimmeringImageView

class HomePageBannerViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
    constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(R.layout.item_home_page_banner, parent, false))

    fun bind(imageUrl: String, clickListener: (Int) -> Unit){
        itemView.setOnClickListener { clickListener.invoke(adapterPosition - 1) }
        itemView.findViewById<ShimmeringImageView>(R.id.image)?.loadImage(imageUrl)
    }
}