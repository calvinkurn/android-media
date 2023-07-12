package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel

object CategoryL2Mapper {

    fun MutableList<Visitable<*>>.addChooseAddress(addressData: LocalCacheModel)  {
        add(TokoNowChooseAddressWidgetUiModel(addressData = addressData))
    }
}
