package com.tokopedia.tokofood.common.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.tokofood.databinding.LayoutTokofoodPromoRibbonBinding
import com.tokopedia.unifycomponents.BaseCustomView

class TokofoodPromoRibbonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: LayoutTokofoodPromoRibbonBinding? = null
    private var currentRibbonText: String = ""

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (binding == null) {
            initBinding()
            setRibbonText(currentRibbonText)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding = null
    }

    fun setRibbonText(ribbonText: String) {
        binding?.tvTokofoodPromoName?.text = ribbonText
        currentRibbonText = ribbonText
    }

    private fun initBinding() {
        binding = LayoutTokofoodPromoRibbonBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

}
