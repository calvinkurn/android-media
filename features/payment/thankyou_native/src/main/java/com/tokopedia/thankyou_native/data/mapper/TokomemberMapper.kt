package com.tokopedia.thankyou_native.data.mapper

import com.tokopedia.thankyou_native.presentation.adapter.model.*
import com.tokopedia.tokomember.model.BottomSheetContentItem
import com.tokopedia.tokomember.model.MembershipGetShopRegistrationWidget
import com.tokopedia.tokomember.model.WidgetContentItem

object TokomemberMapper {
    fun getGyroTokomemberItem(
        shopRegisterResponse: MembershipGetShopRegistrationWidget?,
        tokomemberRequestParam: TokoMemberRequestParam
    ): TokomemberModel {
        val membershipType = shopRegisterResponse?.membershipType ?: 0
        val cardId = shopRegisterResponse?.cardID ?: 0
        val waitingWidgetData = shopRegisterResponse?.widgetContent?.get(TOKOMEMBER_WAITING_WIDGET) ?: WidgetContentItem()
        val instantWidgetData = shopRegisterResponse?.widgetContent?.get(TOKOMEMBER_INSTANT_WIDGET) ?: WidgetContentItem()
        val successWidgetData = shopRegisterResponse?.widgetContent?.get(TOKOMEMBER_SUCCESS_WIDGET) ?: WidgetContentItem()
        val bottomSheetContentItem = shopRegisterResponse?.bottomSheetContent?.get(TOKOMEMBER_SUCCESS_BOTTOMSHEET) ?: BottomSheetContentItem()

        bottomSheetContentItem.source = tokomemberRequestParam.source
        bottomSheetContentItem.shopID = tokomemberRequestParam.shopID
        bottomSheetContentItem.paymentID = tokomemberRequestParam.paymentID
        bottomSheetContentItem.membershipType = membershipType


        return TokomemberModel(
            listOfTokomemberItem = listOf(
                getWidgetData(
                    waitingWidgetData,
                    membershipType,
                    bottomSheetContentItem,
                    cardId
                ),
                getWidgetData(
                    instantWidgetData,
                    membershipType,
                    bottomSheetContentItem,
                    cardId
                ),
                getWidgetData(
                    successWidgetData,
                    membershipType,
                    cardId = cardId
                )
            )
        )
    }

    private fun getWidgetData(
        widgetContentItem: WidgetContentItem,
        membershipType: Int,
        bottomSheetContentItem: BottomSheetContentItem = BottomSheetContentItem(),
        cardId: Int
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
            listOfBottomSheetContent = bottomSheetContentItem,
            membershipCardId = cardId.toString()
        )
    }
}