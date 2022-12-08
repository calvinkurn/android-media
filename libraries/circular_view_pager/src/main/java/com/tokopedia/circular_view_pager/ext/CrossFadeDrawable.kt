package com.tokopedia.home_page_banner.ext

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.SystemClock
import androidx.annotation.Nullable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.transition.NoTransition
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.Transition.ViewAdapter
import com.bumptech.glide.request.transition.TransitionFactory


class CrossFadeDrawable(previousDrawable: Drawable, currentDrawable: Drawable) : Drawable(), Drawable.Callback {
    private var previousDrawable: Drawable?
    private val currentDrawable: Drawable
    private var fadeDuration = 300f
    private var startTimeMillis = 0L
    private var animating = false
    private var alpha = 0xFF
    /**
     * Indicates whether the cross fade is enabled for this transition.
     *
     * @return True if cross fading is enabled, false otherwise.
     */
    /**
     * Enables or disables the cross fade of the drawables. When cross fade
     * is disabled, the first drawable is always drawn opaque. With cross
     * fade enabled, the first drawable is drawn with the opposite alpha of
     * the second drawable. Cross fade is disabled by default.
     *
     * @param enabled True to enable cross fading, false otherwise.
     */
    var isCrossFadeEnabled = false

    fun startTransition(duration: Float) {
        fadeDuration = duration
        startTransition()
    }

    fun startTransition() {
        animating = true
        startTimeMillis = SystemClock.uptimeMillis()
        invalidateSelf()
    }

    private val normalizedTime: Float
        private get() = if (startTimeMillis == 0L) {
            0f
        } else Math.min(1f, (SystemClock.uptimeMillis() - startTimeMillis) / fadeDuration)

    override fun draw(canvas: Canvas) {
        if (!animating) {
            if (previousDrawable != null) {
                previousDrawable?.draw(canvas)
            } else {
                currentDrawable.draw(canvas)
            }
        } else {
            val normalized = normalizedTime
            if (normalized >= 1f) {
                previousDrawable?.callback = null
                animating = false
                previousDrawable = null
                currentDrawable.draw(canvas)
            } else {
                if (isCrossFadeEnabled) {
                    val partialAlpha = (alpha * normalized).toInt()
                    previousDrawable?.let { previousDrawable ->
                        previousDrawable.alpha = 255 - partialAlpha
                        previousDrawable.draw(canvas)
                        previousDrawable.alpha = alpha
                    }
                    currentDrawable.alpha = partialAlpha
                    currentDrawable.draw(canvas)
                    currentDrawable.alpha = alpha
                } else {
                    if (previousDrawable != null) {
                        previousDrawable?.draw(canvas)
                    } else {
                        currentDrawable.draw(canvas)
                    }
                }
                invalidateSelf()
            }
        }
    }

    override fun setAlpha(alpha: Int) {
        this.alpha = alpha
    }

    override fun getAlpha(): Int {
        return alpha
    }

    override fun setColorFilter(@Nullable colorFilter: ColorFilter?) {}
    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        if (previousDrawable != null) {
            previousDrawable?.bounds = bounds
        }
        currentDrawable.bounds = bounds
    }

    override fun getIntrinsicWidth(): Int {
        return if (!animating && previousDrawable != null) {
            previousDrawable?.intrinsicWidth ?: 0
        } else {
            currentDrawable.intrinsicWidth
        }
    }

    override fun getIntrinsicHeight(): Int {
        return if (!animating && previousDrawable != null) {
            previousDrawable?.intrinsicHeight ?: 0
        } else {
            currentDrawable.intrinsicHeight
        }
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun invalidateDrawable(who: Drawable) {
        invalidateSelf()
    }

    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
        scheduleSelf(what, `when`)
    }

    override fun unscheduleDrawable(who: Drawable, what: Runnable) {
        unscheduleSelf(what)
    }

    init {
        this.previousDrawable = previousDrawable
        this.currentDrawable = currentDrawable
        previousDrawable.callback = this
        currentDrawable.callback = this
    }
}

class CrossFadeFactory : TransitionFactory<Drawable> {
    override fun build(dataSource: DataSource, isFirstResource: Boolean): Transition<Drawable> {
        return if (dataSource === DataSource.MEMORY_CACHE) {
            NoTransition.get()
        } else CrossFadeTransition()
    }
}

class CrossFadeTransition : Transition<Drawable> {
    override fun transition(current: Drawable, adapter: ViewAdapter): Boolean {
        var previous = adapter.currentDrawable
        if (previous == null) {
            previous = ColorDrawable(Color.TRANSPARENT)
        }
        val crossFadeDrawable = CrossFadeDrawable(previous, current)
        crossFadeDrawable.isCrossFadeEnabled = true
        crossFadeDrawable.startTransition()
        adapter.setDrawable(crossFadeDrawable)
        return true
    }
}
