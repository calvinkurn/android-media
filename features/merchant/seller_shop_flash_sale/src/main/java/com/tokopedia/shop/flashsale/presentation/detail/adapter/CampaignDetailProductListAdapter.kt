package com.tokopedia.shop.flashsale.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemCampaignDetailProductBinding
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.presentation.detail.adapter.CampaignDetailProductListAdapter.*
import com.tokopedia.unifyprinciples.Typography

class CampaignDetailProductListAdapter :
    RecyclerView.Adapter<CampaignDetailProductListViewHolder>() {

    private var products: MutableList<SellerCampaignProductList.Product> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CampaignDetailProductListViewHolder {
        val binding =
            SsfsItemCampaignDetailProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CampaignDetailProductListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(
        holder: CampaignDetailProductListViewHolder,
        position: Int
    ) {
        val product = products[position]
        holder.bind(product)
    }

    fun clearAll() {
        products.clear()
        notifyDataSetChanged()
    }

    fun submit(newProducts: List<SellerCampaignProductList.Product>) {
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    inner class CampaignDetailProductListViewHolder(private val binding: SsfsItemCampaignDetailProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: SellerCampaignProductList.Product) {
            binding.apply {
                imageProduct.setImageUrl(product.imageUrl.img200)
                tpgProductName.text = product.productName
                tpgProductSku.setProductSku(product.productSku)
                tpgProductStock.text = product.productMapData.customStock.toString()
                tpgProductCampaignPrice.setProductPrice(product.productMapData.discountedPrice)
            }
        }

        private fun Typography.setProductSku(sku: String) {
            if (sku.isNotEmpty()) {
                this.text = context.getString(R.string.campaign_detail_SKU_placeholder, sku)
            } else {
                this.text = context.getString(R.string.campaign_detail_SKU_placeholder, "-")
            }
        }

        private fun Typography.setProductPrice(price: Long) {
            this.text = price.getCurrencyFormatted()
        }
    }
}