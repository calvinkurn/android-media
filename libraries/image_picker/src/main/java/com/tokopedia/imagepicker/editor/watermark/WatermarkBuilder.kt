package com.tokopedia.imagepicker.editor.watermark

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.tokopedia.imagepicker.editor.watermark.entity.Image
import com.tokopedia.imagepicker.editor.watermark.entity.Position
import com.tokopedia.imagepicker.editor.watermark.entity.Text
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.resizeBitmap
import com.tokopedia.imagepicker.editor.watermark.utils.MAX_IMAGE_SIZE
import com.yalantis.ucrop.util.FastBitmapDrawable

open class WatermarkBuilder {

    private var context: Context
    private var backgroundImg: Bitmap? = null
    private var isTitleMode: Boolean = false
    private var isCombine: Boolean = false
    private var resizeBackgroundImg: Boolean
    private var watermarkImage: Image? = null
    private var watermarkText: Text? = null
    private var watermarkTexts: List<Text> = arrayListOf()
    private var watermarkBitmaps: List<Image> = arrayListOf()

    private constructor(
        context: Context,
        backgroundImg: Bitmap,
        resizeBackgroundImg: Boolean
    ) {
        this.context = context
        this.resizeBackgroundImg = resizeBackgroundImg
        if (resizeBackgroundImg) {
            this.backgroundImg = backgroundImg.resizeBitmap(MAX_IMAGE_SIZE)
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
            this.backgroundImg = BitmapFactory
                .decodeResource(context.resources, backgroundDrawable)
                .resizeBitmap(MAX_IMAGE_SIZE)
        } else {
            this.backgroundImg = BitmapFactory.decodeResource(
                context.resources,
                backgroundDrawable
            )
        }
    }

    constructor(context: Context, backgroundImg: Bitmap) : this(context, backgroundImg, false)

    constructor(context: Context, backgroundImageView: ImageView) : this(context, backgroundImageView, true)

    constructor(context: Context, backgroundDrawable: Int) : this(context, backgroundDrawable, true)

    fun loadWatermarkText(inputText: String) = apply {
        watermarkText = Text(text = inputText)
    }

    fun loadWatermarkText(inputText: String, position: Position) = apply {
        watermarkText = Text(text = inputText, position = position)
    }

    fun loadWatermarkText(watermarkString: Text) = apply {
        watermarkText = watermarkString
    }

    fun loadWatermarkTexts(watermarkTexts: List<Text>) = apply {
        this.watermarkTexts = watermarkTexts
    }

    fun loadWatermarkImage(watermarkImage: Bitmap) = apply {
        this.watermarkImage = Image(image = watermarkImage)
    }

    fun loadWatermarkImage(watermarkImage: Bitmap, position: Position) = apply {
        this.watermarkImage = Image(image = watermarkImage, position = position)
    }

    fun loadWatermarkImage(watermarkImage: Image) = apply {
        this.watermarkImage = watermarkImage
    }

    fun loadWatermarkImageAndText(image: Image, text: Text) = apply {
        this.isCombine = true

        this.watermarkImage = image
        this.watermarkText = text
    }

    fun loadWatermarkImages(watermarkImages: List<Image>) = apply {
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
                drawable.bitmap.resizeBitmap(MAX_IMAGE_SIZE)
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
            isTitleMode = isTitleMode,
            isCombine = isCombine
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