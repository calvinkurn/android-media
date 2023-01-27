package com.tokopedia.tokopedianow.category.presentation.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.model.LinkerData.NOW_TYPE
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.TOKONOW_CLP
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_ATC_CLP_PRODUCT_TOKONOW
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_CLP_PRODUCT_TOKONOW
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_CLP_RECOM_OOC
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.IMPRESSION_CLP_PRODUCT_TOKONOW
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.IMPRESSION_CLP_RECOM_OOC
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Category.TOKONOW_CATEGORY_PAGE
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Category.TOKONOW_DASH_CATEGORY_PAGE
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Misc.RECOM_LIST_PAGE
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Misc.RECOM_LIST_PAGE_NON_OOC
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Misc.TOKONOW_CATEGORY_ORGANIC
import com.tokopedia.tokopedianow.category.di.CategoryComponent
import com.tokopedia.tokopedianow.category.domain.model.CategorySharingModel
import com.tokopedia.tokopedianow.category.domain.model.CategoryTrackerModel
import com.tokopedia.tokopedianow.category.presentation.listener.CategoryAisleListener
import com.tokopedia.tokopedianow.category.presentation.listener.CategoryMenuCallback
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleItemDataView
import com.tokopedia.tokopedianow.category.presentation.typefactory.CategoryTypeFactoryImpl
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryViewModel
import com.tokopedia.tokopedianow.common.model.ShareTokonow
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.util.StringUtil.getOrDefaultZeroString
import com.tokopedia.tokopedianow.common.util.TokoNowUniversalShareUtil
import com.tokopedia.tokopedianow.common.util.TokoNowUniversalShareUtil.shareRequest
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.THUMBNAIL_AND_OG_IMAGE_SHARE_URL
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.VALUE_LIST_OOC
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.VALUE_TOPADS
import com.tokopedia.tokopedianow.searchcategory.data.model.QuerySafeModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.view.BaseSearchCategoryFragment
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_DIRECTORY
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import javax.inject.Inject

