package com.tokopedia.vouchergame.detail.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.vouchergame.R
import kotlinx.android.synthetic.main.view_enquiry_result.view.*
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 20/08/19.
 */
open class VoucherGameEnquiryResultWidget @JvmOverloads constructor(@NotNull context: Context,
                                                                    attrs: AttributeSet? = null,
                                                                    defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, getLayout(), this)
    }

    fun setEnquiryResultField(title: String) {
        enquiry_result_field.text = title
    }

    fun setEnquiryResultValue(value: String) {
        enquiry_result_value.text = value
    }

    fun setEnquiryResult(field: String, value: String) {
        setEnquiryResultField(field)
        setEnquiryResultValue(value)
    }

    open fun getLayout(): Int {
        return R.layout.view_enquiry_result
    }
}
