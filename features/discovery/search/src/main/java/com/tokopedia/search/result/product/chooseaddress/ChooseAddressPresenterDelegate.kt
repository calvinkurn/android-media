package com.tokopedia.search.result.product.chooseaddress

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.utils.toSearchParams
import javax.inject.Inject

@SearchScope
class ChooseAddressPresenterDelegate @Inject constructor(
    private val chooseAddressView: ChooseAddressView
) {
    private var chooseAddressData: LocalCacheModel? = null

    fun updateChooseAddress(onChooseAddressUpdated: () -> Unit = { }) {
        chooseAddressData = chooseAddressView.chooseAddressData

        onChooseAddressUpdated()
    }

    fun getChooseAddressParams(): Map<String, String>? {
        val chooseAddressData = chooseAddressData ?: return null

        return chooseAddressData.toSearchParams()
    }

    fun reCheckChooseAddressData(onChooseAddressUpdated: () -> Unit) {
        val isAddressDataUpdated = isAddressDataUpdated()

        if (isAddressDataUpdated)
            updateChooseAddress(onChooseAddressUpdated)
    }

   private fun isAddressDataUpdated(): Boolean {
        val chooseAddressData = chooseAddressData ?: return false

        return chooseAddressView.getIsLocalizingAddressHasUpdated(chooseAddressData)
    }
}
