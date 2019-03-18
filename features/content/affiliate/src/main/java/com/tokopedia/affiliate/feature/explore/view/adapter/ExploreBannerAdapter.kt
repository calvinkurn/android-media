package com.tokopedia.affiliate.feature.explore.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreBannerChildViewModel
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_af_banner_child.view.*

/**
 * @author by milhamj on 18/03/19.
 */

class ExploreBannerAdapter: RecyclerView.Adapter<ExploreBannerAdapter.ViewHolder>() {

    val list: MutableList<ExploreBannerChildViewModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_af_banner_child, parent, false)
        return ViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = list[position]
        val itemView = holder.itemView

        itemView.banner.loadImage(element.imageUrl)
        itemView.setOnClickListener {
            RouteManager.route(itemView.context, element.redirectUrl)
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}