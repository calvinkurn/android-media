package com.tokopedia.home_component.viewholders

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
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
    private var iconContainer: LinearLayout? = null
    private val longPressHandler = Handler(Looper.getMainLooper())
    private var isLongPress = false
    private var scaleAnimator = ValueAnimator.ofFloat()
    private var onLongPress = Runnable {
        isLongPress = true
        itemView.performLongClick()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_dynamic_icon_item_interaction
        private const val SCALE_MIN_IMAGE = 0.8125f
        private const val SCALE_MAX_IMAGE = 1f
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
        interpolator: TimeInterpolator
    ) {
        scaleAnimator = ValueAnimator.ofFloat()
        scaleAnimator.setFloatValues(start, end)
        scaleAnimator.removeAllListeners()
        scaleAnimator.removeAllUpdateListeners()
        scaleAnimator.addUpdateListener {
            iconImageView?.scaleX = it.animatedValue as Float
            iconImageView?.scaleY = it.animatedValue as Float
        }
        scaleAnimator.duration = UnifyMotion.T1
        scaleAnimator.interpolator = interpolator
        scaleAnimator.start()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun bind(
        item: DynamicIconComponent.DynamicIcon,
        isScrollable: Boolean,
        parentPosition: Int,
        type: Int,
        isCache: Boolean,
        titleHeight: Int
    ) {
        iconTvName = itemView.findViewById(R.id.dynamic_icon_typography)
        iconImageView = itemView.findViewById(R.id.dynamic_icon_image_view)
        iconContainer = itemView.findViewById(R.id.dynamic_icon_container)
        iconTvName?.text = item.name
        iconImageView?.setImageUrl(item.imageUrl)
        iconContainer?.layoutParams = ViewGroup.LayoutParams(
            if (isScrollable) ViewGroup.LayoutParams.WRAP_CONTENT else ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        iconTvName?.height = titleHeight
        iconContainer?.setOnTouchListener { _, event ->
            iconImageView?.performTouchDown()
            when (event?.action) {
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    longPressHandler.removeCallbacks(onLongPress)
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            animateScaling(SCALE_MIN_IMAGE, SCALE_MAX_IMAGE, UnifyMotion.EASE_OUT)
                            scaleAnimator.addListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(p0: Animator) {}

                                override fun onAnimationCancel(p0: Animator) {}

                                override fun onAnimationStart(p0: Animator) {}

                                override fun onAnimationEnd(p0: Animator) {
                                    animateScaling(
                                        SCALE_MAX_IMAGE,
                                        SCALE_MAX_IMAGE,
                                        UnifyMotion.EASE_IN_OUT
                                    )
                                }
                            })
                        },
                        if (event.eventTime - event.downTime <= UnifyMotion.T1) UnifyMotion.T1 - (event.eventTime - event.downTime) else Int.ZERO.toLong()
                    )
                }
                MotionEvent.ACTION_DOWN -> {
                    longPressHandler.postDelayed(
                        onLongPress,
                        ViewConfiguration.getLongPressTimeout().toLong()
                    )
                    animateScaling(SCALE_MAX_IMAGE, SCALE_MIN_IMAGE, UnifyMotion.EASE_OUT)
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
