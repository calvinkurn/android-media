package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.Interpolator
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.home_component.analytics.TrackRecommendationMapper.asCardTrackModel
import com.tokopedia.home_component.analytics.TrackRecommendationMapper.asProductTrackModel
import com.tokopedia.home_component.databinding.HomeComponentItemMissionWidgetClearBinding
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.home_component.util.HomeComponentFeatureFlag
import com.tokopedia.home_component.util.loadImageRounded
import com.tokopedia.home_component.util.overlay
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.R as home_componentR

/**
 * Created by dhaba
 */
class MissionWidgetClearItemViewHolder(
    view: View,
    private val missionWidgetComponentListener: MissionWidgetComponentListener
) : AbstractViewHolder<CarouselMissionWidgetDataModel>(view) {
    val pathInputClick = UnifyMotion.EASE_OUT
    val pathOutputClick = UnifyMotion.EASE_IN_OUT
    val durationInputClick = UnifyMotion.T2
    val durationOutputClick = UnifyMotion.T1
    val scaleMinImage: Float = SCALE_MIN_IMAGE
    val maxAlphaRipple: Float = MAX_ALPHA_RIPPLE

    private val longPressHandler = Handler(Looper.getMainLooper())
    private var scaleAnimator = ValueAnimator.ofFloat()
    private var rippleAnimator = ValueAnimator.ofFloat()
    private var currentScaleRipple = 0f

    private var onLongPress = Runnable {
        itemView.performLongClick()
    }

    private var binding: HomeComponentItemMissionWidgetClearBinding? by viewBinding()

    companion object {
        val LAYOUT = home_componentR.layout.home_component_item_mission_widget_clear
        private const val SCALE_MAX_IMAGE = 1f
        private const val SCALE_MIN_IMAGE = 0.9375f
        private const val MAX_ALPHA_RIPPLE = 0.6f
    }

    override fun bind(element: CarouselMissionWidgetDataModel) {
        setupListeners(element)
        setLayoutParams(element)
        renderImage(element.data.imageURL)
        renderText(element)
    }

    private fun setupListeners(element: CarouselMissionWidgetDataModel) {
        setOnTouchListener()
        binding?.run {
            containerMissionWidget.setOnClickListener {
                if (element.isProduct()) {
                    AppLogRecommendation.sendProductClickAppLog(
                        element.asProductTrackModel(element.isCache)
                    )
                } else {
                    AppLogRecommendation.sendCardClickAppLog(element.asCardTrackModel(element.isCache))
                }
                AppLogAnalytics.setGlobalParamOnClick(enterMethod = AppLogParam.ENTER_METHOD_FMT_PAGENAME.format("${element.data.pageName}_${element.cardPosition + 1}"))
                missionWidgetComponentListener.onMissionClicked(element, element.cardPosition)
            }
            containerMissionWidget.addOnImpressionListener(element) {
                missionWidgetComponentListener.onMissionImpressed(element, element.cardPosition)
            }
            containerMissionWidget.addOnImpression1pxListener(element.data.appLogImpressHolder) {
                if (element.isProduct()) {
                    AppLogRecommendation.sendProductShowAppLog(element.asProductTrackModel(element.isCache))
                } else {
                    AppLogRecommendation.sendCardShowAppLog(element.asCardTrackModel(element.isCache))
                }
            }
        }
    }

    private fun setLayoutParams(element: CarouselMissionWidgetDataModel) {
        val imageLayoutParams = binding?.imageMissionWidget?.layoutParams
        val titleLayoutParams = binding?.titleMissionWidget?.layoutParams
        val subtitleLayoutParams = binding?.subtitleMissionWidget?.layoutParams
        imageLayoutParams?.width = element.width
        titleLayoutParams?.height = element.titleHeight
        subtitleLayoutParams?.height = element.subtitleHeight
        binding?.imageMissionWidget?.layoutParams = imageLayoutParams
        binding?.titleMissionWidget?.layoutParams = titleLayoutParams
        binding?.subtitleMissionWidget?.layoutParams = subtitleLayoutParams
    }

    private fun renderImage(imageUrl: String) {
        binding?.imageMissionWidget?.apply {
            loadImageRounded(
                imageUrl,
                itemView.context.resources.getDimensionPixelSize(home_componentR.dimen.home_mission_widget_clear_image_corner_radius)
            )
            if (HomeComponentFeatureFlag.isMissionExpVariant()) {
                overlay()
            }
        }
    }

    private fun renderText(element: CarouselMissionWidgetDataModel) {
        binding?.run {
            titleMissionWidget.renderTitle(element)
            subtitleMissionWidget.renderSubtitle(element)
        }
    }

    private fun Typography.renderTitle(element: CarouselMissionWidgetDataModel) {
        if (element.data.title.isEmpty()) {
            hide()
        } else {
            val fontWeight = if (element.withSubtitle) {
                Typography.BOLD
            } else {
                Typography.REGULAR
            }
            text = element.data.title
            setWeight(fontWeight)
            show()
        }
    }

    private fun Typography.renderSubtitle(element: CarouselMissionWidgetDataModel) {
        if (element.data.subTitle.isEmpty() || !element.withSubtitle) {
            hide()
        } else {
            text = element.data.subTitle
            show()
        }
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
            binding?.imageMissionWidget?.scaleX = value
            binding?.imageMissionWidget?.scaleY = value
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
            binding?.containerRippleMission?.show()
            val value = it.animatedValue as Float
            if (start < end) {
                binding?.containerRippleMission?.scaleX = value
                binding?.containerRippleMission?.scaleY = value
            }
            val alpha =
                ((value - scaleMinImage) / (SCALE_MAX_IMAGE - scaleMinImage)) * maxAlphaRipple
            binding?.containerRippleMission?.alpha = alpha
            currentScaleRipple = value
        }
        rippleAnimator.duration = duration
        rippleAnimator.interpolator = pathInterpolator
        rippleAnimator.start()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchListener() {
        binding?.containerMissionWidget?.setOnTouchListener { _, event ->
            when (event?.action) {
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    longPressHandler.removeCallbacks(onLongPress)
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            if (binding?.imageMissionWidget?.scaleX == scaleMinImage) {
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
                                    if (binding?.imageMissionWidget?.scaleX == scaleMinImage) {
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
}
