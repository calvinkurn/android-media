package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.SeeMorePdpDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.listener.PdpViewListener

class SeeMorePdpViewHolder(view: View, private val listener: PdpViewListener) : AbstractViewHolder<SeeMorePdpDataModel>(view){

    private val container: View by lazy { view.findViewById<View>(R.id.container_banner_mix_more) }
    private val bannerBackgroundImage: ImageView by lazy { view.findViewById<ImageView>(R.id.background_banner_mix_more)}

    override fun bind(element: SeeMorePdpDataModel) {
        bannerBackgroundImage.setOnClickListener {
            RouteManager.route(itemView.context, element.applink)
            listener.onSetTrackerClickSeeMore()
        }
        Glide.with(itemView.context)
                .load(element.backgroundImage)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(bannerBackgroundImage)
        container.setOnClickListener {
            RouteManager.route(itemView.context, element.applink)
            listener.onSetTrackerClickSeeMore()
        }
    }

    companion object{
        val LAYOUT = R.layout.home_banner_item_carousel_see_more
    }
}