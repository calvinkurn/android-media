package com.tokopedia.home_recom.view.viewholder

import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.model.entity.ProductDetailData
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.empty_product_info.view.*
import kotlinx.android.synthetic.main.fragment_product_info.view.*


class ProductInfoViewHolder(view: View, val listener: ProductInfoListener) : AbstractViewHolder<ProductInfoDataModel>(view) {

    init {
        view.hide()
    }

    override fun bind(element: ProductInfoDataModel) {
        if(element.productDetailData == null){
            // show error
            itemView.show()
            itemView.container_empty.show()
        } else {
            initView(element)
        }
    }

    override fun bind(element: ProductInfoDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun initView(productInfoDataModel: ProductInfoDataModel){
        productInfoDataModel.productDetailData?.let {productDetailData ->
            itemView.show()
            itemView.product_name?.text = productDetailData.name
            itemView.product_price?.text = productDetailData.price
            itemView.location?.text = productDetailData.shop.location
            if (productDetailData.badges.isNotEmpty()) {
                itemView.badge?.show()
                itemView.context?.let{
                    ImageHandler.loadImageFitCenter(it, itemView.badge, productDetailData.badges[0].imageUrl)
                }
            } else {
                itemView.badge?.hide()
            }
            itemView.textTopAds.gone()
            if (productDetailData.isTopads) {
                itemView.textTopAds.visible()
            }
            setRatingReviewCount(productDetailData.rating, productDetailData.countReview)
            itemView.context?.let{
                ImageHandler.loadImageRounded2(it, itemView.product_image, productDetailData.imageUrl)
            }
            setStatusStock(productDetailData)
            handleDiscount(productDetailData.discountPercentage, productDetailData.slashedPrice)
            updateWishlist(productDetailData.isWishlist)
            onProductImpression(productInfoDataModel)
            onClickProductCard(productInfoDataModel)
            onClickAddToCart(productInfoDataModel)
            onClickBuyNow(productInfoDataModel)
            onClickWishlist(productInfoDataModel)
        }
    }

    private fun handleDiscount(discountPercentage: Int, slashedPrice: String){
        if(discountPercentage > 0){
            itemView.product_discount?.show()
            itemView.product_slashed_price?.show()
            itemView.product_discount?.text = "$discountPercentage%"
            setSplashedText(slashedPrice)
        } else {
            itemView.product_discount?.gone()
            itemView.product_slashed_price?.gone()
        }
    }
    
    private fun setStatusStock(productDetailData: ProductDetailData){
        when(productDetailData.status){
            0 or -2 -> {
                itemView.container_product?.hide()
                itemView.container_empty?.show()
            }
            3 -> {
                itemView.add_to_cart?.isEnabled = false
                itemView.buy_now?.hide()
                itemView.add_to_cart?.text = getString(R.string.empty_stock)
            }
        }
    }

    private fun setRatingReviewCount(rating: Int, review: Int){
        if (rating in 1..5) {
            itemView.rating?.setImageResource(getRatingDrawable(rating))
            itemView.review_count?.text = String.format(getString(R.string.recom_review_count), review)
        } else {
            itemView.rating?.visibility = View.GONE
            itemView.review_count?.visibility = View.GONE
        }
    }

    private fun getRatingDrawable(rating: Int): Int {
        return when (rating) {
            0 -> com.tokopedia.productcard.R.drawable.product_card_ic_star_none
            1 -> com.tokopedia.productcard.R.drawable.product_card_ic_star_one
            2 -> com.tokopedia.productcard.R.drawable.product_card_ic_star_two
            3 -> com.tokopedia.productcard.R.drawable.product_card_ic_star_three
            4 -> com.tokopedia.productcard.R.drawable.product_card_ic_star_four
            5 -> com.tokopedia.productcard.R.drawable.product_card_ic_star_five
            else -> com.tokopedia.productcard.R.drawable.product_card_ic_star_none
        }
    }

    private fun setSplashedText(text: String){
        itemView.product_slashed_price?.text = text
        itemView.product_slashed_price?.paintFlags = itemView.product_slashed_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun updateWishlist(wishlisted: Boolean) {
        itemView.fab_detail?.let{
            with(it){
                show()
                if (wishlisted) {
                    isActivated = true
                    setImageDrawable(ContextCompat.getDrawable(context, R.drawable.recom_ic_product_action_wishlist_added_28))
                } else {
                    isActivated = false
                    setImageDrawable(ContextCompat.getDrawable(context, R.drawable.recom_ic_product_action_wishlist_gray_28))
                }
            }
        }
    }

    private fun onProductImpression(productInfoDataModel: ProductInfoDataModel){
        productInfoDataModel.productDetailData?.let {
            if (it.isTopads) {
                itemView.addOnImpressionListener(productInfoDataModel, object: ViewHintListener {
                    override fun onViewHint() {
                        listener.onProductAnchorImpression(productInfoDataModel)
                    }
                })
            }
        }
    }

    private fun onClickProductCard(productInfoDataModel: ProductInfoDataModel){
        itemView.product_card?.setOnClickListener {
            listener.onProductAnchorClick(productInfoDataModel)
        }
    }

    private fun onClickAddToCart(productInfoDataModel: ProductInfoDataModel){
        itemView.add_to_cart?.show()
        itemView.add_to_cart?.setOnClickListener {
            listener.onProductAnchorAddToCart(productInfoDataModel)
        }
    }

    private fun onClickBuyNow(productInfoDataModel: ProductInfoDataModel){
        itemView.buy_now?.show()
        itemView.buy_now?.setOnClickListener {
            listener.onProductAnchorBuyNow(productInfoDataModel)
        }
    }

    private fun onClickWishlist(productInfoDataModel: ProductInfoDataModel){
        itemView.fab_detail?.setOnClickListener {
            listener.onProductAnchorClickWishlist(productInfoDataModel, !itemView.fab_detail.isActivated){ state, throwable ->
                if(state){
                    itemView.fab_detail.isActivated = !itemView.fab_detail.isActivated
                    updateWishlist(itemView.fab_detail.isActivated)
                }
            }
        }
    }

    interface ProductInfoListener{
        fun onProductAnchorImpression(productInfoDataModel: ProductInfoDataModel)
        fun onProductAnchorClick(productInfoDataModel: ProductInfoDataModel)
        fun onProductAnchorAddToCart(productInfoDataModel: ProductInfoDataModel)
        fun onProductAnchorBuyNow(productInfoDataModel: ProductInfoDataModel)
        fun onProductAnchorClickWishlist(productInfoDataModel: ProductInfoDataModel, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit)
    }
}