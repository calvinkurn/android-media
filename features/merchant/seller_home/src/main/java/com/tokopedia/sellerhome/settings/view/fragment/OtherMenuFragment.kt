package com.tokopedia.sellerhome.settings.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.requestStatusBarDark
import com.tokopedia.kotlin.extensions.view.requestStatusBarLight
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.common.StatusbarHelper
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.settings.analytics.SettingTrackingConstant
import com.tokopedia.sellerhome.settings.analytics.SettingTrackingListener
import com.tokopedia.sellerhome.settings.analytics.sendShopInfoImpressionData
import com.tokopedia.sellerhome.settings.data.constant.SellerBaseUrl
import com.tokopedia.sellerhome.settings.view.activity.MenuSettingActivity
import com.tokopedia.sellerhome.settings.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.DividerUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.MenuItemUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.SettingTitleUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.*
import com.tokopedia.sellerhome.settings.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.sellerhome.settings.view.viewholder.OtherMenuViewHolder
import com.tokopedia.sellerhome.settings.view.viewmodel.OtherMenuViewModel
import com.tokopedia.sellerhome.view.StatusBarCallback
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_other_menu.*
import javax.inject.Inject

class OtherMenuFragment: BaseListFragment<SettingUiModel, OtherMenuAdapterTypeFactory>(), OtherMenuViewHolder.Listener, StatusBarCallback, SettingTrackingListener{

