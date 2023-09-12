package com.tokopedia.buy_more_get_more.olp.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.FragmentOfferLandingPageBinding
import com.tokopedia.buy_more_get_more.olp.di.component.DaggerBuyMoreGetMoreComponent
import com.tokopedia.buy_more_get_more.olp.domain.entity.EmptyStateUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.OlpEvent
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductSortingUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.enum.Status
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapter
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapterTypeFactoryImpl
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.decoration.ProductListItemDecoration
import com.tokopedia.buy_more_get_more.olp.presentation.bottomsheet.TncBottomSheet
import com.tokopedia.buy_more_get_more.olp.presentation.listener.AtcProductListener
import com.tokopedia.buy_more_get_more.olp.presentation.listener.OfferingInfoListener
import com.tokopedia.buy_more_get_more.olp.utils.constant.BundleConstant
import com.tokopedia.buy_more_get_more.olp.utils.constant.Constant
import com.tokopedia.buy_more_get_more.olp.utils.extension.setDefaultStatusBar
import com.tokopedia.buy_more_get_more.olp.utils.extension.setTransparentStatusBar
import com.tokopedia.buy_more_get_more.olp.utils.tracker.OlpTracker
import com.tokopedia.buy_more_get_more.sort.activity.ShopProductSortActivity
import com.tokopedia.buy_more_get_more.sort.listener.ProductSortListener
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.campaign.helper.BuyMoreGetMoreHelper
import com.tokopedia.campaign.utils.extension.doOnDelayFinished
import com.tokopedia.campaign.utils.extension.showToaster
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongSafely
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.extensions.view.visibleWithCondition
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.resources.isDarkMode
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject

