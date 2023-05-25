package com.tokopedia.tokopedianow.category.presentation.fragment

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
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.model.LinkerData.NOW_TYPE
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryAdapter
import com.tokopedia.tokopedianow.common.constant.RequestCode
import com.tokopedia.tokopedianow.common.model.ShareTokonow
import com.tokopedia.tokopedianow.common.util.GlobalErrorUtil
import com.tokopedia.tokopedianow.common.util.GlobalErrorUtil.ERROR_MAINTENANCE
import com.tokopedia.tokopedianow.common.util.GlobalErrorUtil.ERROR_PAGE_FULL
import com.tokopedia.tokopedianow.common.util.GlobalErrorUtil.ERROR_PAGE_NOT_FOUND
import com.tokopedia.tokopedianow.common.util.GlobalErrorUtil.ERROR_SERVER
import com.tokopedia.tokopedianow.common.util.StringUtil.getOrDefaultZeroString
import com.tokopedia.tokopedianow.common.util.TokoNowUniversalShareUtil
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowCategoryBaseBinding
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking
import com.tokopedia.tokopedianow.oldcategory.presentation.view.TokoNowCategoryFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.utils.lifecycle.autoClearedNullable

abstract class TokoNowCategoryBaseFragment: BaseDaggerFragment(),
    ScreenShotListener,
    ShareBottomsheetListener,
    PermissionListener,
    MiniCartWidgetListener
{
    private companion object {
        const val SCROLL_DOWN_DIRECTION = 1
        const val START_SWIPE_PROGRESS_POSITION = 120
        const val END_SWIPE_PROGRESS_POSITION = 200

        const val PAGE_NAME = "TokoNow Category"
        const val TOKONOW_DIRECTORY = "tokonow_directory"
        const val THUMBNAIL_AND_OG_IMAGE_SHARE_URL = TokopediaImageUrl.THUMBNAIL_AND_OG_IMAGE_SHARE_URL
    }

    abstract val userId: String
    abstract val categoryIdL1: String
    abstract val categoryIdL2: String
    abstract val categoryIdL3: String
    abstract val currentCategoryId: String
    abstract val shopId: String
    abstract val adapter: CategoryAdapter
    abstract val addressData: LocalCacheModel

    protected var binding by autoClearedNullable<FragmentTokopedianowCategoryBaseBinding>()

    protected val navToolbarHeight: Int
        get() {
            val defaultHeight = context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_default_toolbar_height).orZero()
            return if (binding?.navToolbar?.height.isZero()) defaultHeight else binding?.navToolbar?.height ?: defaultHeight
        }

    protected val onScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val isAtTheBottomOfThePage = !recyclerView.canScrollVertically(SCROLL_DOWN_DIRECTION)
            loadMore(isAtTheBottomOfThePage)
        }
    }

    private var shareTokonow: ShareTokonow? = null
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var screenshotDetector : ScreenshotDetector? = null

    override fun getScreenName(): String = String.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shareTokonow = createShareTokonow()
    }

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
        UniversalShareBottomSheet.clearState(screenshotDetector)
        super.onStop()
    }

    override fun onDestroy() {
        UniversalShareBottomSheet.clearState(screenshotDetector)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupScreenshotDetector()

        binding?.apply {
            setupNavigationToolbar()
            setupRecyclerView()
            setupRefreshLayout()
            showShimmering()
        }
    }

    override fun screenShotTaken() {
        updateShareCategoryData(
            isScreenShot = true,
            thumbNailTitle = context?.resources?.getString(R.string.tokopedianow_share_thumbnail_title_ss).orEmpty()
        )

        showUniversalShareBottomSheet(shareTokonow)
    }

    override fun permissionAction(action: String, label: String) {
        CategoryTracking.trackClickAccessMediaAndFiles(label,
            userId = userId,
            categoryIdLvl1 = categoryIdL1,
            categoryIdLvl2 = categoryIdL2,
            categoryIdLvl3 = categoryIdL3
        )
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        if (shareTokonow?.isScreenShot == true) {
            CategoryTracking.trackClickChannelShareBottomSheetScreenshot(
                channel = shareModel.channel.orEmpty(),
                userId = userId,
                categoryIdLvl1 = categoryIdL1,
                categoryIdLvl2 = categoryIdL2,
                categoryIdLvl3 = categoryIdL3
            )
        } else {
            CategoryTracking.trackClickChannelShareBottomSheet(
                channel = shareModel.channel.orEmpty(),
                userId = userId,
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
            CategoryTracking.trackClickCloseScreenShotShareBottomSheet(
                userId = userId,
                categoryIdLvl1 = categoryIdL1,
                categoryIdLvl2 = categoryIdL2,
                categoryIdLvl3 = categoryIdL3
            )
        } else {
            CategoryTracking.trackClickCloseShareBottomSheet(
                userId = userId,
                categoryIdLvl1 = categoryIdL1,
                categoryIdLvl2 = categoryIdL2,
                categoryIdLvl3 = categoryIdL3
            )
        }
    }

    abstract fun loadMore(
        isAtTheBottomOfThePage: Boolean
    )

    abstract fun refreshLayout()

    abstract fun getMiniCart()

    protected fun FragmentTokopedianowCategoryBaseBinding.showRecyclerView() {
        rvCategory.show()
        categoryShimmering.root.hide()
        globalError.hide()
    }

    protected fun FragmentTokopedianowCategoryBaseBinding.showGlobalError(
        throwable: Throwable
    ) {
        rvCategory.hide()
        categoryShimmering.root.hide()
        globalError.show()

        if (throwable is MessageErrorException) {
            val errorCode = throwable.errorCode
            val errorType = GlobalErrorUtil.getErrorType(
                throwable = throwable,
                errorCode = errorCode
            )

            globalError.setType(errorType)

            when (errorCode) {
                ERROR_PAGE_NOT_FOUND -> setupGlobalErrorPageNotFound()
                ERROR_SERVER -> setupGlobalErrorServer()
                ERROR_MAINTENANCE -> setupGlobalErrorMaintenance()
                ERROR_PAGE_FULL -> setupGlobalErrorPageFull()
            }
        } else {
            val errorType = GlobalErrorUtil.getErrorType(
                throwable = throwable,
                errorCode = String.EMPTY
            )
            globalError.setType(errorType)
            setupGlobalErrorNoConnection()
        }
    }

    private fun FragmentTokopedianowCategoryBaseBinding.setupGlobalErrorPageNotFound(){
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

    private fun FragmentTokopedianowCategoryBaseBinding.setupGlobalErrorServer() {
        globalError.apply {
            setActionClickListener {
                refreshLayout()
            }
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

    private fun FragmentTokopedianowCategoryBaseBinding.setupGlobalErrorPageFull() {
        globalError.apply {
            setActionClickListener {
                refreshLayout()
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

    protected fun FragmentTokopedianowCategoryBaseBinding.showShimmering() {
        rvCategory.hide()
        categoryShimmering.root.show()
        globalError.hide()
    }

    protected fun showMiniCart(
        data: MiniCartSimplifiedData
    ) {
        val miniCartWidget = binding?.miniCartWidget
        val showMiniCartWidget = data.isShowMiniCartWidget

        if(showMiniCartWidget) {
            val pageName = MiniCartAnalytics.Page.HOME_PAGE
            val shopIds = listOf(shopId)
            val source = MiniCartSource.TokonowHome
            miniCartWidget?.initialize(
                shopIds = shopIds,
                fragment = this,
                listener = this,
                pageName = pageName,
                source = source
            )
            miniCartWidget?.show()
        } else {
            hideMiniCart()
        }
    }

    protected fun setupPadding(
        data: MiniCartSimplifiedData
    ) {
        binding?.miniCartWidget?.post {
            val paddingZero = context?.resources?.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.layout_lvl0
            ).orZero()
            val paddingBottom = if (data.isShowMiniCartWidget) getMiniCartHeight() else paddingZero
            binding?.rvCategory?.setPadding(paddingZero, paddingZero, paddingZero, paddingBottom)
        }
    }

    protected fun onSuccessAddItemToCart(
        data: AddToCartDataModel
    ) {
        val message = data.errorMessage.joinToString(separator = ", ")
        val actionText = getString(R.string.tokopedianow_toaster_see)
        showToaster(message = message, actionText = actionText, onClickAction = {
            showMiniCartBottomSheet()
        })
        getMiniCart()
    }

    protected fun onSuccessUpdateCartItem() {
        val shopIds = listOf(shopId)
        binding?.miniCartWidget?.updateData(shopIds)
    }

    protected fun showToaster(
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

    protected fun showErrorToaster(error: Fail) {
        showToaster(
            message = error.throwable.message.orEmpty(),
            type = Toaster.TYPE_ERROR
        )
    }

    protected fun onSuccessRemoveCartItem(data: Pair<String, String>) {
        showToaster(message = data.second)
        getMiniCart()
    }

    protected fun showToasterWhenAddToCartBlocked() {
        showToaster(
            message = getString(R.string.tokopedianow_home_toaster_description_you_are_not_be_able_to_shop),
            type = Toaster.TYPE_ERROR
        )
    }

    protected fun updateToolbarNotification() {
        binding?.navToolbar?.updateNotification()
    }

    protected fun openLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        activity?.startActivityForResult(intent, RequestCode.REQUEST_CODE_LOGIN)
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
        },
        disableDefaultGtmTracker = true,
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

        CategoryTracking.trackClickShareButtonTopNav(
            userId = userId,
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
        CategoryTracking.sendCartClickEvent(categoryIdL1)
    }

    private fun FragmentTokopedianowCategoryBaseBinding.setupNavigationToolbar() {
        navToolbar.apply {
            bringToFront()
            setToolbarPageName(PAGE_NAME)
            setIcon(
                IconBuilder()
                    .addShare()
                    .addCart()
                    .addNavGlobal()
            )
            setupSearchbar()
            setupNavigationToolbarInteraction()
        }
    }

    private fun FragmentTokopedianowCategoryBaseBinding.setupRecyclerView() {
        rvCategory.adapter = this@TokoNowCategoryBaseFragment.adapter
        rvCategory.layoutManager = LinearLayoutManager(context)
        rvCategory.addOnScrollListener(onScrollListener)
        rvCategory.addOnScrollListener(createNavRecyclerViewOnScrollListener(navToolbar))
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

    private fun createShareTokonow(): ShareTokonow = ShareTokonow(
        thumbNailImage = THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
        ogImageUrl = THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
        linkerType = NOW_TYPE
    )

    private fun shareClicked(shareCategoryTokonow: ShareTokonow?){
        if(UniversalShareBottomSheet.isCustomSharingEnabled(context)){
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

    private fun showUniversalShareBottomSheet(shareCategoryTokonow: ShareTokonow?) {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@TokoNowCategoryBaseFragment)
            setUtmCampaignData(
                pageName = TokoNowCategoryFragment.PAGE_SHARE_NAME,
                userId = userId.getOrDefaultZeroString(),
                pageIdConstituents = shareCategoryTokonow?.pageIdConstituents.orEmpty(),
                feature = TokoNowCategoryFragment.SHARE
            )
            setMetaData(
                tnTitle = shareCategoryTokonow?.thumbNailTitle.orEmpty(),
                tnImage = shareCategoryTokonow?.thumbNailImage.orEmpty(),
            )
            //set the Image Url of the Image that represents page
            setOgImageUrl(imgUrl = shareCategoryTokonow?.ogImageUrl.orEmpty())
        }

        if (shareCategoryTokonow?.isScreenShot == true) {
            CategoryTracking.trackImpressChannelShareBottomSheetScreenShot(
                userId = userId,
                categoryIdLvl1 = categoryIdL1,
                categoryIdLvl2 = categoryIdL2,
                categoryIdLvl3 = categoryIdL3
            )
        } else {
            CategoryTracking.trackImpressChannelShareBottomSheet(
                userId = userId,
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

    private fun createNavRecyclerViewOnScrollListener(
        navToolbar: NavToolbar,
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

    private fun setupScreenshotDetector() {
        context?.let {
            screenshotDetector = UniversalShareBottomSheet.createAndStartScreenShotDetector(
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
               localizingAddressStateData =  addressData
            )
        }
        return false
    }

    fun resetPadding() {
        val paddingZero = context?.resources?.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.layout_lvl0
        ).orZero()
        binding?.rvCategory?.setPadding(
            paddingZero,
            paddingZero,
            paddingZero,
            paddingZero
        )
    }

    fun hideMiniCart() {
        binding?.miniCartWidget?.hide()
    }
}
