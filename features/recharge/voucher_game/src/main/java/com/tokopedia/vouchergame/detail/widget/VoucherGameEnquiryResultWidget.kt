package com.tokopedia.vouchergame.detail.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.databinding.ViewEnquiryResultBinding
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 20/08/19.
 */
open class VoucherGameEnquiryResultWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    BaseCustomView(context, attrs, defStyleAttr) {

    private var binding = ViewEnquiryResultBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    fun setEnquiryResultField(title: String) {
        binding.enquiryResultField.text = title
    }

    fun setEnquiryResultValue(value: String) {
        binding.enquiryResultValue.text = value
    }

    fun setEnquiryResult(field: String, value: String) {
        setEnquiryResultField(field)
        setEnquiryResultValue(value)
    }

    open fun getLayout(): Int {
        return R.layout.view_enquiry_result
    }
}
