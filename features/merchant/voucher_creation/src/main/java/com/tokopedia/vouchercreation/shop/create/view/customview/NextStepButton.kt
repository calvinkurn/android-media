package com.tokopedia.vouchercreation.shop.create.view.customview

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.LayoutRes
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.view.VoucherCustomView
import kotlinx.android.synthetic.main.mvc_next_step_button.view.*

class NextStepButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
        @LayoutRes layoutResource: Int = R.layout.mvc_next_step_button
) : VoucherCustomView(context, attrs, defStyleAttr, defStyleRes, layoutResource) {

    override fun setupAttributes() {}

    override fun setupView() {}

    var isLoading = nextButton?.isLoading ?: false
        set(value) {
            field = value
            nextButton?.isLoading = value
        }

    var isButtonEnabled = nextButton?.isEnabled ?: false
        set(value) {
            field = value
            nextButton?.isEnabled = value
            isEnabled = value
        }

}