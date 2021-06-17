package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.play.broadcaster.R


/**
 * Created by mzennis on 16/06/21.
 */
class SurfaceAspectRatioView : FrameLayout {

    val surfaceHolder: SurfaceHolder
        get() = mSurfaceView.holder

    private var mTargetAspect = -1.0 // initially use default window size

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var mSurfaceView: SurfaceView

    init {
        val view = View.inflate(context, R.layout.view_play_surface_aspect_ratio, this)
        mSurfaceView = view.findViewById(R.id.surface_view)
    }

    fun setCallback(callback: Callback) {
        mSurfaceView.holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder?) {
                callback.onSurfaceCreated()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                callback.onSurfaceDestroyed()
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var modifyWidthMeasureSpec = widthMeasureSpec
        var modifyHeightMeasureSpec = heightMeasureSpec

        // Target aspect ratio will be < 0 if it hasn't been set yet.  In that case,
        // we just use whatever we've been handed.
        if (mTargetAspect > 0) {
            var initialWidth = MeasureSpec.getSize(modifyWidthMeasureSpec)
            var initialHeight = MeasureSpec.getSize(modifyHeightMeasureSpec)

            // factor the padding out
            val horizPadding = paddingLeft + paddingRight
            val vertPadding = paddingTop + paddingBottom
            initialWidth -= horizPadding
            initialHeight -= vertPadding
            val viewAspectRatio = initialWidth.toDouble() / initialHeight
            val aspectDiff = mTargetAspect / viewAspectRatio - 1
            if (Math.abs(aspectDiff) < 0.01) {
                // We're very close already.  We don't want to risk switching from e.g. non-scaled
                // 1280x720 to scaled 1280x719 because of some floating-point round-off error,
                // so if we're really close just leave it alone.
            } else {
                if (aspectDiff > 0) {
                    // limited by narrow width; restrict height
                    initialHeight = (initialWidth / mTargetAspect).toInt()
                } else {
                    // limited by short height; restrict width
                    initialWidth = (initialHeight * mTargetAspect).toInt()
                }
                initialWidth += horizPadding
                initialHeight += vertPadding
                modifyWidthMeasureSpec = MeasureSpec.makeMeasureSpec(initialWidth, MeasureSpec.EXACTLY)
                modifyHeightMeasureSpec = MeasureSpec.makeMeasureSpec(initialHeight, MeasureSpec.EXACTLY)
            }
        }
        super.onMeasure(modifyWidthMeasureSpec, modifyHeightMeasureSpec)
    }

    interface Callback {
        fun onSurfaceCreated()
        fun onSurfaceDestroyed()
    }
}