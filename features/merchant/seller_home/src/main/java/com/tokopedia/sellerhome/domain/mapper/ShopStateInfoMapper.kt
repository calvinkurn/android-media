package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.sellerhome.domain.model.GetShopStateInfoResponse
import com.tokopedia.sellerhome.domain.model.InfoWidgetButtonModel
import com.tokopedia.sellerhome.view.model.ShopStateInfoUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 23/11/22.
 */

class ShopStateInfoMapper @Inject constructor() {

    companion object {
        private const val TOAST: Long = 0
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
            isNewSellerState = info.showWidget
        )
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