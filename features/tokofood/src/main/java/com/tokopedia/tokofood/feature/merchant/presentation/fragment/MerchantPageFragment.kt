package com.tokopedia.tokofood.feature.merchant.presentation.fragment

import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.databinding.FragmentMerchantPageLayoutBinding
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
import com.tokopedia.tokofood.feature.merchant.presentation.model.MerchantOpsHour
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.MerchantCarouseItemViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductCardViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.MerchantPageViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.SHAPE_FULL
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_WARNING
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class MerchantPageFragment : BaseMultiFragment(),
    MerchantCarouseItemViewHolder.OnCarouselItemClickListener,
    ProductCardViewHolder.OnProductCardItemClickListener,
    OrderNoteBottomSheet.OnSaveNoteButtonClickListener,
    CategoryFilterBottomSheet.CategoryFilterListener,
    ProductDetailBottomSheet.Listener {

    private var parentActivity: HasViewModel<MultipleFragmentsViewModel>? = null

    private val activityViewModel: MultipleFragmentsViewModel?
        get() = parentActivity?.viewModel()

    private var binding: FragmentMerchantPageLayoutBinding? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(MerchantPageViewModel::class.java)
    }

    private val dummyMerchantId = "88d9f5a4-7410-46f5-a835-93955b8e3496"

    private var universalShareBottomSheet: UniversalShareBottomSheet? = null

    private var merchantInfoBottomSheet: MerchantInfoBottomSheet? = null
    private var orderNoteBottomSheet: OrderNoteBottomSheet? = null

    private var carouselAdapter: MerchantPageCarouselAdapter? = null
    private var productListAdapter: ProductListAdapter? = null

    private var filterNameSelected: String = ""

    override fun getFragmentToolbar(): Toolbar? {
        return binding?.toolbar
    }

    override fun getFragmentTitle(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                shareMerchantPage()
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
        setBackgroundColor()
        setupMerchantLogo()
        setupMerchantProfileCarousel()
        setupProductList()
        setupOrderNoteBottomSheet()
        observeLiveData()
        fetchMerchantData()
        setupCardSticky()
    }

    private fun setBackgroundColor() {
        binding?.toolbarParent?.let {
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

                if (verticalOffset == Int.ZERO) {
                    //collapsed
                    binding?.merchantInfoViewGroup?.show()
                    binding?.cardUnifySticky?.hide()
                } else {
                    //expanded
                    binding?.merchantInfoViewGroup?.hide()
                    binding?.cardUnifySticky?.show()
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
        val dummyLatLong = "-6.2,106.816666"
        showLoader()
        viewModel.getMerchantData(dummyMerchantId, dummyLatLong, "")
    }

    private fun showLoader() {
        binding?.merchantInfoViewGroup?.hide()
        binding?.shimmeringMerchantPage?.root?.show()
    }

    private fun hideLoader() {
        binding?.shimmeringMerchantPage?.root?.hide()
    }

    private fun shareMerchantPage() {

    }

    private fun shareFoodItem() {

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
                    val productListItems =
                        viewModel.mapFoodCategoriesToProductListItems(isShopClosed, foodCategories)
                    filterNameSelected =
                        productListItems.firstOrNull()?.productCategory?.title.orEmpty()
                    renderProductList(productListItems)
                    setCategoryPlaceholder()
                }
                is Fail -> {

                }
            }
        })
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

    override fun onProductCardClicked(productUiModel: ProductUiModel) {
        ProductDetailBottomSheet.createInstance(productUiModel).apply {
            setListener(this@MerchantPageFragment)
            show(childFragmentManager)
        }
    }

    override fun onAtcButtonClicked(productUiModel: ProductUiModel, cardPositions: Pair<Int, Int>) {
        viewModel.cardPositions = cardPositions
        if (productUiModel.isCustomizable && productUiModel.isAtc) {
            CustomOrderDetailBottomSheet.createInstance(
                productName = productUiModel.name,
                customOrderDetails = productUiModel.customOrderDetails
            ).apply {
                show(childFragmentManager)
            }
        } else if (productUiModel.isCustomizable) {
            val orderCustomizationFragment =
                OrderCustomizationFragment.createInstance(productUiModel)
            navigateToNewFragment(orderCustomizationFragment)
        } else {
            val updateParam = TokoFoodMerchantUiModelMapper.mapProductUiModelToAtcRequestParam(
                shopId = userSession.shopId,
                productUiModels = listOf(productUiModel),
                addOnUiModels = listOf()
            )
            activityViewModel?.addToCart(updateParam, "")
            productListAdapter?.updateAtcStatus(true, cardPositions)
        }
    }

    override fun onAddNoteButtonClicked(orderNote: String, cardPositions: Pair<Int, Int>) {
        orderNoteBottomSheet?.renderOrderNote(orderNote)
        orderNoteBottomSheet?.show(childFragmentManager)
        viewModel.cardPositions = cardPositions
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

    override fun onDeleteButtonClicked() {

    }

    override fun onIncreaseQtyButtonClicked() {

    }

    override fun onDecreaseQtyButtonClicked() {

    }

    override fun onSaveNoteButtonClicked(orderNote: String) {
        viewModel.cardPositions?.run {
            productListAdapter?.updateOrderNote(orderNote, this)
        }
        orderNoteBottomSheet?.dismiss()
    }

    override fun onFoodItemShareClicked() {
        universalShareBottomSheet = UniversalShareBottomSheet().apply {

        }
    }
}