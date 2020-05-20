package com.tokopedia.sellerhomecommon.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.analytics.SellerHomeTracking
import com.tokopedia.sellerhomecommon.presentation.model.CarouselItemUiModel
import kotlinx.android.synthetic.main.shc_banner_item_layout.view.*

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class CarouselBannerAdapter(
        private val dataKey: String,
        private val items: List<CarouselItemUiModel>
) : RecyclerView.Adapter<CarouselBannerAdapter.CarouselBannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselBannerViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.shc_banner_item_layout, parent, false)
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