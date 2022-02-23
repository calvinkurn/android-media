package com.tokopedia.shop_widget.mvc_locked_to_product.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.simplified.MiniCartSimplifiedWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.FragmentMvcLockedToProductBinding
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTracking
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.model.MvcLockedToProductAddToCartTracker
import com.tokopedia.shop_widget.mvc_locked_to_product.di.component.DaggerMvcLockedToProductComponent
import com.tokopedia.shop_widget.mvc_locked_to_product.di.component.MvcLockedToProductComponent
import com.tokopedia.shop_widget.mvc_locked_to_product.di.module.MvcLockedToProductModule
import com.tokopedia.shop_widget.mvc_locked_to_product.util.MvcLockedToProductMapper
import com.tokopedia.shop_widget.mvc_locked_to_product.util.MvcLockedToProductUtil
import com.tokopedia.shop_widget.mvc_locked_to_product.view.ProductItemDecoration
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.MvcLockedToProductAdapter
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.MvcLockedToProductTypeFactory
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder.MvcLockedToProductGlobalErrorViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder.MvcLockedToProductGridViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder.MvcLockedToProductSortSectionViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.bottomsheet.MvcLockedToProductSortListBottomSheet
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.*
import com.tokopedia.shop_widget.mvc_locked_to_product.view.viewmodel.MvcLockedToProductViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

