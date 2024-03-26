package com.tokopedia.autocompletecomponent.util

import android.annotation.SuppressLint
import android.content.Context
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils

/**
 * Used as wrapper for ChooseAdressUtils.kt to make it injectable into viewmodel.
 */
internal class ChooseAddressUtilsWrapper(private val context: Context) {
    @SuppressLint("PII Data Exposure")
    fun getLocalizingAddressData() = ChooseAddressUtils.getLocalizingAddressData(context)
}
