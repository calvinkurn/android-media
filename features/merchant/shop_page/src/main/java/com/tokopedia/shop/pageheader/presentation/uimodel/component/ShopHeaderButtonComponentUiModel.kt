package com.tokopedia.shop.pageheader.presentation.uimodel.component

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderLayoutResponse
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component.ShopHeaderButtonComponentTypeFactory

open class ShopHeaderButtonComponentUiModel : BaseShopHeaderComponentUiModel, ImpressHolder() {

    override var name: String = ""
    override var type: String = ""
    override var componentPosition: Int = -1
    var icon: String = ""
    var label: String = ""
    var buttonType: String = ""
    var link: String = ""
    var isBottomSheet: Boolean = false

    fun mapComponentModel(component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component) {
        name = component.name
        type = component.type
        icon = component.data.icon
        label = component.data.label
        buttonType = component.data.buttonType
        link = component.data.link
        isBottomSheet = component.data.isBottomSheet
    }

    override fun type(typeFactory: BaseAdapterTypeFactory): Int {
        return if (typeFactory is ShopHeaderButtonComponentTypeFactory) {
            typeFactory.type(this)
        } else {
            -1
        }
    }

}
