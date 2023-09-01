package com.tokopedia.shop.home.view.model.banner_product_group

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.WidgetType
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import kotlinx.parcelize.Parcelize

data class ShopWidgetComponentBannerProductGroupUiModel(
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = WidgetName.BANNER_PRODUCT_GROUP,
    override val type: String = WidgetType.COMPONENT,
    override val header: Header = Header(),
    override val isFestivity: Boolean = false,
    val title: String = "",
    val tabs: List<Tab> = emptyList(),
    val viewAllChevronAppLink: String = "",
    val widgetStyle: String = ""
) : BaseShopHomeWidgetUiModel() {

    enum class WidgetStyle(val id: String) {
        VERTICAL("vertical"),
        HORIZONTAL("horizontal")
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
            val componentName: ComponentName,
            val data: List<Data>
        ) : Parcelable {
            enum class ComponentName(val id: String) {
                DISPLAY_SINGLE_COLUMN("display_single_column"),
                PRODUCT("product")
            }

            @Parcelize
            data class Data(
                val imageUrl: String,
                val ctaLink: String,
                val linkId: Long,
                val linkType: LinkType,
                val isShowProductInfo: Boolean
            ) : Parcelable {
                @Parcelize
                enum class LinkType(var id: String) : Parcelable {
                    PRODUCT("product"),
                    SHOWCASE("showcase"),
                    FEATURED_PRODUCT("featured")
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
