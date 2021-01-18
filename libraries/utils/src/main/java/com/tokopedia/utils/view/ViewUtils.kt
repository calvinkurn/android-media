package com.tokopedia.utils.view

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Yehezkiel on 29/11/20
 */
object ViewUtils {

    @JvmStatic
    fun Activity.screenShotAndSave(view: View, dir: String, fileName: String) {
        val bitmap = Bitmap.createBitmap(
                view.width,
                view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        saveImage(this, dir, fileName, bitmap)
    }

    private fun saveImage(activity: Activity, dir: String, fileName: String, bitmap: Bitmap?) {
        val path = activity.getExternalFilesDir(null)
        val imageDir = File(path, dir)

        try {
            if (!imageDir.exists() && dir.isNotEmpty()) {
                imageDir.mkdirs()
            }

            val imagePath = if (dir.isNotEmpty()) File(imageDir, "${fileName}.png") else File(path, "${fileName}.png")

            val fos = FileOutputStream(imagePath)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            Log.e("screenshot", "success")
        } catch (e: FileNotFoundException) {
            Log.e("screenshot", "fail : ${e.message}")
        } catch (e: IOException) {
            Log.e("screenshot", "fail : ${e.message}")
        }
    }
}