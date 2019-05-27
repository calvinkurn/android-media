package com.tokopedia.home_recom.view.productInfo

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.dataModel.ProductInfoDataModel
import com.tokopedia.home_recom.viewmodel.PrimaryProductViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_product_info.*
import javax.inject.Inject

class ProductInfoFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userSessionInterface : UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy{ ViewModelProviders.of(this, viewModelFactory) }

    private val primaryProductViewModel by lazy { viewModelProvider.get(PrimaryProductViewModel::class.java) }

    private lateinit var dataModel: ProductInfoDataModel

    private lateinit var productId: String

    private lateinit var productView: View
    private val productName: TextView by lazy { productView.findViewById<TextView>(R.id.product_name) }
    private val productImage: ImageView by lazy { productView.findViewById<ImageView>(R.id.product_image) }
    private val productDiscount: TextView by lazy { productView.findViewById<TextView>(R.id.product_discount) }
    private val productPrice: TextView by lazy { productView.findViewById<TextView>(R.id.product_price) }
    private val location: TextView by lazy { productView.findViewById<TextView>(R.id.location) }

    private val productSlashedPrice: TextView by lazy { productView.findViewById<TextView>(R.id.product_slashed_price) }
    private val ratingView: ImageView by lazy { productView.findViewById<ImageView>(R.id.rating) }
    private val ratingCountView: TextView by lazy { productView.findViewById<TextView>(R.id.review_count) }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(HomeRecommendationComponent::class.java).inject(this)
    }

    companion object{
        fun newInstance(dataModel: ProductInfoDataModel) = ProductInfoFragment().apply {
            this.dataModel = dataModel
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
        productName.text = dataModel.productDetailData.name
        productDiscount.text = "20%"
        setSplashedText("RP100.000")
        productPrice.text = dataModel.productDetailData.price
        location.text = "Jakarta"
        ImageHandler.loadImageFitCenter(view.context, productImage, dataModel.productDetailData.imageUrl)
        setRatingReviewCount(dataModel.productDetailData.rating, dataModel.productDetailData.countReview)

        fab_detail.setOnClickListener {
            if (it.isActivated) {
                productId?.let {
                    primaryProductViewModel.removeWishList(it,
                            onSuccessRemoveWishlist = this::onSuccessRemoveWishlist,
                            onErrorRemoveWishList = this::onErrorRemoveWishList)
                }

            } else {
                productId?.let {
                    primaryProductViewModel.addWishList(it,
                            onSuccessAddWishlist = this::onSuccessAddWishlist,
                            onErrorAddWishList = this::onErrorAddWishList)
                }
            }
        }
    }

    private fun onErrorRemoveWishList(errorMessage: String?) {
        showToastError(MessageErrorException(errorMessage))
    }

    private fun onSuccessRemoveWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_remove_wishlist))
//        productInfoViewModel.productInfoP3resp.value?.isWishlisted = false
        updateWishlist(false)
        //TODO clear cache
        sendIntentResusltWishlistChange(productId ?: "", false)

    }

    private fun sendIntentResusltWishlistChange(productId: String, isInWishlist: Boolean) {
        val resultIntent = Intent()
                .putExtra(Companion.WISHLIST_STATUS_UPDATED_POSITION, activity?.intent?.getIntExtra(Companion.WISHLIST_STATUS_UPDATED_POSITION, -1))
        resultIntent.putExtra(Companion.WIHSLIST_STATUS_IS_WISHLIST, isInWishlist)
        resultIntent.putExtra("product_id", productId)
        activity!!.setResult(Activity.RESULT_CANCELED, resultIntent)
    }

    private fun onErrorAddWishList(errorMessage: String?) {
        showToastError(MessageErrorException(errorMessage))
    }

    private fun onSuccessAddWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_add_wishlist))
//        productInfoViewModel.productInfoP3resp.value?.isWishlisted = true
        updateWishlist(true)
        //TODO clear cache
        sendIntentResusltWishlistChange(productId ?: "", true)
    }

    private fun showToastError(throwable: Throwable) {
        activity?.run {
//            ToasterError.make(findViewById(android.R.id.content),
//                    ProductDetailErrorHandler.getErrorMessage(this, throwable),
//                    ToasterError.LENGTH_LONG)
//                    .show()
        }
    }

    private fun showToastSuccess(message: String) {
        activity?.run {
//            ToasterNormal.make(findViewById(android.R.id.content),
//                    message,
//                    ToasterNormal.LENGTH_LONG)
//                    .show()
        }
    }

    private fun setRatingReviewCount(rating: Int, review: Int){
        if (rating in 1..5) {
            ratingView.setImageResource(getRatingDrawable(rating))
            ratingCountView.text = getString(R.string.review_count, review)
        } else {
            ratingView.visibility = View.INVISIBLE
            ratingCountView.visibility = View.INVISIBLE
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
        productSlashedPrice.text = text
        productSlashedPrice.paintFlags = productSlashedPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun updateWishlist(wishlisted: Boolean) {
        context?.let {
            if (wishlisted) {
                fab_detail.isActivated = true
                fab_detail.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_wishlist_checked))
            } else {
                fab_detail.isActivated = false
                fab_detail.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_wishlist_unchecked))
            }
        }
    }
}