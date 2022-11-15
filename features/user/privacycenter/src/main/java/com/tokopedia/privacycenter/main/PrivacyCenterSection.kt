package com.tokopedia.privacycenter.main

import android.widget.LinearLayout
import com.tokopedia.privacycenter.main.section.BasePrivacyCenterSection
import com.tokopedia.privacycenter.main.section.accountlinking.AccountLinkingSection
import com.tokopedia.privacycenter.main.section.consentwithdrawal.ConsentWithdrawalSection
import com.tokopedia.privacycenter.main.section.dummy.DummySection
import com.tokopedia.privacycenter.main.section.tokopediacare.TokopediaCareSection
import com.tokopedia.privacycenter.main.section.faqPrivacySection.FaqPrivacySection
import com.tokopedia.privacycenter.main.section.recommendation.RecommendationSection

/**
 * ## Centralize Privacy Center Sections Initialize
 */

interface PrivacyCenterSectionDelegate {
    val tokopediaCareSection: TokopediaCareSection
    val accountLinkingSection: AccountLinkingSection
    val dummySection: DummySection
    val recommendationSection: RecommendationSection
    val consentWithdrawalSection: ConsentWithdrawalSection
    val faqPrivacySection: FaqPrivacySection
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
            TokopediaCareSection.TAG to delegate.tokopediaCareSection,
            DummySection.TAG to delegate.dummySection,
            RecommendationSection.TAG to delegate.recommendationSection,
            ConsentWithdrawalSection.TAG to delegate.consentWithdrawalSection,
            FaqPrivacySection.TAG to delegate.faqPrivacySection
        )
    }

    fun renderSections() {
        privacyCenterSections().forEach {
            parentView?.addView(it.value.getView())
        }
    }

    fun removeAllViews() {
        parentView?.removeAllViews()
    }
}
