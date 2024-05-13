package com.tokopedia.thankyou_native.presentation.activity

import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.transition.R.*
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.analytics.byteio.AppLogInterface
import com.tokopedia.analytics.byteio.IAdsLog
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.nps.helper.InAppReviewHelper
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey.ANDROID_ENABLE_PURCHASE_INFO
import com.tokopedia.remoteconfig.RemoteConfigKey.ANDROID_ENABLE_THANKYOUPAGE_V2
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.TkpdIdlingResource
import com.tokopedia.thankyou_native.TkpdIdlingResourceProvider
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.data.mapper.InstantPaymentPage
import com.tokopedia.thankyou_native.data.mapper.PaymentPageMapper
import com.tokopedia.thankyou_native.data.mapper.PaymentPreAuth
import com.tokopedia.thankyou_native.data.mapper.PaymentStatusMapper
import com.tokopedia.thankyou_native.data.mapper.PaymentVerified
import com.tokopedia.thankyou_native.data.mapper.PaymentWaiting
import com.tokopedia.thankyou_native.data.mapper.PaymentWaitingCOD
import com.tokopedia.thankyou_native.data.mapper.ProcessingPaymentPage
import com.tokopedia.thankyou_native.data.mapper.WaitingPaymentPage
import com.tokopedia.thankyou_native.di.component.DaggerThankYouPageComponent
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.DialogController
import com.tokopedia.thankyou_native.presentation.fragment.CashOnDeliveryFragment
import com.tokopedia.thankyou_native.presentation.fragment.DeferredPaymentFragment
import com.tokopedia.thankyou_native.presentation.fragment.InstantPaymentFragment
import com.tokopedia.thankyou_native.presentation.fragment.LoaderFragment
import com.tokopedia.thankyou_native.presentation.fragment.ProcessingPaymentFragment
import com.tokopedia.thankyou_native.presentation.fragment.ThankYouBaseFragment
import com.tokopedia.thankyou_native.presentation.helper.PostPurchaseShareHelper
import com.tokopedia.thankyou_native.presentation.helper.ThankYouPageDataLoadCallback
import com.tokopedia.unifycomponents.fade
import com.tokopedia.unifyprinciples.UnifyMotion
import kotlinx.android.synthetic.main.thank_activity_thank_you.*
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

var idlingResource: TkpdIdlingResource? = null

const val SCREEN_NAME = "Finish Transaction"
const val ARG_PAYMENT_ID = "payment_id"
const val ARG_MERCHANT = "merchant"

private const val GLOBAL_NAV_HINT = "Cari di Tokopedia"

private const val KEY_CONFIG_NEW_NAVIGATION = "app_flag_thankyou_new_navigation"
private const val KEY_ROLLENCE_SHARE = "share_thankyoupage"
private const val VALUE_MERCHANT_TOKOPEDIA = "tokopedia"

