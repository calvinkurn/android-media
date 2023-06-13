package com.tokopedia.chat_common.view.adapter.viewholder

import android.graphics.PorterDuff
import android.text.TextUtils
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.util.ChatTimeConverter.formatTime
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderUiModel.Companion.ROLE_USER
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import java.util.*

/**
 * @author by nisie on 5/9/18.
 */
open class BaseChatViewHolder<T : Visitable<*>>(
    protected var view: View?
) : AbstractViewHolder<T>(view) {

    protected val hour: TextView? by lazy {
        itemView.findViewById(hourId)
    }

    protected val date: TextView? by lazy {
        itemView.findViewById(dateId)
    }

    protected val chatReadStatus: ImageView? by lazy {
        itemView.findViewById(chatStatusId)
    }

    private var roleContainer: LinearLayout? = null
    protected var roleType: TextView? = null
    private var roleName: TextView? = null

    protected fun changeHourColor(@ColorInt color: Int) {
        if (hour != null) {
            hour?.setTextColor(color)
        }
    }

    private val roleNameId: Int
        get() = R.id.tvName
    private val roleId: Int
        get() = R.id.tvRole
    protected open val hourId: Int
        get() = R.id.hour
    protected open val dateId: Int
        get() = R.id.date
    protected open val chatStatusId: Int
        get() = R.id.chat_status
    private val roleContainerId: Int
        get() = R.id.llRoleUser

    init {
        roleContainer = itemView.findViewById(roleContainerId)
        roleType = itemView.findViewById(roleId)
        roleName = itemView.findViewById(roleNameId)
    }

    override fun bind(uiModel: T) {
        if (uiModel is BaseChatUiModel) {
            try {
                val replyTIme: Long = uiModel.replyTime?.toLongOrZero() ?: 0
                if (replyTIme / MILISECONDS < START_YEAR) {
                    uiModel.replyTime = (replyTIme * MILISECONDS).toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            view?.setOnClickListener { v: View? ->
                KeyboardHandler.DropKeyboard(
                    itemView.context,
                    view
                )
            }
            setHeaderDate(uiModel)
            setBottomHour(uiModel)
        }
    }

    protected open fun setBottomHour(element: BaseChatUiModel) {
        val hourTime = getHourTime(element.replyTime)
        if (hour != null && (TextUtils.isEmpty(hourTime) || !element.isShowTime) &&
            !alwaysShowTime()
        ) {
            hour?.visibility = View.GONE
        } else if (hour != null) {
            hour?.text = hourTime
            hour?.visibility = View.VISIBLE
        }
    }

    protected open fun alwaysShowTime(): Boolean {
        return false
    }

    protected open fun getHourTime(replyTime: String?): String? {
        val hourTime: String? = try {
            val longReplyTime: Long = replyTime?.toLongOrZero()?: 0L
            formatTime(
                longReplyTime / MILISECONDS
            )
        } catch (e: NumberFormatException) {
            replyTime
        }
        return hourTime
    }

    protected open fun setHeaderDate(element: BaseChatUiModel) {
        if (date == null) return
        var time: String?
        try {
            var myTime = element.replyTime?.toLongOrZero() ?: 0
            myTime /= MILISECONDS
            val date = Date(myTime)
            time = when {
                DateUtils.isToday(myTime) -> {
                    itemView.context.getString(R.string.chat_today_date)
                }
                DateUtils.isToday(myTime + DateUtils.DAY_IN_MILLIS) -> {
                    itemView.context.getString(R.string.chat_yesterday_date)
                }
                else -> {
                    DateFormat.getLongDateFormat(itemView.context).format(date)
                }
            }
        } catch (e: NumberFormatException) {
            time = element.replyTime
        }
        if (date != null && element.isShowDate
            && !TextUtils.isEmpty(time)
        ) {
            date?.visibility = View.VISIBLE
            date?.text = time
        } else if (date != null) {
            date?.visibility = View.GONE
        }
    }

    protected open fun bindChatReadStatus(element: SendableUiModel) {
        if (chatReadStatus == null) return
        var imageResource: Int
        if (element.isShowTime || alwaysShowTime()) {
            chatReadStatus?.visibility = View.VISIBLE
            imageResource = if (element.isRead) {
                R.drawable.ic_chatcommon_check_read_rounded_green
            } else {
                R.drawable.ic_chatcommon_check_sent_rounded_grey
            }
            if (element.isDummy) {
                imageResource = R.drawable.ic_chatcommon_check_rounded_grey
            }
            val drawable = MethodChecker.getDrawable(
                chatReadStatus?.context, imageResource
            )
            if (useWhiteReadStatus() && !element.isRead) {
                chatReadStatus?.context?.let {
                    drawable.mutate()
                    drawable.setColorFilter(
                        ContextCompat.getColor(
                            it, com.tokopedia.unifyprinciples.R.color.Unify_N0
                        ), PorterDuff.Mode.SRC_ATOP
                    )
                }
            } else {
                drawable.clearColorFilter()
            }
            chatReadStatus?.setImageDrawable(drawable)
        } else {
            chatReadStatus?.visibility = View.GONE
        }
        if (element.isSender) {
            chatReadStatus?.visibility = View.VISIBLE
        } else {
            chatReadStatus?.visibility = View.GONE
        }
    }

    protected fun bindRoleHeader(chat: SendableUiModel, gravity: Int) {
        if (roleContainer == null) {
            hideHeader()
            return
        }
        if (chat.fromRole.isNotEmpty() &&
            chat.fromRole.lowercase(Locale.getDefault()) != ROLE_USER.lowercase(Locale.getDefault()) &&
            chat.isSender &&
            !chat.isDummy &&
            chat.isShowRole
        ) {
            roleType?.text = chat.from
            roleName?.text = chat.fromRole
            roleContainer?.visibility = View.VISIBLE
            roleContainer?.gravity = gravity
        } else {
            roleContainer?.visibility = View.GONE
        }
    }

    protected open fun useWhiteReadStatus(): Boolean {
        return false
    }

    private fun hideHeader() {
        roleContainer?.visibility = View.GONE
    }

    override fun onViewRecycled() {}

    companion object {
        const val MILISECONDS: Long = 1000000
        const val START_YEAR: Long = 1230768000
    }
}