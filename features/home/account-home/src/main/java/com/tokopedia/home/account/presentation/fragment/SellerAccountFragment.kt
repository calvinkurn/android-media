package com.tokopedia.home.account.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home.account.AccountConstants
import com.tokopedia.home.account.R
import com.tokopedia.home.account.data.mapper.SellerAccountMapper
import com.tokopedia.home.account.di.component.DaggerSellerAccountComponent
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory
import com.tokopedia.home.account.presentation.adapter.seller.SellerAccountAdapter
import com.tokopedia.home.account.presentation.listener.AccountItemListener
import com.tokopedia.home.account.presentation.util.AccountHomeErrorHandler.logExceptionToCrashlytics
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel
import com.tokopedia.home.account.presentation.viewmodel.TickerViewModel
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel
import com.tokopedia.home.account.revamp.viewmodel.SellerAccountViewModel
import com.tokopedia.navigation_common.listener.FragmentListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTracking.eventOnClickAccountTicker
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.seller_migration_common.getSellerMigrationDate
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.presentation.widget.SellerMigrationAccountBottomSheet
import com.tokopedia.seller_migration_common.presentation.widget.SellerMigrationVoucherTokoBottomSheet
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.make
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

class SellerAccountFragment : BaseAccountFragment(), AccountItemListener, FragmentListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(SellerAccountViewModel::class.java) }

    @Inject
    lateinit var sellerAccountMapper: SellerAccountMapper

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SellerAccountAdapter
    private lateinit var fpmSeller: PerformanceMonitoring
    private lateinit var migrationTicker: Ticker
    private lateinit var sellerMigrationVoucherTokoBottomSheet: SellerMigrationVoucherTokoBottomSheet

    private var isLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fpmSeller = PerformanceMonitoring.start(FPM_SELLER)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_seller_account, container, false)
        migrationTicker = view.findViewById(R.id.account_seller_migration_ticker)
        recyclerView = view.findViewById(R.id.recycler_seller)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        swipeRefreshLayout.setColorSchemeResources(R.color.tkpd_main_green)
        return view
    }

    override fun onResume() {
        super.onResume()
        isLoaded = false
        getData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()

        adapter = SellerAccountAdapter(AccountTypeFactory(this), ArrayList())
        recyclerView.adapter = adapter
        setupSellerMigrationTicker()
        swipeRefreshLayout.setOnRefreshListener {
            isLoaded = false
            getData()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            getData()
            TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
        }
    }

    private fun getData() {
        if (!isLoaded && context != null) {
            showLoading()
            viewModel.getSellerData(userSession.shopId)
            isLoaded = !isLoaded
        }
    }

    private fun initObserver() {
        viewModel.sellerData.observe(viewLifecycleOwner, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    loadSellerData(sellerAccountMapper.call(it.data))
                }
                is Fail -> {
                    if (it.throwable is UnknownHostException || it.throwable is SocketTimeoutException) {
                        showErrorNoConnection()
                    } else {
                        showError(it.throwable, AccountConstants.ErrorCodes.ERROR_CODE_SELLER_ACCOUNT)
                    }
                }
            }
        })
    }

    private fun loadSellerData(model: SellerViewModel) {
        if (model.items != null) {
            adapter.clearAllElements()
            adapter.setElement(model.items)
        }
        fpmSeller.stopTrace()
    }

    override fun getScreenName(): String {
        return String.format("/%s/%s",
                AccountConstants.Analytics.USER,
                AccountConstants.Analytics.JUAL)
    }

    private fun initInjector() {
        DaggerSellerAccountComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun showLoading() {
        adapter.showLoading()
    }

    private fun hideLoading() {
        adapter.hideLoading()
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    fun showError(message: String) {
        view?.let {
            if (userVisibleHint) {
                make(it, message, Snackbar.LENGTH_LONG, TYPE_ERROR,
                        getString(R.string.title_try_again),
                        View.OnClickListener {
                            getData()
                        })
            }
            fpmSeller.stopTrace()
        }
    }

    fun showError(e: Throwable, errorCode: String) {
        view?.let {
            if (userVisibleHint) {
                val message = String.format("%s (%s)", ErrorHandler.getErrorMessage(activity, e), errorCode)
                make(it, message, Snackbar.LENGTH_LONG, TYPE_ERROR,
                        getString(R.string.title_try_again), View.OnClickListener {
                    getData()
                })
            }
            logExceptionToCrashlytics(e, userSession.userId, userSession.email, errorCode)
            fpmSeller.stopTrace()
        }
    }

    fun showErrorNoConnection() {
        showError(getString(R.string.error_no_internet_connection))
    }

    override fun onScrollToTop() {
        recyclerView.scrollToPosition(0)
    }

    override fun isLightThemeStatusBar(): Boolean {
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == OPEN_SHOP_SUCCESS) {
            isLoaded = false
            data
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PHONE_VERIFICATION) {
            userSession.setIsMSISDNVerified(true)
            moveToCreateShop()
        }
    }

    override fun onTickerClosed() {
        if (adapter.itemCount > 0 &&
                adapter.getItemAt(0) is TickerViewModel) {
            adapter.removeElementAt(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.sellerData.removeObservers(viewLifecycleOwner)
    }

    public override fun notifyItemChanged(position: Int) {
        adapter.notifyItemChanged(position)
    }

    override fun onMenuListClicked(item: MenuListViewModel) {
        if (item.menu.equals(getString(R.string.title_menu_voucher_toko), ignoreCase = true)) {
            sendTracking(item.titleTrack, item.sectionTrack, item.menu)
            showSellerMigrationVoucherTokoBottomSheet()
        } else {
            super.onMenuListClicked(item)
        }
    }

    override fun onProductRecommendationClicked(product: RecommendationItem, adapterPosition: Int, widgetTitle: String) {}
    override fun onProductRecommendationImpression(product: RecommendationItem, adapterPosition: Int) {}
    override fun onProductRecommendationWishlistClicked(product: RecommendationItem, wishlistStatus: Boolean, callback: (Boolean, Throwable?) -> Unit) {}
    override fun onProductRecommendationThreeDotsClicked(product: RecommendationItem, adapterPosition: Int) {}
    private fun setupSellerMigrationTicker() {
        if (isSellerMigrationEnabled(this.context)) {
            migrationTicker.tickerTitle = getString(com.tokopedia.seller_migration_common.R.string.seller_migration_account_home_ticker_title)
            val remoteConfigDate = getSellerMigrationDate(this.context)
            if (remoteConfigDate.isEmpty()) {
                migrationTicker.setHtmlDescription(getString(com.tokopedia.seller_migration_common.R.string.seller_migration_generic_ticker_content))
            } else {
                migrationTicker.setHtmlDescription(getString(com.tokopedia.seller_migration_common.R.string.seller_migration_account_home_ticker_content, remoteConfigDate))
            }
            migrationTicker.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(charSequence: CharSequence) {
                    eventOnClickAccountTicker(userSession.userId)
                    openSellerMigrationBottomSheet()
                }

                override fun onDismiss() {
                    // No op
                }
            })
        } else {
            migrationTicker.visibility = View.GONE
        }
    }

    private fun openSellerMigrationBottomSheet() {
        context?.let {
            val sellerMigrationBottomSheet: BottomSheetUnify = SellerMigrationAccountBottomSheet.createNewInstance(it)
            sellerMigrationBottomSheet.show(childFragmentManager, SellerMigrationConstants.TAG_SELLER_MIGRATION_BOTTOM_SHEET)
        }
    }

    private fun showSellerMigrationVoucherTokoBottomSheet() {
        context?.let {
            sellerMigrationVoucherTokoBottomSheet = SellerMigrationVoucherTokoBottomSheet.createNewInstance(it)
            sellerMigrationVoucherTokoBottomSheet.show(childFragmentManager, SellerMigrationConstants.TAG_SELLER_MIGRATION_BOTTOM_SHEET)
        }
    }

    companion object {
        private val TAG = SellerAccountFragment::class.java.simpleName
        private const val FPM_SELLER = "mp_account_seller"

        fun newInstance(): SellerAccountFragment {
            val fragment = SellerAccountFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}