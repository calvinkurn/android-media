package com.tokopedia.people.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.library.baseadapter.BaseItem
import com.tokopedia.people.R
import com.tokopedia.people.model.PlayPostContentItem
import com.tokopedia.people.model.UserPostModel
import com.tokopedia.people.utils.UserProfileVideoMapper
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.views.UserProfileFragment.Companion.VAL_FEEDS_PROFILE
import com.tokopedia.people.views.UserProfileFragment.Companion.VAL_SOURCE_BUYER
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.reminded
import com.tokopedia.play.widget.ui.widget.large.PlayWidgetCardLargeChannelView

open class UserPostBaseAdapter(
    val viewModel: UserProfileViewModel,
    val callback: AdapterCallback,
    private var userName: String = "",
    val userProfileTracker: UserProfileTracker?,
    val profileUserId: String,
    val userId: String,
    val reminderCallback: ReminderCallback
) : BaseAdapter<PlayPostContentItem>(callback), PlayWidgetCardLargeChannelView.Listener {

    var activityId = ""
    protected var cList: MutableList<BaseItem>? = null
    public var cursor: String = ""
    var displayName = ""

    inner class ViewHolder(view: View) : BaseVH(view) {
        internal var playWidgetLargeView: PlayWidgetCardLargeChannelView =
            view.findViewById(R.id.play_widget_large_view)

        override fun bindView(item: PlayPostContentItem, position: Int) {
            setData(this, item, position)
        }
    }

    public fun setUserName(userName: String) {
        this.userName = userName
    }

    override fun getItemViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): BaseVH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.up_item_user_post, parent, false)

        return ViewHolder(itemView)
    }

    override fun loadData(currentPageIndex: Int, vararg args: String?) {
        super.loadData(currentPageIndex, *args)
        if (args.isNotEmpty()) {
            args[0]?.let {
                viewModel.getUPlayVideos(
                    VAL_FEEDS_PROFILE,
                    cursor,
                    VAL_SOURCE_BUYER,
                    it
                )
            }
        }
    }

    fun onSuccess(data: UserPostModel) {
        if (data == null
            || data.playGetContentSlot == null
            || data.playGetContentSlot.data == null
            || data.playGetContentSlot.data.size == 0
        ) {
            loadCompleted(mutableListOf(), data)
            isLastPage = true
            cursor = ""
        } else {
            data.playGetContentSlot.data.firstOrNull()?.items?.let { loadCompleted(it, data) }
            isLastPage = data?.playGetContentSlot?.playGetContentSlot?.nextCursor?.isEmpty()
            cursor = data?.playGetContentSlot?.playGetContentSlot?.nextCursor;
        }
    }

    fun onError() {
        loadCompletedWithError()
    }

    private fun setData(holder: ViewHolder, playPostContent: PlayPostContentItem, position: Int) {
        holder.playWidgetLargeView.setModel(UserProfileVideoMapper.map(playPostContent, "", displayName))
        holder.playWidgetLargeView.setListener(this)

    }




    private fun addVideoPostReminderClickCallBack(
        channelId: String,
        isActive: Boolean
    ) {
        val pos = getItemPosition(channelId)
        reminderCallback.updatePostReminderStatus(channelId, isActive, pos)
        items[pos].configurations.reminder.isSet = isActive
    }

    private fun getItemPosition(channelId: String): Int {
        items.forEachIndexed { index, playPostContentItem ->
            if(playPostContentItem.id == channelId){
                return index
            }
        }
        return -1
    }


    override fun onViewAttachedToWindow(vh: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(vh)

        if (vh is ViewHolder) {
            val holder = vh as ViewHolder
            val data = items[holder.adapterPosition] ?: return
        }
    }

    companion object {
        const val COMING_SOON = "COMING_SOON"
        const val UPCOMING = "UPCOMING"
        const val WATCH_AGAIN = "WATCH_AGAIN"
        const val DATE_FORMAT_INPUT = "yyyy-MM-dd'T'HH:mm:ss"
        const val DATE_FORMAT_OUTPUT = "dd MMM yyyy - HH:mm"
    }

    override fun onChannelClicked(view: View, item: PlayWidgetChannelUiModel) {
        RouteManager.route(view.context, item.appLink)
    }

    override fun onToggleReminderChannelClicked(
        item: PlayWidgetChannelUiModel,
        reminderType: PlayWidgetReminderType
    ) {
        addVideoPostReminderClickCallBack(
            item.channelId,
            reminderType.reminded
        )
    }

    override fun onLabelPromoClicked(view: View, item: PlayWidgetChannelUiModel) {
       //add tracker later
    }

    override fun onLabelPromoImpressed(view: View, item: PlayWidgetChannelUiModel) {
        //add tracker later
    }

}

interface ReminderCallback{
    fun updatePostReminderStatus(channelId: String, isActive: Boolean, pos: Int)
}
