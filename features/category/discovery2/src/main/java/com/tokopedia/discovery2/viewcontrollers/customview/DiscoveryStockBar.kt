package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import com.tokopedia.discovery2.databinding.DiscoveryStockBarLayoutBinding

class DiscoveryStockBar : FrameLayout {

    private var binding : DiscoveryStockBarLayoutBinding? = null
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        binding = DiscoveryStockBarLayoutBinding.inflate(LayoutInflater.from(context), this, true )
        binding?.stockProgressBar?.setValue(75)
    }
}
