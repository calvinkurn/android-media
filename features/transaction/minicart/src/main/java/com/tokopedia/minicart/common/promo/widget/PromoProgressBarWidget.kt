package com.tokopedia.minicart.common.promo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.minicart.databinding.WidgetPromoProgressBarBinding
import com.tokopedia.unifycomponents.BaseCustomView

class PromoProgressBarWidget: BaseCustomView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    private var binding: WidgetPromoProgressBarBinding =
        WidgetPromoProgressBarBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.promoProgressBar.onValueChangeListener = { _, current ->
            if (current >= 100) {
                binding.promoProgressBar.gone()
            } else {
                binding.promoProgressBar.visible()
            }
        }
    }

    fun updateProgress(progressValue: Int) {
        binding.promoProgressBar.setValue(progressValue)
        binding.promoIcon.visible()
    }

    fun updatePromoText(promoText: String) {
        binding.promoText.text = promoText
        if (promoText.isEmpty()) {
            binding.promoText.gone()
        } else {
            binding.promoText.visible()
        }
    }
}