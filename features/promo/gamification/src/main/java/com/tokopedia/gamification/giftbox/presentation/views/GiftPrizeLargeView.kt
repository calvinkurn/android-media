package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.gamification.R
import com.tokopedia.utils.image.ImageUtils

class GiftPrizeLargeView : FrameLayout {

    lateinit var image: AppCompatImageView
    lateinit var tvTitle: AppCompatTextView
    lateinit var tvDescription: AppCompatTextView
    lateinit var tvMessage: AppCompatTextView

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

    fun getLayout() = com.tokopedia.gamification.R.layout.view_gift_benefits_large

    fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(getLayout(), this, true)

        image = findViewById(R.id.image)
        tvTitle = findViewById(R.id.tvTitle)
        tvDescription = findViewById(R.id.tvDesc)
        tvMessage = findViewById(R.id.tvMessage)
    }

    fun setData(imageUrl: String, text: List<String>?, imageCallback: ((isLoaded: Boolean) -> Unit)) {
        Glide.with(image)
                .load(imageUrl)
                .addListener(getGlideListener(imageCallback))
                .dontAnimate()
                .into(image)
        val tvList = arrayListOf<AppCompatTextView>(tvTitle, tvDescription, tvMessage)
        text?.forEachIndexed { index, s ->
            tvList[index].text = s
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