package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChooseAddressWidgetTypeFactory

data class TokoNowChooseAddressWidgetUiModel(
        val id: String = "",
        val backgroundLightColor: String = String.EMPTY,
        val backgroundDarkColor: String = String.EMPTY,
        val addressData: LocalCacheModel = LocalCacheModel()
) : Visitable<TokoNowChooseAddressWidgetTypeFactory>  {
    override fun type(typeFactory: TokoNowChooseAddressWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
