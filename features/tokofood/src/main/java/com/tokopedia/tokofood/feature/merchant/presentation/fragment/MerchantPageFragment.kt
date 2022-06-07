package com.tokopedia.tokofood.feature.merchant.presentation.fragment

import android.content.Context
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
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.constants.ShareComponentConstants
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.Constant
import com.tokopedia.tokofood.databinding.FragmentMerchantPageLayoutBinding
import com.tokopedia.tokofood.feature.merchant.analytics.MerchantPageAnalytics
import com.tokopedia.tokofood.feature.merchant.common.util.MerchantShareComponentUtil
import com.tokopedia.tokofood.feature.merchant.di.DaggerMerchantPageComponent
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodMerchantProfile
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodTickerDetail
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.MerchantPageCarouselAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.ProductListAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.CategoryFilterBottomSheet
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
    ShareBottomsheetListener {

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

    private var merchantId: String = ""

    private var productId: String = ""

    private val merchantShareComponentUtil: MerchantShareComponentUtil? by lazy {
        context?.let { MerchantShareComponentUtil(it) }
    }

    private var universalShareBottomSheet: UniversalShareBottomSheet? = null

    private var merchantInfoBottomSheet: MerchantInfoBottomSheet? = null
    private var orderNoteBottomSheet: OrderNoteBottomSheet? = null

    private var carouselAdapter: MerchantPageCarouselAdapter? = null
    private var productListAdapter: ProductListAdapter? = null

    private var filterNameSelected: String = ""

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackgroundDefaultColor()
        setupMerchantLogo()
        setupMerchantProfileCarousel()
        setupProductList()
        setupOrderNoteBottomSheet()
        setupCardSticky()
        observeLiveData()
        collectFlow()
        fetchMerchantData()
        initializeMiniCartWidget()
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

    private fun setCategoryPlaceholder() {
        filterNameSelected = productListAdapter?.getProductListItems()
            ?.firstOrNull()?.productCategory?.title.orEmpty()
        binding?.tvCategoryPlaceholder?.text = filterNameSelected
    }

    private fun fetchMerchantData() {
        showLoader()
        context?.run {
            ChooseAddressUtils.getLocalizingAddressData(this)
                    .let { addressData ->
                        viewModel.getMerchantData(
                                merchantId,
                                addressData.latLong,
                                ""
                        )
                    }
        }
    }

    private fun showLoader() {
        binding?.merchantInfoViewGroup?.hide()
        binding?.shimmeringMerchantPage?.root?.show()
    }

    private fun hideLoader() {
        binding?.shimmeringMerchantPage?.root?.hide()
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
            hideLoader()
            when (result) {
                is Success -> {
                    binding?.merchantInfoViewGroup?.show()
                    setupAppBarLayoutListener()

                    val merchantData = result.data.tokofoodGetMerchantData
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
                    val merchantOpsHours =
                        viewModel.mapOpsHourDetailsToMerchantOpsHours(merchantProfile.opsHourDetail)
                    setupMerchantInfoBottomSheet(name, address, merchantOpsHours)
                    // render product list
                    val isShopClosed = merchantProfile.opsHourFmt.isWarning
                    val foodCategories = merchantData.categories
                    val productListItems = viewModel.mapFoodCategoriesToProductListItems(isShopClosed, foodCategories)
                    filterNameSelected =
                        productListItems.firstOrNull()?.productCategory?.title.orEmpty()
                    val finalProductListItems = viewModel.applyProductSelection(productListItems, viewModel.selectedProducts)
                    renderProductList(finalProductListItems)
                    setCategoryPlaceholder()
                }
                is Fail -> {

                }
            }
        })
    }

    private fun collectFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityViewModel?.cartDataValidationFlow?.collect {
                when (it.state) {
                    UiEvent.EVENT_SUCCESS_LOAD_CART -> {
                        activityViewModel?.cartDataFlow?.collect { cartData ->
                            viewModel.selectedProducts = cartData.availableSection.products
                        }
                    }
                    UiEvent.EVENT_SUCCESS_ADD_TO_CART -> {
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
                                                        dataSetPosition = viewModel.getDataSetPosition(this),
                                                        adapterPosition = viewModel.getAdapterPosition(this)
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
                                                val cardPositions = viewModel.productMap[requestParam.productId]
                                                cardPositions?.run {
                                                    productListAdapter?.updateProductUiModel(
                                                        cartTokoFood = cartTokoFood,
                                                        dataSetPosition = viewModel.getDataSetPosition(this),
                                                        adapterPosition = viewModel.getAdapterPosition(this)
                                                    )
                                                }
                                            }
                                    }
                                    view?.let { view ->
                                        Toaster.build(
                                            view = view,
                                            text = getString(com.tokopedia.tokofood.R.string.text_note_saved_message),
                                            duration = Toaster.LENGTH_SHORT,
                                            type = Toaster.TYPE_NORMAL,
                                            actionText = getString(com.tokopedia.tokofood.R.string.action_merchant_ok)
                                        ).show()
                                    }
                                }
                        }
                    }

                    UiEvent.EVENT_SUCCESS_DELETE_PRODUCT -> {
                        (it.data as? Pair<*, *>)?.let { pair ->
                            (pair.first as? String)?.let { cartId ->
                                (pair.second as? CartTokoFoodData)?.carts?.firstOrNull()?.let { product ->
                                    val cardPositions = viewModel.productMap[product.productId]
                                    cardPositions?.run {
                                        val dataSetPosition = viewModel.getDataSetPosition(this)
                                        val productUiModel = productListAdapter?.getProductUiModel(dataSetPosition)
                                        if (productUiModel?.isCustomizable == true) {
                                            productListAdapter?.removeCustomOrder(
                                                    cartId = cartId,
                                                    dataSetPosition = dataSetPosition
                                            )
                                        } else {
                                            productListAdapter?.resetProductUiModel(
                                                    dataSetPosition = dataSetPosition,
                                                    adapterPosition = viewModel.getAdapterPosition(this)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY -> {
                        (it.data as? Pair<*, *>)?.let { pair ->
                            (pair.first as? UpdateParam)?.productList?.firstOrNull()?.let { requestParam ->
                                (pair.second as? CartTokoFoodData)?.let { cartTokoFoodData ->
                                    cartTokoFoodData.carts.firstOrNull { data -> data.productId == requestParam.productId }?.let { cartTokoFood ->
                                        val cardPositions = viewModel.productMap[requestParam.productId]
                                        cardPositions?.run {
                                            val dataSetPosition = viewModel.getDataSetPosition(this)
                                            val productUiModel = productListAdapter?.getProductUiModel(dataSetPosition)
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
                                                    adapterPosition = viewModel.getAdapterPosition(this)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initializeMiniCartWidget() {
        activityViewModel?.let {
            binding?.miniCartWidget?.initialize(it, viewLifecycleOwner.lifecycleScope, SOURCE)
            binding?.miniCartWidget?.setOnATCClickListener {
                navigateToNewFragment(TokoFoodPurchaseFragment.createInstance())
            }
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
        }
        (binding?.rvProductList?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    private fun setupOrderNoteBottomSheet() {
        orderNoteBottomSheet = OrderNoteBottomSheet.createInstance(this)
    }

    private fun renderTicker(tickerData: TokoFoodTickerDetail) {
        binding?.tickerMerchantPage?.apply {
            this.tickerType = TYPE_WARNING
            this.tickerShape = SHAPE_FULL
            this.closeButtonVisibility = View.GONE
            this.tickerTitle = tickerTitle
            this.setTextDescription(tickerData.subtitle)
            this.show()
        }
    }

    private fun renderMerchantProfile(merchantProfile: TokoFoodMerchantProfile) {
        val imageUrl = merchantProfile.imageURL ?: ""
        if (imageUrl.isNotBlank()) binding?.iuMerchantLogo?.setImageUrl(imageUrl)
        binding?.tpgMerchantName?.text = merchantProfile.name
        binding?.tpgMerchantCategory?.text = merchantProfile.merchantCategories.joinToString()
        val carouselData = viewModel.mapMerchantProfileToCarouselData(merchantProfile)
        carouselAdapter?.setCarouselData(carouselData)
        // TODO: add close in xxx
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

    override fun onProductCardClicked(productUiModel: ProductUiModel, cardPositions: Pair<Int, Int>) {
        val bottomSheet = ProductDetailBottomSheet.createInstance(productUiModel, this)
        bottomSheet.setListener(this@MerchantPageFragment)
        bottomSheet.setSelectedCardPositions(cardPositions)
        bottomSheet.show(childFragmentManager)
    }

    override fun onAtcButtonClicked(productUiModel: ProductUiModel, cardPositions: Pair<Int, Int>) {
        viewModel.productMap[productUiModel.id] = cardPositions
        if (productUiModel.isCustomizable && productUiModel.isAtc) {
            CustomOrderDetailBottomSheet.createInstance(productUiModel = productUiModel, this).show(childFragmentManager)
        } else if (productUiModel.isCustomizable) {
            navigateToOrderCustomizationPage(cartId = "", productUiModel = productUiModel)
        } else {
            val updateParam = viewModel.mapProductUiModelToAtcRequestParam(
                    shopId = merchantId,
                    productUiModel = productUiModel
            )
            activityViewModel?.addToCart(updateParam, SOURCE)
        }
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

    override fun onDeleteButtonClicked(cartId: String, productId: String, cardPositions: Pair<Int, Int>) {
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

    override fun onNavigateToOrderCustomizationPage(cartId: String, productUiModel: ProductUiModel) {
        navigateToOrderCustomizationPage(cartId = cartId, productUiModel = productUiModel)
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

    override fun onUpdateCustomOrderQtyButtonClicked(productId: String, quantity: Int, customOrderDetail: CustomOrderDetail) {
        customOrderDetail.qty = quantity
        val updateParam = viewModel.mapCustomOrderDetailToAtcRequestParam(
            shopId = merchantId,
            productId = productId,
            customOrderDetail = customOrderDetail
        )
        activityViewModel?.updateQuantity(updateParam, SOURCE)
    }

    private fun navigateToOrderCustomizationPage(cartId: String, productUiModel: ProductUiModel) {
        val orderCustomizationFragment = OrderCustomizationFragment.createInstance(
            productUiModel = productUiModel,
            cartId = cartId,
            merchantId = merchantId
        )
        navigateToNewFragment(orderCustomizationFragment)
    }

    companion object {
        const val SHARE = "share"

        private const val SOURCE = "merchant_page"

        fun createInstance(): MerchantPageFragment {
            return MerchantPageFragment()
        }
    }
}