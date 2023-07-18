package com.tokopedia.tokopedianow.category.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryAdapter
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowViewCallback
import com.tokopedia.tokopedianow.category.presentation.viewmodel.BaseCategoryViewModel
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowDiffer
import com.tokopedia.tokopedianow.common.constant.RequestCode
import com.tokopedia.tokopedianow.common.model.ShareTokonow
import com.tokopedia.tokopedianow.common.util.GlobalErrorUtil
import com.tokopedia.tokopedianow.common.util.StringUtil.getOrDefaultZeroString
import com.tokopedia.tokopedianow.common.util.TokoNowUniversalShareUtil
import com.tokopedia.tokopedianow.common.view.NoAddressEmptyStateView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowCategoryBaseBinding
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.LinkProperties
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

abstract class BaseCategoryFragment : Fragment(), ScreenShotListener,
    ShareBottomsheetListener, PermissionListener, MiniCartWidgetListener,
    NoAddressEmptyStateView.ActionListener {

    companion object {
        private const val SCROLL_DOWN_DIRECTION = 1
        private const val START_SWIPE_PROGRESS_POSITION = 120
        private const val END_SWIPE_PROGRESS_POSITION = 200

        private const val PAGE_NAME = "TokoNow Category"
        private const val PAGE_SHARE_NAME = "Tokonow"
        private const val SHARE = "share"

        private const val NO_ADDRESS_EVENT_TRACKER = "tokonow - category page"
        private const val THUMBNAIL_AND_OG_IMAGE_SHARE_URL = TokopediaImageUrl.THUMBNAIL_AND_OG_IMAGE_SHARE_URL

        private const val TOKONOW_DIRECTORY = "tokonow_directory"
    }

    @Inject
    lateinit var analytic: CategoryAnalytic

    protected var categoryIdL1 = String.EMPTY
    protected var categoryIdL2 = String.EMPTY
    protected var currentCategoryId = String.EMPTY
    protected var queryParamMap: HashMap<String, String>? = hashMapOf()

    protected val shopId: String
        get() = viewModel.getShopId().toString()

    private val categoryIdL3: String
        get() = String.EMPTY
    private val userId: String
        get() = viewModel.getUserId()
    private val addressData: LocalCacheModel
        get() = viewModel.getAddressData()

    protected val navToolbarHeight: Int
        get() {
            val defaultHeight = context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_default_toolbar_height).orZero()
            return if (binding?.navToolbar?.height.isZero()) {
                defaultHeight
            } else {
                binding?.navToolbar?.height
            } ?: defaultHeight
        }

    private val onScrollListener: RecyclerView.OnScrollListener
        get() = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val isAtTheBottomOfThePage =
                    !recyclerView.canScrollVertically(SCROLL_DOWN_DIRECTION)
                viewModel.onScroll(isAtTheBottomOfThePage)
            }
        }

    private val recycledViewPool
        get() = RecyclerView.RecycledViewPool()

    private var shareTokonow: ShareTokonow? = null
    private var screenshotDetector: ScreenshotDetector? = null
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null

    private var binding by autoClearedNullable<FragmentTokopedianowCategoryBaseBinding>()

    private val adapter: CategoryAdapter by lazy {
        CategoryAdapter(createAdapterTypeFactory(), createAdapterDiffer())
    }

    protected abstract val viewModel: BaseCategoryViewModel

    protected abstract fun createAdapterTypeFactory(): BaseAdapterTypeFactory

    protected abstract fun createAdapterDiffer(): BaseTokopediaNowDiffer

    protected abstract fun initInjector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shareTokonow = createShareTokonow()
        setCategoryViewModelData()
        setupScreenshotDetector()
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
        observeLiveData()
        setupView(binding)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onViewResume()
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

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
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

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        viewModel.getMiniCart()
    }

    override fun onPrimaryBtnClicked() {
        showBottomSheetChooseAddress()
    }

    override fun onSecondaryBtnClicked() {
        RouteManager.route(context, ApplinkConst.HOME)
        activity?.finish()
    }

    override fun onGetNoAddressEmptyStateEventCategoryTracker(): String {
        return NO_ADDRESS_EVENT_TRACKER
    }

    protected open fun observeLiveData() {
        observePageLoading()
        observeVisitableList()
        observeMiniCart()
        observeAddToCart()
        observeUpdateCartItem()
        observeRemoveCartItem()
        observeRefreshState()
        observeOutOfCoverageState()
        observeToolbarNotification()
        observeOpenLoginPage()
    }

    protected open fun setupView(
        binding: FragmentTokopedianowCategoryBaseBinding?
    ) {
        binding?.apply {
            setNavToolbarHeight(navToolbar)
            setupRecyclerView(rvCategory, navToolbar)
            setupRefreshLayout(strRefreshLayout)
            setupNavigationToolbar(navToolbar)
        }
    }

    protected open fun setupRecyclerView(
        recyclerView: RecyclerView,
        navToolbar: NavToolbar
    ) {
        recyclerView.apply {
            adapter = this@BaseCategoryFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(onScrollListener)
            setRecycledViewPool(recycledViewPool)
            animation = null
        }
    }

    protected open fun onGetMiniCartSuccess(
        data: MiniCartSimplifiedData
    ) {
        showMiniCart(data)
    }

    protected fun submitList(items: List<Visitable<*>>) {
        adapter.submitList(items)
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

    protected fun onSuccessAddItemToCart(data: AddToCartDataModel) {
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

    protected fun openLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        activity?.startActivityForResult(intent, RequestCode.REQUEST_CODE_LOGIN)
    }

    protected fun updateToolbarNotification() {
        binding?.navToolbar?.updateNotification()
    }

    protected fun removeScrollListener(listener: RecyclerView.OnScrollListener? = onScrollListener) {
        listener?.let {
            binding?.rvCategory?.removeOnScrollListener(it)
        }
    }

    protected fun addScrollListener(listener: RecyclerView.OnScrollListener?) {
        listener?.let {
            binding?.rvCategory?.addOnScrollListener(it)
        }
    }

    protected fun createTokoNowViewCallback() = TokoNowViewCallback(
        fragment = this@BaseCategoryFragment
    ) {
        viewModel.refreshLayout()
    }

    private fun observeVisitableList() {
        observe(viewModel.visitableListLiveData) {
            submitList(it)
        }
    }

    private fun observePageLoading() {
        observe(viewModel.isPageLoading) { loading ->
            if(loading) {
                showShimmeringLayout()
            } else {
                showMainLayout()
            }
        }
    }

    private fun observeMiniCart() {
        observe(viewModel.miniCart) {
            when (it) {
                is Success -> onGetMiniCartSuccess(it.data)
                is Fail -> hideMiniCart()
            }
        }
    }

    private fun observeUpdateCartItem() {
        observe(viewModel.updateCartItem) {
            when (it) {
                is Success -> onSuccessUpdateCartItem()
                is Fail -> showErrorToaster(it)
            }
        }
    }

    private fun observeRemoveCartItem() {
        observe(viewModel.removeCartItem) {
            when (it) {
                is Success -> onSuccessRemoveCartItem(it.data)
                is Fail -> showErrorToaster(it)
            }
        }
    }

    private fun observeAddToCart() {
        observe(viewModel.addItemToCart) {
            when (it) {
                is Success -> onSuccessAddItemToCart(it.data)
                is Fail -> showErrorToaster(it)
            }
        }
    }

    private fun observeRefreshState() {
        viewModel.refreshState.observe(viewLifecycleOwner) {
            binding?.apply {
                rvCategory.removeOnScrollListener(onScrollListener)
                rvCategory.addOnScrollListener(onScrollListener)
            }
            viewModel.onViewCreated()
        }
    }

    private fun observeOutOfCoverageState() {
        viewModel.outOfCoverageState.observe(viewLifecycleOwner) {
            binding?.showOutOfCoverageLayout()
            analytic.sendOocOpenScreenEvent(viewModel.isLoggedIn())
        }
    }

    private fun observeToolbarNotification() {
        viewModel.updateToolbarNotification.observe(viewLifecycleOwner) { needToUpdate ->
            if (needToUpdate) updateToolbarNotification()
        }
    }

    private fun observeOpenLoginPage() {
        observe(viewModel.openLoginPage) {
            openLoginPage()
        }
    }

    private fun setCategoryViewModelData() {
        viewModel.apply {
            categoryIdL1 = this@BaseCategoryFragment.categoryIdL1
            categoryIdL2 = this@BaseCategoryFragment.categoryIdL2
            currentCategoryId = this@BaseCategoryFragment.currentCategoryId
            queryParamMap = this@BaseCategoryFragment.queryParamMap
        }
    }

    private fun setupRefreshLayout(swipeToRefresh :SwipeToRefresh) {
        swipeToRefresh.apply {
            setProgressViewOffset(
                false,
                START_SWIPE_PROGRESS_POSITION,
                END_SWIPE_PROGRESS_POSITION
            )
            setOnRefreshListener {
                isRefreshing = false
                refreshLayout()
            }
        }
    }

    private fun setupNavigationToolbar(navToolbar: NavToolbar) {
        navToolbar.apply {
            viewLifecycleOwner.lifecycle.addObserver(navToolbar)
            setupToolbarWithStatusBar(activity = requireActivity())
            setToolbarPageName(PAGE_NAME)
            setIcon(
                IconBuilder()
                    .addShare()
                    .addCart()
                    .addNavGlobal()
            )
            setupSearchbar()
            bringToFront()
        }
    }

    private fun showShimmeringLayout() {
        binding?.apply {
            mainLayout.hide()
            categoryShimmering.root.show()
            globalError.hide()
            oocLayout.hide()
        }
    }

    private fun setNavToolbarHeight(navToolbar: NavToolbar) {
        navToolbar.post {
            viewModel.navToolbarHeight = navToolbarHeight
            viewModel.onViewCreated()
        }
    }

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
                    fragment = this@BaseCategoryFragment,
                    listener = this@BaseCategoryFragment,
                    pageName = pageName,
                    source = source
                )
                miniCartWidgetShadow.show()
                miniCartWidget.show()
                miniCartWidget.hideTopContentView()
            }
        } else {
            hideMiniCart()
        }
    }

    private fun getMiniCartHeight(): Int {
        val space16 = context?.resources?.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_16
        )?.toInt().orZero()
        return binding?.miniCartWidget?.height.orZero() - space16
    }

    private fun refreshLayout() = viewModel.refreshLayout()

    private fun getMiniCart() = viewModel.getMiniCart()

    private fun hideMiniCart() {
        binding?.apply {
            miniCartWidgetShadow.hide()
            miniCartWidget.hide()
        }
    }

    private fun showMiniCartBottomSheet() {
        binding?.miniCartWidget?.showMiniCartListBottomSheet(
            fragment = this
        )
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

    private fun getNavToolbarHint(): List<HintData> {
        val hint = getString(R.string.tokopedianow_search_bar_hint)
        return listOf(HintData(hint, hint))
    }

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

    private fun showUniversalShareBottomSheet(shareCategoryTokonow: ShareTokonow?, path: String? = null) {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            setFeatureFlagRemoteConfigKey()
            enableDefaultShareIntent()
            path?.let {
                setImageOnlySharingOption(true)
                setScreenShotImagePath(path)
            }
            init(this@BaseCategoryFragment)
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
            setLinkProperties(LinkProperties(
                ogImageUrl = shareCategoryTokonow?.ogImageUrl.orEmpty()
            ))
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

    private fun createShareTokonow(): ShareTokonow = ShareTokonow(
        thumbNailImage = THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
        ogImageUrl = THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
        linkerType = LinkerData.NOW_TYPE
    )

    private fun updateShareCategoryData(
        isScreenShot: Boolean,
        thumbNailTitle: String
    ) {
        shareTokonow?.isScreenShot = isScreenShot
        shareTokonow?.thumbNailTitle = thumbNailTitle
    }

    protected fun showMainLayout() {
        binding?.apply {
            mainLayout.show()
            categoryShimmering.root.hide()
            globalError.hide()
            oocLayout.hide()
        }
    }

    protected fun showErrorLayout(throwable: Throwable) {
       binding?.apply {
           mainLayout.hide()
           categoryShimmering.root.hide()
           globalError.show()
           oocLayout.hide()

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

    private fun FragmentTokopedianowCategoryBaseBinding.showOutOfCoverageLayout() {
        mainLayout.hide()
        globalError.hide()
        categoryShimmering.root.hide()
        oocLayout.show()
        oocLayout.actionListener = this@BaseCategoryFragment
    }
}
