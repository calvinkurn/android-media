package com.tokopedia.localizationchooseaddress.util

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressViewModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import javax.inject.Inject

class ChooseAddressHelper {

    fun getLocalCacheData(context: Context): LocalCacheModel {
        var chooseAddressPref = ChooseAddressSharePref(context)
        return chooseAddressPref?.getLocalCacheData()
    }

    fun compareAddressData(context: Context, data: LocalCacheModel): Boolean {
        var chooseAddressPref = ChooseAddressSharePref(context)
        return chooseAddressPref?.getLocalCacheData() == data
    }

}