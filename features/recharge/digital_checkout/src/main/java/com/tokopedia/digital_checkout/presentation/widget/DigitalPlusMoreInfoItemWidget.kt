package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.digital_checkout.databinding.ItemDigitalPlusSubscriptionMoreInfoBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class DigitalPlusMoreInfoItemWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = ItemDigitalPlusSubscriptionMoreInfoBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun setContent(number: Int, title: String, desc: String) {
        binding.run {
            digitalTgMoreInfoNumber.text = number.toString()
            digitalTgMoreInfoTitle.text = title
            digitalTgMoreInfoDesc.text = desc
        }
    }
}
