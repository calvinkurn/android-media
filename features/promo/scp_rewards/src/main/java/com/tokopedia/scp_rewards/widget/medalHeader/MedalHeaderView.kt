package com.tokopedia.scp_rewards.widget.medalHeader

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.FontAssetDelegate
import com.airbnb.lottie.TextDelegate
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.scp_rewards.common.utils.COACH_MARK_APPEAR
import com.tokopedia.scp_rewards.common.utils.FONT_PATH
import com.tokopedia.scp_rewards.common.utils.LAYER_DYNAMIC_TEXT
import com.tokopedia.scp_rewards.common.utils.MEDAL_AUTOPLAY
import com.tokopedia.scp_rewards.common.utils.MEDAL_SHUTTER_CLOSE
import com.tokopedia.scp_rewards.common.utils.MEDAL_SHUTTER_OPEN
import com.tokopedia.scp_rewards.common.utils.downloadImage
import com.tokopedia.scp_rewards.common.utils.loadImage
import com.tokopedia.scp_rewards.common.utils.loadLottieFromUrl
import com.tokopedia.scp_rewards.databinding.WidgetMedalHeaderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MedalHeaderView(private val context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private sealed class LottieMarker(val markerString: String) {
        object MedalAutoPlay : LottieMarker(MEDAL_AUTOPLAY)
        object CoachMarkAppear : LottieMarker(COACH_MARK_APPEAR)
        object MedalShutterClose : LottieMarker(MEDAL_SHUTTER_CLOSE)
        object MedalShutterOpen : LottieMarker(MEDAL_SHUTTER_OPEN)
    }

    private val binding = WidgetMedalHeaderBinding.inflate(LayoutInflater.from(context), this)

    fun bindData(data: MedalHeader) {
        with(binding) {
            loadSparks(data.lottieSparklesUrl)
            downloadImages(data, {
                loadMedalBadge(data, it)
            }, {
                // show placeholder
            })

            loadBackground(data.background, data.backgroundColor)
        }
    }

    private fun WidgetMedalHeaderBinding.loadSparks(lottieSparklesUrl: String?) {
        lottieViewSparks.loadLottieFromUrl(
            url = lottieSparklesUrl,
            autoPlay = true
        )
    }

    private fun downloadImages(data: MedalHeader, onSuccess: (Map<String, Bitmap?>) -> Unit, onFailure: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val map = mutableMapOf<String, Bitmap?>()

                val image0 = async { this@MedalHeaderView.context.downloadImage(data.maskUrl) }
                val image1 = async { this@MedalHeaderView.context.downloadImage(data.shimmerUrl) }
                val image2 = async { this@MedalHeaderView.context.downloadImage(data.frameUrl) }
                val image3 = async { this@MedalHeaderView.context.downloadImage(data.maskUrl) }
                val image4 = async { this@MedalHeaderView.context.downloadImage(data.shutterUrl) }
                val image5 = async { this@MedalHeaderView.context.downloadImage(data.medalUrl) }

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

    private fun WidgetMedalHeaderBinding.loadCoachMark(coachMarkInformation: String?) {
        CoachMark2(context).showCoachMark(
            arrayListOf(
                CoachMark2Item(
                    this.lottieView,
                    coachMarkInformation.orEmpty(),
                    String.EMPTY,
                    CoachMark2.POSITION_TOP
                )
            )
        )
    }

    private fun WidgetMedalHeaderBinding.loadMedalBadge(data: MedalHeader, map: Map<String, Bitmap?>) {
        var coachMarkTime = 0f

        var isOpenToClose = true

        lottieView.loadLottieFromUrl(
            url = data.lottieUrl,
            onLottieLoaded = {
                coachMarkTime = (it.markers.find { marker -> marker.matchesName(LottieMarker.CoachMarkAppear.markerString) }?.startFrame
                    ?: 0f) / (it.duration)
                lottieView.apply {
                    map.forEach { (key, bitmap) -> this.updateBitmap(key, bitmap) }
                    setMaxFrame(LottieMarker.CoachMarkAppear.markerString)
                    setFontAssetDelegate(object : FontAssetDelegate() {
                        override fun getFontPath(fontFamily: String?): String {
                            return FONT_PATH
                        }
                    })

                    setTextDelegate(object : TextDelegate(this) {
                        override fun getText(layerName: String?, input: String?): String {

                            return when (layerName) {
                                LAYER_DYNAMIC_TEXT -> "Hello World"
                                else -> super.getText(layerName, input)
                            }
                        }
                    })
                }
            },
            onError = {},
            onLottieEnded = {},
            animationUpdateListener = {
                if (it?.animatedFraction == coachMarkTime) {
                    loadCoachMark(data.coachMarkInformation)
                }
            },
            autoPlay = true
        )

        lottieView.setOnClickListener {
            if (isOpenToClose) {
                lottieView.setMinAndMaxFrame(
                    LottieMarker.CoachMarkAppear.markerString,
                    LottieMarker.MedalShutterClose.markerString,
                    true
                )
            } else {
                lottieView.setMinAndMaxFrame(
                    LottieMarker.MedalShutterClose.markerString,
                    LottieMarker.MedalShutterOpen.markerString,
                    true
                )
            }

            isOpenToClose = !isOpenToClose
            lottieView.playAnimation()
        }
    }

    private fun WidgetMedalHeaderBinding.loadBackground(backgroundUrl: String?, backgroundColor: String?) {
        ivBackground.loadImage(backgroundUrl)
    }
}
