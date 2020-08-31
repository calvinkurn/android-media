package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx


class GiftPrizeSmallView : FrameLayout {
    lateinit var image: AppCompatImageView
    lateinit var tvTitle: AppCompatTextView

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        init(attrs)
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    fun getLayout() = com.tokopedia.gamification.R.layout.view_gift_benefits_small

    fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        image = findViewById(R.id.image)
        tvTitle = findViewById(R.id.tvTitle)
        setShadows()
    }

    fun setShadows() {
        val shadowColor = ContextCompat.getColor(context, com.tokopedia.gamification.R.color.gf_box_text_shadow)
        val shadowRadius = tvTitle.dpToPx(5)
        val shadowOffset = tvTitle.dpToPx(4)
        tvTitle.setShadowLayer(shadowRadius, 0f, shadowOffset, shadowColor)
    }

    fun setData(imageUrl: String, textList: List<String>?, imageCallback: ((isLoaded: Boolean) -> Unit)) {
        Glide.with(image)
                .load(imageUrl)
                .addListener(getGlideListener(imageCallback))
                .dontAnimate()
                .into(image)
        if (!textList.isNullOrEmpty()) {
            tvTitle.text = textList[0]
        }
    }

    fun getGlideListener(imageCallback: ((isLoaded: Boolean) -> Unit)): RequestListener<Drawable> {
        return object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                imageCallback.invoke(false)
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                imageCallback.invoke(true)
                return false
            }
        }
    }
}