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
        var args: ShareExBottomSheetArg? = null
        intent.extras?.let {
            val defaultUrl = it.getString(ShareExConst.Applink.Param.DEFAULT_URL).orEmpty()
            val pageType = it.getString(ShareExConst.Applink.Param.PAGE_TYPE).orEmpty().toZeroStringIfNullOrBlank()
            val pageTypeEnum = ShareExPageTypeEnum.fromValueInt(pageType.toIntSafely())

            val utmCampaign = it.getString(ShareExConst.Applink.Param.UTM_CAMPAIGN).orEmpty()
            val event_category = it.getString(ShareExConst.Applink.Param.EVENT_CATEGORY).orEmpty()
            val defaultImpressionLabel = it.getString(ShareExConst.Applink.Param.DEFAULT_IMPRESSION_LABEL).orEmpty()
            val defaultActionLabel = it.getString(ShareExConst.Applink.Param.DEFAULT_ACTION_LABEL).orEmpty()

            val trackerArg = ShareExTrackerArg(
                utmCampaign = utmCampaign,
                labelImpressionBottomSheet = defaultImpressionLabel,
                labelActionClickShareIcon = defaultActionLabel,
                labelActionCloseIcon = defaultActionLabel,
                labelActionClickChannel = defaultActionLabel,
                labelImpressionAffiliateRegistration = defaultImpressionLabel,
                labelActionClickAffiliateRegistration = defaultActionLabel
            )

            val argsBuilder = ShareExBottomSheetArg.Builder(pageTypeEnum, defaultUrl, trackerArg)

            it.getString(ShareExConst.Applink.Param.PRODUCT_ID)?.let {
                argsBuilder.withProductId(it)
            }
            it.getString(ShareExConst.Applink.Param.CAMPAIGN_ID)?.let {
                argsBuilder.withCampaignId(it)
            }
            it.getString(ShareExConst.Applink.Param.SHOP_ID)?.let {
                argsBuilder.withShopId(it)
            }
            it.getString(ShareExConst.Applink.Param.REVIEW_ID)?.let {
                argsBuilder.withReviewId(it)
            }
            it.getString(ShareExConst.Applink.Param.ATTACHMENT_ID)?.let {
                argsBuilder.withAttachmentId(it)
            }

            val metadata = mutableMapOf<String, String>()
            it.getString(ShareExConst.Applink.Param.REFERRAL_CODE)?.let {
                metadata.put(ShareExConst.Applink.Param.REFERRAL_CODE, it)
            }
            argsBuilder.withMetadata(metadata)

            args = argsBuilder.build()
        }

        if (args != null) {
            if (shareExInitializer == null) {
                shareExInitializer = ShareExInitializer(this)
            }
            shareExInitializer?.openShareBottomSheet(args!!)
        } else {
            finish()
        }
    }
}
