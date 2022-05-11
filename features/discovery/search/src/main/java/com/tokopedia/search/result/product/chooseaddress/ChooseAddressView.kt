package com.tokopedia.search.result.product.chooseaddress

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

interface ChooseAddressView {
    @Deprecated("isEnableChooseAddress is always true")
    val isChooseAddressWidgetEnabled: Boolean
    val chooseAddressData: LocalCacheModel?
    fun getIsLocalizingAddressHasUpdated(currentChooseAddressData: LocalCacheModel): Boolean
}