package com.tokopedia.shop.flashsale.presentation.creation.manage.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemManageProductBinding
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList.*
import com.tokopedia.unifyprinciples.Typography

class ManageProductListAdapter(
    private val onEditClicked: (product: Product) -> Unit,
    private val onDeleteClicked: (product: Product) -> Unit
) : RecyclerView.Adapter<ManageProductListAdapter.ManageProductListViewHolder>() {

    private var products: MutableList<Product> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ManageProductListViewHolder {
        val binding =
            SsfsItemManageProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ManageProductListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(
        holder: ManageProductListViewHolder,
        position: Int
    ) {
        val product = products[position]
        holder.bind(
            product,
            onEditClicked,
            onDeleteClicked,
        )
    }

    fun clearAll() {
        products.clear()
        notifyDataSetChanged()
    }

    fun submit(newProducts: List<Product>) {
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    class ManageProductListViewHolder(private val binding: SsfsItemManageProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            product: Product,
            onEditClicked: (product: Product) -> Unit,
            onDeleteClicked: (product: Product) -> Unit
        ) {
            binding.apply {
                imageProductItem.setImageUrl(product.imageUrl.img200)
                tpgProductName.text = product.productName

                when {
                    product.productMapData.discountPercentage.isMoreThanZero() -> {
                        labelDiscount.visible()
                        tpgDiscountedPrice.visible()
                        tpgDiscountedPrice.text =
                            product.productMapData.discountedPrice.getCurrencyFormatted()
                        labelDiscount.text = "${product.productMapData.discountPercentage}%"
                        tpgOriginalPrice.text =
                            product.productMapData.originalPrice.getCurrencyFormatted()
                        tpgOriginalPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    }
                    else -> {
                        labelDiscount.gone()
                        tpgDiscountedPrice.gone()
                        tpgOriginalPrice.text =
                            product.productMapData.originalPrice.getCurrencyFormatted()
                    }
                }

                when {
                    product.isInfoComplete -> {
                        when {
                            product.errorMessage.isNotEmpty() -> {
                                labelBelumLengkap.invisible()
                                tpgErrorCopy.visible()
                                tpgErrorCopy.text = product.errorMessage
                                tpgCompleteProductInfoDesc.visible()
                            }
                            else -> {
                                labelBelumLengkap.gone()
                                tpgErrorCopy.gone()
                                tpgCompleteProductInfoDesc.gone()
                            }
                        }
                    }
                    else -> {
                        labelBelumLengkap.visible()
                        tpgErrorCopy.invisible()
                        tpgCompleteProductInfoDesc.visible()
                    }
                }

                tpgStockProduct.setOriginalStock(product)
                tpgStockCampaign.setCampaignStock(product.productMapData.originalCustomStock)
                tpgMaxOrder.setMaxOrder(product.productMapData.maxOrder)

                btnUpdateProduct.setOnClickListener {
                    onEditClicked.invoke(product)
                }

                iconDeleteProduct.setOnClickListener {
                    onDeleteClicked.invoke(product)
                }
            }
        }

        private fun Typography.setOriginalStock(product: Product) {
            this.text = if (product.warehouseList.isNullOrEmpty()) {
                MethodChecker.fromHtml(
                    context.getString(
                        R.string.manage_product_item_stock_at_single_location_label,
                        product.productMapData.originalStock
                    )
                )
            } else {
                binding.iconDilayaniTokopedia.visible()
                product.warehouseList.firstOrNull { it.chosenWarehouse }?.let { warehouse ->
                    MethodChecker.fromHtml(
                        context.getString(
                            R.string.manage_product_item_stock_at_seller_location_label,
                            warehouse.stock,
                            warehouse.warehouseName
                        )
                    )
                }
            }
        }

        private fun Typography.setCampaignStock(campaignStock: Int) {
            if (campaignStock > 0) {
                this.visible()
                binding.tpgSeparator.visible()
                this.text = MethodChecker.fromHtml(
                    context.getString(
                        R.string.manage_product_item_campaign_stock_label,
                        campaignStock
                    )
                )
            } else {
                binding.tpgSeparator.gone()
                this.gone()
            }
        }

        private fun Typography.setMaxOrder(maxOrder: Int) {
            if (maxOrder.isMoreThanZero()) {
                this.visible()
                this.text = MethodChecker.fromHtml(
                    context.getString(R.string.manage_product_item_max_buy_label, maxOrder)
                )
            } else {
                this.gone()
            }
        }
    }
}