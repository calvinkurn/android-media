package com.tokopedia.dilayanitokopedia.home.presentation.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.common.constant.ConstantKey.PARAM_APPLINK_AUTOCOMPLETE
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutState
import com.tokopedia.dilayanitokopedia.common.model.DtShareUniversalModel
import com.tokopedia.dilayanitokopedia.common.util.CustomLinearLayoutManager
import com.tokopedia.dilayanitokopedia.common.util.DtUniversalShareUtil
import com.tokopedia.dilayanitokopedia.databinding.FragmentDtHomeBinding
import com.tokopedia.dilayanitokopedia.home.constant.AnchorTabStatus
import com.tokopedia.dilayanitokopedia.home.di.component.DaggerHomeComponent
import com.tokopedia.dilayanitokopedia.home.domain.model.Data
import com.tokopedia.dilayanitokopedia.home.domain.model.SearchPlaceholder
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.DtAnchorTabAdapter
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.DtHomeAdapter
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.DtHomeAdapterTypeFactory
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.differ.HomeListDiffer
import com.tokopedia.dilayanitokopedia.home.presentation.listener.DtDynamicLegoBannerCallback
import com.tokopedia.dilayanitokopedia.home.presentation.listener.DtHomeCategoryListener
import com.tokopedia.dilayanitokopedia.home.presentation.listener.DtLeftCarouselCallback
import com.tokopedia.dilayanitokopedia.home.presentation.listener.DtSlideBannerCallback
import com.tokopedia.dilayanitokopedia.home.presentation.listener.DtTopCarouselCallback
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.AnchorTabUiModel
import com.tokopedia.dilayanitokopedia.home.presentation.view.NestedRecyclerView
import com.tokopedia.dilayanitokopedia.home.presentation.viewmodel.DtHomeViewModel
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutListUiModel
import com.tokopedia.dilayanitokopedia.home.widget.ToggleableSwipeRefreshLayout
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.listener.FeaturedShopListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.listener.MixTopComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.linker.LinkerManager
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * Dilayani Tokopedia  ( DT )
 * Created by irpan on 07/09/22.
 */
class DtHomeFragment : Fragment(), ShareBottomsheetListener, ScreenShotListener, PermissionListener {

    companion object {
        const val SOURCE = "dilayanitokopedia"
        const val SOURCE_TRACKING = "dilayanitokopedia page"

        // scroll listener
        private const val RV_DIRECTION_TOP = 1
        private const val VERTICAL_SCROLL_FULL_BOTTOM_OFFSET = 0
        private const val SHARE = "share"

        private const val CLICK_TIME_INTERVAL: Long = 500

        private const val SHARE_LINK_TITLE = "Dilayani Tokopedia | Tokopedia"
        private const val SHARE_LINK_URL = "https://www.tokopedia.com/dilayani-tokopedia"
        private const val SHARE_LINK_THUMBNAIL_IMAGE = "https://images.tokopedia.net/img/QBrNqa/2023/1/12/b0a09eb3-7876-4a21-a9ba-4c532f500559.png"
        private const val SHARE_LINK_OG_IMAGE = "https://images.tokopedia.net/img/QBrNqa/2023/1/12/b0a09eb3-7876-4a21-a9ba-4c532f500559.png"
        private const val SHARE_LINK_PAGE_NAME = "DilayaniTokopedia"
    }

    @Inject
    lateinit var viewModelDtHome: DtHomeViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var navToolbar: NavToolbar? = null
    private var statusBarBackground: View? = null
    private var rvHome: NestedRecyclerView? = null
    private var ivHeaderBackground: ImageView? = null

    private var root: View? = null
    private var refreshLayout: ToggleableSwipeRefreshLayout? = null

    private var rvLayoutManager: CustomLinearLayoutManager? = null

    private var localCacheModel: LocalCacheModel? = null

    private var chooseAddressWidget: ChooseAddressWidget? = null

    private var statusBarState = AnchorTabStatus.MAXIMIZE

