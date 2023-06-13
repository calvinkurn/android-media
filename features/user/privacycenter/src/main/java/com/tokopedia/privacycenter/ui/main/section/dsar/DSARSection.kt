package com.tokopedia.privacycenter.ui.main.section.dsar

import android.content.Context
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.ui.main.analytics.MainPrivacyCenterAnalytics
import com.tokopedia.privacycenter.ui.main.section.BasePrivacyCenterSection

class DSARSection(
    val context: Context?
) : BasePrivacyCenterSection(context) {
    override val sectionTextTitle: String = context?.getString(R.string.dsar_section_title).orEmpty()
    override val sectionTextDescription: String = context?.getString(R.string.dsar_section_description).orEmpty()
    override val isShowDirectionButton: Boolean = true

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
