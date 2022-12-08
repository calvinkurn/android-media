package com.tokopedia.mvc.presentation.creation.step2.helper

import android.content.Context
import androidx.annotation.StringRes
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.mvc.R
import javax.inject.Inject

class ErrorMessageHelper @Inject constructor(@ApplicationContext private val context: Context) {

    fun getString(@StringRes resId: Int): String {
        return try {
            context.getString(resId)
        } catch (e: Exception) {
            ""
        }
    }

    fun getVoucherInputErrorMessage(input: String): String {
        return if (input.count() in 1..4) {
            getString(R.string.smvc_input_count_error)
        } else if (input.isEmpty()) {
            getString(R.string.smvc_input_empty_error)
        } else {
            ""
        }
    }
}
