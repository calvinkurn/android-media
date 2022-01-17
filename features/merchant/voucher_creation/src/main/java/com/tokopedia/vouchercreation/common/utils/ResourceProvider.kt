package com.tokopedia.vouchercreation.common.utils

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.vouchercreation.R
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext val context : Context) {

    fun getInvalidMinimalQuantityQuantityErrorMessage(): String {
        return context.getString(R.string.error_message_invalid_cashback_minimum_purchase_quantity)
    }

    fun getInvalidMinimalPurchaseNominalErrorMessage(): String {
        return context.getString(R.string.error_message_invalid_cashback_minimum_purchase_nominal)
    }

}