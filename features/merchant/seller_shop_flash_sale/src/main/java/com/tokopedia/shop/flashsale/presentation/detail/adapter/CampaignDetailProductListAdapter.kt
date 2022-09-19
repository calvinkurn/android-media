package com.tokopedia.shop.flashsale.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemCampaignDetailProductBinding
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignProductListResponse
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.isActive
import com.tokopedia.shop.flashsale.domain.entity.enums.isOngoing
import com.tokopedia.shop.flashsale.presentation.detail.adapter.CampaignDetailProductListAdapter.*
import com.tokopedia.unifyprinciples.Typography

class CampaignDetailProductListAdapter :
    RecyclerView.Adapter<CampaignDetailProductListViewHolder>() {

    private var products: MutableList<SellerCampaignProductList.Product> = mutableListOf()
    var campaignStatus: CampaignStatus = CampaignStatus.READY

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
                //bind data to view
                imageProduct.setImageUrl(product.imageUrl.img200)
                tpgProductName.text = product.productName
                tpgProductSku.setProductSku(product.productSku)
                tpgProductHighlightName.text = product.highlightProductWording
                tpgProductHighlightName.setProductHighlightVisibility(product.highlightProductWording)
                tpgProductCampaignStock.text = product.productMapData.originalCustomStock.toString()
                tpgProductCampaignPrice.setProductPrice(product.productMapData.discountedPrice)
                tpgProductSold.text = product.productMapData.campaignSoldCount.toString()
                tpgViewCount.text = product.viewCount.toString()
                tpgCampaignStock.text = product.productMapData.originalCustomStock.toString()
                tpgRemaining.text = product.productMapData.customStock.toString()

                //handle ongoing item view visibility
                if (campaignStatus.isOngoing()) {
                    groupItemOngoing.visible()
                    groupCampaignStock.gone()
                } else {
                    groupItemOngoing.gone()
                    groupCampaignStock.visible()
                }
            }
        }

        private fun Typography.setProductHighlightVisibility(highlightProductWording: String) {
            this.visibility = if (highlightProductWording.isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        private fun Typography.setProductSku(sku: String) {
            this.text = if (sku.isNotEmpty()) {
                context.getString(R.string.campaign_detail_SKU_placeholder, sku)
            } else {
                context.getString(R.string.campaign_detail_SKU_placeholder, "-")
            }
        }

        private fun Typography.setProductPrice(price: Long) {
            this.text = price.getCurrencyFormatted()
        }
    }
}