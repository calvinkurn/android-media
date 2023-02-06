package com.tokopedia.tokofood.feature.merchant.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
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
import com.tokopedia.abstraction.base.view.fragment.enums.BaseMultiFragmentLaunchMode
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.constants.ShareComponentConstants
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodBottomSheet
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.Constant
import com.tokopedia.tokofood.common.util.TokofoodErrorLogger
import com.tokopedia.tokofood.common.util.TokofoodExt.addAndReturnImpressionListener
import com.tokopedia.tokofood.common.util.TokofoodExt.getGlobalErrorType
import com.tokopedia.tokofood.common.util.TokofoodExt.getSuccessUpdateResultPair
import com.tokopedia.tokofood.common.util.TokofoodExt.setBackIconUnify
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
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.CategoryFilterBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.ChangeMerchantBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.CustomOrderDetailBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.MerchantInfoBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.OrderNoteBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.PhoneNumberVerificationBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.ProductDetailBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.ManageLocationFragment.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.ManageLocationFragment.Companion.EMPTY_STATE_NO_PIN_POINT
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.ManageLocationFragment.Companion.EMPTY_STATE_OUT_OF_COVERAGE
import com.tokopedia.tokofood.feature.merchant.presentation.mapper.TokoFoodMerchantUiModelMapper
import com.tokopedia.tokofood.feature.merchant.presentation.model.CategoryFilterListUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail
import com.tokopedia.tokofood.feature.merchant.presentation.model.MerchantOpsHour
import com.tokopedia.tokofood.feature.merchant.presentation.model.MerchantShareComponent
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.VariantWrapperUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.mvc.TokofoodMerchantMvcTrackerImpl
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.MerchantCarouseItemViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductCardViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.MerchantPageViewModel
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.TokoFoodPromoFragment
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.SHAPE_FULL
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_WARNING
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.resources.isDarkMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import timber.log.Timber
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
    PhoneNumberVerificationBottomSheet.OnButtonCtaClickListener,
    TokofoodScrollChangedListener {

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

    private val toolbarHeight by lazy {
        val tv = TypedValue()
        context?.let {
            if (it.theme?.resolveAttribute(android.R.attr.actionBarSize, tv, true) == true) {
                val actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, it.resources.displayMetrics)
                actionBarHeight
            } else {
                Int.ZERO
            }
        }.orZero()
    }

    // TODO: move states to view model
    private var merchantId: String = ""
    private var productId: String = ""
    private var currentPromoName: String? = null

    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var merchantInfoBottomSheet: MerchantInfoBottomSheet? = null
    private var orderNoteBottomSheet: OrderNoteBottomSheet? = null
    private var customOrderDetailBottomSheet: CustomOrderDetailBottomSheet? = null
    private var carouselAdapter: MerchantPageCarouselAdapter? = null
    private var productListAdapter: ProductListAdapter? = null

    private var merchantShareComponent: MerchantShareComponent? = null

    private var cartDataUpdateJob: Job? = null
    private var uiEventUpdateJob: Job? = null
    private var updateQuantityJob: Job? = null
    private var onOffsetChangedListener: AppBarLayout.OnOffsetChangedListener? = null
    private var onScrollChangedListenerList: MutableList<ViewTreeObserver.OnScrollChangedListener> = mutableListOf()

    private var mvcImpressHolder: ImpressHolder? = null
    private var mvcOnScrollChangeListener: ViewTreeObserver.OnScrollChangedListener? = null

    override fun getFragmentToolbar(): Toolbar? = binding?.toolbarMerchantPage

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
        // handle negative case #1 non-login
        if (!userSession.isLoggedIn) {
            goToLoginPage()
        }

    }

    override fun initInjector() {
        activity?.let {
            DaggerMerchantPageComponent
                .builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun getLaunchMode(): BaseMultiFragmentLaunchMode {
        return BaseMultiFragmentLaunchMode.SINGLE_TASK
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_merchant_page, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                shareMerchantPage(viewModel.merchantData?.merchantProfile)
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
        currentPromoName = null
        removeCurrentMvcScrollChangedListener()
        removeListeners()
    }

    override fun onStart() {
        super.onStart()
        initializeMiniCartWidget()
        cartDataUpdateJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            collectCartDataFlow()
        }
        uiEventUpdateJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            collectUiEventFlow()
        }
        updateQuantityJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            collectUpdateQuantity()
        }
    }

    override fun onResume() {
        super.onResume()
        merchantPageAnalytics.openMerchantPage(
            merchantId,
            viewModel.merchantData?.merchantProfile?.opsHourFmt?.isWarning.orFalse()
        )
        applySelectedProducts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarBackIconUnify()
        setBackgroundDefaultColor()
        setHeaderBackground()
        setupMerchantLogo()
        setupMerchantProfileCarousel()
        setupProductList()
        setupOrderNoteBottomSheet()
        setupCardSticky()
        observeLiveData()

        // TODO : move this block of code into a function
        if (viewModel.productListItems.isNotEmpty()) {
            viewModel.merchantData?.run {
                // render ticker data if not empty
                val tickerData = this.ticker
                if (!viewModel.isTickerDetailEmpty(tickerData)) {
                    renderTicker(tickerData)
                }
                // render merchant logo, name, categories, carousel
                val merchantProfile = this.merchantProfile
                renderMerchantProfile(merchantProfile)
                // setup merchant info bottom sheet
                val name = merchantProfile.name
                val address = merchantProfile.address
                val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
                val merchantOpsHours = viewModel.mapOpsHourDetailsToMerchantOpsHours(today, merchantProfile.opsHourDetail)
                setupMerchantInfoBottomSheet(name, address, merchantOpsHours)
            }
            renderProductList(viewModel.productListItems)
            setCategoryPlaceholder(viewModel.filterNameSelected)
            binding?.toolbarParent?.setExpanded(!viewModel.isStickyBarVisible)
            binding?.cardUnifySticky?.isVisible = viewModel.isStickyBarVisible
        } else {
            // handle negative case: no-address,no-pinpoint
            validateAddressData()
        }

    }

    override fun onStop() {
        super.onStop()
        cartDataUpdateJob?.cancel()
        uiEventUpdateJob?.cancel()
        updateQuantityJob?.cancel()
    }

    private fun setToolbarBackIconUnify() {
        binding?.toolbarMerchantPage?.setBackIconUnify()
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

    private fun setHeaderBackground() {
        context?.let {
            val backgroundResourceId =
                if (it.isDarkMode()) {
                    com.tokopedia.tokofood.R.drawable.header_background_dark
                } else {
                    com.tokopedia.tokofood.R.drawable.header_background
                }
            binding?.bgMerchantHeader?.setImageResource(backgroundResourceId)
        }
    }

    private fun setToolbarTransparentColor() {
        setHeaderBackground()
    }

    private fun setToolbarWhiteColor() {
        try {
            binding?.bgMerchantHeader?.setImageDrawable(null)
        } catch (ignored: Exception) {}
    }

    private fun setupAppBarLayoutListener() {
        if (onOffsetChangedListener == null) {
            onOffsetChangedListener = object :
                AppBarLayout.OnOffsetChangedListener {

                override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                    if (appBarLayout == null) return
                    val offset = abs(verticalOffset)
                    if (offset >= (appBarLayout.totalScrollRange - binding?.toolbarMerchantPage?.height.orZero())
                    ) {
                        // show sticky filter
                        binding?.cardUnifySticky?.show()
                        setToolbarWhiteColor()
                        viewModel.isStickyBarVisible = binding?.cardUnifySticky?.isVisible ?: false
                    } else {
                        // hide stick filter
                        binding?.cardUnifySticky?.hide()
                        setToolbarTransparentColor()
                        viewModel.isStickyBarVisible = binding?.cardUnifySticky?.isVisible ?: false
                    }
                }
            }
            onOffsetChangedListener?.let {
                binding?.toolbarParent?.addOnOffsetChangedListener(it)
            }
        }
    }

    private fun setupCardSticky() {
        binding?.cardUnifySticky?.run {
            setOnClickListener {
                hideKeyboard()
                val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
                val categoryFilter =
                    TokoFoodMerchantUiModelMapper.mapProductListItemToCategoryFilterUiModel(
                        viewModel.filterList, viewModel.filterNameSelected
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
                bottomSheet.setCategoryFilterListener(this@MerchantPageFragment)
                bottomSheet.show(childFragmentManager)
            }
        }
    }

    private fun setCategoryPlaceholder(filterNameSelected: String) {
        binding?.tvCategoryPlaceholder?.text = filterNameSelected
    }

    private fun openSharedProductDetail(sharedProductId: String) {
        if (sharedProductId.isBlank()) return
        else {
            val productListItem = productListAdapter?.getProductListItems()?.find { productList -> productList.productUiModel.id == sharedProductId }
            val position = productListAdapter?.getProductListItems()?.indexOf(productListItem)
            if (position != null && position != RecyclerView.NO_POSITION) {
                // post delayed runnable using the main looper; allowing rv to have enough data to be scrolled
                Handler(Looper.getMainLooper()).postDelayed({
                    scrollToCategorySection(position)
                    productListItem?.run {
                        // adapter dataset position should be the same at the start
                        onProductCardClicked(productListItem, position to position)
                    }
                }, DELAY_TIME_HUNDRED_MILLIS)
            }
        }
    }

    private fun fetchMerchantData() {
        showLoader()
        getMerchantData()
    }

    private fun getMerchantData() {
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

    private fun applySelectedProducts() {
        activityViewModel?.hasCartUpdatedIntoLatestState?.value?.let { hasCartUpdated ->
            if (hasCartUpdated) {
                viewModel.getAppliedProductSelection()?.let { appliedProductList ->
                    renderProductList(appliedProductList)
                }
            }
        }
    }

    private fun showLoader() {
        binding?.merchantInfoViewGroup?.hide()
        binding?.geMerchantPageErrorView?.hide()
        binding?.toolbarParent?.show()
        binding?.productListLayout?.show()
        binding?.shimmeringMerchantPage?.root?.show()
    }

    private fun hideLoader() {
        binding?.shimmeringMerchantPage?.root?.hide()
        binding?.geMerchantPageErrorView?.hide()
        binding?.toolbarParent?.show()
        binding?.productListLayout?.show()
        binding?.merchantInfoViewGroup?.show()
    }

    private fun showGlobalError(throwable: Throwable) {
        binding?.geMerchantPageErrorView?.setType(throwable.getGlobalErrorType())
        binding?.geMerchantPageErrorView?.setActionClickListener { fetchMerchantData() }
        binding?.geMerchantPageErrorView?.show()
    }

    private fun shareMerchantPage(
        tokoFoodMerchantProfile: TokoFoodMerchantProfile?
    ) {
        merchantShareComponent = null
        merchantShareComponent = merchantShareComponentUtil?.getMerchantShareComponent(
            tokoFoodMerchantProfile,
            merchantId
        )
        context?.let {
            merchantShareComponent?.let { merchantShareComponent ->
                SharingUtil.saveImageFromURLToStorage(
                    it,
                    merchantShareComponent.previewThumbnail
                ) { storageImagePath ->
                    showMerchantShareBottomSheet(merchantShareComponent, storageImagePath)
                }
            }
        }
    }

    private fun showMerchantShareBottomSheet(
        merchantShareComponent: MerchantShareComponent,
        storageImagePath: String
    ) {
        if (isAdded) {
            universalShareBottomSheet = null
            universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
                init(this@MerchantPageFragment)
                setUtmCampaignData(
                    pageName = ShareComponentConstants.TOKOFOOD,
                    userId = userSession.userId.orEmpty(),
                    pageIdConstituents = listOf(
                        ShareComponentConstants.MERCHANT,
                        merchantShareComponent.id
                    ),
                    feature = SHARE
                )
                setMetaData(
                    tnTitle = merchantShareComponent.previewTitle,
                    tnImage = merchantShareComponent.previewThumbnail,
                )
                setOgImageUrl(imgUrl = merchantShareComponent.ogImage)
                imageSaved(storageImagePath)
            }

            universalShareBottomSheet?.show(childFragmentManager, this)
        }
    }

    private fun shareFoodItem(
        tokoFoodMerchantProfile: TokoFoodMerchantProfile?,
        productUiModel: ProductUiModel
    ) {
        if (isAdded) {
            merchantShareComponent = null
            merchantShareComponent = merchantShareComponentUtil?.getFoodShareComponent(
                tokoFoodMerchantProfile,
                productUiModel,
                merchantId
            )
            context?.let {
                merchantShareComponent?.let { merchantShareComponent ->
                    SharingUtil.saveImageFromURLToStorage(
                        it,
                        merchantShareComponent.previewThumbnail
                    ) { storageImagePath ->
                        showFoodShareBottomSheet(merchantShareComponent, storageImagePath)
                    }
                }
            }
        }
    }

    private fun showFoodShareBottomSheet(
        merchantShareComponent: MerchantShareComponent,
        storageImagePath: String
    ) {
        universalShareBottomSheet = null
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@MerchantPageFragment)
            setUtmCampaignData(
                pageName = ShareComponentConstants.TOKOFOOD,
                userId = userSession.userId.orEmpty(),
                pageIdConstituents = listOf(
                    ShareComponentConstants.Merchant.FOOD,
                    merchantShareComponent.id
                ),
                feature = SHARE
            )
            setMetaData(
                tnTitle = merchantShareComponent.previewTitle,
                tnImage = merchantShareComponent.previewThumbnail,
            )
            setOgImageUrl(imgUrl = merchantShareComponent.ogImage)
            imageSaved(storageImagePath)
        }

        merchantPageAnalytics.impressShareBottomSheet(
            merchantShareComponent.id,
            userSession.userId.orEmpty()
        )

        universalShareBottomSheet?.show(childFragmentManager, this)
    }

    private fun observeLiveData() {
        viewModel.getMerchantDataResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    val merchantData = result.data.tokofoodGetMerchantData
                    val isDeliverable = merchantData.merchantProfile.deliverable
                    if (isDeliverable) {
                        hideLoader()
                        setupAppBarLayoutListener()
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
                        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
                        val merchantOpsHours = viewModel.mapOpsHourDetailsToMerchantOpsHours(
                            today,
                            merchantProfile.opsHourDetail
                        )
                        setupMerchantInfoBottomSheet(name, address, merchantOpsHours)
                        // render product list
                        val isShopClosed = merchantProfile.opsHourFmt.isWarning
                        val foodCategories = merchantData.categories
                        val productListItems = viewModel.mapFoodCategoriesToProductListItems(
                            isShopClosed,
                            foodCategories
                        )
                        // set default category filter selection
                        viewModel.filterNameSelected =
                            productListItems.firstOrNull()?.productCategory?.title.orEmpty()
                        val finalProductListItems = viewModel.applyProductSelection(
                            productListItems,
                            viewModel.selectedProducts
                        )
                        renderProductList(finalProductListItems)
                        setCategoryPlaceholder(viewModel.filterNameSelected)
                        openSharedProductDetail(sharedProductId = productId)
                    } else {
                        navigateToNewFragment(
                            ManageLocationFragment.createInstance(
                                negativeCaseId = EMPTY_STATE_OUT_OF_COVERAGE,
                                merchantId = merchantId
                            )
                        )
                    }
                }
                is Fail -> {
                    binding?.toolbarParent?.hide()
                    binding?.productListLayout?.hide()
                    showGlobalError(result.throwable)
                    logExceptionToServerLogger(
                        result.throwable,
                        TokofoodErrorLogger.ErrorType.ERROR_PAGE,
                        TokofoodErrorLogger.ErrorDescription.RENDER_PAGE_ERROR
                    )
                }
            }
        }

        viewModel.chooseAddress.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    setupChooseAddress(it.data)
                }

                is Fail -> {
                    validateAddressData()
                    logExceptionToServerLogger(
                        it.throwable,
                        TokofoodErrorLogger.ErrorType.ERROR_CHOOSE_ADDRESS,
                        TokofoodErrorLogger.ErrorDescription.ERROR_CHOOSE_ADDRESS_MERCHANT_PAGE
                    )
                }
            }
        }

        viewModel.mvcLiveData.observe(viewLifecycleOwner) {
            renderMvc(it)
        }
    }

    private fun logExceptionToServerLogger(
        throwable: Throwable,
        errorType: String,
        errorDesc: String
    ) {
        TokofoodErrorLogger.logExceptionToServerLogger(
            TokofoodErrorLogger.PAGE.MERCHANT,
            throwable,
            errorType,
            userSession.deviceId.orEmpty(),
            errorDesc
        )
    }

    private suspend fun collectCartDataFlow() {
        activityViewModel?.cartDataFlow?.collect { cartData ->
            viewModel.selectedProducts = cartData?.availableSection?.products.orEmpty()
        }
    }

    private suspend fun collectUiEventFlow() {
        activityViewModel?.cartDataValidationFlow?.collect {
            when (it.state) {
                UiEvent.EVENT_SUCCESS_LOAD_CART -> {
                    toggleMiniCartVisibility(true)
                }
                UiEvent.EVENT_FAILED_LOAD_CART -> {
                    toggleMiniCartVisibility(false)
                }
                UiEvent.EVENT_FAILED_ADD_TO_CART -> {
                    if (it.source == SOURCE) {
                        view?.showErrorToaster(it.throwable?.message.orEmpty())
                    }
                }
                UiEvent.EVENT_SUCCESS_ADD_TO_CART -> {
                    if (it.source == SOURCE) {
                        onSuccessAddCart(it.data?.getSuccessUpdateResultPair())
                    }
                }
                UiEvent.EVENT_PHONE_VERIFICATION -> {
                    if (it.source == SOURCE) {
                        val bottomSheetData = it.data as? CartTokoFoodBottomSheet
                        bottomSheetData?.run {
                            if (isShowBottomSheet) {
                                val bottomSheet = PhoneNumberVerificationBottomSheet.createInstance(bottomSheetData = this)
                                bottomSheet.setClickListener(this@MerchantPageFragment)
                                bottomSheet.show(childFragmentManager)
                            }
                        }
                    }
                }
                UiEvent.EVENT_SUCCESS_UPDATE_CART -> {
                    if (it.source == SOURCE) {
                        onSuccessUpdateCart(it.data?.getSuccessUpdateResultPair())
                    }
                }
                UiEvent.EVENT_SUCCESS_UPDATE_NOTES -> {
                    if (it.source == SOURCE) {
                        (it.data as? Pair<*, *>)?.let { pair ->
                            (pair.first as? UpdateParam)?.productList?.firstOrNull()
                                ?.let { requestParam ->
                                    (pair.second as? CartTokoFoodData)?.let { cartTokoFoodData ->
                                        cartTokoFoodData.carts.firstOrNull { data -> data.productId == requestParam.productId }
                                            ?.let { cartTokoFood ->
                                                val cardPositions =
                                                    viewModel.productMap[requestParam.productId]
                                                cardPositions?.run {
                                                    binding?.rvProductList?.post {
                                                        productListAdapter?.updateProductUiModel(
                                                            cartTokoFood = cartTokoFood,
                                                            dataSetPosition = viewModel.getDataSetPosition(this),
                                                            adapterPosition = viewModel.getAdapterPosition(this)
                                                        )
                                                    }
                                                }
                                            }
                                    }
                                    view?.let { view ->
                                        Toaster.build(
                                            view = view,
                                            text = getString(com.tokopedia.tokofood.R.string.text_note_saved_message),
                                            duration = Toaster.LENGTH_SHORT,
                                            type = Toaster.TYPE_NORMAL
                                        ).show()
                                    }
                                }
                        }
                    }
                }
                UiEvent.EVENT_SUCCESS_DELETE_PRODUCT -> {
                    if (it.source == SOURCE) {
                        (it.data as? String)?.let { cartId ->
                            val cardPositions = viewModel.productCartMap[cartId]
                            cardPositions?.run {
                                val dataSetPosition = viewModel.getDataSetPosition(this)
                                val productUiModel =
                                    productListAdapter?.getProductUiModel(dataSetPosition)
                                if (productUiModel?.isCustomizable == true) {
                                    productListAdapter?.removeCustomOrder(
                                        cartId = cartId,
                                        dataSetPosition = dataSetPosition,
                                        adapterPosition = viewModel.getAdapterPosition(this)
                                    )
                                } else {
                                    productListAdapter?.resetProductUiModel(
                                        dataSetPosition = dataSetPosition,
                                        adapterPosition = viewModel.getAdapterPosition(this)
                                    )
                                }
                                view?.let { view ->
                                    Toaster.build(
                                        view = view,
                                        text = getString(com.tokopedia.tokofood.R.string.text_product_removed),
                                        duration = Toaster.LENGTH_SHORT,
                                        type = Toaster.TYPE_NORMAL
                                    ).show()
                                }
                            }
                        }
                    }
                }
                UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY -> {
                    if (it.source == SOURCE) {
                        (it.data as? Pair<*, *>)?.let { pair ->
                            (pair.first as? UpdateParam)?.productList?.forEach { requestParam ->
                                    (pair.second as? CartTokoFoodData)?.let { cartTokoFoodData ->
                                        cartTokoFoodData.carts.firstOrNull { data -> data.productId == requestParam.productId }
                                            ?.let { cartTokoFood ->
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
                                                        binding?.rvProductList?.post {
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
                    }
                }
                UiEvent.EVENT_SUCCESS_VALIDATE_CHECKOUT -> {
                    (it.data as? CheckoutTokoFoodData)?.let { checkOutTokoFoodData ->
                        if (it.source == SOURCE){
                            val products = checkOutTokoFoodData.getProductListFromCart()
                            val purchaseAmount = checkOutTokoFoodData.summaryDetail.totalPrice
                            val merchantId = checkOutTokoFoodData.shop.shopId
                            val merchantName = checkOutTokoFoodData.shop.name
                            merchantPageAnalytics.clickCheckoutOnMiniCart(
                                products,
                                purchaseAmount,
                                merchantId,
                                merchantName
                            )
                            navigateToNewFragment(TokoFoodPurchaseFragment.createInstance())
                        }
                    }
                }
            }
        }
    }

    private suspend fun collectUpdateQuantity() {
        viewModel.updateQuantityParam.collect { updateParam ->
            activityViewModel?.updateQuantity(updateParam, SOURCE)
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
        productListAdapter = ProductListAdapter(this, this)
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
                        productListAdapter?.getProductListItems()
                            ?.filterIndexed { position, _ ->
                                position == firstVisibleItemPos
                            }
                            ?.firstOrNull { productItem -> productItem.productCategory.title.isNotBlank() }
                    productListItem?.let { productsItem ->
                        viewModel.filterNameSelected = productsItem.productCategory.title
                        setCategoryPlaceholder(viewModel.filterNameSelected)
                    }
                }
            })
        }
        (binding?.rvProductList?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
    }

    private fun setupOrderNoteBottomSheet() {
        orderNoteBottomSheet = OrderNoteBottomSheet.createInstance()
        orderNoteBottomSheet?.setClickListener(this)
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

    private fun renderMvc(mvcData: MvcData?) {
        binding?.mvcTokofoodMerchantPage?.run {
            if (mvcData == null) {
                hide()
            } else {
                show()
                renderMvcData(mvcData)
                currentPromoName = mvcData.animatedInfoList?.firstOrNull()?.title
                setMvcImpressionHandling(currentPromoName.orEmpty())
                renderMvcImageBackground()
            }
        }
    }

    private fun renderMvcData(mvcData: MvcData) {
        binding?.mvcTokofoodMerchantPage?.setData(
            mvcData = mvcData,
            shopId = merchantId,
            source = MvcSource.TOKOFOOD,
            startActivityForResultFunction = ::goToPromoPage,
            mvcTrackerImpl = TokofoodMerchantMvcTrackerImpl()
        )
    }

    private fun renderMvcImageBackground() {
        try {
            binding?.mvcTokofoodMerchantPage?.imageCouponBackground?.setImageResource(
                com.tokopedia.tokofood.R.drawable.ic_mvc_tokofood_background
            )
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    private fun setMvcImpressionHandling(promoName: String) {
        removeCurrentMvcScrollChangedListener()
        mvcImpressHolder = ImpressHolder()
        mvcImpressHolder?.let { impressHolder ->
            binding?.mvcTokofoodMerchantPage?.addAndReturnImpressionListener(impressHolder, this) {
                merchantPageAnalytics.impressPromoMvc(promoName, merchantId)
            }
        }
    }

    private fun removeCurrentMvcScrollChangedListener() {
        mvcOnScrollChangeListener?.let {
            binding?.mvcTokofoodMerchantPage?.viewTreeObserver?.removeOnScrollChangedListener(it)
        }
        mvcOnScrollChangeListener = null
        mvcImpressHolder = null
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


    private fun onSuccessAddCart(addCartData: Pair<UpdateParam, CartTokoFoodData>?) {
        addCartData?.let { (updateParam, cartTokoFoodData) ->
            updateParam.productList.firstOrNull()?.let { requestParam ->
                cartTokoFoodData.carts.firstOrNull { data -> data.productId == requestParam.productId }
                    ?.let { cartTokoFood ->
                        val cardPositions = viewModel.productMap[requestParam.productId]
                        cardPositions?.run {
                            val dataSetPosition = viewModel.getDataSetPosition(this)
                            val adapterPosition = viewModel.getAdapterPosition(this)
                            binding?.rvProductList?.post {
                                productListAdapter?.updateProductUiModel(
                                    cartTokoFood = cartTokoFood,
                                    dataSetPosition = dataSetPosition,
                                    adapterPosition = adapterPosition,
                                    customOrderDetail = viewModel.mapCartTokoFoodToCustomOrderDetail(
                                        cartTokoFood = cartTokoFood,
                                        productUiModel = productListAdapter?.getProductUiModel(dataSetPosition) ?: ProductUiModel()
                                    )
                                )
                                val productUiModel = productListAdapter?.getProductUiModel(dataSetPosition) ?: ProductUiModel()
                                val isSameCustomProductExist = productUiModel.customOrderDetails.firstOrNull { it.qty > Int.ONE } != null
                                val isMultipleCustomProductMade = productUiModel.customOrderDetails.size > Int.ONE
                                if (isSameCustomProductExist || isMultipleCustomProductMade) {
                                    showCustomOrderDetailBottomSheet(productUiModel, this)
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun onSuccessUpdateCart(updateCartData: Pair<UpdateParam, CartTokoFoodData>?) {
        updateCartData?.let { (updateParam, cartTokoFoodData) ->
            updateParam.productList.firstOrNull()?.let { requestParam ->
                cartTokoFoodData.carts.firstOrNull { data -> data.productId == requestParam.productId }
                    ?.let { cartTokoFood ->
                        val cardPositions =
                            viewModel.productMap[requestParam.productId]
                        cardPositions?.run {
                            val dataSetPosition =
                                viewModel.getDataSetPosition(this)
                            val adapterPosition =
                                viewModel.getAdapterPosition(this)
                            val productUiModel =
                                productListAdapter?.getProductUiModel(
                                    dataSetPosition
                                ) ?: ProductUiModel()
                            productListAdapter?.updateCartProductUiModel(
                                cartTokoFood = cartTokoFood,
                                dataSetPosition = dataSetPosition,
                                adapterPosition = adapterPosition,
                                customOrderDetail = viewModel.mapCartTokoFoodToCustomOrderDetail(
                                    cartTokoFood,
                                    productUiModel
                                )
                            )
                            showCustomOrderDetailBottomSheet(
                                productListAdapter?.getProductUiModel(
                                    dataSetPosition
                                ) ?: ProductUiModel(), this
                            )
                        }
                    }
            }
        }
    }

    private fun showCustomOrderDetailBottomSheet(productUiModel: ProductUiModel, cardPositions: Pair<Int, Int>) {
        hideKeyboard()
        customOrderDetailBottomSheet?.dismiss()
        val bundle = Bundle().apply {
            putInt(
                CustomOrderDetailBottomSheet.BUNDLE_KEY_PRODUCT_POSITION,
                cardPositions.first
            )
            putInt(
                CustomOrderDetailBottomSheet.BUNDLE_KEY_ADAPTER_POSITION,
                cardPositions.second
            )
            putParcelable(
                CustomOrderDetailBottomSheet.BUNDLE_KEY_PRODUCT_UI_MODEL,
                productUiModel
            )
        }
        customOrderDetailBottomSheet = CustomOrderDetailBottomSheet.createInstance(bundle)
        customOrderDetailBottomSheet?.setClickListener(this)
        customOrderDetailBottomSheet?.show(childFragmentManager)
    }

    override fun onCarouselItemClicked() {
        hideKeyboard()
        merchantInfoBottomSheet?.show(childFragmentManager)
    }

    override fun onProductCardClicked(productListItem: ProductListItem, cardPositions: Pair<Int, Int>) {
        if (viewModel.isProductDetailBottomSheetVisible) return
        val productUiModel = productListItem.productUiModel
        // update product id - card positions map
        viewModel.productMap[productUiModel.id] = cardPositions
        viewModel.productCartMap[productUiModel.cartId] = cardPositions
        // track click product card event
        merchantPageAnalytics.clickProductCard(
            getProductItemList(),
            productListItem,
            cardPositions.first,
            merchantId
        )
        hideKeyboard()
        val bottomSheet = ProductDetailBottomSheet.createInstance(productUiModel)
        bottomSheet.setOnDismissListener { viewModel.isProductDetailBottomSheetVisible = false }
        bottomSheet.setClickListener(this)
        bottomSheet.setListener(this@MerchantPageFragment)
        bottomSheet.sendTrackerInMerchantPage {
            viewModel.merchantData?.let {
                merchantPageAnalytics.clickOnOrderProductBottomSheet(
                    productListItem,
                    merchantId,
                    it.merchantProfile.name,
                    cardPositions.first
                )
            }
        }
        bottomSheet.setSelectedCardPositions(cardPositions)
        bottomSheet.setProductListItem(productListItem)
        bottomSheet.show(childFragmentManager)
        viewModel.isProductDetailBottomSheetVisible = true
    }

    override fun onAtcButtonClicked(productListItem: ProductListItem, cardPositions: Pair<Int, Int>) {
        val productUiModel = productListItem.productUiModel
        if (activityViewModel?.shopId.isNullOrBlank() || activityViewModel?.shopId == merchantId) {
            // update product id - card positions map
            viewModel.productMap[productUiModel.id] = cardPositions
            viewModel.productCartMap[productUiModel.cartId] = cardPositions
            // customized product exists navigate to custom order detail bottom sheet
            if (productUiModel.isCustomizable && productUiModel.isAtc) {
                showCustomOrderDetailBottomSheet(productUiModel, cardPositions)
            }
            // no customized product yet, navigate to customization page
            else if (productUiModel.isCustomizable) {
                navigateToOrderCustomizationPage(
                    cartId = productUiModel.cartId,
                    productListItem = productListItem,
                    cardPositions
                )
            }
            // add non customizable product to cart
            else {
                val updateParam = viewModel.mapProductUiModelToAtcRequestParam(
                    shopId = merchantId,
                    productUiModel = productUiModel
                )
                activityViewModel?.addToCart(updateParam, SOURCE)
            }
        } else {
            // product added from different merchant
            showChangeMerchantBottomSheet(productUiModel, cardPositions)
        }
        // track click atc button event
        viewModel.merchantData?.let {
            merchantPageAnalytics.clickOnAtcButton(
                productListItem, merchantId,
                it.merchantProfile.name,
                cardPositions.first
            )
        }
    }

    override fun changeMerchantConfirmAddToCart(
        updateParam: UpdateParam,
        productUiModel: ProductUiModel,
        cardPositions: Pair<Int, Int>
    ) {
        if (!productUiModel.isCustomizable) {
            activityViewModel?.deleteAllAtcAndAddProduct(updateParam, SOURCE)
        } else {
            val productListItem =
                getProductItemList().find { it.productUiModel.id == productUiModel.id }
            productListItem?.let {
                navigateToOrderCustomizationPage(
                    it.productUiModel.cartId,
                    it,
                    cardPositions,
                    true
                )
            }
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

    override fun onDeleteButtonClicked(
        cartId: String,
        productId: String,
        cardPositions: Pair<Int, Int>
    ) {
        viewModel.productMap[productId] = cardPositions
        viewModel.productCartMap[cartId] = cardPositions
        activityViewModel?.deleteProduct(
            cartId = cartId,
            source = SOURCE,
        )
    }

    override fun onFinishCategoryFilter(categoryFilterList: List<CategoryFilterListUiModel>) {
        val categoryFilter = categoryFilterList.find { it.isSelected }
        categoryFilter?.let {
            viewModel.filterNameSelected = it.categoryUiModel.title
            binding?.tvCategoryPlaceholder?.text = viewModel.filterNameSelected
            val productItem = productListAdapter
                ?.getProductListItems()
                ?.find { productList -> productList.productCategory.title == viewModel.filterNameSelected }
            val position = productListAdapter?.getProductListItems()?.indexOf(productItem)
            if (position != null && position != RecyclerView.NO_POSITION) {
                scrollToCategorySection(position)
            }
        }
    }

    override fun onUpdateProductQty(cartId: String, quantity: Int, cardPositions: Pair<Int, Int>) {
        viewModel.productMap[productId] = cardPositions
        val dataSetPosition = viewModel.getDataSetPosition(cardPositions)
        val productUiModel = productListAdapter?.getProductUiModel(dataSetPosition)
        productUiModel?.run {
            orderQty = quantity
            viewModel.updateQuantity(cartId, quantity)
        }
    }

    override fun onIncreaseQtyButtonClicked(
        cartId: String,
        quantity: Int,
        cardPositions: Pair<Int, Int>
    ) {
        viewModel.productCartMap[cartId] = cardPositions
        val dataSetPosition = viewModel.getDataSetPosition(cardPositions)
        val productUiModel = productListAdapter?.getProductUiModel(dataSetPosition)
        productUiModel?.run {
            orderQty = quantity
            viewModel.updateQuantity(cartId, quantity)
        }
    }

    override fun onNavigateToOrderCustomizationPage(cartId: String, productUiModel: ProductUiModel, cardPositions: Pair<Int, Int>) {
        if (activityViewModel?.shopId.isNullOrBlank() || activityViewModel?.shopId == merchantId) {
            val productListItem = getProductItemList().find { it.productUiModel.id == productUiModel.id }
            if (productListItem != null) {
                navigateToOrderCustomizationPage(
                    cartId = cartId,
                    productListItem = productListItem,
                    cardPositions
                )
            }
        } else {
            showChangeMerchantBottomSheet(productUiModel, cardPositions)
        }
    }

    override fun onDecreaseQtyButtonClicked(
        cartId: String,
        quantity: Int,
        cardPositions: Pair<Int, Int>
    ) {
        viewModel.productCartMap[cartId] = cardPositions
        val dataSetPosition = viewModel.getDataSetPosition(cardPositions)
        val productUiModel = productListAdapter?.getProductUiModel(dataSetPosition)
        productUiModel?.run {
            orderQty = quantity
            viewModel.updateQuantity(cartId, quantity)
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
        shareFoodItem(viewModel.merchantData?.merchantProfile, productUiModel)
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


    override fun onDeleteCustomOrderButtonClicked(cartId: String) {
        activityViewModel?.deleteProduct(
            cartId = cartId,
            source = SOURCE
        )
    }

    override fun onUpdateCustomOrderQtyButtonClicked(
        productId: String,
        quantity: Int,
        customOrderDetail: CustomOrderDetail
    ) {
        customOrderDetail.qty = quantity
        viewModel.updateQuantity(customOrderDetail.cartId, customOrderDetail.qty)
    }

    override fun onButtonCtaClickListener(appLink: String) {
        var applicationLink = ApplinkConstInternalUserPlatform.ADD_PHONE
        if (appLink.isNotEmpty()) applicationLink = appLink
        context?.run {
            TokofoodRouteManager.routePrioritizeInternal(this, applicationLink)
        }
    }

    override fun onScrollChangedListenerAdded(onScrollChangedListener: ViewTreeObserver.OnScrollChangedListener) {
        onScrollChangedListenerList.add(onScrollChangedListener)
    }

    private fun showChangeMerchantBottomSheet(
        productUiModel: ProductUiModel,
        cardPositions: Pair<Int, Int>
    ) {
        val updateParam = viewModel.mapProductUiModelToAtcRequestParam(
            shopId = merchantId,
            productUiModel = productUiModel
        )

        viewModel.productMap[productUiModel.id] = cardPositions

        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }

        cacheManager?.apply {
            put(
                ChangeMerchantBottomSheet.KEY_UPDATE_PARAM,
                updateParam
            )
        }

        val bundle = Bundle().apply {
            putString(
                ChangeMerchantBottomSheet.KEY_CACHE_MANAGER_ID,
                cacheManager?.id.orEmpty()
            )
            putInt(
                ChangeMerchantBottomSheet.KEY_PRODUCT_POSITION,
                cardPositions.first
            )
            putInt(
                ChangeMerchantBottomSheet.KEY_ADAPTER_POSITION,
                cardPositions.second
            )
            putParcelable(
                ChangeMerchantBottomSheet.KEY_PRODUCT_UI_MODEL,
                productUiModel
            )
        }


        val bottomSheet = ChangeMerchantBottomSheet.newInstance(bundle)

        bottomSheet.setChangeMerchantListener(this)
        bottomSheet.show(childFragmentManager)
    }

    private fun navigateToOrderCustomizationPage(
        cartId: String,
        productListItem: ProductListItem,
        cardPositions: Pair<Int, Int>,
        isChangeMerchant: Boolean = false
    ) {
        viewModel.productListItems = productListAdapter?.getProductListItems() ?: mutableListOf()

        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        val variantTracker = VariantWrapperUiModel(
            productListItem,
            merchantId,
            viewModel.merchantData?.merchantProfile?.name.orEmpty(),
            cardPositions.first
        )

        cacheManager?.put(
            OrderCustomizationFragment.BUNDLE_KEY_VARIANT_TRACKER,
            variantTracker
        )

        val orderCustomizationFragment = OrderCustomizationFragment.createInstance(
            productUiModel = productListItem.productUiModel,
            cartId = cartId,
            merchantId = merchantId,
            source = SOURCE,
            cacheManagerId = cacheManager?.id.orEmpty(),
            isChangeMerchant = isChangeMerchant
        )

        navigateToNewFragment(orderCustomizationFragment)
    }

    private fun getProductItemList() = productListAdapter?.getProductListItems().orEmpty()

    private fun goToLoginPage() {
        context?.run {
            val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN)
        }
    }

    private fun validateAddressData() {
        context?.run {
            ChooseAddressUtils.getLocalizingAddressData(this).let { addressData ->
                when {
                    isNoAddress(addressData) -> {
                        if (isAddressManuallyUpdated()) {
                            viewModel.isAddressManuallyUpdated = false
                            navigateToNewFragment(
                                ManageLocationFragment.createInstance(
                                    negativeCaseId = EMPTY_STATE_NO_ADDRESS,
                                    merchantId = merchantId
                                )
                            )
                        } else {
                            setAddressManually()
                        }
                    }
                    isNoPinPoin(addressData) -> {
                        if (isAddressManuallyUpdated()) {
                            viewModel.isAddressManuallyUpdated = false
                            navigateToNewFragment(
                                ManageLocationFragment.createInstance(
                                    negativeCaseId = EMPTY_STATE_NO_PIN_POINT,
                                    merchantId = merchantId
                                )
                            )
                        } else {
                            setAddressManually()
                        }
                    }

                    else -> {
                        fetchMerchantData()
                    }
                }
            }
        }
    }

    private fun onResultFromLogin(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) {
            viewModel.isAddressManuallyUpdated = false
            validateAddressData()
        } else {
            activity?.finish()
        }
    }

    private fun toggleMiniCartVisibility(shouldShow: Boolean) {
        binding?.miniCartWidget?.showWithCondition(shouldShow)
        setRecyclerViewPaddingBottom(shouldShow)
    }

    private fun isAddressManuallyUpdated(): Boolean = viewModel.isAddressManuallyUpdated

    private fun setAddressManually() {
        if (userSession.isLoggedIn) viewModel.getChooseAddress(SOURCE_ADDESS)
    }

    private fun isNoAddress(localCacheModel: LocalCacheModel): Boolean {
        return (localCacheModel.address_id.isNullOrEmpty() || localCacheModel.address_id == EMPTY_ADDRESS_ID)
    }

    private fun isNoPinPoin(localCacheModel: LocalCacheModel): Boolean {
        return (!localCacheModel.address_id.isNullOrEmpty() || localCacheModel.address_id != EMPTY_ADDRESS_ID)
                && (localCacheModel.lat.isNullOrEmpty() || localCacheModel.long.isNullOrEmpty() ||
                localCacheModel.lat.equals(EMPTY_COORDINATE) || localCacheModel.long.equals(
            EMPTY_COORDINATE
        ))
    }

    private fun setupChooseAddress(data: GetStateChosenAddressResponse) {
        data.let { chooseAddressData ->
            if (chooseAddressData.data.addressId.isMoreThanZero()
                && chooseAddressData.data.latitude.isNotEmpty()
                && chooseAddressData.data.longitude.isNotEmpty()
                && chooseAddressData.data.latitude != EMPTY_COORDINATE
                && chooseAddressData.data.longitude != EMPTY_COORDINATE
            ) {
                ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                    context = requireContext(),
                    addressId = chooseAddressData.data.addressId.toString(),
                    cityId = chooseAddressData.data.cityId.toString(),
                    districtId = chooseAddressData.data.districtId.toString(),
                    lat = chooseAddressData.data.latitude,
                    long = chooseAddressData.data.longitude,
                    label = String.format(
                        "%s %s",
                        chooseAddressData.data.addressName,
                        chooseAddressData.data.receiverName
                    ),
                    postalCode = chooseAddressData.data.postalCode,
                    warehouseId = chooseAddressData.tokonow.warehouseId.toString(),
                    shopId = chooseAddressData.tokonow.shopId.toString(),
                    warehouses = TokonowWarehouseMapper.mapWarehousesResponseToLocal(
                        chooseAddressData.tokonow.warehouses
                    ),
                    serviceType = chooseAddressData.tokonow.serviceType,
                    lastUpdate = chooseAddressData.tokonow.tokonowLastUpdate
                )
            }
        }
        validateAddressData()
    }

    private fun setRecyclerViewPaddingBottom(isMiniCartShown: Boolean) {
        val bottomPadding =
            if (isMiniCartShown) {
                toolbarHeight
            } else {
                Int.ZERO
            }
        binding?.rvProductList?.run {
            setPadding(paddingLeft, paddingTop, paddingRight, bottomPadding)
        }
    }

    private fun hideKeyboard() {
        try {
            KeyboardHandler.hideSoftKeyboard(activity)
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    private fun removeListeners() {
        onOffsetChangedListener?.let {
            binding?.toolbarParent?.removeOnOffsetChangedListener(it)
        }
        onOffsetChangedListener = null
        onScrollChangedListenerList.forEach {
            view?.viewTreeObserver?.removeOnScrollChangedListener(it)
        }
    }

    private fun goToPromoPage() {
        merchantPageAnalytics.clickPromoMvc(currentPromoName.orEmpty(), merchantId)
        navigateToNewFragment(
            TokoFoodPromoFragment.createInstance(
                SOURCE,
                merchantId
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_LOGIN -> onResultFromLogin(resultCode)
        }
    }

    companion object {

        private const val SHARE = "share"
        private const val SOURCE_ADDESS = "tokofood"
        private const val EMPTY_COORDINATE = "0.0"
        private const val EMPTY_ADDRESS_ID = "0"
        private const val DELAY_TIME_HUNDRED_MILLIS = 100L

        private const val REQUEST_CODE_LOGIN = 123
        private const val SOURCE = "merchant_page"

        fun createInstance(): MerchantPageFragment {
            return MerchantPageFragment()
        }
    }
}
