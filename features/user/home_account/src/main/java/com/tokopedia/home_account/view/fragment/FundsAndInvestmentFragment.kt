package com.tokopedia.home_account.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.AccountConstants.Analytics.Screen.SCREEN_FUNDS_AND_INVESTMENT
import com.tokopedia.home_account.R
import com.tokopedia.home_account.ResultBalanceAndPoint
import com.tokopedia.home_account.analytics.HomeAccountAnalytics
import com.tokopedia.home_account.data.model.CentralizedUserAssetConfig
import com.tokopedia.home_account.data.model.WalletappGetAccountBalance
import com.tokopedia.home_account.databinding.FundsAndInvestmentFragmentBinding
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.view.HomeAccountUserViewModel
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity
import com.tokopedia.home_account.view.adapter.HomeAccountFundsAndInvestmentAdapter
import com.tokopedia.home_account.view.adapter.uimodel.SubtitleUiModel
import com.tokopedia.home_account.view.adapter.uimodel.TitleUiModel
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel
import com.tokopedia.home_account.view.listener.WalletListener
import com.tokopedia.home_account.view.listener.onAppBarCollapseListener
import com.tokopedia.home_account.view.mapper.UiModelMapper
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import javax.inject.Inject


open class FundsAndInvestmentFragment : BaseDaggerFragment(), WalletListener {

