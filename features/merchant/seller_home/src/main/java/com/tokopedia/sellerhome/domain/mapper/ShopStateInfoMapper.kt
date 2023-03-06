package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.sellerhome.domain.model.GetShopStateInfoResponse
import com.tokopedia.sellerhome.domain.model.GetShopStateResponse
import com.tokopedia.sellerhome.domain.model.InfoWidgetButtonModel
import com.tokopedia.sellerhome.view.model.ShopStateInfoUiModel
import com.tokopedia.sellerhomecommon.presentation.model.ShopStateUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 23/11/22.
 */

class ShopStateInfoMapper @Inject constructor() {

    companion object {
        private const val TOAST: Long = 1
    }

    fun mapToUiModel(data: GetShopStateInfoResponse): ShopStateInfoUiModel {
        val info = data.fetchInfoWidgetData.data.firstOrNull() ?: return ShopStateInfoUiModel()
        return ShopStateInfoUiModel(
            dataKey = info.dataKey,
            imageUrl = info.imageUrl,
            title = info.title,
            subtitle = info.subtitle,
            button = getButton(info.button),
            buttonAlt = getButton(info.buttonAlt),
            dataSign = info.dataSign,
            subType = getSubType(info.subType),
            isNewSellerState = isNewSellerState(info.meta.shopState)
        )
    }

    fun mapToUiModel(data: GetShopStateResponse): ShopStateInfoUiModel {
        val info = data.fetchInfoWidgetData.data.firstOrNull() ?: return ShopStateInfoUiModel()
        return ShopStateInfoUiModel(
            isNewSellerState = isNewSellerState(info.meta.shopState)
        )
    }

    private fun isNewSellerState(shopState: Long): Boolean {
        return listOf(
            ShopStateUiModel.NEW_REGISTERED_SHOP,
            ShopStateUiModel.ADDED_PRODUCT,
            ShopStateUiModel.VIEWED_PRODUCT
        ).contains(shopState)
    }

    private fun getButton(button: InfoWidgetButtonModel): ShopStateInfoUiModel.Button {
        return ShopStateInfoUiModel.Button(
            name = button.name,
            appLink = button.appLink
        )
    }

    private fun getSubType(subType: Long): ShopStateInfoUiModel.SubType {
        return when (subType) {
            TOAST -> ShopStateInfoUiModel.SubType.TOAST
            else -> ShopStateInfoUiModel.SubType.POPUP
        }
    }
}
