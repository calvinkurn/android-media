package com.tokopedia.campaign.utils.textwatcher

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.tokopedia.kotlin.extensions.view.digitsOnly
import java.text.DecimalFormat

class NumberThousandSeparatorTextWatcher(
    private val view: EditText,
    private val decimalFormatter: DecimalFormat,
    private val afterTextChangedAction: (Long, String) -> Unit
) : TextWatcher {

    var isForceTextChanged = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (view.hasFocus() || isForceTextChanged) {
            view.removeTextChangedListener(this)
            isForceTextChanged = false
            try {
                val number = s.toString().digitsOnly()
                val formattedNumber = decimalFormatter.format(number)
                afterTextChangedAction(number, formattedNumber)

            }  catch (e: Exception) {
                e.printStackTrace()
            }

            view.addTextChangedListener(this)
        }
    }

}