class ThankYouPageActivity :
    BaseSimpleActivity(),
    HasComponent<ThankYouPageComponent>,
    ThankYouPageDataLoadCallback,
    AppLogInterface,
    IAdsLog {

    @Inject
    lateinit var thankYouPageAnalytics: dagger.Lazy<ThankYouPageAnalytics>

    @Inject
    lateinit var postPurchaseShareHelper: dagger.Lazy<PostPurchaseShareHelper>

    private lateinit var thankYouPageComponent: ThankYouPageComponent

    lateinit var thanksPageData: ThanksPageData

    private val dialogController: DialogController by lazy {
        DialogController(GratificationPresenter(this))
    }

//    fun getHeader(): HeaderUnify = thank_header

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar()
        configSpaceFullScreen()
        updateTitle("")
        component.inject(this)
        sendOpenScreenEvent(intent.data)
        idlingResource = TkpdIdlingResourceProvider.provideIdlingResource("Purchase")
        idlingResource?.increment()
    }

    override fun getLayoutRes() = R.layout.thank_activity_thank_you

    override fun getParentViewResourceID(): Int = R.id.thank_loader_view

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        intent.data?.getQueryParameter(ARG_PAYMENT_ID)?.let {
            intent.putExtra(ARG_MERCHANT, intent.data?.getQueryParameter(ARG_MERCHANT))
            intent.putExtra(ARG_PAYMENT_ID, it)
            intent.putExtra(ThankYouBaseFragment.ARG_IS_V2_ENABLED, isV2Enabled())
            if (intent.extras != null) {
                bundle.putAll(intent.extras)
            }
        }
        return LoaderFragment.getLoaderFragmentInstance(bundle)
    }

    override fun sendScreenAnalytics() {
        // Empty to remove double open screen events
    }

    private fun sendOpenScreenEvent(data: Uri?) {
        data?.apply {
            thankYouPageAnalytics.get().sendScreenAuthenticatedEvent(
                getQueryParameter(ARG_PAYMENT_ID),
                getQueryParameter(ARG_MERCHANT),
                SCREEN_NAME
            )
        }
    }

    override fun onThankYouPageDataLoaded(thanksPageData: ThanksPageData) {
        this.thanksPageData = thanksPageData

        val header = findViewById<ImageView>(R.id.header_background)
        when (PaymentPageMapper.getPaymentPageType(thanksPageData.pageType, thanksPageData.paymentStatus)) {
            InstantPaymentPage -> {
                this.let {
                    header.setColorFilter(ContextCompat.getColor(it, unifyprinciplesR.color.Unify_GN500))
                }
            }
            ProcessingPaymentPage -> {
                this.let {
                    header.setColorFilter(ContextCompat.getColor(it, unifyprinciplesR.color.Unify_TN50))
                }
            }
            WaitingPaymentPage -> {
                this.let {
                    header.setColorFilter(ContextCompat.getColor(it, unifyprinciplesR.color.Unify_YN50))
                }
            }
            null -> {
                // no op
            }
        }

        val fragmentByPaymentMode = getGetFragmentByPaymentMode(thanksPageData)
        fragmentByPaymentMode?.let {
            showToolbarAfterLoading(fragmentByPaymentMode.title)
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.thank_parent_view, fragmentByPaymentMode.fragment, "asdasd")
                .commit()
            showAppFeedbackBottomSheet(thanksPageData)
        } ?: run { gotoHomePage() }
        postEventOnThankPageDataLoaded(thanksPageData)
        thankYouPageAnalytics.get().sendSubmitOrderByteIoTracker(thanksPageData)
        if (!isV2Enabled()) {
            findViewById<FrameLayout>(R.id.thank_parent_view).layoutParams.height = 0
            findViewById<FrameLayout>(R.id.thank_parent_view).updateLayoutParams<ConstraintLayout.LayoutParams> {
                topToBottom = findViewById<View>(R.id.globalNabToolbar).id
                bottomToBottom = ConstraintSet.PARENT_ID
            }
            findViewById<View>(R.id.toolbarBackground).gone()
        }
        idlingResource?.decrement()
        findViewById<FrameLayout>(getParentViewResourceID()).animate().alpha(0f).setDuration(UnifyMotion.T5).withEndAction {
            supportFragmentManager.findFragmentById(getParentViewResourceID())?.let {
                supportFragmentManager.beginTransaction().remove(it).commit()
            }
        }.start()
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
        if (!::thankYouPageComponent.isInitialized) {
            thankYouPageComponent = DaggerThankYouPageComponent.builder()
                .baseAppComponent(
                    (applicationContext as BaseMainApplication)
                        .baseAppComponent
                ).build()
        }
        return thankYouPageComponent
    }

    private fun getGetFragmentByPaymentMode(thanksPageData: ThanksPageData): FragmentByPaymentMode? {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }

        if (isV2Enabled()) {
            return FragmentByPaymentMode(
                ThankYouBaseFragment.getFragmentInstance(
                    bundle,
                    thanksPageData,
                    isWidgetOrderingEnabled(),
                    isV2Enabled(),
                    isPurchaseInfoEnabled()
                ),
                ""
            )
        }

        return when (PaymentPageMapper.getPaymentPageType(thanksPageData.pageType)) {
            is ProcessingPaymentPage -> {
                FragmentByPaymentMode(
                    ProcessingPaymentFragment.getFragmentInstance(
                        bundle,
                        thanksPageData,
                        isWidgetOrderingEnabled()
                    ),
                    ProcessingPaymentFragment.SCREEN_NAME
                )
            }
            is InstantPaymentPage -> {
                FragmentByPaymentMode(
                    InstantPaymentFragment.getFragmentInstance(
                        bundle,
                        thanksPageData,
                        isWidgetOrderingEnabled()
                    ),
                    InstantPaymentFragment.SCREEN_NAME
                )
            }
            is WaitingPaymentPage -> {
                return when (PaymentStatusMapper.getPaymentStatusByInt(thanksPageData.paymentStatus)) {
                    is PaymentWaitingCOD -> {
                        FragmentByPaymentMode(
                            CashOnDeliveryFragment.getFragmentInstance(
                                bundle,
                                thanksPageData,
                                isWidgetOrderingEnabled()
                            ),
                            CashOnDeliveryFragment.SCREEN_NAME
                        )
                    }
                    is PaymentWaiting -> {
                        FragmentByPaymentMode(
                            DeferredPaymentFragment.getFragmentInstance(
                                bundle,
                                thanksPageData,
                                isWidgetOrderingEnabled()
                            ),
                            DeferredPaymentFragment.SCREEN_NAME
                        )
                    }
                    else -> null
                }
            }
            else -> null
        }
    }

    private fun showToolbarAfterLoading(title: String) {
        if (isGlobalNavEnable()) {
            globalNabToolbar.show()
            initializeGlobalNav(title)
        } else {
            globalNabToolbar.gone()
            setupOldToolbar(title)
        }
    }

    private fun setupOldToolbar(title: String) {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }
    }

    private fun isGlobalNavEnable(): Boolean {
        val isNewNavigationEnabled = FirebaseRemoteConfigImpl(this).getBoolean(
            KEY_CONFIG_NEW_NAVIGATION,
            false
        )
        if (isNewNavigationEnabled) {
            return true
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
            var hideSearchBar = false
            var hideGlobalMenu = false
            if (::thanksPageData.isInitialized) {
                hideSearchBar = thanksPageData.configFlagData?.shouldHideSearchBar ?: false
                hideGlobalMenu = thanksPageData.configFlagData?.shouldHideGlobalMenu ?: false
            }
            this@ThankYouPageActivity.lifecycle.addObserver(this)
            setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
            if (hideSearchBar.not()) setIcon(getIconBuilder())
            if (hideGlobalMenu.not()) setupSearchbar(listOf(HintData(GLOBAL_NAV_HINT)))
            setToolbarPageName(title)
            show()
        }
    }

    /**
     * this function is override because need to check payment
     * status if payment type is deferred/Processing
     * */
    override fun onBackPressed() {
        if (::thanksPageData.isInitialized) {
            thankYouPageAnalytics.get().sendBackPressedEvent(
                thanksPageData.profileCode,
                thanksPageData.paymentID
            )
        }
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
        private const val REMOTE_GRATIF_DISABLED = "android_disable_gratif_thankyou"
    }

    private fun isGratifDisabled(): Boolean {
        return try {
            val remoteConfig = FirebaseRemoteConfigImpl(this)
            return remoteConfig.getBoolean(REMOTE_GRATIF_DISABLED, false)
        } catch (e: Exception) {
            false
        }
    }

    private fun isWidgetOrderingEnabled(): Boolean {
        return try {
            return (
                getAbTestPlatform()
                    ?.getString(RollenceKey.THANKYOU_PAGE_WIDGET_ORDERING, String.EMPTY) ?: String.EMPTY
                ).isNotEmpty()
        } catch (e: Exception) {
            Timber.e(e)
            true
        }
    }

    data class FragmentByPaymentMode(val fragment: Fragment, val title: String)

    /**
     * Header Icon(s) Section
     */

    private fun getIconBuilder(): IconBuilder {
        return try {
            val shareRollence: String = getAbTestPlatform()?.getString(
                key = KEY_ROLLENCE_SHARE,
                defaultValue = ""
            ) ?: ""
            val merchantCode = intent.data?.getQueryParameter(ARG_MERCHANT)
            if (shareRollence == KEY_ROLLENCE_SHARE &&
                merchantCode == VALUE_MERCHANT_TOKOPEDIA
            ) {
                getCustomizeIconBuilder()
            } else {
                getDefaultIconBuilder()
            }
        } catch (throwable: Throwable) { // in case error, show default
            Timber.d(throwable)
            getDefaultIconBuilder()
        }
    }

    private fun getCustomizeIconBuilder(): IconBuilder {
        // Track view
        thankYouPageAnalytics.get().sendViewShareIcon(
            orderIdList = postPurchaseShareHelper.get().getOrderIdListString(
                thanksPageData.shopOrder
            )
        )

        // asc order from left to right
        return IconBuilder(
            builderFlags = IconBuilderFlag(
                pageSource = NavSource.THANKYOU
            )
        ).addIcon(IconList.ID_SHARE) {
            thankYouPageAnalytics.get().sendClickShareIcon(
                orderIdList = postPurchaseShareHelper.get().getOrderIdListString(
                    thanksPageData.shopOrder
                )
            )
            postPurchaseShareHelper.get().goToSharePostPurchase(
                this,
                thanksPageData.shopOrder
            )
        }
        .addIcon(IconList.ID_CART) {
            // no-op
        }
        .addIcon(IconList.ID_NAV_GLOBAL) {
            // no-op
        }
    }

    private fun getDefaultIconBuilder(): IconBuilder {
        return IconBuilder(
            builderFlags = IconBuilderFlag(
                pageSource = NavSource.THANKYOU
            )
        ).addIcon(IconList.ID_NAV_GLOBAL) {
            // no-op
        }
    }

    private fun isV2Enabled(): Boolean {
        return try {
            val remoteConfig = FirebaseRemoteConfigImpl(this)
            return remoteConfig.getBoolean(ANDROID_ENABLE_THANKYOUPAGE_V2, true)
        } catch (ignore: Exception) {
            false
        }
    }

    private fun isPurchaseInfoEnabled(): Boolean {
        return try {
            val remoteConfig = FirebaseRemoteConfigImpl(this)
            return remoteConfig.getBoolean(ANDROID_ENABLE_PURCHASE_INFO, true)
        } catch (ignore: Exception) {
            false
        }
    }

    private fun hideStatusBar() {

        rootView.apply {
            fitsSystemWindows = false
            requestApplyInsets()
        }

        window?.let { window ->
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun configSpaceFullScreen() {
        globalNabToolbar.updateLayoutParams<ConstraintLayout.LayoutParams> {
            this.setMargins(0, DisplayMetricUtils.getStatusBarHeight(this@ThankYouPageActivity), 0, 0)
        }
    }

    override fun getPageName(): String {
        return PageName.ORDER_SUBMIT
    }

    override fun getAdsPageName(): String {
        return PageName.ORDER_SUBMIT
    }
}
