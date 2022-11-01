package com.tokopedia.shop.flashsale.presentation.creation.manage.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemManageProductBinding
import com.tokopedia.shop.flashsale.common.extension.convertRupiah
import com.tokopedia.shop.flashsale.common.util.DiscountUtil
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList.*
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductErrorType
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
                        setErrorMessage(product)
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

        private fun setErrorMessage(product: Product) {
            var errorMsg = ""
            val maxDiscountedPriceInCurrency =
                DiscountUtil.getProductMaxDiscountedPrice(product.productMapData.originalPrice).convertRupiah()
            val minDiscountedPriceInCurrency =
                DiscountUtil.getProductMinDiscountedPrice(product.productMapData.originalPrice).convertRupiah()
            when (product.errorType) {
                ManageProductErrorType.MAX_DISCOUNT_PRICE -> {
                    errorMsg = String.format(
                        itemView.context.getString(R.string.error_msg_max_discounted_price),
                        maxDiscountedPriceInCurrency
                    )
                }
                ManageProductErrorType.MAX_DISCOUNT_PRICE_AND_OTHER -> {
                    errorMsg = String.format(
                        itemView.context.getString(R.string.error_msg_max_discounted_price),
                        maxDiscountedPriceInCurrency
                    ) + itemView.context.getString(R.string.error_msg_other)
                }
                ManageProductErrorType.MAX_STOCK -> {
                    errorMsg = String.format(
                        itemView.context.getString(R.string.error_msg_max_campaign_stock),
                        product.productMapData.originalStock
                    )
                }
                ManageProductErrorType.MAX_STOCK_AND_OTHER -> {
                    errorMsg = String.format(
                        itemView.context.getString(R.string.error_msg_max_campaign_stock),
                        product.productMapData.originalStock
                    ) + itemView.context.getString(R.string.error_msg_other)
                }
                ManageProductErrorType.MIN_DISCOUNT_PRICE -> {
                    errorMsg = String.format(
                        itemView.context.getString(R.string.error_msg_min_discounted_price),
                        minDiscountedPriceInCurrency
                    )
                }
                ManageProductErrorType.MIN_DISCOUNT_PRICE_AND_OTHER -> {
                    errorMsg = String.format(
                        itemView.context.getString(R.string.error_msg_min_discounted_price),
                        minDiscountedPriceInCurrency
                    ) + itemView.context.getString(R.string.error_msg_other)
                }
                ManageProductErrorType.MIN_STOCK -> {
                    errorMsg = itemView.context.getString(R.string.error_msg_min_campaign_stock)
                }
                ManageProductErrorType.MIN_STOCK_AND_OTHER -> {
                    errorMsg =
                        itemView.context.getString(R.string.error_msg_min_campaign_stock) + itemView.context.getString(
                            R.string.error_msg_other
                        )
                }
                ManageProductErrorType.MAX_ORDER -> {
                    errorMsg = itemView.context.getString(R.string.error_msg_max_campaign_order)
                }
                else -> {}
            }
            binding.apply {
                if (errorMsg.isNotEmpty()) {
                    labelBelumLengkap.invisible()
                    tpgErrorCopy.visible()
                    tpgErrorCopy.text = errorMsg
                    tpgCompleteProductInfoDesc.visible()
                } else {
                    labelBelumLengkap.gone()
                    tpgErrorCopy.gone()
                    tpgCompleteProductInfoDesc.gone()
                }
            }
        }

        private fun Typography.setOriginalStock(product: Product) {
            this.text = if (product.warehouseList.isEmpty()) {
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
