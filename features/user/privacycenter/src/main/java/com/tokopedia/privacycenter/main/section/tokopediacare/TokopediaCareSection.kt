package com.tokopedia.privacycenter.main.section.tokopediacare

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.BaseItemPrivacyCenterBinding
import com.tokopedia.privacycenter.main.analytics.MainPrivacyCenterAnalytics
import com.tokopedia.privacycenter.main.section.BasePrivacyCenterSection
import com.tokopedia.url.TokopediaUrl

class TokopediaCareSection(private val context: Context?) : BasePrivacyCenterSection(context) {

    override val sectionViewBinding: BaseItemPrivacyCenterBinding = BaseItemPrivacyCenterBinding.inflate(
        LayoutInflater.from(context)
    )
    override val sectionTextTitle: String =
        context?.getString(R.string.privacy_center_tokopedia_care_title).orEmpty()
    override val sectionTextDescription: String = ""
    override val isShowDirectionButton: Boolean = false
    override val isShowDivider: Boolean = false

    private fun goToTokopediaCare() {
        RouteManager.route(
            context,
            ApplinkConstInternalGlobal.WEBVIEW,
            TokopediaUrl.getInstance().MOBILEWEB.plus(PATH_TOKOPEDIA_CARE)
        )
    }

    override fun initObservers() {
        // none
    }

    override fun onViewRendered() {
        initListener()
        showShimmering(false)
        sectionViewBinding.itemViewPrivacyCenter.setIcon(IconUnify.CALL_CENTER)
        sectionViewBinding.itemViewPrivacyCenter.title =
            context?.getString(R.string.privacy_center_tokopedia_care_item_title).orEmpty()
    }

    private fun initListener() {
        sectionViewBinding.itemViewPrivacyCenter.setOnClickListener {
            MainPrivacyCenterAnalytics.sendClickOnButtonTokopediaCareEvent()
            goToTokopediaCare()
        }
    }

    override fun onButtonDirectionClick(view: View) {
        // none
    }

    companion object {
        const val TAG = "TokopediaCareSection"
        private const val PATH_TOKOPEDIA_CARE = "help?lang=id?isBack=true"
    }
}
