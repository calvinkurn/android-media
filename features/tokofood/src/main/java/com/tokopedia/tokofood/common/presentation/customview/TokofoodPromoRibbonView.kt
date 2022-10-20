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

    init {
        binding = LayoutTokofoodPromoRibbonBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun setRibbonText(ribbonText: String) {
        binding?.tvTokofoodPromoName?.text = ribbonText
    }

}
