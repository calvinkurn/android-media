package com.tokopedia.shareexperience.ui.listener

interface ShareExBottomSheetListener {
    fun onSuccessCopyLink()
    fun refreshPage()
    fun onFailGenerateAffiliateLink(shareMessage: String)
}
