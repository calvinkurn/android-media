package com.tokopedia.media.editor.data.repository

import android.graphics.Bitmap
import com.tokopedia.unifycomponents.dpToPx
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tokopedia.media.editor.ui.component.WatermarkToolUiComponent
import javax.inject.Inject
import com.tokopedia.media.editor.R

interface WatermarkFilterRepository {
    fun watermark(
        context: Context,
        source: Bitmap,
        watermarkType: Int,
        shopNameParam: String
    ): Bitmap
}

class WatermarkFilterRepositoryImpl @Inject constructor() : WatermarkFilterRepository {
    private var shopText = ""
    set(value) {
        field = if (value.length > SHOP_NAME_CHAR_LIMIT){
           value.substring(0, SHOP_NAME_CHAR_LIMIT - 1) + ELLIPSIS_CONST
        } else {
            value
        }
    }

    private var topedDrawable: Drawable? = null

    // image ratio 14:3 || refer to watermark_tokopedia.xml vector drawable
    private var imageWidth: Float = 0f
        set(value) {
            field = value
            imageHeight = (value / 14) * 3
            fontSize = imageHeight
        }

    private var imageHeight: Float = 0f
    private var fontSize: Float = 0f

    override fun watermark(
        context: Context,
        source: Bitmap,
        watermarkType: Int,
        shopNameParam: String
    ): Bitmap {
        shopText = if(shopNameParam.isNullOrEmpty()) DEFAULT_SHOP_NAME else shopNameParam
        if (topedDrawable == null) {
            topedDrawable = ContextCompat.getDrawable(context, R.drawable.watermark_tokopedia)
        }

        val w: Int = source.width
        val h: Int = source.height
        val result = Bitmap.createBitmap(w, h, source.config)

        imageWidth = (w / 5).toFloat()

        val canvas = Canvas(result)
        canvas.drawBitmap(source, 0f, 0f, null)

        val paint = Paint()
        val colorAlpha = ContextCompat.getColor(context, R.color.dms_watermark_text_light)

        paint.color = colorAlpha
        paint.textSize = fontSize
        paint.isAntiAlias = true

        when (watermarkType) {
            WatermarkToolUiComponent.WATERMARK_TOKOPEDIA -> {
                watermark1(w, h, canvas)
            }
            WatermarkToolUiComponent.WATERMARK_SHOP -> {
                watermark2(w, h, canvas, paint)
            }
        }


        return result
    }

    // tengah gambar
    private fun watermark2(
        width: Int,
        height: Int,
        canvas: Canvas,
        paint: Paint
    ) {
        val shopTextBound = Rect()
        paint.getTextBounds(shopText, 0, shopText.length, shopTextBound)

        var xPos = ((width / 2) - (shopTextBound.width() / 2)).toFloat()
        var yPos = ((height / 2) + shopTextBound.height()).toFloat()
        canvas.drawText(shopText, xPos, yPos, paint)


        xPos = ((width / 2) - (imageWidth / 2))
        yPos = ((height / 2) - (imageHeight))
        topedDrawable?.setBounds(
            0,
            0,
            imageWidth.toInt(),
            imageHeight.toInt()
        )
        canvas.translate(xPos, yPos)
        topedDrawable?.draw(canvas)
    }

    // miring & full image
    private fun watermark1(
        width: Int,
        height: Int,
        canvas: Canvas
    ) {
        val paddingHorizontal = width / 5
        val paddingVertical = height / 5

        var xLastPost: Float
        var yLastPost = imageHeight

        val yLimit = height + (height / 2)
        val xStart = -(width / 2)

        while (yLastPost <= yLimit) {
            xLastPost = xStart.toFloat()
            while (xLastPost <= width) {
                canvas.save()
                canvas.rotate(-30f)
                topedDrawable?.setBounds(
                    0,
                    0,
                    imageWidth.toInt(),
                    imageHeight.toInt()
                )
                canvas.translate(xLastPost, yLastPost)
                topedDrawable?.draw(canvas)
                canvas.restore()

                xLastPost += (imageWidth + (paddingHorizontal))
            }
            yLastPost += imageHeight + (paddingVertical)
        }
    }

    companion object{
        private const val DEFAULT_SHOP_NAME = "Shop Name"
        private const val SHOP_NAME_CHAR_LIMIT = 24
        private const val ELLIPSIS_CONST = "..."
    }
}