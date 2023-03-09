package com.tokopedia.analytics.performance.perf

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import java.io.File
import java.util.*
import com.tokopedia.analytics.R
object PerformanceTraceDebugger {
    const val PERF_TRACE_LOG = "PerformanceTraceDebugger"
    var DEBUG = false

    fun logTrace(message: String) {
        if (DEBUG) {
            Log.d("PerfTraceDebugger", message)
        }
    }

    fun Activity.takeScreenshot(name: String, v1: View) {
        if (DEBUG) {
            val systemCurrentMillis = System.currentTimeMillis()
            try {
                // create bitmap screen capture

                v1.isDrawingCacheEnabled = true
                val bitmap: Bitmap = Bitmap.createBitmap(v1.drawingCache)
                v1.isDrawingCacheEnabled = false

                val resolver: ContentResolver = this.getContentResolver()
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name+"_$systemCurrentMillis")
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + File.separator + resources.getString(
                        R.string.app_name
                    ) + File.separator + "PerfTraceScreenshot"
                )
                contentValues.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1)
                val imageUri: Uri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
                val fos = resolver.openOutputStream(Objects.requireNonNull(imageUri))

                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos?.close()
                } catch (_: Exception) {
                } finally {
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(imageUri, contentValues, null, null)
                }
            } catch (e: Throwable) {
                // Several error may come out with file handling or DOM
            }
        }
    }
}
