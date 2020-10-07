package com.tokopedia.play.widget.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.*
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumChannelViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumOverlayViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumOverlayUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType


/**
 * Created by mzennis on 05/10/20.
 */
class PlayWidgetCardMediumAdapter : RecyclerView.Adapter<PlayWidgetCardMediumViewHolder>() {

    private var mItemList: MutableList<PlayWidgetItemUiModel> = mutableListOf()
    private var mItemListener: PlayWidgetCardMediumListener? = null

    private var mViewHolder: MutableList<PlayWidgetCardMediumViewHolder> = mutableListOf()

    fun setItems(itemList: List<PlayWidgetItemUiModel>) {
        this.mItemList.addAll(itemList)
        this.notifyItemRangeInserted(mItemList.size, itemList.size)
    }

    fun updateItems(itemList: List<PlayWidgetItemUiModel>) {
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
        return when(mItemList[position]) {
            is PlayWidgetMediumOverlayUiModel -> CARD_TYPE_OVERLAY
            is PlayWidgetMediumChannelUiModel -> CARD_TYPE_CHANNEL
            is PlayWidgetMediumBannerUiModel -> CARD_TYPE_BANNER
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
        if (holder.getChannelType() == PlayWidgetChannelType.Upcoming ||
                holder.getChannelType() == PlayWidgetChannelType.Unknown) return

        // TODO start timer to delay
        holder.playVideo()
    }

    private fun safeStopVideo(holder: PlayWidgetCardMediumViewHolder) {
        if (holder !is PlayWidgetCardMediumChannelViewHolder) return
        if (holder.getChannelType() == PlayWidgetChannelType.Upcoming ||
                holder.getChannelType() == PlayWidgetChannelType.Unknown) return

        holder.stopVideo()
    }

    private fun getView(parent: ViewGroup, @LayoutRes layoutRes: Int) =
            LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)

    interface PlayWidgetCardMediumListener {
        fun onItemClickListener(item: PlayWidgetMediumChannelUiModel)
        fun onItemImpressListener(item: PlayWidgetMediumChannelUiModel)
    }

    companion object {
        private const val CARD_TYPE_BANNER = 1
        private const val CARD_TYPE_CHANNEL = 2
        private const val CARD_TYPE_OVERLAY = 3
    }
}