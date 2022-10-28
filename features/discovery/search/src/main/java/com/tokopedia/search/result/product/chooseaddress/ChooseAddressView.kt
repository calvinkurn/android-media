package com.tokopedia.search.result.product.chooseaddress

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

interface ChooseAddressView {
    val chooseAddressData: LocalCacheModel?
    fun getIsLocalizingAddressHasUpdated(currentChooseAddressData: LocalCacheModel): Boolean
}