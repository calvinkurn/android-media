package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import com.tokopedia.discovery2.databinding.DiscoveryStockBarLayoutBinding
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero

class DiscoveryStockBar : FrameLayout {

    private var binding : DiscoveryStockBarLayoutBinding? = null
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        binding = DiscoveryStockBarLayoutBinding.inflate(LayoutInflater.from(context), this, true )
    }

    fun setValue(value: Double?) {
        binding?.root?.isVisible = value != null

        binding?.stockProgressBar?.setValue(value?.toInt().orZero())
    }

    fun setLabel(label: String?) {
        binding?.label?.text = label
    }
}
