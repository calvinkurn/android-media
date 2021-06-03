package com.tokopedia.imagepicker.editor.watermark

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.tokopedia.imagepicker.editor.watermark.uimodel.WatermarkImage
import com.tokopedia.imagepicker.editor.watermark.uimodel.WatermarkPosition
import com.tokopedia.imagepicker.editor.watermark.uimodel.WatermarkText
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapUtils
import com.tokopedia.imagepicker.editor.watermark.utils.MAX_IMAGE_SIZE
import com.yalantis.ucrop.util.FastBitmapDrawable

open class WatermarkBuilder {

    private var context: Context
    private var backgroundImg: Bitmap? = null
    private var isTitleMode: Boolean = false
    private var resizeBackgroundImg: Boolean
    private var watermarkImage: WatermarkImage? = null
    private var watermarkText: WatermarkText? = null
    private var watermarkTexts: List<WatermarkText> = arrayListOf()
    private var watermarkBitmaps: List<WatermarkImage> = arrayListOf()

    private constructor(
        context: Context,
        backgroundImg: Bitmap,
        resizeBackgroundImg: Boolean
    ) {
        this.context = context
        this.resizeBackgroundImg = resizeBackgroundImg
        if (resizeBackgroundImg) {
            this.backgroundImg = BitmapUtils.resizeBitmap(backgroundImg, MAX_IMAGE_SIZE)
        } else {
            this.backgroundImg = backgroundImg
        }
    }

    private constructor(
        context: Context,
        backgroundImageView: ImageView,
        resizeBackgroundImg: Boolean
    ) {
        this.context = context
        this.resizeBackgroundImg = resizeBackgroundImg
        backgroundFromImageView(backgroundImageView)
    }

    private constructor(
        context: Context,
        backgroundDrawable: Int,
        resizeBackgroundImg: Boolean
    ) {
        this.context = context
        this.resizeBackgroundImg = resizeBackgroundImg
        if (resizeBackgroundImg) {
            this.backgroundImg = BitmapUtils.resizeBitmap(
                BitmapFactory.decodeResource(context.resources, backgroundDrawable),
                MAX_IMAGE_SIZE
            )
        } else {
            this.backgroundImg = BitmapFactory.decodeResource(
                context.resources,
                backgroundDrawable
            )
        }
    }

    constructor(context: Context, backgroundImg: Bitmap) : this(context, backgroundImg, true)

    constructor(context: Context, backgroundImageView: ImageView) : this(context, backgroundImageView, true)

    constructor(context: Context, backgroundDrawable: Int) : this(context, backgroundDrawable, true)

    fun loadWatermarkText(inputText: String) = apply {
        watermarkText = WatermarkText(text = inputText)
    }

    fun loadWatermarkText(inputText: String, position: WatermarkPosition) = apply {
        watermarkText = WatermarkText(text = inputText, position = position)
    }

    fun loadWatermarkText(watermarkString: WatermarkText) = apply {
        watermarkText = watermarkString
    }

    fun loadWatermarkTexts(watermarkTexts: List<WatermarkText>) = apply {
        this.watermarkTexts = watermarkTexts
    }

    fun loadWatermarkImage(watermarkImage: Bitmap) = apply {
        this.watermarkImage = WatermarkImage(image = watermarkImage)
    }

    fun loadWatermarkImage(watermarkImage: Bitmap, position: WatermarkPosition) = apply {
        this.watermarkImage = WatermarkImage(image = watermarkImage, position = position)
    }

    fun loadWatermarkImage(watermarkImage: WatermarkImage) = apply {
        this.watermarkImage = watermarkImage
    }

    fun loadWatermarkImages(watermarkImages: List<WatermarkImage>) = apply {
        this.watermarkBitmaps = watermarkImages
    }

    fun setTileMode(tileMode: Boolean) = apply {
        this.isTitleMode = tileMode
    }

    private fun backgroundFromImageView(imageView: ImageView) {
        imageView.invalidate()
        if (imageView.drawable != null) {
            val drawable = imageView.drawable as FastBitmapDrawable
            backgroundImg = if (resizeBackgroundImg) {
                BitmapUtils.resizeBitmap(drawable.bitmap, MAX_IMAGE_SIZE)
            } else {
                drawable.bitmap
            }
        }
    }

    fun getWatermark(): Watermark {
        return Watermark(
            context = context,
            backgroundImg = backgroundImg,
            watermarkImg = watermarkImage,
            watermarkBitmaps = watermarkBitmaps,
            watermarkText = watermarkText,
            watermarkTexts = watermarkTexts,
            isTitleMode = isTitleMode
        )
    }

    companion object {

        @JvmStatic
        fun create(context: Context, backgroundImg: Bitmap): WatermarkBuilder {
            return WatermarkBuilder(context, backgroundImg)
        }

        @JvmStatic
        fun create(context: Context, imageView: ImageView): WatermarkBuilder {
            return WatermarkBuilder(context, imageView)
        }

        @JvmStatic
        fun create(context: Context, @DrawableRes backgroundDrawable: Int): WatermarkBuilder {
            return WatermarkBuilder(context, backgroundDrawable)
        }

        @JvmStatic
        fun create(context: Context, backgroundImg: Bitmap, resizeBackgroundImg: Boolean): WatermarkBuilder {
            return WatermarkBuilder(context, backgroundImg, resizeBackgroundImg)
        }

        @JvmStatic
        fun create(context: Context, imageView: ImageView, resizeBackgroundImg: Boolean): WatermarkBuilder {
            return WatermarkBuilder(context, imageView, resizeBackgroundImg)
        }

        @JvmStatic
        fun create(context: Context, @DrawableRes backgroundDrawable: Int, resizeBackgroundImg: Boolean): WatermarkBuilder {
            return WatermarkBuilder(context, backgroundDrawable, resizeBackgroundImg)
        }

    }

}