package com.tokopedia.topchat.chatroom.view.adapter

import android.content.Context
import android.os.Parcelable
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.BaseChatAdapter
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.util.ChatRoomDiffUtil
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ProductCarouselListAttachmentViewHolder
import com.tokopedia.topchat.chatroom.view.uimodel.HeaderDateUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.ProductCarouselUiModel

/**
 * @author : Steven 02/01/19
 */
class TopChatRoomAdapter(
        private val context: Context?,
        private val adapterTypeFactory: TopChatTypeFactoryImpl
) : BaseChatAdapter(adapterTypeFactory), ProductCarouselListAttachmentViewHolder.Listener {

    private val productCarouselState: ArrayMap<Int, Parcelable> = ArrayMap()
    private var bottomMostHeaderDate: HeaderDateUiModel? = null
    private var topMostHeaderDate: HeaderDateUiModel? = null
    private var topMostHeaderDateIndex: Int? = null

    override fun enableShowDate(): Boolean = false
    override fun enableShowTime(): Boolean = false

    override fun getItemViewType(position: Int): Int {
        val default = super.getItemViewType(position)
        return adapterTypeFactory.getItemViewType(visitables, position, default)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return adapterTypeFactory.createViewHolder(parent, viewType, this)
    }

    override fun saveProductCarouselState(position: Int, state: Parcelable?) {
        state?.let {
            productCarouselState[position] = it
        }
    }

    override fun getSavedCarouselState(position: Int): Parcelable? {
        return productCarouselState[position]
    }

    override fun addElement(visitables: MutableList<out Visitable<Any>>?) {
        addTopData(visitables)
    }

    fun showRetryFor(model: ImageUploadViewModel, b: Boolean) {
        val position = visitables.indexOf(model)
        if (position < 0) return
        if (visitables[position] is ImageUploadViewModel) {
            (visitables[position] as ImageUploadViewModel).isRetry = true
            notifyItemChanged(position)
        }
    }

    fun addWidgetHeader(widgets: List<Visitable<TopChatTypeFactory>>) {
        if (widgets.isEmpty()) return
        val currentLastPosition = visitables.lastIndex
        visitables.addAll(widgets)
        notifyItemRangeInserted(currentLastPosition, widgets.size)
    }

    fun removeLastHeaderDateIfSame(chatRoom: ChatroomViewModel) {
        if (chatRoom.listChat.isEmpty()) return
        assignLastHeaderDate()
        if (this.topMostHeaderDate?.date == chatRoom.latestHeaderDate) {
            topMostHeaderDateIndex?.let {
                visitables.removeAt(it)
                notifyItemRemoved(it)
            }
        }
    }

    fun removeLatestHeaderDateIfSame(newBottomList: ArrayList<Visitable<*>>) {
        if (newBottomList.isEmpty()) return
        val newListOldestDate = newBottomList[newBottomList.lastIndex]
        if (newListOldestDate is HeaderDateUiModel) {
            if (newListOldestDate == this.bottomMostHeaderDate) {
                newBottomList.removeAt(newBottomList.lastIndex)
            }
        }
    }

    fun setLatestHeaderDate(latestHeaderDate: String) {
        this.bottomMostHeaderDate = HeaderDateUiModel(latestHeaderDate)
    }

    fun addHeaderDateIfDifferent(visitable: Visitable<*>) {
        if (visitable is BaseChatViewModel) {
            val chatTime = visitable.replyTime?.toLong()?.div(SECONDS) ?: return
            val previousChatTime = bottomMostHeaderDate?.dateTimestamp ?: return
            if (!sameDay(chatTime, previousChatTime)) {
                bottomMostHeaderDate = HeaderDateUiModel(chatTime)
                visitables.add(0, bottomMostHeaderDate)
                notifyItemInserted(0)
            }
        }
    }

    fun updateAttachmentView(firstVisible: Int, lastVisible: Int, attachments: ArrayMap<String, Attachment>) {
        if (firstVisible > lastVisible) return
        if (firstVisible < 0 || lastVisible >= visitables.size) return
        for (itemPosition in firstVisible..lastVisible) {
            val item = visitables[itemPosition]
            if (item is DeferredAttachment && attachments.containsKey(item.id)) {
                val attachment = attachments[item.id] ?: continue
                if (item.isLoading) {
                    if (attachment is ErrorAttachment) {
                        item.syncError()
                    } else {
                        item.updateData(attachment.parsedAttributes)
                    }
                    notifyItemChanged(itemPosition, DeferredAttachment.PAYLOAD_DEFERRED)
                }
            }
            if (item is ProductCarouselUiModel) {
                notifyItemChanged(itemPosition, DeferredAttachment.PAYLOAD_DEFERRED)
            }
        }
    }

    private fun assignLastHeaderDate() {
        if (visitables.size <= 1) return
        val topMostDateHeaderItemIndex = visitables.size - 2
        val topMostDateHeaderItem = visitables[topMostDateHeaderItemIndex]
        if (topMostDateHeaderItem is HeaderDateUiModel) {
            topMostHeaderDate = topMostDateHeaderItem
            topMostHeaderDateIndex = topMostDateHeaderItemIndex
        }
    }

    private fun sameDay(chatTime: Long?, previousChatTime: Long?): Boolean {
        if (context == null || chatTime == null || previousChatTime == null) return false
        return compareTime(context, chatTime, previousChatTime)
    }

    fun dataExistAt(position: Int): Boolean {
        return visitables.getOrNull(position) != null
    }

    fun showBottomLoading() {
        if (visitables[0] !is LoadingMoreModel) {
            visitables.add(0, loadingMoreModel)
            notifyItemInserted(0)
        }
    }

    fun hideBottomLoading() {
        if (visitables[0] is LoadingMoreModel) {
            visitables.removeAt(0)
            notifyItemRemoved(0)
        }
    }

    fun addBottomData(listChat: List<Visitable<*>>) {
        val oldList = ArrayList(this.visitables)
        val newList = this.visitables.apply {
            addAll(0, listChat)
        }
        val diffUtil = ChatRoomDiffUtil(oldList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun addTopData(listChat: MutableList<out Visitable<Any>>?) {
        if (listChat == null || listChat.isEmpty()) return
        val oldList = ArrayList(this.visitables)
        val newList = this.visitables.apply {
            addAll(listChat)
        }
        val diffUtil = ChatRoomDiffUtil(oldList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        diffResult.dispatchUpdatesTo(this)
    }

    fun reset() {
        visitables.clear()
        bottomMostHeaderDate = null
        topMostHeaderDate = null
        topMostHeaderDateIndex = null
        notifyDataSetChanged()
    }
}