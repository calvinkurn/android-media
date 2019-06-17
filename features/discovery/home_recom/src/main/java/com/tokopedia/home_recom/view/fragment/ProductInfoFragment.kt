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
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.util.RecommendationPageErrorHandler
import com.tokopedia.home_recom.viewmodel.PrimaryProductViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_product_info.*
import javax.inject.Inject

class ProductInfoFragment : BaseDaggerFragment() {

    private val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"

    @Inject
    lateinit var userSessionInterface : UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy{ ViewModelProviders.of(this, viewModelFactory) }

    private val primaryProductViewModel by lazy { viewModelProvider.get(PrimaryProductViewModel::class.java) }

    private lateinit var productDataModel: ProductInfoDataModel

    private lateinit var productView: View

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

        product_card.setOnClickListener {
            RouteManager.route(
                    context,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    productDataModel.productDetailData.id.toString())
        }

        fab_detail.setOnClickListener {
            if (userSessionInterface.isLoggedIn) {
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
                RouteManager.route(activity, ApplinkConst.LOGIN)
            }
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