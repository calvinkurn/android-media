package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class SprintSaleListAdapter(
        private val channelData: Channel,
        private val dcEventHandler: DynamicChannelEventHandler
) : RecyclerView.Adapter<SprintSaleListAdapter.SprintSaleItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SprintSaleItemViewHolder(
            LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.dynamic_channel_sprintsale_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: SprintSaleItemViewHolder, position: Int) {
        val itemData = channelData.grids?.get(position)

        itemData?.let { item ->
            ImageHandler.loadImageFitCenter(holder.imageView.context, holder.imageView, item.imageUrl)

            holder.apply {
                imageView.setOnClickListener(dcEventHandler.onClickFlashSaleImage(channelData, position))
                discountView.text = item.discount
                oldPrice.text = item.slashedPrice
                oldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                newPrice.text = item.price
                Glide.with(holder.freeOngkirView.context)
                        .asBitmap()
                        .load(item.freeOngkir?.imageUrl)
                        .fitCenter()
                        .dontAnimate()
                        .into(freeOngkirView)
            }
        }
    }

    override fun getItemCount() = channelData.grids?.size ?: 0

    class SprintSaleItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: SquareImageView by lazy {
            view.findViewById<SquareImageView>(R.id.dc_sprintsale_item_image)
        }

        val discountView: Label by lazy {
            view.findViewById<Label>(R.id.dc_sprintsale_item_disc_label)
        }

        val oldPrice: Typography by lazy {
            view.findViewById<Typography>(R.id.dc_sprintsale_item_old_price)
        }

        val newPrice: Typography by lazy {
            view.findViewById<Typography>(R.id.dc_sprintsale_item_new_price)
        }

        val freeOngkirView: AppCompatImageView by lazy {
            view.findViewById<AppCompatImageView>(R.id.dc_sprintsale_item_freeongkir)
        }
    }
}
