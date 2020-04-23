package com.tokopedia.vouchercreation.create.view.customview

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.LayoutRes
import com.tokopedia.vouchercreation.R

class NextStepButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
        @LayoutRes layoutResource: Int = R.layout.mvc_next_step_button
) : VoucherCustomView(context, attrs, defStyleAttr, defStyleRes, layoutResource) {

    override fun setupAttributes() {}

    override fun setupView() {}

}