class OfferLandingPageFragment :
    BaseListFragment<Visitable<*>, AdapterTypeFactory>(),
    ProductSortListener,
    AtcProductListener,
    OfferingInfoListener,
    SwipeRefreshLayout.OnRefreshListener,
    HasPaginatedList by HasPaginatedListImpl() {

    companion object {
        @JvmStatic
        fun newInstance(
            shopId: String,
            offerId: String,
            warehouseIds: String,
            productIds: String
        ) = OfferLandingPageFragment().apply {
            arguments = Bundle().apply {
                putString(BundleConstant.BUNDLE_SHOP_ID, shopId)
                putString(BundleConstant.BUNDLE_OFFER_ID, offerId)
                putString(BuyMoreGetMoreHelper.KEY_WAREHOUSE_IDS, warehouseIds)
                putString(BuyMoreGetMoreHelper.KEY_PRODUCT_IDS, productIds)
            }
        }

        private const val REQUEST_CODE_USER_LOGIN = 101
        private const val REQUEST_CODE_USER_LOGIN_CART = 102
        private const val REQUEST_CODE_SORT = 308
        private const val VIEW_CONTENT = 1
        private const val VIEW_LOADING = 2
        private const val VIEW_ERROR = 3
        private const val FIRST_PAGE = 1
        private const val PAGE_SIZE = 10
        private const val PRODUCT_LIST_SPAN_COUNT = 2
        private const val MINI_CART_REFRESH_DELAY = 800L
    }

    private var binding by autoClearedNullable<FragmentOfferLandingPageBinding>()

    @Inject
    lateinit var tracker: OlpTracker

    private val olpAdapter: OlpAdapter?
        get() = adapter as? OlpAdapter

    private val localCacheModel by lazy {
        context?.let {
            ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private val isLogin: Boolean
        get() = viewModel.isLogin

    private val currentState: OfferInfoForBuyerUiModel.OlpUiState
        get() = viewModel.currentState

    private val olpAdapterTypeFactory by lazy {
        OlpAdapterTypeFactoryImpl(this, this, this)
    }
    private var sortId = ""
    private var sortName = ""
    private var tncBottomSheet: TncBottomSheet? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    @Inject
    lateinit var viewModel: OfferLandingPageViewModel

    private val shopIds by lazy { arguments?.getString(BundleConstant.BUNDLE_SHOP_ID).orEmpty() }
    private val offerId by lazy { arguments?.getString(BundleConstant.BUNDLE_OFFER_ID).orEmpty() }
    private val warehouseIds by lazy {
        arguments?.getString(BuyMoreGetMoreHelper.KEY_WAREHOUSE_IDS).orEmpty()
    }
    private val productIds by lazy {
        arguments?.getString(BuyMoreGetMoreHelper.KEY_PRODUCT_IDS).orEmpty()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerBuyMoreGetMoreComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .build()
            .inject(this)
    }

    override fun loadData(page: Int) {}

    override fun getAdapterTypeFactory(): AdapterTypeFactory {
        return olpAdapterTypeFactory
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun onResume() {
        super.onResume()
        doOnDelayFinished(MINI_CART_REFRESH_DELAY) {
            viewModel.processEvent(OlpEvent.GetNotification)
            fetchMiniCart()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOfferLandingPageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        initMiniCart()
        setupObservables()
        setupProductRv()
        swipeRefreshLayout = binding?.swLayout
        swipeRefreshLayout?.apply {
            setOnRefreshListener(this@OfferLandingPageFragment)
            isEnabled = true
            setOnRefreshListener { loadInitialData() }
        }
        tracker.sendOpenScreenEvent()
    }

    private fun initMiniCart() {
        binding?.miniCartView?.init(lifecycleOwner = viewLifecycleOwner)
    }

    private fun setupObservables() {
        viewModel.offeringInfo.observe(viewLifecycleOwner) { offerInfoForBuyer ->
            when (offerInfoForBuyer.responseHeader.status) {
                Status.SUCCESS -> {
                    setupHeader(offerInfoForBuyer)
                    viewModel.processEvent(OlpEvent.SetWarehouseIds(offerInfoForBuyer.nearestWarehouseIds))
                    viewModel.processEvent(OlpEvent.SetShopData(offerInfoForBuyer.offerings.firstOrNull()?.shopData))
                    viewModel.processEvent(OlpEvent.SetOfferingJsonData(offerInfoForBuyer.offeringJsonData))
                    viewModel.processEvent(OlpEvent.SetTncData(offerInfoForBuyer.offerings.firstOrNull()?.tnc.orEmpty()))
                    setupTncBottomSheet()
                    fetchMiniCart()
                    setMiniCartOnOfferEnd(offerInfoForBuyer)
                }

                else -> {
                    setViewState(VIEW_ERROR, offerInfoForBuyer.responseHeader.status)
                }
            }
        }

        viewModel.productList.observe(viewLifecycleOwner) { productList ->
            if (productList.totalProduct.isMoreThanZero()) {
                setupProductList(productList)
                setViewState(VIEW_CONTENT)
                notifyLoadResult(productList.productList.size >= PAGE_SIZE)
            } else {
                setViewState(VIEW_ERROR, Status.OOS)
            }
        }

        viewModel.navNotificationLiveData.observe(viewLifecycleOwner) { notification ->
            updateCartCounter(notification.totalCart)
        }

        viewModel.miniCartAdd.observe(viewLifecycleOwner) { atc ->
            when (atc) {
                is Success -> {
                    binding?.apply {
                        miniCartView.showToaster(atc.data.data.message.firstOrNull().orEmpty())
                        miniCartView.refreshAfterAtC()
                    }
                    viewModel.processEvent(OlpEvent.GetNotification)
                }

                is Fail -> {
                    setDefaultErrorSelection(atc.throwable)
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { throwable ->
            setDefaultErrorSelection(throwable)
        }
    }

    private fun setMiniCartOnOfferEnd(offerInfoForBuyer: OfferInfoForBuyerUiModel?) {
        binding?.miniCartView?.run {
            val offer = offerInfoForBuyer?.offerings?.firstOrNull() ?: return@run
            setOnCheckCartClickListener(offer.endDate) { isOfferEnded ->
                if (isOfferEnded) {
                    setViewState(VIEW_ERROR, Status.OFFER_ALREADY_FINISH)
                }
            }
        }
    }

    private fun setupHeader(offerInfoForBuyer: OfferInfoForBuyerUiModel) {
        setupToolbar(offerInfoForBuyer)
        binding?.headerBackground?.setBackgroundResource(R.drawable.olp_header)
        binding?.headerOverlay?.visibleWithCondition(activity?.isDarkMode() == true)
        olpAdapter?.submitList(
            newList = listOf(
                offerInfoForBuyer,
                OfferProductSortingUiModel()
            )
        )
        viewModel.processEvent(OlpEvent.GetNotification)
        getProductListData(FIRST_PAGE)
    }

    private fun setupProductList(offerProductList: OfferProductListUiModel) {
        olpAdapter?.apply {
            updateProductCount(offerProductList.totalProduct)
            setProductListData(offerProductList.productList)
            changeSelectedSortFilter(currentState.sortId, currentState.sortName)
        }
    }

    private fun setupToolbar(offerInfoForBuyer: OfferInfoForBuyerUiModel) {
        binding?.header?.apply {
            title = getString(R.string.bmgm_title)
            subTitle = offerInfoForBuyer.offerings.firstOrNull()?.offerName.orEmpty()
            setNavigationOnClickListener {
                tracker.sendClickBackButtonEvent(
                    offerInfoForBuyer.offerings.firstOrNull()?.id.toString(),
                    offerInfoForBuyer.nearestWarehouseIds.toSafeString()
                )
                activity?.finish()
            }
            showShareButton = true
            shareButton?.setOnClickListener {
                //get sharing data
                tracker.sendClickShareButtonEvent(
                    offerInfoForBuyer.offerings.firstOrNull()?.id.toString(),
                    offerInfoForBuyer.nearestWarehouseIds.toSafeString()
                )
                openShareBottomSheet()
            }
            cartButton?.setOnClickListener {
                tracker.sendClickKeranjangButtonEvent(
                    offerInfoForBuyer.offerings.firstOrNull()?.id.toString(),
                    offerInfoForBuyer.nearestWarehouseIds.toSafeString()
                )
                redirectToCartPage()
            }
            moreMenuButton?.setOnClickListener {
                tracker.sendClickBurgerButtonEvent(
                    offerInfoForBuyer.offerings.firstOrNull()?.id.toString(),
                    offerInfoForBuyer.nearestWarehouseIds.toSafeString()
                )
                redirectToMainMenu()
            }
            showWhiteToolbar = false
        }
    }

    private fun renderSortFilter(sortId: String, sortName: String) {
        resetPaging()
        olpAdapter?.apply {
            changeSelectedSortFilter(sortId, sortName)
            removeProductList()
        }
        viewModel.processEvent(OlpEvent.SetSort(sortId, sortName))
        getProductListData(page = FIRST_PAGE)
    }

    private fun getProductListData(page: Int) {
        if (page == FIRST_PAGE) olpAdapter?.removeProductList()
        viewModel.processEvent(OlpEvent.GetOffreringProductList(page = page, pageSize = PAGE_SIZE))
    }

    override fun loadInitialData() {
        setViewState(VIEW_LOADING)
        resetPaging()
        viewModel.processEvent(
            OlpEvent.SetSort(
                sortId = Constant.DEFAULT_SORT_ID,
                sortName = Constant.DEFAULT_SORT_NAME
            )
        )
        viewModel.processEvent(
            OlpEvent.SetInitialUiState(
                offerIds = listOf(offerId.toLongSafely()),
                shopIds = shopIds.toLongSafely(),
                productIds = if (productIds.isNotEmpty()) {
                    productIds.split(",").map { it.toLongSafely() }
                } else {
                    emptyList()
                },
                warehouseIds = if (warehouseIds.isNotEmpty()) {
                    warehouseIds.split(",").map { it.toLongSafely() }
                } else {
                    emptyList()
                },
                localCacheModel = localCacheModel
            )
        )
        viewModel.processEvent(OlpEvent.GetOfferingInfo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SORT -> {
                if (resultCode == Activity.RESULT_OK) {
                    tracker.sendClickFilterButtonEvent(
                        currentState.offerIds.toSafeString(),
                        currentState.warehouseIds.toSafeString()
                    )
                    sortId = data?.getStringExtra(ShopProductSortActivity.SORT_VALUE) ?: ""
                    sortName = data?.getStringExtra(ShopProductSortActivity.SORT_NAME) ?: ""
                    renderSortFilter(sortId, sortName)
                }
            }

            REQUEST_CODE_USER_LOGIN -> {
                if (resultCode == Activity.RESULT_OK) {
                    loadInitialData()
                }
            }

            REQUEST_CODE_USER_LOGIN_CART -> {
                if (resultCode == Activity.RESULT_OK) {
                    loadInitialData()
                    redirectToCartPage()
                }
            }
        }
        AtcVariantHelper.onActivityResultAtcVariant(requireContext(), requestCode, data) {
            if (atcMessage.isNotEmpty()) {
                binding?.miniCartView.showToaster(atcMessage)
            }
            fetchMiniCart()
        }
    }

    override fun onSortChipClicked() {
        tracker.sendClickFilterDropdownButtonEvent(
            currentState.offerIds.toSafeString(),
            currentState.warehouseIds.toSafeString()
        )
        context?.run {
            val intent = ShopProductSortActivity.createIntent(activity, currentState.sortId)
            startActivityForResult(intent, REQUEST_CODE_SORT)
        }
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.rv_olp
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, AdapterTypeFactory> {
        return OlpAdapter(olpAdapterTypeFactory)
    }

    private fun setupProductRv() {
        getRecyclerView(view)?.apply {
            this.layoutManager = StaggeredGridLayoutManager(
                PRODUCT_LIST_SPAN_COUNT,
                StaggeredGridLayoutManager.VERTICAL
            )
            addItemDecoration(ProductListItemDecoration())
            val config = HasPaginatedList.Config(
                pageSize = PAGE_SIZE,
                onLoadNextPage = {},
                onLoadNextPageFinished = {}
            )
            attachPaging(this, config) { page, _ ->
                getProductListData(page)
            }
        }
    }

    private fun setupTncBottomSheet() {
        tncBottomSheet = TncBottomSheet.newInstance(currentState.tnc)
    }

    private fun setViewState(viewState: Int, status: Status = Status.SUCCESS) {
        swipeRefreshLayout?.isRefreshing = false
        swipeRefreshLayout?.isEnabled = true
        when (viewState) {
            VIEW_LOADING -> {
                activity?.setDefaultStatusBar()
                binding?.apply {
                    loadingStateOlp.root.visible()
                    statusBar.gone()
                    header.gone()
                    headerBackground.gone()
                    headerOverlay.gone()
                    stickyContent.gone()
                    errorPageLarge.gone()
                    miniCartView.gone()
                }
            }

            VIEW_ERROR -> {
                activity?.setDefaultStatusBar()
                viewModel.processEvent(OlpEvent.GetNotification)
                when (status) {
                    Status.INVALID_OFFER_ID -> {
                        setErrorPage(
                            title = getString(R.string.bmgm_title_error_not_found),
                            description = getString(R.string.bmgm_description_error_not_found),
                            errorType = GlobalError.PAGE_NOT_FOUND,
                            primaryCtaText = getString(R.string.bmgm_cta_text_error_not_found),
                            primaryCtaAction = { loadInitialData() },
                            imageUrl = TokopediaImageUrl.ILLUSTRATION_SHOP_ETALASE_NOT_FOUND,
                            isShowProductList = false
                        )
                    }

                    Status.OFFER_ALREADY_FINISH -> {
                        setErrorPage(
                            title = getString(R.string.bmgm_title_error_ended_promo),
                            description = getString(
                                R.string.bmgm_description_error_ended_promo,
                                currentState.shopData.shopName
                            ),
                            errorType = GlobalError.PAGE_NOT_FOUND,
                            primaryCtaText = getString(R.string.bmgm_cta_text_error_ended_promo),
                            primaryCtaAction = { activity?.finish() },
                            imageUrl = TokopediaImageUrl.ILLUSTRATION_SHOP_ETALASE_NOT_FOUND,
                            isShowProductList = true
                        )
                    }

                    Status.OOS -> {
                        setErrorPage(
                            title = getString(R.string.bmgm_title_error_out_of_stock),
                            description = getString(
                                R.string.bmgm_description_error_out_of_stock,
                                currentState.shopData.shopName
                            ),
                            errorType = GlobalError.PAGE_NOT_FOUND,
                            primaryCtaText = getString(R.string.bmgm_cta_text_error_out_of_stock),
                            primaryCtaAction = { activity?.finish() },
                            imageUrl = TokopediaImageUrl.ILLUSTRATION_SHOP_ETALASE_NOT_FOUND,
                            isShowProductList = true
                        )
                    }

                    Status.NO_CONNECTION -> {
                        setErrorPage(
                            title = getString(R.string.bmgm_title_error_no_internet_connection),
                            description = getString(R.string.bmgm_description_error_no_internet_connection),
                            errorType = GlobalError.NO_CONNECTION,
                            primaryCtaText = getString(R.string.bmgm_cta_text_error_no_internet_connection),
                            primaryCtaAction = { loadInitialData() },
                            imageUrl = TokopediaImageUrl.REVIEW_INBOX_UNIFY_GLOBAL_ERROR_CONNECTION,
                            isShowProductList = false
                        )
                    }

                    else -> {
                        setErrorPage(
                            title = getString(R.string.bmgm_title_error_server),
                            description = getString(R.string.bmgm_description_error_server),
                            errorType = GlobalError.SERVER_ERROR,
                            primaryCtaText = getString(R.string.bmgm_cta_text_error_server),
                            primaryCtaAction = { loadInitialData() },
                            imageUrl = TokopediaImageUrl.OLP_SERVER_ERROR_ILLUSTRATION,
                            isShowProductList = false
                        )
                    }
                }
            }

            else -> {
                context?.let { activity?.setTransparentStatusBar(it) }
                binding?.apply {
                    loadingStateOlp.root.gone()
                    statusBar.visible()
                    header.visible()
                    headerBackground.visible()
                    stickyContent.visible()
                    errorPageLarge.gone()
                    miniCartView.visible()
                }
            }
        }
    }

    private fun setErrorPage(
        title: String,
        description: String,
        errorType: Int,
        primaryCtaText: String,
        primaryCtaAction: () -> Unit,
        imageUrl: String,
        isShowProductList: Boolean = false
    ) {
        setupToolbarForErrorState()
        binding?.apply {
            loadingStateOlp.root.gone()
            headerBackground.gone()
            when (isShowProductList) {
                true -> {
                    stickyContent.visible()
                    errorPageLarge.gone()
                    val emptyStateUiModel = EmptyStateUiModel(
                        title = title,
                        description = description,
                        imageUrl = imageUrl
                    )
                    olpAdapter?.submitList(listOf(emptyStateUiModel))
                }

                false -> {
                    stickyContent.gone()
                    errorPageLarge.apply {
                        visible()
                        setType(errorType)
                        errorTitle.text = title
                        errorDescription.text = description
                        errorAction.text = primaryCtaText
                        setActionClickListener {
                            primaryCtaAction.invoke()
                        }
                    }
                }
            }
            miniCartView.gone()
        }
    }

    private fun setupToolbarForErrorState() {
        binding?.apply {
            statusBar.gone()
            headerBackground.gone()
            header.apply {
                visible()
                title = getString(R.string.bmgm_title)
                setNavigationOnClickListener { activity?.finish() }
                cartButton?.setOnClickListener { redirectToCartPage() }
                moreMenuButton?.setOnClickListener { redirectToMainMenu() }
                showWhiteToolbar = true
                showShareButton = false
            }
        }
    }

    override fun onProductAtcVariantClicked(product: OfferProductListUiModel.Product) {
        if (isLogin) {
            if (product.isVbs) {
                openAtcVariant(product)
            } else {
                addToCartProduct(product)
            }
        } else {
            redirectToLoginPage(REQUEST_CODE_USER_LOGIN)
        }
    }

    override fun onProductCardClicked(productId: Long, productUrl: String) {
        tracker.sendClickProductCardEvent(
            currentState.offerIds.toSafeString(),
            currentState.warehouseIds.toSafeString()
        )
        redirectToPDP(productId, productUrl)
    }

    private fun addToCartProduct(product: OfferProductListUiModel.Product) {
        tracker.sendClickAtcEvent(
            currentState.offerIds.toSafeString(),
            currentState.warehouseIds.toSafeString()
        )
        viewModel.processEvent(OlpEvent.AddToCart(product))
    }

    private fun openAtcVariant(product: OfferProductListUiModel.Product) {
        val stringOfferIds = currentState.offerIds.joinToString(",")
        val stringWarehouseIds = currentState.warehouseIds.joinToString(",")
        val shopId = currentState.shopData.shopId
        context?.let {
            AtcVariantHelper.goToAtcVariant(
                context = it,
                productId = product.productId.toString(),
                pageSource = VariantPageSource.BUY_MORE_GET_MORE,
                shopId = shopId.toString(),
                saveAfterClose = false,
                extParams = AtcVariantHelper.generateExtParams(
                    mapOf(
                        Constant.EXT_PARAM_OFFER_ID to stringOfferIds,
                        Constant.EXT_PARAM_WAREHOUSE_ID to stringWarehouseIds
                    )
                ),
                startActivitResult = this::startActivityForResult
            )
        }
    }

    private fun fetchMiniCart() {
        val offeringInfo = viewModel.offeringInfo.value
        val offerCount = offeringInfo?.offerings?.firstOrNull()?.tierList?.size.orZero()
        binding?.miniCartView?.fetchData(
            shopIds = listOf(currentState.shopData.shopId),
            offerIds = currentState.offerIds,
            offerJsonData = currentState.offeringJsonData,
            warehouseIds = currentState.warehouseIds,
            offerCount = offerCount
        )
    }

    private fun updateCartCounter(cartCount: Int) {
        binding?.header?.apply {
            this.cartCount = cartCount
        }
    }

    override fun onTncClicked() {
        tracker.sendClickSnkButtonEvent(
            currentState.offerIds.toSafeString(),
            currentState.warehouseIds.toSafeString()
        )
        tncBottomSheet?.apply {
            setCloseClickListener {
                tracker.sendClickCloseSnkButtonEvent(
                    currentState.offerIds.toSafeString(),
                    currentState.warehouseIds.toSafeString()
                )
            }
            show(this@OfferLandingPageFragment)
        }
    }

    override fun onShopNameClicked(shopId: Long) {
        tracker.sendClickShopCtaButtonEvent(
            currentState.offerIds.toSafeString(),
            currentState.warehouseIds.toSafeString()
        )
        redirectToShopPage(shopId)
    }

    private fun redirectToCartPage() {
        context?.let {
            val userSession = UserSession(it)
            if (userSession.isLoggedIn) {
                RouteManager.route(it, ApplinkConst.CART)
            } else {
                startActivityForResult(
                    RouteManager.getIntent(it, ApplinkConst.LOGIN),
                    REQUEST_CODE_USER_LOGIN_CART
                )
            }
        }
    }

    private fun redirectToShopPage(shopId: Long) {
        RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_PAGE, shopId.toString())
    }

    private fun redirectToPDP(productId: Long, productUrl: String) {
        RouteManager.route(context, productUrl)
    }

    private fun redirectToMainMenu() {
        RouteManager.route(context, ApplinkConsInternalNavigation.MAIN_NAVIGATION)
    }

    private fun redirectToLoginPage(requestCode: Int = REQUEST_CODE_USER_LOGIN) {
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            startActivityForResult(intent, requestCode)
        }
    }

    private fun openShareBottomSheet() {
        UniversalShareBottomSheet.createInstance().apply {
            init(object : ShareBottomsheetListener {
                override fun onShareOptionClicked(shareModel: ShareModel) {
                }

                override fun onCloseOptionClicked() {
                }
            })
            enableDefaultShareIntent()
            setMetaData(
                tnTitle = "",
                tnImage = ""
            )
            setUtmCampaignData(
                "",
                "",
                "",
                ""
            )
            enableAffiliateCommission(AffiliateInput())
        }.show(childFragmentManager, "")
    }

    private fun setDefaultErrorSelection(throwable: Throwable) {
        when (throwable) {
            is ConnectException, is SocketException, is UnknownHostException -> {
                setViewState(VIEW_ERROR, Status.NO_CONNECTION)
            }

            is ResponseErrorException -> {
                binding?.miniCartView.showToaster(message = throwable.localizedMessage)
            }

            else -> {
                setViewState(VIEW_ERROR)
            }
        }
    }

    override fun onRefresh() {
        swipeRefreshLayout?.isRefreshing = true
        swipeRefreshLayout?.isEnabled = false
    }

    private fun List<Long>.toSafeString(): String {
        return this.firstOrNull()?.orZero().toString()
    }
}
