package com.tokopedia.play.broadcaster.view.viewmodel

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by furqan on 05/06/2020
 */
class PlayCoverTitleSetupViewModel @Inject constructor(
        @Named(PlayBroadcastDispatcher.MAIN) private val mainDispatcher: CoroutineDispatcher,
        @Named(PlayBroadcastDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
        @Named(PlayBroadcastDispatcher.COMPUTATION) private val computationDispatcher: CoroutineDispatcher)
    : BaseViewModel(mainDispatcher) {

    fun cropImage(imagePath: String, coverImageView: ImageView,
                  leftOfExpected: Float, expectedWidth: Int): String {
        val bitmapToEdit = ImageUtils.getBitmapFromPath(imagePath, ImageUtils.DEF_WIDTH,
                ImageUtils.DEF_HEIGHT, true)

        val height = bitmapToEdit.height
        val width = bitmapToEdit.width
        val scaledWidth: Float = width / coverImageView.width.toFloat()
        val left = getLeftCropPosition(coverImageView.left.toFloat(), leftOfExpected, scaledWidth)
        val right = left + (expectedWidth * scaledWidth)
        val top = 0
        val bottom = height
        val isPng = ImageUtils.isPng(imagePath)

        var outputBitmap: Bitmap? = null
        try {
            outputBitmap = Bitmap.createBitmap(expectedWidth, height, bitmapToEdit.config)
            val canvas = Canvas(outputBitmap!!)
            canvas.drawBitmap(bitmapToEdit,
                    Rect(left.toInt(), top, right.toInt(), bottom),
                    Rect(left.toInt(), top, right.toInt(), bottom),
                    null)
            val file = ImageUtils.writeImageToTkpdPath(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA,
                    outputBitmap, isPng)
            bitmapToEdit.recycle()
            outputBitmap.recycle()
            System.gc()

            return file.absolutePath
        } catch (e: Throwable) {
            if (outputBitmap != null && !outputBitmap.isRecycled) {
                outputBitmap.recycle()
            }
            return imagePath
        }
    }

    /**
     * X1 = Left position to crop
     * X0 = Left position of Image
     *
     * Formula :
     * if (X0 >= 0)
     *   X1 - X0
     * else
     *   (X0 * -1) + X1
     */
    private fun getLeftCropPosition(leftOfImages: Float, leftOfExpected: Float, scaledWidth: Float): Float =
            if (leftOfImages >= 0) {
                (leftOfExpected - leftOfImages) * scaledWidth
            } else {
                ((leftOfImages * -1) + leftOfExpected) * scaledWidth
            }

}