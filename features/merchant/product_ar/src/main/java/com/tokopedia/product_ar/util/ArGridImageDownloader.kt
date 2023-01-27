package com.tokopedia.product_ar.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.file.PublicFolderUtil

object ArGridImageDownloader {
    private const val TOTAL_MARGIN_LEFT_RIGHT_MULTIPLE_COMPARISSON_RV = 32 //padding left right rv
    private const val TOTAL_MARGIN_LEFT_RIGHT_SINGLE_COMPARISSON_RV = 34 //padding left right + padding item decorator

    fun screenShotAndSaveGridImage(v: View?,
                                   bitmapsSize: Int,
                                   failScreenShot: () -> Unit,
                                   finishScreenshot: () -> Unit) {
        try {
            if (v != null) {
                val totalPadding = if (bitmapsSize == 1)
                    TOTAL_MARGIN_LEFT_RIGHT_SINGLE_COMPARISSON_RV
                else
                    TOTAL_MARGIN_LEFT_RIGHT_MULTIPLE_COMPARISSON_RV

                v.measure(
                        View.MeasureSpec.makeMeasureSpec(v.width, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                val screenshot = Bitmap.createBitmap(v.width - totalPadding.toPx(), v.measuredHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(screenshot)
                canvas.translate(-(totalPadding / 2).toPx().toFloat(), 0F)
                canvas.drawColor(Color.WHITE)
                v.draw(canvas)

                screenshot?.let {
                    PublicFolderUtil.putImageToPublicFolder(
                            v.context,
                            it,
                            System.currentTimeMillis().toString() + ".jpg"
                    )
                    finishScreenshot.invoke()
                }
            }
        } catch (e: Exception) {
            failScreenShot.invoke()
        }
    }
}