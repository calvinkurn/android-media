package com.tokopedia.buy_more_get_more.olp.presentation

import android.R.color.transparent
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.FragmentOfferLandingPageBinding
import com.tokopedia.buy_more_get_more.olp.di.component.DaggerBuyMoreGetMoreComponent
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductSortingUiModel
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapter
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapterTypeFactoryImpl
import com.tokopedia.buy_more_get_more.olp.presentation.listener.AtcProductListener
import com.tokopedia.buy_more_get_more.olp.utils.BundleConstant
import com.tokopedia.buy_more_get_more.sort.activity.ShopProductSortActivity
import com.tokopedia.buy_more_get_more.sort.listener.ProductSortListener
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.campaign.helper.BuyMoreGetMoreHelper
import com.tokopedia.campaign.utils.extension.showToaster
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import javax.inject.Inject

class OfferLandingPageFragment :
    BaseListFragment<Visitable<*>, AdapterTypeFactory>(),
    ProductSortListener,
    AtcProductListener,
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
        OlpAdapterTypeFactoryImpl(this, this)
    }
    private var sortId = ""
    private var sortName = ""

    @Inject
    lateinit var viewModel: OfferLandingPageViewModel
    private val shopIds by lazy { arguments?.getString(BundleConstant.BUNDLE_SHOP_ID).orEmpty() }
    private val offerId by lazy { arguments?.getString(BundleConstant.BUNDLE_OFFER_ID).orEmpty() }
    private val warehouseIds by lazy { arguments?.getString(BuyMoreGetMoreHelper.KEY_WAREHOUSE_IDS).orEmpty() }
    private val productIds by lazy { arguments?.getString(BuyMoreGetMoreHelper.KEY_PRODUCT_IDS).orEmpty() }

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
    }

    private fun setupObservables() {
        viewModel.offeringInfo.observe(viewLifecycleOwner) { offerInfoForBuyer ->
            setupHeader(offerInfoForBuyer)
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
                    binding?.miniCartPlaceholder.showToaster(atc.data.data.success.toString())
                }

                is Fail -> {
                    binding?.miniCartPlaceholder.showToaster(message = atc.throwable.localizedMessage)
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { throwable ->
            setViewState(VIEW_ERROR, throwable.localizedMessage)
        }
    }

    private fun setupHeader(offerInfoForBuyer: OfferInfoForBuyerUiModel) {
        setupStatusBar()
        setupToolbar(offerInfoForBuyer)
        olpAdapter?.submitList(
            newList = listOf(
                offerInfoForBuyer,
                OfferProductSortingUiModel()
            )
        )
        viewModel.getNotification()
        getProductListData(1)
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
            cartButton?.setOnClickListener { TODO("Navigate to cart") }
            moreMenuButton?.setOnClickListener { TODO("Navigate to more menu") }
        }
    }

    private fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity?.isDarkMode() == true) {
                activity?.window?.decorView?.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        activity?.let {
            val transparent = MethodChecker.getColor(
                context,
                transparent
            )
            it.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            it.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            WindowCompat.getInsetsController(it.window, it.window.decorView).apply {
                isAppearanceLightStatusBars = false
            }
            it.window.statusBarColor = transparent
        }
    }

    private fun renderSortFilter(sortId: String, sortName: String) {
        olpAdapter?.changeSelectedSortFilter(sortId, sortName)
        getProductListData(1, sortId)
    }

    private fun getProductListData(page: Int, sortId: String = "") {
        viewModel.getOfferingProductList(
            offerIds = listOf(offerId.toIntOrZero()),
            warehouseIds = listOf(1, 2),
            localCacheModel = localCacheModel,
            page = page,
            sortId = sortId
        )
    }

    override fun loadInitialData() {
        Log.d("Masuk", localCacheModel?.shop_id ?: "Kosong")
        setViewState(VIEW_LOADING)
        viewModel.getOfferingInfo(
            offerIds = listOf(offerId.toIntOrZero()),
            shopIds = if (shopIds.isNotEmpty()) {
                shopIds.split(",").map { it.toIntSafely() }
            } else {
                emptyList()
            },
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
                onLoadNextPage = {
                    // TODO: Implement loading
                },
                onLoadNextPageFinished = {
                    // TODO: Implement loading
                }
            )
            attachPaging(this, config) { page, _ ->
                getProductListData(page)
            }
        }
    }

    private fun setViewState(viewState: Int, errorMsg: String = "") {
        when (viewState) {
            VIEW_LOADING -> {
                binding?.apply {
                    loadingStateOlp.root.visible()
                    groupHeader.gone()
                    stickyContent.gone()
                    errorPageLarge.gone()
                    miniCartPlaceholder.gone()
                }
            }

            VIEW_ERROR -> {
                binding?.apply {
                    loadingStateOlp.root.gone()
                    groupHeader.gone()
                    stickyContent.gone()
                    errorPageLarge.apply {
                        visible()
                        setTitle("Something Went Wrong")
                        setDescription(errorMsg)
                        setPrimaryCTAText("Coba lagi")
                        setPrimaryCTAClickListener {
                            loadInitialData()
                        }
                    }
                    miniCartPlaceholder.gone()
                }
            }

            else -> {
                binding?.apply {
                    loadingStateOlp.root.gone()
                    groupHeader.visible()
                    stickyContent.visible()
                    errorPageLarge.gone()
                    miniCartPlaceholder.visible()
                }
            }
        }
    }

    private fun generateDummyOfferingData(): OfferInfoForBuyerUiModel {
        return OfferInfoForBuyerUiModel(
            offerings = listOf(
                OfferInfoForBuyerUiModel.Offering(
                    tierList = listOf(
                        OfferInfoForBuyerUiModel.Offering.Tier(
                            rules = listOf(OfferInfoForBuyerUiModel.Offering.Tier.Rule(value = 3)),
                            benefits = listOf(OfferInfoForBuyerUiModel.Offering.Tier.Benefit(value = 20))
                        ),
                        OfferInfoForBuyerUiModel.Offering.Tier(
                            rules = listOf(OfferInfoForBuyerUiModel.Offering.Tier.Rule(value = 5)),
                            benefits = listOf(OfferInfoForBuyerUiModel.Offering.Tier.Benefit(value = 15))
                        )
                    )
                )
            )
        )
    }

    private fun generateDummyProductData(): List<OfferProductListUiModel.Product> {
        val products: MutableList<OfferProductListUiModel.Product> = mutableListOf()
        for (product in 1..50) {
            products.add(
                OfferProductListUiModel.Product(
                    name = "Product $product",
                    imageUrl = "https://images.tokopedia.net/img/cache/200-square/attachment/2018/8/9/3127195/3127195_e5b3e074-c897-4cf0-9ced-5572d0538e7c.jpg",
                    price = "Rp.20.000",
                    rating = "4.5",
                    soldCount = 80,
                    campaign = OfferProductListUiModel.Product.Campaign(
                        discountedPrice = "Rp.10.000",
                        originalPrice = "Rp.15.000",
                        discountedPercentage = 50
                    )
                )
            )
        }
        return products
    }

    override fun onProductAtcVariantClicked(product: OfferProductListUiModel.Product) {
        if (product.isVbs) {
            openAtcVariant(product)
        } else {
            addToCartProduct(product)
        }
    }

    private fun addToCartProduct(product: OfferProductListUiModel.Product) {
        viewModel.addToCart(product, shopIds)
    }

    private fun openAtcVariant(product: OfferProductListUiModel.Product) {
        context?.let {
            AtcVariantHelper.goToAtcVariant(
                context = it,
                productId = product.productId.toString(),
                pageSource = VariantPageSource.BUY_MORE_GET_MORE,
                shopId = shopIds,
                startActivitResult = this::startActivityForResult
            )
        }
    }

    private fun updateCartCounter(cartCount: Int) {
        binding?.header?.apply {
            this.cartCount = cartCount
        }
    }
}
