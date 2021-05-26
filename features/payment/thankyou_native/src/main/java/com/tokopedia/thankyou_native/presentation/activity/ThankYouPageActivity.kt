package com.tokopedia.thankyou_native.presentation.activity

import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.nps.helper.InAppReviewHelper
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.TkpdIdlingResource
import com.tokopedia.thankyou_native.TkpdIdlingResourceProvider
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.data.mapper.*
import com.tokopedia.thankyou_native.di.component.DaggerThankYouPageComponent
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.DialogController
import com.tokopedia.thankyou_native.presentation.fragment.*
import com.tokopedia.thankyou_native.presentation.helper.ThankYouPageDataLoadCallback
import kotlinx.android.synthetic.main.thank_activity_thank_you.*
import java.lang.ref.WeakReference
import javax.inject.Inject
import com.tokopedia.config.GlobalConfig

var idlingResource: TkpdIdlingResource? = null


const val SCREEN_NAME = "Finish Transaction"
const val ARG_PAYMENT_ID = "payment_id"
const val ARG_MERCHANT = "merchant"

private const val GLOBAL_NAV_HINT = "Cari lagi barang impianmu"

private const val KEY_CONFIG_NEW_NAVIGATION = "app_flag_thankyou_new_navigation"

class ThankYouPageActivity : BaseSimpleActivity(), HasComponent<ThankYouPageComponent>,
        ThankYouPageDataLoadCallback {

    @Inject
    lateinit var thankYouPageAnalytics: dagger.Lazy<ThankYouPageAnalytics>

    private lateinit var thankYouPageComponent: ThankYouPageComponent

    lateinit var thanksPageData: ThanksPageData

    private val dialogController: DialogController by lazy {
        DialogController(GratificationPresenter(this))
    }

    fun getHeader(): HeaderUnify = thank_header

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSecureWindowFlag()
        updateTitle("")
        component.inject(this)
        sendOpenScreenEvent(intent.data)
        idlingResource = TkpdIdlingResourceProvider.provideIdlingResource("Purchase")
        idlingResource?.increment()
    }


    private fun setSecureWindowFlag() {
        if (GlobalConfig.APPLICATION_TYPE == GlobalConfig.CONSUMER_APPLICATION || GlobalConfig.APPLICATION_TYPE == GlobalConfig.SELLER_APPLICATION) {
            runOnUiThread { window.addFlags(WindowManager.LayoutParams.FLAG_SECURE) }
        }
    }


    override fun getLayoutRes() = R.layout.thank_activity_thank_you

    override fun getParentViewResourceID(): Int = R.id.thank_parent_view

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        intent.data?.getQueryParameter(ARG_PAYMENT_ID)?.let {
            intent.putExtra(ARG_MERCHANT, intent.data?.getQueryParameter(ARG_MERCHANT))
            intent.putExtra(ARG_PAYMENT_ID, it.toLong())
            if (intent.extras != null) {
                bundle.putAll(intent.extras)
            }
        }
        showToolbarBeforeLoading()
        return LoaderFragment.getLoaderFragmentInstance(bundle)
    }

    override fun sendScreenAnalytics() {
        //Empty to remove double open screen events
    }

    private fun sendOpenScreenEvent(data: Uri?) {
        data?.apply {
            thankYouPageAnalytics.get().sendScreenAuthenticatedEvent(
                    getQueryParameter(ARG_PAYMENT_ID),
                    getQueryParameter(ARG_MERCHANT),
                    SCREEN_NAME)
        }
    }

    override fun onThankYouPageDataLoaded(thanksPageData: ThanksPageData) {
        this.thanksPageData = thanksPageData
        val fragmentByPaymentMode = getGetFragmentByPaymentMode(thanksPageData)
        fragmentByPaymentMode?.let {
            showToolbarAfterLoading(fragmentByPaymentMode.title)
            supportFragmentManager.beginTransaction()
                    .replace(parentViewResourceID, fragmentByPaymentMode.fragment, tagFragment)
                    .commit()
            decideDialogs(it.fragment, thanksPageData)
        } ?: run { gotoHomePage() }
        postEventOnThankPageDataLoaded(thanksPageData)
        idlingResource?.decrement()
    }

    private fun decideDialogs(selectedFragment: Fragment?, thanksPageData: ThanksPageData) {
        if (selectedFragment is InstantPaymentFragment && !isGratifDisabled()) {
            dialogController.showGratifDialog(WeakReference(this), thanksPageData.paymentID,
                    object : GratificationPresenter.AbstractGratifPopupCallback() {
                override fun onIgnored(reason: Int) {
                    showAppFeedbackBottomSheet(thanksPageData)
                }

            }, selectedFragment::class.java.name)
        } else {
            showAppFeedbackBottomSheet(thanksPageData)
        }
    }

    fun cancelGratifDialog() {
        dialogController.cancelJob()
    }

    private fun showAppFeedbackBottomSheet(thanksPageData: ThanksPageData) {
        val paymentStatus = PaymentStatusMapper.getPaymentStatusByInt(thanksPageData.paymentStatus)
        if (paymentStatus == PaymentVerified || paymentStatus == PaymentPreAuth) {
            InAppReviewHelper.launchInAppReview(this, null)
        }
    }

    private fun postEventOnThankPageDataLoaded(thanksPageData: ThanksPageData) {
        thankYouPageAnalytics.get().postThankYouPageLoadedEvent(thanksPageData)
    }

    override fun onInvalidThankYouPage() {
        gotoHomePage()
        idlingResource?.decrement()
        finish()
    }

    override fun getComponent(): ThankYouPageComponent {
        if (!::thankYouPageComponent.isInitialized)
            thankYouPageComponent = DaggerThankYouPageComponent.builder()
                    .baseAppComponent((applicationContext as BaseMainApplication)
                            .baseAppComponent).build()
        return thankYouPageComponent
    }

    private fun getGetFragmentByPaymentMode(thanksPageData: ThanksPageData): FragmentByPaymentMode? {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return when (PaymentPageMapper.getPaymentPageType(thanksPageData.pageType)) {
            is ProcessingPaymentPage -> {
                FragmentByPaymentMode(ProcessingPaymentFragment.getFragmentInstance(bundle, thanksPageData),
                        ProcessingPaymentFragment.SCREEN_NAME)
            }
            is InstantPaymentPage -> {
                FragmentByPaymentMode(InstantPaymentFragment.getFragmentInstance(bundle, thanksPageData),
                        InstantPaymentFragment.SCREEN_NAME)
            }
            is WaitingPaymentPage -> {
                return when (PaymentStatusMapper.getPaymentStatusByInt(thanksPageData.paymentStatus)) {
                    is PaymentWaitingCOD -> {
                        FragmentByPaymentMode(CashOnDeliveryFragment.getFragmentInstance(bundle, thanksPageData),
                                CashOnDeliveryFragment.SCREEN_NAME)
                    }
                    is PaymentWaiting -> {
                        FragmentByPaymentMode(DeferredPaymentFragment.getFragmentInstance(bundle, thanksPageData),
                                DeferredPaymentFragment.SCREEN_NAME)
                    }
                    else -> null
                }
            }
            else -> null
        }

    }

    private fun showToolbarBeforeLoading() {
        thank_header.isShowBackButton = false
        thank_header.visible()
        globalNabToolbar.gone()
    }

    private fun showToolbarAfterLoading(title: String) {
        if (isGlobalNavEnable()) {
            thank_header.gone()
            globalNabToolbar.visible()
            initializeGlobalNav(title)
        } else {
            globalNabToolbar.gone()
            thank_header.visible()
            setupOldToolbar(title)
        }
    }

    private fun setupOldToolbar(title: String){
        thank_header.isShowBackButton = true
        toolbar = thank_header
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }
        updateHeaderTitle(title)
    }

    private fun isGlobalNavEnable(): Boolean {
        val isNewNavigationEnabled = FirebaseRemoteConfigImpl(this).getBoolean(KEY_CONFIG_NEW_NAVIGATION,
                false)
        if(isNewNavigationEnabled) {
            getAbTestPlatform()?.let {
                return (it.getString(AbTestPlatform.NAVIGATION_EXP_TOP_NAV, AbTestPlatform.NAVIGATION_VARIANT_OLD)
                        == AbTestPlatform.NAVIGATION_VARIANT_REVAMP)
            }
        }
        return false
    }

    private fun getAbTestPlatform(): AbTestPlatform? {
        return try {
            RemoteConfigInstance.getInstance().abTestPlatform
        } catch (e: IllegalStateException) {
            null
        }
    }


    private fun initializeGlobalNav(title: String) {
        globalNabToolbar?.apply {
            this@ThankYouPageActivity.lifecycle.addObserver(this)
            setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
            setIcon(IconBuilder().addIcon(IconList.ID_NAV_GLOBAL) {})
            setupSearchbar(listOf(HintData(GLOBAL_NAV_HINT)))
            setToolbarPageName(title)
            show()
        }
    }

    private fun updateHeaderTitle(screenName: String) {
        thank_header.title = screenName
    }

    /**
     * this function is override because need to check payment
     * status if payment type is deferred/Processing
     * */
    override fun onBackPressed() {
        if (::thanksPageData.isInitialized)
            thankYouPageAnalytics.get().sendBackPressedEvent(thanksPageData.profileCode,
                    thanksPageData.paymentID.toString())
        if (!isOnBackPressOverride()) {
            gotoHomePage()
            finish()
        }
    }

    private fun isOnBackPressOverride(): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(tagFragment)
        fragment?.let {
            return when (it) {
                is LoaderFragment -> true
                else -> false
            }

        }
        return false
    }

    private fun gotoHomePage() {
        RouteManager.route(this, ApplinkConst.HOME, "")
        finish()
    }

    companion object {
        const val REMOTE_GRATIF_DISABLED = "android_disable_gratif_thankyou"
    }

    private fun isGratifDisabled(): Boolean {
        return try {
            val remoteConfig = FirebaseRemoteConfigImpl(this)
            return remoteConfig.getBoolean(REMOTE_GRATIF_DISABLED, false)
        } catch (e: Exception) {
            false
        }
    }

    data class FragmentByPaymentMode(val fragment: Fragment, val title : String)
}

