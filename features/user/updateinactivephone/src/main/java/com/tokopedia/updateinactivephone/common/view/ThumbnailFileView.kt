package com.tokopedia.updateinactivephone.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.tokopedia.kotlin.extensions.view.clearImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.utils.convertToBitmap

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
                val titleAttr = it.getString(R.styleable.ThumbnailFileView_title) ?: ""

                textTitle.text = titleAttr
                if (imageAttr != 0) {
                    setImage(imageAttr)
                }

                it.recycle()
            }
        }
    }

    fun setImage(url: String) {
        imageThumbnail.scaleType = ImageView.ScaleType.CENTER_CROP
        convertToBitmap(url)?.let {
            imageThumbnail.setImageBitmap(it)
        }
    }

    fun setImage(@DrawableRes drawable: Int) {
        imageThumbnail.clearImage()
        imageThumbnail.setImage(drawable, IMAGE_RADIUS)
    }

    companion object {
        const val IMAGE_RADIUS = 6f
    }
}