package com.tokopedia.shop.home

import com.tokopedia.shop.home.view.model.WidgetDataModel
import com.tokopedia.shop.home.view.model.WidgetModel


const val WidgetMultipleImageColumn = "multiple_image_column"
const val WidgetSliderSquareBanner = "slider_square_widget"
const val WidgetSliderBanner = "slider_banner_widget"
const val WidgetYoutubeVideo = "widget_video_youtube"

object HomeConstant {
    const val SHOP_PAGE_HOME_GET_LAYOUT_QUERY = "shop_page_home_get_layout"
    const val YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="

    val singleItemImage = WidgetModel(
            name = WidgetMultipleImageColumn,
            data = mutableListOf(WidgetDataModel(
                    imageUrl = "https://ecs7.tokopedia.net/img/attachment/2020/1/9/11329846/11329846_1a88a8a6-64a5-4fde-a424-38d6afe11014.jpg"
            ))
    )

    val doubleItemImage = WidgetModel(
            name = WidgetMultipleImageColumn,
            data = mutableListOf<WidgetDataModel>().apply {
                add(WidgetDataModel(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/7/31/11329846/11329846_076fed82-338d-4b19-8cc3-3746c5118ba2.jpg"
                ))
                add(WidgetDataModel(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2020/1/9/11329846/11329846_1a88a8a6-64a5-4fde-a424-38d6afe11014.jpg"
                ))
            }
    )

    val tripleItemImage = WidgetModel(
            name = WidgetMultipleImageColumn,
            data = mutableListOf<WidgetDataModel>().apply {
                add(WidgetDataModel(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/7/31/11329846/11329846_076fed82-338d-4b19-8cc3-3746c5118ba2.jpg"
                ))
                add(WidgetDataModel(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/7/31/11329846/11329846_076fed82-338d-4b19-8cc3-3746c5118ba2.jpg"
                ))
                add(WidgetDataModel(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2020/1/9/11329846/11329846_1a88a8a6-64a5-4fde-a424-38d6afe11014.jpg"
                ))
            }
    )

    val sliderSquareWidget = WidgetModel(
            name = WidgetSliderSquareBanner,
            data = mutableListOf<WidgetDataModel>().apply {
                add(WidgetDataModel(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/8/27/48918596/48918596_1aec657d-beaa-4df0-a4e1-36eec6543eb3.jpg"
                ))
                add(WidgetDataModel(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/8/27/48918596/48918596_0f4a65fd-87a8-4223-9bce-ae6b29c11d1b.jpg"
                ))
                add(WidgetDataModel(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/8/27/48918596/48918596_9da7403f-7721-433b-ad90-877455c7a190.jpg"
                ))
                add(WidgetDataModel(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/8/27/48918596/48918596_c285a0ae-74ae-41c3-83bd-f893b4a7831f.jpg"
                ))
            }
    )

    val sliderBannerWidget = WidgetModel(
            name = WidgetSliderBanner,
            data = mutableListOf<WidgetDataModel>().apply {
                add(WidgetDataModel(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2020/1/27/24495178/24495178_f53ec2ce-93c5-46dd-88ae-380b48148a74.jpg"
                ))
                add(WidgetDataModel(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2020/1/27/24495178/24495178_7264a34c-df5d-496b-9d0f-092d59ab0cb4.jpg"
                ))
                add(WidgetDataModel(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2020/1/27/24495178/24495178_d99d8289-88ad-43a2-b166-90a7d1110631.jpg"
                ))
                add(WidgetDataModel(
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2020/1/27/24495178/24495178_26902c6c-feaa-4e49-bcbc-e6c9ff0506aa.jpg"
                ))
            }
    )

    //TODO data static video
    val videoWidget = WidgetModel(
            name = WidgetYoutubeVideo,
            data = mutableListOf<WidgetDataModel>().apply {
                add(WidgetDataModel(
                        videoUrl = "Ez6ODtGpL1w"
                ))
            }
    )
}