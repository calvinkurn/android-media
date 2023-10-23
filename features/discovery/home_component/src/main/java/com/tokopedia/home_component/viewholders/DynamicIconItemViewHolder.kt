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
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.UnifyMotion

/**
 * Created by dhaba
 */
abstract class DynamicIconItemViewHolder(
    itemView: View,
    private val listener: DynamicIconComponentListener
) : RecyclerView.ViewHolder(itemView) {
    open val pathInputClick = UnifyMotion.EASE_OUT
    open val pathOutputClick = UnifyMotion.EASE_IN_OUT
    open val durationInputClick = UnifyMotion.T2
    open val durationOutputClick = UnifyMotion.T1
    open val scaleMinImage: Float = SCALE_MIN_IMAGE
    open val maxAlphaRipple: Float = MAX_ALPHA_RIPPLE

    abstract val dynamicIconTypography: Typography?
    abstract val dynamicIconImageView: ImageView?
    abstract val dynamicIconRippleContainer: View?
    abstract val dynamicIconContainer: ViewGroup?

    private val longPressHandler = Handler(Looper.getMainLooper())
    private var scaleAnimator = ValueAnimator.ofFloat()
    private var rippleAnimator = ValueAnimator.ofFloat()
    private var currentScaleRipple = 0f
    
    private var onLongPress = Runnable {
        itemView.performLongClick()
    }

    companion object {
        private const val SCALE_MAX_IMAGE = 1f
        private const val SCALE_MIN_IMAGE = 0.9375f
        private const val MAX_ALPHA_RIPPLE = 0.6f
        
    }

    private fun animateScaling(
        start: Float,
        end: Float,
        duration: Long,
        pathInterpolator: Interpolator
    ) {
        scaleAnimator.setFloatValues(start, end)
        scaleAnimator.removeAllListeners()
        scaleAnimator.removeAllUpdateListeners()
        scaleAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            dynamicIconImageView?.scaleX = value
            dynamicIconImageView?.scaleY = value
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
        rippleAnimator.setFloatValues(start, end)
        rippleAnimator.removeAllListeners()
        rippleAnimator.removeAllUpdateListeners()
        rippleAnimator.addUpdateListener {
            dynamicIconRippleContainer?.visible()
            val value = it.animatedValue as Float
            if (start < end) {
                dynamicIconRippleContainer?.scaleX = value
                dynamicIconRippleContainer?.scaleY = value
            }
            val alpha =
                ((value - scaleMinImage) / (SCALE_MAX_IMAGE - scaleMinImage)) * maxAlphaRipple
            dynamicIconRippleContainer?.alpha = alpha
            currentScaleRipple = value
        }
        rippleAnimator.duration = duration
        rippleAnimator.interpolator = pathInterpolator
        rippleAnimator.start()
    }

    @SuppressLint("ClickableViewAccessibility")
    open fun bind(
        item: DynamicIconComponent.DynamicIcon,
        isScrollable: Boolean,
        parentPosition: Int,
        isCache: Boolean,
    ) {
        renderData(item, isScrollable)
        setupListeners(item, parentPosition, isCache)
    }

    private fun renderData(item: DynamicIconComponent.DynamicIcon, isScrollable: Boolean) {
        dynamicIconTypography?.text = item.name
        loadImage(item)
        listener.onSuccessLoadImage()
        setLayoutParams(isScrollable)
    }

    abstract fun loadImage(item: DynamicIconComponent.DynamicIcon)

    private fun setupListeners(item: DynamicIconComponent.DynamicIcon, parentPosition: Int, isCache: Boolean) {
        setOnTouchListener()
        setOnClickListener(item, parentPosition)
        setOnImpressionListener(item, parentPosition, isCache)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchListener() {
        dynamicIconContainer?.setOnTouchListener { _, event ->
            when (event?.action) {
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    longPressHandler.removeCallbacks(onLongPress)
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            if (dynamicIconImageView?.scaleX == scaleMinImage) {
                                animateScaling(
                                    scaleMinImage,
                                    SCALE_MAX_IMAGE,
                                    durationOutputClick,
                                    pathOutputClick
                                )
                            }

                            scaleAnimator.addListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(p0: Animator) {
                                    // no-op
                                }

                                override fun onAnimationCancel(p0: Animator) {
                                    // no-op
                                }

                                override fun onAnimationStart(p0: Animator) {
                                    // no-op
                                }

                                override fun onAnimationEnd(p0: Animator) {
                                    if (dynamicIconImageView?.scaleX == scaleMinImage) {
                                        animateScaling(
                                            scaleMinImage,
                                            SCALE_MAX_IMAGE,
                                            durationOutputClick,
                                            pathOutputClick
                                        )
                                    }
                                }
                            })

                            if (currentScaleRipple == SCALE_MAX_IMAGE) {
                                scalingRipple(
                                    SCALE_MAX_IMAGE,
                                    scaleMinImage,
                                    durationOutputClick,
                                    pathOutputClick
                                )
                            }
                            rippleAnimator.addListener(object : Animator.AnimatorListener {
                                override fun onAnimationStart(p0: Animator) {
                                    // no-op
                                }

                                override fun onAnimationEnd(p0: Animator) {
                                    if (currentScaleRipple == SCALE_MAX_IMAGE) {
                                        scalingRipple(
                                            end = scaleMinImage,
                                            duration = durationOutputClick,
                                            pathInterpolator = pathOutputClick
                                        )
                                    }
                                }

                                override fun onAnimationCancel(p0: Animator) {
                                    // no-op
                                }

                                override fun onAnimationRepeat(p0: Animator) {
                                    // no-op
                                }
                            })
                        },
                        if (event.eventTime - event.downTime <= durationOutputClick) durationOutputClick - (event.eventTime - event.downTime) else 0.toLong()
                    )
                }
                MotionEvent.ACTION_DOWN -> {
                    longPressHandler.postDelayed(
                        onLongPress,
                        ViewConfiguration.getLongPressTimeout().toLong()
                    )
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            scalingRipple(
                                scaleMinImage,
                                SCALE_MAX_IMAGE,
                                durationInputClick,
                                pathInputClick
                            )
                            animateScaling(
                                SCALE_MAX_IMAGE,
                                scaleMinImage,
                                durationInputClick,
                                pathInputClick
                            )
                        },
                        Int.ZERO.toLong()
                    )
                }
            }
            false
        }
    }

    private fun setOnClickListener(item: DynamicIconComponent.DynamicIcon, parentPosition: Int) {
        itemView.setOnClickListener {
            listener.onClickIcon(item, parentPosition, absoluteAdapterPosition)
        }
    }

    private fun setOnImpressionListener(
        item: DynamicIconComponent.DynamicIcon,
        parentPosition: Int,
        isCache: Boolean,
    ) {
        if (!isCache) {
            itemView.addOnImpressionListener(item) {
                listener.onImpressIcon(
                    dynamicIcon = item,
                    iconPosition = item.position,
                    parentPosition = parentPosition,
                    view = itemView
                )
            }
        }
    }

    private fun setLayoutParams(isScrollable: Boolean) {
        dynamicIconContainer?.layoutParams = ViewGroup.LayoutParams(
            if (isScrollable) ViewGroup.LayoutParams.WRAP_CONTENT else ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
