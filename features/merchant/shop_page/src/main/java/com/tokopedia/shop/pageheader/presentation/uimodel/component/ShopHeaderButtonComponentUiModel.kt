package com.tokopedia.shop.pageheader.presentation.uimodel.component

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component.ShopHeaderButtonComponentTypeFactory

data class ShopHeaderButtonComponentUiModel(
        override val name: String = "",
        override val type: String = "",
        val icon: String = "",
        var label: String = "",
        val buttonType: String = "",
        val link: String = "",
        val isBottomSheet: Boolean = false
) : BaseShopHeaderComponentUiModel, InterfaceShopHeaderFollowButtonFields {

    override var isButtonLoading = false
    override var leftDrawableUrl = ""
    override var isFollowing = false

    override fun type(typeFactory: BaseAdapterTypeFactory): Int {
        return if (typeFactory is ShopHeaderButtonComponentTypeFactory) {
            typeFactory.type(this)
        } else {
            -1
        }
    }

}
