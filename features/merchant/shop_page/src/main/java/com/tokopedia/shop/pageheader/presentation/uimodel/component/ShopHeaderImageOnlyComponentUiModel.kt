package com.tokopedia.shop.pageheader.presentation.uimodel.component

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component.ShopPageHeaderImageOnlyComponentTypeFactory

data class ShopHeaderImageOnlyComponentUiModel(
    override val name: String = "",
    override val type: String = "",
    val image: String = "",
    val imageLink: String = "",
    val isBottomSheet: Boolean = false
) : BaseShopHeaderComponentUiModel, ImpressHolder() {

    override var componentPosition: Int = -1

    override fun type(typeFactory: BaseAdapterTypeFactory): Int {
        return if (typeFactory is ShopPageHeaderImageOnlyComponentTypeFactory) {
            typeFactory.type(this)
        } else {
            -1
        }
    }
}
