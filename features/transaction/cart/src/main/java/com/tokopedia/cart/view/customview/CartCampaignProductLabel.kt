package com.tokopedia.cart.view.customview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.cart.databinding.LayoutCartCampaignProductLabelBinding
import com.tokopedia.cart.view.uimodel.HexColor
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage

class CartCampaignProductLabel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val LABEL_IMAGE_MAX_WIDTH_DP = 128
        private const val LABEL_IMAGE_MAX_HEIGHT_DP = 12
        private const val LABEL_CORNER_RADIUS_DP = 4
        private const val LABEL_STROKE_WIDTH_DP = 1

        private const val COUNTDOWN_TIMER_FORMAT = "%s : %s : %s"
        private const val COUNTDOWN_TIMER_INTERVAL_MS = 1000L
    }

    private val binding: LayoutCartCampaignProductLabelBinding =
        LayoutCartCampaignProductLabelBinding
            .inflate(LayoutInflater.from(context), this, true)

    private var timer: CountDownTimer? = null

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearTimer()
    }

    fun showImageLabel(
        logoUrl: String,
        backgroundStartColor: HexColor,
        backgroundEndColor: HexColor
    ) {
        with(binding) {
            iuCampaignIcon.gone()
            iuCampaignLogo.loadImage(
                url = logoUrl,
                properties = {
                    overrideSize(
                        Resize(
                            context.dpToPx(LABEL_IMAGE_MAX_WIDTH_DP).toInt(),
                            context.dpToPx(LABEL_IMAGE_MAX_HEIGHT_DP).toInt()
                        )
                    )
                    fitCenter()
                }
            )
            iuCampaignLogo.visible()
            tpgCampaignLabel.gone()
            if (backgroundStartColor.hexCode.isNotBlank() && backgroundEndColor.hexCode.isNotBlank()) {
                val tickerBackgroundDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    orientation = GradientDrawable.Orientation.LEFT_RIGHT
                    val startColor = Color.parseColor(backgroundStartColor.hexCode)
                    val endColor = Color.parseColor(backgroundEndColor.hexCode)
                    colors = intArrayOf(startColor, endColor)
                    setCornerRadius(context.dpToPx(LABEL_CORNER_RADIUS_DP))
                }
                clProductLabel.background = tickerBackgroundDrawable
            }
            clProductLabel.visible()
            tpgProductLabelCountdown.gone()
            container.visible()
        }
    }

    fun showTextLabel(
        text: String,
        textColor: HexColor,
        backgroundStartColor: HexColor,
        backgroundEndColor: HexColor
    ) {
        with(binding) {
            iuCampaignIcon.gone()
            iuCampaignLogo.gone()
            tpgCampaignLabel.text = text
            tpgCampaignLabel.setTextColor(Color.parseColor(textColor.hexCode))
            tpgCampaignLabel.visible()
            if (backgroundStartColor.hexCode.isNotBlank() && backgroundEndColor.hexCode.isNotBlank()) {
                val tickerBackgroundDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    orientation = GradientDrawable.Orientation.LEFT_RIGHT
                    val startColor = Color.parseColor(backgroundStartColor.hexCode)
                    val endColor = Color.parseColor(backgroundEndColor.hexCode)
                    colors = intArrayOf(startColor, endColor)
                    setCornerRadius(context.dpToPx(LABEL_CORNER_RADIUS_DP))
                }
                clProductLabel.background = tickerBackgroundDrawable
            }
            clProductLabel.visible()
            tpgProductLabelCountdown.gone()
            container.visible()
        }
    }

    fun showTimedLabel(
        remainingTimeMillis: Long = 0L,
        iconUrl: String,
        backgroundColor: HexColor
    ) {
        with(binding) {
            if (iconUrl.isNotBlank()) {
                iuCampaignIcon.loadImage(url = iconUrl)
                iuCampaignIcon.visible()
            } else {
                iuCampaignIcon.gone()
            }
            iuCampaignLogo.gone()
            tpgCampaignLabel.gone()
            if (backgroundColor.hexCode.isNotBlank()) {
                val tickerBackgroundDrawable = GradientDrawable().apply {
                    orientation = GradientDrawable.Orientation.LEFT_RIGHT
                    val backgroundColor = Color.parseColor(backgroundColor.hexCode)
                    colors = intArrayOf(backgroundColor, backgroundColor)
                    val cornerRadius = context.dpToPx(LABEL_CORNER_RADIUS_DP)
                    shape = GradientDrawable.RECTANGLE
                    cornerRadii = floatArrayOf(
                        cornerRadius,
                        cornerRadius,
                        0f,
                        0f,
                        0f,
                        0f,
                        cornerRadius,
                        cornerRadius
                    )
                }
                clProductLabel.background = tickerBackgroundDrawable
            }
            clProductLabel.visible()
            if (backgroundColor.hexCode.isNotBlank()) {
                val countdownBackgroundDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    val cornerRadius = context.dpToPx(LABEL_CORNER_RADIUS_DP)
                    cornerRadii = floatArrayOf(
                        0f,
                        0f,
                        cornerRadius,
                        cornerRadius,
                        cornerRadius,
                        cornerRadius,
                        0f,
                        0f
                    )
                    setStroke(
                        context.dpToPx(LABEL_STROKE_WIDTH_DP).toInt(),
                        Color.parseColor(backgroundColor.hexCode)
                    )
                }
                tpgProductLabelCountdown.background = countdownBackgroundDrawable
            }
            timer?.cancel()
            timer = object : CountDownTimer(remainingTimeMillis, COUNTDOWN_TIMER_INTERVAL_MS) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = (millisUntilFinished / 1000) % 60
                    val minutes = (millisUntilFinished / (1000 * 60) % 60)
                    val hours = (millisUntilFinished / (1000 * 60 * 60))

                    val hourText = "${if (hours < 10) 0.toString() else ""}$hours"
                    val minuteText = "${if (minutes < 10) 0.toString() else ""}$minutes"
                    val secondText = "${if (seconds < 10) 0.toString() else ""}$seconds"

                    tpgProductLabelCountdown.text =
                        COUNTDOWN_TIMER_FORMAT.format(hourText, minuteText, secondText)
                }

                override fun onFinish() {
                    // no-op
                }
            }
            tpgProductLabelCountdown.visible()
            timer?.start()
            iuCampaignLogo.gone()
            container.visible()
        }
    }

    fun hideTicker() {
        binding.container.gone()
    }

    private fun clearTimer() {
        timer?.cancel()
        timer = null
    }
}
