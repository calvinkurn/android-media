package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.digital_checkout.databinding.ItemDigitalCheckoutSimpleViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class DigitalCheckoutSimpleWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = ItemDigitalCheckoutSimpleViewBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    fun setContent(leftContent: CharSequence, rightContent: CharSequence) {
        with(binding) {
            tvDigitalCheckoutLeftContent.text = leftContent
            tvDigitalCheckoutRightContent.text = rightContent
        }
    }
}
