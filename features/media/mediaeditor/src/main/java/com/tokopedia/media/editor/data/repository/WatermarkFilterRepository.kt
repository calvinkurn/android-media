package com.tokopedia.media.editor.data.repository

import android.graphics.Bitmap
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.tokopedia.media.editor.ui.component.WatermarkToolUiComponent
import javax.inject.Inject
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorWatermarkModel
import com.tokopedia.media.editor.utils.isDark
import com.tokopedia.media.loader.loadImageRounded
import kotlin.math.min

interface WatermarkFilterRepository {
    fun watermark(
        context: Context,
        source: Bitmap,
        watermarkType: Int,
        shopNameParam: String,
        isThumbnail: Boolean,
        detailUiModel: EditorDetailUiModel? = null
    ): Bitmap

    fun watermarkDrawerItem(
        context: Context,
        implementedBaseBitmap: Bitmap?,
        shopName: String,
        buttonRef: Pair<ImageView, ImageView>
    )
}

class WatermarkFilterRepositoryImpl @Inject constructor() : WatermarkFilterRepository {
    private var shopText = ""
        set(value) {
            field = if (value.length > SHOP_NAME_CHAR_LIMIT) {
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

    private var imageHeight = 0f
    private var fontSize = 0f

    private var textWidth = 0
    private var textHeight = 0

    private var watermarkColor = 0

    override fun watermark(
        context: Context,
        source: Bitmap,
        watermarkType: Int,
        shopNameParam: String,
        isThumbnail: Boolean,
        detailUiModel: EditorDetailUiModel?
    ): Bitmap {
        shopText = if (shopNameParam.isEmpty()) DEFAULT_SHOP_NAME else shopNameParam
        if (topedDrawable == null) {
            topedDrawable = ContextCompat.getDrawable(context, R.drawable.watermark_tokopedia)
        }

        var isDark = source.isDark()
        detailUiModel?.watermarkMode?.let {
            isDark = it.textColorDark
        } ?: kotlin.run {
            detailUiModel?.watermarkMode = EditorWatermarkModel(watermarkType, isDark)
        }

        watermarkColor = if (!isThumbnail) {
            ContextCompat.getColor(
                context,
                if (isDark) R.color.dms_watermark_text_dark
                else R.color.dms_watermark_text_light
            )
        } else {
            Color.WHITE
        }

        val w: Int = source.width
        val h: Int = source.height
        val result = Bitmap.createBitmap(w, h, source.config)

        imageWidth = if (!isThumbnail)
            (w / IMAGE_SIZE_DIVIDER).toFloat()
        else
            min(w, h) / 3f

        val canvas = Canvas(result)
        canvas.drawBitmap(source, 0f, 0f, null)

        val paint = Paint()

        paint.color = watermarkColor
        paint.textSize = fontSize
        paint.isAntiAlias = true

        topedDrawable?.setTint(watermarkColor)

        val shopTextBound = Rect()
        paint.getTextBounds(shopText, 0, shopText.length, shopTextBound)

        textWidth = shopTextBound.width()
        textHeight = shopTextBound.height()

        when (watermarkType) {
            WatermarkToolUiComponent.WATERMARK_TOKOPEDIA -> {
                watermark1(w, h, canvas, paint)
            }
            WatermarkToolUiComponent.WATERMARK_SHOP -> {
                watermark2(w, h, canvas, paint)
            }
        }

        return result
    }

    override fun watermarkDrawerItem(
        context: Context,
        implementedBaseBitmap: Bitmap?,
        shopName: String,
        buttonRef: Pair<ImageView, ImageView>
    ) {
        implementedBaseBitmap?.let { bitmap ->
            val resultBitmap1 = watermark(
                context,
                bitmap,
                WatermarkToolUiComponent.WATERMARK_TOKOPEDIA,
                shopName,
                true
            )

            val resultBitmap2 = watermark(
                context,
                bitmap,
                WatermarkToolUiComponent.WATERMARK_SHOP,
                shopName,
                true
            )

            val roundedCorner =
                context.resources?.getDimension(R.dimen.editor_watermark_rounded) ?: 0f
            buttonRef.first.loadImageRounded(resultBitmap1, roundedCorner) {
                centerCrop()
            }
            buttonRef.second.loadImageRounded(resultBitmap2, roundedCorner) {
                centerCrop()
            }
        }
    }

    // Tokopedia text & shop name only on center image
    private fun watermark2(
        width: Int,
        height: Int,
        canvas: Canvas,
        paint: Paint
    ) {
        var xPos = ((width / 2) - (textWidth / 2)).toFloat()
        var yPos = ((height / 2) + textHeight).toFloat()
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

    // Tokopedia text & shop name fill image
    private fun watermark1(
        width: Int,
        height: Int,
        canvas: Canvas,
        paint: Paint
    ) {
        val paddingHorizontal = width / PADDING_DIVIDER
        val paddingVertical = height / PADDING_DIVIDER

        var xLastPost: Float
        var yLastPost = imageHeight

        val yLimit = height + (height / 2)
        val xStart = -(textWidth + paddingHorizontal)


        var index: Int
        topedDrawable?.setBounds(
            0,
            0,
            imageWidth.toInt(),
            imageHeight.toInt()
        )

        while (yLastPost <= yLimit) {
            xLastPost = xStart.toFloat()
            index = 0
            while (xLastPost <= width) {
                canvas.save()
                canvas.rotate(-30f)

                if (index % 2 == 1) {
                    canvas.translate(xLastPost, yLastPost)
                    topedDrawable?.draw(canvas)
                    xLastPost += (imageWidth + (paddingHorizontal))
                } else {
                    canvas.drawText(shopText, xLastPost, yLastPost, paint)
                    xLastPost += (textWidth + (paddingHorizontal))
                }

                canvas.restore()
                index++
            }
            yLastPost += imageHeight + (paddingVertical)
        }
    }

    companion object {
        private const val DEFAULT_SHOP_NAME = "Shop Name"
        private const val SHOP_NAME_CHAR_LIMIT = 24
        private const val ELLIPSIS_CONST = "..."
        private const val PADDING_DIVIDER = 6
        private const val IMAGE_SIZE_DIVIDER = 6
    }
}