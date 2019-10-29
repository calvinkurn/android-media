package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class SprintSaleListAdapter(
        private val ctx: Context?,
        private val listData: MutableList<Grid?>
) : RecyclerView.Adapter<SprintSaleListAdapter.SprintSaleItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SprintSaleItemViewHolder(
            LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.dynamic_channel_sprintsale_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: SprintSaleItemViewHolder, position: Int) {
        val itemData = listData[position]

        itemData?.let { item ->
            ImageHandler.loadImageFitCenter(ctx, holder.imageView, item.imageUrl)

            holder.apply {
                imageView.setOnClickListener {
                    RouteManager.route(ctx, item.applink)
                }
                discountView.text = item.discount
                oldPrice.text = item.slashedPrice
                oldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                newPrice.text = item.price
                ImageHandler.loadImageFitCenter(ctx, freeOngkirView, item.freeOngkir?.imageUrl)
            }
        }
    }

    override fun getItemCount() = listData.size

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
