package com.tokopedia.home_recom.view.productInfo

import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.dataModel.ProductInfoDataModel

class ProductInfoFragment : Fragment() {

    private lateinit var dataModel: ProductInfoDataModel

    private lateinit var productView: View
    private val productName: TextView by lazy { productView.findViewById<TextView>(R.id.product_name) }
    private val productImage: ImageView by lazy { productView.findViewById<ImageView>(R.id.product_image) }
    private val productDiscount: TextView by lazy { productView.findViewById<TextView>(R.id.product_discount) }
    private val productPrice: TextView by lazy { productView.findViewById<TextView>(R.id.product_price) }
    private val location: TextView by lazy { productView.findViewById<TextView>(R.id.location) }

    private val productSlashedPrice: TextView by lazy { productView.findViewById<TextView>(R.id.product_slashed_price) }
    private val ratingView: ImageView by lazy { productView.findViewById<ImageView>(R.id.rating) }
    private val ratingCountView: TextView by lazy { productView.findViewById<TextView>(R.id.review_count) }

    companion object{
        fun newInstance(dataModel: ProductInfoDataModel) = ProductInfoFragment().apply {
            this.dataModel = dataModel
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        productView = inflater.inflate(R.layout.fragment_product_info, container, false)
        return productView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productName.text = dataModel.product.name
        productDiscount.text = "20%"
        setSplashedText("RP100.000")
        productPrice.text = dataModel.product.price
        location.text = "Jakarta"
        ImageHandler.loadImageFitCenter(view.context, productImage, dataModel.product.imageUrl)
        setRatingReviewCount(dataModel.product.rating, dataModel.product.countReview)
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
}