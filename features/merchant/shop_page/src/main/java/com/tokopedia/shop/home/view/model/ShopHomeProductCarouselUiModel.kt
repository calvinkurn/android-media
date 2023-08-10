package com.tokopedia.shop.home.view.model

import android.os.Parcelable
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory
import kotlinx.parcelize.Parcelize

data class ShopHomeProductCarouselUiModel(
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = "",
    override val type: String = "",
    override val header: Header = Header(),
    override val isFestivity: Boolean = false,
    val title: String,
    val tabs: List<Tab>
) : BaseShopHomeWidgetUiModel() {

    enum class ComponentType {
        BANNER_SINGLE,
        PRODUCT_CARD_WITHOUT_PRODUCT_INFO,
        PRODUCT_CARD_WITH_PRODUCT_INFO
    }

    @Parcelize
    data class Tab(
        val tabId: Long,
        val label: String,
        val name: String,
        val components: List<Component>
    ) : Parcelable {

        @Parcelize
        data class Component(
            val id: Long,
            val name: String,
            val type: ComponentType,
            val ratio: String,
            val componentChild: List<ComponentChild>
        ) : Parcelable {

            @Parcelize
            data class ComponentChild(
                val imageId: Long,  //For main banner
                val imageUrl: String,  //For main banner
                val ctaLink: String, //For main banner
                val linkId: Long,  //For product info reference
                val linkType: String //For product info reference
            ) : Parcelable

        }
    }

    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