    @Inject
    lateinit var homeAccountAnalytic: HomeAccountAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(HomeAccountUserViewModel::class.java) }

    private val binding by viewBinding(FundsAndInvestmentFragmentBinding::bind)
    private var adapter: HomeAccountFundsAndInvestmentAdapter? = null

    var appBarCollapseListener: onAppBarCollapseListener? = null

    override fun getScreenName(): String = SCREEN_FUNDS_AND_INVESTMENT

    override fun initInjector() {
        getComponent(HomeAccountUserComponents::class.java).inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeAccountUserActivity) appBarCollapseListener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.funds_and_investment_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        initView()

        showLoading()
        viewModel.getCentralizedUserAssetConfig(USER_CENTRALIZED_ASSET_CONFIG_ASSET_PAGE)
    }

    override fun onStart() {
        super.onStart()
        homeAccountAnalytic.trackScreen(screenName)
        homeAccountAnalytic.eventViewAssetPage()
    }

    override fun onClickWallet(walletUiModel: WalletUiModel) {
        homeAccountAnalytic.eventClickAssetPage(
            walletUiModel.id,
            walletUiModel.isActive,
            walletUiModel.isFailed
        )
        if (walletUiModel.isFailed) {
            adapter?.changeItemToShimmer(UiModelMapper.getWalletShimmeringUiModel(walletUiModel))
            viewModel.getBalanceAndPoint(walletUiModel.id)
        } else if (!walletUiModel.applink.isEmpty()) {
            goToApplink(walletUiModel.applink)
        }
    }

    private fun initView() {
        setupAdapter()
        setupSwipeRefresh()
    }

    private fun setupObserver() {
        viewModel.centralizedUserAssetConfig.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetCentralizedAssetConfig(it.data)
                }
                is Fail -> {
                    onFailedGetCentralizedAssetConfig()
                }
            }
        })

        viewModel.balanceAndPoint.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultBalanceAndPoint.Success -> {
                    onSuccessGetBalanceAndPoint(it.data)
                }
                is ResultBalanceAndPoint.Fail -> {
                    onFailedGetBalanceAndPoint(it.walletId)
                }
            }
        })
    }

    private fun onSuccessGetCentralizedAssetConfig(centralizedUserAssetConfig: CentralizedUserAssetConfig) {
        hideLoading()
        addTitleView()
        if (centralizedUserAssetConfig.assetConfigVertical.isNotEmpty()) {
            centralizedUserAssetConfig.assetConfigVertical.forEach {
                adapter?.addItemAndAnimateChanges(UiModelMapper.getWalletShimmeringUiModel(it))
                viewModel.getBalanceAndPoint(it.id)
            }
        }

        if (centralizedUserAssetConfig.assetConfigHorizontal.isNotEmpty()) {
            addSubtitleView()
            centralizedUserAssetConfig.assetConfigHorizontal.forEach {
                if (it.id == AccountConstants.WALLET.CO_BRAND_CC) {
                    adapter?.addItemAndAnimateChanges(UiModelMapper.getWalletShimmeringUiModel(it))
                    viewModel.getBalanceAndPoint(it.id)
                } else {
                    adapter?.addItemAndAnimateChanges(UiModelMapper.getWalletUiModel(it))
                }
            }
        }

        if (adapter?.isWalletExistById(AccountConstants.WALLET.GOPAY).orFalse() ||
                adapter?.isWalletExistById(AccountConstants.WALLET.GOPAYLATER).orFalse()) {
            adapter?.removeById(AccountConstants.WALLET.TOKOPOINT)
        }
    }

    private fun onFailedGetCentralizedAssetConfig() {
        hideLoading()
        showErrorState()
    }

    private fun onSuccessGetBalanceAndPoint(balanceAndPoint: WalletappGetAccountBalance) {
        if (balanceAndPoint.id == AccountConstants.WALLET.CO_BRAND_CC && balanceAndPoint.isActive) {
            val wallet = UiModelMapper.getWalletUiModel(
                balanceAndPoint
            ).apply {
                this.isVertical = false
            }

            if (balanceAndPoint.isActive) {
                adapter?.moveWalletAboveSubtitle(wallet)
                if (adapter?.getSubtitleIndex() == adapter?.lastIndex) {
                    adapter?.removeSubtitle()
                }
            } else {
                adapter?.changeItemToSuccessBySameId(wallet)
            }
        } else {
            adapter?.changeItemToSuccessBySameId(
                    UiModelMapper.getWalletUiModel(
                            balanceAndPoint
                    ).apply {
                        if (balanceAndPoint.id == AccountConstants.WALLET.CO_BRAND_CC) {
                            this.isActive = true
                        }
                        this.isVertical = true
                    }
            )
        }
    }

    private fun onFailedGetBalanceAndPoint(walletId: String) {
        adapter?.changeItemToFailedById(walletId)
    }

    private fun setupAdapter() {
        adapter = HomeAccountFundsAndInvestmentAdapter(this)
        binding?.fundsAndInvestmentRv?.adapter = adapter
        val layoutManager = LinearLayoutManager(
            binding?.fundsAndInvestmentRv?.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding?.fundsAndInvestmentRv?.layoutManager = layoutManager
    }

    private fun setupSwipeRefresh() {
        binding?.fundsAndInvestmentRv?.swipeLayout = binding?.homeAccountUserFragmentSwipeRefresh
        binding?.homeAccountUserFragmentSwipeRefresh?.setOnRefreshListener {
            onRefresh()
            binding?.homeAccountUserFragmentSwipeRefresh?.isRefreshing = false
        }
    }

    private fun addTitleView() {
        adapter?.addItemAndAnimateChanges(TitleUiModel(getString(R.string.funds_and_investment_balance_and_points)))
    }

    private fun addSubtitleView() {
        adapter?.addItemAndAnimateChanges(SubtitleUiModel(getString(R.string.funds_and_investment_try_another)))
    }

    private fun onRefresh() {
        showLoading()
        adapter?.clearAllItemsAndAnimateChanges()
        viewModel.getCentralizedUserAssetConfig(USER_CENTRALIZED_ASSET_CONFIG_ASSET_PAGE)
    }

    private fun showLoading() {
        binding?.fundsAndInvestmentShimmer?.root?.show()
        binding?.fundsAndInvestmentRv?.hide()
    }

    private fun hideLoading() {
        binding?.fundsAndInvestmentShimmer?.root?.hide()
        binding?.fundsAndInvestmentRv?.show()
    }

    private fun showErrorState() {
        binding?.fundsAndInvestmentEmptyState?.show()
        binding?.fundsAndInvestmentRv?.hide()
        binding?.fundsAndInvestmentEmptyState?.setImageUrl(FAILED_IMG_URL)
        binding?.fundsAndInvestmentEmptyState?.setPrimaryCTAClickListener {
            hideErrorState()
            onRefresh()
        }
    }

    private fun hideErrorState() {
        binding?.fundsAndInvestmentEmptyState?.hide()
        binding?.fundsAndInvestmentRv?.show()
    }

    private fun goToApplink(applink: String) {
        if (applink.isNotEmpty()) {
            val intent = RouteManager.getIntent(context, applink)
            startActivity(intent)
        }
    }

    companion object {

        private const val FAILED_IMG_URL =
            "https://images.tokopedia.net/img/android/user/failed_fund_and_investment.png"
        private const val USER_CENTRALIZED_ASSET_CONFIG_ASSET_PAGE = "asset_page"

        fun newInstance(bundle: Bundle?): Fragment {
            return FundsAndInvestmentFragment().apply {
                arguments = bundle
            }
        }
    }
}