package com.tokopedia.tokomember_seller_dashboard.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import com.tokopedia.promoui.common.dpToPx
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object TmFileUtil {

     @SuppressLint("Range")
     private fun getBitmapFromView(v: View): Bitmap? {

         val specWidth = View.MeasureSpec.makeMeasureSpec(dpToPx(328).toInt(), View.MeasureSpec.EXACTLY)
         val specHeight = View.MeasureSpec.makeMeasureSpec(dpToPx(111).toInt(), View.MeasureSpec.EXACTLY)

         v.measure(specWidth, specHeight)
         val questionWidth: Int = v.measuredWidth
         val questionHeight = v.measuredHeight

         val b = Bitmap.createBitmap(questionWidth, questionHeight, Bitmap.Config.ARGB_8888)
         val c = Canvas(b)
         c.drawColor(Color.WHITE)
         v.layout(v.left, v.top, v.right, v.bottom)
         v.draw(c)
         return b
     }

     fun saveBitMap(context: Context, drawView: View): File {
        val filename: String =
            "del_tm_coupon" + System.currentTimeMillis()
                .toString() + ".png"
        val pictureFile = File(context.cacheDir,filename)
        try {
            val bitmap = getBitmapFromView(drawView)
            pictureFile.createNewFile()
            val oStream = FileOutputStream(pictureFile)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, oStream)
            oStream.flush()
            oStream.close()
        } catch (e: IOException) {
            Timber.d(e)
        }
        return pictureFile
    }
}