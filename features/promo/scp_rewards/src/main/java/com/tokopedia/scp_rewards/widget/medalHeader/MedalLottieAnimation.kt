package com.tokopedia.scp_rewards.widget.medalHeader

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.scale
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.scp_rewards.R
import com.tokopedia.scp_rewards.common.utils.downloadImage
import com.tokopedia.scp_rewards.databinding.WidgetMedalLottieAnimationBinding
import com.tokopedia.scp_rewards_common.EASE_IN_OUT
import com.tokopedia.scp_rewards_common.OVER_SHOOT
import com.tokopedia.scp_rewards_common.animateView
import com.tokopedia.scp_rewards_common.loadLottieFromUrl
import com.tokopedia.scp_rewards_common.propertyValueHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val SHUTTER_AUTO_CLOSE = "shutter_auto_close"
private const val SHUTTER_AUTO_OPEN = "shutter_auto_open"
private const val COACH_MARK_APPEAR = "coachmark_appear"
private const val SHUTTER_MANUAL_CLOSE = "shutter_manual_close"
private const val SHUTTER_MANUAL_OPEN = "shutter_manual_open"

class MedalLottieAnimation(private val context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private companion object {
        const val SCALE_100 = 1f
        const val SCALE_75 = 0.75f
        const val DURATION = 300L
    }

    private val binding =
        WidgetMedalLottieAnimationBinding.inflate(LayoutInflater.from(context), this)

    private var onClickAction: (() -> Unit)? = null

    fun loadLottie(data: MedalHeaderData, onClickAction: (() -> Unit)? = null) {
        this.onClickAction = onClickAction
        try {
            loadSparks(data.lottieSparklesUrl)
            downloadImages(data, {
                loadMedalBadge(data, it)
            }, {
                binding.lottieView.setImageResource(R.drawable.fallback_badge)
            })
        } catch (ex: Throwable) {
            // For Out of memory exceptions
            FirebaseCrashlytics.getInstance().recordException(ex)
        }
    }

    private fun loadSparks(lottieSparklesUrl: String?) {
        binding.lottieViewSparks.loadLottieFromUrl(
            url = lottieSparklesUrl,
            autoPlay = false
        )
    }

    private fun loadMedalBadge(data: MedalHeaderData, map: Map<String, Bitmap?>) {
        binding.apply {
            tvShutter.text = data.shutterText
            lottieView.loadLottieFromUrl(
                url = data.lottieUrl,
                onLottieLoaded = {
                    map.forEach { (key, bitmap) ->
                        val imageAsset = lottieView.composition?.images?.get(key)
                        lottieView.updateBitmap(
                            key,
                            bitmap?.scale(
                                imageAsset?.width
                                    ?: bitmap.width,
                                imageAsset?.height ?: bitmap.height
                            )
                        )
                    }
                    val markersList = listOf(
                        SHUTTER_AUTO_CLOSE,
                        SHUTTER_AUTO_OPEN,
                        SHUTTER_MANUAL_OPEN,
                        SHUTTER_MANUAL_CLOSE
                    )
                    val textAutoShow = markersList.all { name ->
                        lottieView.composition?.markers?.any { it.matchesName(name) } == true
                    }
                    if (textAutoShow) {
                        lottieView.setMaxFrame(SHUTTER_AUTO_CLOSE)
                    }
                    binding.lottieViewSparks.playAnimation()
                },
                onError = {
                    lottieView.setImageResource(R.drawable.fallback_badge)
                },
                onLottieEnded = {
                    val marker = lottieView.composition?.markers?.find { it.startFrame.toInt() == lottieView.frame }
                    when {
                        marker?.matchesName(SHUTTER_AUTO_CLOSE) == true -> {
                            binding.tvShutter.animate().apply {
                                duration = 120
                                alpha(1F)
                            }
                            lottieView.setMinAndMaxFrame(
                                SHUTTER_AUTO_CLOSE,
                                SHUTTER_AUTO_OPEN,
                                false
                            )
                            lottieView.playAnimation()
                        }

                        marker?.matchesName(SHUTTER_AUTO_OPEN) == true -> {
                            binding.tvShutter.animate().apply {
                                duration = 120
                                alpha(0F)
                            }
                            lottieView.removeAllAnimatorListeners()
                            lottieView.setMinAndMaxFrame(
                                SHUTTER_AUTO_OPEN,
                                COACH_MARK_APPEAR,
                                false
                            )
                            lottieView.playAnimation()
                            setupClickListeners()
                        }
                    }
                },
                autoPlay = true
            )
        }
    }

    private fun setupClickListeners() {
        onClickAction?.invoke()
        var isOpenToClose = true
        binding.lottieView.apply {
            setOnClickListener {
                if (isOpenToClose) {
                    setMinAndMaxFrame(
                        COACH_MARK_APPEAR,
                        SHUTTER_MANUAL_CLOSE,
                        false
                    )
                    binding.tvShutter.animate().apply {
                        duration = 120
                        alpha(1F)
                    }
                } else {
                    setMinAndMaxFrame(
                        SHUTTER_MANUAL_CLOSE,
                        SHUTTER_MANUAL_OPEN,
                        false
                    )
                    binding.tvShutter.animate().apply {
                        duration = 120
                        alpha(0F)
                    }
                }

                isOpenToClose = !isOpenToClose
                playAnimation()
            }

            setOnTouchListener { _, event ->
                when (event?.action) {
                    MotionEvent.ACTION_UP -> {
                        scaleView(SCALE_75, SCALE_100, OVER_SHOOT)
                        binding.tvShutter.scaleView(SCALE_75, SCALE_100, OVER_SHOOT)
                        performClick()
                    }

                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_MOVE -> {
                        scaleView(SCALE_75, SCALE_100, OVER_SHOOT)
                        binding.tvShutter.scaleView(SCALE_75, SCALE_100, OVER_SHOOT)
                    }

                    MotionEvent.ACTION_DOWN -> {
                        scaleView(SCALE_100, SCALE_75, EASE_IN_OUT)
                        binding.tvShutter.scaleView(SCALE_100, SCALE_75, EASE_IN_OUT)
                    }
                }

                return@setOnTouchListener true
            }
        }
    }

    private fun downloadImages(data: MedalHeaderData, onSuccess: (Map<String, Bitmap?>) -> Unit, onFailure: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val map = mutableMapOf<String, Bitmap?>()

                val image0 = async { context.downloadImage(data.frameMaskingImageUrl) }
                val image1 = async { context.downloadImage(data.shimmerImageUrl) }
                val image2 = async { context.downloadImage(data.frameImageUrl) }
                val image3 = async { context.downloadImage(data.shutterMaskingImageUrl) }
                val image4 = async { context.downloadImage(data.shutterUrl) }
                val image5 = async { context.downloadImage(data.medalUrl) }

                map["image_0"] = image0.await()
                map["image_1"] = image1.await()
                map["image_2"] = image2.await()
                map["image_3"] = image3.await()
                map["image_4"] = image4.await()
                map["image_5"] = image5.await()
                withContext(Dispatchers.Main) {
                    onSuccess(map)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure()
                }
            }
        }
    }

    private fun View.scaleView(from: Float, to: Float, interpolatorType: Int) {
        if (to == scaleX && to == scaleY) {
            return
        }
        animateView(
            arrayOf(
                propertyValueHolder(View.SCALE_X, from, to),
                propertyValueHolder(View.SCALE_Y, from, to)
            ),
            DURATION,
            interpolatorType
        )
    }
}
