package com.tokopedia.tokomember.util

import androidx.annotation.StringDef
import com.tokopedia.tokomember.util.FollowWidgetType.Companion.MEMBERSHIP_CLOSE
import com.tokopedia.tokomember.util.FollowWidgetType.Companion.MEMBERSHIP_OPEN

const val IO = "IO"

const val TM_BOTTOMSHEET_DATA =""""""

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
@StringDef(MEMBERSHIP_OPEN, MEMBERSHIP_CLOSE)
annotation class FollowWidgetType {

    companion object {
        const val MEMBERSHIP_OPEN = "membership_open"
        const val MEMBERSHIP_CLOSE = "membership_close"
    }
}