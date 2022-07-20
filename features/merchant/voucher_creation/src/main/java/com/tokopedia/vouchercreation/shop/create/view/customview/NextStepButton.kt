package com.tokopedia.vouchercreation.shop.create.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.vouchercreation.common.view.VoucherCustomView
import com.tokopedia.vouchercreation.databinding.MvcNextStepButtonBinding

class NextStepButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : VoucherCustomView(context, attrs, defStyleAttr, defStyleRes) {

    private var binding: MvcNextStepButtonBinding = MvcNextStepButtonBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setupLayout(binding)
    }

    override fun setupAttributes() {}

    override fun setupView() {}

    var isLoading = binding.nextButton.isLoading ?: false
        set(value) {
            field = value
            binding.nextButton.isLoading = value
        }

    var isButtonEnabled = binding.nextButton.isEnabled ?: false
        set(value) {
            field = value
            binding.nextButton.isEnabled = value
            isEnabled = value
        }

}