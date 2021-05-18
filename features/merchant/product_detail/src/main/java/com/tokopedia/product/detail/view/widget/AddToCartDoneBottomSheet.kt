package com.tokopedia.product.detail.view.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.component.BottomSheets.BottomSheetDismissListener
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.*
import com.tokopedia.product.detail.data.util.DynamicProductDetailTracking
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.adapter.AddToCartDoneAdapter
import com.tokopedia.product.detail.view.adapter.AddToCartDoneTypeFactory
import com.tokopedia.product.detail.view.util.ProductDetailErrorHandler
import com.tokopedia.product.detail.view.util.createProductCardOptionsModel
import com.tokopedia.product.detail.view.util.showToasterError
import com.tokopedia.product.detail.view.util.showToasterSuccess
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneAddedProductViewHolder
import com.tokopedia.product.detail.view.viewmodel.AddToCartDoneViewModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

open class AddToCartDoneBottomSheet :
        BottomSheetDialogFragment(),
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
    private var recomWishlistItem: RecommendationItem? = null
    private var addedProductDataModel: AddToCartDoneAddedProductDataModel? = null

    private var inflatedView: View? = null
    private var dismissListener: BottomSheetDismissListener? = null
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null

    fun setDismissListener(dismissListener: BottomSheetDismissListener) {
        this.dismissListener = dismissListener
    }

    fun getRecyclerView(): RecyclerView? {
        if(::recyclerView.isInitialized) {
            return recyclerView
        }
        return null
    }

    private fun configView(parentView: View) {
        val closeButton = parentView.findViewById<View>(R.id.btn_close)
        closeButton?.setOnClickListener { this@AddToCartDoneBottomSheet.dismiss() }
        shadow = parentView.findViewById(R.id.shadow)
        recyclerView = parentView.findViewById(R.id.recycler_view_add_to_cart_done)
        containerLayout = parentView.findViewById(R.id.container_layout)
        viewShimmeringLoading = parentView.findViewById(R.id.atc_done_bottomsheet_shimmering_loading)
        headerPrice = parentView.findViewById(R.id.heading_price)
        discountPercentage = parentView.findViewById(R.id.discount_percentage)
        stateAtcView = parentView.findViewById<Label>(R.id.state_atc)
        slashedPrice = parentView.findViewById(R.id.slashed_price)
        price = parentView.findViewById(R.id.price)
        addToCartButton = parentView.findViewById(R.id.btn_add_to_cart)

    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        dialog.run {
            inflatedView = View.inflate(context, R.layout.add_to_cart_done_bottomsheet, null)
            inflatedView?.let{
                configView(it)
                dialog.setContentView(it)
                initInjector()
                initViewModel()
                getArgumentsData()
                trackingQueue = TrackingQueue(context)
                initAdapter()
                observeRecommendationProduct()
                observeAtcStatus()
                getRecommendationProduct()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.let {
            it.setBackgroundColor(Color.TRANSPARENT)
            bottomSheetBehavior = BottomSheetBehavior.from(it)
            if(getRemoteConfig()?.getString(abNewPdpAfterAtcKey) != oldVariantPDP){
                dialog.setCanceledOnTouchOutside(false)
                recyclerView.isNestedScrollingEnabled = false
                val bottomSheetBehavior = BottomSheetBehavior.from<View>(it)
                bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                })
            }
        }
        dialog.setOnShowListener {
            val bottomSheet = (it as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            bottomSheet?.let{
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(true)
        }

        return dialog
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleProductCardOptionsActivityResult(requestCode, resultCode, data,
                object : ProductCardOptionsWishlistCallback {
                    override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                        handleWishlistAction(productCardOptionsModel)
                    }
                })
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (dismissListener != null) {
            dismissListener!!.onDismiss()
        }
        super.onDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface) {
        if (dismissListener != null) {
            dismissListener!!.onDismiss()
        }
        super.onCancel(dialog)
    }

    override fun onDestroy() {
        if (dismissListener != null) {
            dismissListener!!.onDismiss()
        }
        super.onDestroy()
    }

    override fun onDetach() {
        if (dismissListener != null) {
            dismissListener!!.onDismiss()
        }
        super.onDetach()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val fragmentTransaction = manager.beginTransaction()
        fragmentTransaction.add(this, tag)
        fragmentTransaction.commitAllowingStateLoss()
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
            if (result is Success) {
                stateAtcView.visible()
                addToCartButton.hide()
            } else if (result is Fail) {
                dialog?.run {
                    Toaster.toasterCustomBottomHeight = resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_80)
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
                    val abValue = getRemoteConfig()?.getString(abNewPdpAfterAtcKey)
                    if (abValue?.isNotEmpty() == true && abValue != oldVariantPDP) {
                        atcDoneAdapter.addElement(AddToCartDoneRecommendationCarouselDataModel(mapRecommendationWidgetToAddToCartRecommendationDataModel(it.data.first()), addedProductDataModel?.shopId
                                ?: -1))
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

    private fun mapRecommendationWidgetToAddToCartRecommendationDataModel(recommendationWidget: RecommendationWidget): AddToCartDoneRecommendationWidgetDataModel{
        return AddToCartDoneRecommendationWidgetDataModel(
                title = recommendationWidget.title,
                currentPage = recommendationWidget.currentPage,
                foreignTitle = recommendationWidget.foreignTitle,
                hasNext = recommendationWidget.hasNext,
                layoutType = recommendationWidget.layoutType,
                nextPage = recommendationWidget.nextPage,
                pageName = recommendationWidget.pageName,
                prevPage = recommendationWidget.prevPage,
                recommendationItemList = recommendationWidget.recommendationItemList.map { AddToCartDoneRecommendationItemDataModel(it) },
                seeMoreAppLink = recommendationWidget.seeMoreAppLink,
                source = recommendationWidget.source,
                tid = recommendationWidget.tid,
                widgetUrl = recommendationWidget.widgetUrl
        )
    }

    private fun showToasterRequestError(throwable: Throwable, onClickListener: View.OnClickListener) {
        dialog?.run {
            Toaster.showErrorWithAction(
                    findViewById(android.R.id.content),
                    ErrorHandler.getErrorMessage(this.context, throwable),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(com.tokopedia.abstraction.R.string.title_try_again),
                    onClickListener
            )
        }
    }

    private fun configBottomSheetHeight() {
        if(getRemoteConfig()?.getString(abNewPdpAfterAtcKey) == oldVariantPDP) {
            dialog?.run {
                val parent = findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
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
            dismissAllowingStateLoss()
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
        dismissAllowingStateLoss()
    }

    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        if (item.isTopAds) {
            context?.run {
                TopAdsUrlHitter(className).hitClickUrl(
                        this,
                        item.clickUrl,
                        item.productId.toString(),
                        item.name,
                        item.imageUrl
                )
            }
        }
        addedProductDataModel?.productId?.let {
            DynamicProductDetailTracking.Click.eventAddToCartRecommendationClick(
                    item,
                    item.position,
                    addToCartDoneViewModel.isLoggedIn(),
                    item.pageName,
                    item.header,
                    it
            )
        }
        if (position.size > 1) {
            lastAdapterPosition = position[0]
            goToPDP(item, position[1])
        } else {
            goToPDP(item, position[0])
        }
    }

    override fun onProductImpression(item: RecommendationItem) {
        if (item.isTopAds) {
            context?.run {
                TopAdsUrlHitter(className).hitImpressionUrl(
                        this,
                        item.trackerImageUrl,
                        item.productId.toString(),
                        item.name,
                        item.imageUrl
                )
            }
        }
        addedProductDataModel?.productId?.let {
            DynamicProductDetailTracking.Recommendation.eventAddToCartRecommendationImpression(
                    item.position,
                    item,
                    addToCartDoneViewModel.isLoggedIn(),
                    item.pageName,
                    item.header,
                    it,
                    trackingQueue
            )
        }
    }

    override fun onThreeDotsClick(item: RecommendationItem, vararg position: Int) {
        recomWishlistItem = item
        showProductCardOptions(
                this,
                item.createProductCardOptionsModel(position[0]))
    }

    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {
        if (isAddWishlist) {
            addToCartDoneViewModel.addWishList(item.productId.toString(), callback)
        } else {
            addToCartDoneViewModel.removeWishList(item.productId.toString(), callback)
        }
        DynamicProductDetailTracking.Click.eventAddToCartRecommendationWishlist(item, addToCartDoneViewModel.isLoggedIn(), isAddWishlist)
    }

    override fun onRecommendationItemSelected(dataModel: AddToCartDoneRecommendationItemDataModel, position: Int) {
        val item = dataModel.recommendationItem
        shadow.visible()
        price.visible()
        addToCartButton.visible()
        stateAtcView.visibility = if(dataModel.isAddedToCart) View.VISIBLE else View.GONE
        addToCartButton.visibility = if(!dataModel.isAddedToCart) View.VISIBLE else View.GONE
        discountPercentage.visibility = if(item.discountPercentage.isNotBlank()) View.VISIBLE else View.GONE
        slashedPrice.visibility = if(item.discountPercentage.isNotBlank()) View.VISIBLE else View.GONE
        headerPrice.visibility = if(!item.discountPercentage.isNotBlank()) View.VISIBLE else View.GONE
        discountPercentage.text = item.discountPercentage
        slashedPrice.text = item.slashedPrice
        slashedPrice.paintFlags = slashedPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        price.text = item.price

        addToCartButton.setOnClickListener {
            if(!addToCartButton.isLoading){
                addToCartButton.isLoading = true
                addToCartDoneViewModel.addToCart(dataModel)
                addedProductDataModel?.productId?.let {
                    DynamicProductDetailTracking.Click.eventAddToCartRecommendationATCClick(
                            item,
                            item.position,
                            addToCartDoneViewModel.isLoggedIn(),
                            item.pageName,
                            item.header,
                            it
                    )
                }
            }
        }
    }

    override fun onButtonGoToCartClicked() {
        DynamicProductDetailTracking.Click.eventAtcClickLihat(addedProductDataModel?.productId ?: "")
        goToCart()
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel?) {
        if (productCardOptionsModel == null) return

        val wishlistResult = productCardOptionsModel.wishlistResult
        if (wishlistResult.isUserLoggedIn) {
            if (wishlistResult.isSuccess) {
                recomWishlistItem?.isWishlist = !(recomWishlistItem?.isWishlist ?: false)
                recomWishlistItem?.let { DynamicProductDetailTracking.Click.eventAddToCartRecommendationWishlist(it, addToCartDoneViewModel.isLoggedIn(), wishlistResult.isAddWishlist) }
                view?.showToasterSuccess(
                        message = if(wishlistResult.isAddWishlist) getString(com.tokopedia.topads.sdk.R.string.msg_success_add_wishlist) else getString(com.tokopedia.topads.sdk.R.string.msg_success_remove_wishlist),
                        ctaText = getString(R.string.recom_go_to_wishlist),
                        ctaListener = {
                            goToWishlist()
                        }
                )
            } else {
                view?.showToasterError(
                        if(wishlistResult.isAddWishlist) getString(com.tokopedia.topads.sdk.R.string.msg_error_add_wishlist) else getString(com.tokopedia.topads.sdk.R.string.msg_error_remove_wishlist)
                )
            }
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    private fun goToWishlist(){
        RouteManager.route(context, ApplinkConst.WISHLIST)
    }

    private fun getRemoteConfig(): RemoteConfig? {
        return try{
            RemoteConfigInstance.getInstance().abTestPlatform
        }catch (e: Exception){
            activity?.application?.let { RemoteConfigInstance.initAbTestPlatform(it) }
            return try {
                RemoteConfigInstance.getInstance().abTestPlatform
            }catch (e: Exception){
                null
            }
        }
    }

    override fun getComponent(): ProductDetailComponent = DaggerProductDetailComponent.builder()
            .baseAppComponent(
                    (activity?.applicationContext as BaseMainApplication).baseAppComponent
            ).build()

}