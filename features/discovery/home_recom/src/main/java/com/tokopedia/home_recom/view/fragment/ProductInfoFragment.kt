package com.tokopedia.home_recom.view.fragment

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.*
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.analytics.RecommendationPageTracking
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.util.RecommendationPageErrorHandler
import com.tokopedia.home_recom.viewmodel.PrimaryProductViewModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_product_info.*
import javax.inject.Inject

class ProductInfoFragment : BaseDaggerFragment() {

    private val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"

    @Inject
    lateinit var userSessionInterface : UserSessionInterface

    @Inject
    lateinit var trackingQueue: TrackingQueue

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy{ ViewModelProviders.of(this, viewModelFactory) }

    private val primaryProductViewModel by lazy { viewModelProvider.get(PrimaryProductViewModel::class.java) }

    private lateinit var productDataModel: ProductInfoDataModel

    private lateinit var productView: View

    private val recommendationItem: RecommendationItem by lazy { mapToRecommendationItem() }

    private var menu: Menu? = null

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

        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val WISHLIST_STATUS_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val SHARE_PRODUCT_TITLE = "Bagikan Produk Ini"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        productView = inflater.inflate(R.layout.fragment_product_info, container, false)
        return productView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        product_name.text = productDataModel.productDetailData.name
        fab_detail.visibility = View.GONE
        handleDiscount()
        product_price.text = productDataModel.productDetailData.price
        location.text = productDataModel.productDetailData.shop.location
        if (!productDataModel.productDetailData.badges.isEmpty()) {
            badge.visibility = View.VISIBLE
            ImageHandler.loadImageFitCenter(view.context, badge, productDataModel.productDetailData.badges.get(0).imageUrl)
        } else {
            badge.visibility = View.GONE
        }
        updateWishlist(productDataModel.productDetailData.isWishlist)
        ImageHandler.loadImageRounded2(view.context, product_image, productDataModel.productDetailData.imageUrl)
        setRatingReviewCount(productDataModel.productDetailData.rating, productDataModel.productDetailData.countReview)

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

        product_card.setOnClickListener {
            RecommendationPageTracking.eventClickPrimaryProduct(recommendationItem, "0")
            if(productDataModel.productDetailData.isTopads){
                onClickTopAds(recommendationItem)
            }else{
                onClickOrganic(recommendationItem)
            }

            RouteManager.route(
                    context,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    productDataModel.productDetailData.id.toString())
        }

