package com.tokopedia.tokomember_seller_dashboard.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
     private fun getBitmapFromView(view: View): Bitmap? {
        //Define a bitmap with the same size as the view
         val spec = View.MeasureSpec.makeMeasureSpec(
             ViewGroup.LayoutParams.WRAP_CONTENT,
             View.MeasureSpec.UNSPECIFIED
         )

         view.measure(spec, spec)
         view.layout(0, 0, view.measuredWidth, view.measuredHeight)
         val returnedBitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(ContextCompat.getColor(view.context,com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
        }
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
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
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, oStream)
            oStream.flush()
            oStream.close()
        } catch (e: IOException) {
            Timber.d(e)
        }
        return pictureFile
    }
}