package com.tokopedia.shop.pageheader.presentation.uimodel.component

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component.ShopPageHeaderBadgeTextValueComponentTypeFactory

data class ShopPageHeaderBadgeTextValueComponentUiModel(
    override val name: String = "",
    override val type: String = "",
    val ctaText: String = "",
    val ctaLink: String = "",
    val ctaIcon: String = "",
    val text: List<Text> = listOf()
) : BaseShopPageHeaderComponentUiModel, ImpressHolder() {

    override var componentPosition: Int = -1

    data class Text(
        val icon: String = "",
        val textLink: String = "",
        var textHtml: String = "",
        val isBottomSheet: Boolean = false
    )

    override fun type(typeFactory: BaseAdapterTypeFactory): Int {
        return if (typeFactory is ShopPageHeaderBadgeTextValueComponentTypeFactory) {
            typeFactory.type(this)
        } else {
            -1
        }
    }
}
