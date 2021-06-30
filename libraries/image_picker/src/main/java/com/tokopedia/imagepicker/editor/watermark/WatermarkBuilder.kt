package com.tokopedia.imagepicker.editor.watermark

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.tokopedia.imagepicker.editor.watermark.builder.Image
import com.tokopedia.imagepicker.editor.watermark.builder.Text
import com.tokopedia.imagepicker.editor.watermark.builder.TextAndImage
import com.tokopedia.imagepicker.editor.watermark.entity.TextAndImageUIModel
import com.tokopedia.imagepicker.editor.watermark.entity.TextUIModel
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.resizeScaledBitmap
import com.tokopedia.imagepicker.editor.watermark.utils.MAX_IMAGE_SIZE
import com.tokopedia.imagepicker.editor.watermark.utils.takeAndEllipsizeOf
import com.yalantis.ucrop.util.FastBitmapDrawable
import android.graphics.Bitmap.Config.ARGB_8888 as ARGB_8888
import android.graphics.Bitmap.createBitmap as createBitmap

open class WatermarkBuilder {

    private var context: Context
    private var backgroundImg: Bitmap? = null
    private var isTitleMode: Boolean = false
    private var isCombine: Boolean = false
    private var onlyWatermark: Boolean = false
    private var resizeBackgroundImg: Boolean
    private var watermarkImage: Image? = null
    private var watermarkText: TextUIModel? = null
    private var watermarkTextAndImage: TextAndImageUIModel? = null

    private constructor(
        context: Context,
        backgroundImg: Bitmap,
        resizeBackgroundImg: Boolean
    ) {
        this.context = context
        this.resizeBackgroundImg = resizeBackgroundImg
        if (resizeBackgroundImg) {
            this.backgroundImg = backgroundImg.resizeScaledBitmap(MAX_IMAGE_SIZE)
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
                .resizeScaledBitmap(MAX_IMAGE_SIZE)
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
        watermarkText = Text().apply {
            text = inputText
        }
    }

    fun loadWatermarkImage(watermarkImage: Bitmap) = apply {
        this.watermarkImage = Image().apply {
            this.image = watermarkImage
        }
    }

    fun loadWatermarkTextImage(text: String, bitmap: Bitmap) = apply {
        this.isCombine = true

        this.watermarkTextAndImage = TextAndImage().apply {
            this.image = bitmap
            this.text = text.takeAndEllipsizeOf()
        }
    }

    fun loadOnlyWatermarkTextImage(text: String, bitmap: Bitmap) = apply {
        this.onlyWatermark = true

        loadWatermarkTextImage(text, bitmap)
    }

    fun setTileMode(tileMode: Boolean) = apply {
        this.isTitleMode = tileMode
    }

    private fun backgroundFromImageView(imageView: ImageView) {
        imageView.invalidate()
        if (imageView.drawable != null) {
            val drawable = imageView.drawable as FastBitmapDrawable
            backgroundImg = if (resizeBackgroundImg) {
                drawable.bitmap.resizeScaledBitmap(MAX_IMAGE_SIZE)
            } else {
                drawable.bitmap
            }
        }
    }

    fun getWatermark(): Watermark {
        return Watermark(
            watermarkTextAndImage = watermarkTextAndImage,
            watermarkImg = watermarkImage,
            watermarkText = watermarkText,
            backgroundImg = backgroundImg,
            onlyWatermark = onlyWatermark,
            isTitleMode = isTitleMode,
            isCombine = isCombine,
            context = context,
        )
    }

    companion object {

        @JvmStatic
        fun createWithSize(context: Context): WatermarkBuilder {
            val bitmap = createBitmap(2000, 2000, ARGB_8888)
            return WatermarkBuilder(context, bitmap)
        }

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