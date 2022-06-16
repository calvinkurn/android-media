package com.tokopedia.buyerorder.detail.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.buyerorder.databinding.LayoutHorizontalTextViewBinding
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.BaseCustomView

/**
 * created by @bayazidnasir on 22/3/2022
 */

class HorizontalCoupleTextView constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : BaseCustomView(context, attrs, defStyleAttr){

    private var binding: LayoutHorizontalTextViewBinding =
        LayoutHorizontalTextViewBinding.inflate(LayoutInflater.from(context), this, true)

    private var isShowSeparator: Boolean = false
        set(value){
            field = value
            binding.lineSeparator.showWithCondition(value)
        }


    fun setLabel(label: String){
        binding.leftText.text = label
    }

    fun setValue(value: String){
        binding.rightText.text = value
    }

    fun isShowSeparator(isShowSeparator: Boolean){
        this.isShowSeparator = isShowSeparator
    }

    fun setLabelColor(color: Int){
        binding.leftText.setTextColor(color)
    }

    fun setValueColor(color: Int){
        binding.rightText.setTextColor(color)
    }
}