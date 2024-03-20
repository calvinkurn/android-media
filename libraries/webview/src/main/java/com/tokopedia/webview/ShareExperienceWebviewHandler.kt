package com.tokopedia.webview

import android.content.Context
import android.net.Uri
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum.Companion.fromValue
import com.tokopedia.shareexperience.ui.ShareExConst
import com.tokopedia.shareexperience.ui.ShareExConst.Applink.DUMMY_APPLINK
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg
import com.tokopedia.shareexperience.ui.util.ShareExInitializer

object ShareExperienceWebviewHandler {

    private var shareExInitializer: ShareExInitializer? = null

    fun init(context: Context, urix: Uri) {
        val uri = Uri.parse(DUMMY_APPLINK)
        if (shareExInitializer == null) {
            shareExInitializer = ShareExInitializer(context)
        }

        val defaultUrl: String = uri.getQueryParameter(ShareExConst.Applink.Param.DEFAULT_URL).orEmpty()
        val pageType: String = uri.getQueryParameter(ShareExConst.Applink.Param.PAGE_TYPE) ?: "0"
        val referralId: String = uri.getQueryParameter(ShareExConst.Applink.Param.REFERRAL_ID).orEmpty()
        val defaultImpressionLabel: String = uri.getQueryParameter(ShareExConst.Applink.Param.DEFAULT_IMPRESSION_LABEL).orEmpty()
        val defaultActionLabel: String = uri.getQueryParameter(ShareExConst.Applink.Param.DEFAULT_ACTION_LABEL).orEmpty()

        val pageTypeEnum = fromValue(pageType)
        val trackerArg = ShareExTrackerArg(
            utmCampaign = "pdp-${ShareExTrackerArg.SHARE_ID_KEY}-2151019476",
            labelImpressionBottomSheet = defaultImpressionLabel,
            labelActionClickShareIcon = defaultActionLabel,
            labelActionCloseIcon = defaultActionLabel,
            labelActionClickChannel = defaultActionLabel,
            labelImpressionAffiliateRegistration = defaultImpressionLabel,
            labelActionClickAffiliateRegistration = defaultActionLabel
        )

        val args = ShareExBottomSheetArg.Builder(ShareExPageTypeEnum.PDP, defaultUrl, trackerArg)
            .withProductId("2151019476")
//            .withReferralId(referralId)
            .build()

        shareExInitializer?.openShareBottomSheet(args)
    }
}
