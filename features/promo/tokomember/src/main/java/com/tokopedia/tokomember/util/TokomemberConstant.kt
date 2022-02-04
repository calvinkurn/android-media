package com.tokopedia.tokomember.util

import androidx.annotation.IntDef
import com.tokopedia.tokomember.util.MembershipWidgetType.Companion.MEMBERSHIP_CLOSE
import com.tokopedia.tokomember.util.MembershipWidgetType.Companion.MEMBERSHIP_OPEN

const val IO = "IO"

const val TM_REGISTRATION_SHOP_DATA ="""
     query membershipGetShopRegistrationWidget(${'$'}shopID: Int!, ${'$'}amount: Float!) {
  membershipGetShopRegistrationWidget(orderData: {
        shopID: ${'$'}shopID, amount: ${'$'}amount } ) {
        resultStatus {
        code
        message
        reason
    }
    widgetContent {
        imageURL
        title
        description
        isShown
        isOpenBottomSheet
        url
        appLink
        usecase
    }
    bottomSheetContent {
        imageURL
        title
        description
        cta {
            text
            url
            appLink
            isShown
        }
    }
    membershipType
    cardID
  }
 }
"""

const val MEMBERSHIP_REGISTER ="""
    mutation membershipRegister(${'$'}cardID: Int!) {
  membershipRegister(cardID: ${'$'}cardID) {
    resultStatus {
      code
      message
      reason
    }
    infoMessage {
      imageURL
      title
      subtitle
      cta {
        text
        url
        appLink
      }
    }
  }
}
"""


@Retention(AnnotationRetention.SOURCE)
@IntDef(MEMBERSHIP_OPEN, MEMBERSHIP_CLOSE)
annotation class MembershipWidgetType {

    companion object {
        const val MEMBERSHIP_OPEN = 1
        const val MEMBERSHIP_CLOSE = 2
    }
}