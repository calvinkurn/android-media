package com.tokopedia.digital.topupbillsproduct.compoundview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.digital.topupbillsproduct.R
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 25/04/19.
 */
open class DigitalBaseInputNumberView @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                           defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val hintInputNumber: TextView
    private val imgOperator: ImageView
    private val btnClear: Button
    private val autoCompleteInputNumber: AutoCompleteTextView
    private val btnContactPicker: Button
    private val errorInputNumber: TextView

    init {
        val view = View.inflate(context, getLayout(), this)
        hintInputNumber = view.findViewById(R.id.hint_input_number)
        imgOperator = view.findViewById(R.id.img_operator)
        btnClear = view.findViewById(R.id.btn_clear_input_number)
        autoCompleteInputNumber = view.findViewById(R.id.ac_input_number)
        btnContactPicker = view.findViewById(R.id.btn_contact_picker)
        errorInputNumber = view.findViewById(R.id.error_input_number)
    }

    open fun getLayout() : Int {
        return R.layout.view_digital_input_number
    }

    fun setInputNumber(inputNumber: String) {

    }
}
