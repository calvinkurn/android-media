package com.tokopedia.play.widget.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.adapter.viewholder.PlayWidgetCardMediumBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.PlayWidgetCardMediumChannelViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.PlayWidgetCardMediumOverlayViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.PlayWidgetCardMediumViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetCardItemUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetCardUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetCardItemType
import com.tokopedia.play.widget.ui.type.PlayWidgetCardType


/**
 * Created by mzennis on 05/10/20.
 */
class PlayWidgetCardMediumAdapter : RecyclerView.Adapter<PlayWidgetCardMediumViewHolder>() {

    private var mItemList: MutableList<PlayWidgetCardUiModel> = mutableListOf()
    private var mItemListener: PlayWidgetCardMediumListener? = null

    private var mViewHolder: MutableList<PlayWidgetCardMediumViewHolder> = mutableListOf()

    fun setItems(itemList: List<PlayWidgetCardUiModel>) {
        this.mItemList.addAll(itemList)
        this.notifyItemRangeInserted(mItemList.size, itemList.size)
    }

    fun updateItems(itemList: List<PlayWidgetCardUiModel>) {
        this.mItemList = itemList.toMutableList()
        this.notifyDataSetChanged()
    }

    fun setItemListener(listener: PlayWidgetCardMediumListener) {
        this.mItemListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayWidgetCardMediumViewHolder {
        return when(viewType) {
            CARD_TYPE_OVERLAY -> PlayWidgetCardMediumOverlayViewHolder(getView(parent, R.layout.play_widget_card_medium_overlay))
            CARD_TYPE_CHANNEL -> PlayWidgetCardMediumChannelViewHolder(getView(parent, R.layout.play_widget_card_medium), mItemListener)
            CARD_TYPE_BANNER -> PlayWidgetCardMediumBannerViewHolder(getView(parent, R.layout.play_widget_card_medium_banner))
            else -> throw IllegalStateException("unhandled view type")
        }
    }

    override fun getItemCount(): Int = mItemList.size

    override fun onBindViewHolder(holder: PlayWidgetCardMediumViewHolder, position: Int) {
        holder.bind(mItemList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when(mItemList[position].type) {
            PlayWidgetCardType.Overlay -> CARD_TYPE_OVERLAY
            PlayWidgetCardType.Channel -> CARD_TYPE_CHANNEL
            PlayWidgetCardType.Banner -> CARD_TYPE_BANNER
            else -> super.getItemViewType(position)
        }
    }

    override fun onViewAttachedToWindow(holder: PlayWidgetCardMediumViewHolder) {
        safePlayVideo(holder)
        mViewHolder.add(holder)
    }

    override fun onViewDetachedFromWindow(holder: PlayWidgetCardMediumViewHolder) {
        safeStopVideo(holder)
        mViewHolder.remove(holder)
    }

    fun safeReleaseVideo() {
        mViewHolder.forEach { if (it is PlayWidgetCardMediumChannelViewHolder) it.release() }
    }

    private fun safePlayVideo(holder: PlayWidgetCardMediumViewHolder) {
        if (holder !is PlayWidgetCardMediumChannelViewHolder) return
        if (holder.getCardItemType() == PlayWidgetCardItemType.Upcoming ||
                holder.getCardItemType() == PlayWidgetCardItemType.Unknown) return

        // TODO start timer to delay
        holder.playVideo()
    }

    private fun safeStopVideo(holder: PlayWidgetCardMediumViewHolder) {
        if (holder !is PlayWidgetCardMediumChannelViewHolder) return
        if (holder.getCardItemType() == PlayWidgetCardItemType.Upcoming ||
                holder.getCardItemType() == PlayWidgetCardItemType.Unknown) return

        holder.stopVideo()
    }

    private fun getView(parent: ViewGroup, @LayoutRes layoutRes: Int) =
            LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)

    interface PlayWidgetCardMediumListener {
        fun onItemClickListener(item: PlayWidgetCardItemUiModel)
        fun onItemImpressListener(item: PlayWidgetCardItemUiModel)
    }

    companion object {
        private const val CARD_TYPE_BANNER = 1
        private const val CARD_TYPE_CHANNEL = 2
        private const val CARD_TYPE_OVERLAY = 3
    }
}