    private var shareHome = DtShareUniversalModel()

    private var screenshotDetector: ScreenshotDetector? = null

    var universalShareBottomSheet: UniversalShareBottomSheet? = null

    private var linearLayoutManager: CustomLinearLayoutManager? = null
    private var anchorTabLinearLayoutManager: LinearLayoutManager? = null

    private var mLastClickTime = System.currentTimeMillis()

    private var coachMark: CoachMark2? = null

    private val adapter by lazy {
        DtHomeAdapter(
            typeFactory = DtHomeAdapterTypeFactory(
                homeRecommendationFeedListener = createRecommendationCallback(),
                featuredShopListener = createFeatureShopCallback(),
                bannerComponentListener = createSlideBannerCallback(),
                homeTopComponentListener = createTopComponentCallback(),
                homeTopCarouselListener = createTopCarouselCallback(),
                homeLeftCarouselListener = createLeftCarouselCallback(),
                dynamicLegoBannerCallback = createLegoBannerCallback()
            ),
            differ = HomeListDiffer()
        )
    }

    private var anchorTabAdapter: DtAnchorTabAdapter? = null

    private var binding by autoClearedNullable<FragmentDtHomeBinding>()

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    private fun initInjector() {
        DaggerHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDtHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUiVariable()
        initNavToolbar()
        initRecyclerView()
        initAnchorTabMenu()
        initRecyclerScrollListener()
        initRefreshLayout()
        initScreenSootListener()
        initStatusBar()

        updateCurrentPageLocalCacheModelData()

        observeLiveData()
        loadLayout()
    }

    override fun onPause() {
        coachMark?.dismissCoachMark()
        coachMark = null
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        checkIfChooseAddressWidgetDataUpdated()
    }

    private fun initScreenSootListener() {
        context?.let {
            screenshotDetector = UniversalShareBottomSheet.createAndStartScreenShotDetector(
                context = it,
                screenShotListener = this,
                fragment = this,
                permissionListener = this
            )
        }
    }

    private fun initChooseAddressWidget() {
        chooseAddressWidget = binding?.chooseAddressWidget
        bindChooseAddressWidget()
        showCoachMark()
    }

    private fun initAnchorTabMenu() {
        anchorTabLinearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        anchorTabAdapter = DtAnchorTabAdapter(anchorTabListener())
        binding?.headerCompHolder?.layoutManager = anchorTabLinearLayoutManager
        binding?.headerCompHolder?.adapter = anchorTabAdapter
    }

    private fun anchorTabListener(): DtAnchorTabAdapter.AnchorTabListener {
        return object : DtAnchorTabAdapter.AnchorTabListener {

            override fun onMenuSelected(anchorTabUiModel: AnchorTabUiModel, position: Int) {
                var scrollPosition =
                    adapter.data.indexOf(viewModelDtHome.getPositionUsingGroupId(anchorTabUiModel.groupId)?.layout)
                // handle 0 value
                if (position != 0 && scrollPosition == 0) return
                if (scrollPosition == -1) return

                if (statusBarState == AnchorTabStatus.MAXIMIZE && scrollPosition != 0) {
                    setAnchorTabMinimize()
                }

                smoothScrollToComponentWithPosition(scrollPosition)
            }
        }
    }

