package com.tokopedia.usercomponents.userconsent.ui

sealed class ConsentType{
    class Info: ConsentType()
    class SingleConsent: ConsentType()
    class MultiConsent: ConsentType()
}
