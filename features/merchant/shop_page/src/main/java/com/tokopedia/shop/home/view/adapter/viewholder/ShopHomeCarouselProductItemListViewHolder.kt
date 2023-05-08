package com.tokopedia.shop.home.view.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopCarouselProductCardListBinding
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

//need to surpress this one, since there are no pii related data defined on this class
@SuppressLint("PII Data Exposure")
class ShopHomeCarouselProductItemListViewHolder(
    itemView: View,
    private val listProductCardModel: List<ProductCardModel>,
    private val carouselProductCardOnItemAddToCartListener: CarouselProductCardListener.OnItemAddToCartListener?,
    private val carouselProductCardOnItemClickListener: CarouselProductCardListener.OnItemClickListener?,
    private val carouselProductCardOnItemImpressedListener: CarouselProductCardListener.OnItemImpressedListener?,
    private val carouselProductCardOnItemATCNonVariantClickListener: CarouselProductCardListener.OnATCNonVariantClickListener?,
    private val carouselProductCardOnItemAddVariantClickListener: CarouselProductCardListener.OnAddVariantClickListener?,
) : AbstractViewHolder<ShopHomeProductUiModel>(itemView) {

    private val viewBinding: ItemShopCarouselProductCardListBinding? by viewBinding()
    private val productCardView: ProductCardListView? by lazy {
        viewBinding?.productCard
    }
    private var productCardModel: ProductCardModel? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_carousel_product_card_list
    }

    override fun bind(shopHomeProductUiModel: ShopHomeProductUiModel) {
        setProductData()
        setProductListener()
    }

    private fun setProductListener() {
        setProductOnImpressionListener()
        setProductOnClickListener()
        setProductAddToCartOnClickListener()
        setProductOnItemAtcNonVariantClickListener()
        setProductOnItemAddVariantClickListener()
    }

    private fun setProductOnImpressionListener() {
        carouselProductCardOnItemImpressedListener?.getImpressHolder(bindingAdapterPosition)?.let {
            viewBinding?.productCard?.setImageProductViewHintListener(it, object : ViewHintListener {
                override fun onViewHint() {
                    productCardModel?.let { productCardModel ->
                        carouselProductCardOnItemImpressedListener.onItemImpressed(
                            productCardModel,
                            bindingAdapterPosition
                        )
                    }
                }
            })
        }
    }

    private fun setProductOnItemAddVariantClickListener() {
        productCardView?.setAddVariantClickListener {
            productCardModel?.let { productCardModel ->
                carouselProductCardOnItemAddVariantClickListener?.onAddVariantClick(
                    productCardModel,
                    bindingAdapterPosition
                )
            }
        }
    }

    private fun setProductOnItemAtcNonVariantClickListener() {
        productCardView?.setAddToCartNonVariantClickListener(object : ATCNonVariantListener {
            override fun onQuantityChanged(quantity: Int) {
                productCardModel?.let { productCardModel ->
                    carouselProductCardOnItemATCNonVariantClickListener?.onATCNonVariantClick(
                        productCardModel,
                        bindingAdapterPosition,
                        quantity
                    )
                }
            }
        })
    }

    private fun setProductOnClickListener() {
        productCardView?.setOnClickListener {
            productCardModel?.let { productCardModel ->
                carouselProductCardOnItemClickListener?.onItemClick(
                    productCardModel,
                    bindingAdapterPosition
                )
            }
        }
    }

    private fun setProductAddToCartOnClickListener() {
        productCardView?.setAddToCartOnClickListener {
            productCardModel?.let { productCardModel ->
                carouselProductCardOnItemAddToCartListener?.onItemAddToCart(
                    productCardModel, bindingAdapterPosition
                )
            }
        }
    }

    private fun setProductData() {
        productCardModel = getProductCardModel()
        productCardModel?.let {
            viewBinding?.productCard?.setProductModel(it)
        }
    }

    private fun getProductCardModel(): ProductCardModel?{
        return listProductCardModel.getOrNull(bindingAdapterPosition)
    }

}
