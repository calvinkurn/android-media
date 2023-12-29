package com.tokopedia.privacycenter.ui.main

import android.widget.LinearLayout
import com.tokopedia.privacycenter.common.PrivacyCenterConst
import com.tokopedia.privacycenter.ui.main.section.BasePrivacyCenterSection
import com.tokopedia.privacycenter.ui.main.section.activity.ActivitySection
import com.tokopedia.privacycenter.ui.main.section.consentwithdrawal.ConsentWithdrawalSection
import com.tokopedia.privacycenter.ui.main.section.dsar.DSARSection
import com.tokopedia.privacycenter.ui.main.section.faqPrivacySection.FaqPrivacySection
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.PrivacyPolicySection
import com.tokopedia.privacycenter.ui.main.section.recommendation.RecommendationSection
import com.tokopedia.privacycenter.ui.main.section.tokopediacare.TokopediaCareSection
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

/**
 * ## Centralize Privacy Center Sections Initialize
 */

interface PrivacyCenterSectionDelegate {
    val activitySection: ActivitySection
    val recommendationSection: RecommendationSection
    val consentWithdrawalSection: ConsentWithdrawalSection
    val privacyPolicySection: PrivacyPolicySection
    val dsarSection: DSARSection
    val faqPrivacySection: FaqPrivacySection
    val tokopediaCareSection: TokopediaCareSection
}

class PrivacyCenterSection constructor(
    private val parentView: LinearLayout?,
    private val delegate: PrivacyCenterSectionDelegate
) {

    /**
     * list position will reflect with section index
     */
    fun privacyCenterSections(): MutableMap<String, BasePrivacyCenterSection> {
        return removeSectionByConfig(
            mutableMapOf(
                ActivitySection.TAG to delegate.activitySection,
                RecommendationSection.TAG to delegate.recommendationSection,
                ConsentWithdrawalSection.TAG to delegate.consentWithdrawalSection,
                DSARSection.TAG to delegate.dsarSection,
                PrivacyPolicySection.TAG to delegate.privacyPolicySection,
                FaqPrivacySection.TAG to delegate.faqPrivacySection,
                TokopediaCareSection.TAG to delegate.tokopediaCareSection
            )
        )
    }

    private fun removeSectionByConfig(sections: MutableMap<String, BasePrivacyCenterSection>): MutableMap<String, BasePrivacyCenterSection> {
        val remoteConfig = FirebaseRemoteConfigImpl(parentView?.context)
        val isEnableDsar = remoteConfig.getBoolean(PrivacyCenterConst.DSAR_REMOTE_CONFIG_KEY)
        if (!isEnableDsar) {
            sections.remove(DSARSection.TAG)
        }
        return sections
    }

    fun renderSections() {
        privacyCenterSections().forEach {
            parentView?.addView(it.value.getView())
        }
    }

    fun removeAllViews() {
        parentView?.removeAllViews()

        delegate.consentWithdrawalSection.lifecycleOwner = null
        delegate.privacyPolicySection.lifecycleOwner = null
    }
}
