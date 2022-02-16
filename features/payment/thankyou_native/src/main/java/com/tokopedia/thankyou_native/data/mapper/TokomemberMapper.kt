package com.tokopedia.thankyou_native.data.mapper

import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.CLOSE_MEMBERSHIP
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.OPEN_MEMBERSHIP
import com.tokopedia.thankyou_native.presentation.adapter.model.*
import com.tokopedia.tokomember.model.BottomSheetContentItem
import com.tokopedia.tokomember.model.MembershipGetShopRegistrationWidget
import com.tokopedia.tokomember.model.WidgetContentItem

object TokomemberMapper {
    var waitingWidgetData = WidgetContentItem()
    var instantWidgetData = WidgetContentItem()
    var successWidgetData = WidgetContentItem()
    fun getGyroTokomemberItem(
        shopRegisterResponse: MembershipGetShopRegistrationWidget?,
        tokomemberRequestParam: TokoMemberRequestParam
    ): TokomemberModel {
        val membershipType = shopRegisterResponse?.membershipType ?: 0
        val cardId = shopRegisterResponse?.cardID ?: 0

        if (membershipType == CLOSE_MEMBERSHIP) {
            populateWidgetContent(shopRegisterResponse)
        }
        else if (membershipType ==  OPEN_MEMBERSHIP) {
            populateWidgetContent(shopRegisterResponse)
            successWidgetData = shopRegisterResponse?.widgetContent?.getOrNull(TOKOMEMBER_SUCCESS_WIDGET) ?: WidgetContentItem()
        }
        val bottomSheetContentItem = shopRegisterResponse?.bottomSheetContent?.getOrNull(TOKOMEMBER_SUCCESS_BOTTOMSHEET) ?: BottomSheetContentItem()

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

    private fun populateWidgetContent(shopRegisterResponse: MembershipGetShopRegistrationWidget?){
        waitingWidgetData = shopRegisterResponse?.widgetContent?.getOrNull(TOKOMEMBER_WAITING_WIDGET) ?: WidgetContentItem()
        instantWidgetData = shopRegisterResponse?.widgetContent?.getOrNull(TOKOMEMBER_INSTANT_WIDGET) ?: WidgetContentItem()
    }

}