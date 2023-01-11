package com.tokopedia.people.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.people.R
import com.tokopedia.people.model.PlayPostContentItem
import com.tokopedia.people.model.UserPostModel
import com.tokopedia.people.utils.UserProfileVideoMapper
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.reminded
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.widget.large.PlayWidgetCardLargeChannelView

open class UserPostBaseAdapter(
    callback: AdapterCallback,
    val playWidgetCallback: PlayWidgetCallback,
    val onLoadMore: (cursor: String) -> Unit,
) : BaseAdapter<PlayPostContentItem>(callback), PlayWidgetCardLargeChannelView.Listener {

    var cursor: String = ""

    inner class ViewHolder(view: View) : BaseVH(view) {
        internal var playWidgetLargeView: PlayWidgetCardLargeChannelView =
            view.findViewById(R.id.play_widget_large_view)

        override fun bindView(item: PlayPostContentItem, position: Int) {
            playWidgetCallback.onImpressPlayWidgetData(
                item,
                item.isLive,
                item.id,
                position + 1,
            )
            setData(this, item)
        }
    }

    override fun getItemViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int,
    ): BaseVH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.up_item_user_post, parent, false)

        return ViewHolder(itemView)
    }

    override fun loadData(currentPageIndex: Int, vararg args: String?) {
        super.loadData(currentPageIndex, *args)
        if (args.isNotEmpty()) {
            args[0]?.let {
                onLoadMore(cursor)
            }
        }
    }

    fun onSuccess(data: UserPostModel) {
        if (data.playGetContentSlot.data.size == 0) {
            loadCompleted(mutableListOf(), data)
            isLastPage = true
            cursor = ""
        } else {
            data.playGetContentSlot.data.firstOrNull()?.items?.let { loadCompleted(it, data) }
            isLastPage = data.playGetContentSlot.playGetContentSlot.nextCursor.isEmpty()
            cursor = data.playGetContentSlot.playGetContentSlot.nextCursor
        }
    }

    fun onError() {
        loadCompletedWithError()
    }

    private fun setData(holder: ViewHolder, playPostContent: PlayPostContentItem) {
        holder.playWidgetLargeView.setModel(UserProfileVideoMapper.map(playPostContent, ""))
        holder.playWidgetLargeView.setListener(this)
    }

    private fun addVideoPostReminderClickCallBack(
        channelId: String,
        isActive: Boolean,
    ) {
        val pos = getItemPosition(channelId)
        playWidgetCallback.updatePostReminderStatus(channelId, isActive, pos)
        items[pos].configurations.reminder.isSet = isActive
    }

    private fun getItemPosition(channelId: String): Int {
        items.forEachIndexed { index, playPostContentItem ->
            if (playPostContentItem.id == channelId) {
                return index
            }
        }
        return -1
    }

    fun updatePlayWidgetLatestData(
        channelId: String,
        totalView: String?,
        isReminderSet: Boolean?,
    ) {
        val selectedData = items
            .filterIsInstance<PlayPostContentItem>()
            .firstOrNull { it.id == channelId } ?: return

        val currTotalView = selectedData.stats.view.formatted
        val currIsReminderSet = selectedData.configurations.reminder.isSet

        if (totalView != null && totalView != currTotalView) {
            selectedData.stats.view.formatted = totalView
            selectedData.stats.view.value = totalView
        } else if (isReminderSet != null && isReminderSet != currIsReminderSet) {
            selectedData.configurations.reminder.isSet = isReminderSet
        } else {
            return
        }

        val position = items.indexOf(selectedData)

        notifyItemChanged(position)
    }

    override fun onChannelClicked(view: View, item: PlayWidgetChannelUiModel) {
        playWidgetCallback.onPlayWidgetLargeClick(
            item.appLink,
            item.channelId,
            item.video.isLive && item.channelType == PlayWidgetChannelType.Live,
            item.video.coverUrl,
            getItemPosition(item.channelId) + 1,
        )
    }

    override fun onToggleReminderChannelClicked(
        item: PlayWidgetChannelUiModel,
        reminderType: PlayWidgetReminderType,
    ) {
        addVideoPostReminderClickCallBack(
            item.channelId,
            reminderType.reminded,
        )
    }

    interface PlayWidgetCallback {
        fun updatePostReminderStatus(channelId: String, isActive: Boolean, pos: Int)
        fun updatePlayWidgetLatestData(channelId: String, totalView: String, isReminderSet: Boolean)
        fun onPlayWidgetLargeClick(appLink: String, channelID: String, isLive: Boolean, imageUrl: String, pos: Int)
        fun onImpressPlayWidgetData(item: PlayPostContentItem, isLive: Boolean, channelId: String, pos: Int)
    }
}
