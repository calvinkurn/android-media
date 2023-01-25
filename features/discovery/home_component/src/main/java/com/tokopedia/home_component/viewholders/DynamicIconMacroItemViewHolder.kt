package com.tokopedia.home_component.viewholders

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by dhaba
 */
class DynamicIconMacroItemViewHolder(
    itemView: View,
    private val listener: DynamicIconComponentListener
) : RecyclerView.ViewHolder(itemView) {
    private var iconTvName: Typography? = null
    private var iconImageView: ImageUnify? = null
    private var iconContainer: ConstraintLayout? = null
    private var containerRipple: ConstraintLayout? = null
    private val longPressHandler = Handler(Looper.getMainLooper())
    private var isLongPress = false
    private var scaleAnimator = ValueAnimator.ofFloat()
    private var rippleAnimator = ValueAnimator.ofFloat()
    private var onLongPress = Runnable {
        isLongPress = true
        itemView.performLongClick()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_dynamic_icon_item_interaction
        private const val SCALE_MIN_IMAGE = 0.8f
        private const val SCALE_MAX_IMAGE = 1f
        private val pathInterpolator = PathInterpolatorCompat.create(.63f, .01f, .19f, 1.15f)
        private const val DURATION_INPUT_CLICK = 200L
        private const val DURATION_OUTPUT_CLICK = 120L
        private const val MAX_ALPHA_RIPPLE = 0.6f
    }

    private fun View.performTouchDown() = dispatchTouchEvent(
        MotionEvent.obtain(
            SystemClock.uptimeMillis(),
            SystemClock.uptimeMillis(),
            MotionEvent.ACTION_DOWN,
            Float.ZERO,
            Float.ZERO,
            Int.ZERO
        )
    )

    private fun animateScaling(
        start: Float,
        end: Float,
        duration: Long
    ) {
        scaleAnimator = ValueAnimator.ofFloat()
        scaleAnimator.setFloatValues(start, end)
        scaleAnimator.removeAllListeners()
        scaleAnimator.removeAllUpdateListeners()
        scaleAnimator.addUpdateListener {
            iconImageView?.scaleX = it.animatedValue as Float
            iconImageView?.scaleY = it.animatedValue as Float
        }
        scaleAnimator.duration = duration
        scaleAnimator.interpolator = pathInterpolator
        scaleAnimator.start()
    }

    private fun scalingRipple(
        start: Float,
        end: Float,
        duration: Long,
        isShow: Boolean
    ) {
        rippleAnimator = ValueAnimator.ofFloat()
        rippleAnimator.setFloatValues(start, end)
        rippleAnimator.removeAllListeners()
        rippleAnimator.removeAllUpdateListeners()
        rippleAnimator.addUpdateListener {
//            it.
            containerRipple?.visible()
            val value = it.animatedValue as Float
            containerRipple?.scaleX = value
            containerRipple?.scaleY = value
            containerRipple?.alpha = ((value - SCALE_MIN_IMAGE) / (SCALE_MAX_IMAGE - SCALE_MIN_IMAGE)) * MAX_ALPHA_RIPPLE
//            if (!isShow && it.animatedValue as Float == SCALE_MIN_IMAGE) containerRipple?.invisible()
        }
        rippleAnimator.duration = duration
        rippleAnimator.interpolator = pathInterpolator
        rippleAnimator.start()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun bind(
        item: DynamicIconComponent.DynamicIcon,
        isScrollable: Boolean,
        parentPosition: Int,
        type: Int,
        isCache: Boolean
    ) {
        iconTvName = itemView.findViewById(R.id.dynamic_icon_typography)
        iconImageView = itemView.findViewById(R.id.dynamic_icon_image_view)
        iconContainer = itemView.findViewById(R.id.dynamic_icon_container)
        containerRipple = itemView.findViewById(R.id.container_ripple_dynamic_icons)
        iconTvName?.text = item.name
        iconImageView?.setImageUrl(item.imageUrl)
        iconContainer?.layoutParams = ViewGroup.LayoutParams(
            if (isScrollable) ViewGroup.LayoutParams.WRAP_CONTENT else ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        iconContainer?.setOnTouchListener { _, event ->
            when (event?.action) {
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    longPressHandler.removeCallbacks(onLongPress)
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            if (iconImageView?.scaleX == SCALE_MIN_IMAGE) {
                                animateScaling(
                                    SCALE_MIN_IMAGE,
                                    SCALE_MAX_IMAGE,
                                    DURATION_OUTPUT_CLICK
                                )
                            }
                            if (containerRipple?.alpha == MAX_ALPHA_RIPPLE) {
                                scalingRipple(
                                    SCALE_MIN_IMAGE,
                                    SCALE_MIN_IMAGE,
                                    DURATION_OUTPUT_CLICK,
                                    false
                                )
                            }
                        },
                        0
                    )
                    rippleAnimator.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(p0: Animator) {
                            val a = 1 + 1
                        }

                        override fun onAnimationEnd(p0: Animator) {
                            val a = 1 + 1
                            if (containerRipple?.alpha == MAX_ALPHA_RIPPLE) {
                                scalingRipple(
                                    SCALE_MAX_IMAGE,
                                    SCALE_MIN_IMAGE,
                                    DURATION_OUTPUT_CLICK,
                                    false
                                )
                            }
                        }

                        override fun onAnimationCancel(p0: Animator) {
                            scalingRipple(
                                SCALE_MIN_IMAGE,
                                SCALE_MIN_IMAGE,
                                DURATION_OUTPUT_CLICK,
                                false
                            )
                        }

                        override fun onAnimationRepeat(p0: Animator) {
                            val a = 1 + 1
                        }
                    })
                    scaleAnimator.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator) {}

                        override fun onAnimationCancel(p0: Animator) {}

                        override fun onAnimationStart(p0: Animator) {}

                        override fun onAnimationEnd(p0: Animator) {
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    animateScaling(
                                        SCALE_MAX_IMAGE,
                                        SCALE_MAX_IMAGE,
                                        DURATION_OUTPUT_CLICK
                                    )
                                },
                                0
                            )
                        }
                    })
                }
                MotionEvent.ACTION_DOWN -> {
                    longPressHandler.postDelayed(
                        onLongPress,
                        ViewConfiguration.getLongPressTimeout().toLong()
                    )
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            scalingRipple(
                                SCALE_MIN_IMAGE,
                                SCALE_MAX_IMAGE,
                                200,
                                true
                            )
                            animateScaling(SCALE_MAX_IMAGE, SCALE_MIN_IMAGE, 200)
                        },
                        0
                    )
                }
            }
            false
        }
        itemView.setOnClickListener {
            listener.onClickIcon(item, parentPosition, absoluteAdapterPosition, type)
//            containerRipple?.visibility = View.INVISIBLE
        }

        if (!isCache) {
            itemView.addOnImpressionListener(item) {
                listener.onImpressIcon(
                    dynamicIcon = item,
                    iconPosition = absoluteAdapterPosition,
                    parentPosition = parentPosition,
                    type = type,
                    view = itemView
                )
            }
        }
    }
}
