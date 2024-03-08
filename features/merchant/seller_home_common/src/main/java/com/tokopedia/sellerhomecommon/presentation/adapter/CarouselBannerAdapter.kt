package com.tokopedia.sellerhomecommon.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.sellerhomecommon.databinding.ShcBannerItemLayoutBinding
import com.tokopedia.sellerhomecommon.presentation.model.CarouselItemUiModel
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.CarouselViewHolder

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class CarouselBannerAdapter(
    private val dataKey: String,
    private val items: List<CarouselItemUiModel>,
    private val listener: CarouselViewHolder.Listener
) : RecyclerView.Adapter<CarouselBannerAdapter.CarouselBannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselBannerViewHolder {
        val binding = ShcBannerItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CarouselBannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselBannerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class CarouselBannerViewHolder(
        private val binding: ShcBannerItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CarouselItemUiModel) = with(binding) {
            imgCarouselBanner.loadImageRounded(item.featuredMediaURL, root.context.dpToPx(8))

            imgCarouselBanner.setOnClickListener {
                if (RouteManager.route(root.context, item.appLink)) {
                    listener.sendCarouselClickTracking(dataKey, items, adapterPosition)
                }
            }

            root.addOnImpressionListener(item.impressHolder) {
                listener.sendCarouselImpressionEvent(dataKey, items, adapterPosition)
            }
        }
    }
}
