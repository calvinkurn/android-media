package com.tokopedia.vouchercreation.shop.create.view.customview

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.view.VoucherCustomView
import kotlinx.android.synthetic.main.mvc_double_line_info.view.*

class DoubleLineInfoView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
        @LayoutRes layoutResource: Int = R.layout.mvc_double_line_info,
        styleableResource: IntArray = R.styleable.DoubleLineInfo)
    : VoucherCustomView(context, attrs, defStyleAttr, defStyleRes, layoutResource, styleableResource) {

    private var infoTitleString: String = ""
    var infoValueString: String = ""
        set(value) {
            field = value
            view?.infoValue?.text = value
        }

    override fun setupAttributes() {
        attributes?.run {
            infoValueString = getString(R.styleable.DoubleLineInfo_value).toBlankOrString()
            infoTitleString = getString(R.styleable.DoubleLineInfo_infoTitle).toBlankOrString()
        }
    }

    override fun setupView() {
        view?.run {
            infoTitle?.text = infoTitleString
            infoValue?.text = infoValueString
        }
    }
}