        fab_detail.setOnClickListener {
            if (userSessionInterface.isLoggedIn) {
                if (it.isActivated) {
                    RecommendationPageTracking.eventAddWishlistOnProductRecommendationLogin()
                    productDataModel.productDetailData.id.let {
                        primaryProductViewModel.removeWishList(it.toString(),
                                onSuccessRemoveWishlist = this::onSuccessRemoveWishlist,
                                onErrorRemoveWishList = this::onErrorRemoveWishList)
                    }

                } else {
                    RecommendationPageTracking.eventRemoveWishlistOnProductRecommendationLogin()
                    productDataModel.productDetailData.id.let {
                        primaryProductViewModel.addWishList(it.toString(),
                                onSuccessAddWishlist = this::onSuccessAddWishlist,
                                onErrorAddWishList = this::onErrorAddWishList)
                    }
                }
            } else {
                RecommendationPageTracking.eventAddWishlistOnProductRecommendationNonLogin()
                RouteManager.route(activity, ApplinkConst.LOGIN)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_product_detail_dark, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                RecommendationPageTracking.eventUserClickBack()
                RouteManager.route(activity, ApplinkConst.HOME)
                true
            }
            R.id.action_share -> {
                shareProduct(); true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onErrorRemoveWishList(errorMessage: String?) {
        showToastError(MessageErrorException(errorMessage))
    }

    private fun onSuccessRemoveWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_remove_wishlist))
        updateWishlist(false)
        //TODO clear cache
        sendIntentResusltWishlistChange(productId ?: "", false)

    }

    private fun sendIntentResusltWishlistChange(productId: String, isInWishlist: Boolean) {
        val resultIntent = Intent()
                .putExtra(WISHLIST_STATUS_UPDATED_POSITION, activity?.intent?.getIntExtra(WISHLIST_STATUS_UPDATED_POSITION, -1))
        resultIntent.putExtra(WIHSLIST_STATUS_IS_WISHLIST, isInWishlist)
        resultIntent.putExtra("product_id", productId)
        activity!!.setResult(Activity.RESULT_CANCELED, resultIntent)
    }

    private fun onErrorAddWishList(errorMessage: String?) {
        showToastError(MessageErrorException(errorMessage))
    }

    private fun onSuccessAddWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_add_wishlist))
        updateWishlist(true)
        //TODO clear cache
        sendIntentResusltWishlistChange(productId ?: "", true)
    }

    private fun showToastError(throwable: Throwable) {
        activity?.run {
            Toaster.showRed(
                    findViewById(android.R.id.content),
                    RecommendationPageErrorHandler.getErrorMessage(activity!!, throwable),
                    Snackbar.LENGTH_LONG)
        }
    }

    private fun showToastSuccess(message: String) {
        activity?.run {
            Toaster.showGreen(
                    findViewById(android.R.id.content),
                    message,
                    Snackbar.LENGTH_LONG)
        }
    }

    private fun setRatingReviewCount(ratingValue: Int, review: Int){
        if (ratingValue in 1..5) {
            rating.setImageResource(getRatingDrawable(ratingValue))
            review_count.text = getString(R.string.review_count, review)
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
            price = productDataModel.productDetailData.price,
            priceInt = productDataModel.productDetailData.priceInt,
            rating = productDataModel.productDetailData.rating,
            recommendationType = productDataModel.productDetailData.recommendationType,
            stock = productDataModel.productDetailData.stock,
            trackerImageUrl = productDataModel.productDetailData.trackerImageUrl,
            url = productDataModel.productDetailData.url,
            wishlistUrl = productDataModel.productDetailData.wishlistUrl
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
        if(userSessionInterface.isLoggedIn){
            RecommendationPageTracking.eventImpressionOnOrganicProductRecommendationForLoginUser(trackingQueue, item, item.position.toString())
        } else {
            RecommendationPageTracking.eventImpressionOnOrganicProductRecommendationForNonLoginUser(trackingQueue, item, item.position.toString())
        }
    }

    private fun onImpressionTopAds(item: RecommendationItem) {
        if(userSessionInterface.isLoggedIn){
            RecommendationPageTracking.eventImpressionOnTopAdsProductRecommendationForLoginUser(trackingQueue, item, item.position.toString())
        } else {
            RecommendationPageTracking.eventImpressionOnTopAdsProductRecommendationForNonLoginUser(trackingQueue, item, item.position.toString())
        }
    }

    private fun onClickTopAds(item: RecommendationItem) {
        if(userSessionInterface.isLoggedIn){
            RecommendationPageTracking.eventClickOnTopAdsProductRecommendationForLoginUser(item, item.position.toString())
        }else{
            RecommendationPageTracking.eventClickOnTopAdsProductRecommendationForNonLoginUser(item, item.position.toString())
        }
    }

    private fun onClickOrganic(item: RecommendationItem) {
        if(userSessionInterface.isLoggedIn){
            RecommendationPageTracking.eventClickOnOrganicProductRecommendationForLoginUser(item, item.position.toString())
        }else{
            RecommendationPageTracking.eventClickOnOrganicProductRecommendationForNonLoginUser(item, item.position.toString())
        }
    }

    private fun shareProduct(){
        LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0,
                productDataToLinkerDataMapper(), object : ShareCallback {
            override fun urlCreated(linkerShareData: LinkerShareResult) {
                openIntentShare(productDataModel.productDetailData.name, context!!.getString(R.string.home_recommendation), linkerShareData.url)
            }

            override fun onError(linkerError: LinkerError) {
                openIntentShare(productDataModel.productDetailData.name, context!!.getString(R.string.home_recommendation), "https://tokopedia.com/rekomendasi/${productDataModel.productDetailData.id.toString()}")
            }
        }))
    }

    private fun productDataToLinkerDataMapper(): LinkerShareData{
        var linkerData = LinkerData()
        linkerData.id = productDataModel.productDetailData.id
        linkerData.name = productDataModel.productDetailData.name
        linkerData.description = productDataModel.productDetailData.name
        linkerData.imgUri = productDataModel.productDetailData.imageUrl
        linkerData.ogUrl = null
        linkerData.type = LinkerData.PRODUCT_TYPE
        linkerData.uri =  productDataModel.productDetailData.url
        var linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }

    private fun openIntentShare(title: String, shareContent: String, shareUri: String){

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_REFERRER, shareUri)
            putExtra(Intent.EXTRA_HTML_TEXT, shareContent)
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(Intent.EXTRA_TEXT, shareContent)
            putExtra(Intent.EXTRA_SUBJECT, title)
        }

        activity?.startActivity(Intent.createChooser(shareIntent, SHARE_PRODUCT_TITLE))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            data?.let {
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST,
                        productDataModel.productDetailData.isWishlist)
                updateWishlist(wishlistStatusFromPdp)
            }
        }
    }
}