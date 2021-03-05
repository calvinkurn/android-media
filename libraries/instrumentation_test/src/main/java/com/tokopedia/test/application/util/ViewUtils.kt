package com.tokopedia.test.application.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.instrumentation.test.R
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Yehezkiel on 29/11/20
 */
object ViewUtils {

    /**
     * Screenshot specific view
     */
    @JvmStatic
    fun Activity.takeScreenShot(view: View?, fileName: String, dir: String = "") {
        if (view != null) {
            view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.Unify_N0))
            Handler(Looper.getMainLooper()).post {
                val bitmap = Bitmap.createBitmap(
                        view.width,
                        view.height, Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                view.draw(canvas)
                saveImage(this, dir, fileName, bitmap)
            }
        }
    }

    /**
     * Screenshot entire activity/fragment
     */
    @JvmStatic
    fun Activity.takeScreenShot(fileName: String, dir: String = "") {
        Handler(Looper.getMainLooper()).post {
            val view = window.decorView
            val bitmap = Bitmap.createBitmap(
                    view.width,
                    view.height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            saveImage(this, dir, fileName, bitmap)
        }
    }

    fun saveImage(activity: Activity, dir: String, fileName: String, bitmap: Bitmap?) {
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
        } catch (e: FileNotFoundException) {
            Timber.d("screenshot fail : ${e.message}")
        } catch (e: IOException) {
            Timber.d("screenshot fail : ${e.message}")
        }
    }
}