package com.tokopedia.shop.pageheader.presentation.uimodel.component

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component.ShopHeaderBadgeTextValueComponentTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component.ShopHeaderButtonComponentTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component.ShopHeaderImageOnlyComponentTypeFactory

data class ShopHeaderButtonComponentUiModel(
        override val name: String = "",
        override val type: String = "",
        val icon: String = "",
        val label: String = "",
        val buttonType: String = "",
        val link: String = "",
        val isBottomSheet: Boolean = false
) : BaseShopHeaderComponentUiModel {

    override fun type(typeFactory: BaseAdapterTypeFactory): Int {
        return if (typeFactory is ShopHeaderButtonComponentTypeFactory) {
            typeFactory.type(this)
        } else {
            -1
        }
    }

}
