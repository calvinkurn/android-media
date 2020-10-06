package com.tokopedia.play.widget.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.adapter.viewholder.PlayWidgetCardMediumViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetCardUiModel
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.play_widget_card_medium, parent, false)
        return PlayWidgetCardMediumViewHolder(view, mItemListener)
    }

    override fun getItemCount(): Int = mItemList.size

    override fun onBindViewHolder(holder: PlayWidgetCardMediumViewHolder, position: Int) {
        holder.bind(mItemList[position])
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
        mViewHolder.forEach { it.release() }
    }

    private fun safePlayVideo(holder: PlayWidgetCardMediumViewHolder) {
        if (holder.getWidgetType() == PlayWidgetCardType.Upcoming ||
                holder.getWidgetType() == PlayWidgetCardType.Unknown) return

        // TODO start timer to delay
        holder.playVideo()
    }

    private fun safeStopVideo(holder: PlayWidgetCardMediumViewHolder) {
        if (holder.getWidgetType() == PlayWidgetCardType.Upcoming ||
                holder.getWidgetType() == PlayWidgetCardType.Unknown) return

        holder.stopVideo()
    }

    interface PlayWidgetCardMediumListener {
        fun onItemClickListener(item: PlayWidgetCardUiModel)
        fun onItemImpressListener(item: PlayWidgetCardUiModel)
    }
}