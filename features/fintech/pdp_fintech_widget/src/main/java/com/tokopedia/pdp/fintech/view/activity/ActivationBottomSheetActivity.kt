package com.tokopedia.pdp.fintech.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.pdp.fintech.analytics.FintechWidgetAnalyticsEvent
import com.tokopedia.pdp.fintech.analytics.PdpFintechWidgetAnalytics
import com.tokopedia.pdp.fintech.di.components.DaggerFintechWidgetComponent
import com.tokopedia.pdp.fintech.di.components.FintechWidgetComponent
import com.tokopedia.pdp.fintech.view.bottomsheet.GopayLinkBenefitBottomSheet
import javax.inject.Inject


class ActivationBottomSheetActivity : BaseSimpleActivity(), HasComponent<FintechWidgetComponent> {

    private val fintechWidgetComponent: FintechWidgetComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }

    @Inject
    lateinit var pdpWidgetAnalytics: PdpFintechWidgetAnalytics


    override fun getNewFragment(): Fragment? = null
    lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fintechWidgetComponent.inject(this)
        intent.extras?.let {
            bundle = it
        }
        hideTitleAndHomeButton()
        openChooseBankBottomSheet()
    }

    private fun hideTitleAndHomeButton() {
        supportActionBar?.setDisplayShowCustomEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = ""
    }

    private fun openChooseBankBottomSheet() {

        GopayLinkBenefitBottomSheet().showBottomSheet(supportFragmentManager, bundle)
            .setOnDismissListener {
                finish()
            }
    }

    override fun getComponent() = fintechWidgetComponent


    private fun initInjector() =
        DaggerFintechWidgetComponent.builder()
            .baseAppComponent(
                (applicationContext as BaseMainApplication)
                    .baseAppComponent
            ).build()


    fun sendAnalytic(bottomSheetClickEvent: FintechWidgetAnalyticsEvent.ActivationBottomSheetClick) {
        pdpWidgetAnalytics.sendAnalyticsEvent(bottomSheetClickEvent)
    }

}
