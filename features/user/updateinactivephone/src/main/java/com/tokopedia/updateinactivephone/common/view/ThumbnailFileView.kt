package com.tokopedia.updateinactivephone.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.updateinactivephone.R

class ThumbnailFileView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var imageThumbnail: ImageUnify
    private var textTitle: Typography
    private var imageEdit: ImageView

    var title: String
        get() = textTitle.text.toString()
        set(value) {
            textTitle.text = value
        }

    init {
        val layout: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layout.inflate(R.layout.view_thumbnail_file, this, true)

        imageThumbnail = view.findViewById(R.id.imageThumbnailFile)
        textTitle = view.findViewById(R.id.labelThumbnailFile)
        imageEdit = view.findViewById(R.id.imageEditThumbnailFile)

        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ThumbnailFileView, 0, 0)
            typedArray?.let {
                val imageAttr = it.getInteger(R.styleable.ThumbnailFileView_image, 0)
                val titleAttr = it.getString(R.styleable.ThumbnailFileView_title).orEmpty()

                textTitle.text = titleAttr
                if (imageAttr != 0) {
                    setImage(imageAttr)
                }

                it.recycle()
            }
        }
    }

    fun setImage(url: String) {
        imageThumbnail.apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            loadImage(url) {
                useCache(false)
                setCacheStrategy(MediaCacheStrategy.NONE)
            }
        }
    }

    private fun setImage(@DrawableRes drawable: Int) {
        imageThumbnail.loadImage(drawable)
    }
}