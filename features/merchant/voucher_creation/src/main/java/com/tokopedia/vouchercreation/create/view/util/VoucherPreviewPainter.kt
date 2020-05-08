package com.tokopedia.vouchercreation.create.view.util

import android.content.Context
import android.graphics.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.vouchercreation.create.view.typefactory.voucherimage.VoucherImageTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel

class VoucherPreviewPainter(private val context: Context,
                            private val bitmap: Bitmap) {

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
            textSize = 35f
        }
    }
    private val promoNamePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 50f
            typeface = Typeface.DEFAULT_BOLD
        }
    }
    private val bitmapWidth = bitmap.width
    private val bitmapHeight = bitmap.height
    private val shopAvatarX = (bitmapWidth * 0.07).toInt()
    private val shopAvatarY = (bitmapHeight * 0.22).toInt()
    private val shopNameX = (bitmapWidth * 0.048f)
    private val shopNameY = (bitmapHeight * 0.75f)
    private val promoNameX = (bitmapWidth * 0.42f)
    private val promoNameY = (bitmapHeight * 0.4f)
    private val shopAvatarSize = (bitmapWidth * 0.075).toInt()

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
        }
        return bitmap
    }
}