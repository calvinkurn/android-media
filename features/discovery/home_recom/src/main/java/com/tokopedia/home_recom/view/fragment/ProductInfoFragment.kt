package com.tokopedia.home_recom.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.analytics.RecommendationPageTracking
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.util.RecommendationPageErrorHandler
import com.tokopedia.home_recom.util.Status
import com.tokopedia.home_recom.util.fadeShow
import com.tokopedia.home_recom.util.startFade
import com.tokopedia.home_recom.view.fragment.ProductInfoFragment.Companion.WIHSLIST_STATUS_IS_WISHLIST
import com.tokopedia.home_recom.viewmodel.PrimaryProductViewModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.error_product_info_layout.*
import kotlinx.android.synthetic.main.fragment_product_info.*
import javax.inject.Inject

/**
 * Created by Lukas 25/06/2019
 *
 * A Fragment class for Primary Product
 *
 * @property viewModelFactory the factory for ViewModel provide by Dagger.
 * @property trackingQueue the queue util for handle tracking.
 * @property ref the ref code for know source page.
 * @property viewModelProvider the viewModelProvider by Dagger
 * @property primaryProductViewModel the viewModel for Primary Product.
 * @property productView the view for Primary Product.
 * @property productDataModel the model for Primary Product.
 * @property recommendationItem the model for handle tracker.
 * @property menu the menu of this activity.
 * @property WIHSLIST_STATUS_IS_WISHLIST the const value for get extras `isWhislist` from ActivityFromResult ProductDetailActivity.
 * @property REQUEST_CODE_PDP the const value for set request calling startActivityForResult ProductDetailActivity.
 * @property REQUEST_CODE_LOGIN the const value for set request calling startActivityForResult LoginActivity.
 * @constructor Creates an empty recommendation.
 */
class ProductInfoFragment : BaseDaggerFragment() {

    private val REQUEST_CODE_LOGIN = 283
    private val REQUEST_CODE_PDP = 284

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModelProvider: ViewModelProvider

    private lateinit var primaryProductViewModel: PrimaryProductViewModel

    private lateinit var trackingQueue: TrackingQueue

    private lateinit var ref: String

    private lateinit var productId: String

    private lateinit var queryParam: String

    private lateinit var productView: View

