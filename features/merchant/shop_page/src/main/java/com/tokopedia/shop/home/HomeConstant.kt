package com.tokopedia.shop.home

import com.tokopedia.shop.home.WidgetName.DISPLAY_DOUBLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_SINGLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_TRIPLE_COLUMN
import com.tokopedia.shop.home.WidgetName.SLIDER_BANNER
import com.tokopedia.shop.home.WidgetName.SLIDER_SQUARE_BANNER
import com.tokopedia.shop.home.WidgetName.VIDEO
import com.tokopedia.shop.home.WidgetType.DISPLAY
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel.DisplayWidgetItem


object HomeConstant {
    const val YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="

    val singleItemImage = ShopHomeDisplayWidgetUiModel(
            name = DISPLAY,
            type = DISPLAY_SINGLE_COLUMN,
            data = mutableListOf(DisplayWidgetItem(
                    imageUrl = "https://ecs7.tokopedia.net/img/attachment/2020/1/9/11329846/11329846_1a88a8a6-64a5-4fde-a424-38d6afe11014.jpg"
            ))
    )

    val doubleItemImage = ShopHomeDisplayWidgetUiModel(
            name = DISPLAY,
            type = DISPLAY_DOUBLE_COLUMN,
            data = mutableListOf<DisplayWidgetItem>().apply {
                add(DisplayWidgetItem(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/7/31/11329846/11329846_076fed82-338d-4b19-8cc3-3746c5118ba2.jpg"
                ))
                add(DisplayWidgetItem(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2020/1/9/11329846/11329846_1a88a8a6-64a5-4fde-a424-38d6afe11014.jpg"
                ))
            }
    )

    val tripleItemImage = ShopHomeDisplayWidgetUiModel(
            name = DISPLAY,
            type = DISPLAY_TRIPLE_COLUMN,
            data = mutableListOf<DisplayWidgetItem>().apply {
                add(DisplayWidgetItem(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/7/31/11329846/11329846_076fed82-338d-4b19-8cc3-3746c5118ba2.jpg"
                ))
                add(DisplayWidgetItem(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/7/31/11329846/11329846_076fed82-338d-4b19-8cc3-3746c5118ba2.jpg"
                ))
                add(DisplayWidgetItem(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2020/1/9/11329846/11329846_1a88a8a6-64a5-4fde-a424-38d6afe11014.jpg"
                ))
            }
    )

    val sliderSquareWidget = ShopHomeDisplayWidgetUiModel(
            name = DISPLAY ,
            type = SLIDER_SQUARE_BANNER,
            data = mutableListOf<DisplayWidgetItem>().apply {
                add(DisplayWidgetItem(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/8/27/48918596/48918596_1aec657d-beaa-4df0-a4e1-36eec6543eb3.jpg"
                ))
                add(DisplayWidgetItem(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/8/27/48918596/48918596_0f4a65fd-87a8-4223-9bce-ae6b29c11d1b.jpg"
                ))
                add(DisplayWidgetItem(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/8/27/48918596/48918596_9da7403f-7721-433b-ad90-877455c7a190.jpg"
                ))
                add(DisplayWidgetItem(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/8/27/48918596/48918596_c285a0ae-74ae-41c3-83bd-f893b4a7831f.jpg"
                ))
            }
    )

    val sliderBannerWidget = ShopHomeDisplayWidgetUiModel(
            name = SLIDER_BANNER,
            data = mutableListOf<DisplayWidgetItem>().apply {
                add(DisplayWidgetItem(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2020/1/27/24495178/24495178_f53ec2ce-93c5-46dd-88ae-380b48148a74.jpg"
                ))
                add(DisplayWidgetItem(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2020/1/27/24495178/24495178_7264a34c-df5d-496b-9d0f-092d59ab0cb4.jpg"
                ))
                add(DisplayWidgetItem(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2020/1/27/24495178/24495178_d99d8289-88ad-43a2-b166-90a7d1110631.jpg"
                ))
                add(DisplayWidgetItem(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2020/1/27/24495178/24495178_26902c6c-feaa-4e49-bcbc-e6c9ff0506aa.jpg"
                ))
            }
    )

    //TODO data static video
    val videoWidget = ShopHomeDisplayWidgetUiModel(
            name = VIDEO,
            data = mutableListOf<DisplayWidgetItem>().apply {
                add(DisplayWidgetItem(
                        videoUrl = "Ez6ODtGpL1w"
                ))
            }
    )
}

object GqlQueryConstant {
    const val GQL_GET_SHOP_PAGE_HOME_LAYOUT = "gql_get_shop_page_home_layout"
    const val GQL_ATC_MUTATION = "atcMutation"
}

object WidgetType {
    const val DISPLAY = "display"
    const val PRODUCT = "product"
    const val VOUCHER = "promo"
}

object WidgetName {
    const val SLIDER_BANNER = "slider_banner"
    const val SLIDER_SQUARE_BANNER = "slider_square"
    const val DISPLAY_SINGLE_COLUMN = "display_single_column"
    const val DISPLAY_DOUBLE_COLUMN = "display_double_column"
    const val DISPLAY_TRIPLE_COLUMN = "display_triple_column"
    const val VIDEO = "video"
    const val PRODUCT = "product"
    const val VOUCHER = "voucher"
}