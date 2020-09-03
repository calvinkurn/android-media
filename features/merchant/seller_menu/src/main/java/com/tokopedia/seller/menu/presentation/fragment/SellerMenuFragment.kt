package com.tokopedia.seller.menu.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.analytics.sendShopInfoImpressionData
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
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
import com.tokopedia.seller.menu.presentation.util.SellerMenuList
import com.tokopedia.seller.menu.presentation.viewmodel.SellerMenuViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_seller_menu.*
import javax.inject.Inject

class SellerMenuFragment : Fragment(), SettingTrackingListener, ShopInfoViewHolder.ShopInfoListener,
    ShopInfoErrorViewHolder.ShopInfoErrorListener {

    companion object {
        private const val GO_TO_REPUTATION_HISTORY = "GO_TO_REPUTATION_HISTORY"
        private const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"
        private const val TOPADS_BOTTOMSHEET_TAG = "topads_bottomsheet"
    }

    @Inject
    lateinit var viewModel: SellerMenuViewModel
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var remoteConfig: RemoteConfig

    private var canShowErrorToaster = true

    private val adapter by lazy {
        SellerMenuAdapter(OtherMenuAdapterTypeFactory(
            this,
            this,
            this,
            userSession
        ))
    }

    private val topAdsBottomSheet by lazy {
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
        setupMenuList()
        setupSwipeRefresh()
        observeViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.seller_menu_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_inbox -> RouteManager.route(context, ApplinkConst.INBOX)
            R.id.action_notification -> RouteManager.route(context, ApplinkConst.NOTIFICATION)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun sendImpressionDataIris(settingShopInfoImpressionTrackable: SettingShopInfoImpressionTrackable) {
        settingShopInfoImpressionTrackable.sendShopInfoImpressionData()
    }

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
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, ApplinkConst.WebViewUrl.SALDO_DETAIL)
            context?.startActivity(intent)
        }
    }

    override fun onRefreshShopInfo() {
        showShopInfoLoading()
        getAllShopInfo()
    }

    override fun onKreditTopadsClicked() {
        val bottomSheet = childFragmentManager.findFragmentByTag(TOPADS_BOTTOMSHEET_TAG)
        if (bottomSheet is BottomSheetUnify) {
            bottomSheet.dismiss()
            RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_AUTO_TOPUP)
        } else {
            RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_CREDIT)
        }
    }

    override fun onTopAdsTooltipClicked(isTopAdsActive: Boolean) {
        val bottomSheetChildView = setupBottomSheetLayout(isTopAdsActive)
        bottomSheetChildView?.run {
            with(topAdsBottomSheet) {
                setChild(this@run)
                show(this@SellerMenuFragment.childFragmentManager, TOPADS_BOTTOMSHEET_TAG)
            }
        }
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
        observeErrorToaster()
        getAllShopInfo()
    }

    private fun observeShopInfo() {
        observe(viewModel.settingShopInfoLiveData) {
            when (it) {
                is Success -> showShopInfo(it.data)
                is Fail -> showShopInfo(SettingError)
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

    private fun observeErrorToaster() {
        observe(viewModel.isToasterAlreadyShown) {
            canShowErrorToaster = !it
        }
    }

    private fun showShopInfo(settingResponseState: SettingResponseState) {
        when (settingResponseState) {
            is SettingSuccess -> {
                if (settingResponseState is SettingShopInfoUiModel) {
                    adapter.showShopInfo(settingResponseState)
                }
            }
            is SettingLoading -> {
                showShopInfoLoading()
            }
            is SettingError -> {
                if (canShowErrorToaster) {
                    view?.showToasterError(resources.getString(R.string.setting_toaster_error_message))
                }
                adapter.showShopInfoError()
            }
        }
    }

    private fun getAllShopInfo() {
        viewModel.getAllSettingShopInfo()
        viewModel.getProductCount()
    }

    private fun setupMenuList() {
        context?.let { context ->
            val menuList = SellerMenuList.create(context, userSession)

            with(listMenu) {
                adapter = this@SellerMenuFragment.adapter
                layoutManager = LinearLayoutManager(context)
            }

            adapter.addElement(menuList)
            adapter.notifyDataSetChanged()
        }
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

    private fun retryFetchAfterError() {
        showShopInfoLoading()
        viewModel.getAllSettingShopInfo(isToasterRetry = true)
    }

    private fun showShopInfoLoading() {
        adapter.showShopInfoLoading()
    }

    private fun setupBottomSheetLayout(isTopAdsActive: Boolean) : View? {
        val bottomSheetInfix: String
        val bottomSheetDescription: String
        if (isTopAdsActive) {
            bottomSheetInfix = resources.getString(R.string.setting_topads_status_active)
            bottomSheetDescription = resources.getString(R.string.setting_topads_description_active)
        } else {
            bottomSheetInfix = resources.getString(R.string.setting_topads_status_inactive)
            bottomSheetDescription = resources.getString(R.string.setting_topads_description_inactive)
        }
        val bottomSheetTitle = resources.getString(R.string.setting_topads_status, bottomSheetInfix)
        return topAdsBottomSheetView?.apply {
            findViewById<Typography>(R.id.topAdsBottomSheetTitle).text = bottomSheetTitle
            findViewById<TextView>(R.id.topAdsBottomSheetDescription).text = bottomSheetDescription
            findViewById<UnifyButton>(R.id.topAdsNextButton).setOnClickListener{
                onKreditTopadsClicked()
            }
        }
    }

    private fun goToReputationHistory() {
        val reputationHistoryIntent = RouteManager.getIntent(context, ApplinkConst.REPUTATION).apply {
            putExtra(GO_TO_REPUTATION_HISTORY, true)
        }
        startActivity(reputationHistoryIntent)
    }

    private fun goToShopFavouriteList() {
        val shopFavouriteListIntent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_FAVOURITE_LIST).apply {
            putExtra(EXTRA_SHOP_ID, userSession.shopId)
        }
        startActivity(shopFavouriteListIntent)
    }
}