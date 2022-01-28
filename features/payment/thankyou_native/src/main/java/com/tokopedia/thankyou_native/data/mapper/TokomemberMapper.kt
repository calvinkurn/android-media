package com.tokopedia.thankyou_native.data.mapper

import com.tokopedia.thankyou_native.presentation.adapter.model.GyroTokomemberItem
import com.tokopedia.thankyou_native.presentation.adapter.model.TokomemberModel
import com.tokopedia.tokomember.model.BottomSheetContentItem
import com.tokopedia.tokomember.model.ShopRegisterResponse
import com.tokopedia.tokomember.model.WidgetContentItem

object TokomemberMapper {
    fun getGyroTokomemberItem(shopRegisterResponse: ShopRegisterResponse): TokomemberModel {
        val data = shopRegisterResponse.data?.membershipGetShopRegistrationWidget
        val membershipType = data?.membershipType ?: 0
        val initialWidgetData = data?.widgetContent?.get(0) ?: WidgetContentItem()
        val successWidgetData = data?.widgetContent?.get(1) ?: WidgetContentItem()

        return TokomemberModel(
            listOf(
                getWidgetData(initialWidgetData, membershipType),
                getWidgetData(successWidgetData, membershipType)
            ),
            data?.bottomSheetContent?.get(0) ?: BottomSheetContentItem()
        )
    }

    private fun getWidgetData(
        widgetContentItem: WidgetContentItem,
        membershipType: Int
    ): GyroTokomemberItem {
        return GyroTokomemberItem(
            title = widgetContentItem.title,
            description = widgetContentItem.description,
            url = widgetContentItem.url,
            imageURL = widgetContentItem.imageURL,
            isOpenBottomSheet = widgetContentItem.isOpenBottomSheet ?: false,
            isShown = widgetContentItem.isShown,
            urlApp = widgetContentItem.appLink,
            usecase = widgetContentItem.usecase,
            membershipType = membershipType
        )
    }
}