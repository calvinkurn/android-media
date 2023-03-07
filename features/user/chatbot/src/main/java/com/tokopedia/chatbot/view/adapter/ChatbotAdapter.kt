package com.tokopedia.chatbot.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.view.adapter.BaseChatAdapter
import com.tokopedia.chatbot.data.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.data.videoupload.VideoUploadUiModel
import com.tokopedia.chatbot.domain.pojo.senderinfo.SenderInfoData
import com.tokopedia.chatbot.util.ChatDiffUtil
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatbotAdapterListener

/**
 * @author by nisie on 27/11/18.
 */

class ChatbotAdapter(private val adapterTypeFactory: ChatbotTypeFactoryImpl) :
    BaseChatAdapter(adapterTypeFactory), ChatbotAdapterListener {

    override fun enableShowTime(): Boolean = false

    override fun getItemViewType(position: Int): Int {
        val default = super.getItemViewType(position)
        return adapterTypeFactory.getItemViewType(visitables, position, default)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return adapterTypeFactory.createViewHolder(parent, viewType, this)
    }

    fun showRetryFor(model: ImageUploadUiModel, b: Boolean) {
        val position = visitables.indexOf(model)
        if (position < 0) return
        if (visitables[position] is ImageUploadUiModel) {
            (visitables[position] as ImageUploadUiModel).isRetry = true
            notifyItemChanged(position)
        }
    }

    fun showRetryForVideo(model: VideoUploadUiModel) {
        val position = visitables.indexOf(model)
        if (position < 0) return
        if (visitables[position] is VideoUploadUiModel) {
            (visitables[position] as VideoUploadUiModel).isRetry = true
            notifyItemChanged(position)
        }
    }

    fun removeDummyVideo() {
        if (visitables.isNotEmpty()) {
            val iter = visitables.iterator()

            while (iter.hasNext()) {
                val chatItem = iter.next()
                if (chatItem is VideoUploadUiModel
                    && chatItem.isDummy) {
                    val position = this.visitables.indexOf(chatItem)
                    this.visitables.remove(chatItem)
                    notifyItemRemoved(position)
                    break
                }
            }
        }
    }

    fun getBubblePosition(replyTime: String): Int {
        return visitables.indexOfFirst {
            it is BaseChatUiModel && it.replyTime == replyTime
        }
    }

    override fun isPreviousItemSender(adapterPosition: Int): Boolean {
        val item = visitables.getOrNull(adapterPosition + 1)
        return item is SendableUiModel && item.isSender || item is ChatSepratorUiModel
    }

    fun showBottomLoading() {
        if (visitables.getOrNull(0) !is LoadingMoreModel) {
            visitables.add(0, loadingMoreModel)
            notifyItemInserted(0)
        }
    }

    fun getMostRecentTokopediaCareMessage(): Int {
        var item = DEFAULT_POSITION_FOR_TKPD_CARD_BUBBLE
        visitables.forEachIndexed { index, visitable ->
            try {
                if (visitable is SendableUiModel && convertToSenderInfo(visitable.source)?.name == TOKOPEDIA_CARE) {
                    item = index
                    return item
                }
            } catch (_: Exception) {

            }
        }
        return item
    }

    private fun convertToSenderInfo(source: String): SenderInfoData? {
        val senderInfoPrefix = SENDER_INFO_PREFIX
        if (source.isNotEmpty() && source.startsWith(senderInfoPrefix)) {
            val s = source.substring(senderInfoPrefix.length, source.length)
            return GsonBuilder().create()
                .fromJson<SenderInfoData>(s,
                    SenderInfoData::class.java)
        } else return null
    }


    fun showTopLoading() {
        if (visitables.isEmpty()) {
            visitables.add(visitables.size, loadingMoreModel)
            notifyItemInserted(visitables.size)
        } else if (visitables[visitables.size - 1] !is LoadingMoreModel) {
            visitables.add(visitables.size, loadingMoreModel)
        }
        notifyItemInserted(visitables.size)
    }

    fun hideBottomLoading() {
        if (visitables.getOrNull(0) is LoadingMoreModel) {
            visitables.removeAt(0)
            notifyItemRemoved(0)
        }
    }

    fun hideTopLoading() {
        if (visitables.getOrNull(visitables.size - 1) is LoadingMoreModel) {
            visitables.removeAt(visitables.size - 1)
            notifyItemRemoved(visitables.size - 1)
        }
    }

    fun addTopData(listChat: List<Visitable<*>>) {
        if (listChat == null || listChat.isEmpty()) return
        val oldList = ArrayList(this.visitables)
        val newList = this.visitables.apply {
            addAll(listChat)
        }
        val diffUtil = ChatDiffUtil(oldList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        diffResult.dispatchUpdatesTo(this)
    }

    fun addBottomData(listChat: List<Visitable<*>>) {
        val oldList = ArrayList(this.visitables)
        val newList = this.visitables.apply {
            addAll(0, listChat)
        }

        val diffUtil = ChatDiffUtil(oldList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        diffResult.dispatchUpdatesTo(this)
    }

    fun reset() {
        visitables.clear()
        notifyDataSetChanged()
    }

    companion object {
        const val TOKOPEDIA_CARE = "Tokopedia Care"
        const val SENDER_INFO_PREFIX = "chatbot_"
        const val DEFAULT_POSITION_FOR_TKPD_CARD_BUBBLE = -1
    }
}
