package com.tokopedia.people.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.library.baseadapter.BaseItem
import com.tokopedia.people.R
import com.tokopedia.people.model.PlayPostContentItem
import com.tokopedia.people.model.UserPostModel
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.views.UserProfileFragment.Companion.VAL_FEEDS_PROFILE
import com.tokopedia.people.views.UserProfileFragment.Companion.VAL_SOURCE_BUYER
import com.tokopedia.play_common.util.datetime.PlayDateTimeFormatter
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster

open class UserPostBaseAdapter(
    val viewModel: UserProfileViewModel,
    val callback: AdapterCallback,
    private var userName: String = "",
    val userProfileTracker: UserProfileTracker?,
    val profileUserId: String,
    val userId: String
    ) : BaseAdapter<PlayPostContentItem>(callback) {

    var activityId = ""
    protected var cList: MutableList<BaseItem>? = null
    private var cursor: String = ""

    inner class ViewHolder(view: View) : BaseVH(view) {
        internal var imgCover: ImageUnify = view.findViewById(R.id.img_banner)
        internal var textLiveCount: TextView = view.findViewById(R.id.text_live_view_count)
        internal var textName: TextView = view.findViewById(R.id.text_display_name)
        internal var textUsername: TextView = view.findViewById(R.id.text_user_name)
        internal var textLive: TextView = view.findViewById(R.id.text_live)
        internal var textDate: TextView = view.findViewById(R.id.text_date)
        internal var btnReminder: AppCompatImageView = view.findViewById(R.id.btn_reminder)
        internal var textSpecialLabel: Label = view.findViewById(R.id.text_special_label)
        var isVisited = false

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
            loadCompleted(data?.playGetContentSlot?.data[0]?.items, data)
            isLastPage = data?.playGetContentSlot?.playGetContentSlot?.nextCursor?.isEmpty()
            cursor = data?.playGetContentSlot?.playGetContentSlot?.nextCursor;
        }
    }

    fun onError() {
        loadCompletedWithError()
    }

    private fun setData(holder: ViewHolder, item: PlayPostContentItem, position: Int) {
        val itemContext = holder.itemView.context
        holder.imgCover.setImageUrl(item.coverUrl)
        holder.textName.text = item.title

        try {
            if (item.stats.view.formatted.toInt() == 0) {
                holder.textLiveCount.hide()
            } else {
                holder.textLiveCount.show()
                holder.textLiveCount.text = item.stats.view.formatted
            }
        } catch (e: Exception) {

        }

        if (item.startTime.isNullOrBlank()) {
            holder.textDate.hide()
        } else {
            holder.textDate.text = PlayDateTimeFormatter.formatDate(
                item.startTime,
                DATE_FORMAT_INPUT,
                DATE_FORMAT_OUTPUT
            )
            holder.textDate.show()
        }

        if (item.isLive) {
            holder.textLive.show()
        } else {
            holder.textLive.hide()
        }

        if (userName.isNotBlank()) {
            holder.textUsername.show()
            holder.textUsername.text = "@$userName"
        } else {
            holder.textUsername.hide()
        }

        if (item.airTime?.equals(COMING_SOON, ignoreCase = true)) {
            holder.btnReminder.show()

            if (item.configurations?.reminder?.isSet) {
                holder.btnReminder.setImageDrawable(
                    holder.btnReminder.context
                        .getDrawable(com.tokopedia.unifycomponents.R.drawable.iconunify_bell_filled)
                )

                holder.btnReminder.setOnClickListener(
                    addVideoPostReminderClickCallBack(
                        item.id,
                        false,
                        position,
                        holder.itemView
                    )
                )
                item.configurations?.reminder?.isSet = false;
            } else {
                holder.btnReminder.setImageDrawable(
                    holder.btnReminder.context
                        .getDrawable(com.tokopedia.unifycomponents.R.drawable.iconunify_bell)
                )

                holder.btnReminder.setOnClickListener(
                    addVideoPostReminderClickCallBack(
                        item.id,
                        true,
                        position,
                        holder.itemView
                    )
                )
                item.configurations?.reminder?.isSet = true;
            }
        } else if (item.airTime?.equals(UPCOMING, ignoreCase = true)) {
            holder.btnReminder.show()

            holder.btnReminder.setImageDrawable(
                holder.btnReminder.context
                    .getDrawable(com.tokopedia.unifycomponents.R.drawable.iconunify_bell_filled)
            )
            holder.btnReminder.setColorFilter(
                ContextCompat.getColor(
                    holder.btnReminder.context,
                    com.tokopedia.unifycomponents.R.color.Unify_GN500
                )
            )
        } else {
            holder.btnReminder.hide()
        }

        if (item.configurations?.hasPromo) {
            holder.textSpecialLabel.setLabel(item.configurations?.promoLabels?.get(0)?.text)
            holder.textSpecialLabel.setLabelImage(
                holder.textSpecialLabel.context.getDrawable(com.tokopedia.unifycomponents.R.drawable.iconunify_promo),
                holder.textSpecialLabel.context.dpToPx(13).toInt(),
                holder.textSpecialLabel.context.dpToPx(13).toInt()
            )

            holder.textSpecialLabel.show()
        } else {
            holder.textSpecialLabel.hide()
        }

        holder.itemView.setOnClickListener { v ->
            userProfileTracker?.clickVideo(userId = userId, self = userId == profileUserId, live = item.isLive, activityId = activityId, imageUrl = item.webLink,  videoPosition = position)
            RouteManager.route(itemContext, item.appLink)
        }
    }

    private fun addVideoPostReminderClickCallBack(
        channelId: String,
        isActive: Boolean,
        position: Int,
        item: View
    ) =
        View.OnClickListener {
            if (!DeviceConnectionInfo.isInternetAvailable(
                    item.context
                )
            ) {
                Toaster.build(
                    item,
                    item.context.getString(com.tokopedia.people.R.string.up_error_local_error),
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR
                ).show()
                return@OnClickListener
            }

            viewModel.updatePostReminderStatus(channelId, isActive)
            notifyItemChanged(position)
        }


    override fun onViewAttachedToWindow(vh: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(vh)

        if (vh is ViewHolder) {
            val holder = vh as ViewHolder
            val data = items[holder.adapterPosition] ?: return
            userProfileTracker?.impressionVideo(userId = userId, self = userId == profileUserId, live = data.isLive, activityId = activityId, imageUrl = data.webLink,  videoPosition = vh.adapterPosition)
        }
    }

    companion object {
        const val COMING_SOON = "COMING_SOON"
        const val UPCOMING = "UPCOMING"
        const val WATCH_AGAIN = "WATCH_AGAIN"
        const val DATE_FORMAT_INPUT = "yyyy-MM-dd'T'HH:mm:ss"
        const val DATE_FORMAT_OUTPUT = "dd MMM yyyy - HH:mm"
    }
}

