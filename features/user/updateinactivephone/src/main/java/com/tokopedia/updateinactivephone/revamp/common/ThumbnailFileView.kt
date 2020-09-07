package com.tokopedia.updateinactivephone.revamp.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.tokopedia.utils.image.ImageUtils
import java.io.File

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
        ImageUtils.loadImage(imageThumbnail, url)
    }

    fun setImage(file: File) {
        imageThumbnail.clearImage()
        imageThumbnail.setImage(file.absolutePath, IMAGE_RADIUS)
        requestLayout()
    }

    fun setImage(@DrawableRes drawable: Int) {
        imageThumbnail.clearImage()
        imageThumbnail.setImage(drawable, IMAGE_RADIUS)
        requestLayout()
    }

    private fun convertToBitmap(path: String): Bitmap {
        return BitmapFactory.decodeFile(path)
    }

    private fun convertToBitmap(file: File): Bitmap {
        return convertToBitmap(file.absolutePath)
    }

    companion object {
        const val IMAGE_RADIUS = 6f
    }
}