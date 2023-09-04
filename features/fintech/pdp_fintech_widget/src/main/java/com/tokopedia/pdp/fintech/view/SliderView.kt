package com.tokopedia.pdp.fintech.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.pdp_fintech.databinding.SliderViewLayoutBinding
import com.tokopedia.utils.view.binding.viewBinding

class SliderView: FrameLayout {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var binding: SliderViewLayoutBinding? = null

    init {
        binding = SliderViewLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setItems(firstItem: View, secondItem: View) {
        binding?.firstItem?.addView(firstItem)
        binding?.secondItem?.addView(secondItem)
    }
}
