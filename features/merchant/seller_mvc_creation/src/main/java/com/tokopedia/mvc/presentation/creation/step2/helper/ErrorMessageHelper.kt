package com.tokopedia.mvc.presentation.creation.step2.helper

import android.content.Context
import androidx.annotation.StringRes
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject
import com.tokopedia.mvc.R

class ErrorMessageHelper @Inject constructor(@ApplicationContext private val context: Context) {

    fun getString(@StringRes resId: Int): String {
        return try {
            context.getString(resId)
        } catch (e: Exception) {
            ""
        }
    }

    fun getVoucherNameErrorMessage(voucherName: String): String {
        return if (voucherName.count() in 1 .. 4 ) {
            getString(R.string.smvc_input_count_error)
        } else if (voucherName.isEmpty()) {
            getString(R.string.smvc_input_empty_error)
        } else {
            ""
        }
    }
}
