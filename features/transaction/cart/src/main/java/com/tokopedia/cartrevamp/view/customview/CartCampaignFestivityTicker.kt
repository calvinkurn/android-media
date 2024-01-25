package com.tokopedia.cartrevamp.view.customview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.cart.databinding.LayoutCartCampaignFestivityTickerBinding
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage

class CartCampaignFestivityTicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutCartCampaignFestivityTickerBinding =
        LayoutCartCampaignFestivityTickerBinding
            .inflate(LayoutInflater.from(context), this, true)

    private var timer: CountDownTimer? = null
    private var onCountdownTick: ((remainingTimeMs: Long) -> Unit)? = null
    private var onCountdownFinish: (() -> Unit)? = null

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearTimer()
    }

    fun showLogoTicker(
        logoUrl: String,
        backgroundColorStart: HexColor,
        backgroundColorEnd: HexColor
    ) {
        with(binding) {
            iuCampaignLogo.loadImage(
                url = logoUrl,
                properties = {
                    overrideSize(Resize(context.dpToPx(128).toInt(), context.dpToPx(12).toInt()))
                    fitCenter()
                }
            )
            iuCampaignLogo.visible()
            val tickerBackgroundDrawable = GradientDrawable().apply {
                orientation = GradientDrawable.Orientation.LEFT_RIGHT
                val startColor = backgroundColorStart.parsedColor
                val endColor = backgroundColorEnd.parsedColor
                colors = intArrayOf(startColor, endColor)
                val cornerRadius = context.dpToPx(8)
                shape = GradientDrawable.RECTANGLE
                cornerRadii = floatArrayOf(
                    0f, 0f, cornerRadius, cornerRadius,
                    cornerRadius, cornerRadius, 0f, 0f
                )
            }
            clCampaignTicker.background = tickerBackgroundDrawable
            clCampaignTicker.visible()
            iuCampaignIcon.gone()
            tpgCampaignLabel.gone()
            tpgCampaignCountdown.gone()
        }
    }

    fun showTextTicker(
        text: String,
        backgroundColor: HexColor,
        iconUrl: String? = null,
        textColor: HexColor? = null,
        remainingTimeMillis: Long = 0L,
    ) {
        val showCountdown = remainingTimeMillis > 0
        with(binding) {
            if (!iconUrl.isNullOrBlank()) {
                iuCampaignIcon.loadImage(url = iconUrl)
                iuCampaignIcon.visible()
            } else {
                iuCampaignIcon.gone()
            }
            tpgCampaignLabel.text = text
            if (textColor != null) {
                tpgCampaignLabel.setTextColor(textColor.parsedColor)
            }
            tpgCampaignLabel.visible()
            val tickerBackgroundDrawable = GradientDrawable().apply {
                orientation = GradientDrawable.Orientation.LEFT_RIGHT
                val startColor = backgroundColor.parsedColor
                val endColor = backgroundColor.parsedColor
                colors = intArrayOf(startColor, endColor)
                val cornerRadius = context.dpToPx(8)
                shape = GradientDrawable.RECTANGLE
                if (showCountdown) {
                    cornerRadii = floatArrayOf(
                        cornerRadius, cornerRadius, 0f, 0f,
                        0f, 0f, cornerRadius, cornerRadius
                    )
                } else {
                    cornerRadii = floatArrayOf(
                        0f, 0f, cornerRadius, cornerRadius,
                        cornerRadius, cornerRadius, 0f, 0f
                    )
                }
            }
            clCampaignTicker.background = tickerBackgroundDrawable
            clCampaignTicker.visible()
            if (showCountdown) {
                val countdownBackgroundDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    val cornerRadius = context.dpToPx(8)
                    cornerRadii = floatArrayOf(
                        0f, 0f, cornerRadius, cornerRadius,
                        cornerRadius, cornerRadius, 0f, 0f
                    )
                    setStroke(context.dpToPx(1).toInt(), backgroundColor.parsedColor)
                }
                tpgCampaignCountdown.background = countdownBackgroundDrawable
                timer?.cancel()
                timer = object : CountDownTimer(remainingTimeMillis, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val seconds = (millisUntilFinished / 1000) % 60
                        val minutes = (millisUntilFinished / (1000 * 60) % 60)
                        val hours = (millisUntilFinished / (1000 * 60 * 60))

                        val hourText = "${if (hours < 10) 0.toString() else ""}${hours}"
                        val minuteText = "${if (minutes < 10) 0.toString() else ""}${minutes}"
                        val secondText = "${if (seconds < 10) 0.toString() else ""}${seconds}"

                        tpgCampaignCountdown.text = "$hourText : $minuteText : $secondText"

                        onCountdownTick?.invoke(millisUntilFinished)
                    }

                    override fun onFinish() {
                        onCountdownFinish?.invoke()
                    }
                }
                tpgCampaignCountdown.visible()
                timer?.start()
            } else {
                tpgCampaignCountdown.gone()
            }
            iuCampaignLogo.gone()
        }
    }

    fun hideTicker() {
        binding.clCampaignTicker.gone()
        binding.tpgCampaignCountdown.gone()
    }

    private fun clearTimer() {
        timer?.cancel()
        timer = null
        onCountdownTick = null
        onCountdownFinish = null
    }
}

@JvmInline
value class HexColor(private val hexCode: String) {
    val parsedColor: Int
        get() = Color.parseColor(hexCode)
}