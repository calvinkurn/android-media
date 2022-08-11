package com.tokopedia.play.ui.chatlist.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.util.extension.append
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.play_common.R as commonR

/**
 * Created by jegul on 04/12/19
 */
class ChatAdapter : ListAdapter<PlayChatUiModel, ChatAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<PlayChatUiModel>() {
        override fun areItemsTheSame(
            oldItem: PlayChatUiModel,
            newItem: PlayChatUiModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PlayChatUiModel,
            newItem: PlayChatUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvChat = itemView.findViewById<Typography>(com.tokopedia.play_common.R.id.tv_chat).apply {
            setType(Typography.DISPLAY_3)
        }

        fun bind(item: PlayChatUiModel) {
            val chatText = constructChatText(
                userName = item.name,
                message = item.message
            )

            tvChat.text = chatText
        }

        private fun constructChatText(
            userName: String,
            message: String,
        ): CharSequence {
            val spanBuilder = SpannableStringBuilder()
            spanBuilder.append(
                text = userName,
                flags = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
                StyleSpan(Typeface.BOLD),
                ForegroundColorSpan(
                    MethodChecker.getColor(
                        itemView.context,
                        commonR.color.play_dms_chat_user_color
                    )
                ),
            )
            spanBuilder.append(" $message")

            return spanBuilder
        }

        companion object {
            fun create(
                parent: ViewGroup,
            ): ViewHolder {
                return ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(commonR.layout.item_play_chat, parent, false))
            }
        }
    }
}