package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid

class LegoListAdapter(
        private val ctx: Context?,
        private val listData: MutableList<Grid?>
) : RecyclerView.Adapter<LegoListAdapter.LegoItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LegoItemViewHolder(
            LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.dynamic_channel_lego_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: LegoItemViewHolder, position: Int) {
        val itemData = listData[position]

        itemData?.let { item ->
            ImageHandler.loadImageFitCenter(ctx, holder.imageView, item.imageUrl)
            holder.imageView.setOnClickListener {
                RouteManager.route(ctx, item.applink)
            }
        }
    }

    override fun getItemCount() = listData.size

    class LegoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: SquareImageView by lazy {
            view.findViewById<SquareImageView>(R.id.dc_lego_image_item)
        }
    }
}
