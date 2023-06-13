package com.tokopedia.home_recom.view.viewholder

import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.databinding.FragmentProductInfoBinding
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.model.entity.ProductDetailData
import com.tokopedia.home_recom.util.RecomServerLogger
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.RECOM_PAGE_DISABLE_VIEWPORT_DS_TOPADS
import com.tokopedia.utils.view.binding.viewBinding


class ProductInfoViewHolder(view: View, val listener: ProductInfoListener?) : AbstractViewHolder<ProductInfoDataModel>(view) {

    private var binding: FragmentProductInfoBinding? by viewBinding()
    
    init {
        view.hide()
    }

    override fun bind(element: ProductInfoDataModel) {
        if (element.productDetailData == null) {
            // show error
            itemView.show()
            binding?.emptyLayout?.root?.show()
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
            binding?.productName?.text = productDetailData.name
            binding?.productPrice?.text = productDetailData.price
            binding?.location?.text = productDetailData.shop.location
            if (productDetailData.badges.isNotEmpty()) {
                binding?.badge?.show()
                itemView.context?.let{
                    if (productDetailData.badges.isNotEmpty()) {
                        binding?.badge?.setImageUrl(productDetailData.badges[0].imageUrl)
                    }
                }
            } else {
                binding?.badge?.hide()
            }
            binding?.textTopAds?.gone()
            if (productDetailData.isTopads) {
                binding?.textTopAds?.visible()
            }
            setRatingReviewCount(productDetailData.rating, productDetailData.countReview)
            itemView.context?.let{
                binding?.productImage?.setImageUrl(productDetailData.imageUrl)
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
            binding?.productDiscount?.show()
            binding?.productSlashedPrice?.show()
            binding?.productDiscount?.text = "$discountPercentage%"
            setSplashedText(slashedPrice)
        } else {
            binding?.productDiscount?.gone()
            binding?.productSlashedPrice?.gone()
        }
    }
    
    private fun setStatusStock(productDetailData: ProductDetailData){
        when(productDetailData.status){
            0 or -2 -> {
                binding?.containerProduct?.hide()
                binding?.emptyLayout?.root?.show()
            }
            3 -> {
                binding?.addToCart?.isEnabled = false
                binding?.buyNow?.hide()
                binding?.addToCart?.text = getString(R.string.empty_stock)
            }
        }
    }

    private fun setRatingReviewCount(rating: Int, review: Int){
        if (rating in 1..5) {
            binding?.rating?.setImageResource(getRatingDrawable(rating))
            binding?.reviewCount?.text = String.format(getString(R.string.recom_review_count), review)
        } else {
            binding?.rating?.visibility = View.GONE
            binding?.reviewCount?.visibility = View.GONE
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
        binding?.productSlashedPrice?.text = text
        var flags = Paint.STRIKE_THRU_TEXT_FLAG
        binding?.productSlashedPrice?.paintFlags?.let {
            flags = it or Paint.STRIKE_THRU_TEXT_FLAG
        }
        binding?.productSlashedPrice?.paintFlags = flags
    }

    private fun updateWishlist(wishlisted: Boolean) {
        binding?.fabDetail?.let{
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
        if (listener?.getFragmentRemoteConfig()?.getBoolean(RECOM_PAGE_DISABLE_VIEWPORT_DS_TOPADS, true) == true) {
            impressWithoutViewportValidation(productInfoDataModel)
        } else {
            impressWithViewportValidation(productInfoDataModel)
        }
        listener?.onProductAnchorImpressionHitGTM(productInfoDataModel)
    }

    private fun impressWithViewportValidation(productInfoDataModel: ProductInfoDataModel) {
        productInfoDataModel.productDetailData?.let {
            itemView.addOnImpressionListener(productInfoDataModel, object: ViewHintListener {
                override fun onViewHint() {
                    if (it.isTopads) {
                        listener?.onProductAnchorImpression(productInfoDataModel)
                    } else {
                        RecomServerLogger.logServer(
                            RecomServerLogger.TOPADS_RECOM_PAGE_IS_NOT_ADS,
                            productId = it.id.toString(),
                            queryParam = listener?.getProductQueryParam()?:""
                        )
                    }
                    listener?.onProductAnchorImpressionHitGTM(productInfoDataModel)
                }
            })
        }
    }

    private fun impressWithoutViewportValidation(productInfoDataModel: ProductInfoDataModel) {
        if (!productInfoDataModel.isInvoke && productInfoDataModel.isGetTopAds) {
            if (productInfoDataModel.productDetailData?.isTopads == true) {
                listener?.onProductAnchorImpression(productInfoDataModel)
            } else {
                RecomServerLogger.logServer(
                    RecomServerLogger.TOPADS_RECOM_PAGE_IS_NOT_ADS,
                    productId = productInfoDataModel.productDetailData?.toString()?:"",
                    queryParam = listener?.getProductQueryParam()?:""
                )
            }
            productInfoDataModel.invoke()
        }
    }

    private fun onClickProductCard(productInfoDataModel: ProductInfoDataModel){
        binding?.productCard?.setOnClickListener {
            listener?.onProductAnchorClick(productInfoDataModel)
        }
    }

    private fun onClickAddToCart(productInfoDataModel: ProductInfoDataModel){
        binding?.addToCart?.show()
        binding?.addToCart?.setOnClickListener {
            listener?.onProductAnchorAddToCart(productInfoDataModel)
        }
    }

    private fun onClickBuyNow(productInfoDataModel: ProductInfoDataModel){
        binding?.buyNow?.show()
        binding?.buyNow?.setOnClickListener {
            listener?.onProductAnchorBuyNow(productInfoDataModel)
        }
    }

    private fun onClickWishlist(productInfoDataModel: ProductInfoDataModel){
        binding?.fabDetail?.setOnClickListener {
            val fabDetailActivated = binding?.fabDetail?.isActivated == true
            binding?.fabDetail?.isActivated = !fabDetailActivated
            listener?.onProductAnchorClickWishlist(productInfoDataModel, !fabDetailActivated) { state, _ ->
                updateWishlist(state)
            }
        }
    }

    fun getAddToCartView(): View? {
        return binding?.addToCart
    }

    fun getBuyNowView(): View? {
        return binding?.buyNow
    }

    interface ProductInfoListener{
        fun onProductAnchorImpression(productInfoDataModel: ProductInfoDataModel)
        fun onProductAnchorImpressionHitGTM(productInfoDataModel: ProductInfoDataModel)
        fun onProductAnchorClick(productInfoDataModel: ProductInfoDataModel)
        fun onProductAnchorAddToCart(productInfoDataModel: ProductInfoDataModel)
        fun onProductAnchorBuyNow(productInfoDataModel: ProductInfoDataModel)
        fun onProductAnchorClickWishlist(productInfoDataModel: ProductInfoDataModel, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit)

        fun getProductQueryParam(): String
        fun getFragmentRemoteConfig(): RemoteConfig?
    }
}
