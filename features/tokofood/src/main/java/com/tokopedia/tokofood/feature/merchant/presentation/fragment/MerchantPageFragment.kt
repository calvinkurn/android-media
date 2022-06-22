package com.tokopedia.tokofood.feature.merchant.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.constants.ShareComponentConstants
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.Constant
import com.tokopedia.tokofood.common.util.TokofoodExt.showErrorToaster
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.databinding.FragmentMerchantPageLayoutBinding
import com.tokopedia.tokofood.feature.merchant.analytics.MerchantPageAnalytics
import com.tokopedia.tokofood.feature.merchant.common.util.MerchantShareComponentUtil
import com.tokopedia.tokofood.feature.merchant.di.DaggerMerchantPageComponent
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodMerchantProfile
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodTickerDetail
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.MerchantPageCarouselAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.ProductListAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.*
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.ManageLocationFragment.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.ManageLocationFragment.Companion.EMPTY_STATE_NO_PIN_POINT
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.ManageLocationFragment.Companion.EMPTY_STATE_OUT_OF_COVERAGE
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.CategoryFilterBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.ChangeMerchantBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.CustomOrderDetailBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.MerchantInfoBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.OrderNoteBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.ProductDetailBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.mapper.TokoFoodMerchantUiModelMapper
import com.tokopedia.tokofood.feature.merchant.presentation.model.CategoryFilterListUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail
import com.tokopedia.tokofood.feature.merchant.presentation.model.MerchantOpsHour
import com.tokopedia.tokofood.feature.merchant.presentation.model.MerchantShareComponent
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.VariantWrapperUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.MerchantCarouseItemViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductCardViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.MerchantPageViewModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.SHAPE_FULL
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_WARNING
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

