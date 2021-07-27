package com.tokopedia.tokopedianow.home.presentation.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.media.loader.loadImage
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.stickylogin.common.StickyLoginConstant
import com.tokopedia.stickylogin.view.StickyLoginAction
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.common.constant.ConstantKey.AB_TEST_AUTO_TRANSITION_KEY
import com.tokopedia.tokopedianow.common.constant.ConstantKey.AB_TEST_EXP_NAME
import com.tokopedia.tokopedianow.common.constant.ConstantKey.AB_TEST_VARIANT_OLD
import com.tokopedia.tokopedianow.common.constant.ConstantKey.PARAM_APPLINK_AUTOCOMPLETE
import com.tokopedia.tokopedianow.common.constant.ConstantKey.REMOTE_CONFIG_KEY_FIRST_DURATION_TRANSITION_SEARCH
import com.tokopedia.tokopedianow.common.constant.ConstantKey.REMOTE_CONFIG_KEY_FIRST_INSTALL_SEARCH
import com.tokopedia.tokopedianow.common.constant.ConstantKey.SHARED_PREFERENCES_KEY_FIRST_INSTALL_SEARCH
import com.tokopedia.tokopedianow.common.constant.ConstantKey.SHARED_PREFERENCES_KEY_FIRST_INSTALL_TIME_SEARCH
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.common.util.CustomLinearLayoutManager
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowCategoryGridViewHolder
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE
import com.tokopedia.tokopedianow.home.di.component.DaggerHomeComponent
import com.tokopedia.tokopedianow.home.domain.model.Data
import com.tokopedia.tokopedianow.home.domain.model.SearchPlaceholder
import com.tokopedia.tokopedianow.home.domain.model.ShareHomeTokonow
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapter
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapterTypeFactory
import com.tokopedia.tokopedianow.home.presentation.adapter.differ.HomeListDiffer
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeProductRecomViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeTickerViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_SHORT
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_tokopedianow_home.*

import java.util.*
import javax.inject.Inject

