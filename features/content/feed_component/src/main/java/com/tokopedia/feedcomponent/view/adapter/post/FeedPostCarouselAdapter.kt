package com.tokopedia.feedcomponent.view.adapter.post

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.CarouselImageViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.CarouselVideoViewHolder
import com.tokopedia.feedcomponent.view.widget.PostTagView
import com.tokopedia.feedcomponent.view.widget.listener.FeedCampaignListener

/**
 * Created by kenny.hadisaputra on 24/06/22
 */
class FeedPostCarouselAdapter(
    dataSource: DataSource,
    imageListener: CarouselImageViewHolder.Listener,
    videoListener: CarouselVideoViewHolder.Listener,
    listener: FeedCampaignListener? = null
) : BaseDiffUtilAdapter<FeedXMedia>(true) {

    init {
        delegatesManager
            .addDelegate(CarouselImageDelegate(dataSource, imageListener, listener))
            .addDelegate(CarouselVideoDelegate(videoListener))
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

    fun updateReminderStatusForAllButtonsInCarousel(){
        val changeReminderBtnState = Bundle().apply {
            putBoolean(PAYLOAD_REMINDER_BTN, true)
        }
        notifyItemRangeChanged(0, itemList.size, changeReminderBtnState)
    }

    fun focusItemAt(position: Int) {
        val focusPayload = Bundle().apply {
            putBoolean(PAYLOAD_FOCUS, true)
        }
        val removeFocusPayload = Bundle().apply {
            putBoolean(PAYLOAD_FOCUS, false)
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

    fun removeAllFocus(position: Int) {
        val removeFocusPayload = Bundle().apply {
            putBoolean(PAYLOAD_FOCUS, false)
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
        private val fstListener: FeedCampaignListener?,
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
                    else holder.removeFocus()
                }
                if (payloads.containsKey(PAYLOAD_REMINDER_BTN)) {
                    if (payloads.getBoolean(PAYLOAD_REMINDER_BTN)) holder.updateAsgcButton()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): CarouselImageViewHolder {
            return CarouselImageViewHolder.create(parent, dataSource, listener, fstListener)
        }
    }

    private class CarouselVideoDelegate(
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
            return CarouselVideoViewHolder.create(parent, listener)
        }
    }

    companion object {
        private const val PAYLOAD_FOCUS = "payload_focus"
        private const val PAYLOAD_PAUSE = "payload_pause"
        private const val PAYLOAD_REMINDER_BTN = "payload_reminder_button"

        private const val FOCUS_POSITION_THRESHOLD = 2
    }

    interface DataSource {
        fun getFeedXCard(): FeedXCard
        fun getTagBubbleListener(): PostTagView.TagBubbleListener?
        fun getPositionInFeed(): Int
    }
}
