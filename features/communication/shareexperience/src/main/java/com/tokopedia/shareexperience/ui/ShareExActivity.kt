package com.tokopedia.shareexperience.ui

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNullOrBlank
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg
import com.tokopedia.shareexperience.ui.util.ShareExInitializer

class ShareExActivity : BaseSimpleActivity() {

    private var shareExInitializer: ShareExInitializer? = null

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.shareexperience_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustOrientation()
        init()
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    fun init() {
        val bundle = intent.extras
        if (shareExInitializer == null) {
            shareExInitializer = ShareExInitializer(this)
        }

        val defaultUrl = bundle?.getString(ShareExConst.Applink.Param.DEFAULT_URL).orEmpty()
        val pageType = bundle?.getString(ShareExConst.Applink.Param.PAGE_TYPE).orEmpty().toZeroStringIfNullOrBlank()
        val referralId = bundle?.getString(ShareExConst.Applink.Param.REFERRAL_ID).orEmpty()
        val utmCampaign = bundle?.getString(ShareExConst.Applink.Param.UTM_CAMPAIGN).orEmpty()
        val event_category = bundle?.getString(ShareExConst.Applink.Param.EVENT_CATEGORY).orEmpty()
        val defaultImpressionLabel = bundle?.getString(ShareExConst.Applink.Param.DEFAULT_IMPRESSION_LABEL).orEmpty()
        val defaultActionLabel = bundle?.getString(ShareExConst.Applink.Param.DEFAULT_ACTION_LABEL).orEmpty()

        val pageTypeEnum = ShareExPageTypeEnum.fromValueInt(pageType.toIntSafely())
        val trackerArg = ShareExTrackerArg(
            utmCampaign = utmCampaign,
            labelImpressionBottomSheet = defaultImpressionLabel,
            labelActionClickShareIcon = defaultActionLabel,
            labelActionCloseIcon = defaultActionLabel,
            labelActionClickChannel = defaultActionLabel,
            labelImpressionAffiliateRegistration = defaultImpressionLabel,
            labelActionClickAffiliateRegistration = defaultActionLabel
        )

        val args = ShareExBottomSheetArg.Builder(pageTypeEnum, defaultUrl, trackerArg)
            .withProductId("2151019476")
//            .withReferralId(referralId)
            .build()

        shareExInitializer?.openShareBottomSheet(args)
    }

}
