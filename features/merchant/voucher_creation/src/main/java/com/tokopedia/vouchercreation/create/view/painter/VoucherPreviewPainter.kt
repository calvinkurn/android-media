package com.tokopedia.vouchercreation.create.view.painter

import android.content.Context
import android.graphics.*
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IntDef
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageTextType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.enums.getScaledValuePair
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.BannerBaseUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel

class VoucherPreviewPainter(private val context: Context,
                            private val bitmap: Bitmap,
                            var onSuccessGetBitmap: (Bitmap) -> Unit = { _ -> },
                            private val bannerBaseUiModel: BannerBaseUiModel) {

    companion object {
        private const val ASTERISK = "*"
        private const val PERCENT = "%"
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(VoucherValuePosition.TOP, VoucherValuePosition.CENTER, VoucherValuePosition.BOTTOM)
    annotation class VoucherValuePosition {
        companion object {
            const val TOP = 0
            const val CENTER = 1
            const val BOTTOM = 2
        }
    }

    private val canvas = Canvas(bitmap)

    private val wrapContentLayoutParams by lazy {
        LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private val voucherBannerPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
        }
    }
    private val shopAvatarPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
        }
    }
    private val shopNamePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 30f
            typeface = Typeface.DEFAULT_BOLD
        }
    }
    private val promoNamePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 40f
            typeface = Typeface.DEFAULT_BOLD
        }
    }
    private val promoTypePaint by lazy {
        Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
        }
    }
    private val linearLayoutParams by lazy {
        LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            gravity = Gravity.TOP
            topMargin = valueMarginTop
        }
    }
    private val bitmapWidth = bitmap.width
    private val bitmapHeight = bitmap.height
    private val shopAvatarX = (bitmapWidth * 0.07).toInt()
    private val shopAvatarY = (bitmapHeight * 0.22).toInt()
    private val shopNameX = (bitmapWidth * 0.048f)
    private val shopNameY = (bitmapHeight * 0.72f)
    private val promoNameX = (bitmapWidth * 0.42f)
    private val promoNameY = (bitmapHeight * 0.4f)
    private val shopAvatarSize = (bitmapWidth * 0.075).toInt()
    private val middleX = (bitmapWidth * 0.82f)
    private val labelHeight = (bitmapHeight * 0.12).toInt()
    private val topLabelY = (bitmapHeight * 0.1).toInt()
    private val middleLabelY = (bitmapHeight * 0.32).toInt()
    private val bottomLabelY = (bitmapHeight * 0.5).toInt()
    private val topValueY = (bitmapHeight * 0.18f)
    private val middleValueY = (bitmapHeight * 0.4f)
    private val bottomValueY = (bitmapHeight * 0.58f)
    private val valueMarginTop = (bitmapHeight * 0.05).toInt()

    fun drawInitial(uiModel: BannerVoucherUiModel) {
        uiModel.run {
            canvas.drawText(shopName, shopNameX, shopNameY, shopNamePaint)
            Glide.with(context)
                    .asBitmap()
                    .load(shopAvatar)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            val bitmapRect = Rect().apply {
                                set(shopAvatarX, shopAvatarY, shopAvatarX + shopAvatarSize, shopAvatarY + shopAvatarSize)
                            }
                            canvas.drawBitmap(resource, null, bitmapRect, shopAvatarPaint)
                            onSuccessGetBitmap(bitmap)
                            return false
                        }
                    })
                    .submit()
        }
    }

    fun drawFull(uiModel: BannerVoucherUiModel, newBitmap: Bitmap? = null) {
        uiModel.run {
            val bannerRect = Rect().apply {
                set(0, 0, bitmapWidth, bitmapHeight)
            }
            newBitmap?.let {
                canvas.drawBitmap(it, null, bannerRect, voucherBannerPaint)
            }
            canvas.drawText(shopName, shopNameX, shopNameY, shopNamePaint)
            canvas.drawText(promoName, promoNameX, promoNameY, promoNamePaint)
            Glide.with(context)
                    .asBitmap()
                    .load(shopAvatar)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            val bitmapRect = Rect().apply {
                                set(shopAvatarX, shopAvatarY, shopAvatarX + shopAvatarSize, shopAvatarY + shopAvatarSize)
                            }
                            canvas.drawBitmap(resource, null, bitmapRect, shopAvatarPaint)
                            drawVoucherValue(uiModel)
                            return false
                        }
                    })
                    .submit()
        }
    }

    private fun drawVoucherValue(uiModel: BannerVoucherUiModel) {
        uiModel.run {
            when(imageType) {
                is VoucherImageType.FreeDelivery -> {
                    Glide.with(context)
                            .asBitmap()
                            .load(bannerBaseUiModel.freeDeliveryLabelUrl)
                            .listener(object : RequestListener<Bitmap> {
                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                    return false
                                }

                                override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    imageType.value.let {
                                        canvas.drawPromotionLabel(resource, middleLabelY, it, VoucherValuePosition.CENTER)
                                    }
                                    return false
                                }
                            })
                            .submit()
                }
                is VoucherImageType.Rupiah -> {
                    Glide.with(context)
                            .asBitmap()
                            .load(bannerBaseUiModel.cashbackLabelUrl)
                            .listener(object : RequestListener<Bitmap> {
                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                    return false
                                }

                                override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    imageType.value.let {
                                        canvas.drawPromotionLabel(resource, middleLabelY, it, VoucherValuePosition.CENTER)
                                    }
                                    return false
                                }
                            })
                            .submit()

                }
                is VoucherImageType.Percentage -> {
                    Glide.with(context)
                            .asBitmap()
                            .load(bannerBaseUiModel.cashbackLabelUrl)
                            .listener(object : RequestListener<Bitmap> {
                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                    return false
                                }

                                override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    (imageType as? VoucherImageType.Percentage)?.percentage?.let {
                                        canvas.drawPromotionLabel(resource, topLabelY, it, VoucherValuePosition.TOP)
                                    }
                                    return false
                                }
                            })
                            .submit()

                    Glide.with(context)
                            .asBitmap()
                            .load(bannerBaseUiModel.cashbackUntilLabelUrl)
                            .listener(object : RequestListener<Bitmap> {
                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                    return false
                                }

                                override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    (imageType as? VoucherImageType.Percentage)?.value?.let {
                                        canvas.drawPromotionLabel(resource, bottomLabelY, it, VoucherValuePosition.BOTTOM)
                                    }
                                    return false
                                }
                            })
                            .submit()


                }
            }
        }
    }

    private fun Canvas.drawPromotionLabel(resource: Bitmap, yPosition: Int, value: Int, @VoucherValuePosition voucherValuePosition: Int) {
        val bitmapRatio = resource.width / resource.height
        val fittedLabelWidth = bitmapRatio * labelHeight
        val bitmapRect = Rect().apply {
            set(middleX.toInt() - fittedLabelWidth/2, yPosition, middleX.toInt() + fittedLabelWidth/2, yPosition + labelHeight)
        }
        drawBitmap(resource, null, bitmapRect, promoTypePaint)

        drawValueText(value, voucherValuePosition)
    }

    private fun Canvas.drawValueText(value: Int, @VoucherValuePosition voucherValuePosition: Int) {
        val isPercentagePosition = voucherValuePosition == VoucherValuePosition.TOP
        val horizontalLinearLayout = getValueLinearLayout(value, isPercentagePosition)
        val yPosition: Float = when(voucherValuePosition) {
            VoucherValuePosition.TOP -> topValueY
            VoucherValuePosition.CENTER -> middleValueY
            VoucherValuePosition.BOTTOM -> bottomValueY
            else -> return
        }
        drawBitmap(horizontalLinearLayout.toBitmap(null, null), middleX - horizontalLinearLayout.width/2, yPosition, null)
        onSuccessGetBitmap(bitmap)
    }

    private fun getValueLinearLayout(value: Int, isPercentage: Boolean): LinearLayout {
        return if(isPercentage) {
            val valueTextView = getTextView(value.toString(), VoucherImageTextType.VALUE)
            val percentageTextView = getTextView(PERCENT, VoucherImageTextType.SCALE)
            LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                addView(valueTextView)
                addView(percentageTextView)
                layoutParams = wrapContentLayoutParams
            }
        } else {
            val valuePair = getScaledValuePair(context, value)
            val valueTextView = getTextView(valuePair.first, VoucherImageTextType.VALUE)
            val scaleTextView = getTextView(valuePair.second, VoucherImageTextType.SCALE)
            val asterixTextView = getTextView(ASTERISK, VoucherImageTextType.ASTERISK)
            LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                addView(valueTextView)
                addView(scaleTextView)
                addView(asterixTextView)
                layoutParams = wrapContentLayoutParams
            }
        }
    }

    private fun getTextView(value: String, type: VoucherImageTextType) =
            TextView(context).apply {
                visibility = View.VISIBLE
                typeface = Typeface.DEFAULT_BOLD
                text = value
                textSize = context.resources.getDimension(type.dimenRes)
                setTextColor(Color.WHITE)
                if (type != VoucherImageTextType.VALUE) {
                    layoutParams = linearLayoutParams
                }
            }
}