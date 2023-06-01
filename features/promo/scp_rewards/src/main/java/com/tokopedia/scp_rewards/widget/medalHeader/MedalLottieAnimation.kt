package com.tokopedia.scp_rewards.widget.medalHeader

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.scp_rewards.common.utils.downloadImage
import com.tokopedia.scp_rewards.common.utils.loadLottieFromUrl
import com.tokopedia.scp_rewards.databinding.WidgetMedalLottieAnimationBinding
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
private const val IMAGES_FOLDER = "images"

class MedalLottieAnimation(private val context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding =
        WidgetMedalLottieAnimationBinding.inflate(LayoutInflater.from(context), this)

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
                    lottieView.imageAssetsFolder = IMAGES_FOLDER
                    map.forEach { (key, bitmap) -> lottieView.updateBitmap(key, bitmap) }
                    val textAutoShow = lottieView.composition?.markers
                        ?.map { it.name }
                        ?.containsAll(
                            listOf(
                                SHUTTER_AUTO_CLOSE,
                                SHUTTER_AUTO_OPEN,
                                SHUTTER_MANUAL_OPEN,
                                SHUTTER_MANUAL_CLOSE
                            )
                        )
                    if (textAutoShow == true) {
                        lottieView.setMaxFrame(SHUTTER_AUTO_CLOSE)
                    }
                },
                onError = {
                    // show placeholder
                },
                onLottieEnded = {
                    val markerName =
                        lottieView.composition?.markers?.find { it.startFrame.toInt() == lottieView.frame }?.name
                    when (markerName) {
                        SHUTTER_AUTO_CLOSE -> {
                            binding.tvShutter.text = data.shutterText
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

                        SHUTTER_AUTO_OPEN -> {
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
//                val image3 = async { context.downloadImage(data.maskingShapeUrl) }
                val image4 = async { context.downloadImage(data.shutterUrl) }
                val image5 = async { context.downloadImage(data.medalUrl) }

                map["image_0"] = image0.await()
                map["image_1"] = image1.await()
                map["image_2"] = image2.await()
//                The lottie doesn't work properly when masking shape image is download from server
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
