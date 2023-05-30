package com.tokopedia.scp_rewards.widget.medalHeader

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.scp_rewards.common.utils.COACH_MARK_APPEAR
import com.tokopedia.scp_rewards.common.utils.SHUTTER_AUTO_CLOSE
import com.tokopedia.scp_rewards.common.utils.SHUTTER_AUTO_OPEN
import com.tokopedia.scp_rewards.common.utils.SHUTTER_MANUAL_CLOSE
import com.tokopedia.scp_rewards.common.utils.SHUTTER_MANUAL_OPEN
import com.tokopedia.scp_rewards.common.utils.downloadImage
import com.tokopedia.scp_rewards.common.utils.loadLottieFromUrl
import com.tokopedia.scp_rewards.databinding.WidgetMedalLottieAnimationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MedalLottieAnimation(private val context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding =
        WidgetMedalLottieAnimationBinding.inflate(LayoutInflater.from(context), this)


    private sealed class LottieMarker(val markerString: String) {
        object ShutterAutoClose : LottieMarker(SHUTTER_AUTO_CLOSE)
        object ShutterAutoOpen : LottieMarker(SHUTTER_AUTO_OPEN)
        object CoachMarkAppear : LottieMarker(COACH_MARK_APPEAR)
        object ShutterManualClose : LottieMarker(SHUTTER_MANUAL_CLOSE)
        object ShutterManualOpen : LottieMarker(SHUTTER_MANUAL_OPEN)
    }

    fun loadLottie(data: MedalHeader) {
        downloadImages(data, {
            loadMedalBadge(data, it)
        }, {
            // show placeholder
        })
    }

    private fun loadMedalBadge(data: MedalHeader, map: Map<String, Bitmap?>) {
        binding.apply {
            lottieView.loadLottieFromUrl(
                url = data.lottieUrl,
                onLottieLoaded = {
                    lottieView.imageAssetsFolder = "images"
                    map.forEach { (key, bitmap) -> lottieView.updateBitmap(key, bitmap) }
                    lottieView.setMaxFrame(LottieMarker.ShutterAutoClose.markerString)
                },
                onError = {},
                onLottieEnded = {
                    val markerName =
                        lottieView.composition?.markers?.find { it.startFrame.toInt() == lottieView.frame }?.name
                    when (markerName) {
                        SHUTTER_AUTO_CLOSE -> {
                            binding.tvShutter.text = "Earned by Clara\non 17 March 2023"
                            binding.tvShutter.animate().apply {
                                duration = 120
                                alpha(1F)
                            }
                            lottieView.setMinAndMaxFrame(
                                LottieMarker.ShutterAutoClose.markerString,
                                LottieMarker.ShutterAutoOpen.markerString,
                                false
                            )
                            lottieView.playAnimation()
                        }

                        SHUTTER_AUTO_OPEN -> {
                            binding.tvShutter.animate().apply {
                                duration = 120
                                alpha(0F)
                            }
                            lottieView.removeAllAnimatorListeners()
                            lottieView.setMinAndMaxFrame(
                                LottieMarker.ShutterAutoOpen.markerString,
                                LottieMarker.CoachMarkAppear.markerString,
                                false
                            )
                            lottieView.playAnimation()
                        }
                    }
                },
                animationUpdateListener = {
                },
                autoPlay = true
            )
        }
        setupClickListeners()
    }

    private fun setupClickListeners() {
        var isOpenToClose = true
        binding.lottieView.apply {
            setOnClickListener {
                if (isOpenToClose) {
                    setMinAndMaxFrame(
                        LottieMarker.CoachMarkAppear.markerString,
                        LottieMarker.ShutterManualClose.markerString,
                        false
                    )
                    binding.tvShutter.animate().apply {
                        duration = 120
                        alpha(1F)
                    }
                } else {
                    setMinAndMaxFrame(
                        LottieMarker.ShutterManualClose.markerString,
                        LottieMarker.ShutterManualOpen.markerString,
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
        }
    }

    private fun downloadImages(
        data: MedalHeader,
        onSuccess: (Map<String, Bitmap?>) -> Unit,
        onFailure: () -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val map = mutableMapOf<String, Bitmap?>()

                val image0 = async { context.downloadImage(data.frameMaskUrl) }
                val image1 = async { context.downloadImage(data.shimmerUrl) }
                val image2 = async { context.downloadImage(data.frameUrl) }
                val image3 = async { context.downloadImage(data.maskingShapeUrl) }
                val image4 = async { context.downloadImage(data.shutterUrl) }
                val image5 = async { context.downloadImage(data.medalUrl) }

                map["image_0"] = image0.await()
                map["image_1"] = image1.await()
                map["image_2"] = image2.await()
//                map["image_3"] = image3.await()
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


}
