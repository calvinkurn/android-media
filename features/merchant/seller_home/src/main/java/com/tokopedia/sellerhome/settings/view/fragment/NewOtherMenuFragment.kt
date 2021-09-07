package com.tokopedia.sellerhome.settings.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.requestStatusBarLight
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.analytics.sendEventImpressionStatisticMenuItem
import com.tokopedia.seller.menu.common.analytics.sendShopInfoImpressionData
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.StatisticMenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.common.FragmentType
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.settings.view.activity.MenuSettingActivity
import com.tokopedia.sellerhome.settings.view.adapter.OtherMenuAdapter
import com.tokopedia.sellerhome.settings.view.bottomsheet.SettingsFreeShippingBottomSheet
import com.tokopedia.sellerhome.settings.view.viewholder.NewOtherMenuViewHolder
import com.tokopedia.sellerhome.settings.view.viewmodel.NewOtherMenuViewModel
import com.tokopedia.sellerhome.view.StatusBarCallback
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

//TODO: Preserve name OtherMenuFragment and move the older one to different path
class NewOtherMenuFragment : BaseListFragment<SettingUiModel, OtherMenuAdapterTypeFactory>(),
    SettingTrackingListener, OtherMenuAdapter.Listener, NewOtherMenuViewHolder.Listener,
    StatusBarCallback {

    companion object {
        private const val APPLINK_FORMAT_ALLOW_OVERRIDE = "%s?allow_override=%b&url=%s"
        private const val TAB_PM_PARAM = "tab"

        @JvmStatic
        fun createInstance(): NewOtherMenuFragment = NewOtherMenuFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfigImpl

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(NewOtherMenuViewModel::class.java)
    }

    private val topAdsBottomSheet by lazy {
        // TODO: tidy up this
        BottomSheetUnify().apply {
            setCloseClickListener {
                this.dismiss()
            }
        }
    }

    private val topAdsBottomSheetView by lazy {
        context?.let {
            View.inflate(it, R.layout.setting_topads_bottomsheet_layout, null)
        }
    }

    @FragmentType
    private var currentFragmentType: Int = FragmentType.OTHER

    private var viewHolder: NewOtherMenuViewHolder? = null

    private var multipleErrorSnackbar: Snackbar? = null

    private var canShowErrorToaster = true

    private var shopSnippetUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? SellerHomeActivity)?.attachCallback(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_other_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            viewHolder = NewOtherMenuViewHolder(view, it, this, userSession, this)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBar()
        }
        observeLiveData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllOtherMenuData()
    }

    override fun onItemClicked(t: SettingUiModel?) {}

    override fun loadData(page: Int) {}

    override fun getAdapterTypeFactory(): OtherMenuAdapterTypeFactory =
        OtherMenuAdapterTypeFactory(this, userSession = userSession)

    override fun initInjector() {
        DaggerSellerHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun getScreenName(): String = ""

    override fun sendImpressionDataIris(settingShopInfoImpressionTrackable: SettingShopInfoImpressionTrackable) {
        if (settingShopInfoImpressionTrackable is StatisticMenuItemUiModel) {
            sendEventImpressionStatisticMenuItem(userSession.userId)
        } else {
            settingShopInfoImpressionTrackable.sendShopInfoImpressionData()
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<SettingUiModel, OtherMenuAdapterTypeFactory> {
        return OtherMenuAdapter(context, this, adapterTypeFactory)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rv_sah_new_other_menu

    override fun goToPrintingPage() {
        val url = "${TokopediaUrl.getInstance().WEB}${SellerBaseUrl.PRINTING}"
        val applink = String.format(APPLINK_FORMAT_ALLOW_OVERRIDE, ApplinkConst.WEBVIEW, false, url)
        RouteManager.getIntent(context, applink)?.let {
            context?.startActivity(it)
        }
    }

    override fun goToSettings() {
        startActivity(Intent(context, MenuSettingActivity::class.java))
    }

    override fun getRecyclerView(): RecyclerView? = getRecyclerView(view)

    override fun getFragmentAdapter(): BaseListAdapter<SettingUiModel, OtherMenuAdapterTypeFactory> =
        adapter

    override fun onShopInfoClicked() {
        RouteManager.route(context, ApplinkConst.SHOP, userSession.shopId)
    }

    override fun onShopBadgeClicked() {
        goToReputationHistory()
    }

    override fun onFollowersCountClicked() {
        goToShopFavouriteList()
    }

    override fun onSaldoClicked() {
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP, false))
            RouteManager.route(context, ApplinkConstInternalGlobal.SALDO_DEPOSIT)
        else {
            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalGlobal.WEBVIEW,
                ApplinkConst.WebViewUrl.SALDO_DETAIL
            )
            context?.startActivity(intent)
        }
    }

    override fun onKreditTopadsClicked() {
        val bottomSheet =
            childFragmentManager.findFragmentByTag(OtherMenuFragment.TOPADS_BOTTOMSHEET_TAG)
        if (bottomSheet is BottomSheetUnify) {
            bottomSheet.dismiss()
            RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_AUTO_TOPUP)
        } else {
            RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_CREDIT)
        }
    }

    override fun onFreeShippingClicked() {
        val freeShippingBottomSheet = SettingsFreeShippingBottomSheet.createInstance()
        if (isActivityResumed()) {
            freeShippingBottomSheet.show(childFragmentManager)
        }
    }

    override fun onShopOperationalClicked() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_EDIT_SCHEDULE)
    }

    override fun onGoToPowerMerchantSubscribe(tab: String) {
        val appLink = ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
        val appLinkPMTab =
            Uri.parse(appLink).buildUpon().appendQueryParameter(TAB_PM_PARAM, tab).build()
                .toString()
        context.let { RouteManager.route(context, appLinkPMTab) }
    }

    override fun onRefreshData() {
        // TODO("Not yet implemented")
    }

    override fun onRefreshShopInfo() {
        viewModel.getUserShopInfo()
    }

    override fun onShopBadgeRefresh() {
        viewModel.getShopBadge()
    }

    override fun onShopTotalFollowersRefresh() {
        viewModel.getShopTotalFollowers()
    }

    override fun onUserInfoRefresh() {
        viewModel.getUserShopInfo()
    }

    override fun onOperationalHourRefresh() {
        viewModel.getShopOperational()
    }

    override fun onSaldoBalanceRefresh() {
        viewModel.getBalanceInfo()
    }

    override fun onKreditTopAdsRefresh() {
        viewModel.getKreditTopAds()
    }

    override fun onFreeShippingRefresh() {
        viewModel.getFreeShippingStatus()
    }

    override fun onTopAdsTooltipClicked(isTopAdsActive: Boolean) {
        val bottomSheetChildView = setupBottomSheetLayout(isTopAdsActive)
        bottomSheetChildView?.run {
            with(topAdsBottomSheet) {
                setChild(this@run)
                show(
                    this@NewOtherMenuFragment.childFragmentManager,
                    OtherMenuFragment.TOPADS_BOTTOMSHEET_TAG
                )
            }
        }
    }

    override fun onTopadsValueSet() {
        viewModel.startToggleTopadsCredit()
    }

    override fun getIsShopShareReady(): Boolean = shopSnippetUrl?.isNotBlank() == true

    @RequiresApi(Build.VERSION_CODES.M)
    override fun setStatusBar() {
        (activity as? Activity)?.requestStatusBarLight()
    }

    override fun setCurrentFragmentType(fragmentType: Int) {
        currentFragmentType = fragmentType
        if (fragmentType != FragmentType.OTHER) {
            multipleErrorSnackbar?.dismiss()
        }
    }

    private fun observeLiveData() {
        observeShopBadge()
        observeShopTotalFollowers()
        observeShopStatus()
        observeShopOperationalHour()
        observeSaldoBalance()
        observeKreditTopads()
        observeFreeShipping()
        observeIsTopAdsAutoTopup()
        observeShopPeriod()
        observeShopSnippet()
        observeShouldSwipeSecondaryInfo()
        observeMultipleErrorToaster()
        observeToasterAlreadyShown()
        observeToggleTopadsCount()
    }

    private fun observeShopBadge() {
        viewModel.shopBadgeLiveData.observe(viewLifecycleOwner) {
            viewHolder?.setReputationBadgeData(it)
            if (it is SettingResponseState.SettingError) {
                showErrorToaster(it.throwable) {
                    onShopBadgeRefresh()
                }
            }
        }
    }

    private fun observeShopTotalFollowers() {
        viewModel.shopTotalFollowersLiveData.observe(viewLifecycleOwner) {
            viewHolder?.setShopFollowersData(it)
            if (it is SettingResponseState.SettingError) {
                showErrorToaster(it.throwable) {
                    onShopTotalFollowersRefresh()
                }
            }
        }
    }

    private fun observeShopStatus() {
        viewModel.userShopInfoLiveData.observe(viewLifecycleOwner) {
            viewHolder?.setShopStatusData(it)
            if (it is SettingResponseState.SettingError) {
                showErrorToaster(it.throwable) {
                    onRefreshShopInfo()
                }
            }
        }
    }

    private fun observeShopOperationalHour() {
        viewModel.shopOperationalLiveData.observe(viewLifecycleOwner) {
            viewHolder?.setShopOperationalData(it)
            if (it is SettingResponseState.SettingError) {
                showErrorToaster(it.throwable) {
                    onOperationalHourRefresh()
                }
            }
        }
    }

    private fun observeSaldoBalance() {
        viewModel.balanceInfoLiveData.observe(viewLifecycleOwner) {
            viewHolder?.setBalanceSaldoData(it)
            if (it is SettingResponseState.SettingError) {
                showErrorToaster(it.throwable) {
                    onSaldoBalanceRefresh()
                }
            }
        }
    }

    private fun observeKreditTopads() {
        viewModel.kreditTopAdsLiveData.observe(viewLifecycleOwner) {
            viewHolder?.setBalanceTopadsData(it)
            if (it is SettingResponseState.SettingError) {
                showErrorToaster(it.throwable) {
                    onKreditTopAdsRefresh()
                }
            }
        }
    }

    private fun observeFreeShipping() {
        viewModel.freeShippingLiveData.observe(viewLifecycleOwner) {
            viewHolder?.setFreeShippingData(it)
            if (it is SettingResponseState.SettingError) {
                showErrorToaster(it.throwable) {
                    onFreeShippingRefresh()
                }
            }
        }
    }

    private fun observeIsTopAdsAutoTopup() {
        viewModel.isTopAdsAutoTopupLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    viewHolder?.setIsTopadsAutoTopup(result.data)
                }
                is Fail -> {
                    // TODO: Show toaster error
                }
            }
        }
    }

    private fun observeShopPeriod() {
        observe(viewModel.shopPeriodType) {
            when (it) {
                is Success -> {
                    // TODO: Set tracker
                }
                is Fail -> {
                }
            }
        }
        viewModel.getShopPeriodType()
    }

    private fun observeShopSnippet() {
        viewModel.shopSnippetUrl.observe(viewLifecycleOwner) { url ->
            if (url.isNotBlank()) {
                shopSnippetUrl = url
                viewHolder?.runShareButtonAnimation()
            }
        }
    }

    private fun observeShouldSwipeSecondaryInfo() {
        viewModel.shouldSwipeSecondaryInfo.observe(viewLifecycleOwner) { shouldSwipe ->
            if (shouldSwipe) {
                viewHolder?.swipeSecondaryInfoGently()
            }
        }
    }

    private fun observeMultipleErrorToaster() {
        viewModel.shouldShowMultipleErrorToaster.observe(viewLifecycleOwner) { shouldShowError ->
            if (shouldShowError) {
                showMultipleErrorToaster()
            } else {
                multipleErrorSnackbar?.dismiss()
            }
        }
    }

    private fun observeToasterAlreadyShown() {
        viewModel.isToasterAlreadyShown.observe(viewLifecycleOwner) { isToasterAlreadyShown ->
            canShowErrorToaster = !isToasterAlreadyShown
        }
    }

    private fun observeToggleTopadsCount() {
        viewModel.numberOfTopupToggleCounts.observe(viewLifecycleOwner) { count ->
            if (count != null) {
                viewHolder?.toggleTopadsTopup()
            }
        }
    }

    private fun goToReputationHistory() {
        val reputationHistoryIntent =
            RouteManager.getIntent(context, ApplinkConst.REPUTATION).apply {
                putExtra(OtherMenuFragment.GO_TO_REPUTATION_HISTORY, true)
            }
        startActivity(reputationHistoryIntent)
    }

    private fun goToShopFavouriteList() {
        val shopFavouriteListIntent =
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_FAVOURITE_LIST)
                .apply {
                    putExtra(OtherMenuFragment.EXTRA_SHOP_ID, userSession.shopId)
                }
        startActivity(shopFavouriteListIntent)
    }

    private fun isActivityResumed(): Boolean {
        val state = (activity as? AppCompatActivity)?.lifecycle?.currentState
        return state == Lifecycle.State.STARTED || state == Lifecycle.State.RESUMED
    }

    private fun setupBottomSheetLayout(isTopAdsActive: Boolean): View? {
        val bottomSheetInfix: String
        val bottomSheetDescription: String
        if (isTopAdsActive) {
            bottomSheetInfix = resources.getString(R.string.setting_topads_status_active)
            bottomSheetDescription = resources.getString(R.string.setting_topads_description_active)
        } else {
            bottomSheetInfix = resources.getString(R.string.setting_topads_status_inactive)
            bottomSheetDescription =
                resources.getString(R.string.setting_topads_description_inactive)
        }
        val bottomSheetTitle = resources.getString(R.string.setting_topads_status, bottomSheetInfix)
        return topAdsBottomSheetView?.apply {
            findViewById<Typography>(R.id.topAdsBottomSheetTitle)?.text = bottomSheetTitle
            findViewById<TextView>(R.id.topAdsBottomSheetDescription)?.text = bottomSheetDescription
            findViewById<UnifyButton>(R.id.topAdsNextButton)?.setOnClickListener {
                onKreditTopadsClicked()
            }
        }
    }

    private fun showMultipleErrorToaster() {
        multipleErrorSnackbar =
            view?.run {
                Toaster.build(
                    this,
                    context?.getString(R.string.setting_header_multiple_error_message).orEmpty(),
                    Snackbar.LENGTH_INDEFINITE,
                    Toaster.TYPE_NORMAL,
                    context?.getString(R.string.setting_toaster_error_retry).orEmpty()
                )
                {
                    viewModel.reloadErrorData()
                    viewModel.onReloadErrorData()
                }
            }
        multipleErrorSnackbar?.show()
    }

    private fun showErrorToaster(throwable: Throwable, onRetryAction: () -> Unit = {}) {
        viewModel.onCheckDelayErrorResponseTrigger()
        val canShowToaster = currentFragmentType == FragmentType.OTHER && canShowErrorToaster
        if (canShowToaster) {
            val errorMessage = context?.let {
                ErrorHandler.getErrorMessage(it, throwable)
            } ?: resources.getString(R.string.setting_toaster_error_message)
            view?.showToasterError(errorMessage, onRetryAction)
        }
    }

    private fun View.showToasterError(errorMessage: String, onRetryAction: () -> Unit) {
        Toaster.build(
            this,
            errorMessage,
            Snackbar.LENGTH_LONG,
            Toaster.TYPE_ERROR,
            resources.getString(R.string.setting_toaster_error_retry)
        ) {
            onRetryAction()
        }.show()
    }

}