package com.tokopedia.media.editor.data.repository

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.tokopedia.media.editor.data.entity.EditorDetailEntity
import javax.inject.Inject
import com.tokopedia.media.editor.ui.uimodel.EditorWatermarkUiModel
import com.tokopedia.media.editor.utils.isDark
import kotlin.math.min

interface WatermarkFilterRepository {
    fun watermark(
        source: Bitmap,
        type: WatermarkType,
        shopNameParam: String,
        isThumbnail: Boolean,
        element: EditorDetailEntity? = null,
        useStorageColor: Boolean
    ): Bitmap

    fun watermarkDrawerItem(
        implementedBaseBitmap: Bitmap,
        shopName: String
    ): Pair<Bitmap, Bitmap>

    fun isAssetInitialize(): Boolean
    fun setAsset(logoDrawable: Drawable, textLightColor: Int, textDarkColor: Int)
}

class WatermarkFilterRepositoryImpl @Inject constructor() : WatermarkFilterRepository {
    private var logoDrawable: Drawable? = null

    private var shopText = ""
        set(value) {
            field = if (value.length > SHOP_NAME_CHAR_LIMIT) {
                value.substring(0, SHOP_NAME_CHAR_LIMIT - 1) + ELLIPSIS_CONST
            } else {
                value
            }
        }

    @Suppress("SpellCheckingInspection")
    // image ratio 14:3 || refer to watermark_tokopedia.xml vector drawable
    private var logoDrawableWidth: Float = 0f
        set(value) {
            field = value
            logoDrawableHeight = (value / 14) * 3
        }

    private var logoDrawableHeight = 0f

    private var textWidth = 0
    private var textHeight = 0

    private var textLightColor = 0
    private var textDarkColor = 0

    override fun isAssetInitialize(): Boolean {
        return logoDrawable != null
    }

    override fun setAsset(
        logoDrawable: Drawable,
        textLightColor: Int,
        textDarkColor: Int
    ) {
        this.logoDrawable = logoDrawable
        this.textLightColor = textLightColor
        this.textDarkColor = textDarkColor
    }

    override fun watermark(
        source: Bitmap,
        type: WatermarkType,
        shopNameParam: String,
        isThumbnail: Boolean,
        element: EditorDetailEntity?,
        useStorageColor: Boolean
    ): Bitmap {
        shopText = if (shopNameParam.isEmpty()) DEFAULT_SHOP_NAME else shopNameParam

        var isDark = source.isDark()
        if (useStorageColor) {
            element?.watermarkModeEntityData?.let {
                isDark = it.textColorDark
            }
        }

        val watermarkColor = if (!isThumbnail) {
            if (isDark) textDarkColor else textLightColor
        } else {
            Color.WHITE
        }

        val sourceWidth: Int = source.width
        val sourceHeight: Int = source.height
        val result = Bitmap.createBitmap(sourceWidth, sourceHeight, source.config)

        logoDrawableWidth = if (!isThumbnail)
            (sourceWidth / IMAGE_SIZE_DIVIDER).toFloat()
        else
            min(sourceWidth, sourceHeight) / 3f

        val canvas = Canvas(result)
        canvas.drawBitmap(source, 0f, 0f, null)

        val paint = Paint()

        // font size will refer to logo height to achieve same visual size
        paint.textSize = logoDrawableHeight
        paint.isAntiAlias = true
        paint.color = watermarkColor

        logoDrawable?.setTint(watermarkColor)

        val shopTextBound = Rect()
        paint.getTextBounds(shopText, 0, shopText.length, shopTextBound)

        textWidth = shopTextBound.width()
        textHeight = shopTextBound.height()

        when (type) {
            WatermarkType.Diagonal -> {
                setWatermarkDiagonal(sourceWidth, sourceHeight, canvas, paint)
            }
            WatermarkType.Center -> {
                setWatermarkCenter(sourceWidth, sourceHeight, canvas, paint)
            }
        }

        element?.watermarkModeEntityData?.let {
            it.textColorDark = isDark
            it.watermarkType = type.value
        } ?: run {
            element?.watermarkModeEntityData = EditorWatermarkUiModel(type.value, isDark)
        }

        return result
    }

    override fun watermarkDrawerItem(
        implementedBaseBitmap: Bitmap,
        shopName: String
    ): Pair<Bitmap, Bitmap> {
        val resultBitmap1 = watermark(
            implementedBaseBitmap,
            WatermarkType.Diagonal,
            shopName,
            true,
            useStorageColor = false
        )

        val resultBitmap2 = watermark(
            implementedBaseBitmap,
            WatermarkType.Center,
            shopName,
            true,
            useStorageColor = false
        )

        return Pair(resultBitmap1, resultBitmap2)
    }

    private fun setWatermarkCenter(
        width: Int,
        height: Int,
        canvas: Canvas,
        paint: Paint
    ) {
        var xPos = ((width / 2) - (textWidth / 2)).toFloat()
        var yPos = ((height / 2) + textHeight).toFloat()
        canvas.drawText(shopText, xPos, yPos, paint)


        xPos = ((width / 2) - (logoDrawableWidth / 2))
        yPos = ((height / 2) - (logoDrawableHeight))
        logoDrawable?.setBounds(
            0,
            0,
            logoDrawableWidth.toInt(),
            logoDrawableHeight.toInt()
        )
        canvas.translate(xPos, yPos)
        logoDrawable?.draw(canvas)
    }

    private fun setWatermarkDiagonal(
        width: Int,
        height: Int,
        canvas: Canvas,
        paint: Paint
    ) {
        val paddingHorizontal = width / PADDING_DIVIDER
        val paddingVertical = height / PADDING_DIVIDER

        var xLastPost: Float
        var yLastPost = logoDrawableHeight

        // text box bound height will be 2x source height
        // text box bound width will be source width + 2x text column
        val yLimit = height * 2
        val xStart = -((textWidth + paddingHorizontal) * 2)


        var index: Int
        logoDrawable?.setBounds(
            0,
            0,
            logoDrawableWidth.toInt(),
            logoDrawableHeight.toInt()
        )

        while (yLastPost <= yLimit) {
            xLastPost = xStart.toFloat()
            index = 0
            while (xLastPost <= width) {
                canvas.save()
                canvas.rotate(-30f)

                if (index % 2 == 1) {
                    canvas.translate(xLastPost, yLastPost)
                    logoDrawable?.draw(canvas)
                    xLastPost += (logoDrawableWidth + (paddingHorizontal))
                } else {
                    canvas.drawText(shopText, xLastPost, yLastPost, paint)
                    xLastPost += (textWidth + (paddingHorizontal))
                }

                canvas.restore()
                index++
            }
            yLastPost += logoDrawableHeight + (paddingVertical)
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

sealed class WatermarkType(val value: Int) {
    object Diagonal : WatermarkType(DIAGONAL_INDEX)
    object Center : WatermarkType(CENTER_INDEX)

    companion object {
        private const val DIAGONAL_INDEX = 0
        private const val CENTER_INDEX = 1

        fun map(type: Int?): WatermarkType? {
            return when (type) {
                DIAGONAL_INDEX -> Diagonal
                CENTER_INDEX -> Center
                else -> null
            }
        }
    }
}