open class MvcLockedToProductFragment : BaseDaggerFragment(),
    HasComponent<MvcLockedToProductComponent>,
    MvcLockedToProductGlobalErrorViewHolder.Listener,
    MvcLockedToProductSortSectionViewHolder.Listener,
    MvcLockedToProductSortListBottomSheet.Callback,
    MvcLockedToProductGridViewHolder.Listener,
    MiniCartWidgetListener {

    companion object {
        private const val CART_LOCAL_CACHE_NAME = "CART"
        private const val TOTAL_CART_CACHE_KEY = "CACHE_TOTAL_CART"
        private const val GRID_SPAN_COUNT = 2
        private const val START_PAGE = 1
        private const val PER_PAGE = 10
        private const val PAGE_SOURCE_KEY = "page_source"
        private const val REQUEST_CODE_USER_LOGIN = 101
        fun createInstance() = MvcLockedToProductFragment()
    }

    private val viewBinding: FragmentMvcLockedToProductBinding? by viewBinding()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var tracking: MvcLockedToProductTracking
    private var viewModel: MvcLockedToProductViewModel? = null
    private var cartLocalCacheHandler: LocalCacheHandler? = null
    private var chooseAddressLocalCacheModel: LocalCacheModel? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private var voucherId: String = ""
    private var shopId: String = ""
    private var previousPage: String = "NULL"
    private var selectedSortData: MvcLockedToProductSortUiModel =
        MvcLockedToProductSortListFactory.getDefaultSortData()
    private val isUserLogin: Boolean
        get() = viewModel?.isUserLogin.orFalse()
    private val userId: String
        get() = viewModel?.userId.orEmpty()
    private val isSellerView: Boolean
        get() = viewModel?.isSellerView(shopId).orFalse()
    private val adapter by lazy {
        MvcLockedToProductAdapter(
            typeFactory = MvcLockedToProductTypeFactory(
                this,
                this,
                this
            )
        )
    }
    private val staggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }
    private var miniCartWidget: MiniCartSimplifiedWidget? = null
    private var navigationToolbar: NavToolbar? = null
    private var swipeRefreshView:  SwipeToRefresh? = null
    private var rvProductList:  RecyclerView? = null


    private fun getIntentData() {
        activity?.intent?.data?.let {
            val shopIdSegmentData = it.pathSegments.getOrNull(4).orEmpty()
            if (shopIdSegmentData.toIntOrNull() != null) {
                shopId = shopIdSegmentData
            }
            previousPage = it.getQueryParameter(PAGE_SOURCE_KEY)?.takeIf { queryParamValue ->
                queryParamValue.isNotEmpty()
            } ?: previousPage
            voucherId = it.pathSegments.getOrNull(5).orEmpty()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMvcLockedToProductBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getIntentData()
        initVariable()
        initView()
        setupView()
        observeLiveData()
        loadInitialData()
        sendOpenScreenTracker()
    }

    private fun setupView() {
        setupToolbar()
        setupRecyclerView()
        setupSwipeRefreshLayout()
    }

    private fun observeLiveData() {
        observeHasNextPageLiveData()
        observeMvcLockToProductLiveData()
        observeProductListLiveData()
        observeAddToCartLiveData()
        observeUpdateCartLiveData()
        observeDeleteCartLiveData()
        observeMvcAddToCartTrackerLiveData()
    }

    private fun initView() {
        miniCartWidget = viewBinding?.miniCartSimplifiedWidget
        navigationToolbar = viewBinding?.navigationToolbar
        swipeRefreshView = viewBinding?.swipeRefresh
        rvProductList = viewBinding?.rvProductList
    }

    private fun observeMvcAddToCartTrackerLiveData() {
        viewModel?.mvcAddToCartTracker?.observe(viewLifecycleOwner, {
            when (it.atcType) {
                MvcLockedToProductAddToCartTracker.AtcType.ADD -> {
                    sendClickAddToCartTracker(it)
                }
                MvcLockedToProductAddToCartTracker.AtcType.UPDATE_ADD, MvcLockedToProductAddToCartTracker.AtcType.UPDATE_REMOVE -> {
                    sendUpdateCartProductQuantityTracker(it)
                }
                else -> {
                    sendRemoveCartProductTracker(it)
                }
            }
        })
    }

    private fun observeAddToCartLiveData() {
        viewModel?.miniCartAdd?.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    updateMiniCartWidget()
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
        })
    }

    private fun sendClickAddToCartTracker(atcTrackerModel: MvcLockedToProductAddToCartTracker) {
        adapter.getProductUiModel(atcTrackerModel.productId)?.let {
            tracking.sendClickAddToCartTracker(
                atcTrackerModel.cartId,
                it,
                voucherId,
                atcTrackerModel.quantity,
                shopId,
                userId,
                isSellerView
            )
        }
    }

    private fun sendUpdateCartProductQuantityTracker(atcTrackerModel: MvcLockedToProductAddToCartTracker) {
        tracking.sendClickUpdateProductQuantityTracker(
            atcTrackerModel.productId,
            atcTrackerModel.quantity,
            atcTrackerModel.atcType,
            shopId,
            userId,
            isSellerView
        )
    }

    private fun sendRemoveCartProductTracker(atcTrackerModel: MvcLockedToProductAddToCartTracker) {
        tracking.sendClickAtcTrashButton(
            atcTrackerModel.productId,
            atcTrackerModel.quantity,
            atcTrackerModel.atcType,
            shopId,
            userId,
            voucherId,
            isSellerView
        )
    }

    private fun observeUpdateCartLiveData() {
        viewModel?.miniCartUpdate?.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    updateMiniCartWidget()
                }
                is Fail -> {
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = Toaster.TYPE_ERROR
                    )
                }
            }
        })
    }

    private fun observeDeleteCartLiveData() {
        viewModel?.miniCartRemove?.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    updateMiniCartWidget()
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
        })
    }

    private fun updateMiniCartWidget() {
        if(isUserLogin && MvcLockedToProductUtil.isMvcPhase2())
            miniCartWidget?.updateData()
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

    private fun initMiniCart(
        voucherUiModel: MvcLockedToProductVoucherUiModel?
    ) {
        miniCartWidget?.apply {
            initialize(
                shopIds = listOf(shopId),
                fragment = this@MvcLockedToProductFragment,
                listener = this@MvcLockedToProductFragment,
                promoId = voucherId,
                promoCode = voucherUiModel?.baseCode.orEmpty()
            )
            updateMiniCartWidget()
        }
    }

    private fun hideMiniCartWidget() {
        miniCartWidget?.hide()
    }

    private fun showMiniCartWidget() {
        miniCartWidget?.show()
    }

    override fun onResume() {
        super.onResume()
        refreshCartCounterData()
        updateMiniCartWidget()
    }

    private fun refreshCartCounterData() {
        if (isUserLogin && !MvcLockedToProductUtil.isSellerApp())
            navigationToolbar?.setBadgeCounter(IconList.ID_CART, getCartCounter())
    }

    private fun sendOpenScreenTracker() {
        tracking.sendOpenScreenMvcLockedToProduct(
            voucherId,
            shopId,
            userId,
            previousPage,
            isUserLogin
        )
    }

    private fun loadInitialData() {
        hideMiniCartWidget()
        resetEndlessScrollState()
        adapter.showInitialPagePlaceholderLoading()
        getMvcLockedToProductData(voucherId)
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshView?.setOnRefreshListener {
            loadInitialData()
        }
    }

    private fun resetSwipeLayout() {
        swipeRefreshView?.isEnabled = true
        swipeRefreshView?.isRefreshing = false
    }

    private fun observeHasNextPageLiveData() {
        viewModel?.hasNextPageLiveData?.observe(viewLifecycleOwner, {
            updateEndlessScrollListener(it)
        })
    }

    private fun observeProductListLiveData() {
        viewModel?.productListDataProduct?.observe(viewLifecycleOwner, {
            adapter.hideLoading()
            when (it) {
                is Success -> {
                    setProductListSectionData(it.data)
                }
                is Fail -> {

                }
            }
        })
    }

    private fun observeMvcLockToProductLiveData() {
        viewModel?.mvcLockToProductLiveData?.observe(viewLifecycleOwner, {
            adapter.hideLoading()
            resetSwipeLayout()
            when (it) {
                is Success -> {
                    if (it.data.mvcLockedToProductErrorUiModel.errorTitle.isNotEmpty()) {
                        showErrorView(it.data.mvcLockedToProductErrorUiModel)
                    } else {
                        setVoucherSectionData(it.data.mvcLockedToProductVoucherUiModel)
                        setTotalProductAndSortSectionData(it.data.mvcLockedToProductTotalProductAndSortUiModel)
                        setProductListSectionData(it.data.mvcLockedToProductListGridProductUiModel)
                    }
                    initMiniCart(adapter.getVoucherUiModel())
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    val globalErrorType: Int = when (it.throwable) {
                        is UnknownHostException, is SocketTimeoutException -> {
                            GlobalError.NO_CONNECTION
                        }
                        else -> {
                            GlobalError.SERVER_ERROR
                        }
                    }
                    val failErrorUiModel =
                        MvcLockedToProductMapper.mapToMvcLockedToProductErrorUiModel(
                            errorDescription = errorMessage,
                            globalErrorType = globalErrorType
                        )
                    showErrorView(failErrorUiModel)
                }
            }
        })
    }

    private fun showErrorView(uiModel: MvcLockedToProductGlobalErrorUiModel) {
        adapter.showGlobalErrorView(uiModel)
    }

    private fun getMvcLockedToProductData(promoId: String) {
        val userAddressLocalData = MvcLockedToProductUtil.getWidgetUserAddressLocalData(context)
        viewModel?.getMvcLockedToProductData(
            MvcLockedToProductRequestUiModel(
                shopId,
                promoId,
                START_PAGE,
                PER_PAGE,
                selectedSortData,
                userAddressLocalData
            )
        )
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(
            MvcLockedToProductViewModel::class.java
        )
        cartLocalCacheHandler = LocalCacheHandler(context, CART_LOCAL_CACHE_NAME)
        chooseAddressLocalCacheModel = ChooseAddressUtils.getLocalizingAddressData(requireContext())
        endlessRecyclerViewScrollListener = createEndlessRecyclerViewListener()
    }

    private fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                getNextProductListData(page)
            }
        }
    }

    private fun getNextProductListData(page: Int) {
        adapter.showLoadMoreLoading()
        getProductListData(voucherId, page)
    }

    private fun getProductListData(promoId: String, page: Int) {
        val userAddressLocalData = MvcLockedToProductUtil.getWidgetUserAddressLocalData(context)
        viewModel?.getProductListData(
            MvcLockedToProductMapper.mapToMvcLockedToProductRequestUiModel(
                shopId,
                promoId,
                page,
                PER_PAGE,
                selectedSortData,
                userAddressLocalData
            )
        )
    }

    private fun updateEndlessScrollListener(hasNextPage: Boolean) {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(hasNextPage)
    }

    private fun setupRecyclerView() {
        rvProductList?.apply {
            staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            adapter = this@MvcLockedToProductFragment.adapter
            layoutManager = staggeredGridLayoutManager
            itemAnimator = null
            endlessRecyclerViewScrollListener?.let {
                addOnScrollListener(it)
            }
            addProductItemDecoration()
        }
    }

    protected open fun RecyclerView.addProductItemDecoration() {
        try {
            val context = context ?: return
            val unifySpace16 = com.tokopedia.unifyprinciples.R.dimen.unify_space_16
            val spacing = context.getDimensionPixelSize(unifySpace16)

            if (itemDecorationCount >= 1)
                invalidateItemDecorations()

            addItemDecoration(ProductItemDecoration(spacing))
        } catch (throwable: Throwable) {

        }
    }

    private fun Context.getDimensionPixelSize(@DimenRes id: Int) =
        resources.getDimensionPixelSize(id)

    private fun setVoucherSectionData(
        mvcLockedToProductVoucherUiModel: MvcLockedToProductVoucherUiModel
    ) {
        adapter.addVoucherData(mvcLockedToProductVoucherUiModel)
    }

    private fun setTotalProductAndSortSectionData(
        mvcLockedToProductTotalProductAndSortUiModel: MvcLockedToProductSortSectionUiModel
    ) {
        adapter.addTotalProductAndSortData(mvcLockedToProductTotalProductAndSortUiModel)
    }

    private fun setProductListSectionData(
        mvcLockedToListProductGridProductUiModel: List<MvcLockedToProductGridProductUiModel>
    ) {
        adapter.addProductListData(mvcLockedToListProductGridProductUiModel)
    }

    private fun setupToolbar() {
        navigationToolbar?.apply {
            if (!MvcLockedToProductUtil.isSellerApp()) {
                val iconBuilder = IconBuilder()
                iconBuilder.addIcon(IconList.ID_CART) {}
                iconBuilder.addIcon(IconList.ID_NAV_GLOBAL) {}
                setIcon(iconBuilder)
            }
            setToolbarPageName(getString(R.string.mvc_locked_to_product_toolbar_name))
        }
    }

    private fun getCartCounter(): Int {
        return cartLocalCacheHandler?.getInt(TOTAL_CART_CACHE_KEY, 0).orZero()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent() = activity?.run {
        DaggerMvcLockedToProductComponent.builder()
            .mvcLockedToProductModule(MvcLockedToProductModule())
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onGlobalErrorActionRefreshPage() {
        loadInitialData()
    }

    override fun onGlobalErrorActionRedirectAppLink(appLink: String) {
        RouteManager.route(context, appLink)
    }

    override fun onSortChipClicked() {
        openSortBottomSheet()
    }

    private fun openSortBottomSheet() {
        val bottomSheet = MvcLockedToProductSortListBottomSheet.createInstance()
        bottomSheet.show(
            childFragmentManager,
            selectedSortData,
            this
        )
    }

    override fun onApplySort(mvcLockedToProductSortUiModel: MvcLockedToProductSortUiModel) {
        selectedSortData = mvcLockedToProductSortUiModel
        tracking.clickSortOption(
            mvcLockedToProductSortUiModel.name,
            shopId,
            userId,
            isSellerView
        )
        getNewProductListData()
    }

    private fun getNewProductListData() {
        resetEndlessScrollState()
        adapter.updateTotalProductAndSortData(selectedSortData)
        adapter.showNewProductListPlaceholder()
        getProductListData(voucherId, START_PAGE)
    }

    override fun onProductClicked(index: Int, uiModel: MvcLockedToProductGridProductUiModel) {
        val productPosition = index - adapter.getFirstProductCardPosition()
        sendProductClickTracker(productPosition, uiModel)
        redirectToPdp(uiModel.productID)
    }

    override fun onProductVariantClickAtc(uiModel: MvcLockedToProductGridProductUiModel) {
        if (isUserLogin) {
            AtcVariantHelper.goToAtcVariant(
                context = requireContext(),
                productId = uiModel.productID,
                pageSource = VariantPageSource.SHOP_COUPON_PAGESOURCE,
                shopId = shopId,
                dismissAfterTransaction = true,
                startActivitResult = this::startActivityForResult
            )
        } else {
            redirectToLoginPage()
        }
    }

    override fun onProductVariantQuantityChanged(
        productInCart: MvcLockedToProductGridProductUiModel.ProductInCart,
        quantity: Int
    ) {
        if (quantity.isZero()) {
            adapter.resetProductVariantQuantity(productInCart.productId, quantity)
        }
        handleAtcFlow(productInCart.productId, quantity, shopId)
    }

    override fun onProductNonVariantAtcQuantityChanged(
        productId: String,
        quantity: Int
    ) {
        if (isUserLogin) {
            handleAtcFlow(productId, quantity, shopId)
        } else {
            redirectToLoginPage()
        }
    }

    private fun handleAtcFlow(productId: String, quantity: Int, shopId: String) {
        viewModel?.handleAtcFlow(productId, quantity, shopId)
    }

    private fun redirectToLoginPage() {
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_USER_LOGIN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_USER_LOGIN -> {
                loadInitialData()
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun sendProductClickTracker(index: Int, uiModel: MvcLockedToProductGridProductUiModel) {
        tracking.clickProductCard(
            MvcLockedToProductUtil.getActualPositionFromIndex(index),
            uiModel.productID,
            uiModel.productCardModel.productName,
            uiModel.finalPrice,
            voucherId,
            shopId,
            userId,
            adapter.getVoucherName(),
            isSellerView
        )
    }

    private fun redirectToPdp(productId: String) {
        context?.let {
            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                productId
            )
            startActivity(intent)
        }
    }

    private fun resetEndlessScrollState() {
        endlessRecyclerViewScrollListener?.resetState()
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        viewModel?.setMiniCartData(miniCartSimplifiedData)
        rvProductList?.invalidateItemDecorations()
        adapter.updateProductListDataWithMiniCartData(miniCartSimplifiedData)
        if (miniCartSimplifiedData.isShowMiniCartWidget) {
            showMiniCartWidget()
        } else {
            hideMiniCartWidget()
        }
    }

}