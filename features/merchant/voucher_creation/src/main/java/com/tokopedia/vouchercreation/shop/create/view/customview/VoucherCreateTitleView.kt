package com.tokopedia.vouchercreation.shop.create.view.customview

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.view.VoucherCustomView
import kotlinx.android.synthetic.main.mvc_create_widget_title.view.*

class VoucherCreateTitleView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
        @LayoutRes layoutResource: Int = R.layout.mvc_create_widget_title,
        styleableResource: IntArray = R.styleable.VoucherCreateWidgetTitle
) : VoucherCustomView(context, attrs, defStyleAttr, defStyleRes, layoutResource, styleableResource) {

    private var title: String = ""

    override fun setupAttributes() {
        attributes?.run {
            title = getString(R.styleable.VoucherCreateWidgetTitle_title).toBlankOrString()
        }
    }

    override fun setupView() {
        view?.run {
            widgetTitle?.text = title
        }
    }
}