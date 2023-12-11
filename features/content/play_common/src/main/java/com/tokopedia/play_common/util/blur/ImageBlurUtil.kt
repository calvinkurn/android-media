package com.tokopedia.play_common.util.blur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.widget.ImageView
import androidx.annotation.FloatRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Closeable

/**
 * Created by jegul on 09/09/20
 */
class ImageBlurUtil(context: Context) : Closeable {

    private val renderScript = RenderScript.create(context)

    suspend fun blurImage(
        src: Bitmap,
        @FloatRange(from = 0.0, to = 25.0) radius: Float = 5.0f
    ): Bitmap = withContext(Dispatchers.IO) {
        val outputBitmap: Bitmap =
            Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)

        return@withContext try {
            val inputAllocation = Allocation.createFromBitmap(renderScript, src)
            val outputAllocation = Allocation.createTyped(renderScript, inputAllocation.type)
            val blurScript =
                ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript)).apply {
                    setInput(inputAllocation)
                    setRadius(radius)
                }
            blurScript.forEach(outputAllocation)
            outputAllocation.copyTo(outputBitmap)
            outputBitmap
        } catch (ex: Exception) {
            outputBitmap.recycle()
            src
        }
    }

    suspend fun blurredView(
        src: Bitmap,
        view: ImageView,
        @FloatRange(from = 0.0, to = 25.0) radius: Float = 25.0f,
        repeatCount: Int = 1,
        shader: Shader.TileMode = Shader.TileMode.CLAMP
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val newRadius = radius * repeatCount
            view.setImageBitmap(src)
            view.setRenderEffect(
                RenderEffect.createBlurEffect(
                    newRadius,
                    newRadius,
                    shader
                )
            )
        } else {
            var resultBlur: Bitmap = src
            repeat(repeatCount) {
                 resultBlur = blurImage(src, radius)
            }
            view.setImageBitmap(resultBlur)
        }
    }

    override fun close() {
        renderScript.destroy()
    }
}
