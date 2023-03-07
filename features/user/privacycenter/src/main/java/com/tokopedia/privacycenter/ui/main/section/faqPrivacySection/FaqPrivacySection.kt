package com.tokopedia.privacycenter.ui.main.section.faqPrivacySection

import android.content.Context
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.ui.main.section.BasePrivacyCenterSection
import com.tokopedia.url.TokopediaUrl

class FaqPrivacySection(private val context: Context?) : BasePrivacyCenterSection(context) {
    override val sectionTextTitle: String = context?.getString(R.string.privacy_center_faq_title).orEmpty()
    override val sectionTextDescription: String = context?.getString(R.string.privacy_center_faq_subtitle).orEmpty()
    override val isShowDirectionButton: Boolean = true

    private fun goToFaqPrivacyWebview() {
        RouteManager.route(
            context,
            ApplinkConstInternalGlobal.WEBVIEW,
            TokopediaUrl.getInstance().MOBILEWEB.plus(PATH_FAQ_PRIVACY)
        )
    }

    override fun onViewRendered() {
        showShimmering(false)
    }

    override fun onButtonDirectionClick(view: View) {
        goToFaqPrivacyWebview()
    }

    companion object {
        const val TAG = "FaqPrivacySection"
        private const val PATH_FAQ_PRIVACY = "faq?lang=id?isBack=true"
    }
}
