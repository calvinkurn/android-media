package com.tokopedia.product.detail.view.widget

import android.app.Dialog
import android.graphics.Paint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductDataModel
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationCarouselDataModel
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationDataModel
import com.tokopedia.product.detail.data.util.DynamicProductDetailTracking
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.adapter.AddToCartDoneAdapter
import com.tokopedia.product.detail.view.adapter.AddToCartDoneTypeFactory
import com.tokopedia.product.detail.view.util.ProductDetailErrorHandler
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneAddedProductViewHolder
import com.tokopedia.product.detail.view.viewmodel.AddToCartDoneViewModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
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
        private const val abNewPdpAfterAtcKey = "PDP ATC 2020"
        private const val oldVariantPDP = "PDP after ATC"
        private const val className: String = "com.tokopedia.product.detail.view.widget.AddToCartDoneBottomSheet"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var productDetailTracking: ProductDetailTracking
    private lateinit var trackingQueue: TrackingQueue
    private lateinit var addToCartDoneViewModel: AddToCartDoneViewModel
    private lateinit var atcDoneAdapter: AddToCartDoneAdapter
    private var lastAdapterPosition = -1
    private lateinit var recyclerView: RecyclerView
    private lateinit var containerLayout: ConstraintLayout
    private lateinit var viewShimmeringLoading: View
    private lateinit var headerPrice: Typography
    private lateinit var discountPercentage: Label
    private lateinit var slashedPrice: Typography
    private lateinit var price: Typography
    private lateinit var shadow: View
    private lateinit var stateAtcView: View
    private lateinit var addToCartButton: UnifyButton
    private var addedProductDataModel: AddToCartDoneAddedProductDataModel? = null

    override fun getLayoutResourceId(): Int {
        return R.layout.add_to_cart_done_bottomsheet
    }

    override fun getBaseLayoutResourceId(): Int {
        return R.layout.widget_bottomsheet_add_to_cart_done
    }

    override fun initView(view: View?) {
        view?.let {
            shadow = it.findViewById(R.id.shadow)
            recyclerView = it.findViewById(R.id.recycler_view_add_to_cart_done)
            containerLayout = it.findViewById(R.id.container_layout)
            viewShimmeringLoading = it.findViewById(R.id.atc_done_bottomsheet_shimmering_loading)
            headerPrice = view.findViewById<Typography>(R.id.heading_price)
            discountPercentage = view.findViewById<Label>(R.id.discount_percentage)
            stateAtcView = view.findViewById<Label>(R.id.state_atc)
            slashedPrice = view.findViewById<Typography>(R.id.slashed_price)
            price = view.findViewById<Typography>(R.id.price)
            addToCartButton = view.findViewById<UnifyButton>(R.id.btn_add_to_cart)
        }
    }

    override fun title(): String {
        return ""
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
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
        observeAtcStatus()
        getRecommendationProduct()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun getRecommendationProduct() {
        addedProductDataModel?.productId?.let {
            viewShimmeringLoading.show()
            addToCartDoneViewModel.getRecommendationProduct(it)
        }
    }

    private fun initAdapter() {
        val factory = AddToCartDoneTypeFactory(this, this)
        atcDoneAdapter = AddToCartDoneAdapter(factory)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.animation= null
        recyclerView.adapter = atcDoneAdapter
    }

    private fun observeAtcStatus(){
        addToCartDoneViewModel.addToCartLiveData.observe(this, Observer { result ->
            addToCartButton.isLoading = false
            if(result is Success){
                stateAtcView.visible()
                addToCartButton.hide()
            } else if(result is Fail){
                dialog?.run{
                    Toaster.toasterCustomBottomHeight = resources.getDimensionPixelOffset(R.dimen.dp_80)
                    Toaster.make(findViewById(android.R.id.content),
                            ProductDetailErrorHandler.getErrorMessage(context, result.throwable),
                            Snackbar.LENGTH_LONG,
                            Toaster.TYPE_ERROR
                    )

                }
            }
        })
    }

    private fun observeRecommendationProduct() {
        addToCartDoneViewModel.recommendationProduct.observe(this, Observer {
            when (it) {
                is Success -> {
                    viewShimmeringLoading.hide()
                    atcDoneAdapter.clearAllElements()
                    if(RemoteConfigInstance.getInstance().abTestPlatform.getString(abNewPdpAfterAtcKey) != oldVariantPDP){
                        atcDoneAdapter.addElement(AddToCartDoneRecommendationCarouselDataModel(it.data.first(), addedProductDataModel?.shopId ?: -1))
                    } else {
                        for (res in it.data) {
                            atcDoneAdapter.addElement(AddToCartDoneRecommendationDataModel(res))
                        }
                    }
                    atcDoneAdapter.addElement(0, addedProductDataModel)
                    atcDoneAdapter.notifyDataSetChanged()
                }
                is Fail -> {
                    showToasterRequestError(
                            it.throwable,
                            View.OnClickListener {
                                getRecommendationProduct()
                            }
                    )
                }
            }
            configBottomSheetHeight()
        })
    }

    private fun showToasterRequestError(throwable: Throwable, onClickListener: View.OnClickListener) {
        dialog?.run {
            Toaster.showErrorWithAction(
                    findViewById(android.R.id.content),
                    ErrorHandler.getErrorMessage(this.context, throwable),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.title_try_again),
                    onClickListener
            )
        }
    }

    private fun configBottomSheetHeight() {
        if(RemoteConfigInstance.getInstance().abTestPlatform.getString(abNewPdpAfterAtcKey) == oldVariantPDP) {
            dialog?.run {
                val parent = findViewById<FrameLayout>(R.id.design_bottom_sheet)
                val displaymetrics = DisplayMetrics()
                activity?.windowManager?.defaultDisplay?.getMetrics(displaymetrics)
                val screenHeight = displaymetrics.heightPixels
                val maxHeight = (screenHeight * 0.9f).toInt()
                val params = parent.layoutParams
                params.height = maxHeight
                parent.layoutParams = params
            }
        }
    }

    private fun getArgumentsData() {
        arguments?.let {
            addedProductDataModel = it.getParcelable(KEY_ADDED_PRODUCT_DATA_MODEL)
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
            dismiss()
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
        dismiss()
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FLEXIBLE
    }

    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        if (item.isTopAds) ImpresionTask(className).execute(item.clickUrl)
        DynamicProductDetailTracking.Click.eventAddToCartRecommendationClick(
                item,
                item.position,
                addToCartDoneViewModel.isLoggedIn(),
                item.pageName,
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
        if (item.isTopAds) ImpresionTask(className).execute(item.trackerImageUrl)
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

    override fun onRecommendationItemSelected(recommendation: RecommendationItem, position: Int) {
        shadow.visible()
        price.visible()
        addToCartButton.visible()
        stateAtcView.hide()
        addToCartButton.show()
        discountPercentage.visibility = if(recommendation.discountPercentage.isNotBlank()) View.VISIBLE else View.GONE
        slashedPrice.visibility = if(recommendation.discountPercentage.isNotBlank()) View.VISIBLE else View.GONE
        headerPrice.visibility = if(!recommendation.discountPercentage.isNotBlank()) View.VISIBLE else View.GONE
        discountPercentage.text = recommendation.discountPercentage
        slashedPrice.text = recommendation.slashedPrice
        slashedPrice.paintFlags = slashedPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        price.text = recommendation.price
        addToCartButton.setOnClickListener {
            if(!addToCartButton.isLoading){
                addToCartButton.isLoading = true
                addToCartDoneViewModel.addToCart(recommendation)
                productDetailTracking.eventAddToCartRecommendationATCClick(
                        recommendation,
                        recommendation.position,
                        addToCartDoneViewModel.isLoggedIn(),
                        recommendation.pageName,
                        recommendation.header
                )
            }
        }
    }

    override fun onButtonGoToCartClicked() {
        productDetailTracking.eventAtcClickLihat(addedProductDataModel?.productId ?: "")
        goToCart()
    }

    override fun getComponent(): ProductDetailComponent = DaggerProductDetailComponent.builder()
            .baseAppComponent(
                    (activity?.applicationContext as BaseMainApplication).baseAppComponent
            ).build()

}