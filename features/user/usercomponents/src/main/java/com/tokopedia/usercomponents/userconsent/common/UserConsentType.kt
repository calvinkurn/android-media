package com.tokopedia.usercomponents.userconsent.common

enum class UserConsentType(val value: Int) {
    TNC_MANDATORY(0),
    TNC_PRIVACY_MANDATORY(1),
    TNC_OPTIONAL(2),
    TNC_PRIVACY_OPTIONAL(3),
    NONE(-1)


}