    private fun initStatusBar() {
        /*
            this status bar background only shows for android Kitkat below
            In that version, status bar can't be forced to dark mode
            We must set background to keep status bar icon visible
        */
        activity?.let {
            statusBarBackground?.apply {
                layoutParams?.height = ViewHelper.getStatusBarHeight(activity)
                visibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) View.INVISIBLE else View.VISIBLE
            }
            setStatusBarAlpha()
        }
    }

    private fun setStatusBarAlpha() {
        val drawable = statusBarBackground?.background
        drawable?.alpha = 0
        statusBarBackground?.background = drawable
    }

    private fun initUiVariable() {
        view?.apply {
            ivHeaderBackground = binding?.dtViewBackgroundImage
            navToolbar = binding?.dtHomeNavToolbar
            statusBarBackground = binding?.dtHomeStatusBarBackground
            rvHome = binding?.rvHome

            refreshLayout = binding?.homeSwipeRefreshLayout
            root = binding?.root
        }
    }

    private fun initRefreshLayout() {
        refreshLayout?.isEnabled = false
    }

    private fun initNavToolbar() {
        setupTopNavigation()
        setIconNewTopNavigation()
    }

    private fun setIconNewTopNavigation() {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
            .addIcon(IconList.ID_SHARE, onClick = ::onClickShareButton, disableDefaultGtmTracker = true)
            .addIcon(IconList.ID_CART, onClick = ::onClickCartButton)
            .addIcon(IconList.ID_NAV_GLOBAL) {}
        navToolbar?.setIcon(icons)
    }

    private fun setupTopNavigation() {
        navToolbar?.let { toolbar ->
            viewLifecycleOwner.lifecycle.addObserver(toolbar)
            initHint(SearchPlaceholder(Data(null, context?.resources?.getString(R.string.dt_search_bar_hint).orEmpty(), "")))
            activity?.let {
                toolbar.setupToolbarWithStatusBar(it)
                toolbar.setToolbarTitle(getString(R.string.dt_home_title))
            }
        }
        navToolbar?.hideShadow()
    }

    private fun initHint(searchPlaceholder: SearchPlaceholder) {
        searchPlaceholder.data?.let { data ->
            navToolbar?.setupSearchbar(
                hints = listOf(
                    HintData(
                        data.placeholder ?: "",
                        data.keyword
                            ?: ""
                    )
                ),
                applink = if (data.keyword?.isEmpty() != false) {
                    ApplinkConstInternalDiscovery.AUTOCOMPLETE
                } else {
                    PARAM_APPLINK_AUTOCOMPLETE
                },
                searchbarClickCallback = { onSearchBarClick() },
                searchbarImpressionCallback = {}
            )
        }
    }

    private fun onClickCartButton() {
        // no-op
    }

    private fun onClickShareButton() {
        updateShareHomeData(
            pageIdConstituents = listOf("home"),
            isScreenShot = false,
            linkerType = "Dilayani-tokopedia"
        )

        shareClicked(shareHome)
    }

    private fun updateShareHomeData(
        pageIdConstituents: List<String>,
        isScreenShot: Boolean,
        linkerType: String,
        id: String = ""
    ) {
        shareHome.pageIdConstituents = pageIdConstituents
        shareHome.isScreenShot = isScreenShot
        shareHome.thumbNailTitle = SHARE_LINK_TITLE
        shareHome.linkerType = linkerType
        shareHome.id = id
        shareHome.sharingUrl = SHARE_LINK_URL
        shareHome.thumbNailImage = SHARE_LINK_THUMBNAIL_IMAGE
    }

    private fun shareClicked(shareHomeTokonow: DtShareUniversalModel?) {
        if (UniversalShareBottomSheet.isCustomSharingEnabled(context)) {
            showUniversalShareBottomSheet(shareHomeTokonow)
        } else {
            LinkerManager.getInstance().apply {
                executeShareRequest(DtUniversalShareUtil.shareRequest(context, shareHomeTokonow))
            }
        }
    }

    private fun showUniversalShareBottomSheet(shareHomeDt: DtShareUniversalModel?) {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@DtHomeFragment)
            setUtmCampaignData(
                pageName = SHARE_LINK_PAGE_NAME,
                userId = userSession.userId,
                pageIdConstituents = shareHomeDt?.pageIdConstituents.orEmpty(),
                feature = SHARE
            )

            setMetaData(
                tnTitle = shareHomeDt?.thumbNailTitle.orEmpty(),
                tnImage = shareHomeDt?.thumbNailImage.orEmpty()
            )
            // set the Image Url of the Image that represents page
            setOgImageUrl(SHARE_LINK_OG_IMAGE)
        }

        universalShareBottomSheet?.show(childFragmentManager, this, screenshotDetector)
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        DtUniversalShareUtil.shareOptionRequest(
            shareModel = shareModel,
            shareData = shareHome,
            activity = activity,
            view = view,
            onSuccess = {
                universalShareBottomSheet?.dismiss()
            }
        )
    }

    override fun onCloseOptionClicked() {
        // no-op
    }

    private fun showHomeLayout(data: HomeLayoutListUiModel) {
        rvHome?.post {
            adapter.submitList(data.items)
        }
    }

    private fun showEmptyState() {
        NetworkErrorHelper.showEmptyState(
            activity,
            binding?.root,
            this::loadLayout
        )
    }

    private fun updateCurrentPageLocalCacheModelData() {
        context?.let {
            localCacheModel = ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private fun onSuccessGetHomeLayout(data: HomeLayoutListUiModel) {
        when (data.state) {
            DtLayoutState.SHOW -> onShowHomeLayout(data)
            DtLayoutState.LOADING -> onLoadingHomeLayout(data)
            else -> {
                showHomeLayout(data)
            }
        }
    }

    private fun onShowHomeLayout(data: HomeLayoutListUiModel) {
        initChooseAddressWidget()
        showHomeLayout(data)
        showHeaderBackground()
        visibilityChooseAddress()
        visibilityAnchorTab()
    }

    private fun updateAnchorTab(data: List<AnchorTabUiModel>) {
        anchorTabAdapter?.updateList(data)
    }

    private fun observeLiveData() {
        observe(viewModelDtHome.homeLayoutList) {
            when (it) {
                is Success -> onSuccessGetHomeLayout(it.data)
                is Fail -> showEmptyState()
            }
        }

        observeMenuList()
    }

    private fun observeMenuList() {
        observe(viewModelDtHome.menuList) {
            updateAnchorTab(it)
        }
    }

    private fun getHomeLayout() {
        localCacheModel?.let { lca ->
            viewModelDtHome.getHomeLayout(lca)
        }
    }

    private fun showLayout() {
        getHomeLayout()
        navToolbar?.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_SEARCH)
    }

    private fun initRecyclerView() {
        linearLayoutManager = CustomLinearLayoutManager(requireContext())

        context?.let {
            rvHome?.apply {
                adapter = this@DtHomeFragment.adapter
                rvLayoutManager = linearLayoutManager
                layoutManager = rvLayoutManager
            }
        }
    }

    private fun createLeftCarouselCallback(): MixLeftComponentListener {
        return DtLeftCarouselCallback.createLeftCarouselCallback {
            onActionLinkClicked(it)
        }
    }

    private fun createLegoBannerCallback(): DynamicLegoBannerListener? {
        return DtDynamicLegoBannerCallback(requireContext())
    }

    private fun onRefreshLayout() {
        rvLayoutManager?.setScrollEnabled(true)
        anchorTabAdapter?.resetToFirst()
        updateCurrentPageLocalCacheModelData()
        refreshLayout()
    }

    private fun loadLayout() {
        viewModelDtHome.loadLayout()
    }

    private fun onLoadingHomeLayout(data: HomeLayoutListUiModel) {
        showHomeLayout(data)
        loadHeaderBackgroundLoading()
        showLayout()
        visibilityChooseAddress(false)
        visibilityAnchorTab(false)
    }

    private fun visibilityChooseAddress(visible: Boolean = true) {
        if (visible) {
            binding?.chooseAddressWidget?.visible()
        } else {
            binding?.chooseAddressWidget?.gone()
        }
    }

    private fun visibilityAnchorTab(visible: Boolean = true) {
        if (visible) {
            binding?.headerCompHolder?.visible()
        } else {
            binding?.headerCompHolder?.gone()
        }
    }

    private fun loadHeaderBackgroundLoading() {
        ivHeaderBackground?.setImageResource(R.drawable.dt_ic_header_background_shimmering)
        ivHeaderBackground?.show()
    }

    private fun onSearchBarClick() {
        RouteManager.route(
            context,
            getAutoCompleteApplinkPattern(),
            SOURCE,
            context?.resources?.getString(R.string.dt_search_bar_hint).orEmpty()
        )
    }

    private fun getAutoCompleteApplinkPattern() = ApplinkConstInternalDiscovery.AUTOCOMPLETE

    private fun refreshLayout() {
        localCacheModel?.apply {
            viewModelDtHome.refreshLayout()
        }
    }

    private fun checkIfChooseAddressWidgetDataUpdated() {
        if (isChooseAddressWidgetDataUpdated()) {
            updateCurrentPageLocalCacheModelData()
            refreshLayout()
        }
    }

    private fun isChooseAddressWidgetDataUpdated(): Boolean {
        localCacheModel?.let {
            context?.apply {
                return ChooseAddressUtils.isLocalizingAddressHasUpdated(
                    this,
                    it
                )
            }
        }
        return false
    }

    private fun createTopComponentCallback(): HomeComponentListener? {
        return object : HomeComponentListener {
            override fun onChannelExpired(channelModel: ChannelModel, channelPosition: Int, visitable: Visitable<*>) {
                // no-op
            }
        }
    }

    private fun onActionLinkClicked(actionLink: String) {
        val now = System.currentTimeMillis()
        if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
            return
        }
        mLastClickTime = now
        if (TextUtils.isEmpty(actionLink)) {
            return
        }
        if (activity != null && RouteManager.isSupportApplink(activity, actionLink)) {
            openApplink(actionLink)
        }
    }

    private fun openApplink(appLink: String) {
        if (appLink.isNotEmpty()) {
            RouteManager.route(context, appLink)
        }
    }

    private fun createSlideBannerCallback(): BannerComponentListener? {
        return DtSlideBannerCallback.createSlideBannerCallback {
            onActionLinkClicked(it)
        }
    }

    private fun createTopCarouselCallback(): MixTopComponentListener? {
        return DtTopCarouselCallback().createTopCarouselCallback {
            onActionLinkClicked(actionLink = it)
        }
    }

    private fun createFeatureShopCallback(): FeaturedShopListener {
        return object : FeaturedShopListener {
            override fun onSeeAllClicked(channelModel: ChannelModel, position: Int) {
                // no-op
            }

            override fun onSeeAllBannerClicked(channelModel: ChannelModel, applink: String, position: Int) {
                // no-op
            }

            override fun onFeaturedShopBannerBackgroundClicked(channel: ChannelModel) {
                // no-op
            }

            override fun onFeaturedShopItemImpressed(
                channelModel: ChannelModel,
                channelGrid: ChannelGrid,
                position: Int,
                parentPosition: Int
            ) {
                // no-op
            }

            override fun onFeaturedShopItemClicked(
                channelModel: ChannelModel,
                channelGrid: ChannelGrid,
                position: Int,
                parentPosition: Int
            ) {
                // no-op
            }
        }
    }

    private fun bindChooseAddressWidget() {
        chooseAddressWidget?.bindChooseAddress(object : ChooseAddressWidget.ChooseAddressWidgetListener {
            override fun onLocalizingAddressUpdatedFromWidget() {
                onRefreshLayout()
            }

            override fun onLocalizingAddressServerDown() {
                visibilityChooseAddress(false)
            }

            override fun onClickChooseAddressTokoNowTracker() {
                // no-op
            }

            override fun needToTrackTokoNow(): Boolean = false

            override fun getLocalizingAddressHostFragment(): Fragment = this@DtHomeFragment

            override fun getLocalizingAddressHostSourceData(): String = SOURCE

            override fun getLocalizingAddressHostSourceTrackingData(): String = SOURCE_TRACKING

            override fun onLocalizingAddressUpdatedFromBackground() {
                // no-op
            }

            override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {
                visibilityChooseAddress(isRollOutUser)
            }

            override fun onLocalizingAddressLoginSuccess() {
                // no-op
            }
        })
    }

    private fun showCoachMark() {
        val coachMarkList = arrayListOf<CoachMark2Item>().apply {
            getChooseAddressWidgetCoachMarkItem()?.let {
                add(it)
            }
        }
        if (coachMarkList.isNotEmpty()) {
            context?.let {
                coachMark = CoachMark2(it)
                coachMark?.isOutsideTouchable = true
                coachMark?.showCoachMark(coachMarkList)
                ChooseAddressUtils.coachMarkLocalizingAddressAlreadyShown(it)
            }
        }
    }

    private fun getChooseAddressWidgetCoachMarkItem(): CoachMark2Item? {
        val isNeedToShowCoachMark = ChooseAddressUtils.isLocalizingAddressNeedShowCoachMark(requireContext())
        return if (isNeedToShowCoachMark == true && chooseAddressWidget?.isShown == true) {
            chooseAddressWidget?.let { chooseAddressWidget ->
                context?.getString(R.string.dt_home_choose_address_widget_coachmark_title)?.let { title ->
                    CoachMark2Item(
                        chooseAddressWidget,
                        title,
                        getString(R.string.dt_home_choose_address_widget_coachmark_description)
                    )
                }
            }
        } else {
            return null
        }
    }

    private fun initRecyclerScrollListener() {
        rvHome?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                /**
                 *  maximize anchor when scroll up && reach top
                 */
                if (dy >= 0) {
                    if (statusBarState == AnchorTabStatus.MAXIMIZE && recyclerView.scrollState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        setAnchorTabMinimize()
                    }
                }

                updateAnchorTabWhenScroll()

                // for recommendation scroll
                evaluateHomeComponentOnScroll(recyclerView)
                lastSection()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                /**
                 *  minimize anchor when scroll down
                 */
                if (!recyclerView.canScrollVertically(-1)) {
                    if (statusBarState == AnchorTabStatus.MINIMIZE) {
                        setAnchorTabMaximize()
                    }
                }
            }
        })
    }

    private fun lastSection() {
        val totalItemCount = linearLayoutManager?.itemCount ?: 0
        val lastVisible = linearLayoutManager?.findLastVisibleItemPosition()
        val listIsRecommendationForYou = viewModelDtHome.isLastWidgetIsRecommendationForYou()
        val endHasBeenReached = (lastVisible?.plus(1) ?: 0) >= totalItemCount
        if (totalItemCount > 0 && endHasBeenReached && listIsRecommendationForYou == true) {
            // you have reached to the bottom of your recycler view
            binding?.headerCompHolder?.gone()
            binding?.chooseAddressWidget?.gone()
        } else {
            binding?.headerCompHolder?.visible()
            binding?.chooseAddressWidget?.visible()
        }
    }

    private fun evaluateHomeComponentOnScroll(recyclerView: RecyclerView) { // set refresh layout to only enabled when reach 0 offset

        /**
         * because later we will disable scroll up for this parent recyclerview
         * and makes refresh layout think we can't scroll up (which actually can! we only disable
         * scroll so that feed recommendation section can scroll its content)
         */
        if (recyclerView.computeVerticalScrollOffset() == VERTICAL_SCROLL_FULL_BOTTOM_OFFSET) {
            refreshLayout?.setCanChildScrollUp(false)
        } else {
            refreshLayout?.setCanChildScrollUp(true)
        }
        if (recyclerView.canScrollVertically(RV_DIRECTION_TOP)) {
            navToolbar?.showShadow(lineShadow = true)
            rvHome?.setNestedCanScroll(false)
        } else {
            // home feed now can scroll up, so hide maintoolbar shadow
            navToolbar?.hideShadow(lineShadow = true)
            rvHome?.setNestedCanScroll(true)
        }
    }

    private fun updateAnchorTabWhenScroll() {
        val visiblePosition = rvLayoutManager?.findFirstCompletelyVisibleItemPosition()

        /**
         * get visitable position from list adapter
         * get position from anchor tab using visitable
         * select and scroll tab anchor from pisition
         */
        if (visiblePosition != null && visiblePosition != -1 && viewModelDtHome.getHomeVisitableList().isNotEmpty()) {
            val anchorTabUiModel = viewModelDtHome.getAnchorTabByVisitablePosition(visiblePosition)
            val indexAnchorTab = viewModelDtHome.menuList.value?.indexOf(anchorTabUiModel)

            if (anchorTabUiModel != null && indexAnchorTab != null) {
                smoothScrollAnchorTab(indexAnchorTab)
                anchorTabAdapter?.selectMenu(anchorTabUiModel)
            }
        }
    }

    private fun smoothScrollAnchorTab(position: Int) {
        val smoothScroller: RecyclerView.SmoothScroller =
            object : LinearSmoothScroller(context) {
                override fun getHorizontalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
        smoothScroller.targetPosition = position
        anchorTabLinearLayoutManager?.startSmoothScroll(smoothScroller)
    }

    private fun smoothScrollToComponentWithPosition(position: Int) {
        val smoothScroller: RecyclerView.SmoothScroller =
            object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
        smoothScroller.targetPosition = position
        linearLayoutManager?.startSmoothScroll(smoothScroller)
    }

    private fun setAnchorTabMaximize() {
        anchorTabAdapter?.setMaximizeIcons()

        val transparentUnify = android.R.color.transparent
        val transparentColor = ResourcesCompat.getColor(requireContext().resources, transparentUnify, null)
        navToolbar?.setBackgroundColor(transparentColor)
        binding?.headerCompHolder?.setBackgroundColor(transparentColor)
        binding?.chooseAddressWidget?.setBackgroundColor(transparentColor)
        binding?.dtViewBackgroundImage?.visible()

        statusBarState = AnchorTabStatus.MAXIMIZE
    }

    private fun setAnchorTabMinimize() {
        anchorTabAdapter?.setMinimizeIcons()

        val whiteUnify = com.tokopedia.unifyprinciples.R.color.Unify_NN0
        val whiteColor = ResourcesCompat.getColor(requireContext().resources, whiteUnify, null)
        navToolbar?.setBackgroundColor(whiteColor)
        binding?.headerCompHolder?.setBackgroundColor(whiteColor)
        binding?.chooseAddressWidget?.setBackgroundColor(whiteColor)
        binding?.dtViewBackgroundImage?.gone()
        statusBarState = AnchorTabStatus.MINIMIZE
    }

    private fun showHeaderBackground() {
        context?.resources?.apply {
            val background = VectorDrawableCompat.create(
                this,
                R.drawable.dt_ic_header_background,
                context?.theme
            )
            ivHeaderBackground?.setImageDrawable(background)
            ivHeaderBackground?.show()
        }
    }

    override fun screenShotTaken() {
        updateShareHomeData(
            pageIdConstituents = listOf("home"),
            isScreenShot = true,
            linkerType = "Dilayani-tokopedia"
        )

        showUniversalShareBottomSheet(shareHome)
    }

    override fun permissionAction(action: String, label: String) {
        // no-op
    }

    private fun createRecommendationCallback(): DtHomeCategoryListener {
        return object : DtHomeCategoryListener {
            override val windowHeight: Int
                get() = if (activity != null) {
                    root?.height ?: 0
                } else {
                    0
                }
            override val homeMainToolbarHeight: Int
                get() {
                    var height = requireActivity().resources.getDimensionPixelSize(R.dimen.default_toolbar_status_height)
                    context?.let { context ->
                        navToolbar?.let {
                            height = navToolbar?.height
                                ?: context.resources.getDimensionPixelSize(R.dimen.default_toolbar_status_height)
                            height += 8f.toDpInt()
                        }
                    }
                    return height
                }
            override val homeMainAnchorTabHeight: Int
                get() {
                    return binding?.headerCompHolder?.height ?: 0
                }
        }
    }
}
