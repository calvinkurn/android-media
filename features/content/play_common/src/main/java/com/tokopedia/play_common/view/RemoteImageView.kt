package com.tokopedia.play_common.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.play_common.R

/**
 * Created by jegul on 27/07/21
 */
class RemoteImageView : AppCompatImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs)
    }

    private var mUrl: String? = null
    private var mCacheStrategy: CacheStrategy = CacheStrategy.None

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.RemoteImageView)

            mUrl = attributeArray.getString(R.styleable.RemoteImageView_imageUrl)
            mCacheStrategy = CacheStrategy.of(attributeArray.getInt(R.styleable.RemoteImageView_cacheStrategy, CacheStrategy.None.value))

            attributeArray.recycle()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        loadRemoteImage()
    }

    private fun loadRemoteImage() {
        val url = mUrl
        if (url.isNullOrBlank()) return

        this.loadImage(url) {
            setCacheStrategy(mCacheStrategy.cacheStrategy)
        }
    }

    enum class CacheStrategy(val value: Int, val cacheStrategy: MediaCacheStrategy) {

        None(0, MediaCacheStrategy.NONE),
        Data(1, MediaCacheStrategy.DATA),
        Resource(2, MediaCacheStrategy.RESOURCE);

        companion object {
            private val values = values()

            fun of(value: Int): CacheStrategy {
                values.forEach {
                    if (it.value == value) return it
                }
                return None
            }
        }
    }
}
