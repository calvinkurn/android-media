package com.tokopedia.chatbot.view.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.BaseChatAdapter
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chatbot.data.seprator.ChatSepratorViewModel
import com.tokopedia.chatbot.data.videoupload.VideoUploadUiModel
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatbotAdapterListener

/**
 * @author by nisie on 27/11/18.
 */

class ChatbotAdapter(private val adapterTypeFactory: ChatbotTypeFactoryImpl)
    : BaseChatAdapter(adapterTypeFactory), ChatbotAdapterListener {

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

    fun showRetryForVideo(model: VideoUploadUiModel, b: Boolean) {
        val position = visitables.indexOf(model)
        if (position < 0) return
        if (visitables[position] is VideoUploadUiModel) {
            (visitables[position] as VideoUploadUiModel).isRetry = true
            notifyItemChanged(position)
        }
    }

    override fun isPreviousItemSender(adapterPosition: Int): Boolean {
        val item = visitables.getOrNull(adapterPosition + 1)
        return if (item is SendableUiModel && item.isSender || item is ChatSepratorViewModel) {
            true
        } else false
    }
}