package com.tokopedia.privacycenter.main

import android.widget.LinearLayout
import com.tokopedia.privacycenter.main.section.BasePrivacyCenterSection
import com.tokopedia.privacycenter.main.section.consentwithdrawal.ConsentWithdrawalSection
import com.tokopedia.privacycenter.main.section.dummy.DummySection
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicySection

/**
 * ## Centralize Privacy Center Sections Initialize
 */

interface PrivacyCenterSectionDelegate {
    val dummySection: DummySection
    val consentWithdrawalSection: ConsentWithdrawalSection
    val privacyPolicySection: PrivacyPolicySection
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
            DummySection.TAG to delegate.dummySection,
            ConsentWithdrawalSection.TAG to delegate.consentWithdrawalSection,
            PrivacyPolicySection.TAG to delegate.privacyPolicySection,
        )
    }

    fun renderSections() {
        privacyCenterSections().forEach {
            parentView?.addView(it.value.getView())
        }
    }

    fun removeAllViews() {
        parentView?.removeAllViews()

        delegate.dummySection.lifecycleOwner = null
        delegate.consentWithdrawalSection.lifecycleOwner = null
        delegate.privacyPolicySection.lifecycleOwner = null
    }
}
