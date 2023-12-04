package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.databinding.DiscoveryStockBarLayoutBinding
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.utils.resources.isDarkMode

class DiscoveryStockBar : FrameLayout {

    private var binding: DiscoveryStockBarLayoutBinding? = null
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        binding = DiscoveryStockBarLayoutBinding.inflate(LayoutInflater.from(context), this, true)

        setIcon()
        setProgressBarColor()
    }

    fun setValue(value: Double?) {
        binding?.root?.isVisible = value != null

        binding?.stockProgressBar?.setValue(value?.toInt().orZero())
    }

    fun setLabel(label: String?) {
        binding?.label?.apply {
            text = label
            textSize = 8f
        }
    }

    private fun setIcon() {
        val iconResId = if (context.isDarkMode()) {
            R.drawable.ic_stock_dark_mode
        } else {
            R.drawable.ic_stock_light_mode
        }

        binding?.stockIcon?.setImageResource(iconResId)
    }

    private fun setProgressBarColor() {
        val firstColor = ContextCompat.getColor(context, R.color.discovery2_dms_ph_stock_bar_start)
        val secondColor = ContextCompat.getColor(context, R.color.discovery2_dms_ph_stock_bar_end)
        binding?.stockProgressBar?.progressBarColor = intArrayOf(firstColor, secondColor)
    }
}
