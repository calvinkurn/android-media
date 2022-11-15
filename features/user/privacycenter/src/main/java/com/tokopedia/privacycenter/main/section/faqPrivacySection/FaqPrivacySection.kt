package com.tokopedia.privacycenter.main.section.faqPrivacySection

import android.content.Context
import android.view.View
import androidx.viewbinding.ViewBinding
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.main.section.BasePrivacyCenterSection
import com.tokopedia.url.TokopediaUrl

class FaqPrivacySection(context: Context?) : BasePrivacyCenterSection(context) {
    override val sectionViewBinding: ViewBinding? = null
    override val sectionTextTitle: String = context?.getString(R.string.privacy_center_faq_title).orEmpty()
    override val sectionTextDescription: String = context?.getString(R.string.privacy_center_faq_subtitle).orEmpty()
    override val isShowDirectionButton: Boolean = true

    private val baseContext = context

    private fun goToFaqPrivacyWebview() {
        RouteManager.route(
            baseContext,
            ApplinkConstInternalGlobal.WEBVIEW,
            TokopediaUrl.getInstance().MOBILEWEB.plus(PATH_FAQ_PRIVACY)
        )
    }

    override fun initObservers() {
        //none
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
