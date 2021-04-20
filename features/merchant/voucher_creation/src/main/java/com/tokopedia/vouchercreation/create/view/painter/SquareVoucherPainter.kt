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
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.whenAlive
import com.tokopedia.vouchercreation.create.view.enums.PostImageTextType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.enums.getScaledValuePair
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.PostBaseUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.PostVoucherUiModel

class SquareVoucherPainter(private val context: Context,
                           private val bitmap: Bitmap,
                           private val onSuccessGetBitmap: (Bitmap) -> Unit,
                           private val onErrorGetBitmap: (Throwable) -> Unit = {}) {

    companion object {
        private const val INITIAL_X = 0.15f
        private const val SHOP_NAME_Y = 0.25f
        private const val PROMO_NAME_Y = 0.425f
        private const val SHOP_AVATAR_X = 0.663f
        private const val SHOP_AVATAR_Y = 0.145f
        private const val SHOP_AVATAR_SIZE = 0.2f
        private const val SHOP_AVATAR_RADIUS = 0.6f
        private const val LABEL_Y = 0.5f
        private const val LABEL_HEIGHT = 0.06f
        private const val VALUE_Y = 0.62f
        private const val RIGHT_LABEL_VALUE_X = 0.55f
        private const val BOTTOM_INFO_X = 0.34f
        private const val PROMO_CODE_Y = 0.87f
        private const val PROMO_PERIOD_Y = 0.93f

        private const val DASH = "-"
        private const val ASTERISK = "*"
        private const val PERCENT = "%"
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(PostValuePosition.LEFT, PostValuePosition.RIGHT)
    annotation class PostValuePosition {
        companion object {
            const val LEFT = 0
            const val RIGHT = 1
        }
    }

    private val canvas = Canvas(bitmap)

    private val wrapContentLayoutParams by lazy {
        LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private val shopNamePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 40f
            typeface = Typeface.DEFAULT_BOLD
        }
    }

    private val promoNamePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 40f
            typeface = Typeface.DEFAULT_BOLD
        }
    }

    private val promoLabelPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
        }
    }

    private val promoCodePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 50f
            typeface = Typeface.DEFAULT_BOLD
        }
    }

    private val promoPeriodPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 45f
        }
    }

    private val shopAvatarPaint by lazy {
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
    private val shopNameX = bitmapWidth * INITIAL_X
    private val shopNameY = bitmapHeight * SHOP_NAME_Y
    private val shopAvatarX = (bitmapWidth * SHOP_AVATAR_X).toInt()
    private val shopAvatarY = (bitmapHeight * SHOP_AVATAR_Y).toInt()
    private val shopAvatarRadius = (bitmapWidth * SHOP_AVATAR_RADIUS).toInt()
    private val shopAvatarSize = (bitmapWidth * SHOP_AVATAR_SIZE).toInt()
    private val promoNameX = bitmapWidth * INITIAL_X
    private val promoNameY = bitmapHeight * PROMO_NAME_Y
    private val leftPromoInfoX = bitmapWidth * INITIAL_X
    private val rightPromoInfoX = bitmapWidth * RIGHT_LABEL_VALUE_X
    private val promoLabelY = (bitmapHeight * LABEL_Y).toInt()
    private val promoLabelHeight = (bitmapHeight * LABEL_HEIGHT).toInt()
    private val promoValueY = bitmapHeight * VALUE_Y
    private val bottomInfoX = bitmapWidth * BOTTOM_INFO_X
    private val promoCodeY = bitmapHeight * PROMO_CODE_Y
    private val promoPeriodY = bitmapHeight * PROMO_PERIOD_Y
    private val valueMarginTop = (bitmapHeight * 0.035).toInt()

    fun drawInfo(postVoucherUiModel: PostVoucherUiModel) {
        postVoucherUiModel.run {
            canvas.run {
                val displayedPromoCode =
                        if (isPublic == true) {
                            DASH
                        } else {
                            promoCode
                        }
                drawTextInformation(shopName, promoName, displayedPromoCode, promoPeriod)
                drawShopAvatar(shopAvatar)
                drawPromoInfo(imageType, postBaseUiModel)
            }
        }
    }

    private fun Canvas.drawTextInformation(shopName: String,
                                           promoName: String,
                                           promoCode: String,
                                           promoPeriod: String) {
        drawText(shopName, shopNameX, shopNameY, shopNamePaint)
        drawText(promoName, promoNameX, promoNameY, promoNamePaint)
        drawText(promoCode, bottomInfoX, promoCodeY, promoCodePaint)
        drawText(promoPeriod, bottomInfoX, promoPeriodY, promoPeriodPaint)
    }

    private fun Canvas.drawShopAvatar(avatarUrl: String) {
        context.whenAlive {
            Glide.with(it)
                    .asBitmap()
                    .load(avatarUrl)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            e?.let { ex ->
                                onErrorGetBitmap(ex)
                            }
                            return false
                        }

                        override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            val bitmapRect = Rect().apply {
                                set(shopAvatarX, shopAvatarY, shopAvatarX + shopAvatarSize, shopAvatarY + shopAvatarSize)
                            }
                            drawBitmap(ImageHandler.getRoundedCornerBitmap(resource, shopAvatarRadius), null, bitmapRect, shopAvatarPaint)
                            return false
                        }
                    })
                    .submit()
        }
    }

    private fun Canvas.drawPromoInfo(imageType: VoucherImageType, postBaseUiModel: PostBaseUiModel) {
        val leftLabelImageUrl =
                if (imageType is VoucherImageType.FreeDelivery) {
                    postBaseUiModel.freeDeliveryLabelUrl
                } else {
                    postBaseUiModel.cashbackLabelUrl
                }
        context.whenAlive {
            Glide.with(it)
                    .asBitmap()
                    .load(leftLabelImageUrl)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            e?.let { ex ->
                                onErrorGetBitmap(ex)
                            }
                            return false
                        }

                        override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            val imageValue: Int
                            val isPercentageType: Boolean =
                                    if (imageType is VoucherImageType.Percentage) {
                                        imageValue = imageType.percentage
                                        true
                                    } else {
                                        imageValue = imageType.value
                                        false
                                    }
                            val percentageValue: Int? =
                                    if (imageType is VoucherImageType.Percentage) {
                                        imageType.value
                                    } else {
                                        null
                                    }
                            drawPromotionLabel(resource, leftPromoInfoX.toInt(), imageValue, PostValuePosition.LEFT, isPercentageType, percentageValue, postBaseUiModel.cashbackUntilLabelUrl)
                            return false
                        }
                    })
                    .submit()
        }
    }

    private fun Canvas.drawPromotionLabel(resource: Bitmap,
                                          xPosition: Int,
                                          value: Int,
                                          @PostValuePosition postValuePosition: Int,
                                          isPercentage: Boolean = false,
                                          percentageValue: Int? = null,
                                          cashbackLabelUrl: String? = null) {
        val bitmapRatio = resource.width / resource.height
        val fittedLabelWidth = (bitmapRatio * promoLabelHeight)
        val bitmapRect = Rect().apply {
            set(xPosition, promoLabelY, xPosition + fittedLabelWidth, promoLabelY + promoLabelHeight)
        }
        drawBitmap(resource, null, bitmapRect, promoLabelPaint)

        drawValueText(value, postValuePosition, isPercentage, percentageValue, cashbackLabelUrl)
    }

    private fun Canvas.drawValueText(value: Int, @PostValuePosition postValuePosition: Int, isPercentage: Boolean, percentageValue: Int? = null, cashbackLabelUrl: String? = null) {
        val horizontalLinearLayout = getValueLinearLayout(value, isPercentage)
        val xPosition: Float = when(postValuePosition) {
            PostValuePosition.LEFT -> leftPromoInfoX
            PostValuePosition.RIGHT -> rightPromoInfoX
            else -> return
        }
        drawBitmap(horizontalLinearLayout.toBitmap(null, null), xPosition, promoValueY - horizontalLinearLayout.height/2, null)
        if (percentageValue == null || !isPercentage) {
            onSuccessGetBitmap(bitmap)
        } else {
            context.whenAlive {
                Glide.with(it)
                        .asBitmap()
                        .load(cashbackLabelUrl.orEmpty())
                        .listener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                e?.let { ex ->
                                    onErrorGetBitmap(ex)
                                }
                                return false
                            }

                            override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                drawPromotionLabel(resource, rightPromoInfoX.toInt(), percentageValue.orZero(), PostValuePosition.RIGHT)
                                return false
                            }
                        })
                        .submit()
            }
        }
    }

    private fun getValueLinearLayout(value: Int, isPercentage: Boolean): LinearLayout {
        return if(isPercentage) {
            val valueTextView = getTextView(value.toString(), PostImageTextType.VALUE)
            val percentageTextView = getTextView(PERCENT, PostImageTextType.SCALE)
            LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                addView(valueTextView)
                addView(percentageTextView)
                layoutParams = wrapContentLayoutParams
            }
        } else {
            val (nominal, currencyScale) = getScaledValuePair(context, value)
            val valueTextView = getTextView(nominal, PostImageTextType.VALUE)
            val scaleTextView = getTextView(currencyScale, PostImageTextType.SCALE)
            val asterixTextView = getTextView(ASTERISK, PostImageTextType.ASTERISK)
            LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                addView(valueTextView)
                addView(scaleTextView)
                addView(asterixTextView)
                layoutParams = wrapContentLayoutParams
            }
        }
    }

    private fun getTextView(value: String, type: PostImageTextType) =
            TextView(context).apply {
                visibility = View.VISIBLE
                typeface = Typeface.DEFAULT_BOLD
                text = value
                textSize = type.textSize

                var textColor = Color.BLACK
                if (type == PostImageTextType.SCALE) {
                    textColor = Color.GRAY
                }
                setTextColor(textColor)

                if (type != PostImageTextType.VALUE) {
                    layoutParams = linearLayoutParams
                }
            }

}