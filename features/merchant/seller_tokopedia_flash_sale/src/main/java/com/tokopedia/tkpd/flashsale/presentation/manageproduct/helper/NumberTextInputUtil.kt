package com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper

import android.text.TextWatcher
import com.tokopedia.campaign.utils.constant.LocaleConstant
import com.tokopedia.campaign.utils.textwatcher.NumberThousandSeparatorTextWatcher
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.TextFieldUnify2
import java.text.DecimalFormat
import java.text.NumberFormat

object NumberTextInputUtil {
    const val NUMBER_PATTERN = "#,###,###"

    fun setNumberTextChangeListener(editText : TextFieldUnify2): TextWatcher {
        val numberFormatter = NumberFormat.getInstance(LocaleConstant.INDONESIA) as DecimalFormat
        numberFormatter.applyPattern(NUMBER_PATTERN)
        return NumberThousandSeparatorTextWatcher(
            editText.editText, numberFormatter
        ) { _, formatNumber ->
            editText.editText.setText(formatNumber)
            editText.editText.setSelection(
                editText.editText.text?.length.orZero()
            )
        }
    }
}
