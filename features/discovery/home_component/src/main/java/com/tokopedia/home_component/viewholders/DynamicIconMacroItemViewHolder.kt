package com.tokopedia.home_component.viewholders

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.Interpolator
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.UnifyMotion

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
    private var currentScaleImage = 0f
    private var currentScaleRipple = 0f
    private var onLongPress = Runnable {
        isLongPress = true
        itemView.performLongClick()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_dynamic_icon_item_interaction
        private const val SCALE_MIN_IMAGE = 0.8f
        private const val SCALE_MAX_IMAGE = 1f

        private val pathInputClick = UnifyMotion.EASE_OUT
        private val pathOutputClick = UnifyMotion.EASE_IN_OUT
        private val DURATION_INPUT_CLICK = UnifyMotion.T2
        private val DURATION_OUTPUT_CLICK = UnifyMotion.T1
        private const val MAX_ALPHA_RIPPLE = 0.6f
    }

    private fun animateScaling(
        start: Float = currentScaleImage,
        end: Float,
        duration: Long,
        pathInterpolator: Interpolator
    ) {
        scaleAnimator = ValueAnimator.ofFloat()
        scaleAnimator.setFloatValues(start, end)
        scaleAnimator.removeAllListeners()
        scaleAnimator.removeAllUpdateListeners()
        scaleAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            iconImageView?.scaleX = value
            iconImageView?.scaleY = value
            currentScaleImage = value
        }
        scaleAnimator.duration = duration
        scaleAnimator.interpolator = pathInterpolator
        scaleAnimator.start()
    }

    private fun scalingRipple(
        start: Float = currentScaleRipple,
        end: Float,
        duration: Long,
        pathInterpolator: Interpolator
    ) {
        rippleAnimator = ValueAnimator.ofFloat()
        rippleAnimator.setFloatValues(start, end)
        rippleAnimator.removeAllListeners()
        rippleAnimator.removeAllUpdateListeners()
        rippleAnimator.addUpdateListener {
            containerRipple?.visible()
            val value = it.animatedValue as Float
            containerRipple?.scaleX = value
            containerRipple?.scaleY = value
            containerRipple?.alpha =
                ((value - SCALE_MIN_IMAGE) / (SCALE_MAX_IMAGE - SCALE_MIN_IMAGE)) * MAX_ALPHA_RIPPLE
            currentScaleRipple = value
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
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    longPressHandler.removeCallbacks(onLongPress)
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            if (iconImageView?.scaleX == SCALE_MIN_IMAGE) {
                                animateScaling(
                                    SCALE_MIN_IMAGE,
                                    SCALE_MAX_IMAGE,
                                    DURATION_OUTPUT_CLICK,
                                    pathOutputClick
                                )
                            }
                            if (currentScaleRipple == SCALE_MAX_IMAGE) {
                                scalingRipple(
                                    SCALE_MAX_IMAGE,
                                    SCALE_MIN_IMAGE,
                                    DURATION_OUTPUT_CLICK,
                                    pathOutputClick
                                )
                            }
                        },
                        Int.ZERO.toLong()
                    )
                    rippleAnimator.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(p0: Animator) {}

                        override fun onAnimationEnd(p0: Animator) {
                            if (currentScaleRipple == SCALE_MAX_IMAGE) {
                                scalingRipple(
                                    end = SCALE_MIN_IMAGE,
                                    duration = DURATION_OUTPUT_CLICK,
                                    pathInterpolator = pathOutputClick
                                )
                            }
                        }

                        override fun onAnimationCancel(p0: Animator) {}

                        override fun onAnimationRepeat(p0: Animator) {}
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
                                        DURATION_OUTPUT_CLICK,
                                        pathOutputClick
                                    )
                                },
                                Int.ZERO.toLong()
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
                                DURATION_INPUT_CLICK,
                                pathInputClick
                            )
                            animateScaling(
                                SCALE_MAX_IMAGE,
                                SCALE_MIN_IMAGE,
                                DURATION_INPUT_CLICK,
                                pathInputClick
                            )
                        },
                        Int.ZERO.toLong()
                    )
                }
            }
            false
        }
        itemView.setOnClickListener {
            listener.onClickIcon(item, parentPosition, absoluteAdapterPosition, type)
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
