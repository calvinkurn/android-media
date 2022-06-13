package com.tokopedia.usercomponents.userconsent.ui

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.usercomponents.R
import com.tokopedia.usercomponents.userconsent.analytics.UserConsentAnalytics
import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.PRIVACY

class UserConsentDescription constructor(
    private val context: Context,
    private val collectionDataModel: UserConsentCollectionDataModel.CollectionPointDataModel
) {

    private val userConsentAnalytics = UserConsentAnalytics()

    private val textTermCondition by lazy {
        context.resources.getString(R.string.user_consent_term_condition)
    }

    private val textPolicy by lazy {
        context.resources.getString(R.string.user_consent_policy)
    }

    fun generateTermConditionSinglePurposeText(
        isMandatory: Boolean,
        policyNoticeTnCPageID: String,
        purposeText: String
    ): SpannableString {
        val message = if(isMandatory) {
            context.resources.getString(R.string.user_consent_agreement_single_purpose_term_condition_mandatory)
        } else {
            context.resources.getString(R.string.user_consent_agreement_single_purpose_term_condition)
        }

        return SpannableString(String.format(message, textTermCondition, purposeText)).apply {
            setSpan(
                createClickableSpannable {
                    collectionDataModel.purposes.let {
                        userConsentAnalytics.trackOnTnCHyperLinkClicked(it)
                    }

                    openConsentPageDetail(policyNoticeTnCPageID)
                },
                this.indexOf(textTermCondition),
                this.indexOf(textTermCondition) + textTermCondition.length,
                0
            )
        }
    }

    fun generateTermConditionPolicySinglePurposeText(
        isMandatory: Boolean,
        policyNoticeTnCPageID: String,
        policyNoticePolicyPageID: String,
        purposeText: String
    ): SpannableString {
        val message = if (isMandatory) {
            context.resources.getString(R.string.user_consent_agreement_single_purpose_term_condition_policy_mandatory)
        } else {
            context.resources.getString(R.string.user_consent_agreement_single_purpose_term_condition_policy)
        }
        return SpannableString(String.format(message, textTermCondition, textPolicy, purposeText)).apply {
            setSpan(
                createClickableSpannable {
                    collectionDataModel.purposes.let {
                        userConsentAnalytics.trackOnTnCHyperLinkClicked(it)
                    }

                    openConsentPageDetail(policyNoticeTnCPageID)
                },
                this.indexOf(textTermCondition),
                this.indexOf(textTermCondition) + textTermCondition.length,
                0
            )

            setSpan(
                createClickableSpannable {
                    collectionDataModel.purposes.let {
                        userConsentAnalytics.trackOnPolicyHyperLinkClicked(it)
                    }

                    openConsentPageDetail(policyNoticePolicyPageID, tab = PRIVACY)
                },
                this.indexOf(textPolicy),
                this.indexOf(textPolicy) + textPolicy.length,
                0
            )
        }
    }

    fun generateTermConditionMultipleOptionalPurposeText(
        policyNoticeTnCPageID: String
    ): SpannableString {
        val message = context.resources.getString(R.string.user_consent_agreement_multiple_purpose_term_condition_optional)
        return SpannableString(String.format(message, textTermCondition)).apply {
            setSpan(
                createClickableSpannable {
                    collectionDataModel.purposes.let {
                        userConsentAnalytics.trackOnTnCHyperLinkClicked(it)
                    }

                    openConsentPageDetail(policyNoticeTnCPageID)
                },
                this.indexOf(textTermCondition),
                this.indexOf(textTermCondition) + textTermCondition.length,
                0
            )
        }
    }

    fun generateTermConditionPolicyMultipleOptionalPurposeText(
        policyNoticeTnCPageID: String,
        policyNoticePolicyPageID: String
    ): SpannableString {
        val message = context.resources.getString(R.string.user_consent_agreement_multiple_purpose_term_condition_policy_optional)
        return SpannableString(String.format(message, textTermCondition, textPolicy)).apply {
            setSpan(
                createClickableSpannable {
                    collectionDataModel.purposes.let {
                        userConsentAnalytics.trackOnTnCHyperLinkClicked(it)
                    }

                    openConsentPageDetail(policyNoticeTnCPageID)
                },
                this.indexOf(textTermCondition),
                this.indexOf(textTermCondition) + textTermCondition.length,
                0
            )

            setSpan(
                createClickableSpannable {
                    collectionDataModel.purposes.let {
                        userConsentAnalytics.trackOnPolicyHyperLinkClicked(it)
                    }

                    openConsentPageDetail(policyNoticePolicyPageID, tab = PRIVACY)
                },
                this.indexOf(textPolicy),
                this.indexOf(textPolicy) + textPolicy.length,
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
                ds.color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            }
        }
    }

    private fun openConsentPageDetail(pageId: String, type: String = "1", tab: String = "") {
        val url = String.format(UserConsentConst.URL_CONSENT_DETAIL, pageId, type, tab)
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, url)
        context.startActivity(intent)
    }
}