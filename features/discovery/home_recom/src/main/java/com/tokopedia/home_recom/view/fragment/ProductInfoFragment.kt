package com.tokopedia.home_recom.view.fragment

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.home_recom.viewmodel.PrimaryProductViewModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_product_info.product_card
import kotlinx.android.synthetic.main.fragment_product_info.buy_now
import kotlinx.android.synthetic.main.fragment_product_info.add_to_cart
import kotlinx.android.synthetic.main.fragment_product_info.pb_buy_now
import kotlinx.android.synthetic.main.fragment_product_info.product_name
import kotlinx.android.synthetic.main.fragment_product_info.product_price
import kotlinx.android.synthetic.main.fragment_product_info.product_discount
import kotlinx.android.synthetic.main.fragment_product_info.fab_detail
import kotlinx.android.synthetic.main.fragment_product_info.review_count
import kotlinx.android.synthetic.main.fragment_product_info.rating
import kotlinx.android.synthetic.main.fragment_product_info.product_slashed_price
import kotlinx.android.synthetic.main.fragment_product_info.location
import kotlinx.android.synthetic.main.fragment_product_info.badge
import kotlinx.android.synthetic.main.fragment_product_info.pb_add_to_cart
import kotlinx.android.synthetic.main.fragment_product_info.product_image
import kotlinx.android.synthetic.main.fragment_product_info.bg_product_info
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class ProductInfoFragment : BaseDaggerFragment() {

    private val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
    private val REQUEST_CODE_LOGIN = 283
    private val REQUEST_CODE_PDP = 284

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy{ ViewModelProviders.of(this, viewModelFactory) }

    private val primaryProductViewModel by lazy { viewModelProvider.get(PrimaryProductViewModel::class.java) }

    private lateinit var trackingQueue: TrackingQueue

    private lateinit var productDataModel: ProductInfoDataModel

    private lateinit var productView: View

    private val recommendationItem: RecommendationItem by lazy { mapToRecommendationItem() }


    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(HomeRecommendationComponent::class.java).inject(this)
    }

    companion object{
        fun newInstance(dataModel: ProductInfoDataModel) = ProductInfoFragment().apply {
            this.productDataModel = dataModel
        }

        private const val WISHLIST_STATUS_UPDATED_POSITION = "wishlistUpdatedPosition"

        val CART_ID = "cartId"
        val MESSAGE = "message"
        val STATUS = "status"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            trackingQueue = TrackingQueue(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        productView = inflater.inflate(R.layout.fragment_product_info, container, false)
        return productView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(this::productDataModel.isInitialized && productDataModel != null) {
            product_name.text = productDataModel.productDetailData.name
            handleDiscount()
            bg_product_info.setImageResource(R.drawable.background_product_info)
            product_price.text = productDataModel.productDetailData.price
            location.text = productDataModel.productDetailData.shop.location
            if (productDataModel.productDetailData.badges.isNotEmpty()) {
                badge.visibility = View.VISIBLE
                ImageHandler.loadImageFitCenter(view.context, badge, productDataModel.productDetailData.badges[0].imageUrl)
            } else {
                badge.visibility = View.GONE
            }
            updateWishlist(productDataModel.productDetailData.isWishlist)
            ImageHandler.loadImageRounded2(view.context, product_image, productDataModel.productDetailData.imageUrl)
            setRatingReviewCount(productDataModel.productDetailData.rating, productDataModel.productDetailData.countReview)


            onProductImpression()
            onClickAddToCart()
            onClickBuyNow()
            onClickProductCard()
            onClickWishlist()
        }
    }

    private fun onProductImpression(){
        product_image.addOnImpressionListener(recommendationItem, object: ViewHintListener{
            override fun onViewHint() {
                RecommendationPageTracking.eventImpressionPrimaryProduct(recommendationItem, "0")
                if(productDataModel.productDetailData.isTopads){
                    onImpressionTopAds(recommendationItem)
                }else {
                    onImpressionOrganic(recommendationItem)
                }
            }
        })
    }

    private fun goToCart(){
        RouteManager.route(context, ApplinkConst.CART)
    }

    private fun onClickProductCard(){
        product_card.setOnClickListener {
            RecommendationPageTracking.eventClickPrimaryProduct(recommendationItem, "0")
            if (productDataModel.productDetailData.isTopads) {
                onClickTopAds(recommendationItem)
            } else {
                onClickOrganic(recommendationItem)
            }
            val intent = RouteManager.getIntent(
                    context,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    productDataModel.productDetailData.id.toString())
            startActivityForResult(intent, REQUEST_CODE_PDP)
        }
    }

    private fun onClickAddToCart(){
        add_to_cart.setOnClickListener {
            if (primaryProductViewModel.isLoggedIn()) {
                pb_add_to_cart.show()
                addToCart(
                        success = { result ->
                            recommendationItem.cartId = result[CART_ID] as Int
                            RecommendationPageTracking.eventUserClickAddToCart(recommendationItem)
                            pb_add_to_cart.hide()
                            if(result.containsKey(STATUS) && !(result[STATUS] as Boolean)){
                                showToastError(MessageErrorException(result[MESSAGE].toString()))
                            }else{
                                showToastSuccessWithAction(result[MESSAGE].toString(), getString(R.string.recom_see_cart)){
                                    RecommendationPageTracking.eventUserClickSeeToCart()
                                    goToCart()
                                }
                            }
                        },
                        error = {
                            pb_add_to_cart.hide()
                            showToastError(it)
                        }
                )
            } else {
                context?.let {
                    RecommendationPageTracking.eventUserAddToCartNonLogin()
                    startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                            REQUEST_CODE_LOGIN)
                }
            }
        }
    }

    private fun onClickBuyNow(){
        buy_now.setOnClickListener {
            if (primaryProductViewModel.isLoggedIn()){
                pb_buy_now.show()
                addToCart(
                        success = { result ->
                            pb_buy_now.hide()
                            if(result.containsKey(STATUS) && !(result[STATUS] as Boolean)){
                                showToastError(MessageErrorException(result[MESSAGE].toString()))
                            }else if(result.containsKey(CART_ID) && result[CART_ID].toString().isNotEmpty()){
                                RecommendationPageTracking.eventUserClickBuy(recommendationItem)
                                goToCart()
                            }
                        },
                        error = {
                            pb_buy_now.hide()
                            showToastError(it)
                        }
                )
            } else {
                RecommendationPageTracking.eventUserClickBuyNonLogin()
                context?.let {
                    startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                            REQUEST_CODE_LOGIN)
                }
            }
        }
    }

    private fun onClickWishlist(){
        fab_detail.setOnClickListener {
            if (primaryProductViewModel.isLoggedIn()) {
                RecommendationPageTracking.eventUserClickProductToWishlistForUserLogin(!it.isActivated)
                if (it.isActivated) {
                    productDataModel.productDetailData.id.let {
                        primaryProductViewModel.removeWishList(it.toString(),
                                onSuccessRemoveWishlist = this::onSuccessRemoveWishlist,
                                onErrorRemoveWishList = this::onErrorRemoveWishList)
                    }

                } else {
                    productDataModel.productDetailData.id.let {
                        primaryProductViewModel.addWishList(it.toString(),
                                onSuccessAddWishlist = this::onSuccessAddWishlist,
                                onErrorAddWishList = this::onErrorAddWishList)
                    }
                }
            } else {
                RecommendationPageTracking.eventUserClickProductToWishlistForNonLogin()
                RouteManager.route(activity, ApplinkConst.LOGIN)
            }
        }
    }

    private fun addToCart(
            success: (Map<String, Any>) -> Unit,
            error: (Throwable) -> Unit
    ){
        val addToCartRequestParams = AddToCartRequestParams()
        addToCartRequestParams.productId = productDataModel.productDetailData.id.toLong()
        addToCartRequestParams.shopId = productDataModel.productDetailData.shop.id
        addToCartRequestParams.quantity = productDataModel.productDetailData.minOrder
        addToCartRequestParams.notes = ""

        primaryProductViewModel.addToCart(addToCartRequestParams, success, error)
    }

    private fun onErrorRemoveWishList(errorMessage: String?) {
        showToastError(MessageErrorException(errorMessage))
    }

    private fun onSuccessRemoveWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_remove_wishlist))
        updateWishlist(false)
        sendIntentResusltWishlistChange(productId ?: "", false)

    }

    private fun sendIntentResusltWishlistChange(productId: String, isInWishlist: Boolean) {
        val resultIntent = Intent()
                .putExtra(WISHLIST_STATUS_UPDATED_POSITION, activity?.intent?.getIntExtra(WISHLIST_STATUS_UPDATED_POSITION, -1))
        resultIntent.putExtra(WIHSLIST_STATUS_IS_WISHLIST, isInWishlist)
        resultIntent.putExtra("product_id", productId)
        activity?.setResult(Activity.RESULT_CANCELED, resultIntent)
    }

    private fun onErrorAddWishList(errorMessage: String?) {
        showToastError(MessageErrorException(errorMessage))
    }

    private fun onSuccessAddWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_add_wishlist))
        updateWishlist(true)
        sendIntentResusltWishlistChange(productId ?: "", true)
    }

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

    private fun showToastError(throwable: Throwable) {
        context?.run {
            Toaster.showError(
                    findViewById(android.R.id.content),
                    RecommendationPageErrorHandler.getErrorMessage(this, throwable),
                    Snackbar.LENGTH_LONG)
        }
    }

    private fun showToastSuccess(message: String) {
        view?.run {
            Toaster.showNormal(
                    findViewById(android.R.id.content),
                    message,
                    Snackbar.LENGTH_LONG)
        }
    }

    private fun setRatingReviewCount(ratingValue: Int, review: Int){
        if (ratingValue in 1..5) {
            rating.setImageResource(getRatingDrawable(ratingValue))
            review_count.text = String.format(getString(R.string.recom_review_count), review)
        } else {
            rating.visibility = View.GONE
            review_count.visibility = View.GONE
        }
    }

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

    private fun setSplashedText(text: String){
        product_slashed_price.text = text
        product_slashed_price.paintFlags = product_slashed_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

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

    private fun mapToRecommendationItem() = RecommendationItem(
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
            discountPercentage = productDataModel.productDetailData.discountPercentage,
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
            type = ""
    )

    private fun handleDiscount(){
        if(productDataModel.productDetailData.discountPercentage > 0){
            product_discount.visibility = View.VISIBLE
            product_slashed_price.visibility = View.VISIBLE
            product_discount.text = "${productDataModel.productDetailData.discountPercentage}%"
            setSplashedText(productDataModel.productDetailData.slashedPrice)
        } else {
            product_discount.visibility = View.GONE
            product_slashed_price.visibility = View.GONE
        }
    }

    private fun onImpressionOrganic(item: RecommendationItem) {
        if(primaryProductViewModel.isLoggedIn()){
            RecommendationPageTracking.eventImpressionOnOrganicProductRecommendationForLoginUser(trackingQueue, item, item.position.toString())
        } else {
            RecommendationPageTracking.eventImpressionOnOrganicProductRecommendationForNonLoginUser(trackingQueue, item, item.position.toString())
        }
    }

    private fun onImpressionTopAds(item: RecommendationItem) {
        if(primaryProductViewModel.isLoggedIn()){
            RecommendationPageTracking.eventImpressionOnTopAdsProductRecommendationForLoginUser(trackingQueue, item, item.position.toString())
        } else {
            RecommendationPageTracking.eventImpressionOnTopAdsProductRecommendationForNonLoginUser(trackingQueue, item, item.position.toString())
        }
    }

    private fun onClickTopAds(item: RecommendationItem) {
        if(primaryProductViewModel.isLoggedIn()){
            RecommendationPageTracking.eventClickOnTopAdsProductRecommendationForLoginUser(item, item.position.toString())
        }else{
            RecommendationPageTracking.eventClickOnTopAdsProductRecommendationForNonLoginUser(item, item.position.toString())
        }
    }

    private fun onClickOrganic(item: RecommendationItem) {
        if(primaryProductViewModel.isLoggedIn()){
            RecommendationPageTracking.eventClickOnOrganicProductRecommendationForLoginUser(item, item.position.toString())
        }else{
            RecommendationPageTracking.eventClickOnOrganicProductRecommendationForNonLoginUser(item, item.position.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PDP) {
            data?.let {
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST,
                        productDataModel.productDetailData.isWishlist)
                updateWishlist(wishlistStatusFromPdp)
            }
        }
    }
}