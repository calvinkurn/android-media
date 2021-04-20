package com.tokopedia.home.account.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.home.account.AccountConstants
import com.tokopedia.home.account.R
import com.tokopedia.home.account.analytics.AccountAnalytics
import com.tokopedia.home.account.data.util.StaticBuyerModelGenerator
import com.tokopedia.home.account.di.component.DaggerBuyerAccountComponent
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory
import com.tokopedia.home.account.presentation.adapter.buyer.BuyerAccountAdapter
import com.tokopedia.home.account.presentation.util.AccountHomeErrorHandler
import com.tokopedia.home.account.presentation.viewmodel.AccountRecommendationTitleViewModel
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel
import com.tokopedia.home.account.presentation.viewmodel.RecommendationProductViewModel
import com.tokopedia.home.account.presentation.viewmodel.TopadsHeadlineUiModel
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel
import com.tokopedia.home.account.revamp.domain.data.mapper.BuyerAccountMapper
import com.tokopedia.home.account.revamp.domain.data.mapper.BuyerAccountStaticMapper
import com.tokopedia.home.account.revamp.viewmodel.BuyerAccountViewModel
import com.tokopedia.navigation_common.listener.FragmentListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.sessioncommon.view.admin.dialog.LocationAdminDialog
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.fragment_buyer_account.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * @author okasurya on 7/16/18.
 */
