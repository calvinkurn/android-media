package com.tokopedia.scp_rewards_touchpoints.bottomsheet.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.scp_rewards_touchpoints.R
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ScpCouponView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    companion object {
        private const val EDGE_RADIUS_MULTIPLIER = 10f
    }
    var circularEdgeColor = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN0)
        set(value) {
            field = value
            refresh()
        }
    var isEdgeControl = true
        set(value) {
            field = value
            refresh()
        }
    private var circularEdgePaint: Paint = Paint()
    private var path = Path()
    private var shimmerPlaceholder: AnimatedVectorDrawableCompat? = null

    init {
        circularEdgePaint.apply {
            color = circularEdgeColor
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    private fun refresh() {
        circularEdgePaint.color = circularEdgeColor
        shimmerPlaceholder = null
        invalidate()
    }

    override fun draw(canvas: Canvas?) {
        canvas?.clipPath(path)
        super.draw(canvas)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (shimmerPlaceholder == null && isEdgeControl) {
            val edgeRadius = height / EDGE_RADIUS_MULTIPLIER
            val leftCx = 0f
            val rightCx = width.toFloat()
            val leftCy = height / 2f
            val rightCy = height / 2f
            canvas?.drawCircle(
                leftCx,
                leftCy,
                edgeRadius,
                circularEdgePaint
            )
            canvas?.drawCircle(
                rightCx,
                rightCy,
                edgeRadius,
                circularEdgePaint
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val rect = RectF(0f, 0f, w.toFloat(), h.toFloat())
        path.reset()
        val radius = resources.getDimensionPixelSize(R.dimen.coupon_image_corner_radius).toFloat()
        path.addRoundRect(
            rect,
            radius,
            radius,
            Path.Direction.CW
        )
    }

    fun setCouponToLockedState() {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f) // 0 means grayscale
        colorFilter = ColorMatrixColorFilter(matrix)
    }

    fun setCouponToActiveState() {
        clearColorFilter()
    }

    fun setImageUrl(url: String) {
        initLoading()
        Glide.with(context)
            .asDrawable()
            .load(url)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    clearLoading()
                    setImageDrawable(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun initLoading() {
        shimmerPlaceholder = AnimatedVectorDrawableCompat.create(
            context,
            com.tokopedia.unifycomponents.R.drawable.unify_loader_shimmer
        )
        background = shimmerPlaceholder
        applyLoopingAnimatedVectorDrawable()
    }

    private fun clearLoading() {
        background = null
        shimmerPlaceholder?.stop()
        shimmerPlaceholder?.clearAnimationCallbacks()
        shimmerPlaceholder = null
    }

    private fun applyLoopingAnimatedVectorDrawable() {
        shimmerPlaceholder?.registerAnimationCallback(object :
                Animatable2Compat.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    post {
                        shimmerPlaceholder?.start()
                    }
                }
            })
        shimmerPlaceholder?.start()
    }

    override fun onDetachedFromWindow() {
        clearLoading()
        super.onDetachedFromWindow()
    }
}
