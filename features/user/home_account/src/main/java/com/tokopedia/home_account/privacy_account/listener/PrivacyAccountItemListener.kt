package com.tokopedia.home_account.privacy_account.listener

/**
 * Created by Yoris on 04/08/21.
 */

interface AccountItemListener {
    fun onLinkAccountClicked()
    fun onViewAccountClicked()
}

interface PrivacyAccountListener {
    fun onConsentGroupClicked(id: String)
}
