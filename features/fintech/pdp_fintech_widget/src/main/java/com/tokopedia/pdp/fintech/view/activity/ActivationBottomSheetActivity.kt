package com.tokopedia.pdp.fintech.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.pdp.fintech.di.components.DaggerFintechWidgetComponent
import com.tokopedia.pdp.fintech.di.components.FintechWidgetComponent
import com.tokopedia.pdp.fintech.view.adapter.GopayLinkBenefitAdapter
import com.tokopedia.pdp.fintech.view.bottomsheet.GopayLinkBenefitBottomSheet
import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.track.TrackApp


class ActivationBottomSheetActivity : BaseSimpleActivity(), HasComponent<FintechWidgetComponent>{

    private val pdpSimulationComponent: FintechWidgetComponent by lazy { initInjector() }

    override fun getNewFragment(): Fragment? = null
    lateinit var  bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let{
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

        GopayLinkBenefitBottomSheet().showBottomSheet( supportFragmentManager, bundle).setOnDismissListener {
            finish()
        }
    }

    override fun getComponent() = pdpSimulationComponent


    private fun initInjector() =
        DaggerFintechWidgetComponent.builder()
            .baseAppComponent(
                (applicationContext as BaseMainApplication)
                    .baseAppComponent
            ).build()

}


//object Fintech{
//    const val FINTECH_CURRENT_SITE  = "TokopediaFintech"
//    const val FINTECH_BOTTOMSHEET_BUSINESS = "fintechPaylater"
//    const val EVENT_VIEW_BOTTOMSHEET = "viewFintechIris"
//    const val ACTION_VIEW_BOTTOMSHEET = "bottom sheet bnpl - impression"
//    const val EVENT_CLICK_BOTTOMSHEET = "clickFintech"
//    const val ACTION_CLICK_BOTTOMSHEET =   "bottom sheet bnpl - click"
//    const val EVENT_FINTECH_BOTTOMSHEET_CATEGORY = "fin - activation bottom sheet"
//
//}


//fun fintechActivationBottomSheetImpression(
//    userStatus: String,
//    gatewayCode: String,
//    userId:String,
//    ctaWording:String
//)
//{
//    val mapEvent = hashMapOf<String, Any>(
//        "event" to ProductTrackingConstant.Fintech.EVENT_VIEW_BOTTOMSHEET,
//        "eventAction" to ProductTrackingConstant.Fintech.ACTION_VIEW_BOTTOMSHEET,
//        "eventCategory" to ProductTrackingConstant.Fintech.EVENT_FINTECH_BOTTOMSHEET_CATEGORY,
//        "eventLabel" to "$userStatus - $gatewayCode - $ctaWording - $userId",
//        "businessUnit" to ProductTrackingConstant.Fintech.FINTECH_BOTTOMSHEET_BUSINESS,
//        "currentSite" to ProductTrackingConstant.Fintech.FINTECH_CURRENT_SITE
//    )
//    TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
//}
//
//fun fintechActivationClickBottomSheet(
//    userStatus: String,
//    gatewayCode: String,
//    userId:String,
//    redirectionUrl: String,
//    ctaWording:String
//)
//{
//    val mapEvent = hashMapOf<String, Any>(
//        "event" to ProductTrackingConstant.Fintech.EVENT_CLICK_BOTTOMSHEET,
//        "eventAction" to ProductTrackingConstant.Fintech.ACTION_CLICK_BOTTOMSHEET,
//        "eventCategory" to ProductTrackingConstant.Fintech.EVENT_FINTECH_BOTTOMSHEET_CATEGORY,
//        "eventLabel" to "$userStatus - $gatewayCode - $redirectionUrl - $ctaWording - $userId",
//        "businessUnit" to ProductTrackingConstant.Fintech.FINTECH_BOTTOMSHEET_BUSINESS,
//        "currentSite" to ProductTrackingConstant.Fintech.FINTECH_CURRENT_SITE
//    )
//    TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
//}