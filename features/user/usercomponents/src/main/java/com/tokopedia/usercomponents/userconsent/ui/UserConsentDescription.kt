package com.tokopedia.usercomponents.userconsent.ui

import android.graphics.Typeface
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.usercomponents.userconsent.analytics.UserConsentAnalytics
import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.PRIVACY

interface UserConsentDescriptionDelegate {
    val textTermCondition: String
    val textPolicy: String
    val textAgreementSingleTncOptional: String
    val textAgreementSingleTncMandatory: String
    val textAgreementSingleTncPolicyOptional: String
    val textAgreementSingleTncPolicyMandatory: String
    val textAgreementMultiTnc: String
    val textAgreementMultiTncPolicy: String
    val unifyG500: Int

    fun openConsentPageDetail(pageId: String, type: String = "1", tab: String = "")
}

class UserConsentDescription constructor(
    private val delegate: UserConsentDescriptionDelegate,
    private val collectionDataModel: UserConsentCollectionDataModel.CollectionPointDataModel
) {

    private val userConsentAnalytics = UserConsentAnalytics()

    fun generateTermConditionSinglePurposeText(
        isChecklist: Boolean,
        purposeText: String
    ): SpannableString {
        val message = if(isChecklist) {
            delegate.textAgreementSingleTncOptional
        } else {
            delegate.textAgreementSingleTncMandatory
        }

        return SpannableString(String.format(message, delegate.textTermCondition, purposeText)).apply {
            setSpan(
                createClickableSpannable {
                    with(collectionDataModel) {
                        purposes.let {
                            userConsentAnalytics.trackOnTnCHyperLinkClicked(it)
                        }

                        delegate.openConsentPageDetail(attributes.policyNoticeTnCPageID)
                    }
                },
                this.indexOf(delegate.textTermCondition),
                this.indexOf(delegate.textTermCondition) + delegate.textTermCondition.length,
                0
            )
        }
    }

    fun generateTermConditionPolicySinglePurposeText(
        isChecklist: Boolean,
        purposeText: String
    ): SpannableString {
        val message = if (isChecklist) {
            delegate.textAgreementSingleTncPolicyOptional
        } else {
            delegate.textAgreementSingleTncPolicyMandatory
        }

        return SpannableString(String.format(message, delegate.textTermCondition, delegate.textPolicy, purposeText)).apply {
            setSpan(
                createClickableSpannable {
                    with(collectionDataModel) {
                        purposes.let {
                            userConsentAnalytics.trackOnTnCHyperLinkClicked(it)
                        }

                        delegate.openConsentPageDetail(attributes.policyNoticeTnCPageID)
                    }
                },
                this.indexOf(delegate.textTermCondition),
                this.indexOf(delegate.textTermCondition) + delegate.textTermCondition.length,
                0
            )

            setSpan(
                createClickableSpannable {
                    with(collectionDataModel) {
                        purposes.let {
                            userConsentAnalytics.trackOnPolicyHyperLinkClicked(it)
                        }

                        delegate.openConsentPageDetail(attributes.policyNoticePolicyPageID, tab = PRIVACY)
                    }
                },
                this.indexOf(delegate.textPolicy),
                this.indexOf(delegate.textPolicy) + delegate.textPolicy.length,
                0
            )
        }
    }

    fun generateTermConditionMultipleOptionalPurposeText(): SpannableString {
        val message = delegate.textAgreementMultiTnc
        return SpannableString(String.format(message, delegate.textTermCondition)).apply {
            setSpan(
                createClickableSpannable {
                    with(collectionDataModel) {
                        purposes.let {
                            userConsentAnalytics.trackOnTnCHyperLinkClicked(it)
                        }

                        delegate.openConsentPageDetail(attributes.policyNoticeTnCPageID)
                    }
                },
                this.indexOf(delegate.textTermCondition),
                this.indexOf(delegate.textTermCondition) + delegate.textTermCondition.length,
                0
            )
        }
    }

    fun generateTermConditionPolicyMultipleOptionalPurposeText(): SpannableString {
        val message = delegate.textAgreementMultiTncPolicy
        return SpannableString(String.format(message, delegate.textTermCondition, delegate.textPolicy)).apply {
            setSpan(
                createClickableSpannable {
                    with(collectionDataModel) {
                        purposes.let {
                            userConsentAnalytics.trackOnTnCHyperLinkClicked(it)
                        }

                        delegate.openConsentPageDetail(attributes.policyNoticeTnCPageID)
                    }
                },
                this.indexOf(delegate.textTermCondition),
                this.indexOf(delegate.textTermCondition) + delegate.textTermCondition.length,
                0
            )

            setSpan(
                createClickableSpannable {
                    with(collectionDataModel) {
                        purposes.let {
                            userConsentAnalytics.trackOnPolicyHyperLinkClicked(it)
                        }

                        delegate.openConsentPageDetail(attributes.policyNoticePolicyPageID, tab = PRIVACY)
                    }
                },
                this.indexOf(delegate.textPolicy),
                this.indexOf(delegate.textPolicy) + delegate.textPolicy.length,
                0
            )
        }
    }

    private fun createClickableSpannable(action: () -> Unit): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) { action.invoke() }
            override fun updateDrawState(ds: TextPaint) {
                ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                ds.color = delegate.unifyG500
            }
        }
    }
}