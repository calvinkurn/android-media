package com.tokopedia.digital_product_detail.data.mapper

import com.tokopedia.digital_product_detail.data.model.data.DigitalPersoData
import com.tokopedia.digital_product_detail.data.model.data.DigitalPersoProduct
import com.tokopedia.digital_product_detail.data.model.data.DigitalPersoWidget
import com.tokopedia.digital_product_detail.data.model.data.RechargeSaveTelcoUserBalanceAccessToken
import com.tokopedia.digital_product_detail.domain.model.DigitalCheckBalanceModel
import com.tokopedia.digital_product_detail.domain.model.DigitalCheckBalanceOTPBottomSheetModel
import com.tokopedia.digital_product_detail.domain.model.DigitalCheckBalanceProductModel
import com.tokopedia.digital_product_detail.domain.model.DigitalCheckBalanceWidgetModel
import com.tokopedia.digital_product_detail.domain.model.DigitalSaveAccessTokenResultModel
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import javax.inject.Inject

class DigitalPersoMapper @Inject constructor() {

    fun mapDigiPersoToCheckBalanceModel(data: DigitalPersoData): DigitalCheckBalanceModel {
        val persoItem = data.items.getOrNull(0)
        return DigitalCheckBalanceModel(
            title = persoItem?.title.toEmptyStringIfNull(),
            subtitle = persoItem?.subtitle.toEmptyStringIfNull(),
            label = persoItem?.label2.toEmptyStringIfNull(),
            bottomSheetModel = DigitalCheckBalanceOTPBottomSheetModel(
                title = persoItem?.title.toEmptyStringIfNull(),
                mediaUrl = persoItem?.mediaURL.toEmptyStringIfNull(),
                description = persoItem?.label1.toEmptyStringIfNull(),
                buttonText = persoItem?.textLink.toEmptyStringIfNull(),
                buttonAppLink = persoItem?.appLink.toEmptyStringIfNull()
            ),
            campaignLabelText = persoItem?.campaignLabelText.toEmptyStringIfNull(),
            campaignLabelTextColor = persoItem?.campaignLabelTextColor.toEmptyStringIfNull(),
            iconUrl = persoItem?.iconUrl.toEmptyStringIfNull(),
            widgets = mapDigiPersoWidgetToCheckBalanceWidgetModel(persoItem?.widgets),
            products = mapDigiPersoProductToCheckBalanceProductModel(persoItem?.products),
            widgetType = persoItem?.mediaUrlType.toEmptyStringIfNull()
        )
    }

    fun mapSaveAccessTokenToAccessTokenResultModel(
        response: RechargeSaveTelcoUserBalanceAccessToken
    ): DigitalSaveAccessTokenResultModel {
        return DigitalSaveAccessTokenResultModel(
            isSuccess = response.grc == SAVE_ACCESS_TOKEN_SUCCESS,
            message = response.message
        )
    }

    private fun mapDigiPersoWidgetToCheckBalanceWidgetModel(
        widgets: List<DigitalPersoWidget>?
    ): List<DigitalCheckBalanceWidgetModel> {
        return widgets?.map {
            DigitalCheckBalanceWidgetModel(
                title = it.title,
                subtitle = it.subtitle,
                iconUrl = it.iconUrl
            )
        } ?: emptyList()
    }

    private fun mapDigiPersoProductToCheckBalanceProductModel(
        products: List<DigitalPersoProduct>?
    ): List<DigitalCheckBalanceProductModel> {
        return products?.map {
            DigitalCheckBalanceProductModel(
                title = it.title,
                subtitle = it.subtitle,
                subtitleColor = it.subtitleColor,
                applink = it.applink,
                buttonText = it.buttonText,
                productId = it.productId,
                productPrice = it.price
            )
        } ?: emptyList()
    }

    companion object {
        const val SAVE_ACCESS_TOKEN_SUCCESS = "E"
    }
}
