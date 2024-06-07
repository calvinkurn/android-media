package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.view.custom.StickerImageView
import com.tokopedia.topchat.common.util.ViewUtil


class StickerViewHolder(
        itemView: View,
        private val listener: Listener?
) : RecyclerView.ViewHolder(itemView) {

    private var stickerImage: StickerImageView? = itemView.findViewById(R.id.iv_sticker)
    private var loader: AnimatedVectorDrawableCompat? = null

    interface Listener {
        fun onClickSticker(sticker: Sticker)
    }

    fun bind(sticker: Sticker) {
        initLoader()
        bindStickerImage(sticker)
        bindStickerImageClick(sticker)
    }

    private fun initLoader() {
        itemView.context?.let {
            loader = AnimatedVectorDrawableCompat.create(
                    it, com.tokopedia.unifycomponents.R.drawable.unify_loader_shimmer
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

    private fun bindStickerImage(sticker: Sticker) {
        stickerImage?.let {
            it.loadImage(sticker.imageUrl) {
                listener(
                    onSuccess = { bitmap, _ ->
                        stickerImage?.setImageBitmap(bitmap)
                        stickerImage?.clipCircle = false
                    }
                )
                loader?.let { loaderDrawable ->
                    setPlaceHolder(loaderDrawable)
                }
                setCacheStrategy(MediaCacheStrategy.DATA)
                isAnimate(false)
            }
        }
    }

    private fun bindStickerImageClick(sticker: Sticker) {
        stickerImage?.setOnClickListener {
            listener?.onClickSticker(sticker)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_sticker
        fun create(parent: ViewGroup, listener: Listener?): StickerViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
            return StickerViewHolder(view, listener)
        }
    }
}
