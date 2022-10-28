package com.tokopedia.vouchercreation.shop.create.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.view.VoucherCustomView
import com.tokopedia.vouchercreation.databinding.MvcCreateWidgetTitleBinding

class VoucherCreateTitleView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
        styleableResource: IntArray = R.styleable.VoucherCreateWidgetTitle
) : VoucherCustomView(context, attrs, defStyleAttr, defStyleRes, styleableResource) {

    private var binding: MvcCreateWidgetTitleBinding = MvcCreateWidgetTitleBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setupLayout(binding)
    }

    private var title: String = ""

    override fun setupAttributes() {
        attributes?.run {
            title = getString(R.styleable.VoucherCreateWidgetTitle_title).toBlankOrString()
        }
    }

    override fun setupView() {
        binding.widgetTitle.text = title
    }
}