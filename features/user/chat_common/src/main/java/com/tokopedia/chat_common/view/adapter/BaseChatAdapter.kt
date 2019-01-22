package com.tokopedia.chat_common

import android.content.Context
import android.text.format.DateFormat
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.data.SendableViewModel
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

    private val MILISECONDS: Long = 1000000

    var typingModel = TypingChatModel()

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        if (visitables[position] is BaseChatViewModel) {
            if (enableShowDate()) showDateBaseChat(holder.itemView.context, holder.adapterPosition)
            if (enableShowTime()) showTimeBaseChat(holder.adapterPosition)
        }

        if (visitables[position] is SendableViewModel) {
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
//
//        if (holder is ImageDualAnnouncementViewHolder) {
//            (holder as ImageDualAnnouncementViewHolder).onViewRecycled()
//        } else if (holder is ProductAttachmentViewHolder) {
//            (holder as ProductAttachmentViewHolder).onViewRecycled()
//        } else if (holder is AttachedInvoiceSentViewHolder) {
//            (holder as AttachedInvoiceSentViewHolder).onViewRecycled()
//        }
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
        if(!this.visitables.any{it == typingModel}) {
            this.visitables.add(0, typingModel)
            notifyItemInserted(0)
        }
    }

    fun removeTyping() {
        var index = visitables.indexOf(typingModel)
        var isContainsTyping = this.visitables.remove(typingModel)
        if (isContainsTyping) {
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
                    val now = visitables[position] as BaseChatViewModel
                    val myTime = java.lang.Long.parseLong(now.replyTime) / MILISECONDS
                    var prevTime: Long = 0

                    if (visitables[position + 1] != null
                            && visitables[position + 1] is BaseChatViewModel) {
                        val prev = visitables[position + 1] as BaseChatViewModel
                        prevTime = java.lang.Long.parseLong(prev.replyTime) / MILISECONDS
                    }

                    (visitables[position] as BaseChatViewModel)
                            .isShowDate = !compareTime(context, myTime, prevTime)
                } catch (e: NumberFormatException) {
                    (visitables[position] as BaseChatViewModel).isShowDate = false
                } catch (e: ClassCastException) {
                    (visitables[position] as BaseChatViewModel).isShowDate = false
                }

            } else {
                try {
                    (visitables[position] as BaseChatViewModel).isShowDate = true
                } catch (e: ClassCastException) {
                    e.printStackTrace()
                }

            }
        }
    }


    private fun showTimeBaseChat(position: Int) {
        if (position != 0) {
            try {

                val now: BaseChatViewModel = visitables[position] as BaseChatViewModel
                var next: BaseChatViewModel = visitables[position - 1] as BaseChatViewModel
                val myTime = java.lang.Long.parseLong(now.replyTime) / MILISECONDS
                var nextItemTime: Long = 0

                if (visitables[position - 1] != null
                        && visitables[position - 1] is BaseChatViewModel) {
                    next = visitables[position - 1] as BaseChatViewModel
                    nextItemTime = java.lang.Long.parseLong(next.replyTime) / MILISECONDS
                }

                (visitables[position] as BaseChatViewModel)
                        .isShowTime = !(compareHour(nextItemTime, myTime)
                        && compareSender(now, next))

            } catch (e: NumberFormatException) {
                (visitables[position] as BaseChatViewModel).isShowTime = true
            } catch (e: ClassCastException) {
                (visitables[position] as BaseChatViewModel).isShowTime = true
            }

        } else {
            try {
                (visitables[position] as BaseChatViewModel).isShowTime = true
            } catch (e: ClassCastException) {
                e.printStackTrace()
            }

        }
    }

    private fun showRoleBaseChat(position: Int) {
        if (position != visitables.size - 1) {
            try {
                val now = visitables[position] as SendableViewModel
                if (!now.isSender) {
                    now.isShowRole = false
                    return
                }

                var prev: SendableViewModel? = null
                val myTime = java.lang.Long.parseLong(now.replyTime) / MILISECONDS
                var prevTime: Long = 0

                if (visitables[position + 1] != null && visitables[position + 1] is SendableViewModel) {
                    prev = visitables.get(position + 1) as SendableViewModel
                    if (prev.replyTime != null) {
                        prevTime = (prev!!.replyTime)!!.toLong() / MILISECONDS
                    }
                }

                (visitables.get(position) as SendableViewModel).isShowRole = !(prev != null
                        && compareSender(now, prev)
                        && compareHour(myTime, prevTime))
            } catch (e: NumberFormatException) {
                (visitables.get(position) as SendableViewModel).isShowRole = false
            } catch (e: ClassCastException) {
                (visitables.get(position) as SendableViewModel).isShowRole = false
            }

        } else {
            try {
                (visitables.get(position) as SendableViewModel).isShowRole = true
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

    private fun compareSender(current: BaseChatViewModel?, compare: BaseChatViewModel?): Boolean {
        if (current == null || compare == null) return false

        val currentIsSender: Boolean
        val compareIsSender: Boolean
        if (current is SendableViewModel && compare is SendableViewModel) {
            currentIsSender = current.isSender
            compareIsSender = compare.isSender
            if (!currentIsSender) return currentIsSender == compareIsSender
        }

        return current.fromRole == compare.fromRole
    }

    private fun compareTime(context: Context, calCurrent: Long, calBefore: Long): Boolean {
        return DateFormat.getLongDateFormat(context).format(Date(calCurrent)) == DateFormat.getLongDateFormat(context).format(Date(calBefore))
    }

    protected fun enableShowDate(): Boolean {
        return true
    }

    protected fun enableShowTime(): Boolean {
        return true
    }

    override fun addElement(item: Visitable<*>) {
        visitables.add(0, item)
        notifyItemInserted(0)
        if (visitables.size > 1) notifyItemRangeChanged(0, 1)
    }

    fun addNewMessage(item: Visitable<*>){
        addElement(item)
    }

    fun removeDummy(visitable: Visitable<*>) {
        if (visitable is SendableViewModel && visitables.isNotEmpty()) {
            val iter = visitables.iterator()

            while (iter.hasNext()) {
                val chatItem = iter.next()
                if (chatItem is SendableViewModel
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
            if (currentItem is MessageViewModel && currentItem.isSender) {
                if ((visitables.get(i) as MessageViewModel).isRead) {
                    break
                } else {
                    (visitables.get(i) as MessageViewModel).isRead = true
                    notifyItemRangeChanged(i, 1)
                }
            }
        }
    }

}