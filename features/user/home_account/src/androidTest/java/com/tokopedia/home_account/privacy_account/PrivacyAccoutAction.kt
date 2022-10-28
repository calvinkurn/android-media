package com.tokopedia.home_account.privacy_account

import com.tokopedia.home_account.privacy_account.common.isDisplayed
import com.tokopedia.home_account.privacy_account.common.isGoneView
import com.tokopedia.home_account.privacy_account.common.isTextDisplayed
import com.tokopedia.home_account.test.R

fun isAccountLinkingDisplayed() {
    isDisplayed(
        R.id.txt_header_link_account,
        R.id.txt_desc_link_account,
        R.id.layout_item_link_account,
        R.id.privacyItemImage,
        R.id.privacyItemTitle,
        R.id.privacyItemDescription,
        R.id.privacyItemImageButton
    )
    isGoneView(R.id.privacyItemTextButton)
}

fun isAccountNotLinkingDisplayed() {
    isDisplayed(
        R.id.txt_header_link_account,
        R.id.txt_desc_link_account,
        R.id.layout_item_link_account,
        R.id.privacyItemImage,
        R.id.privacyItemTitle,
        R.id.privacyItemDescription
    )
    isGoneView(R.id.privacyItemImageButton)
    isTextDisplayed("Sambungin Akun")
}
