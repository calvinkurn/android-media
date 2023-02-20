package com.tokopedia.gopayhomewidget.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gopayhomewidget.analytics.AnalyticsEventGenerator
import com.tokopedia.gopayhomewidget.analytics.AnalyticsUpload
import com.tokopedia.gopayhomewidget.databinding.LayoutGopayHomeWidgetBinding
import com.tokopedia.gopayhomewidget.di.component.DaggerPayLaterHomeWidgetComponent
import com.tokopedia.gopayhomewidget.domain.data.PayLaterButton
import com.tokopedia.gopayhomewidget.domain.data.PayLaterWidgetData
import com.tokopedia.gopayhomewidget.presentation.listener.PayLaterWidgetListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class PayLaterWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    val applink = 1
    val webLink = 2
    private var payLaterWidgetListener: PayLaterWidgetListener? = null
    private lateinit var layoutGopayBinding: LayoutGopayHomeWidgetBinding
    private var caseMap = HashMap<Int, String>()

    @Inject
    lateinit var analyticsUpload: AnalyticsUpload

    @Inject
    lateinit var userSession: dagger.Lazy<UserSessionInterface>

    init {
        initInjector()
        initView()
    }

    private fun initInjector() {
        DaggerPayLaterHomeWidgetComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun initView() {
        layoutGopayBinding =
            LayoutGopayHomeWidgetBinding.inflate(LayoutInflater.from(context), this, true)
        this.visibility = GONE

        generateCaseMap()
    }

    /**
     * Function to populate map
     */
    private fun generateCaseMap() {
        caseMap[DUE_DATE_CASE_NUMBER] = DUE_DATE
        caseMap[FIRST_GRACE_CASE_NUMBER] = FIRST_GRACE
        caseMap[FIFTH_GRACE_CASE_NUMBER] = FIFTH_GRACE
        caseMap[D5_DUE_DATE_CASE_NUMBER] = D5_DUE_DATE
        caseMap[D2_DUE_DATE_CASE_NUMBER] = D2_DUE_DATE
        caseMap[D1_DUE_DATE_CASE_NUMBER] = D1_DUE_DATE
        caseMap[D0_DUE_DATE_CASE_NUMBER] = D0_DUE_DATE
        caseMap[DROP_OFF_CASE_NUMBER] = DROP_OFF
        caseMap[REJECTED_CASE_NUMBER] = REJECTED
    }

    /**
     * Function which returns case value
     * @param caseNumber  case key so map returns value accordingly
     */
    private fun getCaseDetail(caseNumber: Int): String? {
        return if (caseMap.containsKey(caseNumber)) {
            caseMap[caseNumber]
        } else {
            caseNumber.toString()
        }
    }

    fun setPayLaterWidgetListener(payLaterWidgetListener: PayLaterWidgetListener) {
        this.payLaterWidgetListener = payLaterWidgetListener
    }

    /**
     * Set data for the widget
     * @param payLaterWidgetData all the data for the widget
     */

    @SuppressLint("SetTextI18n")
    fun setData(payLaterWidgetData: PayLaterWidgetData) {
        if (payLaterWidgetData.isShow == true) {
            analyticsUpload.sendWidgetAnalyticsEvent(
                AnalyticsEventGenerator.WidgetImpressionAnalytics(
                    getCaseDetail(payLaterWidgetData.caseType ?: 0) ?: "",
                    payLaterWidgetData.gatewayCode ?: ""
                )
            )
            this.visibility = VISIBLE
            layoutGopayBinding.userName.text = "Hi, ${userSession.get().name}!"
            layoutGopayBinding.gopayDetail.text = payLaterWidgetData.description

            payLaterWidgetData.imageLight?.let { imageUrl ->
                layoutGopayBinding.gatewayIcon.setImageUrl(imageUrl)
            }
            layoutGopayBinding.proccedToGopay.text = payLaterWidgetData.button?.buttonName

            initListner(payLaterWidgetData)
        } else {
            this.visibility = GONE
        }
    }

    private fun initListner(payLaterWidgetData: PayLaterWidgetData) {
        layoutGopayBinding.proccedToGopay.setOnClickListener {
            payLaterWidgetData.button?.let { buttonDetail ->
                ctaLogic(
                    payLaterWidgetData,
                    buttonDetail
                )
            }
        }
        layoutGopayBinding.crossIcon.setOnClickListener {
            closeHomeWidget()
        }
    }

    /**
     * Implemented the button click logic
     * @param payLaterWidgetData widget data
     * @param button button data
     */

    private fun ctaLogic(
        payLaterWidgetData: PayLaterWidgetData,
        button: PayLaterButton
    ) {
        getCaseDetail(payLaterWidgetData.caseType ?: Int.ZERO)
        when (payLaterWidgetData.button?.ctaType) {
            applink -> {
                closeHomeWidget()
                analyticsUpload.sendWidgetAnalyticsEvent(
                    AnalyticsEventGenerator.WidgetCtaClickedButton(
                        getCaseDetail(payLaterWidgetData.caseType ?: Int.ZERO) ?: "",
                        button.appsUrl ?: "",
                        payLaterWidgetData.gatewayCode ?: ""
                    )
                )
                RouteManager.route(context, button.appsUrl)
            }
            webLink -> {
                closeHomeWidget()
                analyticsUpload.sendWidgetAnalyticsEvent(
                    AnalyticsEventGenerator.WidgetCtaClickedButton(
                        getCaseDetail(payLaterWidgetData.caseType ?: Int.ZERO) ?: "",
                        button.webUrl ?: "",
                        payLaterWidgetData.gatewayCode ?: ""
                    )
                )
                val webViewAppLink =
                    ApplinkConst.WEBVIEW + "?url=" + button.webUrl
                RouteManager.route(context, webViewAppLink)
            }
        }
    }

    private fun closeHomeWidget() {
        payLaterWidgetListener?.onClosePayLaterWidget()
    }

    companion object {
        const val DUE_DATE = "Due Date"
        const val FIRST_GRACE = "1st Grace"
        const val FIFTH_GRACE = "5th Grace"
        const val D5_DUE_DATE = "D-5 Due Date"
        const val D2_DUE_DATE = "D-2 Due Date"
        const val D1_DUE_DATE = "D-1 Due Date"
        const val D0_DUE_DATE = "D-0 Due Date"
        const val DROP_OFF = "Drop Off (Continue Apply)"
        const val REJECTED = "Rejected KYC (Can Reapply)"
        const val DUE_DATE_CASE_NUMBER = 11
        const val FIRST_GRACE_CASE_NUMBER = 12
        const val FIFTH_GRACE_CASE_NUMBER = 13
        const val D5_DUE_DATE_CASE_NUMBER = 50
        const val D2_DUE_DATE_CASE_NUMBER = 51
        const val D1_DUE_DATE_CASE_NUMBER = 52
        const val D0_DUE_DATE_CASE_NUMBER = 53
        const val DROP_OFF_CASE_NUMBER = 34
        const val REJECTED_CASE_NUMBER = 32
    }
}