    companion object {
        const val URL_KEY = "url"

        private const val START_OFFSET = 56 // Pixels when scrolled past toolbar height
        private const val HEIGHT_OFFSET = 24 // Pixels of status bar height, the view that could be affected by scroll change
        private const val MAXIMUM_ALPHA = 255f
        private const val ALPHA_CHANGE_THRESHOLD = 150
        @JvmStatic
        fun createInstance(): OtherMenuFragment = OtherMenuFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfigImpl

    private var otherMenuViewHolder: OtherMenuViewHolder? = null

    private val statusBarHeight by lazy {
        context?.let { StatusbarHelper.getStatusBarHeight(it) }
    }
    private var startToTransitionOffset = 0
    private var statusInfoTransitionOffset = 0

    private var isInitialStatusBar = false
    private var isDefaultDarkStatusBar = true

    private val otherMenuViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(OtherMenuViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        getAllShopInfoData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? SellerHomeActivity)?.attachCallback(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_other_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOffset()
        setupView(view)
        observeLiveData()
    }

    override fun getAdapterTypeFactory(): OtherMenuAdapterTypeFactory = OtherMenuAdapterTypeFactory(this)

    override fun onItemClicked(settingUiModel: SettingUiModel) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {}

    override fun onShopInfoClicked() {
        RouteManager.route(context, ApplinkConst.SHOP, userSession.shopId)
    }

    override fun onFollowersCountClicked() {
        //No op for now. Will discuss with PM
    }

    override fun onSaldoClicked() {
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP, false))
            RouteManager.route(context, ApplinkConstInternalGlobal.SALDO_DEPOSIT)
        else {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, ApplinkConst.WebViewUrl.SALDO_DETAIL)
            context?.startActivity(intent)
        }
    }

    override fun onKreditTopadsClicked() {
        RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_DASHBOARD)
    }

    override fun onRefreshShopInfo() {
        showAllLoadingShimmering()
        otherMenuViewModel.getAllSettingShopInfo()
    }

    override fun sendImpressionDataIris(settingShopInfoImpressionTrackable: SettingShopInfoImpressionTrackable) {
        context?.run { settingShopInfoImpressionTrackable.sendShopInfoImpressionData(this, userSession) }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun setStatusBar() {
        (activity as? Activity)?.run {
            if (isInitialStatusBar && !isDefaultDarkStatusBar) {
                requestStatusBarLight()
            } else {
                requestStatusBarDark()
            }
        }
    }

    override fun onStatusBarNeedDarkColor(isDefaultDark: Boolean) {
        isDefaultDarkStatusBar = isDefaultDark
        setStatusBarStateInitialIsLight(!isDefaultDark)
        if (otherMenuViewModel.isStatusBarInitialState.value == false && !isDefaultDark) {
            setStatusBarStateInitialIsLight(isDefaultDark)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBar()
        }
    }

    private fun setStatusBarStateInitialIsLight(isLight: Boolean) {
        isInitialStatusBar = isLight
    }

    private fun observeLiveData() {
        with(otherMenuViewModel) {
            settingShopInfoLiveData.observe(viewLifecycleOwner, Observer { result ->
                when(result) {
                    is Success -> showSettingShopInfoState(result.data)
                    is Fail -> showSettingShopInfoState(SettingResponseState.SettingError)
                }
            })

        }
    }

    private fun populateAdapterData() {
        val settingList = mutableListOf(
                SettingTitleUiModel(resources.getString(R.string.setting_menu_improve_sales)),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_shop_statistic),
                        R.drawable.ic_statistic_setting,
                        ApplinkConstInternalMarketplace.GOLD_MERCHANT_STATISTIC_DASHBOARD,
                        eventActionSuffix = SettingTrackingConstant.SHOP_STATISTIC),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_ads_and_shop_promotion),
                        R.drawable.ic_ads_promotion,
                        ApplinkConstInternalSellerapp.CENTRALIZED_PROMO,
                        eventActionSuffix = SettingTrackingConstant.SHOP_ADS_AND_PROMOTION),
                SettingTitleUiModel(resources.getString(R.string.setting_menu_buyer_info)),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_review),
                        R.drawable.ic_star_setting,
                        ApplinkConst.REPUTATION,
                        eventActionSuffix = SettingTrackingConstant.REVIEW),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_discussion),
                        R.drawable.ic_setting_discussion,
                        ApplinkConst.TALK,
                        eventActionSuffix = SettingTrackingConstant.DISCUSSION),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_complaint),
                        R.drawable.ic_complaint,
                        null,
                        eventActionSuffix = SettingTrackingConstant.COMPLAINT) {
                    val intent = RouteManager.getIntent(context, ApplinkConst.SellerApp.WEBVIEW)
                    intent.putExtra(URL_KEY, SellerBaseUrl.HOSTNAME + SellerBaseUrl.RESO_INBOX_SELLER)
                    context?.startActivity(intent)
                },
                DividerUiModel(),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_finance_service),
                        R.drawable.ic_finance,
                        eventActionSuffix = SettingTrackingConstant.FINANCIAL_SERVICE),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_seller_education_center),
                        R.drawable.ic_seller_edu,
                        eventActionSuffix = SettingTrackingConstant.SELLER_CENTER) {
                    val intent = RouteManager.getIntent(context, ApplinkConst.WEBVIEW)
                    intent.putExtra(URL_KEY, SellerBaseUrl.SELLER_HOSTNAME + SellerBaseUrl.SELLER_EDU)
                    context?.startActivity(intent)
                },
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_tokopedia_care),
                        R.drawable.ic_tokopedia_care,
                        ApplinkConst.CONTACT_US_NATIVE,
                        eventActionSuffix = SettingTrackingConstant.TOKOPEDIA_CARE),
                DividerUiModel(DividerType.THIN_PARTIAL),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_setting),
                        R.drawable.ic_setting,
                        null,
                        eventActionSuffix = SettingTrackingConstant.SETTINGS) {
                    startActivity(Intent(context, MenuSettingActivity::class.java))
                }
        )
        adapter.data.addAll(settingList)
        adapter.notifyDataSetChanged()
        renderList(settingList)
    }

    private fun getAllShopInfoData() {
        showAllLoadingShimmering()
        otherMenuViewModel.getAllSettingShopInfo()
    }

    private fun showAllLoadingShimmering() {
        showSettingShopInfoState(SettingResponseState.SettingLoading)
    }

    private fun showSettingShopInfoState(settingResponseState: SettingResponseState) {
        when(settingResponseState) {
            is SettingSuccess -> {
                if (settingResponseState is SettingShopInfoUiModel) {
                    otherMenuViewHolder?.onSuccessGetSettingShopInfoData(settingResponseState)
                }
            }
            is SettingResponseState.SettingLoading -> otherMenuViewHolder?.onLoadingGetSettingShopInfoData()
            is SettingResponseState.SettingError -> {
                view?.showToasterError(resources.getString(R.string.setting_toaster_error_message))
                otherMenuViewHolder?.onErrorGetSettingShopInfoData()
            }
        }
    }

    private fun retryFetchAfterError() {
        otherMenuViewModel.getAllSettingShopInfo()
    }

    private fun View.showToasterError(errorMessage: String) {
        Toaster.make(this,
                errorMessage,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                resources.getString(R.string.setting_toaster_error_retry),
                View.OnClickListener {
                    retryFetchAfterError()
                })
    }

    private fun setupView(view: View) {
        view.run {
            statusBarBackground?.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, statusBarHeight ?: HEIGHT_OFFSET)
        }
        populateAdapterData()
        recycler_view.layoutManager = LinearLayoutManager(context)
        context?.let { otherMenuViewHolder = OtherMenuViewHolder(view, it, this, this)}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isDefaultDarkStatusBar) {
                activity?.requestStatusBarDark()
            } else {
                activity?.requestStatusBarLight()
            }
        }
        observeRecyclerViewScrollListener()
    }

    private fun setupOffset() {
        activity?.theme?.let {
            val tv = TypedValue()
            if (it.resolveAttribute(R.attr.actionBarSize, tv, true)) {
                startToTransitionOffset = START_OFFSET
            }
        }
        statusInfoTransitionOffset = statusBarHeight ?: HEIGHT_OFFSET
    }

    private fun observeRecyclerViewScrollListener() {
        this.otherMenuScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { scrollView, _, _, _, _ ->
            calculateSearchBarView(scrollView.scrollY)
        })
    }

    private fun calculateSearchBarView(offset: Int) {
        val endToTransitionOffset = startToTransitionOffset + statusInfoTransitionOffset
        val maxTransitionOffset = endToTransitionOffset - startToTransitionOffset

        //Offset Alpha is not actually needed for changing the status bar color (only needed the offset),
        //but we will preserve the variable in case the stakeholders need to change the status bar alpha according to the scroll position
        val offsetAlpha = (MAXIMUM_ALPHA/maxTransitionOffset).times(offset - startToTransitionOffset)
        if (offsetAlpha >= ALPHA_CHANGE_THRESHOLD) {
            if (isInitialStatusBar) {
                setDarkStatusBar()
                otherMenuViewModel.setIsStatusBarInitialState(false)
            }
        } else {
            if (!isInitialStatusBar) {
                setLightStatusBar()
                otherMenuViewModel.setIsStatusBarInitialState(true)
            }
        }
    }

    private fun setLightStatusBar() {
        if (!isDefaultDarkStatusBar){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity?.requestStatusBarLight()
            }
        }
        setStatusBarStateInitialIsLight(true)
        statusBarBackground?.hide()
    }

    private fun setDarkStatusBar() {
        setStatusBarStateInitialIsLight(false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.requestStatusBarDark()
        }
        statusBarBackground?.show()
    }

}