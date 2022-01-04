package com.tokopedia.product_ar.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.file.PublicFolderUtil

object ArGridImageDownloader {
    private const val TOTAL_MARGIN_LEFT_RIGHT_COMPARISSON_RV = 32

    fun screenShotAndSaveGridImage(v: View?, finishScreenshot: () -> Unit) {
        try {
            if (v != null) {
                v.measure(
                        View.MeasureSpec.makeMeasureSpec(v.width, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                val screenshot = Bitmap.createBitmap(v.width - TOTAL_MARGIN_LEFT_RIGHT_COMPARISSON_RV.toPx(), v.measuredHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(screenshot)
                canvas.translate(-(TOTAL_MARGIN_LEFT_RIGHT_COMPARISSON_RV / 2).toPx().toFloat(), 0F)
                canvas.drawColor(Color.WHITE)
                v.draw(canvas)

                screenshot?.let {
                    PublicFolderUtil.putImageToPublicFolder(
                            v.context,
                            it,
                            System.currentTimeMillis().toString() + ".jpg"
                    )
                }
            }
        } catch (e: Exception) {
            // NOOP
        } finally {
            finishScreenshot.invoke()
        }
    }
}