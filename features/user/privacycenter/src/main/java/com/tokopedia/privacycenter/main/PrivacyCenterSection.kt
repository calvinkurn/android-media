package com.tokopedia.privacycenter.main

import android.widget.LinearLayout
import com.tokopedia.privacycenter.main.section.BasePrivacyCenterSection
import com.tokopedia.privacycenter.main.section.consentwithdrawal.ConsentWithdrawalSection
import com.tokopedia.privacycenter.main.section.dummy.DummySection
import com.tokopedia.privacycenter.main.section.faqPrivacySection.FaqPrivacySection

/**
 * ## Centralize Privacy Center Sections Initialize
 */

interface PrivacyCenterSectionDelegate {
    val faqPrivacySection: FaqPrivacySection
    val dummySection: DummySection
    val consentWithdrawalSection: ConsentWithdrawalSection
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
            FaqPrivacySection.TAG to  delegate.faqPrivacySection,
            DummySection.TAG to delegate.dummySection,
            ConsentWithdrawalSection.TAG to delegate.consentWithdrawalSection,
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
