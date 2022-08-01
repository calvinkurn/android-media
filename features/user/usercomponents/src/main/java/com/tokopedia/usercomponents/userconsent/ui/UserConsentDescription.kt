package com.tokopedia.usercomponents.userconsent.ui

import android.graphics.Typeface
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.url.TokopediaUrl
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
    val textAgreementDefaultTncMandatory: String
    val textAgreementDefaultTncPolicyMandatory: String
    val textAgreementDefaultTncOptional: String
    val textAgreementDefaultTncPolicyOptional: String
    val unifyG500: Int

    fun openConsentPageDetail(pageId: String, type: String = "1", tab: String = "")
    fun openDefaultConsentPageDetail(url: String)
}

class UserConsentDescription constructor(
    private val delegate: UserConsentDescriptionDelegate,
    private val collectionDataModel: UserConsentCollectionDataModel.CollectionPointDataModel = UserConsentCollectionDataModel.CollectionPointDataModel()
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

    fun generateDefaultTemplateTnc(isMandatory: Boolean, tncPage: String = ""): SpannableString {
        val message = if (isMandatory) {
            delegate.textAgreementDefaultTncMandatory
        } else {
            delegate.textAgreementDefaultTncOptional
        }

        return SpannableString(String.format(message, delegate.textTermCondition)).apply {
            setSpan(
                createClickableSpannable {
                    delegate.openDefaultConsentPageDetail(
                        tncPage.ifEmpty { DEFAULT_TNC_PAGE }
                    )
                },
                this.indexOf(delegate.textTermCondition),
                this.indexOf(delegate.textTermCondition) + delegate.textTermCondition.length,
                0
            )
        }
    }

    fun generateDefaultTemplateTncPolicy(
        isMandatory: Boolean,
        tncPage: String = "",
        policyPage: String = ""
    ): SpannableString {
        val message = if (isMandatory) {
            delegate.textAgreementDefaultTncPolicyMandatory
        } else {
            delegate.textAgreementDefaultTncPolicyOptional
        }

        return SpannableString(String.format(message, delegate.textTermCondition, delegate.textPolicy)).apply {
            setSpan(
                createClickableSpannable {
                    delegate.openDefaultConsentPageDetail(
                        tncPage.ifEmpty { DEFAULT_TNC_PAGE }
                    )
                },
                this.indexOf(delegate.textTermCondition),
                this.indexOf(delegate.textTermCondition) + delegate.textTermCondition.length,
                0
            )

            setSpan(
                createClickableSpannable {
                    delegate.openDefaultConsentPageDetail(
                        policyPage.ifEmpty { DEFAULT_PRIVACY_PAGE }
                    )
                },
                this.indexOf(delegate.textPolicy),
                this.indexOf(delegate.textPolicy) + delegate.textPolicy.length,
                0
            )
        }
    }

    private fun createClickableSpannable(action: () -> Unit): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                action.invoke()
            }
            override fun updateDrawState(ds: TextPaint) {
                ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                ds.color = delegate.unifyG500
            }
        }
    }

    companion object {
        private val DEFAULT_TNC_PAGE = "${TokopediaUrl.getInstance().WEB}terms?lang=id"
        private val DEFAULT_PRIVACY_PAGE = "${TokopediaUrl.getInstance().WEB}privacy?lang=id"
    }
}