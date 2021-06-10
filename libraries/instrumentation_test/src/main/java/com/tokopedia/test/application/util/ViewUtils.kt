package com.tokopedia.test.application.util

import android.app.Activity
import android.content.Context
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
    fun View?.takeScreenShot(fileName: String, dir: String = "") {
        if (this != null) {
            this.setBackgroundColor(ContextCompat.getColor(this.context, R.color.Unify_N0))
            Handler(Looper.getMainLooper()).post {
                val bitmap = Bitmap.createBitmap(
                        this.width,
                        this.height, Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                this.draw(canvas)
                saveImage(this.context, dir, fileName, bitmap)
            }
        }
    }

    fun mergeScreenShot(fileName: String, dir: String = "", views: List<View?>?) {
        var context: Context? = null
        var totalHeight = 0
        var totalWidth = 0
        var accumulateHeight = 0

        views?.forEach {
            val height = it?.height ?: 0
            totalWidth = it?.width ?: 0
            totalHeight += height
        }

        val combineBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
        val c = Canvas(combineBitmap)

        views?.forEachIndexed { index, it ->
            val view = it ?: return
            context = view.context
            Handler(Looper.getMainLooper()).post {
                val bitmap = Bitmap.createBitmap(
                        view.width,
                        view.height, Bitmap.Config.ARGB_8888
                )
                val b1 = Canvas(bitmap)

                val bgDrawable = view.background
                if (bgDrawable != null) {
                    //has background drawable, then draw it on the canvas
                    bgDrawable.draw(b1)
                } else {
                    //does not have background drawable, then draw white background on the canvas
                    b1.drawColor(ContextCompat.getColor(context!!, R.color.Unify_N0))
                }
                view.draw(b1)

                if (index == 0) {
                    c.drawBitmap(bitmap, 0F, 0F, null)
                } else {
                    c.drawBitmap(bitmap, 0F, accumulateHeight.toFloat(), null)
                }
                accumulateHeight += view.height
            }
        }

        saveImage(context, dir, fileName, combineBitmap)

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
            saveImage(this.applicationContext, dir, fileName, bitmap)
        }
    }

    fun saveImage(context: Context?, dir: String, fileName: String, bitmap: Bitmap?) {
        val path = context?.getExternalFilesDir(null)
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