class TokoNowHomeFragment: Fragment(),
        TokoNowView,
        HomeChooseAddressWidgetViewHolder.HomeChooseAddressWidgetListener,
        HomeTickerViewHolder.HomeTickerListener,
        TokoNowCategoryGridViewHolder.TokoNowCategoryGridListener,
        MiniCartWidgetListener,
        BannerComponentListener,
        HomeProductRecomViewHolder.HomeProductRecomListener,
        ShareBottomsheetListener
{

    companion object {
        private const val AUTO_TRANSITION_VARIANT = "auto_transition"
        private const val DEFAULT_INTERVAL_HINT: Long = 1000 * 10
        private const val REQUEST_CODE_LOGIN_STICKY_LOGIN = 130
        const val CATEGORY_LEVEL_DEPTH = 1
        const val SOURCE = "tokonow"
        const val SOURCE_TRACKING = "tokonow page"

        const val SHARE_URL = "https://www.tokopedia.com/now"
        const val THUMBNAIL_IMAGE_SHARE_URL = "https://images.tokopedia.net/img/thumbnail_now_home.png"
        const val OG_IMAGE_SHARE_URL = "https://images.tokopedia.net/img/og_now_home.jpg"
        const val PAGE_SHARE_NAME = "TokoNow"
        const val SHARE = "Share"
        const val MAIN_SHOP_ID = "11530573"

        fun newInstance() = TokoNowHomeFragment()
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelTokoNow: TokoNowHomeViewModel

    @Inject
    lateinit var analytics: HomeAnalytics

    private val adapter by lazy {
        HomeAdapter(
            typeFactory = HomeAdapterTypeFactory(
                tokoNowListener = this,
                homeTickerListener = this,
                homeChooseAddressWidgetListener = this,
                tokoNowCategoryGridListener = this,
                bannerComponentListener = this,
                homeProductRecomListener = this
            ),
            differ = HomeListDiffer()
        )
    }

    private var navToolbar: NavToolbar? = null
    private var statusBarBackground: View? = null
    private var localCacheModel: LocalCacheModel? = null
    private var ivHeaderBackground: ImageView? = null
    private var swipeLayout: SwipeRefreshLayout? = null
    private var sharedPrefs: SharedPreferences? = null
    private var rvLayoutManager: CustomLinearLayoutManager? = null
    private var isShowFirstInstallSearch = false
    private var durationAutoTransition = DEFAULT_INTERVAL_HINT
    private var movingPosition = 0
    private var isVariantAdded = false
    private var isFirstImpressionOnBanner = false
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null

    private val homeMainToolbarHeight: Int
        get() {
            val defaultHeight = resources.getDimensionPixelSize(
                R.dimen.tokopedianow_default_toolbar_status_height)
            val height = (navToolbar?.height ?: defaultHeight)
            val padding = resources.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)

            return height + padding
        }
    private val spaceZero: Int
        get() = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0).toInt()

    private var shareHomeTokonow: ShareHomeTokonow? = null

    private val loadMoreListener by lazy { createLoadMoreListener() }
    private val navBarScrollListener by lazy { createNavBarScrollListener() }
    private val homeComponentScrollListener by lazy { createHomeComponentScrollListener() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firebaseRemoteConfig = FirebaseRemoteConfigImpl(activity)
        firebaseRemoteConfig.let {
            isShowFirstInstallSearch = it.getBoolean(REMOTE_CONFIG_KEY_FIRST_INSTALL_SEARCH, false)
            durationAutoTransition = it.getLong(REMOTE_CONFIG_KEY_FIRST_DURATION_TRANSITION_SEARCH, DEFAULT_INTERVAL_HINT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shareHomeTokonow()
        setupNavToolbar()
        stickyLoginSetup()
        setupStatusBar()
        setupRecyclerView()
        setupSwipeRefreshLayout()
        observeLiveData()
        updateCurrentPageLocalCacheModelData()

        loadLayout()
    }

    override fun getFragmentPage(): Fragment = this

    override fun getFragmentManagerPage(): FragmentManager = childFragmentManager

    override fun refreshLayoutPage() = onRefreshLayout()

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        checkIfChooseAddressWidgetDataUpdated()
        getMiniCart()
    }

    override fun onTickerDismissed(id: String) {
        viewModelTokoNow.removeTickerWidget(id)
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if (!miniCartSimplifiedData.isShowMiniCartWidget) {
            miniCartWidget?.hide()
        }
        updateProductRecom(miniCartSimplifiedData)
        setupPadding(miniCartSimplifiedData)
    }

    private fun updateProductRecom(miniCartSimplifiedData: MiniCartSimplifiedData) {
        viewModelTokoNow.setMiniCartSimplifiedData(miniCartSimplifiedData)
        viewModelTokoNow.updateProductCard(miniCartSimplifiedData, true)
    }

    override fun onBannerClickListener(position: Int, channelGrid: ChannelGrid, channelModel: ChannelModel) {
        analytics.onClickBannerPromo(position, userSession.userId, channelModel, channelGrid)
        context?.let {
            RouteManager.route(it, channelGrid.applink)
        }
    }

    override fun onChooseAddressWidgetRemoved() {
        if(rvHome?.isComputingLayout == false) {
            adapter.removeHomeChooseAddressWidget()
        }
    }

    override fun onCategoryRetried() {
        val item = adapter.getItem(TokoNowCategoryGridUiModel::class.java)
        if (item is TokoNowCategoryGridUiModel) {
            viewModelTokoNow.getCategoryGrid(item, localCacheModel?.warehouse_id.orEmpty())
        }
    }

    override fun onAllCategoryClicked() {
        analytics.onClickAllCategory()
    }

    override fun onCategoryClicked(position: Int, categoryId: String) {
        analytics.onClickCategory(position, userSession.userId, categoryId)
    }

    override fun onProductRecomNonVariantClick(recomItem: RecommendationItem, quantity: Int) {
        if (userSession.isLoggedIn) {
            viewModelTokoNow.addProductToCart(recomItem, quantity)
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    override fun isMainViewVisible(): Boolean = true

    override fun isBannerImpressed(id: String): Boolean = true

    override fun onPromoScrolled(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {}

    override fun onPageDragStateChanged(isDrag: Boolean) {}

    override fun onPromoAllClick(channelModel: ChannelModel) {}

    override fun onChannelBannerImpressed(channelModel: ChannelModel, parentPosition: Int) {
        if (!isFirstImpressionOnBanner) {
            isFirstImpressionOnBanner = true
        } else {
            analytics.onImpressBannerPromo(userSession.userId, channelModel)
            isFirstImpressionOnBanner = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        AtcVariantHelper.onActivityResultAtcVariant(requireContext(), requestCode, data) {
            isVariantAdded = true
        }
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_LOGIN_STICKY_LOGIN -> {
                stickyLoginLoadContent()
            }
        }
    }

    private fun initInjector() {
        DaggerHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun checkStateNotInServiceArea(shopId: Long = -1L, warehouseId: Long) {
        context?.let {
            when {
                shopId == 0L -> {
                    viewModelTokoNow.getChooseAddress(SOURCE)
                }
                warehouseId == 0L -> {
                    showEmptyStateNoAddress()
                }
                else -> {
                    showLayout()
                }
            }
        }
    }

    private fun showEmptyState(id: String) {
        rvLayoutManager?.setScrollEnabled(false)
        viewModelTokoNow.getEmptyState(id)
        miniCartWidget?.hide()
    }

    private fun showFailedToFetchData() {
        showEmptyState(EMPTY_STATE_FAILED_TO_FETCH_DATA)
    }

    private fun showEmptyStateNoAddress() {
        if (localCacheModel?.city_id?.isBlank() == true && localCacheModel?.district_id?.isBlank() == true) {
            showEmptyState(EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE)
        } else {
            showEmptyState(EMPTY_STATE_NO_ADDRESS)
        }
    }

    private fun showLayout() {
        getHomeLayout()
        getMiniCart()
    }

    private fun loadHeaderBackground() {
        ivHeaderBackground?.show()
        ivHeaderBackground?.loadImage(R.drawable.tokopedianow_ic_header_background_shimmering)
    }

    private fun showHeaderBackground() {
        ivHeaderBackground?.show()
        ivHeaderBackground?.loadImage(R.drawable.tokopedianow_ic_header_background)
    }

    private fun hideHeaderBackground() {
        ivHeaderBackground?.hide()
    }

    private fun setupSwipeRefreshLayout() {
        context?.let {
            swipeLayout = view?.findViewById(R.id.swipe_refresh_layout)
            swipeLayout?.setMargin(spaceZero, NavToolbarExt.getFullToolbarHeight(it), spaceZero, spaceZero)
            swipeLayout?.setOnRefreshListener {
                onRefreshLayout()
            }
        }
    }

    private fun onRefreshLayout() {
        resetMovingPosition()
        removeAllScrollListener()
        hideStickyLogin()
        rvLayoutManager?.setScrollEnabled(true)
        loadLayout()
    }

    private fun setupNavToolbar() {
        ivHeaderBackground = view?.findViewById(R.id.view_background_image)
        navToolbar = view?.findViewById(R.id.navToolbar)
        setupTopNavigation()
        navAbTestCondition (
                ifNavRevamp = {
                    setIconNewTopNavigation()
                },
                ifNavOld = {
                    setIconOldTopNavigation()
                }
        )
    }

    private fun setupTopNavigation() {
        navToolbar?.let { toolbar ->
            viewLifecycleOwner.lifecycle.addObserver(toolbar)
            //  because searchHint has not been discussed so for current situation we only use hardcoded placeholder
            setHint(SearchPlaceholder(Data(null, resources.getString(R.string.tokopedianow_search_bar_hint),"")))
            addNavBarScrollListener()
            activity?.let {
                toolbar.setupToolbarWithStatusBar(it)
            }
        }
    }

    private fun setIconNewTopNavigation() {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
                .addIcon(IconList.ID_SHARE, onClick = ::onClickShareButton)
                .addIcon(IconList.ID_CART, onClick = ::onClickCartButton)
                .addIcon(IconList.ID_NAV_GLOBAL) {}
        navToolbar?.setIcon(icons)
    }

    private fun setIconOldTopNavigation() {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
                .addIcon(IconList.ID_SHARE, onClick = ::onClickShareButton)
                .addIcon(IconList.ID_CART, onClick = ::onClickCartButton)
        navToolbar?.setIcon(icons)
    }

    private fun onClickCartButton() {
        analytics.onClickCartButton()
    }

    private fun onClickShareButton() {
        shareIconClicked(shareHomeTokonow)
    }

    private fun evaluateHomeComponentOnScroll(recyclerView: RecyclerView, dy: Int) {
        movingPosition += dy
        ivHeaderBackground?.y = if(movingPosition >= 0) {
            -(movingPosition.toFloat())
        } else {
            resetMovingPosition()
            movingPosition.toFloat()
        }
        if (recyclerView.canScrollVertically(1) || movingPosition != 0) {
            navToolbar?.showShadow(lineShadow = true)
        } else {
            navToolbar?.hideShadow(lineShadow = true)
        }
    }

    private fun isNavOld(): Boolean {
        return try {
            getAbTestPlatform().getString(AB_TEST_EXP_NAME, AB_TEST_VARIANT_OLD) == AB_TEST_VARIANT_OLD
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }

    private fun getAbTestPlatform(): AbTestPlatform {
        val remoteConfigInstance = RemoteConfigInstance(activity?.application)
        return remoteConfigInstance.abTestPlatform
    }

    private fun navAbTestCondition(ifNavRevamp: () -> Unit = {}, ifNavOld: () -> Unit = {}) {
        if (!isNavOld()) {
            ifNavRevamp.invoke()
        } else {
            ifNavOld.invoke()
        }
    }

    private fun setupStatusBar() {
        /*
            this status bar background only shows for android Kitkat below
            In that version, status bar can't be forced to dark mode
            We must set background to keep status bar icon visible
        */
        statusBarBackground = view?.findViewById(R.id.status_bar_bg)
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

    private fun setupRecyclerView() {
        context?.let {
            with(rvHome) {
                adapter = this@TokoNowHomeFragment.adapter
                rvLayoutManager = CustomLinearLayoutManager(it)
                layoutManager = rvLayoutManager
            }

            rvHome?.setItemViewCacheSize(20)
            addHomeComponentScrollListener()
        }
    }

    private fun observeLiveData() {
        observe(viewModelTokoNow.homeLayoutList) {
            removeAllScrollListener()

            if (it is Success) {
                loadHomeLayout(it.data)
            } else {
                showFailedToFetchData()
            }

            rvHome?.post {
                addScrollListener()
                resetSwipeLayout()
                stickyLoginLoadContent()
            }
        }

        observe(viewModelTokoNow.miniCart) {
            if(it is Success) {
                setupMiniCart(it.data)
                setupPadding(it.data)
                if (isVariantAdded) {
                    updateProductRecom(it.data)
                    isVariantAdded = false
                }
            }
        }

        observe(viewModelTokoNow.chooseAddress) {
            if (it is Success) {
                it.data.let { chooseAddressData ->
                    ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                            context = requireContext(),
                            addressId = chooseAddressData.data.addressId.toString(),
                            cityId = chooseAddressData.data.cityId.toString(),
                            districtId = chooseAddressData.data.districtId.toString(),
                            lat = chooseAddressData.data.latitude,
                            long = chooseAddressData.data.longitude,
                            label = String.format("%s %s", chooseAddressData.data.addressName, chooseAddressData.data.receiverName),
                            postalCode = chooseAddressData.data.postalCode,
                            warehouseId = chooseAddressData.tokonow.warehouseId.toString(),
                            shopId = chooseAddressData.tokonow.shopId.toString()
                    )
                }
                checkIfChooseAddressWidgetDataUpdated()
                checkStateNotInServiceArea(
                        warehouseId = it.data.tokonow.warehouseId
                )
            } else {
                showEmptyStateNoAddress()
            }
        }

        observe(viewModelTokoNow.miniCartWidgetDataUpdated) {
            miniCartWidget?.updateData(it)
        }

        observe(viewModelTokoNow.miniCartAdd) {
            when(it) {
                is Success -> {
                    showToaster(
                        message = it.data.errorMessage.joinToString(separator = ", "),
                        type = TYPE_NORMAL
                    )
                    adapter.updateProductRecom(it.data.data.productId, it.data.data.quantity)
                    getMiniCart()
                }
                is Fail -> {
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = TYPE_ERROR
                    )
                }
            }
        }

        observe(viewModelTokoNow.miniCartUpdate) {
            when(it) {
                is Success -> {
                    val shopIds = listOf(localCacheModel?.shop_id.orEmpty())
                    miniCartWidget?.updateData(shopIds)
                }
                is Fail -> {
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = TYPE_ERROR
                    )
                }
            }
        }

        observe(viewModelTokoNow.miniCartRemove) {
            when(it) {
                is Success -> {
                    adapter.updateProductRecom(it.data.toLong(), 0)
                    getMiniCart()
                }
                is Fail -> {
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = TYPE_ERROR
                    )
                }
            }
        }
    }

    private fun showToaster(message: String, duration: Int = LENGTH_SHORT, type: Int) {
        view?.let { view ->
            Toaster.build(
                view = view,
                text = message,
                duration = duration,
                type = type
            ).show()
        }
    }

    private fun resetSwipeLayout() {
        swipeLayout?.isEnabled = true
        swipeLayout?.isRefreshing = false
    }

    private fun stickyLoginSetup(){
        sticky_login_tokonow?.let {
            it.page = StickyLoginConstant.Page.HOME
            it.lifecycleOwner = viewLifecycleOwner
            it.setStickyAction(object : StickyLoginAction {
                override fun onClick() {
                    context?.let {
                        val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
                        startActivityForResult(intent, REQUEST_CODE_LOGIN_STICKY_LOGIN)
                    }
                }

                override fun onDismiss() {
                }

                override fun onViewChange(isShowing: Boolean) {
                }
            })
        }
        hideStickyLogin()
    }

    private fun stickyLoginLoadContent(){
        sticky_login_tokonow.loadContent()
    }

    private fun hideStickyLogin(){
        sticky_login_tokonow.hide()
    }

    private fun resetMovingPosition() {
        movingPosition = 0
    }

    private fun setupMiniCart(data: MiniCartSimplifiedData) {
        if(data.isShowMiniCartWidget) {
            val shopIds = listOf(localCacheModel?.shop_id.orEmpty())
            miniCartWidget?.initialize(shopIds, this, this, pageName = MiniCartAnalytics.Page.HOME_PAGE)
            miniCartWidget?.show()
            hideStickyLogin()
        } else {
            miniCartWidget?.hide()
        }
    }

    private fun setupPadding(data: MiniCartSimplifiedData) {
        miniCartWidget?.post {
            val paddingBottom = if (data.isShowMiniCartWidget) {
                miniCartWidget?.height.orZero()
            } else {
                activity?.resources?.getDimensionPixelSize(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).orZero()
            }
            swipeLayout?.setPadding(0, 0, 0, paddingBottom)
        }
    }

    private fun loadHomeLayout(data: HomeLayoutListUiModel) {
        when (data.state) {
            TokoNowLayoutState.SHOW -> onShowHomeLayout(data)
            TokoNowLayoutState.HIDE -> onHideHomeLayout(data)
            TokoNowLayoutState.LOADING -> onLoadingHomeLayout(data)
            TokoNowLayoutState.LOAD_MORE -> {
                rvHome?.post {
                    showHomeLayout(data)
                }
            }
        }
    }

    private fun onLoadingHomeLayout(data: HomeLayoutListUiModel) {
        showHomeLayout(data)
        loadHeaderBackground()
        checkAddressDataAndServiceArea()
        if (!isChooseAddressWidgetShowed()) {
            adapter.removeHomeChooseAddressWidget()
        }
    }

    private fun onHideHomeLayout(data: HomeLayoutListUiModel) {
        showHomeLayout(data)
        hideHeaderBackground()
    }

    private fun onShowHomeLayout(data: HomeLayoutListUiModel) {
        val initialLoad = data.isInitialLoad
        val isLoadDataFinished = data.isLoadDataFinished
        showHomeLayout(data)

        when {
            initialLoad -> {
                showHeaderBackground()
                loadNextItem(data)
            }
            isLoadDataFinished -> {
                rvHome?.post {
                    addLoadMoreListener()
                }
            }
            !isLoadDataFinished -> loadNextItem(data)
        }
    }

    private fun loadNextItem(data: HomeLayoutListUiModel) {
        rvHome?.post {
            val index = data.nextItemIndex
            loadVisibleLayoutData(index)
        }
    }

    private fun checkAddressDataAndServiceArea() {
        checkIfChooseAddressWidgetDataUpdated()
        val shopId = localCacheModel?.shop_id.toLongOrZero()
        val warehouseId = localCacheModel?.warehouse_id.toLongOrZero()
        checkStateNotInServiceArea(shopId = shopId, warehouseId = warehouseId)
    }

    private fun showHomeLayout(data: HomeLayoutListUiModel) {
        val items = data.result.map { it.layout }
        adapter.submitList(items)
    }

    private fun addLoadMoreListener() {
        rvHome?.addOnScrollListener(loadMoreListener)
    }

    private fun removeLoadMoreListener() {
        rvHome?.removeOnScrollListener(loadMoreListener)
    }

    private fun addNavBarScrollListener() {
        navBarScrollListener?.let {
            rvHome?.addOnScrollListener(it)
        }
    }

    private fun removeNavBarScrollListener() {
        navBarScrollListener?.let {
            rvHome?.removeOnScrollListener(it)
        }
    }

    private fun addHomeComponentScrollListener() {
        rvHome?.addOnScrollListener(homeComponentScrollListener)
    }

    private fun removeHomeComponentScrollListener() {
        rvHome?.removeOnScrollListener(homeComponentScrollListener)
    }

    private fun removeAllScrollListener() {
        removeLoadMoreListener()
        removeNavBarScrollListener()
        removeHomeComponentScrollListener()
    }

    private fun addScrollListener() {
        addNavBarScrollListener()
        addHomeComponentScrollListener()
    }

    private fun loadVisibleLayoutData(index: Int?) {
        val warehouseId = localCacheModel?.warehouse_id.orEmpty()
        val layoutManager = rvHome.layoutManager as? LinearLayoutManager
        val firstVisibleItemIndex = layoutManager?.findFirstVisibleItemPosition().orZero()
        val lastVisibleItemIndex = layoutManager?.findLastVisibleItemPosition().orZero()
        viewModelTokoNow.getLayoutData(index, warehouseId, firstVisibleItemIndex, lastVisibleItemIndex)
    }

    private fun loadMoreLayoutData() {
        val warehouseId = localCacheModel?.warehouse_id.orEmpty()
        val layoutManager = rvHome.layoutManager as? LinearLayoutManager
        val firstVisibleItemIndex = layoutManager?.findFirstVisibleItemPosition().orZero()
        val lastVisibleItemIndex = layoutManager?.findLastVisibleItemPosition().orZero()
        viewModelTokoNow.getMoreLayoutData(warehouseId, firstVisibleItemIndex, lastVisibleItemIndex)
    }

    private fun getHomeLayout() {
        viewModelTokoNow.getHomeLayout()
    }

    private fun getMiniCart()  {
        val shopId = listOf(localCacheModel?.shop_id.orEmpty())
        val warehouseId = localCacheModel?.warehouse_id
        viewModelTokoNow.getMiniCart(shopId, warehouseId)
    }

    private fun loadLayout() {
        viewModelTokoNow.getLoadingState()
    }

    //  because searchHint has not been discussed so for current situation we only use hardcoded placeholder
    private fun getSearchHint() {
        viewModelTokoNow.getKeywordSearch(isFirstInstall(), userSession.deviceId, userSession.userId)
    }

    private fun checkIfChooseAddressWidgetDataUpdated() {
        localCacheModel?.let {
            context?.apply {
                val isUpdated = ChooseAddressUtils.isLocalizingAddressHasUpdated(
                        this,
                        it
                )
                if (isUpdated) {
                    updateCurrentPageLocalCacheModelData()
                }
            }
        }
    }

    private fun isChooseAddressWidgetShowed(): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val isRollOutUser = ChooseAddressUtils.isRollOutUser(context)
        val isRemoteConfigChooseAddressWidgetEnabled = remoteConfig.getBoolean(
                HomeChooseAddressWidgetViewHolder.ENABLE_CHOOSE_ADDRESS_WIDGET,
                true
        )
        return isRollOutUser && isRemoteConfigChooseAddressWidgetEnabled
    }

    private fun updateCurrentPageLocalCacheModelData() {
        context?.let {
            localCacheModel = ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private fun isFirstInstall(): Boolean {
        context?.let {
            if (!userSession.isLoggedIn && isShowFirstInstallSearch) {
                val sharedPrefs = it.getSharedPreferences(SHARED_PREFERENCES_KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
                var firstInstallCacheValue = sharedPrefs.getLong(SHARED_PREFERENCES_KEY_FIRST_INSTALL_TIME_SEARCH, 0)
                if (firstInstallCacheValue == 0L) return false
                firstInstallCacheValue += (30 * 60000).toLong()
                val now = Date()
                val firstInstallTime = Date(firstInstallCacheValue)
                return if (now <= firstInstallTime) {
                    true
                } else {
                    saveFirstInstallTime()
                    false
                }
            } else {
                return false
            }
        }
        return false
    }

    private fun saveFirstInstallTime() {
        context?.let {
            sharedPrefs = it.getSharedPreferences(SHARED_PREFERENCES_KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
            sharedPrefs?.edit()?.putLong(SHARED_PREFERENCES_KEY_FIRST_INSTALL_TIME_SEARCH, 0)?.apply()
        }
    }

    private fun setHint(searchPlaceholder: SearchPlaceholder) {
        searchPlaceholder.data?.let { data ->
            navToolbar?.setupSearchbar(
                    hints = listOf(
                            HintData(
                                    data.placeholder.orEmpty(),
                                    data.keyword.orEmpty()
                            )
                    ),
                    searchbarClickCallback = ::onSearchBarClick,
                    searchbarImpressionCallback = {},
                    durationAutoTransition = durationAutoTransition,
                    shouldShowTransition = shouldShowTransition()
            )
        }
    }

    private fun onSearchBarClick(hint: String) {
        analytics.onClickSearchBar()
        RouteManager.route(context,
                getAutoCompleteApplinkPattern(),
                SOURCE,
                resources.getString(R.string.tokopedianow_search_bar_hint),
                isFirstInstall().toString())
    }

    private fun getAutoCompleteApplinkPattern() =
            ApplinkConstInternalDiscovery.AUTOCOMPLETE +
                    PARAM_APPLINK_AUTOCOMPLETE +
                    "&" + getParamTokonowSRP()

    private fun getParamTokonowSRP() =
            "${SearchApiConst.BASE_SRP_APPLINK}=${ApplinkConstInternalTokopediaNow.SEARCH}"

    private fun shouldShowTransition(): Boolean {
        val abTestValue = getAbTestPlatform().getString(AB_TEST_AUTO_TRANSITION_KEY, "")
        return abTestValue == AUTO_TRANSITION_VARIANT
    }

    private fun String?.safeEncodeUtf8(): String {
        return try {
            this?.encodeToUtf8().orEmpty()
        } catch (throwable: Throwable) {
            ""
        }
    }

    private fun createNavBarScrollListener(): NavRecyclerViewScrollListener? {
        return navToolbar?.let { toolbar ->
            NavRecyclerViewScrollListener(
                navToolbar = toolbar,
                startTransitionPixel = homeMainToolbarHeight,
                toolbarTransitionRangePixel = resources.getDimensionPixelSize(R.dimen.tokopedianow_searchbar_transition_range),
                navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                    override fun onAlphaChanged(offsetAlpha: Float) { /* nothing to do */
                    }

                    override fun onSwitchToLightToolbar() { /* nothing to do */
                    }

                    override fun onSwitchToDarkToolbar() {
                        navToolbar?.hideShadow()
                    }

                    override fun onYposChanged(yOffset: Int) {}
                },
                fixedIconColor = NavToolbar.Companion.Theme.TOOLBAR_LIGHT_TYPE
            )
        }
    }

    private fun createHomeComponentScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                evaluateHomeComponentOnScroll(recyclerView, dy)
            }
        }
    }

    private fun createLoadMoreListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                loadMoreLayoutData()
            }
        }
    }

    private fun shareIconClicked(shareHomeTokonow: ShareHomeTokonow?){
        if(UniversalShareBottomSheet.isCustomSharingEnabled(context)){
            showUniversalShareBottomSheet(shareHomeTokonow)
        }
        else {
            LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(0,
                    linkerDataMapper(shareHomeTokonow), object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult) {
                            if (linkerShareData.url != null) {
                                shareData(
                                    activity,
                                    String.format("%s %s", shareHomeTokonow?.sharingText,
                                            linkerShareData.shareUri),
                                    linkerShareData.url
                                )
                            }
                        }

                        override fun onError(linkerError: LinkerError) {
                            shareData(activity, shareHomeTokonow?.sharingText ?: "",
                                    shareHomeTokonow?.sharingUrl ?: "")
                        }
                    })
            )
        }
    }

    fun shareData(context: Context?, shareTxt: String?, pageUri: String?) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, shareTxt + "\n" + pageUri)
        context?.startActivity(Intent.createChooser(share, shareTxt))
    }

    private fun showUniversalShareBottomSheet(shareHomeTokonow: ShareHomeTokonow?) {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@TokoNowHomeFragment)
            setUtmCampaignData(PAGE_SHARE_NAME, shareHomeTokonow?.userId ?: "", shareHomeTokonow?.pageId ?: "", SHARE)
            setMetaData(
                shareHomeTokonow?.thumbNailTitle ?: "", shareHomeTokonow?.thumbNailImage ?: ""
            )
            //set the Image Url of the Image that represents page
            setOgImageUrl(shareHomeTokonow?.ogImageUrl ?: "")
        }
        universalShareBottomSheet?.show(fragmentManager)
    }

    private fun linkerDataMapper(shareHomeTokonow: ShareHomeTokonow?): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.id = shareHomeTokonow?.pageId ?: ""
        linkerData.name = shareHomeTokonow?.specificPageName ?: ""
        linkerData.uri = shareHomeTokonow?.sharingUrl ?: ""
        linkerData.description = shareHomeTokonow?.specificPageDescription ?: ""
        linkerData.isThrowOnError = true
        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }

    private fun shareHomeTokonow(){
        shareHomeTokonow = ShareHomeTokonow(
                resources.getString(R.string.tokopedianow_home_share_main_text),
                SHARE_URL,
                userSession.userId,
                MAIN_SHOP_ID,
                resources.getString(R.string.tokopedianow_home_share_thumbnail_title),
                THUMBNAIL_IMAGE_SHARE_URL,
                OG_IMAGE_SHARE_URL,
                resources.getString(R.string.tokopedianow_home_share_title),
                resources.getString(R.string.tokopedianow_home_share_desc),
        )
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        val linkerShareData = linkerDataMapper(shareHomeTokonow)
        linkerShareData.linkerData.apply {
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            if(shareModel.ogImgUrl != null && shareModel.ogImgUrl!!.isNotEmpty()) {
                ogImageUrl = shareModel.ogImgUrl
            }
        }
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    val shareString = String.format("%s %s", shareHomeTokonow?.sharingText, linkerShareData?.shareUri)
                    SharingUtil.executeShareIntent(shareModel, linkerShareData, activity, view, shareString)
                    universalShareBottomSheet?.dismiss()
                }

                override fun onError(linkerError: LinkerError?) {}
            })
        )
    }

    override fun onCloseOptionClicked() {
        //you will use this to implement the GA events
    }
}