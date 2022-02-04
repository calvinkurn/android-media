package com.tokopedia.thankyou_native.data.mapper

import com.tokopedia.thankyou_native.presentation.adapter.model.GyroTokomemberItem
import com.tokopedia.thankyou_native.presentation.adapter.model.TokoMemberRequestParam
import com.tokopedia.thankyou_native.presentation.adapter.model.TokomemberModel
import com.tokopedia.tokomember.model.BottomSheetContentItem
import com.tokopedia.tokomember.model.MembershipGetShopRegistrationWidget
import com.tokopedia.tokomember.model.WidgetContentItem

object TokomemberMapper {
    fun getGyroTokomemberItem(
        shopRegisterResponse: MembershipGetShopRegistrationWidget?,
        tokomemberRequestParam: TokoMemberRequestParam
    ): TokomemberModel {
        val membershipType = shopRegisterResponse?.membershipType ?: 0
        val waitingWidgetData = shopRegisterResponse?.widgetContent?.get(0) ?: WidgetContentItem()
        val instantWidgetData = shopRegisterResponse?.widgetContent?.get(1) ?: WidgetContentItem()
        val successWidgetData = shopRegisterResponse?.widgetContent?.get(2) ?: WidgetContentItem()
        val bottomSheetContentItem = shopRegisterResponse?.bottomSheetContent?.get(0) ?: BottomSheetContentItem()

        bottomSheetContentItem.source = tokomemberRequestParam.source
        bottomSheetContentItem.shopID = tokomemberRequestParam.shopID
        bottomSheetContentItem.paymentID = tokomemberRequestParam.paymentID
        bottomSheetContentItem.membershipType = membershipType


        return TokomemberModel(
            listOfTokomemberItem = listOf(
                getWidgetData(
                    waitingWidgetData,
                    membershipType,
                    bottomSheetContentItem
                ),
                getWidgetData(
                    instantWidgetData,
                    membershipType,
                    bottomSheetContentItem
                ),
                getWidgetData(
                    successWidgetData,
                    membershipType
                )
            )
        )
    }

    private fun getWidgetData(
        widgetContentItem: WidgetContentItem,
        membershipType: Int,
        bottomSheetContentItem: BottomSheetContentItem = BottomSheetContentItem()
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
            membershipType = membershipType,
            listOfBottomSheetContent = bottomSheetContentItem
        )
    }
}