class BuyerAccountFragment : BaseAccountFragment(), FragmentListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(BuyerAccountViewModel::class.java) }

    @Inject
    lateinit var buyerAccountMapper: BuyerAccountMapper

    @Inject
    lateinit var buyerAccountStaticMapper: BuyerAccountStaticMapper

    private var adapter: BuyerAccountAdapter? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private var fpmBuyer: PerformanceMonitoring? = null
    private var layoutManager: StaggeredGridLayoutManager = StaggeredGridLayoutManager(
            DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)

    private var shouldRefreshOnResume = true
    private var UOH_AB_TEST_KEY = "uoh_android_v2"
    private var UOH_AB_TEST_VALUE = "uoh_android_v2"
    private var canGoToShopAccount = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fpmBuyer = PerformanceMonitoring.start(FPM_BUYER)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_buyer_account, container, false)

        endlessRecyclerViewScrollListener = getEndlessRecyclerViewScrollListener()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        endlessRecyclerViewScrollListener?.let {
            recycler_buyer.addOnScrollListener(it)
        }

        adapter = BuyerAccountAdapter(AccountTypeFactory(this, userSession), arrayListOf())
        recycler_buyer.layoutManager = layoutManager
        recycler_buyer.adapter = adapter

        swipe_refresh_layout.setColorSchemeResources(com.tokopedia.unifyprinciples.R.color.Unify_G400)

        swipe_refresh_layout.setOnRefreshListener { this.getData() }
        sendBuyerAccountItemImpression()

        setObservers()
    }

    private fun setObservers() {
        viewModel.buyerAccountDataData.observe(viewLifecycleOwner, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    loadBuyerData(buyerAccountMapper.call(it.data))
                }
                is Fail -> {
                    if (it.throwable is UnknownHostException || it.throwable is SocketTimeoutException) {
                        showErrorNoConnection()
                    } else {
                        showError(it.throwable, AccountConstants.ErrorCodes.ERROR_CODE_BUYER_ACCOUNT)
                    }
                    loadStaticBuyerData()
                }
            }
        })

        viewModel.addWishList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    showSuccessAddWishlist()
                }
                is Fail -> {
                    if (it.throwable is UnknownHostException || it.throwable is SocketTimeoutException) {
                        showErrorNoConnection()
                    } else {
                        showError(it.throwable, AccountConstants.ErrorCodes.ERROR_CODE_BUYER_ACCOUNT)
                    }
                }
            }
        })

        viewModel.removeWishList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    showSuccessRemoveWishlist()
                }
                is Fail -> {
                    if (it.throwable is UnknownHostException || it.throwable is SocketTimeoutException) {
                        showErrorNoConnection()
                    } else {
                        showError(it.throwable, AccountConstants.ErrorCodes.ERROR_CODE_BUYER_ACCOUNT)
                    }
                }
            }
        })

        viewModel.firstRecommendation.observe(viewLifecycleOwner, Observer {
            hideLoadMoreLoading()
            when (it) {
                is Success -> {
                    val visitable = ArrayList<Visitable<*>>()
                    visitable.add(AccountRecommendationTitleViewModel(it.data.title))
                    addTopAdsHeadLine(visitable)
                    visitable.addAll(getRecommendationVisitable(it.data))
                    adapter?.addElement(visitable)
                }
                is Fail -> {
                    if (it.throwable is UnknownHostException || it.throwable is SocketTimeoutException) {
                        showErrorNoConnection()
                    } else {
                        showError(it.throwable, AccountConstants.ErrorCodes.ERROR_CODE_BUYER_ACCOUNT)
                    }
                }
            }
        })

        viewModel.recommendation.observe(viewLifecycleOwner, Observer {
            hideLoadMoreLoading()
            when (it) {
                is Success -> {
                    adapter?.addElement(getRecommendationVisitable(it.data))
                }
                is Fail -> {
                    showError(it.throwable.message ?: "")
                }
            }
        })

        viewModel.canGoToSellerAccount.observe(viewLifecycleOwner, Observer {
            canGoToShopAccount = it
        })
    }

    private fun sendBuyerAccountItemImpression() {
        onAccountItemImpression(AccountAnalytics.getAccountPromoImpression(
                AccountConstants.Analytics.CREATIVE_TOKOPOINTS, AccountConstants.Analytics.POSITION_TOKOPOINT
        ))
        onAccountItemImpression(AccountAnalytics.getAccountPromoImpression(
                AccountConstants.Analytics.CREATIVE_KUPON_SAYA, AccountConstants.Analytics.POSITION_KUPON_SAYA
        ))
        onAccountItemImpression(AccountAnalytics.getAccountPromoImpression(
                AccountConstants.Analytics.CREATIVE_TOKO_MEMBER, AccountConstants.Analytics.POSITION_TOKOMEMBER
        ))
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
        }
    }

    override fun onResume() {
        super.onResume()

        if (shouldRefreshOnResume) {
            scrollToTop()
            context?.let {
                GraphqlClient.init(it)
                getData()
            }
        } else {
            shouldRefreshOnResume = true
        }
    }

    override fun getScreenName(): String {
        return String.format("/%s/%s",
                AccountConstants.Analytics.USER,
                AccountConstants.Analytics.BELI)
    }

    private fun loadBuyerData(model: BuyerViewModel?) {
        if (model != null) {
            model.items?.let {
                adapter?.clearAllElements()
                adapter?.setElement(it)
                try {
                    Toaster.snackBar.dismiss()
                } catch (e: Exception) {
                }
            }
        } else {
            context?.let {
                adapter?.clearAllElements()
                adapter?.setElement(StaticBuyerModelGenerator.getModel(it, null, getRemoteConfig(), useUoh()))
            }
        }

        fpmBuyer?.run { stopTrace() }
        viewModel.getFirstRecommendationData()
    }

    private fun loadStaticBuyerData() {
        adapter?.clearAllElements()
        adapter?.setElement(buyerAccountStaticMapper.getStaticBuyerModel(useUoh()).items)
    }

    private fun showLoading() {
        adapter?.showLoading()
        scrollToTop()
    }

    private fun hideLoading() {
        adapter?.hideLoading()

        if (swipe_refresh_layout != null && swipe_refresh_layout.isRefreshing)
            swipe_refresh_layout.isRefreshing = false
    }

    private fun showError(message: String) {
        if (view != null && userVisibleHint) {
            view?.let {
                Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                        getString(R.string.title_try_again), View.OnClickListener { getData() })
            }
        }
        fpmBuyer?.run { stopTrace() }
    }

    private fun showError(e: Throwable, errorCode: String) {
        if (view != null && context != null && userVisibleHint) {
            val message = "${ErrorHandler.getErrorMessage(context, e)} ($errorCode)"
            view?.let {
                Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                        getString(R.string.title_try_again), View.OnClickListener { getData() })
            }
        }
        AccountHomeErrorHandler.logExceptionToCrashlytics(e, userSession.userId, userSession.email, errorCode)
        fpmBuyer?.run { stopTrace() }
    }

    private fun showErrorNoConnection() {
        showError(getString(R.string.error_no_internet_connection))
    }

    override fun onScrollToTop() {
        if (recycler_buyer != null) {
            recycler_buyer?.run {
                scrollToPosition(0)
            }
        }
    }

    override fun isLightThemeStatusBar(): Boolean {
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.addWishList.removeObservers(viewLifecycleOwner)
        viewModel.removeWishList.removeObservers(viewLifecycleOwner)
        viewModel.firstRecommendation.removeObservers(viewLifecycleOwner)
        viewModel.recommendation.removeObservers(viewLifecycleOwner)
    }

    internal override fun notifyItemChanged(position: Int) {
        adapter?.notifyItemChanged(position)
    }

    override fun onProductRecommendationClicked(product: RecommendationItem, adapterPosition: Int, widgetTitle: String) {
        sendProductClickTracking(product, adapterPosition, widgetTitle)
        activity?.let {
            if (product.isTopAds) TopAdsUrlHitter(it).hitClickUrl(it::class.qualifiedName, product.clickUrl, product.productId.toString(), product.name, product.imageUrl, COMPONENT_NAME_TOP_ADS)
        }

        RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, product.productId.toString()).run {
            putExtra(PDP_EXTRA_UPDATED_POSITION, adapterPosition)
            startActivityForResult(this, REQUEST_FROM_PDP)
        }
    }

    override fun onProductRecommendationImpression(product: RecommendationItem, adapterPosition: Int) {
        sendProductImpressionTracking(getTrackingQueue(), product, adapterPosition)
        activity?.let {
            if (product.isTopAds) TopAdsUrlHitter(it).hitImpressionUrl(it::class.qualifiedName, product.trackerImageUrl, product.productId.toString(), product.name, product.imageUrl, COMPONENT_NAME_TOP_ADS)
        }
    }

    override fun onProductRecommendationWishlistClicked(product: RecommendationItem, wishlistStatus: Boolean, callback: (Boolean, Throwable?) -> Unit) {
        sendProductWishlistClickTracking(wishlistStatus)

        if (userSession.isLoggedIn) {
            if (wishlistStatus) {
                viewModel.addWishList(product)
            } else {
                viewModel.removeWishList(product)
            }
        }
    }

    override fun onProductRecommendationThreeDotsClicked(product: RecommendationItem, adapterPosition: Int) {
        shouldRefreshOnResume = false

        showProductCardOptions(
                this,
                ProductCardOptionsModel(
                        hasWishlist = true,
                        isWishlisted = product.isWishlist,
                        productId = product.productId.toString(),
                        isTopAds = product.isTopAds,
                        topAdsWishlistUrl = product.wishlistUrl,
                        productPosition = adapterPosition
                )
        )
    }

    override fun onSellerAccountCardClicked() {
        context?.let {
            if (canGoToShopAccount) {
                RouteManager.route(it, ApplinkConstInternalSellerapp.SELLER_MENU)
            } else {
                LocationAdminDialog(it).show()
            }
        }
    }

    private fun hideLoadMoreLoading() {
        adapter?.hideLoading()
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
    }

    private fun showLoadMoreLoading() {
        adapter?.showLoading()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP) {
            data?.let {
                val id = data.getStringExtra(PDP_EXTRA_PRODUCT_ID)
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST,
                        false)
                val position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1)
                updateWishlist(wishlistStatusFromPdp, position)
            }
        }

        if (requestCode == REQUEST_CODE_CHANGE_NAME && resultCode == Activity.RESULT_OK) {
            gotoSettingProfile()
        }

        if (requestCode == REQUEST_CODE_PROFILE_SETTING) {
            getData()
        }

        handleProductCardOptionsActivityResult(requestCode, resultCode, data, object : ProductCardOptionsWishlistCallback {
            override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                handleWishlistAction(productCardOptionsModel)
            }
        })
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        sendProductWishlistClickTracking(!productCardOptionsModel.isWishlisted)

        if (productCardOptionsModel.wishlistResult.isSuccess)
            handleWishlistActionSuccess(productCardOptionsModel)
        else
            handleWishlistActionError()
    }

    private fun handleWishlistActionSuccess(productCardOptionsModel: ProductCardOptionsModel) {
        val recommendationItem = adapter?.list?.getOrNull(productCardOptionsModel.productPosition) as? RecommendationProductViewModel
                ?: return
        recommendationItem.product.isWishlist = productCardOptionsModel.wishlistResult.isAddWishlist

        if (productCardOptionsModel.wishlistResult.isAddWishlist)
            showSuccessAddWishlist()
        else
            showSuccessRemoveWishlist()
    }

    private fun showSuccessAddWishlist() {
        view?.let {
            Toaster.make(
                    it,
                    getString(com.tokopedia.wishlist.common.R.string.msg_success_add_wishlist),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    getString(R.string.account_go_to_wishlist),
                    View.OnClickListener {
                        RouteManager.route(activity, ApplinkConst.WISHLIST)
                    }
            )
        }
    }

    private fun showSuccessRemoveWishlist() {
        view?.let {
            Toaster.make(
                    it,
                    getString(com.tokopedia.wishlist.common.R.string.msg_success_remove_wishlist),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL
            )
        }
    }

    private fun handleWishlistActionError() {
        view?.let {
            Toaster.make(
                    it,
                    ErrorHandler.getErrorMessage(activity, null),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR)
        }
    }

    private fun getData() {
        scrollToTop()
        endlessRecyclerViewScrollListener?.resetState()

        context?.let {
            showLoading()
            viewModel.getBuyerData()
        }
    }

    private fun initInjector() {
        val component = DaggerBuyerAccountComponent.builder()
                .baseAppComponent(
                        (activity?.application as BaseMainApplication).baseAppComponent
                ).build()

        component.inject(this)
    }

    private fun getEndlessRecyclerViewScrollListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoadMoreLoading()
                viewModel.getRecommendationData(page)
            }
        }
    }

    private fun updateWishlist(wishlistStatusFromPdp: Boolean, position: Int) {
        if (adapter?.list?.get(position) is RecommendationProductViewModel) {
            (adapter?.list?.get(position) as RecommendationProductViewModel).product.isWishlist = wishlistStatusFromPdp
            adapter?.notifyItemChanged(position)
        }
    }

    private fun scrollToTop() {
        recycler_buyer.scrollToPosition(0)
    }

    private fun addTopAdsHeadLine(visitable: ArrayList<Visitable<*>>) {
        visitable.add(TopadsHeadlineUiModel())
    }

    private fun getRecommendationVisitable(recommendationWidget: RecommendationWidget): List<Visitable<*>> {
        val recommendationList = java.util.ArrayList<Visitable<*>>()
        for (item in recommendationWidget.recommendationItemList) {
            recommendationList.add(RecommendationProductViewModel(item, recommendationWidget.title))
        }
        return recommendationList
    }

    private fun useUoh(): Boolean {
        return try {
            val remoteConfigRollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(UOH_AB_TEST_KEY, "")

            val remoteConfigFirebase: Boolean = getRemoteConfig().getBoolean(RemoteConfigKey.ENABLE_UOH)
            (remoteConfigRollenceValue == UOH_AB_TEST_VALUE && remoteConfigFirebase)

        } catch (e: Exception) {
            false
        }
    }

    override fun onIconWarningNameClick(element: BuyerCardViewModel) {
        showBottomSheetAddName(element)
    }

    private fun showBottomSheetAddName(element: BuyerCardViewModel) {
        activity?.let {
            val addNameLayout = View.inflate(context, com.tokopedia.home_account.R.layout.layout_bottom_sheet_add_name, null)
            val btnAddName: UnifyButton = addNameLayout.findViewById(R.id.layout_bottom_sheet_add_name_button)
            val iconAddName: ImageUnify = addNameLayout.findViewById(R.id.layout_bottom_sheet_add_name_icon)
            val bottomSheet = BottomSheetUnify()

            ImageUtils.loadImage(iconAddName, URL_ICON_ADD_NAME_BOTTOM_SHEET)
            iconAddName.setOnClickListener {
                gotoChangeName(element)
                bottomSheet.dismiss()
            }
            btnAddName.setOnClickListener {
                gotoChangeName(element)
                bottomSheet.dismiss()
            }

            bottomSheet.setChild(addNameLayout)
            bottomSheet.clearAction()
            bottomSheet.setCloseClickListener {
                bottomSheet.dismiss()
            }
            childFragmentManager.run {
                bottomSheet.show(this, "bottom sheet add name")
            }
        }
    }

    private fun gotoChangeName(element: BuyerCardViewModel) {
        val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalGlobal.CHANGE_NAME, element.name, "")
        startActivityForResult(intent, REQUEST_CODE_CHANGE_NAME)
    }

    private fun gotoSettingProfile() {
        val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalGlobal.SETTING_PROFILE)
        startActivity(intent)
    }

    companion object {
        val TAG = BuyerAccountFragment::class.java.simpleName
        private val BUYER_DATA = "buyer_data"
        private val FPM_BUYER = "mp_account_buyer"
        private const val RECOMMENDATION_APP_LINK = "https://tokopedia.com/rekomendasi/%s"
        private const val SPAN_COUNT = 2
        private const val SHARE_PRODUCT_TITLE = "Bagikan Produk Ini"
        private const val SAVED_PRODUCT_ID = "saved_product_id"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val PDP_EXTRA_PRODUCT_ID = "product_id"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val REQUEST_FROM_PDP = 394
        private const val className: String = "com.tokopedia.home.account.presentation.fragment.BuyerAccountFragment"
        private const val URL_ICON_ADD_NAME_BOTTOM_SHEET = "https://images.tokopedia.net/img/android/user/profile_page/Group3082@3x.png"

        private val DEFAULT_SPAN_COUNT = 2
        private const val REQUEST_CODE_CHANGE_NAME = 300
        private const val REQUEST_CODE_PROFILE_SETTING = 301

        private const val COMPONENT_NAME_TOP_ADS = "Account Home Recommendation Top Ads"

        fun newInstance(): Fragment {
            val fragment = BuyerAccountFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}
