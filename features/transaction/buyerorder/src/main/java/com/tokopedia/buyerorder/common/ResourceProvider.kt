package com.tokopedia.buyerorder.common

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.buyerorder.R
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext private val context: Context?) {
    private fun getString(resId: Int): String? {
        return try {
            context?.getString(resId)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    fun getBuyerRequestCancelReasonMinCharMessage(): String {
        return getString(R.string.min_char_reason_lainnya).orEmpty()
    }

    fun getBuyerRequestCancelReasonShouldNotContainsSpecialCharsErrorMessage(): String {
        return getString(R.string.error_message_buyer_request_cancel_reason_should_not_contains_special_chars).orEmpty()
    }
}