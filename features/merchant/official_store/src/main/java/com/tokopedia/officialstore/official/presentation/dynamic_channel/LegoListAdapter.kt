package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel

class LegoListAdapter(
        private val channelData: Channel,
        private val dcEventHandler: DynamicChannelEventHandler
) : RecyclerView.Adapter<LegoListAdapter.LegoItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LegoItemViewHolder(
            LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.dynamic_channel_lego_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: LegoItemViewHolder, position: Int) {
        val itemData = channelData.grids?.get(position)

        itemData?.let { item ->
            ImageHandler.loadImageFitCenter(holder.imageView.context, holder.imageView, item.imageUrl)
            holder.imageView.setOnClickListener(dcEventHandler.onClickLegoImage(channelData, position))
        }
    }

    override fun getItemCount() = channelData.grids?.size ?: 0

    class LegoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: SquareImageView by lazy {
            view.findViewById<SquareImageView>(R.id.dc_lego_image_item)
        }
    }
}
