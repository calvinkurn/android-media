package com.tokopedia.vouchercreation.common.textwatcher

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.tokopedia.vouchercreation.common.extension.digitsOnlyInt
import java.text.DecimalFormat

class NumberThousandSeparatorTextWatcher(
    private val view: EditText,
    private val decimalFormatter: DecimalFormat,
    private val afterTextChangedAction: (Int, String) -> Unit
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (view.hasFocus()) {
            view.removeTextChangedListener(this)

            try {
                val number = s.toString().digitsOnlyInt()
                val formattedNumber = decimalFormatter.format(number)
                afterTextChangedAction(number, formattedNumber)

            }  catch (e: Exception) {
                e.printStackTrace()
            }

            view.addTextChangedListener(this)
        }
    }

}