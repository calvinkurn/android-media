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
    @AttrRes defStyleAttr: Int = 0,
) : BaseCustomView(context, attrs, defStyleAttr) {

    val applink = 1
    val webLink = 2
    private var payLaterWidgetListener: PayLaterWidgetListener? = null
    private lateinit var layoutGopayBinding: LayoutGopayHomeWidgetBinding

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
        this.visibility = INVISIBLE
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
                    payLaterWidgetData.caseType.toString()
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
            payLaterWidgetListener?.onClosePayLaterWidget()
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
                analyticsUpload.sendWidgetAnalyticsEvent(
                    AnalyticsEventGenerator.WidgetCtaClickedButton(
                        payLaterWidgetData.caseType.toString(),
                        button.appsUrl ?: ""
                    )
                )
                RouteManager.route(context, button.appsUrl)
            }
            webLink -> {
                analyticsUpload.sendWidgetAnalyticsEvent(
                    AnalyticsEventGenerator.WidgetCtaClickedButton(
                        payLaterWidgetData.caseType.toString(),
                        button.webUrl ?: ""
                    )
                )
                val webViewAppLink =
                    ApplinkConst.WEBVIEW + "?url=" + button.webUrl
                RouteManager.route(context, webViewAppLink)
            }
        }
    }

}