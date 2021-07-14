package com.tokopedia.tokopedianow.home.presentation.fragment

import android.content.Context
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
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.media.loader.loadImage
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
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
import com.tokopedia.tokopedianow.common.util.CustomLinearLayoutManager
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokopedianow.home.di.component.DaggerHomeComponent
import com.tokopedia.tokopedianow.home.domain.model.Data
import com.tokopedia.tokopedianow.home.domain.model.SearchPlaceholder
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapter
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapterTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowCategoryGridViewHolder
import com.tokopedia.tokopedianow.home.presentation.adapter.differ.HomeListDiffer
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeProductRecomViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeTickerViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_SHORT
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.usecase.coroutines.Fail
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
        HomeProductRecomViewHolder.HomeProductRecomListener
{

    companion object {
        private const val AUTO_TRANSITION_VARIANT = "auto_transition"
        private const val DEFAULT_INTERVAL_HINT: Long = 1000 * 10
        const val CATEGORY_LEVEL_DEPTH = 1
        const val SOURCE = "tokonow"
        const val SOURCE_TRACKING = "tokonow page"

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
    private var hasTickerBeenRemoved: Boolean = false
    private var isShowFirstInstallSearch = false
    private var durationAutoTransition = DEFAULT_INTERVAL_HINT
    private var movingPosition = 0
    private var isFirstImpressionOnBanner = false

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
        setupNavToolbar()
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

    override fun onTickerDismissed() {
        hasTickerBeenRemoved = true
        adapter.removeTickerWidget()
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if (!miniCartSimplifiedData.isShowMiniCartWidget) {
            miniCartWidget?.hide()
        } else {
            setupPadding(miniCartSimplifiedData)
            val item = adapter.getItem(HomeProductRecomUiModel::class.java)
            if (item is HomeProductRecomUiModel) {
                viewModelTokoNow.updateCartItems(miniCartSimplifiedData, item)
            }
        }
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

    override fun getCarouselRecycledViewPool(): RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    override fun onProductRecomNonVariantClick(productCard: HomeProductCardUiModel, quantity: Int) {
        if (userSession.isLoggedIn)
            handleAddToCartEventLogin(productCard, quantity)
        else
            handleAddToCartEventNonLogin(productCard)
    }

    private fun handleAddToCartEventLogin(productCard: HomeProductCardUiModel, quantity: Int) {
        val nonVariantATC = productCard.productCardModel.nonVariant
        if (nonVariantATC?.quantity == quantity) return

        if (nonVariantATC?.quantity == 0)
            viewModelTokoNow.addToCart(productCard, quantity, localCacheModel?.shop_id.orEmpty())
        else
            viewModelTokoNow.updateCart(productCard, quantity)
    }

    fun handleAddToCartEventNonLogin(productCard: HomeProductCardUiModel) {
        RouteManager.route(context, ApplinkConst.LOGIN)
//        updatedVisitableIndicesMutableLiveData.value = listOf(visitableList.indexOf(productItem))
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
                    showEmptyState(EMPTY_STATE_NO_ADDRESS)
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
                .addIcon(IconList.ID_CART, onClick = ::onClickCartButton)
                .addIcon(IconList.ID_NAV_GLOBAL) {}
        navToolbar?.setIcon(icons)
    }

    private fun setIconOldTopNavigation() {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
                .addIcon(IconList.ID_CART, onClick = ::onClickCartButton)
        navToolbar?.setIcon(icons)
    }

    private fun onClickCartButton() {
        analytics.onClickCartButton()
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
            }
        }

        observe(viewModelTokoNow.miniCart) {
            if(it is Success) {
                setupMiniCart(it.data)
                setupPadding(it.data)
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
                showEmptyState(EMPTY_STATE_NO_ADDRESS)
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

    private fun resetMovingPosition() {
        movingPosition = 0
    }

    private fun setupMiniCart(data: MiniCartSimplifiedData) {
        if(data.isShowMiniCartWidget) {
            val shopIds = listOf(localCacheModel?.shop_id.orEmpty())
            miniCartWidget.initialize(shopIds, this, this, pageName = MiniCartAnalytics.Page.HOME_PAGE)
            miniCartWidget.show()
        } else {
            miniCartWidget.hide()
        }
    }

    private fun setupPadding(data: MiniCartSimplifiedData) {
        miniCartWidget.post {
            val paddingBottom = if(data.isShowMiniCartWidget) {
                miniCartWidget.height
            } else {
                activity?.resources?.getDimensionPixelSize(
                    com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4).orZero()
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
        val initialLoadFinished = data.isInitialLoadFinished

        when {
            initialLoad -> {
                showHeaderBackground()
                showHomeLayout(data)
                loadNextItem(data)
            }
            initialLoadFinished -> {
                rvHome?.post {
                    addLoadMoreListener()
                }
            }
            else -> {
                showHomeLayout(data)
                loadNextItem(data)
            }
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

    private fun loadVisibleLayoutData(index: Int) {
        val warehouseId = localCacheModel?.warehouse_id.orEmpty()
        val layoutManager = rvHome.layoutManager as? LinearLayoutManager
        val firstVisibleItemIndex = layoutManager?.findFirstVisibleItemPosition().orZero()
        val lastVisibleItemIndex = layoutManager?.findLastVisibleItemPosition().orZero()
        val isVisible = index in firstVisibleItemIndex..lastVisibleItemIndex
        viewModelTokoNow.getInitialLayoutData(index, warehouseId, isVisible)
    }

    private fun loadMoreLayoutData() {
        val warehouseId = localCacheModel?.warehouse_id.orEmpty()
        val layoutManager = rvHome.layoutManager as? LinearLayoutManager
        val firstVisibleItemIndex = layoutManager?.findFirstVisibleItemPosition().orZero()
        val lastVisibleItemIndex = layoutManager?.findLastVisibleItemPosition().orZero()
        viewModelTokoNow.getMoreLayoutData(warehouseId, firstVisibleItemIndex, lastVisibleItemIndex)
    }

    private fun getHomeLayout() {
        viewModelTokoNow.getHomeLayout(hasTickerBeenRemoved)
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
}