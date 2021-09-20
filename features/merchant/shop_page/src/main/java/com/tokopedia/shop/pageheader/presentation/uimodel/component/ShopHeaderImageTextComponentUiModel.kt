package com.tokopedia.shop.pageheader.presentation.uimodel.component

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component.ShopHeaderImageTextComponentTypeFactory

data class ShopHeaderImageTextComponentUiModel(
        override val name: String = "",
        override val type: String = "",
        val images: Images = Images(),
        val textComponent: TextComponent = TextComponent()
) : BaseShopHeaderComponentUiModel, ImpressHolder() {

    override var componentPosition: Int = -1

    data class Images(
            val data: List<Data> = listOf(),
            val style: Int = -1
    ) {
        data class Data(
                val image: String = "",
                val imageLink: String = "",
                val isBottomSheet: Boolean = false
        )
    }

    data class TextComponent(
            val data: Data = Data(),
            val style: Int = -1
    ) {
        data class Data(
                val icon: String = "",
                val isBottomSheet: Boolean = false,
                val textHtml: String = "",
                val textLink: String = ""
        )
    }

    override fun type(typeFactory: BaseAdapterTypeFactory): Int {
        return if (typeFactory is ShopHeaderImageTextComponentTypeFactory) {
            typeFactory.type(this)
        } else {
            -1
        }
    }

}
