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
            val argsBuilder = initializeMandatoryArgs(it)

            // set feature related id
            setFeatureIds(argsBuilder, it)

            // set BU metadata
            setMetadata(argsBuilder, it)

            args = argsBuilder.build()
        }

        if (args != null) {
            if (shareExInitializer == null) {
                shareExInitializer = ShareExInitializer(this)
            }
            shareExInitializer?.openShareBottomSheet(args!!)
            shareExInitializer?.setOnDismissListener {
                finish()
            }
        } else {
            finish()
        }
    }

    private fun setMetadata(argsBuilder: ShareExBottomSheetArg.Builder, bundle: Bundle) {
        val metadata = mutableMapOf<String, String>()
        with(bundle) {
            getString(ShareExConst.Applink.Param.REFERRAL_CODE)?.let {
                metadata.put(ShareExConst.Applink.Param.REFERRAL_CODE, it)
            }
        }

        argsBuilder.withMetadata(metadata)
    }

    private fun setFeatureIds(argsBuilder: ShareExBottomSheetArg.Builder, bundle: Bundle) {
        with(bundle) {
            getString(ShareExConst.Applink.Param.PRODUCT_ID)?.let {
                argsBuilder.withProductId(it)
            }
            getString(ShareExConst.Applink.Param.CAMPAIGN_ID)?.let {
                argsBuilder.withCampaignId(it)
            }
            getString(ShareExConst.Applink.Param.SHOP_ID)?.let {
                argsBuilder.withShopId(it)
            }
            getString(ShareExConst.Applink.Param.REVIEW_ID)?.let {
                argsBuilder.withReviewId(it)
            }
            getString(ShareExConst.Applink.Param.ATTACHMENT_ID)?.let {
                argsBuilder.withAttachmentId(it)
            }
        }
    }

    private fun initializeMandatoryArgs(bundle: Bundle): ShareExBottomSheetArg.Builder {
        val defaultUrl = bundle.getString(ShareExConst.Applink.Param.DEFAULT_URL).orEmpty()
        val pageType = bundle.getString(ShareExConst.Applink.Param.PAGE_TYPE).orEmpty().toZeroStringIfNullOrBlank()
        val pageTypeEnum = ShareExPageTypeEnum.fromValueInt(pageType.toIntSafely())

        val utmCampaign = bundle.getString(ShareExConst.Applink.Param.UTM_CAMPAIGN).orEmpty()
        val labelImpressionBottomSheet = bundle.getString(ShareExConst.Applink.Param.LABEL_IMPRESSION_BOTTOMSHEET).orEmpty()
        val labelActionClickShareIcon = bundle.getString(ShareExConst.Applink.Param.LABEL_ACTION_CLICK_SHARE_ICON).orEmpty()
        val labelActionCloseIcon = bundle.getString(ShareExConst.Applink.Param.LABEL_ACTION_CLICK_CLOSE_ICON).orEmpty()
        val labelActionClickChannel = bundle.getString(ShareExConst.Applink.Param.LABEL_ACTION_CLICK_CHANNEL).orEmpty()
        val labelImpressionAffiliateRegistration = bundle.getString(ShareExConst.Applink.Param.LABEL_IMPRESSION_AFFILIATE_REGISTRATION).orEmpty()
        val labelActionClickAffiliateRegistration = bundle.getString(ShareExConst.Applink.Param.LABEL_ACTION_CLICK_AFFILIATE_REGISTRATION).orEmpty()

        val trackerArg = ShareExTrackerArg(
            utmCampaign = utmCampaign,
            labelImpressionBottomSheet = labelImpressionBottomSheet,
            labelActionClickShareIcon = labelActionClickShareIcon,
            labelActionCloseIcon = labelActionCloseIcon,
            labelActionClickChannel = labelActionClickChannel,
            labelImpressionAffiliateRegistration = labelImpressionAffiliateRegistration,
            labelActionClickAffiliateRegistration = labelActionClickAffiliateRegistration
        )

        return ShareExBottomSheetArg.Builder(pageTypeEnum, defaultUrl, trackerArg)
    }
}
