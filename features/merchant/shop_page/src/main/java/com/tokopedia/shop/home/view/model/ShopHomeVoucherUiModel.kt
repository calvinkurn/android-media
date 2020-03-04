package com.tokopedia.shop.home.view.model

import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory

/**
 * Created by rizqiaryansa on 2020-02-21.
 */
data class ShopHomeVoucherUiModel(
        override val widgetId: Int = -1,
        override val layoutOrder: Int = -1,
        override val name: String = "",
        override val type: String = "",
        override val header: BaseShopHomeWidgetUiModel.Header = BaseShopHomeWidgetUiModel.Header(),
        val data: List<DisplayWidgetItem>? = null
) : BaseShopHomeWidgetUiModel {

    data class DisplayWidgetItem(
            val imageUrl: String? = null,
            val appLink: String? = null,
            val webLinkL: String? = null,
            val videoUrl: String? = null
    )

    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}