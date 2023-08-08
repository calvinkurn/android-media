package com.tokopedia.play.view.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.tokopedia.play.databinding.PlayMotionCardRibbonBinding
import com.tokopedia.play_common.view.setGradientAnimBackground

/**
 * @author by astidhiyaa on 07/08/23
 */
class PlayLabelAnimation : MotionLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = PlayMotionCardRibbonBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    var rankFmt: String = ""
        set(value) {
            field = value
            binding.playTvRibbon.text = value
        }

    fun setRibbonColors(colors: List<String>) {
        if (colors.isEmpty()) return

        try {
            binding.playTvRibbon.setGradientAnimBackground(colors.takeLast(COLOR_ADD))
            binding.ivTailRibbon.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    Color.parseColor(colors.first()),
                    BlendModeCompat.SRC_ATOP
                )
            binding.ivBackRibbon.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    Color.parseColor(colors.getOrElse(1) { colors.first() }),
                    BlendModeCompat.SRC_ATOP
                )
        } catch (expected: Exception) {
            false
        }
    }

    fun startAnimation() {
        binding.root.transitionToEnd()
    }

    companion object {
        private const val COLOR_ADD = 2
    }
}
