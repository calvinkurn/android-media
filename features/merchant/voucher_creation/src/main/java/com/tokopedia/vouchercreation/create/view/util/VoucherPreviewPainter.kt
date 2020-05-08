package com.tokopedia.vouchercreation.create.view.util

import android.content.Context
import android.graphics.*
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.create.view.enums.ValueScaleType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageTextType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.typefactory.voucherimage.VoucherImageTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel

class VoucherPreviewPainter(private val context: Context,
                            private val bitmap: Bitmap) {

    companion object {
        private const val FREE_DELIVERY = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/label/label_gratis_ongkir.png"
        private const val CASHBACK = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/label/label_cashback.png"
        private const val CASHBACK_UNTIL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/label/label_cashback_hingga.png"

        private const val THOUSAND = 1000
        private const val MILLION = 1000000
        private const val BILLION = 1000000000

        private const val ASTERISK = "*"
    }

    private val canvas = Canvas(bitmap)

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
    private val middleLabelY = (bitmapHeight * 0.32).toInt()
    private val middleLabelHeight = (bitmapHeight * 0.12).toInt()
    private val middleValueY = (bitmapHeight * 0.4f)
    private val valueMarginTop = (bitmapHeight * 0.05).toInt()

    fun<T : VoucherImageTypeFactory> drawInitial(uiModel: BannerVoucherUiModel<T>): Bitmap {
        uiModel.run {
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
                            return false
                        }
                    })
                    .submit()
            when(imageType) {
                is VoucherImageType.FreeDelivery -> {
                    Glide.with(context)
                            .asBitmap()
                            .load(FREE_DELIVERY)
                            .listener(object : RequestListener<Bitmap> {
                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                    return false
                                }

                                override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    val bitmapRatio = resource.width / resource.height
                                    val fittedLabelWidth = bitmapRatio * middleLabelHeight
                                    val bitmapRect = Rect().apply {
                                        set(middleX.toInt() - fittedLabelWidth/2, middleLabelY, middleX.toInt() + fittedLabelWidth/2, middleLabelY + middleLabelHeight)
                                    }
                                    canvas.drawBitmap(resource, null, bitmapRect, promoTypePaint)
                                    return false
                                }
                            })
                            .submit()
                    drawValueText(imageType.value)
                }
                is VoucherImageType.Rupiah -> {
                    Glide.with(context)
                            .asBitmap()
                            .load(CASHBACK)
                            .listener(object : RequestListener<Bitmap> {
                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                    return false
                                }

                                override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    val bitmapRatio = resource.width / resource.height
                                    val fittedLabelWidth = bitmapRatio * middleLabelHeight
                                    val bitmapRect = Rect().apply {
                                        set(middleX.toInt() - fittedLabelWidth/2, middleLabelY, middleX.toInt() + fittedLabelWidth/2, middleLabelY + middleLabelHeight)
                                    }
                                    canvas.drawBitmap(resource, null, bitmapRect, promoTypePaint)
                                    return false
                                }
                            })
                            .submit()
                    drawValueText(imageType.value)
                }
                is VoucherImageType.Percentage -> {

                }
            }
        }
        return bitmap
    }

    private fun drawValueText(value: Int) {
        val valuePair = getScaledValuePair(value)
        val valueTextView = getTextView(valuePair.first, VoucherImageTextType.VALUE)
        val scaleTextView = getTextView(valuePair.second, VoucherImageTextType.SCALE)
        val asterixTextView = getTextView(ASTERISK, VoucherImageTextType.ASTERIX)
        val horizontalLinearLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            addView(valueTextView)
            addView(scaleTextView)
            addView(asterixTextView)
            layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        canvas.drawBitmap(horizontalLinearLayout.toBitmap(null, null), middleX - horizontalLinearLayout.width/2, middleValueY, null)
    }

    private fun getScaledValuePair(value: Int): Pair<String, String> {
        if (value >= THOUSAND) {
            return if (value >= MILLION) {
                if (value >= BILLION) {
                    Pair("", "")
                } else {
                    Pair((value/MILLION).toString(), context.getString(ValueScaleType.MILLION.stringRes).toBlankOrString())
                }
            } else {
                val scaleText = context.getString(ValueScaleType.THOUSAND.stringRes).toBlankOrString()
                Pair((value/THOUSAND).toString(), scaleText)
            }
        } else {
            return if (value > 0) {
                Pair(value.toString(), "")
            } else {
                Pair("", "")
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