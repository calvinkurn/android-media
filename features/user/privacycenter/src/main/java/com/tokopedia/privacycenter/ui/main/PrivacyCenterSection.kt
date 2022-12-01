package com.tokopedia.privacycenter.ui.main

import android.widget.LinearLayout
import com.tokopedia.privacycenter.ui.main.section.BasePrivacyCenterSection
import com.tokopedia.privacycenter.ui.main.section.accountlinking.AccountLinkingSection
import com.tokopedia.privacycenter.ui.main.section.activity.ActivitySection
import com.tokopedia.privacycenter.ui.main.section.consentwithdrawal.ConsentWithdrawalSection
import com.tokopedia.privacycenter.ui.main.section.dsar.DSARSection
import com.tokopedia.privacycenter.ui.main.section.faqPrivacySection.FaqPrivacySection
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.PrivacyPolicySection
import com.tokopedia.privacycenter.ui.main.section.recommendation.RecommendationSection
import com.tokopedia.privacycenter.ui.main.section.tokopediacare.TokopediaCareSection

/**
 * ## Centralize Privacy Center Sections Initialize
 */

interface PrivacyCenterSectionDelegate {
    val accountLinkingSection: AccountLinkingSection
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

    fun privacyCenterSections(): MutableMap<String, BasePrivacyCenterSection> {
        /**
         * list position will reflect with section index
         */
        return mutableMapOf(
            AccountLinkingSection.TAG to delegate.accountLinkingSection,
            ActivitySection.TAG to delegate.activitySection,
            RecommendationSection.TAG to delegate.recommendationSection,
            ConsentWithdrawalSection.TAG to delegate.consentWithdrawalSection,
            DSARSection.TAG to delegate.dsarSection,
            PrivacyPolicySection.TAG to delegate.privacyPolicySection,
            FaqPrivacySection.TAG to delegate.faqPrivacySection,
            TokopediaCareSection.TAG to delegate.tokopediaCareSection
        )
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
