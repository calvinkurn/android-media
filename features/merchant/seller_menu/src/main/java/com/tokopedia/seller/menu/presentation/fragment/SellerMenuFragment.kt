package com.tokopedia.seller.menu.presentation.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.gm.common.utils.PMShopScoreInterruptHelper
import com.tokopedia.gm.common.utils.ShopScoreReputationErrorLogger
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.SellerFeatureUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SellerMenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState.SettingError
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState.SettingLoading
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingSuccess
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.seller.menu.common.view.viewholder.ShopInfoErrorViewHolder
import com.tokopedia.seller.menu.common.view.viewholder.ShopInfoViewHolder
import com.tokopedia.seller.menu.di.component.DaggerSellerMenuComponent
import com.tokopedia.seller.menu.presentation.adapter.SellerMenuAdapter
import com.tokopedia.seller.menu.presentation.util.AdminPermissionMapper
import com.tokopedia.seller.menu.presentation.util.SellerMenuList
import com.tokopedia.seller.menu.presentation.viewmodel.SellerMenuViewModel
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_seller_menu.*
import javax.inject.Inject

class SellerMenuFragment : Fragment(), SettingTrackingListener, ShopInfoViewHolder.ShopInfoListener,
        ShopInfoErrorViewHolder.ShopInfoErrorListener {

    companion object {
        private const val SCREEN_NAME = "MA - Akun Toko"
    }

    @Inject
    lateinit var viewModel: SellerMenuViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var sellerMenuTracker: SellerMenuTracker

    @Inject
    lateinit var adminPermissionMapper: AdminPermissionMapper

    @Inject
    lateinit var pmShopScoreInterruptHelper: PMShopScoreInterruptHelper

    private var canShowErrorToaster = true
    private var isNewSeller = false
    private var shopAge = 0

    private val adapter by lazy {
        SellerMenuAdapter(OtherMenuAdapterTypeFactory(
                this,
                this,
                this,
                this,
                sellerMenuTracker,
                userSession
        ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        setHasOptionsMenu(true)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seller_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupMenuList()
        setupSwipeRefresh()
        observeShopInfoPeriod()
        observeViewModel()
        setupPMShopScoreInterrupt()
        setupScrollToShopSetting()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.seller_menu_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        sellerMenuTracker.sendEventOpenScreen(SCREEN_NAME)
    }

    override fun onDestroy() {
        super.onDestroy()
        pmShopScoreInterruptHelper.destroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_inbox -> {
                RouteManager.route(context, ApplinkConst.INBOX)
                sellerMenuTracker.sendEventClickInbox()
            }
            R.id.action_notification -> {
                RouteManager.route(context, ApplinkConst.NOTIFICATION)
                sellerMenuTracker.sendEventClickNotification()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun sendImpressionDataIris(settingShopInfoImpressionTrackable: SettingShopInfoImpressionTrackable) {}

    override fun onScoreClicked() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_PERFORMANCE, userSession.shopId)
        sellerMenuTracker.sendShopScoreEntryPoint(isNewSeller)
    }

    override fun onScoreImpressed() {
        sellerMenuTracker.impressShopScoreEntryPoint(isNewSeller)
    }

    override fun onSaldoClicked() {
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP, false))
            RouteManager.route(context, ApplinkConstInternalGlobal.SALDO_DEPOSIT)
        else {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, ApplinkConst.WebViewUrl.SALDO_DETAIL)
            context?.startActivity(intent)
        }
    }

    override fun onRefreshShopInfo() {
        showShopInfoLoading()
        getAllShopInfo()
    }

    private fun initInjector() {
        DaggerSellerMenuComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            showShopInfoLoading()
            getAllShopInfo()
        }
    }

    private fun observeViewModel() {
        observeShopInfo()
        observeProductCount()
        observeNotifications()
        observeErrorToaster()
        getAllShopInfo()
    }

    private fun observeShopInfoPeriod() {
        observe(viewModel.shopAccountInfo) {
            when (it) {
                is Success -> {
                    isNewSeller = it.data.isNewSeller
                    shopAge = it.data.shopAge
                    getAllShopInfo()
                }
                is Fail -> {
                    ShopScoreReputationErrorLogger.logToCrashlytic(
                            String.format(ShopScoreReputationErrorLogger.SHOP_INFO_PM_SETTING_INFO_ERROR,
                                    ShopScoreReputationErrorLogger.SHOP_ACCOUNT), it.throwable)
                }
            }
        }
    }

    private fun observeShopInfo() {
        observe(viewModel.settingShopInfoLiveData) {
            when (it) {
                is Success -> showShopInfo(it.data.shopInfo, it.data.shopScore, it.data.shopAge)
                is Fail -> {
                    showShopInfo(SettingError)
                    ShopScoreReputationErrorLogger.logToCrashlytic(ShopScoreReputationErrorLogger.SHOP_INFO_SETTING_ERROR, it.throwable)
                }
            }
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observeProductCount() {
        observe(viewModel.shopProductLiveData) {
            when (it) {
                is Success -> adapter.showProductSection(it.data)
            }
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observeNotifications() {
        observe(viewModel.sellerMenuNotification) {
            when (it) {
                is Success -> {
                    val order = it.data.order
                    adapter.showOrderSection(order)
                    adapter.showNotificationCounter(notificationCount = it.data.resolutionCount, matcher = { item ->
                        item is SellerMenuItemUiModel && item.title == getString(com.tokopedia.seller.menu.common.R.string.setting_menu_complaint)
                    })
                }
            }
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observeErrorToaster() {
        observe(viewModel.isToasterAlreadyShown) {
            canShowErrorToaster = !it
        }
    }

    private fun showShopInfo(settingResponseState: SettingResponseState, shopScore: Int = 0, shopAge: Int = 0) {
        when (settingResponseState) {
            is SettingSuccess -> {
                if (settingResponseState is SettingShopInfoUiModel) {
                    adapter.showShopInfo(settingResponseState, shopScore, shopAge)
                    sellerMenuTracker.sendEventViewShopAccount(settingResponseState)
                }
            }
            is SettingLoading -> {
                showShopInfoLoading()
            }
            is SettingError -> {
                if (canShowErrorToaster) {
                    view?.showToasterError(resources.getString(com.tokopedia.seller.menu.common.R.string.setting_toaster_error_message))
                }
                adapter.showShopInfoError()
            }
        }
    }

    private fun getAllShopInfo() {
        viewModel.getAllSettingShopInfo(shopAge = shopAge)
        viewModel.getProductCount()
        viewModel.getNotifications()
    }

    private fun setupMenuList() {
        context?.let { context ->
            val menuList = SellerMenuList.create(context, userSession, adminPermissionMapper)

            with(listMenu) {
                adapter = this@SellerMenuFragment.adapter
                layoutManager = LinearLayoutManager(context)
            }

            adapter.addElement(menuList)
            adapter.notifyDataSetChanged()
        }
    }

    private fun View.showToasterError(errorMessage: String) {
        Toaster.build(this,
                errorMessage,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                resources.getString(com.tokopedia.seller.menu.common.R.string.setting_toaster_error_retry),
                View.OnClickListener {
                    retryFetchAfterError()
                }).show()
    }

    private fun retryFetchAfterError() {
        showShopInfoLoading()
        viewModel.getShopAccountInfo()
        viewModel.getAllSettingShopInfo(isToasterRetry = true, shopAge = shopAge)
    }

    private fun showShopInfoLoading() {
        adapter.showShopInfoLoading()
    }

    private fun setupScrollToShopSetting() {
        val isAnchorToShopSetting = activity?.intent?.getBooleanExtra(SellerMigrationConstants.SELLER_MIGRATION_KEY_AUTO_ANCHOR_ACCOUNT_SHOP, false)
                ?: false
        if (isAnchorToShopSetting) {
            val shopAccountData = adapter.data.firstOrNull { it is SellerFeatureUiModel }
            if (shopAccountData != null) {
                val positionShopAccount = adapter.data.indexOf(shopAccountData)
                val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_END
                    }
                }
                smoothScroller.targetPosition = positionShopAccount
                listMenu?.layoutManager?.startSmoothScroll(smoothScroller)
            }
        }
    }

    private fun setupToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                supportActionBar?.hide()
                setSupportActionBar(seller_menu_toolbar)
                seller_menu_toolbar?.title = getString(R.string.title_seller_menu)
            }
        }
    }

    private fun setupPMShopScoreInterrupt() {
        activity?.let {
            pmShopScoreInterruptHelper.showInterrupt(it, viewLifecycleOwner, childFragmentManager)
        }
    }
}