package com.tokopedia.home_component.customview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.shapes.Shape
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur


/**
 * Created by dhaba
 */
internal class DrawableShadow(
    ctx: Context,
    source: Drawable,
    mask: Drawable,
    shadow: Drawable,
    shadowRadius: Float
) :
    Shape() {
    var ctx: Context
    var source: Drawable
    var mask: Drawable
    var shadow: Drawable
    var shadowRadius: Float
    var matrix: Matrix = Matrix()
    var bitmap: Bitmap? = null
    override fun onResize(width: Float, height: Float) {
        val intrinsicWidth = source.intrinsicWidth
        val intrinsicHeight = source.intrinsicHeight
        source.setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        mask.setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        shadow.setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        val src = RectF(0f, 0f, intrinsicWidth.toFloat(), intrinsicHeight.toFloat())
        val dst = RectF(0f, 0f, width, height)
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER)
        val p = Paint()
        p.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        var c = Canvas(bitmap!!)
        shadow.draw(c)
        c.saveLayer(null, p, Canvas.ALL_SAVE_FLAG)
        mask.draw(c)
        c.restore()
        bitmap = blurRenderScript(bitmap)
        c = Canvas(bitmap!!)
        val count = c.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)
        source.draw(c)
        c.saveLayer(null, p, Canvas.ALL_SAVE_FLAG)
        mask.draw(c)
        c.restoreToCount(count)
    }

    override fun draw(canvas: Canvas, paint: Paint?) {
        canvas.drawColor(-0x222223)
        canvas.drawBitmap(bitmap!!, matrix, null)
    }

    fun blurRenderScript(input: Bitmap?): Bitmap {
        val output = Bitmap.createBitmap(input!!.width, input.height, input.config)
        val rs = RenderScript.create(ctx)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val inAlloc: Allocation = Allocation.createFromBitmap(
            rs,
            input,
            Allocation.MipmapControl.MIPMAP_NONE,
            Allocation.USAGE_GRAPHICS_TEXTURE
        )
        val outAlloc: Allocation = Allocation.createFromBitmap(rs, output)
        script.setRadius(shadowRadius)
        script.setInput(inAlloc)
        script.forEach(outAlloc)
        outAlloc.copyTo(output)
        rs.destroy()
        return output
    }

    init {
        this.ctx = ctx
        this.source = source
        this.mask = mask
        this.shadow = shadow
        this.shadowRadius = shadowRadius
    }
}