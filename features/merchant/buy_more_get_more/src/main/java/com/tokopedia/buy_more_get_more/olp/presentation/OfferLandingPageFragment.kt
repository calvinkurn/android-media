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
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.OlpEvent
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductSortingUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.enum.Status
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapter
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapterTypeFactoryImpl
import com.tokopedia.buy_more_get_more.olp.presentation.bottomsheet.TncBottomSheet
import com.tokopedia.buy_more_get_more.olp.presentation.listener.AtcProductListener
import com.tokopedia.buy_more_get_more.olp.presentation.listener.OfferingInfoListener
import com.tokopedia.buy_more_get_more.olp.utils.BundleConstant
import com.tokopedia.buy_more_get_more.olp.utils.Constant
import com.tokopedia.buy_more_get_more.olp.utils.setDefaultStatusBar
import com.tokopedia.buy_more_get_more.olp.utils.setTransparentStatusBar
import com.tokopedia.buy_more_get_more.sort.activity.ShopProductSortActivity
import com.tokopedia.buy_more_get_more.sort.listener.ProductSortListener
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.campaign.helper.BuyMoreGetMoreHelper
import com.tokopedia.campaign.utils.extension.enable
import com.tokopedia.campaign.utils.extension.showToaster
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toLongSafely
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.autoClearedNullable
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

        private const val REQUEST_CODE_SORT = 308
        private const val VIEW_CONTENT = 1
        private const val VIEW_LOADING = 2
        private const val VIEW_ERROR = 3
        private const val REQUEST_CODE_USER_LOGIN_CART = 102
    }

    private var binding by autoClearedNullable<FragmentOfferLandingPageBinding>()

    private val olpAdapter: OlpAdapter?
        get() = adapter as? OlpAdapter

    private val localCacheModel by lazy {
        context?.let {
            ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

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
        loadInitialData()

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
        setupObservables()
        setupProductRv()
        swipeRefreshLayout = binding?.swLayout
        swipeRefreshLayout?.apply {
            setOnRefreshListener(this@OfferLandingPageFragment)
            isEnabled = true
            setOnRefreshListener { loadInitialData() }
        }
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
                }

                Status.INVALID_OFFER_ID -> {}
                Status.OFFER_ALREADY_FINISH -> {}
            }
        }

        viewModel.productList.observe(viewLifecycleOwner) { productList ->
            setupProductList(productList)
            setViewState(VIEW_CONTENT)
            notifyLoadResult(productList.productList.size >= 10)
        }

        viewModel.navNotificationLiveData.observe(viewLifecycleOwner) { notification ->
            updateCartCounter(notification.totalCart)
        }

        viewModel.miniCartAdd.observe(viewLifecycleOwner) { atc ->
            when (atc) {
                is Success -> {
                    binding?.apply {
                        miniCartView.showToaster(atc.data.data.message.firstOrNull().orEmpty())
                        fetchMiniCart()
                    }
                    viewModel.processEvent(OlpEvent.GetNotification)
                }

                is Fail -> {
                    binding?.miniCartView.showToaster(message = atc.throwable.localizedMessage)
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { throwable ->
            setViewState(VIEW_ERROR)
        }
    }

    private fun setupHeader(offerInfoForBuyer: OfferInfoForBuyerUiModel) {
        setupToolbar(offerInfoForBuyer)
        binding?.headerBackground?.setBackgroundResource(R.drawable.olp_header)
        olpAdapter?.submitList(
            newList = listOf(
                offerInfoForBuyer,
                OfferProductSortingUiModel()
            )
        )
        viewModel.processEvent(OlpEvent.GetNotification)
        getProductListData(Int.ONE)
    }

    private fun setupProductList(offerProductList: OfferProductListUiModel) {
        olpAdapter?.apply {
            updateProductCount(offerProductList.totalProduct)
            setProductListData(offerProductList.productList)
        }
    }

    private fun setupToolbar(offerInfoForBuyer: OfferInfoForBuyerUiModel) {
        binding?.header?.apply {
            title = getString(R.string.bmgm_title)
            subTitle = offerInfoForBuyer.offerings.firstOrNull()?.offerName.orEmpty()
            setNavigationOnClickListener { activity?.finish() }
            cartButton?.setOnClickListener { redirectToCartPage() }
            moreMenuButton?.setOnClickListener { redirectToMainMenu() }
            showWhiteToolbar = false
        }
    }

    private fun renderSortFilter(sortId: String, sortName: String) {
        olpAdapter?.changeSelectedSortFilter(sortId, sortName)
        viewModel.processEvent(OlpEvent.SetSortId(sortId))
        getProductListData(page = Int.ONE)
    }

    private fun getProductListData(page: Int) {
        if (page == Int.ONE) olpAdapter?.removeProductList()
        viewModel.processEvent(OlpEvent.GetOffreringProductList(page = page))
    }

    override fun loadInitialData() {
        setViewState(VIEW_LOADING)
        viewModel.processEvent(
            OlpEvent.SetInitialUiState(
                offerIds = listOf(offerId.toIntSafely()),
                shopIds = shopIds.toLongSafely(),
                productIds = if (productIds.isNotEmpty()) {
                    productIds.split(",").map { it.toIntSafely() }
                } else {
                    emptyList()
                },
                warehouseIds = if (warehouseIds.isNotEmpty()) {
                    warehouseIds.split(",").map { it.toIntSafely() }
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
                    sortId = data?.getStringExtra(ShopProductSortActivity.SORT_VALUE) ?: ""
                    sortName = data?.getStringExtra(ShopProductSortActivity.SORT_NAME) ?: ""
                    renderSortFilter(sortId, sortName)
                }
            }
        }
        AtcVariantHelper.onActivityResultAtcVariant(requireContext(), requestCode, data) {
        }
    }

    override fun onSortChipClicked() {
        context?.run {
            val intent = ShopProductSortActivity.createIntent(activity, sortId)
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
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            val config = HasPaginatedList.Config(
                pageSize = 10,
                onLoadNextPage = {},
                onLoadNextPageFinished = {}
            )
            attachPaging(this, config) { page, _ ->
                getProductListData(page)
            }
        }
    }

    private fun setupTncBottomSheet() {
        tncBottomSheet = TncBottomSheet.newInstance(viewModel.currentState.tnc)
    }

    private fun setViewState(viewState: Int, status: Status = Status.SUCCESS) {
        swipeRefreshLayout?.isRefreshing = false
        swipeRefreshLayout?.isEnabled = true
        when (viewState) {
            VIEW_LOADING -> {
                binding?.apply {
                    loadingStateOlp.root.visible()
                    statusBar.gone()
                    header.gone()
                    headerBackground.gone()
                    stickyContent.gone()
                    errorPageLarge.gone()
                    miniCartView.gone()
                }
            }

            VIEW_ERROR -> {
                when (status) {
                    Status.INVALID_OFFER_ID -> {
                        setErrorPage(
                            title = getString(R.string.bmgm_title_error_not_found),
                            description = getString(R.string.bmgm_description_error_not_found),
                            imageUrl = "https://images.tokopedia.net/android/merchant_voucher/il_mvc_no_result.webp",
                            primaryCtaText = getString(R.string.bmgm_cta_text_error_not_found),
                            primaryCtaAction = { activity?.finish() }
                        )
                    }

                    Status.OFFER_ALREADY_FINISH -> {
                        setErrorPage(
                            title = getString(R.string.bmgm_title_error_ended_promo),
                            description = getString(R.string.bmgm_description_error_ended_promo),
                            imageUrl = "https://images.tokopedia.net/android/merchant_voucher/il_mvc_no_result.webp",
                            primaryCtaText = getString(R.string.bmgm_cta_text_error_ended_promo),
                            primaryCtaAction = { activity?.finish() }
                        )
                    }

                    else -> {
                        setErrorPage(
                            title = getString(R.string.bmgm_title_error_server),
                            description = getString(R.string.bmgm_description_error_server),
                            imageUrl = "https://images.tokopedia.net/android/merchant_voucher/il_mvc_no_result.webp",
                            primaryCtaText = getString(R.string.bmgm_cta_text_error_server),
                            primaryCtaAction = { loadInitialData() }
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
                    miniCartPlaceholder.visible()
                }
            }
        }
    }

    private fun setErrorPage(
        title: String,
        description: String,
        imageUrl: String,
        primaryCtaText: String,
        primaryCtaAction: () -> Unit
    ) {
        setupToolbarForErrorState()
        binding?.apply {
            loadingStateOlp.root.gone()
            headerBackground.gone()
            stickyContent.gone()
            errorPageLarge.apply {
                visible()
                setTitle(title)
                setDescription(description)
                setImageUrl(imageUrl)
                setPrimaryCTAText(primaryCtaText)
                setPrimaryCTAClickListener {
                    primaryCtaAction.invoke()
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
            }
        }
    }

    override fun onProductAtcVariantClicked(product: OfferProductListUiModel.Product) {
        if (product.isVbs) {
            openAtcVariant(product)
        } else {
            addToCartProduct(product)
        }
    }

    override fun onProductCardClicked(productId: Long, productUrl: String) {
        redirectToPDP(productId, productUrl)
    }

    private fun addToCartProduct(product: OfferProductListUiModel.Product) {
        viewModel.processEvent(OlpEvent.AddToCart(product))
    }

    private fun openAtcVariant(product: OfferProductListUiModel.Product) {
        val stringOfferIds = viewModel.currentState.offerIds.joinToString(",")
        val stringWarehouseIds = viewModel.currentState.warehouseIds.joinToString(",")
        context?.let {
            AtcVariantHelper.goToAtcVariant(
                context = it,
                productId = product.productId.toString(),
                pageSource = VariantPageSource.BUY_MORE_GET_MORE,
                shopId = shopIds,
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
        val currentUiState = viewModel.currentState
        binding?.miniCartPlaceholder?.fetchData(
            offerIds = currentUiState.offerIds.map { it.toLong() },
            offerJsonData = currentUiState.offeringJsonData,
            warehouseIds = currentUiState.warehouseIds.map { it.toString() }
        )
    }

    private fun updateCartCounter(cartCount: Int) {
        binding?.header?.apply {
            this.cartCount = cartCount
        }
    }

    override fun onTncClicked() {
        tncBottomSheet?.apply {
            show(this@OfferLandingPageFragment)
        }
    }

    override fun onShopNameClicked(shopId: Long) {
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

    override fun onRefresh() {
        swipeRefreshLayout?.isRefreshing = true
        swipeRefreshLayout?.isEnabled = false
    }
}
