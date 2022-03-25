package com.tokopedia.search.result.product.videowidget

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationCarouselVideoBinding
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView.Option
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.video_widget.VideoPlayer
import com.tokopedia.video_widget.VideoPlayerProvider
import com.tokopedia.video_widget.carousel.VideoCarouselItemListener
import com.tokopedia.video_widget.carousel.VideoCarouselItemModel
import com.tokopedia.video_widget.carousel.VideoCarouselModel
import com.tokopedia.video_widget.carousel.VideoCarouselWidgetCoordinator

class InspirationCarouselVideoViewHolder(
    itemView: View,
    private val inspirationVideoCarouselListener: InspirationVideoCarouselListener,
    private val videoCarouselWidgetCoordinator: VideoCarouselWidgetCoordinator,
) : AbstractViewHolder<InspirationCarouselVideoDataView>(itemView),
    VideoPlayerProvider,
    VideoCarouselItemListener {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_inspiration_carousel_video
    }

    private var binding: SearchInspirationCarouselVideoBinding? by viewBinding()
    private var option: Option? = null

    override fun bind(element: InspirationCarouselVideoDataView) {
        val data = element.data.options.getOrNull(0) ?: return
        option = data
        val videoCarousel = binding?.videoCarousel ?: return
        videoCarouselWidgetCoordinator.controlWidget(videoCarousel, this)
        videoCarouselWidgetCoordinator.connect(videoCarousel, data.toCarouselDataModel())
    }

    override fun onViewRecycled() {
        binding?.videoCarousel?.recycle()
        option = null
        super.onViewRecycled()
    }

    private fun Option.toCarouselDataModel(): VideoCarouselModel {
        return VideoCarouselModel(
            title = title,
            itemList = product.map { it.toCarouselItemModel(this) }
        )
    }

    private fun Option.Product.toCarouselItemModel(option: Option): VideoCarouselItemModel {
        return VideoCarouselItemModel(
            id = id,
            videoURL = customVideoURL,
            imageURL = imgUrl,
            title = name,
            subTitle = shopName,
            applink = applink,
        )
    }

    override val videoPlayer: VideoPlayer?
        get() = binding?.videoCarousel

    override fun onVideoCarouselItemImpressed(position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        val product = option?.product?.get(position) ?: return
        inspirationVideoCarouselListener.onInspirationVideoCarouselProductImpressed(product)
    }

    override fun onVideoCarouselItemClicked(position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        val product = option?.product?.get(position) ?: return
        inspirationVideoCarouselListener.onInspirationVideoCarouselProductClicked(product)
    }
}