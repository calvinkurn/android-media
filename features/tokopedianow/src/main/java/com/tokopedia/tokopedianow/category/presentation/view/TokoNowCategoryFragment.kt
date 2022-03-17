package com.tokopedia.tokopedianow.category.presentation.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.linker.LinkerManager
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.TOKONOW_CLP
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
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
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleItemDataView
import com.tokopedia.tokopedianow.category.presentation.typefactory.CategoryTypeFactoryImpl
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryViewModel
import com.tokopedia.tokopedianow.category.utils.RECOM_QUERY_PARAM_CATEGORY_ID
import com.tokopedia.tokopedianow.category.utils.RECOM_QUERY_PARAM_REF
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.util.TokoNowUniversalShareUtil
import com.tokopedia.tokopedianow.common.util.TokoNowUniversalShareUtil.shareRequest
import com.tokopedia.tokopedianow.common.viewholder.TokoNowCategoryGridViewHolder
import com.tokopedia.tokopedianow.home.domain.model.ShareHomeTokonow
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.VALUE_LIST_OOC
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.VALUE_TOPADS
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
        TokoNowCategoryGridViewHolder.TokoNowCategoryGridListener,
        ShareBottomsheetListener,
        PermissionListener {

    companion object {
        const val PAGE_SHARE_NAME = "Tokonow"
        const val SHARE = "share"
        const val PAGE_TYPE_CATEGORY = "cat[%s]"
        const val DEFAULT_CATEGORY_ID = "0"
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
    private var categorySharingModel: CategorySharingModel? = null
    private var shareHomeTokonow: ShareHomeTokonow? = null
    private var categoryIdLvl2 = ""
    private var categoryIdLvl3 = ""

    override val toolbarPageName = "TokoNow Category"

    override val disableDefaultShareTracker: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        shareHomeTokonow = createShareHomeTokonow()
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
            switcherWidgetListener = this,
            tokoNowEmptyStateNoResultListener = this,
            categoryAisleListener = this,
            recommendationCarouselListener = this,
            tokoNowCategoryGridListener = this,
            tokoNowProductCardListener = this,
            recomWidgetBindPageNameListener = this
    )

    override fun getViewModel() = tokoNowCategoryViewModel

    override fun observeViewModel() {
        super.observeViewModel()

        getViewModel().openScreenTrackingUrlLiveData.observe(this::sendOpenScreenTracking)
        getViewModel().sharingLiveData.observe(this::setCategorySharingModel)
    }

    override val miniCartWidgetPageName: MiniCartAnalytics.Page
        get() = MiniCartAnalytics.Page.CATEGORY_PAGE

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

    override fun screenShotTaken() {
        updateShareHomeData(
            isScreenShot = true,
            thumbNailTitle = resources.getString(R.string.tokopedianow_home_share_thumbnail_title_ss)
        )

        showUniversalShareBottomSheet(shareHomeTokonow)
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        if (shareHomeTokonow?.isScreenShot == true) {
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
            shareHomeTokonow = shareHomeTokonow,
            activity = activity,
            view = view,
            onSuccess = {
                universalShareBottomSheet?.dismiss()
            }
        )
    }

    private fun updateShareHomeData(isScreenShot: Boolean, thumbNailTitle: String) {
        shareHomeTokonow?.isScreenShot = isScreenShot
        shareHomeTokonow?.thumbNailTitle = thumbNailTitle
    }

    private fun createShareHomeTokonow(): ShareHomeTokonow{
        return ShareHomeTokonow(
            resources.getString(R.string.tokopedianow_category_share_main_text, categorySharingModel?.name.orEmpty()),
            categorySharingModel?.url.orEmpty(),
            userSession.userId,
            listOf(String.format(PAGE_TYPE_CATEGORY, getLevelCategory()), categorySharingModel?.id.orEmpty()),
            TokoNowHomeFragment.THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
            TokoNowHomeFragment.THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
            resources.getString(R.string.tokopedianow_category_share_title, categorySharingModel?.name.orEmpty()),
            resources.getString(R.string.tokopedianow_category_share_desc, categorySharingModel?.name.orEmpty()),
        )
    }

    private fun getLevelCategory(): Int = when {
        categoryIdLvl3.isNotBlank() && categoryIdLvl3 != DEFAULT_CATEGORY_ID -> CATEGORY_LVL_3
        categoryIdLvl2.isNotBlank() && categoryIdLvl3 != DEFAULT_CATEGORY_ID -> CATEGORY_LVL_2
        else -> CATEGORY_LVL_1
    }

    override fun onCloseOptionClicked() {
        if (shareHomeTokonow?.isScreenShot == true) {
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
            thumbNailTitle = resources.getString(R.string.tokopedianow_home_share_thumbnail_title)
        )

        CategoryTracking.trackClickShareButtonTopNav(
            userId = userSession.userId,
            categoryIdLvl1 = tokoNowCategoryViewModel.categoryL1,
            categoryIdLvl2 = categoryIdLvl2,
            categoryIdLvl3 = categoryIdLvl3
        )
        shareClicked(shareHomeTokonow)
    }

    private fun shareClicked(shareHomeTokonow: ShareHomeTokonow?){
        if(UniversalShareBottomSheet.isCustomSharingEnabled(context)){
            showUniversalShareBottomSheet(shareHomeTokonow)
        } else {
            LinkerManager.getInstance().executeShareRequest(shareRequest(context, shareHomeTokonow))
        }
    }

    private fun showUniversalShareBottomSheet(shareHomeTokonow: ShareHomeTokonow?) {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@TokoNowCategoryFragment)
            setUtmCampaignData(
                pageName = PAGE_SHARE_NAME,
                userId = shareHomeTokonow?.userId.orEmpty(),
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
        categoryIdLvl2 = option.value
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
        categoryIdLvl3 = selectedOption.value
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

    override fun getAtcEventAction(isOOC: Boolean): String {
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

    override fun getEventLabel(isOOC: Boolean): String {
        return getViewModel().categoryIdTracking
    }

    override fun onCategoryRetried() {
        getViewModel().onCategoryGridRetry()
    }

    override fun onAllCategoryClicked() { }

    override fun onCategoryClicked(position: Int, categoryId: String) { }

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

    override fun onSeeMoreClick(data: RecommendationCarouselData, applink: String) {
        CategoryTracking.sendRecommendationSeeAllClickEvent(getViewModel().categoryIdTracking)

        RouteManager.route(context, modifySeeMoreRecomApplink(applink))
    }

    private fun modifySeeMoreRecomApplink(originalApplink: String): String {
        val uri = Uri.parse(originalApplink)
        val queryParamsMap = UrlParamUtils.getParamMap(uri.query ?: "")
        val recomRef = queryParamsMap[RECOM_QUERY_PARAM_REF] ?: ""

        return if (recomRef == TOKONOW_CLP) {
            val recomCategoryId = queryParamsMap[RECOM_QUERY_PARAM_CATEGORY_ID] ?: ""

            if (recomCategoryId.isEmpty()) {
                queryParamsMap[RECOM_QUERY_PARAM_CATEGORY_ID] = getViewModel().categoryL1
            }

            "${uri.scheme}://" +
                    "${uri.host}/" +
                    "${uri.path}?" +
                    UrlParamUtils.generateUrlParamString(queryParamsMap)
        } else {
            originalApplink
        }
    }

    private fun sendOpenScreenTracking(model: CategoryTrackerModel) {
        val uri = Uri.parse(model.url)
        val categorySlug = uri.lastPathSegment ?: return

        CategoryTracking.sendOpenScreenTracking(categorySlug, model.id, model.name, userSession.isLoggedIn)
    }

    private fun setCategorySharingModel(model: CategorySharingModel) {
        categorySharingModel = model
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
}