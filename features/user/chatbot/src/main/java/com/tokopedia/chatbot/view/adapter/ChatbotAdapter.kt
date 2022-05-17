package com.tokopedia.chatbot.view.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.BaseChatAdapter
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chatbot.data.seprator.ChatSepratorViewModel
import com.tokopedia.chatbot.util.ChatDiffUtil
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatbotAdapterListener

/**
 * @author by nisie on 27/11/18.
 */

class ChatbotAdapter(private val adapterTypeFactory: ChatbotTypeFactoryImpl)
    : BaseChatAdapter(adapterTypeFactory), ChatbotAdapterListener {

    /**
     * String - the replyId or localId
     * BaseChatViewModel - the bubble/reply
     */
    private var replyMap: ArrayMap<String, BaseChatUiModel> = ArrayMap()
    private var offset = 0



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

    fun getBubblePosition(replyTime: String): Int {
        return visitables.indexOfFirst {
            if (it is BaseChatUiModel)
            Log.d("FATAL", "getBubblePosition: " + it.replyTime + " Params : " + replyTime)
            it is BaseChatUiModel && it.replyTime == replyTime
        }
    }

    override fun isPreviousItemSender(adapterPosition: Int): Boolean {
        val item = visitables.getOrNull(adapterPosition + 1)
        return if (item is SendableUiModel && item.isSender || item is ChatSepratorViewModel) {
            true
        } else false
    }

    fun showBottomLoading(){
        if (visitables[0] !is LoadingMoreModel){
            visitables.add(0,loadingMoreModel)
            notifyItemInserted(0)
        }
    }

    fun hideBottomLoading() {
        if (visitables[0] is LoadingMoreModel) {
            visitables.removeAt(0)
            notifyItemRemoved(0)
        }
    }

    override fun addElement(visitables: MutableList<out Visitable<Any>>?) {
        addTopData(visitables)
    }


    private fun addTopData(listChat: List<Visitable<Any>>?) {
        if (listChat == null || listChat.isEmpty()) return
        mapListChat(listChat)
        val oldList = ArrayList(this.visitables)
        val newList = this.visitables.apply {
            addAll(listChat)
        }
        val diffUtil = ChatDiffUtil(oldList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        diffResult.dispatchUpdatesTo(this)
    }


    private fun getOffsetSafely(): Int {
        return if (visitables.size < offset) {
            visitables.size
        } else {
            offset
        }
    }

    fun addBottomData(listChat: List<Visitable<*>>) {
        mapListChat(listChat)
        val oldList = ArrayList(this.visitables)
        val newList = this.visitables.apply {
            addAll(0, listChat)
        }
        val diffUtil = ChatDiffUtil(oldList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun mapListChat(listChat: List<Visitable<*>>) {
        listChat.filterIsInstance(BaseChatUiModel::class.java)
            .forEach {
                val id = it.localId
                if (id.isEmpty()) return@forEach
                replyMap[id] = it
            }
    }

    fun reset() {
        visitables.clear()
        notifyDataSetChanged()
    }

}