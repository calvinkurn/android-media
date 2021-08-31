package com.tokopedia.tokopedianow.recentpurchase.presentation.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ConstantKey
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRecommendationCarouselUiModel
import com.tokopedia.tokopedianow.common.util.CustomLinearLayoutManager
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import com.tokopedia.tokopedianow.recentpurchase.di.component.DaggerRecentPurchaseComponent
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.RecentPurchaseAdapter
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.RecentPurchaseAdapterTypeFactory
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.differ.RecentPurchaseListDiffer
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewmodel.TokoNowRecentPurchaseViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_tokopedianow_home.*
import javax.inject.Inject

class TokoNowRecentPurchaseFragment: Fragment(), MiniCartWidgetListener {

    companion object {
        fun newInstance(): TokoNowRecentPurchaseFragment {
            return TokoNowRecentPurchaseFragment()
        }
    }

    @Inject
    lateinit var viewModel: TokoNowRecentPurchaseViewModel

    private var swipeRefreshLayout: SwipeToRefresh? = null
    private var rvRecentPurchase: RecyclerView? = null
    private var navToolbar: NavToolbar? = null
    private var statusBarBg: View? = null
    private var miniCartWidget: MiniCartWidget? = null
    private var rvLayoutManager: CustomLinearLayoutManager? = null

    private val adapter by lazy {
        RecentPurchaseAdapter(
            RecentPurchaseAdapterTypeFactory(),
            RecentPurchaseListDiffer()
        )
    }

    private var localCacheModel: LocalCacheModel? = null
    private val loadMoreListener by lazy { createLoadMoreListener() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_recent_purchase, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupStatusBar()
        setupNavToolbar()
        setupRecyclerView()
        setupSwipeRefreshLayout()
        observeLiveData()
        updateCurrentPageLocalCacheModelData()

        adapter.submitList(listOf(TokoNowEmptyStateOocUiModel(id = "id"), TokoNowRecommendationCarouselUiModel(pageName = "testing", carouselData = RecommendationCarouselData(
            state = RecommendationCarouselData.STATE_READY,
            recommendationData = RecommendationWidget(
                listOf(RecommendationItem(name = "hello"))
            )
        ))))
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if (!miniCartSimplifiedData.isShowMiniCartWidget) {
            miniCartWidget?.hide()
        }
        viewModel.setProductAddToCartQuantity(miniCartSimplifiedData)
        setupPadding(miniCartSimplifiedData.isShowMiniCartWidget)
    }

    private fun initInjector() {
        DaggerRecentPurchaseComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
    
    private fun initView() {
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh_layout)
        rvRecentPurchase = view?.findViewById(R.id.rv_recent_purchase)
        navToolbar = view?.findViewById(R.id.nav_toolbar)
        statusBarBg = view?.findViewById(R.id.status_bar_bg)
        miniCartWidget = view?.findViewById(R.id.mini_cart_widget)
    }

