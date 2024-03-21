package com.tokopedia.shop.home.util.mapper

import android.content.Context
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.common.domain.entity.ShopPrefetchData
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderP1HeaderData
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderActionWidgetFollowButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderBadgeTextValueComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderImageOnlyComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel
import javax.inject.Inject

class ShopPagePrefetchMapper @Inject constructor() {

    fun createHeaderData(context: Context?, prefetchData: ShopPrefetchData): ShopPageHeaderP1HeaderData {
        val basicInfo = createShopBasicInfo(prefetchData)
        val shopPerformance = createShopPerformance(prefetchData)
        val actionButton = createActionButton(context, prefetchData)

        return ShopPageHeaderP1HeaderData(
            shopName = prefetchData.shopName,
            shopAvatar = prefetchData.shopAvatar,
            shopBadge = prefetchData.shopBadge,
            listShopPageHeaderWidget = listOf(basicInfo, shopPerformance, actionButton)
        )
    }

    private fun createShopBasicInfo(prefetchData: ShopPrefetchData): ShopPageHeaderWidgetUiModel {
        return ShopPageHeaderWidgetUiModel(
            widgetId = "1",
            name = "shop_basic_info",
            type = "shop_basic_info",
            componentPages = listOf(
                ShopPageHeaderImageOnlyComponentUiModel(
                    name = "shop_logo",
                    type = "image_only",
                    image = prefetchData.shopAvatar
                ),
                ShopPageHeaderBadgeTextValueComponentUiModel(
                    name = "shop_name",
                    ctaIcon = "https://images.tokopedia.net/img/chevron_down.png",
                    type = "badge_text_value",
                    text = listOf(
                        ShopPageHeaderBadgeTextValueComponentUiModel.Text(
                            icon = prefetchData.shopBadge,
                            textHtml = prefetchData.shopName
                        ),
                        ShopPageHeaderBadgeTextValueComponentUiModel.Text(
                            textHtml = MethodChecker.fromHtml(prefetchData.shopLastOnline).toString()
                        )
                    )
                )
            )
        )
    }

    private fun createShopPerformance(prefetchData: ShopPrefetchData): ShopPageHeaderWidgetUiModel {
        return ShopPageHeaderWidgetUiModel(
            widgetId = "2",
            name = "shop_performance",
            type = "shop_performance",
            componentPages = listOf(
                ShopPageHeaderBadgeTextValueComponentUiModel(
                    name = "shop_rating",
                    type = "badge_text_value",
                    text = listOf(
                        ShopPageHeaderBadgeTextValueComponentUiModel.Text(
                            icon = "https://images.tokopedia.net/img/autocomplete/rating.png",
                            textHtml = prefetchData.shopRating.toString()
                        )
                    )
                )
            )
        )
    }

    private fun createActionButton(context: Context?, prefetchData: ShopPrefetchData): ShopPageHeaderWidgetUiModel {
        return ShopPageHeaderWidgetUiModel(
            widgetId = "3",
            name = "action_button",
            type = "action_button",
            componentPages = listOf(
                ShopPageHeaderButtonComponentUiModel().apply {
                    this.label = context?.getString(R.string.shop_page_label_chat).orEmpty()
                },
                ShopPageHeaderActionWidgetFollowButtonComponentUiModel().apply {
                    this.isButtonLoading = false
                    this.isFollowing = prefetchData.isFollowed
                    this.label = if (prefetchData.isFollowed) {
                        context?.getString(R.string.shop_page_label_following).orEmpty()
                    } else {
                        context?.getString(R.string.shop_page_label_follow).orEmpty()
                    }
                }
            )
        )
    }
}
