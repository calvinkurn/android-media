package com.tokopedia.buy_more_get_more.olp.presentation.adapter.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buy_more_get_more.databinding.ItemOlpProductListBinding
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel.Product
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.buy_more_get_more.R

class ProductListAdapter: RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder>() {
    var productList: List<Product> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        return ProductListViewHolder(parent.inflateLayout(R.layout.item_olp_product_list))
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        holder.bindData(productList[position], position)
    }

    inner class ProductListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var context: Context

        private val binding: ItemOlpProductListBinding? by viewBinding()

        init {
            context = itemView.context
        }

        fun bindData(product: Product, position: Int) {
            binding?.run {
                productCard.setProductModel(product.mapToProductCardModel())
            }
        }

        private fun Product.mapToProductCardModel(): ProductCardModel {
            return ProductCardModel(
                productImageUrl = imageUrl,
                productName = name,
                discountPercentage = campaign.discountedPercentage,
                slashedPrice = campaign.originalPrice,
                formattedPrice = price,
                ratingString = rating,
                hasAddToCartButton = true,
                countSoldRating = soldCount.toString()
            )
        }
    }
}
