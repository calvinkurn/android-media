package com.tokopedia.search.utils

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import javax.inject.Inject

open class ChooseAddressWrapper @Inject constructor (@ApplicationContext private val context: Context) {

    open fun getChooseAddressData() = try {
        ChooseAddressUtils.getLocalizingAddressData(context)
    } catch (throwable: Throwable) {
        ChooseAddressConstant.emptyAddress
    }

    open fun isChooseAddressUpdated(currentChooseAddressData: LocalCacheModel) = try {
        ChooseAddressUtils.isLocalizingAddressHasUpdated(context, currentChooseAddressData)
    } catch (throwable: Throwable) {
        false
    }
}