package com.tokopedia.sellerhome.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.analytic.SellerHomeTracking
import com.tokopedia.sellerhome.view.model.CarouselItemUiModel
import kotlinx.android.synthetic.main.sah_banner_item_layout.view.*

/**
 * Created By @ilhamsuaib on 2020-02-13
 */

class CarouselBannerAdapter(
        private val dataKey: String,
        private val items: List<CarouselItemUiModel>
) : RecyclerView.Adapter<CarouselBannerAdapter.CarouselBannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselBannerViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.sah_banner_item_layout, parent, false)
        return CarouselBannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselBannerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class CarouselBannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: CarouselItemUiModel) = with(itemView) {
            ImageHandler.loadImageRounded2(context, imgCarouselBanner, item.featuredMediaURL, context.dpToPx(8))

            imgCarouselBanner.setOnClickListener {
                if (RouteManager.route(context, item.appLink)) {
                    SellerHomeTracking.sendClickCarouselItemBannerEvent(dataKey, items, adapterPosition)
                }
            }

            addOnImpressionListener(item.impressHolder) {
                SellerHomeTracking.sendImpressionCarouselItemBannerEvent(dataKey, items, adapterPosition)
            }
        }
    }
}