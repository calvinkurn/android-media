package com.tokopedia.vouchercreation.shop.create.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.view.VoucherCustomView
import com.tokopedia.vouchercreation.databinding.MvcDoubleLineInfoBinding

class DoubleLineInfoView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
        styleableResource: IntArray = R.styleable.DoubleLineInfo)
: VoucherCustomView(context, attrs, defStyleAttr, defStyleRes, styleableResource) {

    private var binding: MvcDoubleLineInfoBinding = MvcDoubleLineInfoBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setupLayout(binding)
    }

    private var infoTitleString: String = ""
    var infoValueString: String = ""
        set(value) {
            field = value
            binding.infoValue.text = value
        }

    override fun setupAttributes() {
        attributes?.run {
            infoValueString = getString(R.styleable.DoubleLineInfo_value).toBlankOrString()
            infoTitleString = getString(R.styleable.DoubleLineInfo_infoTitle).toBlankOrString()
        }
    }

    override fun setupView() {
        binding.run {
            infoTitle.text = infoTitleString
            infoValue.text = infoValueString
        }
    }
}