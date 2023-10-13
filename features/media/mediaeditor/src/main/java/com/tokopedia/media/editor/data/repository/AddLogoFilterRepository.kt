package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.media.editor.ui.uimodel.BitmapCreation
import com.tokopedia.media.editor.utils.roundedBitmap
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

interface AddLogoFilterRepository {
    fun generateOverlayImage(
        bitmap: Bitmap,
        newSize: Pair<Int, Int>,
        isCircular: Boolean = false
    ): Bitmap?
}

class AddLogoFilterRepositoryImpl @Inject constructor(
    private val bitmapCreation: BitmapCreationRepository,
    @ApplicationContext private val context: Context
) : AddLogoFilterRepository {
    /**
     * generate new overlay image if product image size is change
     */
    override fun generateOverlayImage(
        bitmap: Bitmap,
        newSize: Pair<Int, Int>,
        isCircular: Boolean
    ): Bitmap? {
        val originalImageWidth = newSize.first
        val originalImageHeight = newSize.second

        bitmapCreation.createBitmap(
            BitmapCreation.emptyBitmap(
                originalImageWidth,
                originalImageHeight,
                Bitmap.Config.ARGB_8888
            )
        )?.let { resultBitmap ->
            val canvas = Canvas(resultBitmap)

            val logoBitmap = if (isCircular) {
                roundedBitmap(context, bitmap.toSquare(), isCircular = true)
            } else {
                roundedBitmap(context, bitmap, CORNER_RADIUS.toPx())
            }

            getDrawLogo(logoBitmap, originalImageWidth)?.let {
                drawBitmap(
                    canvas,
                    it,
                    newSize
                )
            }

            return resultBitmap
        }

        return null
    }

    private fun getDrawLogo(source: Bitmap, originalImageWidth: Int): Bitmap? {
        val widthTarget = originalImageWidth / LOGO_SIZE_DIVIDER
        val heightTarget = ((widthTarget.toFloat() / source.width) * source.height).toInt()

        return bitmapCreation.createBitmap(
            BitmapCreation.scaledBitmap(
                source,
                widthTarget,
                heightTarget,
                true
            )
        )
    }

    private fun drawBitmap(canvas: Canvas, bitmap: Bitmap, pair: Pair<Int, Int>) {
        canvas.drawBitmap(
            bitmap,
            LOGO_X_POS * pair.first,
            LOGO_Y_POS * pair.second,
            Paint()
        )
    }

    private fun Bitmap.toSquare(): Bitmap {
        return if (width != height) {
            val size = width.coerceAtMost(height)
            val xOffset = (width - size) / 2
            val yOffset = (height - size) / 2

            Bitmap.createBitmap(this, xOffset, yOffset, size, size)
        } else {
            this
        }
    }

    companion object {
        // logo size 1/6 from base image
        private const val LOGO_SIZE_DIVIDER = 6

        private const val LOGO_X_POS = 0.03f
        private const val LOGO_Y_POS = 0.03f

        private const val CORNER_RADIUS = 8f
    }
}
