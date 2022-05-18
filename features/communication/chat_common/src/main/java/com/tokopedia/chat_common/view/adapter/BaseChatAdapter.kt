package com.tokopedia.chat_common

import android.content.Context
import android.text.format.DateFormat
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.data.TypingChatModel
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactoryImpl
import com.tokopedia.chat_common.view.adapter.viewholder.ImageAnnouncementViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.ImageUploadViewHolder
import java.util.*

/**
 * @author by nisie on 23/11/18.
 */
open class BaseChatAdapter(adapterTypeFactory: BaseChatTypeFactoryImpl) :
        BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(adapterTypeFactory) {

    protected val SECONDS: Long = 1000000

    var typingModel = TypingChatModel()

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        if (visitables[position] is BaseChatUiModel) {
            if (enableShowDate()) showDateBaseChat(holder.itemView.context, holder.adapterPosition)
            if (enableShowTime()) showTimeBaseChat(holder.adapterPosition)
        }

        if (visitables[position] is SendableUiModel) {
            showRoleBaseChat(holder.adapterPosition)
        }
        super.onBindViewHolder(holder, position)
    }

    override fun onViewRecycled(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewRecycled(holder)
        if (holder is ImageUploadViewHolder) {
            (holder as ImageUploadViewHolder).onViewRecycled()
        } else if (holder is ImageAnnouncementViewHolder) {
            (holder as ImageAnnouncementViewHolder).onViewRecycled()
        }
    }

    fun setList(list: List<Visitable<*>>) {
        this.visitables.clear()
        this.visitables.addAll(list)
        notifyDataSetChanged()
    }

    fun addList(newItems: List<Visitable<*>>) {
        val positionStart = this.visitables.size
        this.visitables.addAll(newItems)
        notifyItemRangeInserted(positionStart, newItems.size)
        notifyItemRangeChanged(positionStart - 10, 10)
    }

    override fun getList(): List<Visitable<*>> {
        return visitables
    }

    fun showTyping() {
        if (this.visitables.none { it == typingModel }) {
            this.visitables.add(0, typingModel)
            notifyItemInserted(0)
        }
    }

    fun removeTyping() {
        var index = visitables.indexOf(typingModel)
        if (index != -1) {
            this.visitables.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun isTyping(): Boolean {
        return visitables.contains(typingModel)
    }

    private fun showDateBaseChat(context: Context?, position: Int) {
        context?.run {
            if (position != visitables.size - 1) {
                try {
                    val now = visitables[position] as BaseChatUiModel
                    val myTime = (now.replyTime!!).toLong() / SECONDS
                    var prevTime: Long = 0

                    if (visitables[position + 1] != null
                            && visitables[position + 1] is BaseChatUiModel) {
                        val prev = visitables[position + 1] as BaseChatUiModel
                        prevTime = (prev.replyTime!!).toLong() / SECONDS
                    }

                    (visitables[position] as BaseChatUiModel)
                            .isShowDate = !compareTime(context, myTime, prevTime)
                } catch (e: NumberFormatException) {
                    (visitables[position] as BaseChatUiModel).isShowDate = false
                } catch (e: Exception) {
                    (visitables[position] as BaseChatUiModel).isShowDate = false
                }

            } else {
                try {
                    (visitables[position] as BaseChatUiModel).isShowDate = true
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }


    private fun showTimeBaseChat(position: Int) {
        if (position != 0) {
            try {

                val now: BaseChatUiModel = visitables[position] as BaseChatUiModel
                var next: BaseChatUiModel = visitables[position - 1] as BaseChatUiModel
                val myTime = ((now.replyTime)?.toLong() ?: 0L) / SECONDS
                var nextItemTime: Long = 0

                if (visitables[position - 1] != null
                        && visitables[position - 1] is BaseChatUiModel) {
                    next = visitables[position - 1] as BaseChatUiModel
                    nextItemTime = ((next.replyTime)?.toLong() ?: 0L) / SECONDS
                }

                (visitables[position] as BaseChatUiModel)
                        .isShowTime = !(compareHour(nextItemTime, myTime)
                        && compareSender(now, next))

            } catch (e: NumberFormatException) {
                (visitables[position] as BaseChatUiModel).isShowTime = true
            } catch (e: ClassCastException) {
                (visitables[position] as BaseChatUiModel).isShowTime = true
            }

        } else {
            try {
                (visitables[position] as BaseChatUiModel).isShowTime = true
            } catch (e: ClassCastException) {
                e.printStackTrace()
            }

        }
    }

    private fun showRoleBaseChat(position: Int) {
        if (position != visitables.size - 1) {
            try {
                val now = visitables[position] as SendableUiModel
                if (!now.isSender) {
                    now.isShowRole = false
                    return
                }

                var prev: SendableUiModel? = null
                val myTime = ((now.replyTime)?.toLong() ?: 0) / SECONDS
                var prevTime: Long = 0

                if (visitables[position + 1] != null && visitables[position + 1] is SendableUiModel) {
                    prev = visitables.get(position + 1) as SendableUiModel
                    if (prev.replyTime != null) {
                        prevTime = (prev.replyTime)!!.toLong() / SECONDS
                    }
                }

                (visitables.get(position) as SendableUiModel).isShowRole = !(prev != null
                        && compareSender(now, prev)
                        && compareHour(myTime, prevTime))
            } catch (e: NumberFormatException) {
                (visitables.get(position) as SendableUiModel).isShowRole = false
            } catch (e: ClassCastException) {
                (visitables.get(position) as SendableUiModel).isShowRole = false
            }

        } else {
            try {
                (visitables.get(position) as SendableUiModel).isShowRole = true
            } catch (e: ClassCastException) {
                e.printStackTrace()
            }

        }
    }

    private fun compareHour(calCurrent: Long, calBefore: Long): Boolean {
        val MILIS: Long = 1000
        val SECONDS: Long = 60
        val MINUTES = MILIS * SECONDS
        return calCurrent / MINUTES == calBefore / MINUTES
    }

    private fun compareSender(current: BaseChatUiModel?, compare: BaseChatUiModel?): Boolean {
        if (current == null || compare == null) return false

        val currentIsSender: Boolean
        val compareIsSender: Boolean
        if (current is SendableUiModel && compare is SendableUiModel) {
            currentIsSender = current.isSender
            compareIsSender = compare.isSender
            if (!currentIsSender) return currentIsSender == compareIsSender
        }

        return current.fromRole == compare.fromRole
    }

    protected fun compareTime(context: Context, calCurrent: Long, calBefore: Long): Boolean {
        return DateFormat.getLongDateFormat(context).format(Date(calCurrent)) == DateFormat.getLongDateFormat(context).format(Date(calBefore))
    }

    protected open fun enableShowDate(): Boolean {
        return true
    }

    protected open fun enableShowTime(): Boolean {
        return true
    }

    override fun addElement(item: Visitable<*>) {
        visitables.add(0, item)
        notifyItemInserted(0)
        if (visitables.size > 1) notifyItemRangeChanged(0, 1)
    }

    fun addNewMessage(item: Visitable<*>) {
        addElement(item)
    }

    fun removeDummy(visitable: Visitable<*>) {
        if (visitable is SendableUiModel && visitables.isNotEmpty()) {
            val iter = visitables.iterator()

            while (iter.hasNext()) {
                val chatItem = iter.next()
                if (chatItem is SendableUiModel
                        && chatItem.isDummy
                        && chatItem.startTime == visitable.startTime) {
                    val position = this.visitables.indexOf(chatItem)
                    this.visitables.remove(chatItem)
                    notifyItemRemoved(position)
                    break
                }
            }
        }
    }

    fun changeReadStatus() {
        for (i in visitables.indices) {
            val currentItem = visitables.get(i)
            if (currentItem is MessageUiModel && currentItem.isSender) {
                if ((visitables.get(i) as MessageUiModel).isRead) {
                    break
                } else {
                    (visitables.get(i) as MessageUiModel).isRead = true
                    notifyItemRangeChanged(i, 1)
                }
            } else if (currentItem is SendableUiModel && currentItem.isSender) {
                if (!currentItem.isRead) {
                    currentItem.isRead = true
                    notifyItemChanged(i, SendableUiModel.PAYLOAD_EVENT_READ)
                }
            }
        }
    }

}