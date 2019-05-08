package com.tokopedia.topupbills.telco.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.topupbills.R
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 25/04/19.
 */
open class DigitalBaseClientNumberWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                                   defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val hintInputNumber: TextView
    private val imgOperator: ImageView
    private val btnClear: Button
    private val autoCompleteInputNumber: AutoCompleteTextView
    private val btnContactPicker: Button
    private val errorInputNumber: TextView
    private lateinit var listener: ActionListener

    init {
        val view = View.inflate(context, getLayout(), this)
        hintInputNumber = view.findViewById(R.id.hint_input_number)
        imgOperator = view.findViewById(R.id.img_operator)
        btnClear = view.findViewById(R.id.btn_clear_input_number)
        autoCompleteInputNumber = view.findViewById(R.id.ac_input_number)
        btnContactPicker = view.findViewById(R.id.btn_copy_promo)
        errorInputNumber = view.findViewById(R.id.error_input_number)

        autoCompleteInputNumber.clearFocus()
        btnContactPicker.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                listener.navigateToContact()
            }
        })

        btnClear.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                autoCompleteInputNumber.setText("")
            }
        })
    }

    open fun getLayout(): Int {
        return R.layout.view_digital_input_number
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }


    fun setInputNumber(inputNumber: String) {
        autoCompleteInputNumber.setText(inputNumber)
    }

    interface ActionListener {
        fun navigateToContact()
    }
}