    private fun setupStatusBar() {
        /*
            this status bar background only shows for android Kitkat below
            In that version, status bar can't be forced to dark mode
            We must set background to keep status bar icon visible
        */
        activity?.let {
            statusBarBg?.apply {
                layoutParams?.height = ViewHelper.getStatusBarHeight(activity)
                visibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) View.INVISIBLE else View.VISIBLE
            }
            setStatusBarAlpha()
        }
    }

    private fun setStatusBarAlpha() {
        val drawable = statusBarBg?.background
        drawable?.alpha = 0
        statusBarBg?.background = drawable
    }


    private fun setupNavToolbar() {
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

    private fun navAbTestCondition(ifNavRevamp: () -> Unit = {}, ifNavOld: () -> Unit = {}) {
        if (!isNavOld()) {
            ifNavRevamp.invoke()
        } else {
            ifNavOld.invoke()
        }
    }

    private fun setupTopNavigation() {
        navToolbar?.let { toolbar ->
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

    private fun onClickCartButton() {}

    private fun getAbTestPlatform(): AbTestPlatform {
        val remoteConfigInstance = RemoteConfigInstance(activity?.application)
        return remoteConfigInstance.abTestPlatform
    }

    private fun isNavOld(): Boolean {
        return try {
            getAbTestPlatform().getString(ConstantKey.AB_TEST_EXP_NAME, ConstantKey.AB_TEST_VARIANT_OLD) == ConstantKey.AB_TEST_VARIANT_OLD
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }

    private fun setupRecyclerView() {
        context?.let {
            rvRecentPurchase?.apply {
                adapter = this@TokoNowRecentPurchaseFragment.adapter
                rvLayoutManager = CustomLinearLayoutManager(it)
                layoutManager = rvLayoutManager
            }
            rvRecentPurchase?.addOnScrollListener(loadMoreListener)
        }
    }

    private fun setupSwipeRefreshLayout() {
        context?.let {
            val marginZero = context?.resources?.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).orZero()
            val toolbarHeight = NavToolbarExt.getFullToolbarHeight(it)
            swipeRefreshLayout?.setMargin(marginZero, toolbarHeight, marginZero, marginZero)
            swipeRefreshLayout?.setOnRefreshListener {
                // TO-DO: implement refresh here
            }
        }
    }

    private fun observeLiveData() {
        observe(viewModel.miniCart) {
            if(it is Success) {
                setupMiniCart(it.data)
                setupPadding(it.data.isShowMiniCartWidget)
            }
        }

        observe(viewModel.chooseAddress) {
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
                // TO-DO: Show empty state
            }
        }

        observe(viewModel.miniCartAdd) {
            when(it) {
                is Success -> {
                    getMiniCart()
                    showToaster(
                        message = it.data.errorMessage.joinToString(separator = ", "),
                        type = Toaster.TYPE_NORMAL
                    )
                }
                is Fail -> {
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = Toaster.TYPE_ERROR
                    )
                }
            }
        }

        observe(viewModel.miniCartUpdate) {
            when(it) {
                is Success -> {
                    val shopIds = listOf(localCacheModel?.shop_id.orEmpty())
                    miniCartWidget?.updateData(shopIds)
                }
                is Fail -> {
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = Toaster.TYPE_ERROR
                    )
                }
            }
        }

        observe(viewModel.miniCartRemove) {
            when(it) {
                is Success -> {
                    getMiniCart()
                    showToaster(
                        message = it.data.second,
                        type = Toaster.TYPE_NORMAL
                    )
                }
                is Fail -> {
                    val message = it.throwable.message.orEmpty()
                    showToaster(message = message, type = Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun setupMiniCart(data: MiniCartSimplifiedData) {
        if(data.isShowMiniCartWidget) {
            val shopIds = listOf(localCacheModel?.shop_id.orEmpty())
            miniCartWidget?.initialize(shopIds, this, this, pageName = MiniCartAnalytics.Page.HOME_PAGE)
            miniCartWidget?.show()
        } else {
            miniCartWidget?.hide()
        }
    }

    private fun setupPadding(isShowMiniCartWidget: Boolean) {
        miniCartWidget?.post {
            val paddingBottom = if (isShowMiniCartWidget) {
                miniCartWidget?.height.orZero()
            } else {
                activity?.resources?.getDimensionPixelSize(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).orZero()
            }
            swipeRefreshLayout?.setPadding(0, 0, 0, paddingBottom)
        }
    }

    private fun getMiniCart() {
        val shopId = listOf(localCacheModel?.shop_id.orEmpty())
        val warehouseId = localCacheModel?.warehouse_id
        viewModel.getMiniCart(shopId, warehouseId)
    }

    private fun updateCurrentPageLocalCacheModelData() {
        context?.let {
            localCacheModel = ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private fun checkIfChooseAddressWidgetDataUpdated() {
        localCacheModel?.let {
            context?.apply {
                if (ChooseAddressUtils.isLocalizingAddressHasUpdated(this, it)) {
                    updateCurrentPageLocalCacheModelData()
                }
            }
        }
    }

    private fun checkStateNotInServiceArea(shopId: Long = -1L, warehouseId: Long) {
        context?.let {
            when {
                shopId == 0L -> viewModel.getChooseAddress(TokoNowHomeFragment.SOURCE)
                warehouseId == 0L -> {
                    // TO-DO: Implement show empty state
                }
                else -> {
                   // TO-DO: Implement show layout
                }
            }
        }
    }

    private fun showToaster(message: String, duration: Int = Toaster.LENGTH_SHORT, type: Int) {
        view?.let { view ->
            if (message.isNotBlank()) {
                Toaster.build(
                    view = view,
                    text = message,
                    duration = duration,
                    type = type
                ).show()
            }
        }
    }

    private fun createLoadMoreListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                loadMoreProduct()
            }
        }
    }

    private fun loadMoreProduct() {
        // TO-DO: call load more product here
    }
}