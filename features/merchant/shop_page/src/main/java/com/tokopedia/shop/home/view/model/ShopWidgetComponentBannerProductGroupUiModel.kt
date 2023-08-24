package com.tokopedia.shop.home.view.model

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.WidgetType
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory
import kotlinx.parcelize.Parcelize

data class ShopWidgetComponentBannerProductGroupUiModel(
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = WidgetName.BANNER_PRODUCT_GROUP,
    override val type: String = WidgetType.COMPONENT,
    override val header: Header = Header(),
    override val isFestivity: Boolean = false,
    val title: String,
    val tabs: List<Tab>,
    val viewAllChevronAppLink: String
) : BaseShopHomeWidgetUiModel() {

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
            val data: List<Data>
        ) : Parcelable {
            enum class ComponentType(val id: String) {
                DISPLAY_SINGLE_COLUMN("display_single_column"),
                PRODUCT("product")
            }

            @Parcelize
            data class Data(
                val imageUrl: String,
                val ctaLink: String,
                val linkId: Long,
                val linkType: LinkType,
                val isShowProductInfo: Boolean,
                val bannerType: BannerType
            ) : Parcelable {
                @Parcelize
                enum class BannerType(val id: String) : Parcelable {
                    VERTICAL("vertical"),
                    NONE("")
                }

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
