package com.tokopedia.shop.home.view.model

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.WidgetType
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory
import kotlinx.parcelize.Parcelize

data class ShopHomeProductCarouselUiModel(
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = WidgetName.DYNAMIC_COMPONENT,
    override val type: String = WidgetType.DYNAMIC_COMPONENT,
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
        val label: String,
        val name: String,
        val componentList: List<ComponentList>
    ) : Parcelable {

        @Parcelize
        data class ComponentList(
            val componentId: Long,
            val componentName: String,
            val componentType: ComponentType,
            val ratio: String,
            val data: List<Data>
        ) : Parcelable {

            @Parcelize
            data class Data(
                val imageId: Long,
                val imageUrl: String,
                val ctaLink: String,
                val linkId: Long,
                val linkType: String,
                val isShowProductInfo: Boolean,
                val bannerType: BannerType
            ) : Parcelable {
                enum class BannerType(val id : String) {
                    VERTICAL("vertical"),
                    NONE("")
                }
            }


        }
    }

    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return if (typeFactory is ShopHomeAdapterTypeFactory) {
            typeFactory.type(this)
        } else {
            Int.ZERO
        }
    }
}
