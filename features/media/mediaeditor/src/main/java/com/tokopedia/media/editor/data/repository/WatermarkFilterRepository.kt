package com.tokopedia.media.editor.data.repository

import android.R.attr
import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import java.io.File
import android.R.color
import com.tokopedia.unifycomponents.dpToPx
import android.R.attr.src
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.tokopedia.media.editor.ui.component.WatermarkToolUiComponent
import javax.inject.Inject



interface WatermarkFilterRepository {
    fun watermark(source: Bitmap, watermarkType: Int): Bitmap
}

class WatermarkFilterRepositoryImpl @Inject constructor(): WatermarkFilterRepository {
    override fun watermark(source: Bitmap, watermarkType: Int): Bitmap {
        val w: Int = source.width
        val h: Int = source.height
        val result = Bitmap.createBitmap(w, h, source.config)

        val canvas = Canvas(result)
        canvas.drawBitmap(source, 0f, 0f, null)

        val watermarkText = "Watermark"
        val fontSize = 14f.dpToPx()

        val paint = Paint()
        paint.color = Color.RED
        paint.textSize = fontSize
        paint.isAntiAlias = true

        when(watermarkType){
            WatermarkToolUiComponent.WATERMARK_TOKOPEDIA -> {
                watermark1(w, h, canvas, paint)
            }
            WatermarkToolUiComponent.WATERMARK_SHOP -> {
                watermark2(w, h, canvas, paint)
            }
        }


        return result
    }

    private fun watermark2(
        width: Int,
        height: Int,
        canvas: Canvas,
        paint: Paint
    ) {
        val textTokopedia = "Tokopedia"
        val textShop = "Nama Toko"

        val tokopediaTextBound = Rect()
        paint.getTextBounds(textTokopedia, 0, textTokopedia.length, tokopediaTextBound)
        val shopTextBound = Rect()
        paint.getTextBounds(textShop, 0, textShop.length, shopTextBound)

        var xPos = ((width / 2) - (tokopediaTextBound.width() / 2)).toFloat()
        var yPos = (height / 2).toFloat()
        canvas.drawText(textTokopedia, xPos, yPos, paint)

        xPos = ((width / 2) - (shopTextBound.width() / 2)).toFloat()
        yPos = ((height / 2) + shopTextBound.height()).toFloat()
        canvas.drawText(textShop, xPos, yPos, paint)
    }

    private fun watermark1(
        width: Int,
        height: Int,
        canvas: Canvas,
        paint: Paint
    ) {
        val text = "Tokopedia"

        val textBound = Rect()
        paint.getTextBounds(text, 0, text.length, textBound)

        val paddingHorizontal = width / 5
        val paddingVertical = height / 5

        var xLastPost: Float
        var yLastPost = textBound.height().toFloat()

        while (yLastPost <= height) {
            xLastPost = 0f
            while (xLastPost <= width) {
                canvas.drawText(text, xLastPost, yLastPost, paint)
                xLastPost += (textBound.width() + (paddingHorizontal))
            }
            yLastPost += (textBound.height()) + (paddingVertical)
        }
    }
}