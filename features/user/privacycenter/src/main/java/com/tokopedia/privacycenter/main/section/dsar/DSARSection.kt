package com.tokopedia.privacycenter.main.section.dsar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.SectionConsentwithdrawalBinding
import com.tokopedia.privacycenter.main.analytics.MainPrivacyCenterAnalytics
import com.tokopedia.privacycenter.main.section.BasePrivacyCenterSection

class DSARSection(
    val context: Context?
) : BasePrivacyCenterSection(context) {

    override val sectionViewBinding: ViewBinding = SectionConsentwithdrawalBinding.inflate(
        LayoutInflater.from(context)
    )
    override val sectionTextTitle: String? = context?.getString(R.string.dsar_section_title)
    override val sectionTextDescription: String? = context?.getString(R.string.dsar_section_description)
    override val isShowDirectionButton: Boolean = true

    override fun initObservers() {
        // no op
    }

    override fun onViewRendered() {
        showShimmering(false)
    }

    override fun onButtonDirectionClick(view: View) {
        MainPrivacyCenterAnalytics.sendClickOnButtonDownloadDataPribadiEvent()
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.DSAR)
        context?.startActivity(intent)
    }

    companion object {
        const val TAG = "DSARSection"
    }
}