@FlowPreview
@ExperimentalCoroutinesApi
class MerchantPageFragment : BaseMultiFragment(),
    MerchantCarouseItemViewHolder.OnCarouselItemClickListener,
    ProductCardViewHolder.OnProductCardItemClickListener,
    OrderNoteBottomSheet.OnSaveNoteButtonClickListener,
    CustomOrderDetailBottomSheet.OnCustomOrderDetailClickListener,
    CategoryFilterBottomSheet.CategoryFilterListener,
    ProductDetailBottomSheet.Listener,
    ProductDetailBottomSheet.OnProductDetailClickListener,
    ShareBottomsheetListener,
    ChangeMerchantBottomSheet.ChangeMerchantListener,
    PhoneNumberVerificationBottomSheet.OnButtonCtaClickListener{

    private var parentActivity: HasViewModel<MultipleFragmentsViewModel>? = null

    private val activityViewModel: MultipleFragmentsViewModel?
        get() = parentActivity?.viewModel()

    private var binding: FragmentMerchantPageLayoutBinding? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var merchantPageAnalytics: MerchantPageAnalytics

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(MerchantPageViewModel::class.java)
    }

    private val merchantShareComponentUtil: MerchantShareComponentUtil? by lazy(LazyThreadSafetyMode.NONE) {
        context?.let { MerchantShareComponentUtil(it) }
    }

    private var merchantId: String = ""
    private var productId: String = ""
    private var filterNameSelected: String = ""

    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var merchantInfoBottomSheet: MerchantInfoBottomSheet? = null
    private var orderNoteBottomSheet: OrderNoteBottomSheet? = null
    private var customOrderDetailBottomSheet: CustomOrderDetailBottomSheet? = null
    private var carouselAdapter: MerchantPageCarouselAdapter? = null
    private var productListAdapter: ProductListAdapter? = null

    private var merchantShareComponent: MerchantShareComponent? = null

    override fun getFragmentToolbar(): Toolbar? {
        return binding?.toolbar
    }

    override fun getFragmentTitle(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments?.getString(Constant.DATA_KEY) ?: ""
        val uri = Uri.parse(bundle)
        merchantId = uri.getQueryParameter(DeeplinkMapperTokoFood.PARAM_MERCHANT_ID) ?: ""
        productId = uri.getQueryParameter(DeeplinkMapperTokoFood.PARAM_PRODUCT_ID) ?: ""
        setHasOptionsMenu(true)
        initInjector()
        // handle negative case #1 non-login
        if (!userSession.isLoggedIn) { goToLoginPage() }
        else {
            // handle negative case: no-address,no-pinpoint
            validateAddressData()
        }
    }

    private fun initInjector() {
        activity?.let {
            DaggerMerchantPageComponent
                .builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_merchant_page, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                shareMerchantPage(viewModel.merchantData)
                true
            }
            R.id.action_open_global_menu -> {
                RouteManager.route(requireContext(), ApplinkConst.HOME_NAVIGATION)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        parentActivity = activity as? HasViewModel<MultipleFragmentsViewModel>
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewBinding = FragmentMerchantPageLayoutBinding.inflate(inflater)
        binding = viewBinding
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        initializeMiniCartWidget()
        merchantPageAnalytics.openMerchantPage(merchantId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBarLayoutListener()
        setBackgroundDefaultColor()
        setupMerchantLogo()
        setupMerchantProfileCarousel()
        setupProductList()
        setupOrderNoteBottomSheet()
        setupCustomOrderDetailBottomSheet()
        setupCardSticky()
        observeLiveData()
        collectCartDataFlow()
        collectFlow()
        fetchMerchantData()
    }

    private fun setBackgroundDefaultColor() {
        binding?.toolbarParent?.let {
            it.setBackgroundColor(
                ContextCompat.getColor(
                    it.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            )
        }
    }

    private fun setToolbarTransparentColor() {
        binding?.toolbar?.let {
            it.background = null
        }
    }

    private fun setToolbarWhiteColor() {
        binding?.toolbar?.let {
            it.setBackgroundColor(
                ContextCompat.getColor(
                    it.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            )
        }
    }

    private fun setupAppBarLayoutListener() {
        binding?.toolbarParent?.addOnOffsetChangedListener(object :
            AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (appBarLayout == null) return

                val offset = abs(verticalOffset)

                if (offset >= (appBarLayout.totalScrollRange - binding?.toolbar?.height.orZero())) {
                    //collapsed
                    binding?.cardUnifySticky?.show()
                    setToolbarWhiteColor()
                } else {
                    //expanded
                    binding?.cardUnifySticky?.hide()
                    setToolbarTransparentColor()
                }
            }
        })
    }

    private fun setupCardSticky() {
        binding?.cardUnifySticky?.setOnClickListener {
            val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
            val categoryFilter =
                TokoFoodMerchantUiModelMapper.mapProductListItemToCategoryFilterUiModel(
                    viewModel.filterList, filterNameSelected
                )
            cacheManager?.put(
                CategoryFilterBottomSheet.KEY_CATEGORY_FILTER_LIST,
                categoryFilter
            )
            val bundle = Bundle().apply {
                putString(
                    CategoryFilterBottomSheet.KEY_CACHE_MANAGER_ID,
                    cacheManager?.id.orEmpty()
                )
            }
            val bottomSheet = CategoryFilterBottomSheet.createInstance(bundle)
            bottomSheet.setCategoryFilterListener(this)
            bottomSheet.show(childFragmentManager)
        }
    }

    private fun setCategoryPlaceholder(filterNameSelected: String) {
        binding?.tvCategoryPlaceholder?.text = filterNameSelected
    }

    private fun fetchMerchantData() {
        if (viewModel.productListItems.isNotEmpty()) {
            renderProductList(viewModel.productListItems)
        } else {
            showLoader()
            context?.run {
                ChooseAddressUtils.getLocalizingAddressData(this)
                    .let { addressData ->
                        viewModel.getMerchantData(
                            merchantId = merchantId,
                            latlong = addressData.latLong,
                            timezone = TimeZone.getDefault().id
                        )
                    }
            }
        }
    }

    private fun showLoader() {
        binding?.merchantInfoViewGroup?.hide()
        binding?.shimmeringMerchantPage?.root?.show()
    }

    private fun hideLoader() {
        binding?.shimmeringMerchantPage?.root?.hide()
        binding?.merchantInfoViewGroup?.show()
    }

    private fun shareMerchantPage(
        tokoFoodMerchantProfile: TokoFoodMerchantProfile?
    ) {
        merchantShareComponent = merchantShareComponentUtil?.getMerchantShareComponent(
            tokoFoodMerchantProfile,
            merchantId
        )
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@MerchantPageFragment)
            setUtmCampaignData(
                pageName = ShareComponentConstants.TOKOFOOD,
                userId = userSession.userId.orEmpty(),
                pageIdConstituents = listOf(
                    ShareComponentConstants.MERCHANT,
                    merchantShareComponent?.id.orEmpty()
                ),
                feature = SHARE
            )
            setMetaData(
                tnTitle = merchantShareComponent?.previewTitle.orEmpty(),
                tnImage = merchantShareComponent?.previewThumbnail.orEmpty(),
            )
            setOgImageUrl(imgUrl = merchantShareComponent?.ogImage.orEmpty())
        }

        universalShareBottomSheet?.show(childFragmentManager, this)
    }

    private fun shareFoodItem(
        tokoFoodMerchantProfile: TokoFoodMerchantProfile?,
        productUiModel: ProductUiModel
    ) {
        merchantShareComponent = merchantShareComponentUtil?.getFoodShareComponent(
            tokoFoodMerchantProfile,
            productUiModel,
            merchantId
        )
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@MerchantPageFragment)
            setUtmCampaignData(
                pageName = ShareComponentConstants.TOKOFOOD,
                userId = userSession.userId.orEmpty(),
                pageIdConstituents = listOf(
                    ShareComponentConstants.Merchant.FOOD,
                    merchantShareComponent?.id.orEmpty()
                ),
                feature = SHARE
            )
            setMetaData(
                tnTitle = merchantShareComponent?.previewTitle.orEmpty(),
                tnImage = merchantShareComponent?.previewThumbnail.orEmpty(),
            )
            setOgImageUrl(imgUrl = merchantShareComponent?.ogImage.orEmpty())
        }

        merchantPageAnalytics.impressShareBottomSheet(
            merchantShareComponent?.id.orEmpty(),
            userSession.userId.orEmpty()
        )

        universalShareBottomSheet?.show(childFragmentManager, this)
    }

    private fun observeLiveData() {
        viewModel.getMerchantDataResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val merchantData = result.data.tokofoodGetMerchantData
                    val isDeliverable = merchantData.merchantProfile.deliverable
                    if (isDeliverable) {
                        hideLoader()
                        // render ticker data if not empty
                        val tickerData = merchantData.ticker
                        if (!viewModel.isTickerDetailEmpty(tickerData)) {
                            renderTicker(tickerData)
                        }
                        // render merchant logo, name, categories, carousel
                        val merchantProfile = merchantData.merchantProfile
                        renderMerchantProfile(merchantProfile)
                        // setup merchant info bottom sheet
                        val name = merchantProfile.name
                        val address = merchantProfile.address
                        val merchantOpsHours = viewModel.mapOpsHourDetailsToMerchantOpsHours(merchantProfile.opsHourDetail)
                        setupMerchantInfoBottomSheet(name, address, merchantOpsHours)
                        // render product list
                        val isShopClosed = merchantProfile.opsHourFmt.isWarning
                        val foodCategories = merchantData.categories
                        val productListItems = viewModel.mapFoodCategoriesToProductListItems(isShopClosed, foodCategories)
                        // set default category filter selection
                        filterNameSelected = productListItems.firstOrNull()?.productCategory?.title.orEmpty()
                        val finalProductListItems = viewModel.applyProductSelection(productListItems, viewModel.selectedProducts)
                        renderProductList(finalProductListItems)
                        renderProductList(finalProductListItems)
                        setCategoryPlaceholder(filterNameSelected)
                    } else {
                        navigateToNewFragment(ManageLocationFragment.createInstance(
                                negativeCaseId = EMPTY_STATE_OUT_OF_COVERAGE,
                                merchantId = merchantId
                        ))
                    }
                }
                is Fail -> {

                }
            }
        })
    }

    private fun collectCartDataFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityViewModel?.cartDataFlow?.collect { cartData ->
                cartData?.availableSection?.products?.let { products ->
                    viewModel.selectedProducts = products
                }
            }
        }
    }

    private fun collectFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityViewModel?.cartDataValidationFlow?.collect {
                when (it.state) {
                    UiEvent.EVENT_SUCCESS_LOAD_CART -> {
                        toggleMiniCartVisibility(true)
                    }
                    UiEvent.EVENT_FAILED_LOAD_CART -> {
                        toggleMiniCartVisibility(false)
                    }
                    UiEvent.EVENT_FAILED_ADD_TO_CART -> {
                        view?.showErrorToaster(it.throwable?.message.orEmpty())
                    }
                    UiEvent.EVENT_SUCCESS_ADD_TO_CART -> {
                        (it.data as? Pair<*, *>)?.let { pair ->
                            (pair.first as? UpdateParam)?.productList?.firstOrNull()
                                ?.let { requestParam ->
                                    (pair.second as? CartTokoFoodData)?.let { cartTokoFoodData ->
                                        if (cartTokoFoodData.bottomSheet.isShowBottomSheet) {
                                            val bottomSheet = PhoneNumberVerificationBottomSheet.createInstance(
                                                    bottomSheetData = cartTokoFoodData.bottomSheet,
                                                    clickListener = this@MerchantPageFragment
                                            )
                                            bottomSheet.show(childFragmentManager)
                                        }
                                        cartTokoFoodData.carts.firstOrNull { data -> data.productId == requestParam.productId }
                                            ?.let { cartTokoFood ->
                                                val cardPositions =
                                                    viewModel.productMap[requestParam.productId]
                                                cardPositions?.run {
                                                    val dataSetPosition =
                                                        viewModel.getDataSetPosition(this)
                                                    val productUiModel =
                                                        productListAdapter?.getProductUiModel(
                                                            dataSetPosition
                                                        ) ?: ProductUiModel()
                                                    productListAdapter?.updateProductUiModel(
                                                        cartTokoFood = cartTokoFood,
                                                        dataSetPosition = dataSetPosition,
                                                        adapterPosition = viewModel.getAdapterPosition(
                                                            this
                                                        ),
                                                        customOrderDetail = viewModel.mapCartTokoFoodToCustomOrderDetail(
                                                            cartTokoFood,
                                                            productUiModel
                                                        )
                                                    )
                                                }
                                            }
                                    }
                                }
                        }
                    }
                    UiEvent.EVENT_SUCCESS_UPDATE_CART -> {
                        (it.data as? Pair<*, *>)?.let { pair ->
                            (pair.first as? UpdateParam)?.productList?.firstOrNull()
                                ?.let { requestParam ->
                                    (pair.second as? CartTokoFoodData)?.let { cartTokoFoodData ->
                                        cartTokoFoodData.carts.firstOrNull { data -> data.productId == requestParam.productId }
                                            ?.let { cartTokoFood ->
                                                val cardPositions =
                                                    viewModel.productMap[requestParam.productId]
                                                cardPositions?.run {
                                                    val dataSetPosition =
                                                        viewModel.getDataSetPosition(this)
                                                    val productUiModel =
                                                        productListAdapter?.getProductUiModel(
                                                            dataSetPosition
                                                        ) ?: ProductUiModel()
                                                    productListAdapter?.updateProductUiModel(
                                                        cartTokoFood = cartTokoFood,
                                                        dataSetPosition = dataSetPosition,
                                                        adapterPosition = viewModel.getAdapterPosition(
                                                            this
                                                        ),
                                                        customOrderDetail = viewModel.mapCartTokoFoodToCustomOrderDetail(
                                                            cartTokoFood,
                                                            productUiModel
                                                        )
                                                    )
                                                }
                                            }
                                    }
                                }
                        }
                    }
                    UiEvent.EVENT_SUCCESS_UPDATE_NOTES -> {
                        (it.data as? Pair<*, *>)?.let { pair ->
                            (pair.first as? UpdateParam)?.productList?.firstOrNull()
                                ?.let { requestParam ->
                                    (pair.second as? CartTokoFoodData)?.let { cartTokoFoodData ->
                                        cartTokoFoodData.carts.firstOrNull { data -> data.productId == requestParam.productId }
                                            ?.let { cartTokoFood ->
                                                val cardPositions =
                                                    viewModel.productMap[requestParam.productId]
                                                cardPositions?.run {
                                                    productListAdapter?.updateProductUiModel(
                                                        cartTokoFood = cartTokoFood,
                                                        dataSetPosition = viewModel.getDataSetPosition(
                                                            this
                                                        ),
                                                        adapterPosition = viewModel.getAdapterPosition(
                                                            this
                                                        )
                                                    )
                                                }
                                            }
                                    }
                                    view?.let { view ->
                                        Toaster.build(
                                            view = view,
                                            text = getString(com.tokopedia.tokofood.R.string.text_note_saved_message),
                                            duration = Toaster.LENGTH_SHORT,
                                            type = Toaster.TYPE_NORMAL).show()
                                    }
                                }
                        }
                    }
                    UiEvent.EVENT_SUCCESS_DELETE_PRODUCT -> {
                        (it.data as? Pair<*, *>)?.let { pair ->
                            (pair.first as? String)?.let { cartId ->
                                (pair.second as? CartTokoFoodData)?.carts?.firstOrNull()
                                    ?.let { product ->
                                        val cardPositions = viewModel.productMap[product.productId]
                                        cardPositions?.run {
                                            val dataSetPosition = viewModel.getDataSetPosition(this)
                                            val productUiModel =
                                                productListAdapter?.getProductUiModel(
                                                    dataSetPosition
                                                )
                                            if (productUiModel?.isCustomizable == true) {
                                                productListAdapter?.removeCustomOrder(
                                                    cartId = cartId,
                                                    dataSetPosition = dataSetPosition,
                                                    adapterPosition = viewModel.getAdapterPosition(
                                                        this
                                                    )
                                                )
                                            } else {
                                                productListAdapter?.resetProductUiModel(
                                                    dataSetPosition = dataSetPosition,
                                                    adapterPosition = viewModel.getAdapterPosition(
                                                        this
                                                    )
                                                )
                                            }
                                            view?.let { view ->
                                                Toaster.build(
                                                        view = view,
                                                        text = getString(com.tokopedia.tokofood.R.string.text_product_removed),
                                                        duration = Toaster.LENGTH_SHORT,
                                                        type = Toaster.TYPE_NORMAL).show()
                                            }
                                        }
                                    }
                            }
                        }
                    }
                    UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY -> {
                        (it.data as? Pair<*, *>)?.let { pair ->
                            (pair.first as? UpdateParam)?.productList?.firstOrNull()
                                ?.let { requestParam ->
                                    (pair.second as? CartTokoFoodData)?.let { cartTokoFoodData ->
                                        cartTokoFoodData.carts.firstOrNull { data -> data.productId == requestParam.productId }
                                            ?.let { cartTokoFood ->
                                                val cardPositions =
                                                    viewModel.productMap[requestParam.productId]
                                                cardPositions?.run {
                                                    val dataSetPosition =
                                                        viewModel.getDataSetPosition(this)
                                                    val productUiModel =
                                                        productListAdapter?.getProductUiModel(
                                                            dataSetPosition
                                                        )
                                                    if (productUiModel?.isCustomizable == true) {
                                                        productListAdapter?.updateCustomOrderQty(
                                                            cartId = cartTokoFood.cartId,
                                                            orderQty = cartTokoFood.quantity,
                                                            dataSetPosition = dataSetPosition
                                                        )
                                                    } else {
                                                        productListAdapter?.updateProductUiModel(
                                                            cartTokoFood = cartTokoFood,
                                                            dataSetPosition = dataSetPosition,
                                                            adapterPosition = viewModel.getAdapterPosition(
                                                                this
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                    }
                                }
                        }
                    }
                    UiEvent.EVENT_SUCCESS_VALIDATE_CHECKOUT -> {
                        (it.data as? CheckoutTokoFoodData)?.let { checkOutTokoFoodData ->
                            val purchaseAmount = checkOutTokoFoodData.summaryDetail.totalPrice

                            merchantPageAnalytics.clickCheckoutOnMiniCart(
                                purchaseAmount,
                                merchantId
                            )
                        }
                        navigateToNewFragment(TokoFoodPurchaseFragment.createInstance())
                    }
                }
            }
        }
    }

    private fun initializeMiniCartWidget() {
        activityViewModel?.let {
            binding?.miniCartWidget?.initialize(it, viewLifecycleOwner.lifecycleScope, SOURCE)
        }
    }

    private fun setupMerchantLogo() {
        binding?.iuMerchantLogo?.type = ImageUnify.TYPE_CIRCLE
    }

    private fun setupMerchantProfileCarousel() {
        carouselAdapter = MerchantPageCarouselAdapter(this)
        binding?.rvMerchantInfoCarousel?.let {
            it.adapter = carouselAdapter
            it.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun setupProductList() {
        productListAdapter = ProductListAdapter(this)
        binding?.rvProductList?.let {
            it.adapter = productListAdapter
            it.layoutManager = LinearLayoutManager(
                it.context
            )

            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val mLayoutManager = it.layoutManager as? LinearLayoutManager
                    val firstVisibleItemPos =
                        mLayoutManager?.findFirstVisibleItemPosition().orZero()
                    val productListItem =
                        productListAdapter?.getProductListItems()?.filterIndexed { position, _ ->
                            position == firstVisibleItemPos
                        }
                            ?.firstOrNull { productItem -> productItem.productCategory.title.isNotBlank() }
                    productListItem?.let { productsItem ->
                        filterNameSelected = productsItem.productCategory.title
                        setCategoryPlaceholder(filterNameSelected)
                    }
                }
            })
        }
        (binding?.rvProductList?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
    }

    private fun setupOrderNoteBottomSheet() {
        orderNoteBottomSheet = OrderNoteBottomSheet.createInstance(this)
    }

    private fun setupCustomOrderDetailBottomSheet() {
        customOrderDetailBottomSheet = CustomOrderDetailBottomSheet.createInstance(this)
    }

    private fun renderTicker(tickerData: TokoFoodTickerDetail) {
        binding?.tickerMerchantPage?.apply {
            this.tickerType = TYPE_WARNING
            this.tickerShape = SHAPE_FULL
            this.closeButtonVisibility = View.GONE
            this.tickerTitle = tickerData.title
            this.setTextDescription(tickerData.subtitle)
            this.show()
        }
    }

    private fun renderMerchantProfile(merchantProfile: TokoFoodMerchantProfile) {
        val imageUrl = merchantProfile.imageURL ?: ""
        if (imageUrl.isNotBlank()) binding?.iuMerchantLogo?.setImageUrl(imageUrl)
        if (merchantProfile.closeWarning?.isNotBlank() == true) {
            binding?.tpgMerchantCloseWarning?.text = merchantProfile.closeWarning
            binding?.tpgMerchantCloseWarning?.show()
            binding?.tpgDotDivider?.show()
        }
        binding?.tpgMerchantName?.text = merchantProfile.name
        binding?.tpgMerchantCategory?.text = merchantProfile.merchantCategories.joinToString()
        val carouselData = viewModel.mapMerchantProfileToCarouselData(merchantProfile)
        carouselAdapter?.setCarouselData(carouselData)
    }

    private fun setupMerchantInfoBottomSheet(
        name: String,
        address: String,
        merchantOpsHours: List<MerchantOpsHour>
    ) {
        merchantInfoBottomSheet =
            MerchantInfoBottomSheet.createInstance(name, address, merchantOpsHours)
    }

    private fun renderProductList(productListItems: List<ProductListItem>) {
        productListAdapter?.setProductListItems(productListItems)
    }

    private fun scrollToCategorySection(positionItem: Int) {
        context?.let {
            val smoothScroller: RecyclerView.SmoothScroller =
                object : LinearSmoothScroller(it) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }
                }
            smoothScroller.targetPosition = positionItem
            binding?.rvProductList?.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    override fun onCarouselItemClicked() {
        merchantInfoBottomSheet?.show(childFragmentManager)
    }

    override fun onProductCardClicked(
        productListItem: ProductListItem,
        cardPositions: Pair<Int, Int>
    ) {
        val productUiModel = productListItem.productUiModel
        merchantPageAnalytics.clickProductCard(
            getProductItemList(),
            productListItem,
            cardPositions.first,
            merchantId
        )
        val bottomSheet = ProductDetailBottomSheet.createInstance(productUiModel, this)
        bottomSheet.setListener(this@MerchantPageFragment)
        bottomSheet.sendTrackerInMerchantPage {
            viewModel.merchantData?.let {
                merchantPageAnalytics.clickOnOrderProductBottomSheet(
                    productListItem,
                    merchantId,
                    it.name,
                    cardPositions.first
                )
            }
        }
        bottomSheet.setSelectedCardPositions(cardPositions)
        bottomSheet.setProductListItem(productListItem)
        bottomSheet.show(childFragmentManager)
    }

    override fun onAtcButtonClicked(
        productListItem: ProductListItem,
        cardPositions: Pair<Int, Int>
    ) {
        val productUiModel = productListItem.productUiModel
        if (activityViewModel?.shopId == merchantId || activityViewModel?.shopId.isNullOrBlank()) {
            viewModel.productMap[productUiModel.id] = cardPositions
            if (productUiModel.isCustomizable && productUiModel.isAtc) {
                customOrderDetailBottomSheet?.setProductPosition(cardPositions.first)
                customOrderDetailBottomSheet?.setProductUiModel(productUiModel)
                customOrderDetailBottomSheet?.show(childFragmentManager)
            } else if (productUiModel.isCustomizable) {
                navigateToOrderCustomizationPage(
                    cartId = "",
                    productListItem = productListItem,
                    cardPositions.first
                )
            } else {
                val updateParam = viewModel.mapProductUiModelToAtcRequestParam(
                    shopId = merchantId,
                    productUiModel = productUiModel
                )
                activityViewModel?.addToCart(updateParam, SOURCE)
            }
        } else {
            val updateParam = viewModel.mapProductUiModelToAtcRequestParam(
                shopId = merchantId,
                productUiModel = productUiModel
            )

            val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }

            cacheManager?.put(
                ChangeMerchantBottomSheet.KEY_UPDATE_PARAM,
                updateParam
            )
            val bundle = Bundle().apply {
                putString(
                    ChangeMerchantBottomSheet.KEY_CACHE_MANAGER_ID,
                    cacheManager?.id.orEmpty()
                )
            }

            val bottomSheet = ChangeMerchantBottomSheet.newInstance(bundle)

            bottomSheet.setChangeMerchantListener(this)
            bottomSheet.show(childFragmentManager)
        }

        viewModel.merchantData?.let {
            merchantPageAnalytics.clickOnAtcButton(
                productListItem, merchantId,
                it.name,
                cardPositions.first
            )
        }
    }

    override fun changeMerchantConfirmAddToCart(updateParam: UpdateParam) {
        activityViewModel?.deleteAllAtcAndAddProduct(updateParam, SOURCE)
    }

    override fun onAddNoteButtonClicked(
        productId: String,
        orderNote: String,
        cardPositions: Pair<Int, Int>
    ) {
        viewModel.productMap[productId] = cardPositions
        orderNoteBottomSheet?.setSelectedProductId(productId)
        orderNoteBottomSheet?.setOrderNote(orderNote)
        orderNoteBottomSheet?.show(childFragmentManager)
    }

    override fun onDeleteButtonClicked(
        cartId: String,
        productId: String,
        cardPositions: Pair<Int, Int>
    ) {
        viewModel.productMap[productId] = cardPositions
        activityViewModel?.deleteProduct(
            productId = productId,
            cartId = cartId,
            source = SOURCE,
            shopId = merchantId
        )
    }

    override fun onFinishCategoryFilter(categoryFilterList: List<CategoryFilterListUiModel>) {
        val categoryFilter = categoryFilterList.find { it.isSelected }
        categoryFilter?.let {
            filterNameSelected = it.categoryUiModel.title
            binding?.tvCategoryPlaceholder?.text = filterNameSelected
            val productItem = productListAdapter
                ?.getProductListItems()
                ?.find { productList -> productList.productCategory.title == filterNameSelected }
            val position = productListAdapter?.getProductListItems()?.indexOf(productItem)
            if (position != null && position != RecyclerView.NO_POSITION) {
                scrollToCategorySection(position)
            }
        }
    }

    override fun onIncreaseQtyButtonClicked(
        productId: String,
        quantity: Int,
        cardPositions: Pair<Int, Int>
    ) {
        viewModel.productMap[productId] = cardPositions
        val dataSetPosition = viewModel.getDataSetPosition(cardPositions)
        val productUiModel = productListAdapter?.getProductUiModel(dataSetPosition)
        productUiModel?.orderQty = quantity
        productUiModel?.run {
            val updateParam = viewModel.mapProductUiModelToAtcRequestParam(
                shopId = merchantId,
                productUiModel = productUiModel
            )
            activityViewModel?.updateQuantity(updateParam, SOURCE)
        }
    }

    override fun onNavigateToOrderCustomizationPage(
        cartId: String,
        productUiModel: ProductUiModel,
        productPosition: Int
    ) {
        val productListItem =
            getProductItemList().find { it.productUiModel.id == productUiModel.id }
        if (productListItem != null) {
            navigateToOrderCustomizationPage(
                cartId = cartId,
                productListItem = productListItem,
                productPosition = productPosition
            )
        }
    }

    override fun onDecreaseQtyButtonClicked(
        productId: String,
        quantity: Int,
        cardPositions: Pair<Int, Int>
    ) {
        viewModel.productMap[productId] = cardPositions
        val dataSetPosition = viewModel.getDataSetPosition(cardPositions)
        val productUiModel = productListAdapter?.getProductUiModel(dataSetPosition)
        productUiModel?.orderQty = quantity
        productUiModel?.run {
            val updateParam = viewModel.mapProductUiModelToAtcRequestParam(
                shopId = merchantId,
                productUiModel = productUiModel
            )
            activityViewModel?.updateQuantity(updateParam, SOURCE)
        }
    }

    override fun onImpressProductCard(productListItem: ProductListItem, position: Int) {
        merchantPageAnalytics.impressionOnProductCard(
            getProductItemList(),
            productListItem,
            position,
            merchantId
        )
    }

    override fun onSaveNoteButtonClicked(productId: String, orderNote: String) {
        val cardPositions = viewModel.productMap[productId]
        cardPositions?.run {
            val dataSetPosition = viewModel.getDataSetPosition(this)
            val productUiModel = productListAdapter?.getProductUiModel(dataSetPosition)
            productUiModel?.orderNote = orderNote
            productUiModel?.run {
                val updateParam = viewModel.mapProductUiModelToAtcRequestParam(
                    shopId = merchantId,
                    productUiModel = productUiModel
                )
                activityViewModel?.updateNotes(updateParam, SOURCE)
            }
        }
        orderNoteBottomSheet?.dismiss()
    }

    override fun onFoodItemShareClicked(productUiModel: ProductUiModel) {
        shareFoodItem(viewModel.merchantData, productUiModel)
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        merchantShareComponent?.let {

            merchantPageAnalytics.clickShareBottomSheet(
                merchantShareComponent?.id.orEmpty(),
                userSession.userId.orEmpty()
            )

            merchantShareComponentUtil?.createShareRequest(
                shareModel = shareModel,
                merchantShareComponent = it,
                activity = activity,
                view = view,
                onSuccess = {
                    universalShareBottomSheet?.dismiss()
                }
            )
        }
    }

    override fun onCloseOptionClicked() {
        merchantPageAnalytics.closeShareBottomSheet(
            merchantShareComponent?.id.orEmpty(),
            userSession.userId.orEmpty()
        )
    }


    override fun onDeleteCustomOrderButtonClicked(cartId: String, productId: String) {
        activityViewModel?.deleteProduct(
            productId = productId,
            cartId = cartId,
            source = SOURCE,
            shopId = merchantId
        )
    }

    override fun onUpdateCustomOrderQtyButtonClicked(
        productId: String,
        quantity: Int,
        customOrderDetail: CustomOrderDetail
    ) {
        customOrderDetail.qty = quantity
        val updateParam = viewModel.mapCustomOrderDetailToAtcRequestParam(
            shopId = merchantId,
            productId = productId,
            customOrderDetail = customOrderDetail
        )
        activityViewModel?.updateQuantity(updateParam, SOURCE)
    }

    override fun onButtonCtaClickListener(appLink: String) {
        var applicationLink = ApplinkConstInternalGlobal.ADD_PHONE
        if (appLink.isNotEmpty()) applicationLink = appLink
        context?.run {
            TokofoodRouteManager.routePrioritizeInternal(this, applicationLink)
        }
    }

    private fun navigateToOrderCustomizationPage(
        cartId: String,
        productListItem: ProductListItem,
        productPosition: Int
    ) {
        viewModel.productListItems = productListAdapter?.getProductListItems() ?: mutableListOf()

        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        val variantTracker = VariantWrapperUiModel(
            productListItem,
            merchantId,
            viewModel.merchantData?.name.orEmpty(),
            productPosition
        )

        cacheManager?.put(
            OrderCustomizationFragment.BUNDLE_KEY_VARIANT_TRACKER,
            variantTracker
        )

        val orderCustomizationFragment = OrderCustomizationFragment.createInstance(
            productUiModel = productListItem.productUiModel,
            cartId = cartId,
            merchantId = merchantId,
            cacheManagerId = cacheManager?.id.orEmpty()
        )
        navigateToNewFragment(orderCustomizationFragment)
    }

    private fun getProductItemList() = productListAdapter?.getProductListItems().orEmpty()

    private fun goToLoginPage() {
        context?.run {
            val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN)
        }
        viewModel.visitedLoginPage = true
    }

    private fun validateAddressData() {
        context?.run {
            ChooseAddressUtils.getLocalizingAddressData(this).let { addressData ->
                when {
                    addressData.address_id.isBlank() -> {
                        navigateToNewFragment(ManageLocationFragment.createInstance(
                                negativeCaseId = EMPTY_STATE_NO_ADDRESS,
                                merchantId = merchantId
                        ))
                    }
                    addressData.latLong.isBlank() -> {
                        navigateToNewFragment(ManageLocationFragment.createInstance(
                                negativeCaseId = EMPTY_STATE_NO_PIN_POINT,
                                merchantId = merchantId
                        ))
                    }
                    else -> {}
                }
            }
        }
    }

    private fun onResultFromLogin(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            validateAddressData()
        } else {
            activity?.finish()
        }
    }

    private fun toggleMiniCartVisibility(shouldShow: Boolean) {
        binding?.miniCartWidget?.showWithCondition(shouldShow)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_LOGIN -> onResultFromLogin(resultCode, data)
        }
    }

    companion object {
        const val SHARE = "share"

        private const val REQUEST_CODE_LOGIN = 123
        private const val SOURCE = "merchant_page"

        fun createInstance(): MerchantPageFragment {
            return MerchantPageFragment()
        }
    }
}