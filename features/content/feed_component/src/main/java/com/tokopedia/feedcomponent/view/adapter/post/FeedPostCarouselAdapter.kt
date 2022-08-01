package com.tokopedia.feedcomponent.view.adapter.post

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.CarouselImageViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.CarouselVideoViewHolder

/**
 * Created by kenny.hadisaputra on 24/06/22
 */
internal class FeedPostCarouselAdapter(
    dataSource: DataSource,
    imageListener: CarouselImageViewHolder.Listener,
    videoListener: CarouselVideoViewHolder.Listener,
) : BaseDiffUtilAdapter<FeedXMedia>(true) {

    init {
        delegatesManager
            .addDelegate(CarouselImageDelegate(dataSource, imageListener))
            .addDelegate(CarouselVideoDelegate(dataSource, videoListener))
    }

    override fun areItemsTheSame(oldItem: FeedXMedia, newItem: FeedXMedia): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: FeedXMedia,
        newItem: FeedXMedia
    ): Boolean {
        return if (!oldItem.isImage) false
        else oldItem == newItem
    }

    fun focusItemAt(position: Int) {
        val focusPayload = Bundle().apply {
            putBoolean(PAYLOAD_FOCUS, true)
        }
        val removeFocusPayload = Bundle().apply {
            putBoolean(PAYLOAD_FOCUS, false)
            putBoolean(PAYLOAD_RESET_TOP_ADS, false)
        }
        notifyItemChanged(position, focusPayload)
        notifyItemRangeChanged(
            position - FOCUS_POSITION_THRESHOLD,
            FOCUS_POSITION_THRESHOLD,
            removeFocusPayload
        )
        notifyItemRangeChanged(
            position + 1,
            FOCUS_POSITION_THRESHOLD,
            removeFocusPayload
        )
    }

    fun updateCTAasperWidgetColor(position: Int) {
        val greenTopAdsPayload = Bundle().apply {
            putBoolean(PAYLOAD_CTA_COLOR_CHANGED, true)
        }
        notifyItemChanged(position, greenTopAdsPayload)
    }

    fun updateNeighbourTopAdsColor(basePosition: Int) {
        val removeFocusPayload = Bundle().apply {
            putBoolean(PAYLOAD_CHANGE_TOP_ADS, true)
        }
        notifyItemRangeChanged(
            basePosition - FOCUS_POSITION_THRESHOLD,
            2 * FOCUS_POSITION_THRESHOLD + 1,
            removeFocusPayload
        )
    }

    fun removeAllFocus(position: Int) {
        val removeFocusPayload = Bundle().apply {
            putBoolean(PAYLOAD_FOCUS, false)
            putBoolean(PAYLOAD_RESET_TOP_ADS, true)
        }
        notifyItemRangeChanged(
            position - FOCUS_POSITION_THRESHOLD,
            2 * FOCUS_POSITION_THRESHOLD + 1,
            removeFocusPayload
        )
    }

    fun onPause() {
        val removeFocusPayload = Bundle().apply {
            putBoolean(PAYLOAD_PAUSE, true)
        }
        notifyItemRangeChanged(0, itemCount, removeFocusPayload)
    }

    private class CarouselImageDelegate(
        private val dataSource: DataSource,
        private val listener: CarouselImageViewHolder.Listener,
    ) : BaseAdapterDelegate<FeedXMedia, FeedXMedia, CarouselImageViewHolder>(
        R.layout.item_post_image_new
    ) {
        override fun isForViewType(
            itemList: List<FeedXMedia>,
            position: Int,
            isFlexibleType: Boolean
        ): Boolean {
            return itemList[position].isImage
        }

        override fun onBindViewHolder(item: FeedXMedia, holder: CarouselImageViewHolder) {
            holder.bind(item)
        }

        override fun onBindViewHolderWithPayloads(
            item: FeedXMedia,
            holder: CarouselImageViewHolder,
            payloads: Bundle
        ) {
            if (payloads.isEmpty) super.onBindViewHolderWithPayloads(item, holder, payloads)
            else {
                if (payloads.containsKey(PAYLOAD_FOCUS)) {
                    if (payloads.getBoolean(PAYLOAD_FOCUS)) holder.focusMedia()
                    else holder.removeFocus(
                        resetTopAds = payloads.getBoolean(PAYLOAD_RESET_TOP_ADS, true),
                    )
                }

                if (payloads.containsKey(PAYLOAD_CHANGE_TOP_ADS)) {
                    if (payloads.getBoolean(PAYLOAD_CHANGE_TOP_ADS)) holder.changeTopAds(
                        isColorChangedAsPerAsgcWidget = dataSource.getFeedXCard().isAsgcColorChangedAsPerWidgetColor
                    )
                }

                if (payloads.containsKey(PAYLOAD_CTA_COLOR_CHANGED)) {
                    holder.changeTopAds(isColorChangedAsPerAsgcWidget = true)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): CarouselImageViewHolder {
            return CarouselImageViewHolder.create(parent, dataSource, listener)
        }
    }

    private class CarouselVideoDelegate(
        private val dataSource: DataSource,
        private val listener: CarouselVideoViewHolder.Listener,
    ) : BaseAdapterDelegate<FeedXMedia, FeedXMedia, CarouselVideoViewHolder>(
        R.layout.item_post_video_new
    ) {
        override fun isForViewType(
            itemList: List<FeedXMedia>,
            position: Int,
            isFlexibleType: Boolean
        ): Boolean {
            return !itemList[position].isImage
        }

        override fun onBindViewHolder(item: FeedXMedia, holder: CarouselVideoViewHolder) {
            holder.bind(item)
        }

        override fun onBindViewHolderWithPayloads(
            item: FeedXMedia,
            holder: CarouselVideoViewHolder,
            payloads: Bundle
        ) {
            if (payloads.isEmpty) super.onBindViewHolderWithPayloads(item, holder, payloads)
            else {
                if (payloads.containsKey(PAYLOAD_FOCUS)) {
                    if (payloads.getBoolean(PAYLOAD_FOCUS)) holder.focusMedia()
                    else holder.removeFocus()
                }

                if (payloads.containsKey(PAYLOAD_PAUSE)) {
                    if (payloads.getBoolean(PAYLOAD_PAUSE)) holder.removeFocus()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): CarouselVideoViewHolder {
            return CarouselVideoViewHolder.create(parent, dataSource, listener)
        }
    }

    companion object {
        private const val PAYLOAD_FOCUS = "payload_focus"
        private const val PAYLOAD_PAUSE = "payload_pause"
        private const val PAYLOAD_RESET_TOP_ADS = "payload_reset_top_ads"
        private const val PAYLOAD_CHANGE_TOP_ADS = "payload_change_top_ads"
        private const val PAYLOAD_CTA_COLOR_CHANGED = "payload_cta_color_changed"

        private const val FOCUS_POSITION_THRESHOLD = 2
    }

    interface DataSource {
        fun getFeedXCard(): FeedXCard
        fun getDynamicPostListener(): DynamicPostViewHolder.DynamicPostListener?
        fun getPositionInFeed(): Int
    }
}
