package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.editor.data.entity.EditorDetailEntity
import com.tokopedia.media.editor.ui.uimodel.BitmapCreation
import javax.inject.Inject
import com.tokopedia.media.editor.ui.uimodel.EditorWatermarkUiModel
import kotlin.math.min
import com.tokopedia.unifyprinciples.R as principleR

interface WatermarkFilterRepository {
    fun watermark(
        source: Bitmap,
        type: WatermarkType,
        shopNameParam: String,
        isThumbnail: Boolean,
        element: EditorDetailEntity? = null,
        useStorageColor: Boolean
    ): Bitmap?

    fun watermarkDrawerItem(
        implementedBaseBitmap: Bitmap,
        shopName: String
    ): Pair<Bitmap?, Bitmap?>

    fun isAssetInitialize(): Boolean
    fun setAsset(logoDrawable: Drawable, textLightColor: Int, textDarkColor: Int)
}

class WatermarkFilterRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context,
    private val bitmapCreationRepository: BitmapCreationRepository
) : WatermarkFilterRepository {
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
            logoDrawableHeight = (value / WATERMARK_LOGO_RATIO_WIDTH) * WATERMARK_LOGO_RATIO_HEIGHT
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
    ): Bitmap? {
        shopText = shopNameParam.ifEmpty { DEFAULT_SHOP_NAME }

        var isDark = source.isDark()
        if (useStorageColor) {
            element?.watermarkModeEntityData?.let {
                isDark = it.textColorDark
            }
        }

        val watermarkColor = if (!isThumbnail) {
            if (isDark) textDarkColor else textLightColor
        } else {
            ContextCompat.getColor(context, principleR.color.Unify_Static_White)
        }

        val sourceWidth: Int = source.width
        val sourceHeight: Int = source.height

        return bitmapCreationRepository.createBitmap(
            BitmapCreation.emptyBitmap(
                sourceWidth, sourceHeight, source.config
            )
        )?.let { result ->
            logoDrawableWidth = if (!isThumbnail)
                (sourceWidth / IMAGE_SIZE_DIVIDER).toFloat()
            else
                min(sourceWidth, sourceHeight) / THUMBNAIL_SIZE_DIVIDER

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

            // return
            result
        } ?: kotlin.run {
            null
        }
    }

    override fun watermarkDrawerItem(
        implementedBaseBitmap: Bitmap,
        shopName: String
    ): Pair<Bitmap?, Bitmap?> {
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

    // formula to determine brightness 0.299 * r + 0.0f + 0.587 * g + 0.0f + 0.114 * b + 0.0f
    // if total of dark pixel > total of pixel * 0.45 count that as dark image
    private fun Bitmap.isDark(): Boolean {
        val width = this.width
        val height = this.height

        if (bitmapCreationRepository.isBitmapOverflow(width, height)) {
            return false
        }

        return try {
            val ratio = width.toFloat() / height
            val widthBitmapChecker = IMAGE_DARK_CHECKER_SCALE
            val heightBitmapChecker = widthBitmapChecker * ratio
            val bitmapChecker =
                Bitmap.createScaledBitmap(this, widthBitmapChecker, heightBitmapChecker.toInt(), false)
            val darkThreshold = bitmapChecker.width * bitmapChecker.height * DARK_COLOR_THRESHOLD
            var darkPixels = 0
            val pixels = IntArray(bitmapChecker.width * bitmapChecker.height)
            bitmapChecker.getPixels(
                pixels,
                0,
                bitmapChecker.width,
                0,
                0,
                bitmapChecker.width,
                bitmapChecker.height
            )

            for (i in pixels.indices) {
                val color = pixels[i]
                val r = Color.red(color)
                val g = Color.green(color)
                val b = Color.blue(color)
                val luminance =
                    LUMINANCE_MULTIPLIER_RED * r +
                    LUMINANCE_MULTIPLIER_GREEN * g +
                    LUMINANCE_MULTIPLIER_BLUE * b
                if (luminance < LUMINANCE_THRESHOLD) {
                    darkPixels++
                }
            }
            bitmapChecker.recycle()
            darkPixels >= darkThreshold
        } catch (_: Exception) {
            false
        }
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
                canvas.rotate(WATERMARK_LOGO_DIAGONAL_DEGREE)

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
        private const val THUMBNAIL_SIZE_DIVIDER = 3f

        private const val IMAGE_DARK_CHECKER_SCALE = 50
        private const val DARK_COLOR_THRESHOLD = 0.45f
        private const val LUMINANCE_THRESHOLD = 150
        private const val LUMINANCE_MULTIPLIER_RED = 0.299f
        private const val LUMINANCE_MULTIPLIER_GREEN = 0.587f
        private const val LUMINANCE_MULTIPLIER_BLUE = 0.114f

        private const val WATERMARK_LOGO_RATIO_WIDTH = 14
        private const val WATERMARK_LOGO_RATIO_HEIGHT = 3

        private const val WATERMARK_LOGO_DIAGONAL_DEGREE = -30f
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
