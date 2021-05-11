package com.tokopedia.tokomart.searchcategory.utils

import android.content.Context
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import javax.inject.Inject

open class ChooseAddressWrapper @Inject constructor (private val context: Context) {

    open fun isChooseAddressEnabled() = ChooseAddressUtils.isRollOutUser(context)

    open fun getChooseAddressData() = ChooseAddressUtils.getLocalizingAddressData(context)

    open fun isChooseAddressUpdated(currentChooseAddressData: LocalCacheModel) =
            ChooseAddressUtils.isLocalizingAddressHasUpdated(context, currentChooseAddressData)
}
