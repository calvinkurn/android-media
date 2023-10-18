package com.tokopedia.usercomponents.userconsent.ui

import android.graphics.Typeface
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usercomponents.userconsent.analytics.UserConsentAnalytics
import com.tokopedia.usercomponents.userconsent.common.AttributesItem
import com.tokopedia.usercomponents.userconsent.common.CollectionPointDataModel
import com.tokopedia.usercomponents.userconsent.common.StatementWording

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
    fun openWebview(url: String)
}

class UserConsentDescription constructor(
    private val delegate: UserConsentDescriptionDelegate,
    private val collectionDataModel: CollectionPointDataModel = CollectionPointDataModel(),
    private val collectionId: String
) {

    private val userConsentAnalytics = UserConsentAnalytics()

    fun generateDescriptionSpannableText(
        statementWording: StatementWording
    ): SpannableString {
        val customText = replaceText(statementWording)
        return setUpSpannable(customText, statementWording.attributes)
    }

    private fun replaceText(statementWording: StatementWording): String {
        var textResult = statementWording.template

        statementWording.attributes.forEach {
            textResult = textResult.replace(it.key, it.text)
        }

        return textResult
    }

    private fun setUpSpannable(text: String, listSpannable: List<AttributesItem>): SpannableString {
        val spannable = SpannableString(text)

        listSpannable.forEach {
            if (it.link.isNotEmpty()) {
                spannable.setSpan(
                    createClickableSpannable {
                        with(collectionDataModel) {
                            purposes.let { purposes ->
                                when(it.key) {
                                    KEY_TNC -> {
                                        userConsentAnalytics.trackOnTnCHyperLinkClicked(
                                            purposes = purposes,
                                            collectionId = collectionId
                                        )
                                    }
                                    KEY_PRIVACY -> {
                                        userConsentAnalytics.trackOnPolicyHyperLinkClicked(
                                            purposes = purposes,
                                            collectionId = collectionId
                                        )
                                    }
                                }

                            }

                            delegate.openWebview(it.link)
                        }
                    },
                    spannable.indexOf(it.text),
                    spannable.indexOf(it.text) + it.text.length,
                    0
                )
            }
        }

        return spannable
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
                    delegate.openWebview(
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
                    delegate.openWebview(
                        tncPage.ifEmpty { DEFAULT_TNC_PAGE }
                    )
                },
                this.indexOf(delegate.textTermCondition),
                this.indexOf(delegate.textTermCondition) + delegate.textTermCondition.length,
                0
            )

            setSpan(
                createClickableSpannable {
                    delegate.openWebview(
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
        private const val KEY_TNC = "#tnc"
        private const val KEY_PRIVACY = "#privacy"
        private val DEFAULT_TNC_PAGE = "${TokopediaUrl.getInstance().WEB}terms?lang=id"
        private val DEFAULT_PRIVACY_PAGE = "${TokopediaUrl.getInstance().WEB}privacy?lang=id"
    }
}
