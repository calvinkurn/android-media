package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.transition.Slide
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.chat_common.data.SendableViewModel.Companion.PAYLOAD_EVENT_READ
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr.StickerProfile
import com.tokopedia.topchat.chatroom.view.uimodel.StickerUiModel

class StickerMessageViewHolder(itemView: View?) : BaseChatViewHolder<StickerUiModel>(itemView) {

    private var statusFooter: LinearLayout? = itemView?.findViewById(R.id.ll_footer)
    private var stickerImage: ImageView? = itemView?.findViewById(R.id.iv_sticker_message)
    private var loader: AnimatedVectorDrawableCompat? = null

    override fun alwaysShowTime(): Boolean = true

    override fun bind(message: StickerUiModel?) {
        if (message == null) return
        super.bind(message)
        initLoader()
        bindStickerImage(message.sticker)
        alignLayout(message)
        bindChatReadStatus(message)
    }

    override fun bind(message: StickerUiModel?, payloads: List<Any>) {
        if (payloads.isEmpty() || message == null) return
        val payload = payloads[0]
        if (payload === PAYLOAD_EVENT_READ && message.isSender) {
            bindChatReadStatus(message)
        }
    }

    private fun initLoader() {
        itemView.context?.let {
            loader = AnimatedVectorDrawableCompat.create(it, com.tokopedia.unifycomponents.R.drawable.unify_loader)
            loader?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    stickerImage?.handler?.post {
                        loader?.start()
                    }
                }
            })
            loader?.start()
        }
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
            setLayoutGravity(stickerImage, Gravity.END)
            setLayoutGravity(statusFooter, Gravity.END)
        } else {
            setLayoutGravity(stickerImage, Gravity.START)
            setLayoutGravity(statusFooter, Gravity.START)
        }
    }

    private fun setLayoutGravity(view: View?, @Slide.GravityFlag gravity: Int) {
        view?.layoutParams?.let { lp ->
            (lp as LinearLayout.LayoutParams).apply {
                this.gravity = gravity
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_sticker_message
    }
}