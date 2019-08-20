package com.tokopedia.product.detail.view.widget

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WIHSLIST_STATUS_IS_WISHLIST
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductDataModel
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationDataModel
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.adapter.AddToCartDoneAdapter
import com.tokopedia.product.detail.view.adapter.AddToCartDoneTypeFactory
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneAddedProductViewHolder
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneRecommendationViewHolder
import com.tokopedia.product.detail.view.viewmodel.AddToCartDoneViewModel
import com.tokopedia.product.detail.view.viewmodel.Loaded
import com.tokopedia.product.detail.view.viewmodel.Loading
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AddToCartDoneBottomSheet :
        BottomSheets(),
        AddToCartDoneAddedProductViewHolder.AddToCartDoneAddedProductListener,
        HasComponent<ProductDetailComponent>,
        RecommendationListener {

    companion object {
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val REQUEST_FROM_PDP = 394
        const val KEY_ADDED_PRODUCT_DATA_MODEL = "addedProductDataModel"
        const val KEY_SUCCESS_MESSAGE_ATC = "successMessageAtc"


    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var productDetailTracking: ProductDetailTracking
    private lateinit var trackingQueue: TrackingQueue
    private lateinit var addToCartDoneViewModel: AddToCartDoneViewModel
    private lateinit var atcDoneAdapter: AddToCartDoneAdapter
    private var lastAdapterPosition = -1
    lateinit var recyclerView: RecyclerView
    lateinit var containerLayout: CoordinatorLayout
    var addedProductDataModel: AddToCartDoneAddedProductDataModel? = null
    var successMessageAtc = ""

    override fun getLayoutResourceId(): Int {
        return R.layout.add_to_cart_done_bottomsheet
    }

    override fun getBaseLayoutResourceId(): Int {
        return R.layout.widget_bottomsheet_add_to_cart_done
    }

    override fun initView(view: View?) {
        view?.let {
            recyclerView = it.findViewById(R.id.recycler_view_add_to_cart_done)
            containerLayout = it.findViewById(R.id.container_layout)
        }
    }

    override fun title(): String {
        return ""
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        dialog?.run {
            findViewById<FrameLayout>(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initInjector()
        initViewModel()
        getArgumentsData()
        context?.let {
            trackingQueue = TrackingQueue(it)
        }
        initAdapter()
        observeRecommendationProduct()
        getRecommendationProduct()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun getRecommendationProduct() {
        addedProductDataModel?.productId?.let {
            addToCartDoneViewModel.getRecommendationProduct(it)
        }
    }

    private fun initAdapter() {
        val factory = AddToCartDoneTypeFactory(this, this)
        atcDoneAdapter = AddToCartDoneAdapter(factory)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = atcDoneAdapter
        atcDoneAdapter.addElement(addedProductDataModel)
    }

    private fun observeRecommendationProduct() {
        addToCartDoneViewModel.recommendationProduct.observe(this, Observer {
            when (it) {
                is Loading -> {
                    atcDoneAdapter.addElement(LoadingMoreModel())
                    atcDoneAdapter.notifyDataSetChanged()
                }
                is Loaded -> {
                    (it.data as? Success)?.data?.let { result ->
                        atcDoneAdapter.clearAllElements()
                        for (res in result) {
                            atcDoneAdapter.addElement(AddToCartDoneRecommendationDataModel(res))
                        }
                        atcDoneAdapter.addElement(0, addedProductDataModel)
                        atcDoneAdapter.notifyDataSetChanged()
                    }
                }
            }
            configBottomSheetHeight()
        })
    }

    private fun configBottomSheetHeight() {
        dialog?.run {
            val parent = findViewById<FrameLayout>(R.id.design_bottom_sheet)
            val displaymetrics = DisplayMetrics()
            parent.measure(0, 0)
            activity?.windowManager?.defaultDisplay?.getMetrics(displaymetrics)
            val screenHeight = displaymetrics.heightPixels
            val contentHeight = parent.measuredHeight
            val finalHeight = if (contentHeight >= screenHeight)
                (screenHeight * 0.9f).toInt()
            else {
                contentHeight
            }
            val params = parent.layoutParams
            params.height = finalHeight
            parent.layoutParams = params
        }
    }

    private fun getArgumentsData() {
        arguments?.let {
            addedProductDataModel = it.getParcelable(KEY_ADDED_PRODUCT_DATA_MODEL)
            successMessageAtc = it.getString(KEY_SUCCESS_MESSAGE_ATC, "")
        }
    }

    private fun initViewModel() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        addToCartDoneViewModel = viewModelProvider.get(AddToCartDoneViewModel::class.java)
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun goToCart() {
        activity?.let {
            startActivity(RouteManager.getIntent(it, ApplinkConst.CART))
        }
    }

    private fun goToPDP(item: RecommendationItem, position: Int) {
        RouteManager.getIntent(
                activity,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                item.productId.toString()
        ).run {
            putExtra(PDP_EXTRA_UPDATED_POSITION, position)
            startActivityForResult(this, REQUEST_FROM_PDP)
        }
    }

    private fun updateWishlist(isWishlist: Boolean, position: Int) {
        if (recyclerView.findViewHolderForAdapterPosition(lastAdapterPosition) is AddToCartDoneRecommendationViewHolder) {
            (recyclerView.findViewHolderForAdapterPosition(lastAdapterPosition) as AddToCartDoneRecommendationViewHolder).updateWishlist(
                    position,
                    isWishlist
            )
        }
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FLEXIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP) {
            data?.let {
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST,
                        false)
                val position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1)
                updateWishlist(wishlistStatusFromPdp, position)
            }
            lastAdapterPosition = -1
        }
    }

    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        if (item.isTopAds) ImpresionTask().execute(item.clickUrl)
        productDetailTracking.eventAddToCartRecommendationClick(
                item,
                item.position,
                addToCartDoneViewModel.isLoggedIn(),
                item.header
        )
        if (position.size > 1) {
            lastAdapterPosition = position[0]
            goToPDP(item, position[1])
        } else {
            goToPDP(item, position[0])
        }
    }

    override fun onProductImpression(item: RecommendationItem) {
        if (item.isTopAds) ImpresionTask().execute(item.trackerImageUrl)
        productDetailTracking.eventAddToCartRecommendationImpression(
                item.position,
                item,
                addToCartDoneViewModel.isLoggedIn(),
                item.pageName,
                item.header,
                trackingQueue
        )
    }

    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {
        if (isAddWishlist) {
            addToCartDoneViewModel.addWishList(item.productId.toString(), callback)
        } else {
            addToCartDoneViewModel.removeWishList(item.productId.toString(), callback)
        }
        productDetailTracking.eventAddToCartRecommendationWishlist(item, addToCartDoneViewModel.isLoggedIn(), isAddWishlist)
    }

    override fun onButtonGoToCartClicked() {
        productDetailTracking.eventClickAddToCart(addedProductDataModel?.productId ?: "",
                addedProductDataModel?.isVariant ?: false)
        goToCart()
    }

    override fun getComponent(): ProductDetailComponent = DaggerProductDetailComponent.builder()
            .baseAppComponent(
                    (activity?.applicationContext as BaseMainApplication).baseAppComponent
            ).build()

}