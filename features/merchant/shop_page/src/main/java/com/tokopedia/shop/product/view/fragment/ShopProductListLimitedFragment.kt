package com.tokopedia.shop.product.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.network.TextApiUtils
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.gql.data.MessageTitleErrorException
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQueryResult
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListPresenter
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListView
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopModuleRouter
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingConstant
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.analytic.model.ListTitleTypeDef
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.di.ShopCommonModule
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel
import com.tokopedia.shop.page.view.activity.ShopPageActivity
import com.tokopedia.shop.page.view.listener.ShopPageView
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent
import com.tokopedia.shop.product.di.module.ShopProductModule
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils
import com.tokopedia.shop.product.view.activity.ShopProductListActivity
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseListViewHolder
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductPromoViewHolder
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder
import com.tokopedia.shop.product.view.listener.ShopCarouselSeeAllClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.model.BaseShopProductViewModel
import com.tokopedia.shop.product.view.model.EtalaseHighlightCarouselViewModel
import com.tokopedia.shop.product.view.model.ShopMerchantVoucherViewModel
import com.tokopedia.shop.product.view.model.ShopProductEtalaseHighlightViewModel
import com.tokopedia.shop.product.view.model.ShopProductEtalaseListViewModel
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel
import com.tokopedia.shop.product.view.model.ShopProductPromoViewModel
import com.tokopedia.shop.product.view.model.ShopProductViewModel
import com.tokopedia.shop.product.view.viewmodel.ShopProductLimitedViewModel
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.shopetalasepicker.view.activity.ShopEtalasePickerActivity
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.listener.WishListActionListener

import javax.inject.Inject

import com.tokopedia.shop.analytic.ShopPageTrackingConstant.FEATURED_PRODUCT
import com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_ETALASE_POSITION
import com.tokopedia.shop.common.constant.ShopPageConstant.ETALASE_TO_SHOW
import kotlinx.android.synthetic.main.fragment_shop_product_limited_list.*
import kotlin.collections.ArrayList

/**
 * Created by nathan on 2/15/18.
 */

