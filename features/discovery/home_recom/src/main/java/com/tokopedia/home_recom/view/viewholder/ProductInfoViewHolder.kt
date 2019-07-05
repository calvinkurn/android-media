package com.tokopedia.home_recom.view.viewholder

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.R

class ProductInfoViewHolder(private val view: View) : AbstractViewHolder<ProductInfoDataModel>(view) {

    private val productName: TextView by lazy { view.findViewById<TextView>(R.id.product_name) }
    private val productImage: ImageView by lazy { view.findViewById<ImageView>(R.id.product_image) }
    private val productDiscount: TextView by lazy { view.findViewById<TextView>(R.id.product_discount) }
    private val productPrice: TextView by lazy { view.findViewById<TextView>(R.id.product_price) }
    private val location: TextView by lazy { view.findViewById<TextView>(R.id.location) }

    private val productSlashedPrice: TextView by lazy { view.findViewById<TextView>(R.id.product_slashed_price) }
    private val ratingView: ImageView by lazy { view.findViewById<ImageView>(R.id.rating) }
    private val ratingCountView: TextView by lazy { view.findViewById<TextView>(R.id.review_count) }

    override fun bind(element: ProductInfoDataModel) {
        productName.text = element.productDetailData.name
        productDiscount.text = "20%"
        setSplashedText("RP100.000")
        productPrice.text = element.productDetailData.price
        location.text = "Jakarta"
        ImageHandler.loadImageFitCenter(view.context, productImage, element.productDetailData.imageUrl)
        setRatingReviewCount(element.productDetailData.rating, element.productDetailData.countReview)
    }

    private fun setRatingReviewCount(rating: Int, review: Int){
        if (rating in 1..5) {
            ratingView.setImageResource(getRatingDrawable(rating))
            ratingCountView.text = view.context.getString(R.string.review_count, review)
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