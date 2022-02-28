package com.tokopedia.gopayhomewidget.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gopayhomewidget.analytics.AnalyticsUpload
import com.tokopedia.gopayhomewidget.databinding.LayoutGopayHomeWidgetBinding
import com.tokopedia.gopayhomewidget.di.component.DaggerPayLaterHomeWidgetComponent
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

    @SuppressLint("SetTextI18n")
    fun setData(payLaterWidgetData: PayLaterWidgetData) {
        if (payLaterWidgetData.isShow == true) {
            //   analyticsUpload.sendWidgetAnalyticsEvent(AnalyticsEventGenerator.WidgetImpressionAnalytics(payLaterWidgetData.description))
            this.visibility = VISIBLE
            layoutGopayBinding.userName.text = "${userSession.get().name}!"
            layoutGopayBinding.gopayDetail.text = payLaterWidgetData.description
            payLaterWidgetData.imageLight?.let { imageUrl ->
                layoutGopayBinding.gatewayIcon.setImageUrl(imageUrl)
            }
            layoutGopayBinding.proccedToGopay.text = payLaterWidgetData.button?.buttonName
            layoutGopayBinding.proccedToGopay.setOnClickListener {
                when (payLaterWidgetData.button?.ctaType) {
                    1 -> {
                        //    analyticsUpload.sendWidgetAnalyticsEvent(AnalyticsEventGenerator.WidgetCtaClickedButton(payLaterWidgetData.description,payLaterWidgetData.button.appsUrl))
                        RouteManager.route(context, payLaterWidgetData.button.appsUrl)
                    }
                    2 -> {
                        //    analyticsUpload.sendWidgetAnalyticsEvent(AnalyticsEventGenerator.WidgetCtaClickedButton(payLaterWidgetData.description,payLaterWidgetData.button.weburl))
                        val webViewAppLink =
                                ApplinkConst.WEBVIEW + "?url=" + payLaterWidgetData.button.webUrl
                        RouteManager.route(context, webViewAppLink)
                    }
                }
            }
            layoutGopayBinding.crossIcon.setOnClickListener {
                payLaterWidgetListener?.onClosePayLaterWidget()
            }

        }

    }

}