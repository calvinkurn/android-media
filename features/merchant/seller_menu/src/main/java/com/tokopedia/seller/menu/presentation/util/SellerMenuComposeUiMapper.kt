package com.tokopedia.seller.menu.presentation.util

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.common.feature.list.data.model.filter.Tab
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.constant.MenuItemType
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.presentation.uimodel.ShopInfoUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuActionClick
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuComposeItem
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuInfoLoadingUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuInfoUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuItemUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuOrderUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuProductUiModel
import com.tokopedia.seller.menu.common.R as sellermenucommonR

object SellerMenuComposeUiMapper {

    fun getUpdatedProductSection(
        currentMenuList: MutableList<SellerMenuComposeItem>,
        isShopOwner: Boolean,
        productTabs: List<Tab>
    ): List<SellerMenuComposeItem> {
        val productIndex = currentMenuList.indexOfFirst { it is SellerMenuProductUiModel }
        if (productIndex > RecyclerView.NO_POSITION) {
            currentMenuList.run {
                removeAt(productIndex)
                add(
                    productIndex,
                    SellerMenuProductUiModel(
                        getProductCount(productTabs),
                        isShopOwner
                    )
                )
            }
        }
        return currentMenuList
    }

    fun getUpdatedNotificationSection(
        currentMenuList: MutableList<SellerMenuComposeItem>,
        isShopOwner: Boolean,
        newOrderCount: Int,
        readyToShipCount: Int,
        resolutionCount: Int
    ): List<SellerMenuComposeItem> {
        val orderIndex = currentMenuList.indexOfFirst { it is SellerMenuOrderUiModel }
        if (orderIndex > RecyclerView.NO_POSITION) {
            currentMenuList.run {
                removeAt(orderIndex)
                add(
                    orderIndex,
                    SellerMenuOrderUiModel(
                        newOrderCount,
                        readyToShipCount,
                        isShopOwner
                    )
                )
            }
        }

        val resolutionIndex =
            currentMenuList.indexOfFirst { (it as? SellerMenuItemUiModel)?.titleRes == sellermenucommonR.string.setting_menu_complaint }
        if (resolutionIndex > RecyclerView.NO_POSITION) {
            currentMenuList.run {
                removeAt(resolutionIndex)
                add(
                    resolutionIndex,
                    SellerMenuItemUiModel(
                        titleRes = sellermenucommonR.string.setting_menu_complaint,
                        type = MenuItemType.COMPLAIN,
                        eventActionSuffix = SettingTrackingConstant.COMPLAINT,
                        iconUnifyType = IconUnify.PRODUCT_INFO,
                        actionClick = SellerMenuActionClick.COMPLAINTS,
                        notificationCount = resolutionCount
                    )
                )
            }
        }

        return currentMenuList
    }

    fun getSuccessMenuList(
        successMenuList: MutableList<SellerMenuComposeItem>,
        uiModel: ShopInfoUiModel,
        shopAvatar: String,
        shopName: String
    ): List<SellerMenuComposeItem> {
        val loadingIndex = successMenuList.getTopIndex()
        if (loadingIndex > RecyclerView.NO_POSITION) {
            successMenuList.run {
                removeAt(loadingIndex)
                add(
                    loadingIndex,
                    SellerMenuInfoUiModel(
                        shopAvatarUrl = shopAvatar,
                        shopScore = uiModel.shopScore,
                        shopName = shopName,
                        shopAge = uiModel.shopAge,
                        shopFollowers = uiModel.shopInfo.shopFollowersUiModel?.shopFollowers.orZero(),
                        shopBadgeUrl = uiModel.shopInfo.shopBadgeUiModel?.shopBadgeUrl.orEmpty(),
                        userShopInfoWrapper = uiModel.shopInfo.shopStatusUiModel?.userShopInfoWrapper
                            ?: UserShopInfoWrapper(null, null),
                        partialResponseStatus = uiModel.shopInfo.partialResponseStatus,
                        balanceValue = uiModel.shopInfo.saldoBalanceUiModel?.balanceValue.orEmpty()
                    )
                )
            }
        }
        return successMenuList
    }

    fun getLoadingMenuList(
        currentMenuList: MutableList<SellerMenuComposeItem>
    ): List<SellerMenuComposeItem> {
        val loadingIndex = currentMenuList.getTopIndex()
        if (loadingIndex > RecyclerView.NO_POSITION) {
            currentMenuList.run {
                removeAt(loadingIndex)
                add(
                    loadingIndex,
                    SellerMenuInfoLoadingUiModel
                )
            }
        }
        return currentMenuList
    }

    fun getErrorMenuList(
        errorMenuList: MutableList<SellerMenuComposeItem>,
        shopAvatar: String,
        shopName: String,
        shopAge: Long
    ): List<SellerMenuComposeItem> {
        val topIndex = errorMenuList.getTopIndex()
        if (topIndex > RecyclerView.NO_POSITION) {
            errorMenuList.run {
                removeAt(topIndex)
                add(
                    topIndex,
                    SellerMenuInfoUiModel(
                        shopAvatarUrl = shopAvatar,
                        shopScore = Long.ZERO,
                        shopName = shopName,
                        shopAge = shopAge,
                        shopFollowers = Long.ZERO,
                        shopBadgeUrl = String.EMPTY,
                        userShopInfoWrapper = UserShopInfoWrapper(null, null),
                        partialResponseStatus = false to false,
                        balanceValue = String.EMPTY
                    )
                )
            }
        }
        return errorMenuList
    }

    fun getProductCount(response: List<Tab>): Int {
        var totalProductCount = 0

        response.filter { SellerUiModelMapper.supportedProductStatus.contains(it.id) }.map {
            totalProductCount += it.value.toIntOrZero()
        }

        return totalProductCount
    }

    private fun List<SellerMenuComposeItem>.getTopIndex(): Int {
        return indexOfFirst {
            it is SellerMenuInfoLoadingUiModel || it is SellerMenuInfoUiModel
        }
    }
}
