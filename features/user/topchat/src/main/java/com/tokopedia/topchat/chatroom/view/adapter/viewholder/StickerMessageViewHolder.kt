package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.chat_common.data.SendableUiModel.Companion.PAYLOAD_EVENT_READ
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr.StickerProfile
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.Payload
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ChatMessageViewHolderBinder.generateLeftBg
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ChatMessageViewHolderBinder.generateRightBg
import com.tokopedia.topchat.chatroom.view.custom.message.ReplyBubbleAreaMessage
import com.tokopedia.topchat.chatroom.view.uimodel.StickerUiModel
import com.tokopedia.topchat.common.util.ViewUtil

class StickerMessageViewHolder(
    itemView: View?,
    private val replyBubbleListener: ReplyBubbleAreaMessage.Listener
) : BaseChatViewHolder<StickerUiModel>(itemView) {

    private var replyBubbleArea: ReplyBubbleAreaMessage? = itemView?.findViewById(
        R.id.rba_sticker
    )
    private var stickerContainer: LinearLayout? = itemView?.findViewById(
        R.id.sticker_container
    )
    private var stickerBodyContainer: LinearLayout? = itemView?.findViewById(
        R.id.ll_sticker_body_container
    )
    private var msgOffsetLine: View? = itemView?.findViewById(
        R.id.v_sticker_offset
    )
    private var statusFooter: LinearLayout? = itemView?.findViewById(R.id.ll_footer)
    private var stickerImage: ImageView? = itemView?.findViewById(R.id.iv_sticker_message)
    private var loader: AnimatedVectorDrawableCompat? = null

    private val bgLeft = generateLeftBg(stickerBodyContainer, false)
    private val bgRight = generateRightBg(stickerBodyContainer, false)

    override fun alwaysShowTime(): Boolean = true

    override fun bind(message: StickerUiModel?, payloads: List<Any>) {
        if (payloads.isEmpty() || message == null) return
        when (payloads.first()) {
            Payload.REBIND -> bind(message)
            PAYLOAD_EVENT_READ -> {
                if (message.isSender) {
                    bindChatReadStatus(message)
                }
            }
        }
    }

    override fun bind(message: StickerUiModel) {
        super.bind(message)
        initLoader()
        bindStickerImage(message.sticker)
        alignLayout(message)
        bindChatReadStatus(message)
        bindReplyBubbleListener()
        bindReplyReference(message)
        bindBackground(message)
        bindMsgOffsetLine(message)
    }

    private fun bindReplyBubbleListener() {
        replyBubbleArea?.setReplyListener(replyBubbleListener)
    }

    private fun bindReplyReference(message: StickerUiModel) {
        replyBubbleArea?.bindReplyData(message)
    }

    private fun bindBackground(message: StickerUiModel) {
        var bg = if (message.isSender) {
            bgRight
        } else {
            bgLeft
        }
        if (!message.hasReplyBubble()) {
            bg = null
        }
        stickerBodyContainer?.background = bg
    }

    private fun bindMsgOffsetLine(message: StickerUiModel) {
        val msgOffsetLp = msgOffsetLine?.layoutParams as? ViewGroup.MarginLayoutParams
        val bottomMargin: Int = if (message.hasReplyBubble()) {
            itemView.context.resources.getDimension(R.dimen.dp_topchat_20).toInt()
        } else {
            0
        }
        msgOffsetLp?.bottomMargin = bottomMargin
        msgOffsetLine?.layoutParams = msgOffsetLp
    }

    private fun initLoader() {
        itemView.context?.let {
            loader = AnimatedVectorDrawableCompat.create(
                it,
                com.tokopedia.unifycomponents.R.drawable.unify_loader
            )
            loader?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    stickerImage?.handler?.post {
                        loader?.start()
                    }
                }
            })
            if (ViewUtil.areSystemAnimationsEnabled(itemView.context)) {
                loader?.start()
            }
        }
    }

    override fun onViewRecycled() {
        loader?.clearAnimationCallbacks()
    }

    private fun bindStickerImage(sticker: StickerProfile) {
        stickerImage?.let {
            Glide.with(itemView.context)
                .load(sticker.imageUrl)
                .dontAnimate()
                .placeholder(loader)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(it)
        }
    }

    private fun alignLayout(message: StickerUiModel) {
        if (message.isSender) {
            replyBubbleArea?.alignRight()
            stickerContainer?.gravity = Gravity.END
        } else {
            replyBubbleArea?.alignLeft()
            stickerContainer?.gravity = Gravity.START
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_sticker_message
    }
}