class ShopProductListLimitedFragment : BaseListFragment<BaseShopProductViewModel, ShopProductAdapterTypeFactory>(),
        WishListActionListener, BaseEmptyViewHolder.Callback,
        ShopProductPromoViewHolder.PromoViewHolderListener, ShopProductClickedListener,
        ShopProductEtalaseListViewHolder.OnShopProductEtalaseListViewHolderListener,
        ShopCarouselSeeAllClickedListener, MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener,
        MerchantVoucherListView {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: ShopProductLimitedViewModel

    var shopPageTracking: ShopPageTrackingBuyer? = null
    lateinit var merchantVoucherListPresenter: MerchantVoucherListPresenter

    private var progressDialog: ProgressDialog? = null
    private var urlNeedTobBeProceed: String? = null
    private var attribution: String = ""
    private var shopInfo: ShopInfo? = null
    private var shopModuleRouter: ShopModuleRouter? = null

    private var onShopProductListFragmentListener: ShopProductListFragment.OnShopProductListFragmentListener? = null

    private val sortName = Integer.toString(Integer.MIN_VALUE)
    private var recyclerView: RecyclerView? = null

    var selectedEtalaseId: String = ""
        private set
    private var selectedEtalaseName: String = ""

    private var remoteConfig: RemoteConfig? = null

    private val shopProductAdapter: ShopProductAdapter by lazy { adapter as ShopProductAdapter }
    private val gridLayoutManager: GridLayoutManager by lazy {
        GridLayoutManager(activity, GRID_SPAN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (shopProductAdapter.getItemViewType(position) == ShopProductViewHolder.GRID_LAYOUT) {
                        LIST_SPAN_COUNT
                    } else {
                        GRID_SPAN_COUNT
                    }
                }
            }
        }
    }
    private var needReloadData: Boolean = false
    private var needLoadVoucher: Boolean = false

    private var shopId: String? = null
    private var isOfficialStore: Boolean = false
    private var isGoldMerchant: Boolean = false

    private val isCurrentlyShowAllEtalase: Boolean
        get() {
            if (TextUtils.isEmpty(selectedEtalaseId)) {
                return true
            }
            val etalaseViewModelList = shopProductAdapter.shopProductEtalaseListViewModel?.etalaseModelList ?: return false
            return etalaseViewModelList.size > 0 && etalaseViewModelList[0].etalaseId.equals(selectedEtalaseId, ignoreCase = true)
        }

    override val isOwner: Boolean
        get() = if (shopInfo != null && ::viewModel.isInitialized) {
            viewModel.isMyShop(shopInfo!!.shopCore.shopID)
        } else false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.featuredProductResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessGetProductFeature(it.data)
                is Fail -> onErrorGetProductFeature(it.throwable)
            }
        })

        viewModel.etalaseResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessGetEtalaseListByShop(ArrayList(it.data))
                is Fail -> onErrorGetEtalaseListByShop(it.throwable)
            }
        })

        viewModel.productResponse.observe(this, Observer {
            when(it){
                is Success -> renderProductList(it.data.second, it.data.first)
                is Fail -> showGetListError(it.throwable)
            }
        })

        viewModel.productHighlightResp.observe(this, Observer {
            it?.run { onSuccessGetEtalaseHighlight(this) }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_product_limited_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            selectedEtalaseId = it.getString(SAVED_SELECTED_ETALASE_ID) ?: ""
            selectedEtalaseName = it.getString(SAVED_SELECTED_ETALASE_NAME) ?: ""
            shopId = it.getString(SAVED_SHOP_ID)
            isGoldMerchant = it.getBoolean(SAVED_SHOP_IS_GOLD_MERCHANT)
            isOfficialStore = it.getBoolean(SAVED_SHOP_IS_OFFICIAL)
        }
        super.onCreate(savedInstanceState)

        context?.let { shopPageTracking = ShopPageTrackingBuyer(TrackingQueue(it)) }

        val merchantVoucherComponent = DaggerMerchantVoucherComponent.builder()
                .baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent)
                .shopCommonModule(ShopCommonModule())
                .build()

        merchantVoucherListPresenter = merchantVoucherComponent.merchantVoucherListPresenter()
        merchantVoucherListPresenter.attachView(this)

        attribution = arguments?.getString(SHOP_ATTRIBUTION, "") ?: ""

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopProductLimitedViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottom_action_view.setButton1OnClickListener {
            if (shopInfo != null) {
                val intent = ShopProductSortActivity.createIntent(activity, sortName)
                this@ShopProductListLimitedFragment.startActivityForResult(intent, REQUEST_CODE_SORT)
            }
        }
        bottom_action_view.gone()
        bottom_action_view.hide(false)

        progressDialog = ProgressDialog(activity)
        progressDialog?.setMessage(getString(R.string.title_loading))
        initRecyclerView(view)
        super.onViewCreated(view, savedInstanceState)
        if (shopInfo != null) {
            loadInitialData()
        }
    }

    private fun initRecyclerView(view: View) {
        recyclerView = super.getRecyclerView(view)
        recyclerView?.let {
            val animator = it.itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy < 0) { // going up
                        if (shopProductAdapter.shopProductViewModelList.size > 0) {
                            val lastCompleteVisiblePosition = gridLayoutManager.findLastCompletelyVisibleItemPosition()
                            if (lastCompleteVisiblePosition > -1) {
                                if (lastCompleteVisiblePosition >= ShopPageConstant.ITEM_OFFSET) {
                                    bottom_action_view.show()
                                } else {
                                    bottom_action_view.hide()
                                }
                            }
                        } else {
                            bottom_action_view.hide()
                        }
                    } else if (dy > 0) { // going down
                        bottom_action_view.hide()
                    }
                }
            })
        }
    }

    fun displayProduct(shopInfo: ShopInfo) {
        this.shopInfo = shopInfo
        this.isOfficialStore = shopInfo.goldOS.isOfficial == 1
        this.isGoldMerchant = shopInfo.goldOS.isGold == 1
        this.shopId = shopInfo.shopCore.shopID
        loadInitialData()
    }

    fun setShopInfo(shopInfo: ShopInfo) {
        this.shopInfo = shopInfo
        this.isOfficialStore = shopInfo.goldOS.isOfficial == 1
        this.isGoldMerchant = shopInfo.goldOS.isGold == 1
        this.shopId = shopInfo.shopCore.shopID
    }

    fun clearCache() {
        merchantVoucherListPresenter.clearCache()
        viewModel.clearEtalaseCache()
    }

    // load data promo/featured/etalase
    override fun loadInitialData() {
        shopProductAdapter.clearAllNonDataElement()
        loadTopData()
        reloadProductData(true)
    }

    // load product list first time
    private fun reloadProductData(needLoadEtalaseHighlight: Boolean) {
        shopInfo?.let {
            isLoadingInitialData = true
            bottom_action_view.hide(false)
            shopProductAdapter.clearAllNonDataElement()
            shopProductAdapter.clearProductList()
            if (needLoadEtalaseHighlight) {
                shopProductAdapter.shopProductEtalaseHighlightViewModel = null
            }

            showLoading()
            shopId = it.shopCore.shopID
            if (viewModel.isEtalaseEmpty){
                viewModel.getShopEtalase(shopId!!)
            } else {
                loadData(defaultInitialPage)
                if (needLoadEtalaseHighlight) {
                    loadEtalaseHighLight()
                }
            }
        }
    }

    protected fun loadTopData() {
        shopInfo?.let {
            shopProductAdapter.clearPromoData()
            shopProductAdapter.clearMerchantVoucherData()
            shopProductAdapter.clearFeaturedData()
            viewModel.renderProductPromoModel(getOfficialWebViewUrl(shopInfo), this::renderShopProductPromo)
            loadVoucherList()

            viewModel.getFeaturedProduct(it.shopCore.shopID, false)

        }
    }

    private fun getOfficialWebViewUrl(shopInfo: ShopInfo?): String {
        if (shopInfo == null) {
            return ""
        }

        var officialWebViewUrl = shopInfo.topContent.topUrl
        officialWebViewUrl = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) officialWebViewUrl else ""
        officialWebViewUrl = if (TextApiUtils.isTextEmpty(officialWebViewUrl)) "" else officialWebViewUrl
        return officialWebViewUrl
    }

    override fun loadData(page: Int) {
        if (shopInfo != null && !viewModel.isEtalaseEmpty) {
            viewModel.getShopProduct(shopInfo!!.shopCore.shopID,
                    page, ShopPageConstant.DEFAULT_PER_PAGE, 0, selectedEtalaseId,
                    "", false)
        }
    }

    private fun renderShopProductPromo(shopProductPromoViewModel: ShopProductPromoViewModel) {
        shopProductAdapter.setShopProductPromoViewModel(shopProductPromoViewModel)
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return gridLayoutManager
    }

    // load data after get shop info
    override fun callInitialLoadAutomatically(): Boolean = false

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(recyclerView!!.layoutManager, shopProductAdapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                loadData(page)
            }
        }
    }

    override fun getAdapterTypeFactory(): ShopProductAdapterTypeFactory {
        val displaymetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displaymetrics)
        val deviceWidth = displaymetrics.widthPixels
        return ShopProductAdapterTypeFactory(this,
                this, this, this,
                this, this,
                true, deviceWidth, ShopTrackProductTypeDef.PRODUCT
        )
    }

    override fun createAdapterInstance(): BaseListAdapter<BaseShopProductViewModel, ShopProductAdapterTypeFactory> {
        return ShopProductAdapter(adapterTypeFactory)
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.iconRes = R.drawable.ic_empty_list_product
        if (shopInfo != null && viewModel.isMyShop(shopInfo!!.shopCore.shopID)) {
            if (isCurrentlyShowAllEtalase) {
                emptyModel.title = getString(R.string.shop_product_limited_empty_product_title_owner)
            } else {
                emptyModel.title = getString(R.string.shop_product_limited_empty_product_title_owner_at_etalase)
            }
            emptyModel.content = getString(R.string.shop_product_limited_empty_product_content_owner)
            emptyModel.buttonTitle = getString(R.string.shop_page_label_add_product)
            if (shopInfo != null) {
                shopPageTracking?.impressionZeroProduct(CustomDimensionShopPage.create(shopInfo!!.shopCore.shopID,
                        shopInfo!!.goldOS.isOfficial == 1, shopInfo!!.goldOS.isGold == 1))
            }
        } else {
            if (isCurrentlyShowAllEtalase) {
                emptyModel.content = getString(R.string.shop_product_limited_empty_product_title)
            } else {
                emptyModel.content = getString(R.string.shop_product_empty_title_etalase_desc)
            }
        }
        return emptyModel
    }

    override fun initInjector() {
        DaggerShopProductComponent
                .builder()
                .shopProductModule(ShopProductModule())
                .shopComponent(getComponent(ShopComponent::class.java))
                .build()
                .inject(this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onDestroy() {
        viewModel.featuredProductResponse.removeObservers(this)
        viewModel.etalaseResponse.removeObservers(this)
        viewModel.productHighlightResp.removeObservers(this)
        viewModel.productResponse.removeObservers(this)
        viewModel.clear()
        super.onDestroy()
        merchantVoucherListPresenter.detachView()
    }

    private fun showUseMerchantVoucherLoading() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(activity)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setMessage(getString(R.string.title_loading))
        }
        if (progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
        progressDialog!!.show()
    }

    private fun hideUseMerchantVoucherLoading() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun onItemClicked(baseShopProductViewModel: BaseShopProductViewModel) {
        // no op
    }

    override fun onErrorAddWishList(errorMessage: String, productId: String) {
        onErrorAddToWishList(MessageErrorException(errorMessage))
    }

    private fun onErrorAddToWishList(e: Throwable) {
        activity?.let {
            if (!viewModel.isLogin || e is UserNotLoginException) {
                val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
                startActivityForResult(intent, REQUEST_CODE_USER_LOGIN)
                return
            }
            NetworkErrorHelper.showCloseSnackbar(it, ErrorHandler.getErrorMessage(it, e))
        }
    }

    override fun onSuccessAddWishlist(productId: String) {
        shopProductAdapter.updateWishListStatus(productId, true)
    }

    override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
        NetworkErrorHelper.showCloseSnackbar(activity, errorMessage)
    }

    override fun onSuccessRemoveWishlist(productId: String) {
        shopProductAdapter.updateWishListStatus(productId, false)
    }

    private fun renderProductList(list: List<ShopProductViewModel>, hasNextPage: Boolean) {
        if (list.isNotEmpty() && shopInfo != null) {
            shopPageTracking?.impressionProductList(
                    isOwner,
                    ListTitleTypeDef.ETALASE,
                    selectedEtalaseName, CustomDimensionShopPageAttribution.create(shopInfo!!.shopCore.shopID,
                    shopInfo!!.goldOS.isOfficial == 1, shopInfo!!.goldOS.isGold == 1,
                    "", attribution),
                    list, shopProductAdapter.shopProductViewModelList.size, shopInfo!!.shopCore.shopID,
                    shopInfo!!.shopCore.name
            )
        }

        hideLoading()
        shopProductAdapter.clearAllNonDataElement()
        if (isLoadingInitialData) {
            shopProductAdapter.clearProductList()
            endlessRecyclerViewScrollListener.resetState()
        }
        shopProductAdapter.addProductList(list)
        updateScrollListenerState(hasNextPage)

        if (shopProductAdapter.shopProductViewModelList.size == 0) {
            // only add the empty state when the shop has No Product And No Official URL

            val needShowEmpty: Boolean
            val officialWebViewUrl = getOfficialWebViewUrl(shopInfo)
            if (TextUtils.isEmpty(officialWebViewUrl)) { // no promo web, show empty
                needShowEmpty = true
            } else {
                // no need to show empty (even if the product is empty) when there is official web
                // check if it is all product or specific etalase.
                needShowEmpty = !isCurrentlyShowAllEtalase
            }

            if (needShowEmpty) {
                shopProductAdapter.isNeedToShowEtalase = true
                shopProductAdapter.addElement(emptyDataViewModel)
            } else {
                shopProductAdapter.isNeedToShowEtalase = false
                shopProductAdapter.clearAllNonDataElement()
            }
            bottom_action_view.visibility = View.GONE
            bottom_action_view.hide()
        } else {
            shopProductAdapter.isNeedToShowEtalase = true
            bottom_action_view.visibility = View.VISIBLE
            bottom_action_view.show()
            isLoadingInitialData = false
        }
        shopProductAdapter.notifyDataSetChanged()
        shopProductAdapter.refreshSticky()
        if (activity is ShopPageActivity) {
            (activity as? ShopPageActivity)?.stopPerformanceMonitor()
        }
    }

    private fun onSuccessGetEtalaseHighlight(list: List<List<ShopProductViewModel>>) {
        shopInfo?.let {
            val etalaseHighlightCarouselViewModels = ArrayList<EtalaseHighlightCarouselViewModel>()
            val customDimensionShopPageAttribution = CustomDimensionShopPageAttribution
                    .create(it.shopCore.shopID, it.goldOS.isOfficial == 1,
                            it.goldOS.isGold == 1, null, attribution)


            list.forEachIndexed { index, listItem ->
                etalaseHighlightCarouselViewModels.add(
                        EtalaseHighlightCarouselViewModel(listItem, viewModel.etalaseHighLight[index]))
                if (listItem.isNotEmpty()) {
                    shopPageTracking?.impressionProductList(isOwner,
                            ListTitleTypeDef.HIGHLIGHTED,
                            viewModel.etalaseHighLight[index].etalaseName,
                            customDimensionShopPageAttribution,
                            listItem, 0, shopInfo!!.shopCore.shopID,
                            shopInfo!!.shopCore.name)
                }
            }

            shopProductAdapter.shopProductEtalaseHighlightViewModel =
                    ShopProductEtalaseHighlightViewModel(etalaseHighlightCarouselViewModels)
            shopProductAdapter.refreshSticky()
        }
    }

    private fun onErrorGetEtalaseHighlight(e: Throwable) {
        shopProductAdapter.shopProductEtalaseHighlightViewModel = null
    }

    private fun onErrorGetProductFeature(e: Throwable) {
        shopProductAdapter.shopProductFeaturedViewModel = null
    }

    private fun onSuccessGetProductFeature(list: List<ShopProductViewModel>) {
        shopProductAdapter.shopProductFeaturedViewModel = ShopProductFeaturedViewModel(list)
        if (list.isNotEmpty() && shopInfo != null) {
            shopPageTracking?.impressionProductList(
                    isOwner,
                    ListTitleTypeDef.HIGHLIGHTED,
                    FEATURED_PRODUCT,
                    CustomDimensionShopPageAttribution.create(shopInfo!!.shopCore.shopID,
                            shopInfo!!.goldOS.isOfficial == 1, shopInfo!!.goldOS.isGold == 1, "", attribution),
                    list, 0,
                    shopInfo!!.shopCore.shopID, shopInfo!!.shopCore.name)
        }
        shopProductAdapter.refreshSticky()
    }

    override fun showGetListError(throwable: Throwable) {
        if (activity is ShopPageActivity) {
            (activity as? ShopPageActivity)?.stopPerformanceMonitor()
        }
        hideLoading()
        updateStateScrollListener()
        if (shopProductAdapter.shopProductViewModelList.size > 0) {
            onGetListErrorWithExistingData(throwable)
        } else {
            onGetListErrorWithEmptyData(throwable)
        }
    }

    private fun onSuccessGetEtalaseListByShop(shopEtalaseModelList: ArrayList<ShopEtalaseViewModel>?) {
        //default select first index as selected.
        var etalaseBadge: String? = null

        if (TextUtils.isEmpty(selectedEtalaseId) &&
                shopEtalaseModelList != null &&
                shopEtalaseModelList.size > 0) {
            val shopEtalaseViewModel = shopEtalaseModelList[0]
            selectedEtalaseId = shopEtalaseViewModel.etalaseId
            selectedEtalaseName = shopEtalaseViewModel.etalaseName
            etalaseBadge = shopEtalaseViewModel.etalaseBadge


        }
        // update the adapter
        val shopEtalaseModelListToShow: List<ShopEtalaseViewModel>?
        if (shopEtalaseModelList != null && shopEtalaseModelList.size > ETALASE_TO_SHOW) {
            shopEtalaseModelListToShow = shopEtalaseModelList.subList(0, ETALASE_TO_SHOW)
        } else {
            shopEtalaseModelListToShow = shopEtalaseModelList
        }
        shopProductAdapter.setShopEtalase(ShopProductEtalaseListViewModel(shopEtalaseModelListToShow, selectedEtalaseId))
        shopProductAdapter.setShopEtalaseTitle(selectedEtalaseName, etalaseBadge)

        loadData(defaultInitialPage)

        loadEtalaseHighLight()
    }

    private fun loadEtalaseHighLight() {
        // load etalase highlight
        shopId = shopInfo!!.shopCore.shopID
        viewModel.getShopProductsEtalaseHighlight(shopId!!)
    }

    private fun onErrorGetEtalaseListByShop(e: Throwable) {
        shopProductAdapter.setShopEtalase(null)
        shopProductAdapter.setShopEtalaseTitle(null, null)
        shopProductAdapter.shopProductEtalaseHighlightViewModel = null
        showGetListError(e)
    }

    override fun onEmptyContentItemTextClicked() {
        // no-op
    }

    override fun onEmptyButtonClicked() {
        if (shopInfo != null) {
            shopPageTracking?.clickZeroProduct(CustomDimensionShopPage.create(shopInfo!!.shopCore.shopID,
                    shopInfo!!.goldOS.isOfficial == 1, shopInfo!!.goldOS.isGold == 1))
        }
        RouteManager.route(activity, ApplinkConst.PRODUCT_ADD)
    }

    override fun promoClicked(url: String?) {
        activity?.let {
            val urlProceed = ShopProductOfficialStoreUtils.proceedUrl(it, url, shopInfo!!.shopCore.shopID,
                    viewModel.isLogin,
                    viewModel.userDeviceId,
                    viewModel.userId)
            // Need to login
            if (!urlProceed) {
                urlNeedTobBeProceed = url
                val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
                startActivityForResult(intent, REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW)
            }
        }
    }

    override fun onEtalaseChipClicked(shopEtalaseViewModel: ShopEtalaseViewModel) {
        if (shopProductAdapter.isLoading) {
            return
        }
        selectedEtalaseId = shopEtalaseViewModel.etalaseId
        selectedEtalaseName = shopEtalaseViewModel.etalaseName
        shopProductAdapter.setSelectedEtalaseId(selectedEtalaseId)
        shopProductAdapter.setShopEtalaseTitle(selectedEtalaseName, shopEtalaseViewModel.etalaseBadge)

        updateHintRemoteConfig(selectedEtalaseName)

        if (shopInfo != null) {
            shopId = shopInfo!!.shopCore.shopID
            shopPageTracking?.clickEtalaseChip(
                    viewModel.isMyShop(shopId!!),
                    selectedEtalaseName,
                    CustomDimensionShopPage.create(shopId,
                            shopInfo!!.goldOS.isOfficial == 1, shopInfo!!.goldOS.isGold == 1))
        }
        //this is to reset fling and initial load position
        recyclerView?.smoothScrollBy(1, 1)
        gridLayoutManager.scrollToPositionWithOffset(DEFAULT_ETALASE_POSITION, 0)

        // no need ro rearraged, just notify the adapter to reload product list by etalase id
        reloadProductData(false)
    }

    override fun onEtalaseMoreListClicked() {
        if (shopInfo != null) {
            shopPageTracking?.clickMoreMenuChip(
                    viewModel.isMyShop(shopInfo!!.shopCore.shopID),
                    CustomDimensionShopPage.create(shopInfo!!.shopCore.shopID,
                            shopInfo!!.goldOS.isOfficial == 1, shopInfo!!.goldOS.isGold == 1))

            activity?.let {
                val shopEtalaseIntent = ShopEtalasePickerActivity.createIntent(it, shopInfo!!.shopCore.shopID,
                        selectedEtalaseId, true, false)
                startActivityForResult(shopEtalaseIntent, REQUEST_CODE_ETALASE)
            }
        }
    }

    override fun onWishListClicked(shopProductViewModel: ShopProductViewModel, @ShopTrackProductTypeDef shopTrackType: Int) {
        if (shopInfo != null) {
            if (shopTrackType == ShopTrackProductTypeDef.FEATURED) {
                shopPageTracking?.clickWishlist(
                        !shopProductViewModel.isWishList,
                        ListTitleTypeDef.HIGHLIGHTED, FEATURED_PRODUCT,
                        CustomDimensionShopPageProduct.create(shopInfo!!.shopCore.shopID,
                                shopInfo!!.goldOS.isOfficial == 1, shopInfo!!.goldOS.isGold == 1,
                                shopProductViewModel.id))
            } else if (shopTrackType == ShopTrackProductTypeDef.PRODUCT) {
                shopPageTracking?.clickWishlist(
                        !shopProductViewModel.isWishList,
                        ListTitleTypeDef.ETALASE, selectedEtalaseName,
                        CustomDimensionShopPageProduct.create(shopInfo!!.shopCore.shopID,
                                shopInfo!!.goldOS.isOfficial == 1, shopInfo!!.goldOS.isGold == 1,
                                shopProductViewModel.id))
            } else { // highlight
                shopPageTracking?.clickWishlist(
                        !shopProductViewModel.isWishList,
                        ListTitleTypeDef.HIGHLIGHTED,
                        shopProductAdapter.getEtalaseNameHighLight(shopProductViewModel),
                        CustomDimensionShopPageProduct.create(shopInfo!!.shopCore.shopID,
                                shopInfo!!.goldOS.isOfficial == 1, shopInfo!!.goldOS.isGold == 1,
                                shopProductViewModel.id))
            }
        }
        if (!viewModel.isLogin){
            onErrorAddToWishList(UserNotLoginException())
            return
        }
        if (shopProductViewModel.isWishList) {
            viewModel.removeWishList(shopProductViewModel.id, this)
        } else {
            viewModel.addWishList(shopProductViewModel.id, this)
        }
    }

    override fun onSeeAllClicked(shopEtalaseViewModel: ShopEtalaseViewModel) {
        if (shopInfo != null) {
            shopPageTracking?.clickHighLightSeeAll(isOwner, shopEtalaseViewModel.etalaseName,
                    CustomDimensionShopPage.create(shopInfo!!.shopCore.shopID,
                            shopInfo!!.goldOS.isOfficial == 1,
                            shopInfo!!.goldOS.isGold == 1))
        }
        val intent = ShopProductListActivity.createIntent(activity,
                shopInfo!!.shopCore.shopID, "",
                shopEtalaseViewModel.etalaseId, attribution, sortName)
        startActivity(intent)
    }

    override fun onProductClicked(shopProductViewModel: ShopProductViewModel, @ShopTrackProductTypeDef shopTrackType: Int,
                                  productPosition: Int) {
        if (shopInfo != null) {
            if (shopTrackType == ShopTrackProductTypeDef.FEATURED) {
                shopPageTracking?.clickProductPicture(isOwner,
                        ListTitleTypeDef.HIGHLIGHTED,
                        FEATURED_PRODUCT,
                        CustomDimensionShopPageAttribution.create(shopInfo!!.shopCore.shopID,
                                shopInfo!!.goldOS.isOfficial == 1, shopInfo!!.goldOS.isGold == 1,
                                shopProductViewModel.id, attribution),
                        shopProductViewModel, productPosition, shopInfo!!.shopCore.shopID, shopInfo!!.shopCore.name)
            } else if (shopTrackType == ShopTrackProductTypeDef.PRODUCT) {
                shopPageTracking?.clickProductPicture(isOwner,
                        ListTitleTypeDef.ETALASE,
                        selectedEtalaseName,
                        CustomDimensionShopPageAttribution.create(shopInfo!!.shopCore.shopID,
                                shopInfo!!.goldOS.isOfficial == 1, shopInfo!!.goldOS.isGold == 1,
                                shopProductViewModel.id, attribution),
                        shopProductViewModel, productPosition, shopInfo!!.shopCore.shopID, shopInfo!!.shopCore.name)
            } else if (shopTrackType == ShopTrackProductTypeDef.ETALASE_HIGHLIGHT) {
                shopPageTracking?.clickProductPicture(isOwner,
                        ListTitleTypeDef.HIGHLIGHTED,
                        shopProductAdapter.getEtalaseNameHighLight(shopProductViewModel),
                        CustomDimensionShopPageAttribution.create(shopInfo!!.shopCore.shopID,
                                shopInfo!!.goldOS.isOfficial == 1, shopInfo!!.goldOS.isGold == 1,
                                shopProductViewModel.id, attribution),
                        shopProductViewModel, productPosition, shopInfo!!.shopCore.shopID, shopInfo!!.shopCore.name)
            }
        }
        goToPDP(shopProductViewModel.id, attribution,
                shopPageTracking?.getListNameOfProduct(ShopPageTrackingConstant.PRODUCT, selectedEtalaseName) ?: "")


    }

    /**
     * This function is temporary for testing to avoid router and applink
     * For Dynamic Feature Support
     */
    private fun goToPDP(productId: String, attribution: String?, listNameOfProduct: String) {
        startActivity(getProductIntent(productId, attribution, listNameOfProduct))
    }

    private fun getProductIntent(productId: String, attribution: String?, listNameOfProduct: String): Intent? {
        if (context != null) {
            val bundle = Bundle()
            bundle.putString("tracker_attribution", attribution)
            bundle.putString("tracker_list_name", listNameOfProduct)
            return RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        } else {
            return null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ETALASE -> if (resultCode == Activity.RESULT_OK && shopInfo != null
                    && data != null) {
                val etalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID)
                val etalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME)
                val etalaseBadge = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_BADGE)

                // if etalase id is on the list, refresh this page; if etalase id is in other list, go to new page.
                if (shopProductAdapter.isEtalaseInChip(etalaseId)) {
                    this.selectedEtalaseId = etalaseId
                    this.selectedEtalaseName = etalaseName
                    updateHintRemoteConfig(selectedEtalaseName)

                } else {
                    if (shopInfo != null) {
                        val intent = ShopProductListActivity.createIntent(activity,
                                shopInfo!!.shopCore.shopID, "",
                                etalaseId, attribution, sortName)
                        startActivity(intent)
                    }
                }
                shopProductAdapter.setSelectedEtalaseId(selectedEtalaseId)
                shopProductAdapter.setShopEtalaseTitle(selectedEtalaseName, etalaseBadge)

                needReloadData = true

                if (shopPageTracking != null && shopInfo != null) {
                    shopPageTracking!!.clickMenuFromMoreMenu(
                            viewModel.isMyShop(shopInfo!!.shopCore.shopID),
                            etalaseName,
                            CustomDimensionShopPage.create(shopInfo!!.shopCore.shopID,
                                    shopInfo!!.goldOS.isOfficial == 1,
                                    shopInfo!!.goldOS.isGold == 1))
                }
            }
            REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW -> if (resultCode == Activity.RESULT_OK && !TextUtils.isEmpty(urlNeedTobBeProceed)) {
                promoClicked(urlNeedTobBeProceed)
            }
            REQUEST_CODE_SORT -> if (resultCode == Activity.RESULT_OK && data != null) {
                val sortName = data.getStringExtra(ShopProductSortActivity.SORT_NAME)
                if (shopId == null)
                    return

                shopPageTracking?.clickSortBy(viewModel.isMyShop(shopId!!),
                            sortName, CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant))

                startActivity(ShopProductListActivity.createIntent(activity, shopId,
                        "", selectedEtalaseId, "", sortName))
            }
            REQUEST_CODE_LOGIN_USE_VOUCHER, REQUEST_CODE_MERCHANT_VOUCHER, REQUEST_CODE_MERCHANT_VOUCHER_DETAIL -> {
                if (resultCode == Activity.RESULT_OK) {
                    needLoadVoucher = true
                }
            }
            else -> {
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int) {
        if (context == null) {
            return
        }
        shopPageTracking!!.clickUseMerchantVoucher(isOwner, merchantVoucherViewModel, position)
        //TOGGLE_MVC_ON use voucher is not ready, so we use copy instead. Keep below code for future release
        /*if (!merchantVoucherListPresenter.isLogin()) {
            if (RouteManager.isSupportApplink(getContext(), ApplinkConst.LOGIN)) {
                Intent intent = RouteManager.getIntent(getContext(), ApplinkConst.LOGIN);
                startActivityForResult(intent, REQUEST_CODE_LOGIN_USE_VOUCHER);
            }
        } else if (!merchantVoucherListPresenter.isMyShop(shopInfo.getInfo().getShopId())) {
            showUseMerchantVoucherLoading();
            merchantVoucherListPresenter.useMerchantVoucher(merchantVoucherViewModel.getVoucherCode(),
                    merchantVoucherViewModel.getVoucherId());
        }*/
        //TOGGLE_MVC_OFF
        showSnackBarClose(getString(R.string.title_voucher_code_copied))
    }

    fun showSnackBarClose(stringToShow: String) {
        activity?.let {
            val snackbar = Snackbar.make(it.findViewById(android.R.id.content), stringToShow,
                    Snackbar.LENGTH_LONG)
            snackbar.setAction(activity!!.getString(R.string.close)) { snackbar.dismiss() }
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.show()
        }
    }

    override fun onItemClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        shopPageTracking?.clickDetailMerchantVoucher(isOwner)

        context?.let {
            val intent = MerchantVoucherDetailActivity.createIntent(it, merchantVoucherViewModel.voucherId,
                    merchantVoucherViewModel, shopInfo!!.shopCore.shopID)
            startActivityForResult(intent, REQUEST_CODE_MERCHANT_VOUCHER_DETAIL)
        }
    }

    override fun onSeeAllClicked() {
        shopPageTracking?.clickSeeAllMerchantVoucher(isOwner)

        context?.let {
            val intent = MerchantVoucherListActivity.createIntent(it, shopInfo!!.shopCore.shopID,
                    shopInfo!!.shopCore.name)
            startActivityForResult(intent, REQUEST_CODE_MERCHANT_VOUCHER)
        }
    }

    override fun onErrorGetShopInfo(e: Throwable) {
        // no op, shop info is got from activity
    }

    override fun onSuccessUseVoucher(useMerchantVoucherQueryResult: UseMerchantVoucherQueryResult) {
        hideUseMerchantVoucherLoading()
        if (activity != null) {
            val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
            dialog.setTitle(useMerchantVoucherQueryResult.errorMessageTitle)
            dialog.setDesc(useMerchantVoucherQueryResult.errorMessage)
            dialog.setBtnOk(getString(R.string.label_close))
            dialog.setOnOkClickListener { v -> dialog.dismiss() }
            dialog.show()

            merchantVoucherListPresenter.clearCache()
            loadVoucherList()
        }
    }

    override fun onErrorUseVoucher(e: Throwable) {
        hideUseMerchantVoucherLoading()
        if (activity != null) {
            if (e is MessageTitleErrorException) {

                val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
                dialog.setTitle(e.errorMessageTitle)
                dialog.setDesc(e.message)
                dialog.setBtnOk(getString(R.string.label_close))
                dialog.setOnOkClickListener { dialog.dismiss() }
                dialog.show()
            } else {
                ToasterError.showClose(activity!!, ErrorHandler.getErrorMessage(activity, e))
            }
        }
    }

    override fun onSuccessGetMerchantVoucherList(merchantVoucherViewModelList: ArrayList<MerchantVoucherViewModel>) {
        shopPageTracking?.impressionUseMerchantVoucher(isOwner, merchantVoucherViewModelList)

        shopProductAdapter.setShopMerchantVoucherViewModel(ShopMerchantVoucherViewModel(merchantVoucherViewModelList))
        shopProductAdapter.refreshSticky()
    }

    override fun onErrorGetMerchantVoucherList(e: Throwable) {
        shopProductAdapter.setShopMerchantVoucherViewModel(null)
    }

    override fun onResume() {
        super.onResume()
        if (needReloadData) {
            reloadProductData(false)
            needReloadData = false
        }
        if (needLoadVoucher) {
            merchantVoucherListPresenter.clearCache()
            loadVoucherList()
            needLoadVoucher = false
        }
    }

    override fun onPause() {
        super.onPause()
        shopPageTracking!!.sendAllTrackingQueue()
    }

    private fun loadVoucherList() {
        if (shopInfo != null) {
            merchantVoucherListPresenter.getVoucherList(shopInfo!!.shopCore.shopID, NUM_VOUCHER_DISPLAY)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_SELECTED_ETALASE_ID, selectedEtalaseId)
        outState.putString(SAVED_SELECTED_ETALASE_NAME, selectedEtalaseName)
        outState.putString(SAVED_SHOP_ID, shopId)
        outState.putBoolean(SAVED_SHOP_IS_OFFICIAL, isOfficialStore)
        outState.putBoolean(SAVED_SHOP_IS_GOLD_MERCHANT, isGoldMerchant)
    }

    override fun onAttachActivity(context: Context) {
        super.onAttachActivity(context)
        remoteConfig = FirebaseRemoteConfigImpl(context)
        shopModuleRouter = context.applicationContext as ShopModuleRouter
        onShopProductListFragmentListener = context as? ShopProductListFragment.OnShopProductListFragmentListener

    }

    private fun updateHintRemoteConfig(selectedEtalaseName: String?) {
        if (remoteConfig?.getBoolean(RemoteConfigKey.SHOP_ETALASE_TOGGLE) == false) {
            onShopProductListFragmentListener?.updateUIByEtalaseName(selectedEtalaseName)
        }
    }


    override fun onSuccessGetShopInfo(shopInfo: com.tokopedia.shop.common.data.source.cloud.model.ShopInfo) {
        // NO-Op
    }

    companion object {

        private const val REQUEST_CODE_USER_LOGIN = 100
        private const val REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW = 101
        private const val REQUEST_CODE_ETALASE = 205
        private const val REQUEST_CODE_LOGIN_USE_VOUCHER = 206
        private const val REQUEST_CODE_MERCHANT_VOUCHER = 207
        private const val REQUEST_CODE_MERCHANT_VOUCHER_DETAIL = 208

        private const val REQUEST_CODE_SORT = 300
        private const val LIST_SPAN_COUNT = 1
        private const val GRID_SPAN_COUNT = 2

        private const val SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION"

        const val SAVED_SELECTED_ETALASE_ID = "saved_etalase_id"
        const val SAVED_SELECTED_ETALASE_NAME = "saved_etalase_name"
        const val SAVED_SHOP_ID = "saved_shop_id"
        const val SAVED_SHOP_IS_OFFICIAL = "saved_shop_is_official"
        const val SAVED_SHOP_IS_GOLD_MERCHANT = "saved_shop_is_gold_merchant"
        const val NUM_VOUCHER_DISPLAY = 3

        @JvmStatic
        fun createInstance(shopAttribution: String?): ShopProductListLimitedFragment {
            val fragment = ShopProductListLimitedFragment()
            val bundle = Bundle()
            bundle.putString(SHOP_ATTRIBUTION, shopAttribution)
            fragment.arguments = bundle
            return fragment
        }
    }
}