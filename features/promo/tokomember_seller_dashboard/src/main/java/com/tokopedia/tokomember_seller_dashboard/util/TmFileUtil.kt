package com.tokopedia.tokomember_seller_dashboard.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.utils.image.ImageProcessingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object TmFileUtil {

    fun createNewFile(imageName:String , context: Context , bitmap: Bitmap): File{

        val filename = "toko_temp_coupon_create"
        val file = File(context.cacheDir, filename)
        file.createNewFile()
        //Convert bitmap to byte array
        val byteOs = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,0,byteOs)
        val bitmapData = byteOs.toByteArray()

        //write bytes in file

        val fileOs = FileOutputStream(file)
        fileOs.write(bitmapData)
        fileOs.flush()
        fileOs.close()

        return file
    }

    fun saveViewCaptureToStorage(
        resource: Bitmap,
        imageSaved: (savedImgPath: String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launchCatchError(block = {
            withContext(Dispatchers.IO) {
                val savedFile = ImageProcessingUtil.writeImageToTkpdPath(
                    resource,
                    Bitmap.CompressFormat.PNG
                )
                if (savedFile != null) {
                    imageSaved(savedFile.absolutePath)
                }
            }
        }, onError = {
            it.printStackTrace()
        })
    }

     @SuppressLint("Range")
     private fun getBitmapFromView(v: View): Bitmap? {

         val specWidth = View.MeasureSpec.makeMeasureSpec(dpToPx(250).toInt(), View.MeasureSpec.EXACTLY)
         val specHeight = View.MeasureSpec.makeMeasureSpec(dpToPx(108).toInt(), View.MeasureSpec.EXACTLY)

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