package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.digital_checkout.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import org.jetbrains.annotations.NotNull

class DigitalCheckoutSimpleWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var tvLeftContent: Typography
    private var tvRightContent: Typography

    init {
        LayoutInflater.from(context).inflate(R.layout.item_digital_checkout_simple_view, this, true)
        tvLeftContent = findViewById(R.id.tv_digital_checkout_left_content)
        tvRightContent = findViewById(R.id.tv_digital_checkout_right_content)
    }

    fun setContent(leftContent: CharSequence, rightContent: CharSequence) {
        tvLeftContent.text = leftContent
        tvRightContent.text = rightContent
    }
}
