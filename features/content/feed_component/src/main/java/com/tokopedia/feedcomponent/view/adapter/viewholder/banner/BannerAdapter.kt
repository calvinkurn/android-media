package com.tokopedia.feedcomponent.view.adapter.viewholder.banner

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerItemViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_banner_item.view.*

/**
 * @author by milhamj on 08/05/18.
 */

class BannerAdapter(private val itemViewModels: List<BannerItemViewModel>,
                    private val positionInFeed: Int,
                    private var listener: BannerItemListener)
    : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_banner_item, parent, false)
        return ViewHolder(itemLayoutView, positionInFeed, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(itemViewModels[position])
    }

    override fun getItemCount(): Int {
        return itemViewModels.size
    }

    class ViewHolder(v: View, private val positionInFeed: Int, val listener: BannerItemListener)
        : RecyclerView.ViewHolder(v) {

        fun bind(element: BannerItemViewModel) {
            itemView.banner.loadImage(element.imageUrl)
            itemView.setOnClickListener {
                listener.onBannerItemClick(positionInFeed, adapterPosition, element.redirectUrl)
            }
        }
    }

    interface BannerItemListener {
        fun onBannerItemClick(positionInFeed: Int, adapterPosition: Int, redirectUrl: String)
    }
}
