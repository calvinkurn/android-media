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
        caseMap[11] = DUE_DATE
        caseMap[12] = FIRST_GRACE
        caseMap[13] = FITH_GRACE
        caseMap[50] = D5_DUE_DATE
        caseMap[51] = D2_DUE_DATE
        caseMap[52] = D1_DUE_DATE
        caseMap[53] = D0_DUE_DATE
        caseMap[34] = DROP_OFF
        caseMap[32] = REJECTED
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
        when (payLaterWidgetData.button?.ctaType) {
            applink -> {
                closeHomeWidget()
                analyticsUpload.sendWidgetAnalyticsEvent(
                    AnalyticsEventGenerator.WidgetCtaClickedButton(
                        getCaseDetail(payLaterWidgetData.caseType ?: 0) ?: "",
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
                        getCaseDetail(payLaterWidgetData.caseType ?: 0) ?: "",
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
        const val FITH_GRACE = "5th Grace"
        const val D5_DUE_DATE = "D-5 Due Date"
        const val D2_DUE_DATE = "D-2 Due Date"
        const val D1_DUE_DATE = "D-1 Due Date"
        const val D0_DUE_DATE = "D-0 Due Date"
        const val DROP_OFF = "Drop Off (Continue Apply)"
        const val REJECTED = "Rejected KYC (Can Reapply)"
    }
}
