package com.tokopedia.home_component.viewholders

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.os.Handler
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
    var iconTvName: Typography? = null
    var iconImageView: ImageUnify? = null
    var iconContainer: LinearLayout? = null

//    var iconContainerImage: CardUnify2? = null
    val longPressHandler = Handler()
    private var isLongPress = false
    private var scaleAnimator = ValueAnimator.ofFloat()

    var onLongPress = Runnable {
        if (itemView != null) {
            isLongPress = true
            itemView.performLongClick()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_dynamic_icon_item_interaction
    }

    fun View.performTouchDown() = dispatchTouchEvent(
        MotionEvent.obtain(
            SystemClock.uptimeMillis(),
            SystemClock.uptimeMillis() + 700,
            MotionEvent.ACTION_DOWN,
            0f,
            0f,
            0
        )
    )

    private fun animateScaling(
        start: Float,
        end: Float,
        duration: Long,
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
        scaleAnimator.duration = duration
        scaleAnimator.interpolator = interpolator
        scaleAnimator.start()
    }

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

        iconTvName?.text = item.name
        iconImageView?.setImageUrl(item.imageUrl)
        iconContainer?.layoutParams = ViewGroup.LayoutParams(
            if (isScrollable) ViewGroup.LayoutParams.WRAP_CONTENT else ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        iconTvName?.minLines = 2
//        iconContainer?.cardType = CardUnify2.TYPE_CLEAR
//        iconContainer?.animateOnPress = CardUnify2.ANIMATE_OVERLAY
//        iconContainerImage?.cardType = CardUnify2.TYPE_CLEAR

//        iconContainer?.setCardUnifyBackgroundColor(
//            ContextCompat.getColor(
//                itemView.context,
//                com.tokopedia.unifyprinciples.R.color.Unify_Background
//            )
//        )

//            iconContainerImage?.animateOnPress = CardUnify2.ANIMATE_BOUNCE
        iconContainer?.setOnTouchListener { view, event ->
            iconImageView?.performTouchDown()

            when (event?.action) {
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    longPressHandler.removeCallbacks(onLongPress)
                    Handler().postDelayed(
                        {
                            animateScaling(0.8125f, 1.00f, UnifyMotion.T1, UnifyMotion.EASE_OUT)

                            scaleAnimator.addListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(p0: Animator) {
                                }

                                override fun onAnimationCancel(p0: Animator) {
                                }

                                override fun onAnimationStart(p0: Animator) {
                                }

                                override fun onAnimationEnd(p0: Animator) {
                                    animateScaling(
                                        1.00f,
                                        1f,
                                        UnifyMotion.T1,
                                        UnifyMotion.EASE_IN_OUT
                                    )
                                }
                            })
                        },
                        if (event.eventTime - event.downTime <= UnifyMotion.T1) UnifyMotion.T1 - (event.eventTime - event.downTime) else 0.toLong()
                    )

                    if (event.action == MotionEvent.ACTION_UP) {
                        if (!isLongPress) {
                            itemView.performClick()
                        }
                        isLongPress = false
                    }
                    true
                }

                MotionEvent.ACTION_DOWN -> {
                    longPressHandler.postDelayed(
                        onLongPress,
                        ViewConfiguration.getLongPressTimeout().toLong()
                    )

                    animateScaling(1f, 0.8125f, UnifyMotion.T1, UnifyMotion.EASE_OUT)

                    true
                }
            }

            false
        }
        itemView.setOnClickListener {
            listener.onClickIcon(item, parentPosition, adapterPosition, type)
        }

//        iconContainer?.setCardBackgroundColor(
//            ContextCompat.getColor(
//                itemView.context,
//                com.tokopedia.unifyprinciples.R.color.Unify_Background
//            )
//        )

        if (!isCache) {
            itemView.addOnImpressionListener(item) {
                listener.onImpressIcon(
                    dynamicIcon = item,
                    iconPosition = adapterPosition,
                    parentPosition = parentPosition,
                    type = type,
                    view = itemView
                )
            }
        }
    }
}