    private lateinit var recommendationItem: RecommendationItem


    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(HomeRecommendationComponent::class.java).inject(this)
    }

    companion object{

        fun newInstance(productId: String, ref: String, queryParam: String) = ProductInfoFragment().apply {
            this.productId = productId
            this.ref = ref
            this.queryParam = queryParam
        }

        private const val WISHLIST_STATUS_UPDATED_POSITION = "wishlistUpdatedPosition"

        val CART_ID = "cartId"
        val MESSAGE = "message"
        val STATUS = "status"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            primaryProductViewModel = viewModelProvider.get(PrimaryProductViewModel::class.java)
            trackingQueue = TrackingQueue(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        productView = inflater.inflate(R.layout.fragment_product_info, container, false)
        return productView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureContentView(false)
        parentFragment?.viewLifecycleOwner?.let {
            primaryProductViewModel.productInfoDataModel.observe(it, Observer {
            it?.let {
                response ->
                container_loading.hide()
                container_error.hide()
                container_empty.hide()
                container_product.hide()
                when(response.status){
                    Status.LOADING -> {
                        container_loading.fadeShow()
                        container_error.hide()
                        container_empty.hide()
                        container_product.hide()
                    }
                    Status.SUCCESS -> {
                        container_loading.hide()
                        container_error.hide()
                        container_empty.hide()
                        container_product.fadeShow()
                        response.data?.let { primaryProduct ->
                            initView(primaryProduct)
                        }
                    }
                    Status.EMPTY -> {
                        container_loading.hide()
                        container_error.hide()
                        container_product.hide()
                        container_empty.fadeShow()
                    }
                    else -> {
                        container_loading.hide()
                        container_empty.hide()
                        container_product.hide()
                        container_error.fadeShow()
                        buttonReload.setOnClickListener {
                            primaryProductViewModel.getPrimaryProduct(productId, queryParam)
                        }
                    }
                }
            }
        })
        }
    }

    /**
     * Void [initView] for render UI with the real data
     * @param productDataModel is the data model for fill the UI with view model
     */
    private fun initView(productDataModel: ProductInfoDataModel){
        configureContentView(true)
        recommendationItem = mapToRecommendationItem(productDataModel)
        product_name.text = productDataModel.productDetailData.name
        handleDiscount(productDataModel.productDetailData.discountPercentage, productDataModel.productDetailData.slashedPrice)
        product_price.text = productDataModel.productDetailData.price
        location.text = productDataModel.productDetailData.shop.location
        if (productDataModel.productDetailData.badges.isNotEmpty()) {
            badge.visibility = View.VISIBLE
            ImageHandler.loadImageFitCenter(context, badge, productDataModel.productDetailData.badges[0].imageUrl)
        } else {
            badge.visibility = View.GONE
        }
        updateWishlist(productDataModel.productDetailData.isWishlist)
        ImageHandler.loadImageRounded2(context, product_image, productDataModel.productDetailData.imageUrl)
        setRatingReviewCount(productDataModel.productDetailData.rating, productDataModel.productDetailData.countReview)

        when(productDataModel.productDetailData.status){
            0 -> {
                container_loading.hide()
                container_error.hide()
                container_product.hide()
                container_empty.fadeShow()
            }
            3 -> {
                add_to_cart.isEnabled = false
                buy_now.hide()
                add_to_cart.text = "Stok Habis"
            }
        }

        onProductImpression()
        onClickAddToCart(productDataModel.productDetailData.id, productDataModel.productDetailData.shop.id, productDataModel.productDetailData.minOrder)
        onClickBuyNow(productDataModel.productDetailData.id, productDataModel.productDetailData.shop.id, productDataModel.productDetailData.minOrder)
        onClickProductCard(productDataModel.productDetailData.id.toString())
        onClickWishlist(productDataModel.productDetailData.id.toString())
    }

    /**
     * Void [configureContentView]
     * This void handle show or hide content view on primary product
     * @param isShow true or false, if true it will show all content
     */
    private fun configureContentView(isShow: Boolean){
        val show = if(isShow) View.VISIBLE else View.GONE
        product_image.startFade(isShow)
        fab_detail.startFade(isShow)
        product_name.startFade(isShow)
        product_discount.startFade(isShow)
        product_slashed_price.startFade(isShow)
        product_price.startFade(isShow)
        badge.startFade(isShow)
        location.startFade(isShow)
        rating.startFade(isShow)
        review_count.startFade(isShow)
        buy_now.startFade(isShow)
        add_to_cart.startFade(isShow)
//        product_image.visibility = show
//        fab_detail.visibility = show
//        product_name.visibility = show
//        product_discount.visibility = show
//        product_slashed_price.visibility = show
//        product_price.visibility = show
//        badge.visibility = show
//        location.visibility = show
//        rating.visibility = show
//        review_count.visibility = show
//        buy_now.visibility = show
//        add_to_cart.visibility = show
    }

    /**
     * [onProductImpression] it will handle impression image tracking
     */
    private fun onProductImpression(){
        product_image.addOnImpressionListener(recommendationItem, object: ViewHintListener{
            override fun onViewHint() {
                RecommendationPageTracking.eventImpressionPrimaryProductWithProductId(recommendationItem, "0", ref)
            }
        })
    }

    /**
     * [goToCart] it will handle routing to cart page
     */
    private fun goToCart(){
        RouteManager.route(context, ApplinkConst.CART)
    }

    /**
     * [onClickProductCard] it will handle product click
     */
    private fun onClickProductCard(productId: String){
        product_card.setOnClickListener {
            RecommendationPageTracking.eventClickPrimaryProductWithProductId(recommendationItem, "0", ref)
            val intent = RouteManager.getIntent(
                    context,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    productId)
            startActivityForResult(intent, REQUEST_CODE_PDP)
        }
    }

    /**
     * [onClickAddToCart] it will handle click add to cart
     */
    private fun onClickAddToCart(productId: Int, shopId: Int, minOrder: Int){
        add_to_cart.setOnClickListener {
            if (primaryProductViewModel.isLoggedIn()) {
                add_to_cart.isEnabled = false
                addToCart(
                        productId, shopId, minOrder,
                        success = { result ->
                            recommendationItem.cartId = result[CART_ID] as Int
                            RecommendationPageTracking.eventUserClickAddToCartWithProductId(recommendationItem, ref)
                            add_to_cart.isEnabled = true
                            if(result.containsKey(STATUS) && !(result[STATUS] as Boolean)){
                                showToastError(MessageErrorException(result[MESSAGE].toString()))
                            }else{
                                showToastSuccessWithAction(result[MESSAGE].toString(), getString(R.string.recom_see_cart)){
                                    RecommendationPageTracking.eventUserClickSeeToCartWithProductId()
                                    goToCart()
                                }
                            }
                        },
                        error = {
                            add_to_cart.isEnabled = true
                            showToastError(it)
                        }
                )
            } else {
                context?.let {
                    RecommendationPageTracking.eventUserAddToCartNonLoginWithProductId(ref)
                    startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                            REQUEST_CODE_LOGIN)
                }
            }
        }
    }

    /**
     * [onClickBuyNow] it will handle click buy now
     */
    private fun onClickBuyNow(productId: Int, shopId: Int, minOrder: Int){
        buy_now.setOnClickListener {
            if (primaryProductViewModel.isLoggedIn()){
                buy_now.isEnabled = false
                addToCart(
                        productId, shopId, minOrder,
                        success = { result ->
                            buy_now.isEnabled = true
                            if(result.containsKey(STATUS) && !(result[STATUS] as Boolean)){
                                showToastError(MessageErrorException(result[MESSAGE].toString()))
                            }else if(result.containsKey(CART_ID) && result[CART_ID].toString().isNotEmpty()){
                                RecommendationPageTracking.eventUserClickBuyWithProductId(recommendationItem, ref)
                                goToCart()
                            }
                        },
                        error = {
                            buy_now.isEnabled = true
                            showToastError(it)
                        }
                )
            } else {
                RecommendationPageTracking.eventUserClickBuyNonLoginWithProductId(ref)
                context?.let {
                    startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                            REQUEST_CODE_LOGIN)
                }
            }
        }
    }


    /**
     * [onClickWishlist] it will handle click wishlist icon
     */
    private fun onClickWishlist(productId: String){
        fab_detail.setOnClickListener {
            if (primaryProductViewModel.isLoggedIn()) {
                RecommendationPageTracking.eventUserClickProductToWishlistForUserLoginWithProductId(!it.isActivated, ref)
                if (it.isActivated) {
                    primaryProductViewModel.removeWishList(productId,
                            onSuccessRemoveWishlist = this::onSuccessRemoveWishlist,
                            onErrorRemoveWishList = this::onErrorRemoveWishList)
                } else {
                    primaryProductViewModel.addWishList(productId,
                            onSuccessAddWishlist = this::onSuccessAddWishlist,
                            onErrorAddWishList = this::onErrorAddWishList)
                }
            } else {
                RecommendationPageTracking.eventUserClickProductToWishlistForNonLoginWithProductId(ref)
                RouteManager.route(activity, ApplinkConst.LOGIN)
            }
        }
    }

    /**
     * [addToCart] it will handle click add to cart
     * @param success success callback
     * @param error error calback
     */
    private fun addToCart(
            productId: Int,
            shopId: Int,
            minOrder: Int,
            success: (Map<String, Any>) -> Unit,
            error: (Throwable) -> Unit
    ){
        val addToCartRequestParams = AddToCartRequestParams()
        addToCartRequestParams.productId = productId.toLong()
        addToCartRequestParams.shopId = shopId
        addToCartRequestParams.quantity = minOrder
        addToCartRequestParams.notes = ""

        primaryProductViewModel.addToCart(addToCartRequestParams, success, error)
    }

    /**
     * [onErrorRemoveWishList] it will error when remove wishlist
     * @param errorMessage the error message will show at toaster
     */
    private fun onErrorRemoveWishList(errorMessage: String?) {
        showToastError(MessageErrorException(errorMessage))
    }

    /**
     * [onSuccessRemoveWishlist] it will handle show toaster success when remove wishlist success
     * @param productId the product id of primary product
     */
    private fun onSuccessRemoveWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_remove_wishlist))
        updateWishlist(false)
        sendIntentResusltWishlistChange(productId ?: "", false)

    }

    /**
     * [sendIntentResusltWishlistChange] it will handle send result when wishlist success / error
     */
    private fun sendIntentResusltWishlistChange(productId: String, isInWishlist: Boolean) {
        val resultIntent = Intent()
                .putExtra(WISHLIST_STATUS_UPDATED_POSITION, activity?.intent?.getIntExtra(WISHLIST_STATUS_UPDATED_POSITION, -1))
        resultIntent.putExtra(WIHSLIST_STATUS_IS_WISHLIST, isInWishlist)
        resultIntent.putExtra("product_id", productId)
        activity?.setResult(Activity.RESULT_CANCELED, resultIntent)
    }

    /**
     * [onErrorAddWishList] it will handle show error when addWishlist failed
     */
    private fun onErrorAddWishList(errorMessage: String?) {
        showToastError(MessageErrorException(errorMessage))
    }

    /**
     * [onSuccessAddWishlist] it will handle show success when addWishlist success
     * and update icon wishlist
     */
    private fun onSuccessAddWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_add_wishlist))
        updateWishlist(true)
        sendIntentResusltWishlistChange(productId ?: "", true)
    }

    /**
     * [showToastSuccessWithAction] it will handle toaster with success background and show action
     * @param message it will be show at toaster
     * @param actionString the action text
     * @param action the action callback when action clicked
     */
    private fun showToastSuccessWithAction(message: String, actionString: String, action: () -> Unit){
        activity?.run {
            Toaster.showNormalWithAction(
                    findViewById(android.R.id.content),
                    message,
                    Snackbar.LENGTH_LONG,
                    actionString,
                    View.OnClickListener {
                        action.invoke()
                    }
            )
        }
    }

    /**
     * [showToastError] it will handle toaster with error background
     * @param throwable the throwable error
     */
    private fun showToastError(throwable: Throwable) {
        activity?.run {
            Toaster.showError(
                    findViewById(android.R.id.content),
                    RecommendationPageErrorHandler.getErrorMessage(this, throwable),
                    Snackbar.LENGTH_LONG)
        }
    }

    /**
     * [showToastSuccess] it will handle toaster with success background
     * @param message it will be show at toaster
     */
    private fun showToastSuccess(message: String) {
        activity?.run {
            Toaster.showNormal(
                    findViewById(android.R.id.content),
                    message,
                    Snackbar.LENGTH_LONG)
        }
    }

    /**
     * [setRatingReviewCount] it will set rating and review when available
     * @param ratingValue the value for rating
     * @param review the count of review
     */
    private fun setRatingReviewCount(ratingValue: Int, review: Int){
        if (ratingValue in 1..5) {
            rating.setImageResource(getRatingDrawable(ratingValue))
            review_count.text = String.format(getString(R.string.recom_review_count), review)
        } else {
            rating.visibility = View.GONE
            review_count.visibility = View.GONE
        }
    }

    /**
     * [getRatingDrawable] it will checking rating and return drawable resource
     * @return drawable resource
     */
    private fun getRatingDrawable(rating: Int): Int {
        return when (rating) {
            0 -> R.drawable.ic_star_none
            1 -> R.drawable.ic_star_one
            2 -> R.drawable.ic_star_two
            3 -> R.drawable.ic_star_three
            4 -> R.drawable.ic_star_four
            5 -> R.drawable.ic_star_five
            else -> R.drawable.ic_star_none
        }
    }

    /**
     * [setSplashedText] it will handle splashed text
     */
    private fun setSplashedText(text: String){
        product_slashed_price.text = text
        product_slashed_price.paintFlags = product_slashed_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    /**
     * [updateWishlist] it will handle update ui wishlist
     * @param wishlisted boolean true or false
     */
    private fun updateWishlist(wishlisted: Boolean) {
        context?.let {
            if (wishlisted) {
                fab_detail.isActivated = true
                fab_detail.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_product_action_wishlist_added_28))
            } else {
                fab_detail.isActivated = false
                fab_detail.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_product_action_wishlist_gray_28))
            }
        }
    }

    /**
     * [mapToRecommendationItem] it will handle mapping from productDataModel to RecommendationItem
     * @return [RecommendationItem]
     */
    private fun mapToRecommendationItem(productDataModel: ProductInfoDataModel) = RecommendationItem(
            productId = productDataModel.productDetailData.id,
            position = 0,
            name = productDataModel.productDetailData.name,
            appUrl = productDataModel.productDetailData.appUrl,
            clickUrl = productDataModel.productDetailData.clickUrl,
            categoryBreadcrumbs = productDataModel.productDetailData.categoryBreadcrumbs,
            countReview = productDataModel.productDetailData.countReview,
            departmentId = productDataModel.productDetailData.departmentId,
            imageUrl = productDataModel.productDetailData.imageUrl,
            isTopAds = productDataModel.productDetailData.isTopads,
            isWishlist = productDataModel.productDetailData.isWishlist,
            price = productDataModel.productDetailData.price,
            priceInt = productDataModel.productDetailData.priceInt,
            rating = productDataModel.productDetailData.rating,
            recommendationType = productDataModel.productDetailData.recommendationType,
            stock = productDataModel.productDetailData.stock,
            trackerImageUrl = productDataModel.productDetailData.trackerImageUrl,
            url = productDataModel.productDetailData.url,
            wishlistUrl = productDataModel.productDetailData.wishlistUrl,
            slashedPrice = productDataModel.productDetailData.slashedPrice,
            discountPercentageInt = productDataModel.productDetailData.discountPercentage,
            slashedPriceInt = productDataModel.productDetailData.slashedPriceInt,
            cartId = -1,
            shopId = productDataModel.productDetailData.shop.id,
            shopName = productDataModel.productDetailData.shop.name,
            shopType = if(productDataModel.productDetailData.shop.isGold) "gold_merchant" else "reguler",
            quantity = productDataModel.productDetailData.minOrder,
            header = "",
            pageName = "",
            minOrder = productDataModel.productDetailData.minOrder,
            location = "",
            badgesUrl = listOf(),
            type = "",
            isFreeOngkirActive = false,
            freeOngkirImageUrl = "",
            discountPercentage = "",
            labelOffers = RecommendationLabel(),
            labelCredibility = RecommendationLabel(),
            labelPromo = RecommendationLabel(),
            isGold = false
    )

    /**
     * [handleDiscount] for handle discount ui if discount percentage available it will show
     */
    private fun handleDiscount(discountPercentage: Int, slashedPrice: String){
        if(discountPercentage > 0){
            product_discount.visibility = View.VISIBLE
            product_slashed_price.visibility = View.VISIBLE
            product_discount.text = "${discountPercentage}%"
            setSplashedText(slashedPrice)
        } else {
            product_discount.visibility = View.GONE
            product_slashed_price.visibility = View.GONE
        }
    }

    /**
     * [onActivityResult] override from [BaseDaggerFragment]
     * this void for handle request from PDP to update wishlist icon
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PDP) {
            data?.let {
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST, false)
                updateWishlist(wishlistStatusFromPdp)
            }
        }
    }
}