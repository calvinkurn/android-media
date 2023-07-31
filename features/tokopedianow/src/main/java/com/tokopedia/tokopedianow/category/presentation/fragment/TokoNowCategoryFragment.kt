package com.tokopedia.tokopedianow.category.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic
import com.tokopedia.tokopedianow.category.di.component.CategoryComponent
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryAdapter
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryNavigationCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryProductCardAdsCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryProductRecommendationCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryShowcaseHeaderCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryShowcaseItemCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryTitleCallback
import com.tokopedia.tokopedianow.category.presentation.callback.ProductCardCompactCallback
import com.tokopedia.tokopedianow.category.presentation.callback.ProductCardCompactSimilarProductTrackerCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowCategoryMenuCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowChooseAddressWidgetCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowViewCallback
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAtcTrackerModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType.CATEGORY_SHOWCASE
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryViewModel
import com.tokopedia.tokopedianow.common.constant.RequestCode
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
import com.tokopedia.tokopedianow.common.model.ShareTokonow
import com.tokopedia.tokopedianow.common.util.GlobalErrorUtil
import com.tokopedia.tokopedianow.common.util.StringUtil.getOrDefaultZeroString
import com.tokopedia.tokopedianow.common.util.TokoNowUniversalShareUtil
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
import com.tokopedia.tokopedianow.common.view.NoAddressEmptyStateView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowCategoryBaseBinding
import com.tokopedia.tokopedianow.similarproduct.presentation.activity.TokoNowSimilarProductBottomSheetActivity
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowCategoryFragment :
    BaseDaggerFragment(),
    ScreenShotListener,
    ShareBottomsheetListener,
    PermissionListener,
    MiniCartWidgetListener,
    NoAddressEmptyStateView.ActionListener {
    companion object {
        private const val SCROLL_DOWN_DIRECTION = 1
        private const val START_SWIPE_PROGRESS_POSITION = 120
        private const val END_SWIPE_PROGRESS_POSITION = 200

        private const val PAGE_SHARE_NAME = "Tokonow"
        private const val SHARE = "share"
        private const val PAGE_NAME = "TokoNow Category"
        private const val TOKONOW_DIRECTORY = "tokonow_directory"
        private const val NO_ADDRESS_EVENT_TRACKER = "tokonow - category page"
        private const val THUMBNAIL_AND_OG_IMAGE_SHARE_URL = TokopediaImageUrl.THUMBNAIL_AND_OG_IMAGE_SHARE_URL

        fun newInstance(): TokoNowCategoryFragment {
            return TokoNowCategoryFragment()
        }
    }

    /**
     * -- lateinit variable section --
     */

    @Inject
    lateinit var viewModel: TokoNowCategoryViewModel

    @Inject
    lateinit var productRecommendationViewModel: TokoNowProductRecommendationViewModel

    @Inject
    lateinit var analytic: CategoryAnalytic

    /**
     * -- private lazy variable section --
     */

    private var binding by autoClearedNullable<FragmentTokopedianowCategoryBaseBinding>()

    private val adapter: CategoryAdapter by lazy {
        CategoryAdapter(
            typeFactory = CategoryAdapterTypeFactory(
                categoryTitleListener = createTitleCallback(),
                categoryNavigationListener = createCategoryNavigationCallback(),
                categoryShowcaseItemListener = createCategoryShowcaseItemCallback(),
                categoryShowcaseHeaderListener = createCategoryShowcaseHeaderCallback(),
                tokoNowView = createTokoNowViewCallback(),
                tokoNowChooseAddressWidgetListener = createTokoNowChooseAddressWidgetCallback(),
                tokoNowCategoryMenuListener = createTokoNowCategoryMenuCallback(),
                tokoNowProductRecommendationListener = createProductRecommendationCallback(),
                productCardCompactListener = createProductCardCompactCallback(),
                productCardCompactSimilarProductTrackerListener = createProductCardCompactSimilarProductTrackerCallback(),
                productAdsCarouselListener = createProductCardAdsCallback(),
                recycledViewPool = recycledViewPool,
                lifecycleOwner = viewLifecycleOwner
            ),
            differ = CategoryDiffer()
        )
    }

    /**
     * -- private getter variable section --
     */

    private val recycledViewPool
        get() = RecyclerView.RecycledViewPool()

    private val onScrollListener: RecyclerView.OnScrollListener
        get() = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val isAtTheBottomOfThePage = !recyclerView.canScrollVertically(SCROLL_DOWN_DIRECTION)
                viewModel.loadMore(isAtTheBottomOfThePage)
            }
        }

    private val navToolbarHeight: Int
        get() {
            val defaultHeight = context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_default_toolbar_height).orZero()
            return if (binding?.navToolbar?.height.isZero()) defaultHeight else binding?.navToolbar?.height ?: defaultHeight
        }

    private val userId: String
        get() = viewModel.getUserId()
    private val categoryIdL1: String
        get() = viewModel.categoryIdL1
    private val categoryIdL2: String
        get() = String.EMPTY
    private val categoryIdL3: String
        get() = String.EMPTY
    private val shopId: String
        get() = viewModel.getShopId().toString()
    private val currentCategoryId: String
        get() = viewModel.categoryIdL1
    private val addressData: LocalCacheModel
        get() = viewModel.getAddressData()

    /**
     * -- private mutable variable section --
     */

    private var shareTokonow: ShareTokonow? = null
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var screenshotDetector: ScreenshotDetector? = null

    /**
     * -- override function section --
     */

    override fun initInjector() = getComponent(CategoryComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shareTokonow = createShareTokonow()
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        viewModel.getMiniCart()
        productRecommendationViewModel.updateMiniCartSimplified(miniCartSimplifiedData)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupScreenshotDetector()
        setupUi()
        setupObserver()

        binding?.navToolbar?.post {
            viewModel.onViewCreated(navToolbarHeight)
        }
    }

    override fun getScreenName(): String = String.EMPTY

    override fun onResume() {
        super.onResume()
        if (isChooseAddressWidgetDataUpdated()) {
            refreshLayout()
        } else {
            getMiniCart()
        }
        updateToolbarNotification()
        screenshotDetector?.start()
    }

    override fun onStop() {
        SharingUtil.clearState(screenshotDetector)
        super.onStop()
    }

    override fun onDestroy() {
        SharingUtil.clearState(screenshotDetector)
        recycledViewPool.clear()
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokopedianowCategoryBaseBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun screenShotTaken(path: String) {
        updateShareCategoryData(
            isScreenShot = true,
            thumbNailTitle = context?.resources?.getString(R.string.tokopedianow_share_thumbnail_title_ss).orEmpty()
        )
        showUniversalShareBottomSheet(shareTokonow, path)
    }

    override fun permissionAction(action: String, label: String) {
        analytic.categorySharingExperienceAnalytic.trackClickAccessMediaAndFiles(
            accessText = label,
            categoryIdLvl1 = categoryIdL1,
            categoryIdLvl2 = categoryIdL2,
            categoryIdLvl3 = categoryIdL3
        )
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        if (shareTokonow?.isScreenShot == true) {
            analytic.categorySharingExperienceAnalytic.trackClickChannelShareBottomSheetScreenshot(
                channel = shareModel.channel.orEmpty(),
                categoryIdLvl1 = categoryIdL1,
                categoryIdLvl2 = categoryIdL2,
                categoryIdLvl3 = categoryIdL3
            )
        } else {
            analytic.categorySharingExperienceAnalytic.trackClickChannelShareBottomSheet(
                channel = shareModel.channel.orEmpty(),
                categoryIdLvl1 = categoryIdL1,
                categoryIdLvl2 = categoryIdL2,
                categoryIdLvl3 = categoryIdL3
            )
        }

        TokoNowUniversalShareUtil.shareOptionRequest(
            shareModel = shareModel,
            shareTokoNowData = shareTokonow,
            activity = activity,
            view = view,
            onSuccess = {
                universalShareBottomSheet?.dismiss()
            }
        )
    }

    override fun onCloseOptionClicked() {
        if (shareTokonow?.isScreenShot == true) {
            analytic.categorySharingExperienceAnalytic.trackClickCloseScreenShotShareBottomSheet(
                categoryIdLvl1 = categoryIdL1,
                categoryIdLvl2 = categoryIdL2,
                categoryIdLvl3 = categoryIdL3
            )
        } else {
            analytic.categorySharingExperienceAnalytic.trackClickCloseShareBottomSheet(
                categoryIdLvl1 = categoryIdL1,
                categoryIdLvl2 = categoryIdL2,
                categoryIdLvl3 = categoryIdL3
            )
        }
    }

    override fun onPrimaryBtnClicked() {
        showBottomSheetChooseAddress()
    }

    override fun onSecondaryBtnClicked() {
        RouteManager.route(context, ApplinkConst.HOME)
        activity?.finish()
    }

    override fun onGetNoAddressEmptyStateEventCategoryTracker(): String = NO_ADDRESS_EVENT_TRACKER

    /**
     * -- private function section --
     */

    private fun FragmentTokopedianowCategoryBaseBinding.showMainLayout() {
        mainLayout.show()
        categoryShimmering.root.hide()
        globalError.hide()
        oosLayout.hide()
    }

    private fun FragmentTokopedianowCategoryBaseBinding.showOosLayout() {
        mainLayout.hide()
        globalError.hide()
        categoryShimmering.root.hide()
        oosLayout.show()
        oosLayout.actionListener = this@TokoNowCategoryFragment
    }

    private fun FragmentTokopedianowCategoryBaseBinding.showErrorLayout(
        throwable: Throwable
    ) {
        mainLayout.hide()
        categoryShimmering.root.hide()
        globalError.show()
        oosLayout.hide()

        if (throwable is MessageErrorException) {
            val errorCode = throwable.errorCode
            val errorType = GlobalErrorUtil.getErrorType(
                throwable = throwable,
                errorCode = errorCode
            )
            globalError.setType(errorType)

            when (errorCode) {
                GlobalErrorUtil.ERROR_PAGE_NOT_FOUND -> setupGlobalErrorPageNotFound()
                GlobalErrorUtil.ERROR_SERVER -> setupActionGlobalErrorClickListener()
                GlobalErrorUtil.ERROR_MAINTENANCE -> setupGlobalErrorMaintenance()
                GlobalErrorUtil.ERROR_PAGE_FULL -> setupActionGlobalErrorClickListener()
                else -> setupActionGlobalErrorClickListener()
            }
        } else {
            val errorType = GlobalErrorUtil.getErrorType(
                throwable = throwable,
                errorCode = String.EMPTY
            )
            globalError.setType(errorType)

            if (errorType == GlobalError.NO_CONNECTION) {
                setupGlobalErrorNoConnection()
            } else {
                setupActionGlobalErrorClickListener()
            }
        }
    }

    private fun FragmentTokopedianowCategoryBaseBinding.showShimmeringLayout() {
        mainLayout.hide()
        categoryShimmering.root.show()
        globalError.hide()
        oosLayout.hide()
    }

    private fun FragmentTokopedianowCategoryBaseBinding.setupGlobalErrorPageNotFound() {
        globalError.apply {
            errorAction.text = getString(R.string.tokopedianow_common_error_state_button_back_to_tokonow_home_page)
            errorSecondaryAction.show()
            errorSecondaryAction.text = getString(R.string.tokopedianow_common_empty_state_button_return)
            setActionClickListener {
                RouteManager.route(context, ApplinkConstInternalTokopediaNow.HOME)
                activity?.finish()
            }
            setSecondaryActionClickListener {
                RouteManager.route(context, ApplinkConst.HOME)
                activity?.finish()
            }
        }
    }

    private fun FragmentTokopedianowCategoryBaseBinding.setupActionGlobalErrorClickListener() {
        globalError.setActionClickListener {
            refreshLayout()
        }
    }

    private fun FragmentTokopedianowCategoryBaseBinding.setupGlobalErrorMaintenance() {
        globalError.apply {
            errorAction.text = getString(R.string.tokopedianow_common_error_state_button_back_to_tokonow_home_page)
            setActionClickListener {
                RouteManager.route(context, ApplinkConstInternalTokopediaNow.HOME)
                activity?.finish()
            }
        }
    }

    private fun FragmentTokopedianowCategoryBaseBinding.setupGlobalErrorNoConnection() {
        globalError.apply {
            errorSecondaryAction.show()
            errorSecondaryAction.text = getString(R.string.tokopedianow_common_empty_state_button_return)
            setActionClickListener {
                refreshLayout()
            }
            setSecondaryActionClickListener {
                RouteManager.route(context, ApplinkConst.HOME)
                activity?.finish()
            }
        }
    }

    private fun FragmentTokopedianowCategoryBaseBinding.setupNavigationToolbar() {
        navToolbar.apply {
            bringToFront()
            setToolbarPageName(PAGE_NAME)
            setIcon(
                IconBuilder(builderFlags = IconBuilderFlag(pageSource = NavSource.TOKONOW))
                    .addShare()
                    .addCart()
                    .addNavGlobal()
            )
            setupSearchbar()
            setupNavigationToolbarInteraction()
        }
    }

    private fun FragmentTokopedianowCategoryBaseBinding.setupRecyclerView() {
        rvCategory.adapter = this@TokoNowCategoryFragment.adapter
        rvCategory.layoutManager = LinearLayoutManager(context)
        rvCategory.addOnScrollListener(onScrollListener)
        rvCategory.addOnScrollListener(createNavRecyclerViewOnScrollListener(navToolbar))
        rvCategory.animation = null
        rvCategory.setRecycledViewPool(recycledViewPool)
    }

    private fun FragmentTokopedianowCategoryBaseBinding.setupRefreshLayout() {
        strRefreshLayout.setProgressViewOffset(
            false,
            START_SWIPE_PROGRESS_POSITION,
            END_SWIPE_PROGRESS_POSITION
        )
        strRefreshLayout.setOnRefreshListener {
            strRefreshLayout.isRefreshing = false
            refreshLayout()
        }
    }

    private fun NavToolbar.setupNavigationToolbarInteraction() {
        activity?.let { setupToolbarWithStatusBar(activity = it) }
        viewLifecycleOwner.lifecycle.addObserver(this)
    }

    private fun NavToolbar.setupSearchbar() = setupSearchbar(
        hints = getNavToolbarHint(),
        searchbarClickCallback = {
            val params = URLParser(ApplinkConstInternalDiscovery.AUTOCOMPLETE).paramKeyValueMap

            params[SearchApiConst.BASE_SRP_APPLINK] = ApplinkConstInternalTokopediaNow.SEARCH
            params[SearchApiConst.PLACEHOLDER] = context?.resources?.getString(R.string.tokopedianow_search_bar_hint).orEmpty()
            params[SearchApiConst.PREVIOUS_KEYWORD] = String.EMPTY
            params[SearchApiConst.NAVSOURCE] = TOKONOW_DIRECTORY

            val finalAppLink = ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?" + UrlParamUtils.generateUrlParamString(params)

            RouteManager.route(context, finalAppLink)

            analytic.sendClickSearchBarEvent(
                categoryIdL1 = categoryIdL1,
                warehouseId = viewModel.getWarehouseId()
            )
        },
        disableDefaultGtmTracker = true
    )

    private fun IconBuilder.addShare() = addIcon(
        iconId = IconList.ID_SHARE,
        disableRouteManager = false,
        disableDefaultGtmTracker = false
    ) {
        updateShareCategoryData(
            isScreenShot = false,
            thumbNailTitle = context?.resources?.getString(R.string.tokopedianow_share_thumbnail_title).orEmpty()
        )

        analytic.categorySharingExperienceAnalytic.trackClickShareButtonTopNav(
            categoryIdLvl1 = categoryIdL1,
            categoryIdLvl2 = categoryIdL2,
            categoryIdLvl3 = categoryIdL3,
            currentCategoryId = currentCategoryId
        )

        shareClicked(shareTokonow)
    }

    private fun IconBuilder.addNavGlobal(): IconBuilder = addIcon(
        iconId = IconList.ID_NAV_GLOBAL,
        disableRouteManager = false,
        disableDefaultGtmTracker = false
    ) { /* nothing to do */ }

    private fun IconBuilder.addCart(): IconBuilder = addIcon(
        iconId = IconList.ID_CART,
        disableRouteManager = false,
        disableDefaultGtmTracker = true
    ) {
        analytic.sendClickCartButtonEvent(
            categoryIdL1 = categoryIdL1,
            warehouseId = viewModel.getWarehouseId()
        )
    }

    private fun createShareTokonow(): ShareTokonow = ShareTokonow(
        thumbNailImage = THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
        ogImageUrl = THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
        linkerType = LinkerData.NOW_TYPE
    )

    private fun shareClicked(shareCategoryTokonow: ShareTokonow?) {
        if (SharingUtil.isCustomSharingEnabled(context)) {
            showUniversalShareBottomSheet(
                shareCategoryTokonow = shareCategoryTokonow
            )
        } else {
            LinkerManager.getInstance().executeShareRequest(
                TokoNowUniversalShareUtil.shareRequest(
                    context = context,
                    shareTokonow = shareCategoryTokonow
                )
            )
        }
    }

    private fun showUniversalShareBottomSheet(shareCategoryTokonow: ShareTokonow?, path: String? = null) {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            setFeatureFlagRemoteConfigKey()
            path?.let {
                setImageOnlySharingOption(true)
                setScreenShotImagePath(path)
            }
            init(this@TokoNowCategoryFragment)
            setUtmCampaignData(
                pageName = PAGE_SHARE_NAME,
                userId = userId.getOrDefaultZeroString(),
                pageIdConstituents = shareCategoryTokonow?.pageIdConstituents.orEmpty(),
                feature = SHARE
            )
            setMetaData(
                tnTitle = shareCategoryTokonow?.thumbNailTitle.orEmpty(),
                tnImage = shareCategoryTokonow?.thumbNailImage.orEmpty()
            )
            // set the Image Url of the Image that represents page
            setOgImageUrl(imgUrl = shareCategoryTokonow?.ogImageUrl.orEmpty())
        }

        if (shareCategoryTokonow?.isScreenShot == true) {
            analytic.categorySharingExperienceAnalytic.trackImpressChannelShareBottomSheetScreenShot(
                categoryIdLvl1 = categoryIdL1,
                categoryIdLvl2 = categoryIdL2,
                categoryIdLvl3 = categoryIdL3
            )
        } else {
            analytic.categorySharingExperienceAnalytic.trackImpressChannelShareBottomSheet(
                categoryIdLvl1 = categoryIdL1,
                categoryIdLvl2 = categoryIdL2,
                categoryIdLvl3 = categoryIdL3
            )
        }
        universalShareBottomSheet?.show(childFragmentManager, this, screenshotDetector)
    }

    private fun updateShareCategoryData(
        isScreenShot: Boolean,
        thumbNailTitle: String
    ) {
        shareTokonow?.isScreenShot = isScreenShot
        shareTokonow?.thumbNailTitle = thumbNailTitle
    }

    private fun getNavToolbarHint(): List<HintData> {
        val hint = getString(R.string.tokopedianow_search_bar_hint)
        return listOf(HintData(hint, hint))
    }

    private fun showBottomSheetChooseAddress() {
        val chooseAddressBottomSheet = ChooseAddressBottomSheet()
        chooseAddressBottomSheet.setListener(object : ChooseAddressBottomSheet.ChooseAddressBottomSheetListener {
            override fun getLocalizingAddressHostSourceBottomSheet(): String = SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH

            override fun onAddressDataChanged() {
                refreshLayout()
            }

            override fun onLocalizingAddressServerDown() { /* to do : nothing */ }

            override fun onLocalizingAddressLoginSuccessBottomSheet() { /* to do : nothing */ }

            override fun onDismissChooseAddressBottomSheet() { /* to do : nothing */ }

            override fun isFromTokonowPage(): Boolean {
                return true
            }
        })
        chooseAddressBottomSheet.show(
            manager = childFragmentManager,
            tag = TokoNowEmptyStateOocViewHolder.SHIPPING_CHOOSE_ADDRESS_TAG
        )
    }

    private fun createNavRecyclerViewOnScrollListener(
        navToolbar: NavToolbar
    ): RecyclerView.OnScrollListener {
        val transitionRange = context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_searchbar_transition_range).orZero()
        return NavRecyclerViewScrollListener(
            navToolbar = navToolbar,
            startTransitionPixel = navToolbarHeight - transitionRange - transitionRange,
            toolbarTransitionRangePixel = transitionRange,
            navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                override fun onAlphaChanged(offsetAlpha: Float) { /* nothing to do */ }

                override fun onSwitchToLightToolbar() { /* nothing to do */ }

                override fun onYposChanged(yOffset: Int) { /* nothing to do */ }

                override fun onSwitchToDarkToolbar() {
                    navToolbar.hideShadow()
                }
            },
            fixedIconColor = NavToolbar.Companion.Theme.TOOLBAR_LIGHT_TYPE
        )
    }

    private fun setupUi() {
        binding?.apply {
            setupNavigationToolbar()
            setupRecyclerView()
            setupRefreshLayout()
            showShimmeringLayout()
        }
    }

    private fun setupScreenshotDetector() {
        context?.let {
            screenshotDetector = SharingUtil.createAndStartScreenShotDetector(
                context = it,
                screenShotListener = this,
                fragment = this,
                permissionListener = this
            )
        }
    }

    private fun getMiniCartHeight(): Int {
        val space16 = context?.resources?.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_16
        )?.toInt().orZero()
        return binding?.miniCartWidget?.height.orZero() - space16
    }

    private fun showMiniCartBottomSheet() {
        binding?.miniCartWidget?.showMiniCartListBottomSheet(
            fragment = this
        )
    }

    private fun isChooseAddressWidgetDataUpdated(): Boolean {
        context?.apply {
            return ChooseAddressUtils.isLocalizingAddressHasUpdated(
                context = this,
                localizingAddressStateData = addressData
            )
        }
        return false
    }

    private fun refreshLayout() = viewModel.refreshLayout()

    private fun getMiniCart() = viewModel.getMiniCart()

    private fun setupObserver() {
        observeCategoryHeader()
        observeCategoryPage()
        observeScrollNotNeeded()
        observeMiniCart()
        observeAddToCart()
        observeUpdateCartItem()
        observeRemoveCartItem()
        observeAtcDataTracker()
        observeProductRecommendationAddToCart()
        observeProductRecommendationRemoveCartItem()
        observeProductRecommendationUpdateCartItem()
        observeProductRecommendationToolbarNotification()
        observeProductRecommendationAtcDataTracker()
        observeToolbarNotification()
        observeRefreshState()
        observeOosState()
        observeOpenScreenTracker()
        observeOpenLoginPage()
    }

    private fun observeCategoryHeader() {
        viewModel.categoryFirstPage.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    adapter.submitList(result.data)

                    viewModel.getFirstPage()

                    binding?.showMainLayout()
                }
                is Fail -> binding?.showErrorLayout(
                    throwable = result.throwable
                )
            }
        }
    }

    private fun observeCategoryPage() {
        viewModel.categoryPage.observe(viewLifecycleOwner) { result ->
            adapter.submitList(result)
        }
    }

    private fun observeScrollNotNeeded() {
        viewModel.scrollNotNeeded.observe(viewLifecycleOwner) {
            binding?.rvCategory?.removeOnScrollListener(onScrollListener)
        }
    }

    private fun observeMiniCart() {
        viewModel.miniCart.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    val data = result.data
                    showMiniCart(data)
                    productRecommendationViewModel.updateMiniCartSimplified(result.data)
                }
                is Fail -> {
                    hideMiniCart()
                }
            }
        }
    }

    private fun observeAddToCart() {
        viewModel.addItemToCart.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> onSuccessAddItemToCart(
                    data = result.data
                )
                is Fail -> showErrorToaster(
                    error = result
                )
            }
        }
    }

    private fun observeUpdateCartItem() {
        viewModel.updateCartItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> onSuccessUpdateCartItem()
                is Fail -> showErrorToaster(
                    error = result
                )
            }
        }
    }

    private fun observeRemoveCartItem() {
        viewModel.removeCartItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> onSuccessRemoveCartItem(
                    data = result.data
                )
                is Fail -> showErrorToaster(
                    error = result
                )
            }
        }
    }

    private fun observeToolbarNotification() {
        viewModel.updateToolbarNotification.observe(viewLifecycleOwner) { needToUpdate ->
            if (needToUpdate) updateToolbarNotification()
        }
    }

    private fun observeProductRecommendationAddToCart() {
        productRecommendationViewModel.addItemToCart.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> onSuccessAddItemToCart(
                    data = result.data
                )
                is Fail -> showErrorToaster(
                    error = result
                )
            }
        }
    }

    private fun observeProductRecommendationUpdateCartItem() {
        productRecommendationViewModel.updateCartItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    onSuccessUpdateCartItem()
                }
                is Fail -> {
                    showErrorToaster(
                        error = result
                    )
                }
            }
        }
    }

    private fun observeProductRecommendationRemoveCartItem() {
        productRecommendationViewModel.removeCartItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> onSuccessRemoveCartItem(
                    data = result.data
                )
                is Fail -> showErrorToaster(
                    error = result
                )
            }
        }
    }

    private fun observeProductRecommendationToolbarNotification() {
        productRecommendationViewModel.updateToolbarNotification.observe(viewLifecycleOwner) { needToUpdate ->
            if (needToUpdate) updateToolbarNotification()
        }
    }

    private fun observeRefreshState() {
        viewModel.refreshState.observe(viewLifecycleOwner) {
            binding?.apply {
                showShimmeringLayout()
                rvCategory.removeOnScrollListener(onScrollListener)
                rvCategory.addOnScrollListener(onScrollListener)
            }
            viewModel.loadCategoryPage(navToolbarHeight)
        }
    }

    private fun observeOosState() {
        viewModel.oosState.observe(viewLifecycleOwner) {
            binding?.showOosLayout()
            analytic.sendOocOpenScreenEvent(viewModel.isLoggedIn())
        }
    }

    private fun observeOpenScreenTracker() {
        viewModel.openScreenTracker.observe(viewLifecycleOwner) { model ->
            val uri = Uri.parse(model.url)
            uri.lastPathSegment?.let { categorySlug ->
                analytic.sendOpenScreenEvent(
                    slug = categorySlug,
                    id = model.id,
                    name = model.name,
                    isLoggedInStatus = viewModel.isLoggedIn()
                )
            }
        }
    }

    private fun observeOpenLoginPage() {
        observe(viewModel.openLoginPage) {
            openLoginPage()
        }
    }

    private fun observeAtcDataTracker() {
        viewModel.atcDataTracker.observe(viewLifecycleOwner) { model ->
            when(model.layoutType) {
                CATEGORY_SHOWCASE.name -> trackCategoryShowcaseAddToCart(model)
                PRODUCT_ADS_CAROUSEL -> trackProductAdsAddToCart(model)
            }
        }
    }

    private fun observeProductRecommendationAtcDataTracker() {
        productRecommendationViewModel.atcDataTracker.observe(viewLifecycleOwner) { model ->
            analytic.categoryProductRecommendationAnalytic.sendClickAtcCarouselWidgetEvent(
                categoryIdL1 = categoryIdL1,
                index = model.position,
                productId = model.productRecommendation.getProductId(),
                warehouseId = viewModel.getWarehouseId(),
                isOos = model.productRecommendation.productCardModel.isOos(),
                name = model.productRecommendation.getProductName(),
                price = model.productRecommendation.getProductPrice().toIntSafely(),
                headerName = model.productRecommendation.headerName,
                quantity = model.quantity
            )
        }
    }

    private fun clickProductCard(
        appLink: String
    ) {
        if (appLink.isNotEmpty()) {
            val affiliateLink = viewModel.createAffiliateLink(
                url = appLink
            )
            RouteManager.route(context, affiliateLink)
        }
    }

    private fun clickProductCardShowcase(
        appLink: String,
        headerName: String,
        index: Int,
        productId: String,
        productName: String,
        productPrice: String,
        isOos: Boolean
    ) {
        clickProductCard(appLink)

        analytic.categoryShowcaseAnalytic.sendClickProductShowcaseLEvent(
            categoryIdL1 = categoryIdL1,
            headerName = headerName,
            index = index.getTrackerPosition(),
            productId = productId,
            warehouseId = viewModel.getWarehouseId(),
            isOos = isOos,
            name = productName,
            price = productPrice.getDigits()?.toLong().orZero()
        )
    }

    private fun clickProductCardRecommendation(
        appLink: String,
        headerName: String,
        index: Int,
        productId: String,
        productName: String,
        productPrice: String,
        isOos: Boolean
    ) {
        clickProductCard(appLink)

        analytic.categoryProductRecommendationAnalytic.sendClickProductCarouselEvent(
            categoryIdL1 = categoryIdL1,
            headerName = headerName,
            index = index.getTrackerPosition(),
            productId = productId,
            warehouseId = viewModel.getWarehouseId(),
            isOos = isOos,
            name = productName,
            price = productPrice.getDigits()?.toLong().orZero()
        )
    }

    private fun impressProductCardShowcase(
        headerName: String,
        index: Int,
        productId: String,
        productName: String,
        productPrice: String,
        isOos: Boolean
    ) {
        analytic.categoryShowcaseAnalytic.sendImpressionProductInShowcaseLEvent(
            categoryIdL1 = categoryIdL1,
            headerName = headerName,
            index = index.getTrackerPosition(),
            productId = productId,
            warehouseId = viewModel.getWarehouseId(),
            isOos = isOos,
            name = productName,
            price = productPrice.getDigits()?.toLong().orZero()
        )
    }

    private fun impressProductCardRecommendation(
        headerName: String,
        index: Int,
        productId: String,
        productName: String,
        productPrice: String,
        isOos: Boolean
    ) {
        analytic.categoryProductRecommendationAnalytic.sendImpressionProductCarouselEvent(
            categoryIdL1 = categoryIdL1,
            headerName = headerName,
            index = index.getTrackerPosition(),
            productId = productId,
            warehouseId = viewModel.getWarehouseId(),
            isOos = isOos,
            name = productName,
            price = productPrice.getDigits()?.toLong().orZero()
        )
    }

    private fun clickMoreCategories() {
        analytic.categoryTitleAnalytics.sendClickMoreCategoriesEvent(
            categoryIdL1 = categoryIdL1,
            warehouseId = viewModel.getWarehouseId()
        )
    }

    private fun clickCategoryNavigation(
        categoryIdL2: String
    ) {
        analytic.categoryNavigationAnalytic.sendClickCategoryNavigationEvent(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            warehouseId = viewModel.getWarehouseId()
        )
    }

    private fun impressCategoryNavigation(
        categoryIdL2: String
    ) {
        analytic.categoryNavigationAnalytic.sendImpressionCategoryNavigationEvent(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            warehouseId = viewModel.getWarehouseId()
        )
    }

    private fun clickCategoryMenu(
        categoryRecomIdL1: String
    ) {
        analytic.categoryMenuAnalytic.sendClickCategoryRecomWidgetEvent(
            categoryIdL1 = categoryIdL1,
            categoryRecomIdL1 = categoryRecomIdL1,
            warehouseId = viewModel.getWarehouseId()
        )
    }

    private fun impressCategoryMenu(
        categoryRecomIdL1: String
    ) {
        analytic.categoryMenuAnalytic.sendImpressionCategoryRecomWidgetEvent(
            categoryIdL1 = categoryIdL1,
            categoryRecomIdL1 = categoryRecomIdL1,
            warehouseId = viewModel.getWarehouseId()
        )
    }

    private fun clickSeeMoreShowcase(
        categoryIdL2: String
    ) {
        analytic.categoryShowcaseAnalytic.sendClickArrowButtonShowcaseLEvent(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            warehouseId = viewModel.getWarehouseId()
        )
    }

    private fun trackCategoryShowcaseAddToCart(model: CategoryAtcTrackerModel) {
        analytic.categoryShowcaseAnalytic.sendClickAtcOnShowcaseLEvent(
            categoryIdL1 = categoryIdL1,
            index = model.index,
            productId = model.product.productId,
            warehouseId = model.warehouseId,
            isOos = model.product.isOos(),
            name = model.product.name,
            price = model.product.getPriceLong(),
            headerName = model.headerName,
            quantity = model.quantity
        )
    }

    private fun trackProductAdsAddToCart(model: CategoryAtcTrackerModel) {
        val title = getString(R.string.tokopedianow_product_ads_carousel_title)
        analytic.productAdsAnalytic.trackProductAddToCart(
            position = model.index,
            title = title,
            quantity = model.quantity,
            shopId = model.shopId,
            shopName = model.shopName,
            shopType = model.shopType,
            categoryBreadcrumbs = model.categoryBreadcrumbs,
            product = model.product
        )
    }

    private fun changeProductCardQuantity(product: CategoryShowcaseItemUiModel, quantity: Int) {
        viewModel.onCartQuantityChanged(
            product = product.productCardModel,
            shopId = product.shopId,
            quantity = quantity,
            layoutType = CATEGORY_SHOWCASE.name
        )
    }

    private fun hideProductRecommendationWidget() = viewModel.removeProductRecommendation()

    private fun showMiniCart(
        data: MiniCartSimplifiedData
    ) {
        val showMiniCartWidget = data.isShowMiniCartWidget
        if (showMiniCartWidget) {
            val pageName = MiniCartAnalytics.Page.HOME_PAGE
            val shopIds = listOf(shopId)
            val source = MiniCartSource.TokonowHome
            binding?.apply {
                miniCartWidget.initialize(
                    shopIds = shopIds,
                    fragment = this@TokoNowCategoryFragment,
                    listener = this@TokoNowCategoryFragment,
                    pageName = pageName,
                    source = source
                )
                miniCartWidgetShadow?.show()
                miniCartWidget.show()
                miniCartWidget.hideTopContentView()
            }
        } else {
            hideMiniCart()
        }
    }

    private fun onSuccessAddItemToCart(
        data: AddToCartDataModel
    ) {
        val message = data.errorMessage.joinToString(separator = ", ")
        val actionText = getString(R.string.tokopedianow_toaster_see)
        showToaster(message = message, actionText = actionText, onClickAction = {
            showMiniCartBottomSheet()
        })
        getMiniCart()
    }

    private fun onSuccessUpdateCartItem() {
        val shopIds = listOf(shopId)
        binding?.miniCartWidget?.updateData(shopIds)
    }

    private fun showToaster(
        message: String,
        duration: Int = Toaster.LENGTH_SHORT,
        type: Int = Toaster.TYPE_NORMAL,
        actionText: String = "",
        onClickAction: View.OnClickListener = View.OnClickListener { }
    ) {
        view?.let { view ->
            if (message.isNotBlank()) {
                Toaster.toasterCustomBottomHeight = getMiniCartHeight()
                val toaster = Toaster.build(
                    view = view,
                    text = message,
                    duration = duration,
                    type = type,
                    actionText = actionText,
                    clickListener = onClickAction
                )
                toaster.show()
            }
        }
    }

    private fun showErrorToaster(error: Fail) {
        showToaster(
            message = error.throwable.message.orEmpty(),
            type = Toaster.TYPE_ERROR
        )
    }

    private fun onSuccessRemoveCartItem(data: Pair<String, String>) {
        showToaster(message = data.second)
        getMiniCart()
    }

    private fun showToasterWhenAddToCartBlocked() {
        showToaster(
            message = getString(R.string.tokopedianow_home_toaster_description_you_are_not_be_able_to_shop),
            type = Toaster.TYPE_ERROR
        )
    }

    private fun updateToolbarNotification() {
        binding?.navToolbar?.updateNotification()
    }

    private fun openLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        activity?.startActivityForResult(intent, RequestCode.REQUEST_CODE_LOGIN)
    }

    private fun hideMiniCart() {
        binding?.apply {
            miniCartWidgetShadow.hide()
            miniCartWidget.hide()
        }
    }
    private fun clickWishlistButton(
        productId: String,
        isWishlistSelected: Boolean,
        descriptionToaster: String,
        ctaToaster: String,
        type: Int,
        ctaClickListener: (() -> Unit)?
    ) {
        if (isWishlistSelected) {
            analytic.categoryOosProductAnalytic.trackClickAddToWishlist(
                warehouseId = viewModel.getWarehouseId(),
                productId = productId
            )
        } else {
            analytic.categoryOosProductAnalytic.trackClickRemoveFromWishlist(
                warehouseId = viewModel.getWarehouseId(),
                productId = productId
            )
        }
        viewModel.updateWishlistStatus(
            productId,
            isWishlistSelected
        )
        showToaster(
            message = descriptionToaster,
            type = type,
            actionText = ctaToaster
        ) {
            ctaClickListener?.invoke()
        }
    }

    /**
     * -- callback function section --
     */

    private fun createTitleCallback() = CategoryTitleCallback(
        context = context,
        onClickMoreCategories = ::clickMoreCategories
    )

    private fun createCategoryNavigationCallback() = CategoryNavigationCallback(
        onClickCategoryNavigation = ::clickCategoryNavigation,
        onImpressCategoryNavigation = ::impressCategoryNavigation
    )

    private fun createCategoryShowcaseItemCallback() = CategoryShowcaseItemCallback(
        shopId = shopId,
        categoryIdL1 = categoryIdL1,
        onClickProductCard = ::clickProductCardShowcase,
        onImpressProductCard = ::impressProductCardShowcase,
        onAddToCartBlocked = ::showToasterWhenAddToCartBlocked,
        onProductCartQuantityChanged = ::changeProductCardQuantity,
        startActivityForResult = ::startActivityForResult,
        onWishlistButtonClicked = ::clickWishlistButton
    )

    private fun createCategoryShowcaseHeaderCallback() = CategoryShowcaseHeaderCallback(
        onClickSeeMore = ::clickSeeMoreShowcase
    )

    private fun createTokoNowViewCallback() = TokoNowViewCallback(
        fragment = this@TokoNowCategoryFragment
    ) {
        viewModel.refreshLayout()
    }

    private fun createTokoNowCategoryMenuCallback() = TokoNowCategoryMenuCallback(
        onClickCategoryMenu = ::clickCategoryMenu,
        onImpressCategoryMenu = ::impressCategoryMenu
    )

    private fun createTokoNowChooseAddressWidgetCallback() = TokoNowChooseAddressWidgetCallback {
        analytic.sendClickWidgetChooseAddressEvent(
            categoryIdL1 = categoryIdL1,
            warehouseId = viewModel.getWarehouseId()
        )
    }

    private fun createProductRecommendationCallback() = CategoryProductRecommendationCallback(
        productRecommendationViewModel = productRecommendationViewModel,
        activity = activity,
        categoryIdL1 = categoryIdL1,
        onClickProductCard = ::clickProductCardRecommendation,
        onImpressProductCard = ::impressProductCardRecommendation,
        onOpenLoginPage = ::openLoginPage,
        onAddToCartBlocked = ::showToasterWhenAddToCartBlocked,
        onHideProductRecommendationWidget = ::hideProductRecommendationWidget,
        startActivityResult = ::startActivityForResult
    )

    private fun createProductCardCompactCallback() = ProductCardCompactCallback { productId, similarProductTrackerListener ->
        context?.apply {
            val intent = TokoNowSimilarProductBottomSheetActivity.createNewIntent(
                this,
                productId,
                similarProductTrackerListener
            )
            startActivity(intent)
        }
    }

    private fun createProductCardCompactSimilarProductTrackerCallback(): ProductCardCompactSimilarProductTrackerCallback {
        return ProductCardCompactSimilarProductTrackerCallback(analytic.categoryOosProductAnalytic)
    }

    private fun createProductCardAdsCallback(): CategoryProductCardAdsCallback {
        return CategoryProductCardAdsCallback(
            context = context,
            viewModel = viewModel,
            analytic = analytic.productAdsAnalytic,
            categoryIdL1 = categoryIdL1,
            startActivityResult = ::startActivityForResult,
            showToasterWhenAddToCartBlocked = ::showToasterWhenAddToCartBlocked
        )
    }
}
