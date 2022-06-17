package com.tokopedia.shop.flashsale.presentation.creation.manage.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemManageProductBinding
import com.tokopedia.shop.flashsale.common.extension.convertRupiah
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList.Product
import com.tokopedia.shop.flashsale.presentation.creation.manage.ManageProductFragment.Companion.isProductInfoComplete
import com.tokopedia.unifyprinciples.Typography

class ManageProductListAdapter(
    private val onEditClicked: () -> Unit,
    private val onDeleteClicked: () -> Unit
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
            onEditClicked: () -> Unit,
            onDeleteClicked: () -> Unit
        ) {
            binding.apply {
                imageProductItem.setImageUrl(product.imageUrl.img200)
                tpgProductName.text = product.productName

                when {
                    product.productMapData.discountPercentage.isMoreThanZero() -> {
                        labelDiscount.visible()
                        tpgDiscountedPrice.visible()
                        tpgDiscountedPrice.text =
                            product.productMapData.discountedPrice.convertRupiah()
                        labelDiscount.text = "${product.productMapData.discountPercentage}%"
                        tpgOriginalPrice.text = product.formattedPrice
                        tpgOriginalPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    }
                    else -> {
                        labelDiscount.gone()
                        tpgDiscountedPrice.gone()
                        tpgOriginalPrice.text = product.formattedPrice
                    }
                }

                when {
                    isProductInfoComplete(product.productMapData) -> {
                        when {
                            product.errorMessage.isNotEmpty() -> {
                                labelBelumLengkap.invisible()
                                tpgErrorCopy.visible()
                                tpgErrorCopy.text = product.errorMessage
                            }
                            else -> {
                                labelBelumLengkap.gone()
                                tpgErrorCopy.gone()
                            }
                        }
                    }
                    else -> {
                        labelBelumLengkap.visible()
                        tpgErrorCopy.invisible()
                    }
                }

                tpgStockProduct.setOriginalStock(product.productMapData.originalStock)
                tpgStockCampaign.setCampaignStock(product.productMapData.customStock)
                tpgMaxOrder.setMaxOrder(product.productMapData.maxOrder)

                btnUpdateProduct.setOnClickListener {
                    onEditClicked.invoke()
                }

                iconDeleteProduct.setOnClickListener {
                    onDeleteClicked.invoke()
                }
            }
        }

        private fun Typography.setOriginalStock(originalStock: Int) {
            this.text = String.format(
                context.getString(R.string.manage_product_item_stock_at_seller_location_label),
                originalStock
            )
        }

        private fun Typography.setCampaignStock(campaignStock: Int) {
            if (campaignStock.isMoreThanZero()) {
                this.visible()
                binding.tpgSeparator.visible()
                this.text = String.format(
                    context.getString(R.string.manage_product_item_campaign_stock_label),
                    campaignStock
                )
            } else {
                binding.tpgSeparator.gone()
                this.gone()
            }
        }

        private fun Typography.setMaxOrder(maxOrder: Int) {
            if (maxOrder.isMoreThanZero()) {
                this.visible()
                this.text = String.format(
                    context.getString(R.string.manage_product_item_max_buy_label),
                    maxOrder
                )
            } else {
                this.gone()
            }
        }
    }
}