class TokoNowCategoryFragment:
        BaseSearchCategoryFragment(),
        CategoryAisleListener,
        ScreenShotListener,
        ShareBottomsheetListener,
        PermissionListener {

    companion object {
        private const val AR_ORIGIN_TOKONOW_CATEGORY = 5

        const val PAGE_SHARE_NAME = "Tokonow"
        const val SHARE = "share"
        const val PAGE_TYPE_CATEGORY = "cat%s"
        const val URL_PARAM_LVL_2 = "?exclude_sc=%s"
        const val URL_PARAM_LVL_3 = "&sc=%s"
        const val DEEPLINK_PARAM_LVL_3 = "?sc=%s"
        const val DEFAULT_CATEGORY_ID = "0"
        const val DEFAULT_DEEPLINK_PARAM = "category"
        const val CATEGORY_LVL_1 = 1
        const val CATEGORY_LVL_2 = 2
        const val CATEGORY_LVL_3 = 3

        @JvmStatic
        fun create(): TokoNowCategoryFragment {
            return TokoNowCategoryFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var tokoNowCategoryViewModel: TokoNowCategoryViewModel
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var screenshotDetector : ScreenshotDetector? = null
    private var shareCategoryTokonow: ShareTokonow? = null
    private var categoryIdLvl2 = ""
    private var categoryIdLvl3 = ""

    override val toolbarPageName = "TokoNow Category"

    override val disableDefaultShareTracker: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        shareCategoryTokonow = createShareHomeTokonow()
    }

    override fun onResume() {
        super.onResume()
        screenshotDetector?.start()
    }

    override fun onStop() {
        UniversalShareBottomSheet.clearState(screenshotDetector)
        super.onStop()
    }

    override fun onDestroy() {
        UniversalShareBottomSheet.clearState(screenshotDetector)
        super.onDestroy()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            screenshotDetector = UniversalShareBottomSheet.createAndStartScreenShotDetector(
                context = it,
                screenShotListener = this,
                fragment = this,
                permissionListener = this
            )
        }
    }

    private fun initViewModel() {
        activity?.let {
            tokoNowCategoryViewModel = ViewModelProvider(it, viewModelFactory).get(TokoNowCategoryViewModel::class.java)
        }
    }

    override fun createNavToolbarIconBuilder(): IconBuilder = IconBuilder()
        .addShare()
        .addCart()
        .addGlobalNav()

    override val isDisableSearchBarDefaultGtmTracker: Boolean
        get() = true

    override fun getBaseAutoCompleteApplink() =
            super.getBaseAutoCompleteApplink() + "?" +
                    "${SearchApiConst.NAVSOURCE}=$TOKONOW_DIRECTORY"

    override fun onSearchBarClick(hint: String) {
        CategoryTracking.sendSearchBarClickEvent(getViewModel().categoryL1)
        super.onSearchBarClick(hint)
    }

    override val disableDefaultCartTracker: Boolean
        get() = true

    override fun onNavToolbarCartClicked() {
        CategoryTracking.sendCartClickEvent(getViewModel().categoryL1)
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        getComponent(CategoryComponent::class.java).inject(this)
    }

    override fun createTypeFactory() = CategoryTypeFactoryImpl(
            tokoNowEmptyStateOocListener = createTokoNowEmptyStateOocListener(TOKONOW_DASH_CATEGORY_PAGE),
            chooseAddressListener = this,
            titleListener = this,
            bannerListener = this,
            quickFilterListener = this,
            categoryFilterListener = this,
            productItemListener = this,
            similarProductListener = createSimilarProductCallback(true),
            switcherWidgetListener = this,
            tokoNowEmptyStateNoResultListener = this,
            categoryAisleListener = this,
            tokoNowCategoryMenuListener = createCategoryMenuCallback(),
            tokoNowProductCardListener = this,
            productRecommendationOocBindListener = createProductRecommendationOocCallback(),
            productRecommendationOocListener = createProductRecommendationOocCallback(),
            productRecommendationListener = createProductRecommendationCallback().copy(
                categoryL1 = getViewModel().categoryL1,
                cdListName = getCDListName(),
                categoryIdTracking = getViewModel().categoryIdTracking
            ),
            feedbackWidgetListener = this
    )

    override fun getViewModel() = tokoNowCategoryViewModel

    override fun observeViewModel() {
        super.observeViewModel()

        getViewModel().openScreenTrackingUrlLiveData.observe(this::sendOpenScreenTracking)
        getViewModel().shareLiveData.observe(this::setCategorySharingModel)
    }

    override val miniCartWidgetPageName: MiniCartAnalytics.Page
        get() = MiniCartAnalytics.Page.CATEGORY_PAGE

    override val miniCartWidgetSource: MiniCartSource
        get() = MiniCartSource.TokonowCategoryPage

    override fun onAisleClick(categoryAisleItemDataView: CategoryAisleItemDataView) {
        CategoryTracking.sendAisleClickEvent(getViewModel().categoryL1, categoryAisleItemDataView.id)

        RouteManager.route(context, categoryAisleItemDataView.applink)
    }

    override fun onProductImpressed(productItemDataView: ProductItemDataView) {
        val trackingQueue = trackingQueue ?: return

        CategoryTracking.sendProductImpressionEvent(
                trackingQueue,
                productItemDataView,
                getViewModel().categoryL1,
                getUserId(),
                getViewModel().categoryIdTracking,
        )
    }

    override fun onWishlistButtonClicked(
        productId: String,
        isWishlistSelected: Boolean,
        descriptionToaster: String,
        ctaToaster: String,
        type: Int,
        ctaClickListener: (() -> Unit)?
    ) {
        if(isWishlistSelected) {
            CategoryTracking.trackClickAddToWishlist(
                getViewModel().warehouseId,
                productId
            )
        }
        else{
            CategoryTracking.trackClickRemoveFromWishlist(
                getViewModel().warehouseId,
                productId
            )
        }
        getViewModel().updateWishlistStatus(
            productId,
            isWishlistSelected
        )
        showToaster(descriptionToaster, type, ctaToaster) {
            ctaClickListener?.invoke()
        }
    }

    override fun screenShotTaken() {
        updateShareHomeData(
            isScreenShot = true,
            thumbNailTitle = context?.resources?.getString(R.string.tokopedianow_home_share_thumbnail_title_ss).orEmpty()
        )

        showUniversalShareBottomSheet(shareCategoryTokonow)
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        if (shareCategoryTokonow?.isScreenShot == true) {
            CategoryTracking.trackClickChannelShareBottomSheetScreenshot(shareModel.channel.orEmpty(),
                userId = userSession.userId,
                categoryIdLvl1 = tokoNowCategoryViewModel.categoryL1,
                categoryIdLvl2 = categoryIdLvl2,
                categoryIdLvl3 = categoryIdLvl3
            )
        } else {
            CategoryTracking.trackClickChannelShareBottomSheet(channel = shareModel.channel.orEmpty(),
                userId = userSession.userId,
                categoryIdLvl1 = tokoNowCategoryViewModel.categoryL1,
                categoryIdLvl2 = categoryIdLvl2,
                categoryIdLvl3 = categoryIdLvl3
            )
        }

        TokoNowUniversalShareUtil.shareOptionRequest(
            shareModel = shareModel,
            shareTokoNowData = shareCategoryTokonow,
            activity = activity,
            view = view,
            onSuccess = {
                universalShareBottomSheet?.dismiss()
            }
        )
    }

    private fun updateShareHomeData(isScreenShot: Boolean, thumbNailTitle: String) {
        shareCategoryTokonow?.isScreenShot = isScreenShot
        shareCategoryTokonow?.thumbNailTitle = thumbNailTitle
    }

    private fun setCategorySharingModel(model: CategorySharingModel) {
        categoryIdLvl2 = model.categoryIdLvl2
        categoryIdLvl3 = model.categoryIdLvl3

        shareCategoryTokonow?.apply {
            id = model.deeplinkParam
            sharingUrl = model.url
            pageIdConstituents = model.utmCampaignList
            sharingText = context?.resources?.getString(R.string.tokopedianow_category_share_main_text, model.title).orEmpty()
            specificPageName = context?.resources?.getString(R.string.tokopedianow_category_share_title, model.title).orEmpty()
            specificPageDescription = context?.resources?.getString(R.string.tokopedianow_category_share_desc, model.title).orEmpty()
        }
    }

    override fun trackingEventLabel(): String = tokoNowCategoryViewModel.getCurrentCategoryId(
        categoryIdLvl1 = tokoNowCategoryViewModel.categoryL1,
        categoryIdLvl2 = categoryIdLvl2,
        categoryIdLvl3 = categoryIdLvl3
    )

    private fun createShareHomeTokonow(): ShareTokonow {
        return ShareTokonow(
            thumbNailImage = THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
            ogImageUrl = THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
            linkerType = NOW_TYPE
        )
    }

    override fun onCloseOptionClicked() {
        if (shareCategoryTokonow?.isScreenShot == true) {
            CategoryTracking.trackClickCloseScreenShotShareBottomSheet(
                userId = userSession.userId,
                categoryIdLvl1 = tokoNowCategoryViewModel.categoryL1,
                categoryIdLvl2 = categoryIdLvl2,
                categoryIdLvl3 = categoryIdLvl3
            )
        } else {
            CategoryTracking.trackClickCloseShareBottomSheet(
                userId = userSession.userId,
                categoryIdLvl1 = tokoNowCategoryViewModel.categoryL1,
                categoryIdLvl2 = categoryIdLvl2,
                categoryIdLvl3 = categoryIdLvl3
            )
        }
    }

    override fun onNavToolbarShareClicked() {
        updateShareHomeData(
            isScreenShot = false,
            thumbNailTitle = context?.resources?.getString(R.string.tokopedianow_home_share_thumbnail_title).orEmpty()
        )

        CategoryTracking.trackClickShareButtonTopNav(
            userId = userSession.userId,
            categoryIdLvl1 = tokoNowCategoryViewModel.categoryL1,
            categoryIdLvl2 = categoryIdLvl2,
            categoryIdLvl3 = categoryIdLvl3,
            currentCategoryId = tokoNowCategoryViewModel.getCurrentCategoryId(tokoNowCategoryViewModel.categoryL1, categoryIdLvl2, categoryIdLvl3)
        )
        shareClicked(shareCategoryTokonow)
    }

    private fun shareClicked(shareHomeTokonow: ShareTokonow?){
        if(UniversalShareBottomSheet.isCustomSharingEnabled(context)){
            showUniversalShareBottomSheet(shareHomeTokonow)
        } else {
            LinkerManager.getInstance().executeShareRequest(shareRequest(context, shareHomeTokonow))
        }
    }

    private fun showUniversalShareBottomSheet(shareHomeTokonow: ShareTokonow?) {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@TokoNowCategoryFragment)
            setUtmCampaignData(
                pageName = PAGE_SHARE_NAME,
                userId = userSession.userId.getOrDefaultZeroString(),
                pageIdConstituents = shareHomeTokonow?.pageIdConstituents.orEmpty(),
                feature = SHARE
            )
            setMetaData(
                tnTitle = shareHomeTokonow?.thumbNailTitle.orEmpty(),
                tnImage = shareHomeTokonow?.thumbNailImage.orEmpty(),
            )
            //set the Image Url of the Image that represents page
            setOgImageUrl(imgUrl = shareHomeTokonow?.ogImageUrl.orEmpty())
        }

        if (shareHomeTokonow?.isScreenShot == true) {
            CategoryTracking.trackImpressChannelShareBottomSheetScreenShot(
                userId = userSession.userId,
                categoryIdLvl1 = tokoNowCategoryViewModel.categoryL1,
                categoryIdLvl2 = categoryIdLvl2,
                categoryIdLvl3 = categoryIdLvl3
            )
        } else {
            CategoryTracking.trackImpressChannelShareBottomSheet(
                userId = userSession.userId,
                categoryIdLvl1 = tokoNowCategoryViewModel.categoryL1,
                categoryIdLvl2 = categoryIdLvl2,
                categoryIdLvl3 = categoryIdLvl3
            )
        }
        universalShareBottomSheet?.show(childFragmentManager, this, screenshotDetector)
    }


    override fun onProductClick(productItemDataView: ProductItemDataView) {
        CategoryTracking.sendProductClickEvent(
                productItemDataView,
                getViewModel().categoryL1,
                getUserId(),
                getViewModel().categoryIdTracking,
        )

        super.onProductClick(productItemDataView)
    }

    override fun onBannerImpressed(channelModel: ChannelModel, position: Int) {
        CategoryTracking.sendBannerImpressionEvent(channelModel, getViewModel().categoryL1, getUserId())

        super.onBannerImpressed(channelModel, position)
    }

    override fun onBannerClick(channelModel: ChannelModel, applink: String, param: String) {
        CategoryTracking.sendBannerClickEvent(channelModel, getViewModel().categoryL1, getUserId())

        super.onBannerClick(channelModel, applink, param)
    }

    override fun onSeeAllCategoryClicked() {
        CategoryTracking.sendAllCategoryClickEvent(getViewModel().categoryL1)

        super.onSeeAllCategoryClicked()
    }

    override fun onCategoryFilterChipClick(option: Option, isSelected: Boolean) {
        CategoryTracking.sendApplyCategoryL2FilterEvent(getViewModel().categoryL1, option.value)

        super.onCategoryFilterChipClick(option, isSelected)
    }

    override fun openFilterPage() {
        CategoryTracking.sendFilterClickEvent(getViewModel().categoryL1)

        super.openFilterPage()
    }

    override fun sendTrackingQuickFilter(quickFilterTracking: Pair<Option, Boolean>) {
        CategoryTracking.sendQuickFilterClickEvent(getViewModel().categoryL1)
    }

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        CategoryTracking.sendApplySortFilterEvent(getViewModel().categoryL1)

        super.onApplySortFilter(applySortFilterModel)
    }

    override fun openCategoryChooserFilterPage(filter: Filter) {
        CategoryTracking.sendOpenCategoryL3FilterEvent(getViewModel().categoryL1)

        super.openCategoryChooserFilterPage(filter)
    }

    override fun onApplyCategory(selectedOption: Option) {
        CategoryTracking.sendApplyCategoryL3FilterEvent(getViewModel().categoryL1, selectedOption.value)

        super.onApplyCategory(selectedOption)
    }

    override fun sendAddToCartTrackingEvent(atcData: Triple<Int, String, ProductItemDataView>) {
        val (quantity, cartId, productItemDataView) = atcData

        CategoryTracking.sendAddToCartEvent(
                productItemDataView,
                getViewModel().categoryL1,
                getUserId(),
                quantity,
                cartId,
        )
    }

    override fun sendDeleteCartTrackingEvent(productId: String) {

    }

    override fun onProductChooseVariantClicked(productItemDataView: ProductItemDataView) {
        CategoryTracking.sendChooseVariantClickEvent(getViewModel().categoryL1)

        super.onProductChooseVariantClicked(productItemDataView)
    }

    override fun getCDListName(): String {
        return String.format(TOKONOW_CATEGORY_ORGANIC, tokoNowCategoryViewModel.categoryIdTracking)
    }

    override fun sendIncreaseQtyTrackingEvent(productId: String) {
        CategoryTracking.sendIncreaseQtyEvent(getViewModel().categoryL1)
    }

    override fun sendDecreaseQtyTrackingEvent(productId: String) {
        CategoryTracking.sendDecreaseQtyEvent(getViewModel().categoryL1)
    }

    override fun getImpressionEventAction(isOOC: Boolean): String {
        return if (isOOC) {
            IMPRESSION_CLP_RECOM_OOC
        } else {
            IMPRESSION_CLP_PRODUCT_TOKONOW
        }
    }

    override fun getClickEventAction(isOOC: Boolean): String {
        return if (isOOC) {
            CLICK_CLP_RECOM_OOC
        } else {
            CLICK_CLP_PRODUCT_TOKONOW
        }
    }

    override fun getAtcEventAction(): String {
        return CLICK_ATC_CLP_PRODUCT_TOKONOW
    }

    override fun getEventCategory(isOOC: Boolean): String {
        return TOKONOW_CATEGORY_PAGE
    }

    override fun getListValue(isOOC: Boolean, recommendationItem: RecommendationItem): String {
        return if (isOOC) {
            String.format(
                VALUE_LIST_OOC,
                RECOM_LIST_PAGE,
                recommendationItem.recommendationType,
                if (recommendationItem.isTopAds) VALUE_TOPADS else ""
            )
        } else {
            RECOM_LIST_PAGE_NON_OOC
        }
    }

    override fun getEventLabel(): String {
        return getViewModel().categoryIdTracking
    }

    override fun onProductCardImpressed(position: Int, data: TokoNowProductCardUiModel) {
        super.onProductCardImpressed(position, data)

        val trackingQueue = trackingQueue ?: return

        CategoryTracking.sendRepurchaseWidgetImpressionEvent(
            trackingQueue,
            data,
            position,
            userSession.userId
        )
    }

    override fun onProductCardClicked(position: Int, data: TokoNowProductCardUiModel) {
        super.onProductCardClicked(position, data)

        CategoryTracking.sendRepurchaseWidgetClickEvent(
            data,
            position,
            userSession.userId
        )
    }

    override fun sendAddToCartRepurchaseProductTrackingEvent(
        addToCartRepurchaseProductData: Triple<Int, String, TokoNowProductCardUiModel>
    ) {
        val (quantity, cartId, repurchaseProduct) = addToCartRepurchaseProductData

        CategoryTracking.sendRepurchaseWidgetAddToCartEvent(
            repurchaseProduct,
            quantity,
            cartId,
            userSession.userId,
        )
    }

    private fun sendOpenScreenTracking(model: CategoryTrackerModel) {
        val uri = Uri.parse(model.url)
        val categorySlug = uri.lastPathSegment ?: return

        CategoryTracking.sendOpenScreenTracking(categorySlug, model.id, model.name, userSession.isLoggedIn)
    }

    override fun sendOOCOpenScreenTracking(isTracked: Boolean) {
        CategoryTracking.sendOOCOpenScreenTracking(userSession.isLoggedIn)
    }

    override fun permissionAction(action: String, label: String) {
        CategoryTracking.trackClickAccessMediaAndFiles(label,
            userId = userSession.userId,
            categoryIdLvl1 = tokoNowCategoryViewModel.categoryL1,
            categoryIdLvl2 = categoryIdLvl2,
            categoryIdLvl3 = categoryIdLvl3
        )
    }

    override fun showDialogAgeRestriction(querySafeModel: QuerySafeModel) {
        if (!querySafeModel.isQuerySafe) {
            AdultManager.showAdultPopUp(this, AR_ORIGIN_TOKONOW_CATEGORY, "${querySafeModel.warehouseId} - ${tokoNowCategoryViewModel.categoryL1.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl3.getOrDefaultZeroString()}")
        }
    }

    override fun refreshLayout() {
        super.refreshLayout()
        refreshProductRecommendation(TOKONOW_CLP)
    }

    override fun updateProductRecommendation(needToUpdate: Boolean) {
        if (needToUpdate) {
            refreshProductRecommendation(TOKONOW_CLP)
        }
    }

    private fun createCategoryMenuCallback(): CategoryMenuCallback {
        return CategoryMenuCallback(
            viewModel = tokoNowCategoryViewModel,
            userId = userSession.userId
        )
    }
}
