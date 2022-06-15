package com.tokopedia.shop.flashsale.presentation.creation.manage.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemManageProductBinding
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList.Product
import com.tokopedia.unifyprinciples.Typography

class ManageProductListAdapter(
    private val onEditClicked: () -> Unit,
    private val onDeleteClicked: () -> Unit
) : RecyclerView.Adapter<ManageProductListAdapter.ManageProductListViewHolder>() {

    private var products: MutableList<Product> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ManageProductListAdapter.ManageProductListViewHolder {
        val binding =
            SsfsItemManageProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ManageProductListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(
        holder: ManageProductListAdapter.ManageProductListViewHolder,
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
        this.products = mutableListOf()
        notifyItemRangeRemoved(Constant.FIRST_PAGE, products.size)
    }

    fun submit(newProducts: List<Product>) {
        val oldItemSize = products.size
        products.addAll(newProducts)
        notifyItemChanged(oldItemSize, products.size)
    }

    inner class ManageProductListViewHolder(private val binding: SsfsItemManageProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            product: Product,
            onEditClicked: () -> Unit,
            onDeleteClicked: () -> Unit
        ) {
            binding.apply {
                imageProductItem.setImageUrl(product.imageUrl.img200)
                tpgProductName.text = product.productName

                if (product.productMapData.discountPercentage.isMoreThanZero()) {
                    labelDiscount.visible()
                    tpgDiscountedPrice.visible()
                    tpgDiscountedPrice.text = "Rp ${product.productMapData.discountedPrice}"
                    labelDiscount.text = "${product.productMapData.discountPercentage}%"
                    tpgOriginalPrice.text = product.formattedPrice
                    tpgOriginalPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    labelDiscount.gone()
                    tpgDiscountedPrice.gone()
                    tpgOriginalPrice.text = product.formattedPrice
                }

                if (isProductInfoComplete(product.productMapData)) {
                    if (!getErrorMessage(product.productMapData).isNullOrEmpty()) {
                        labelBelumLengkap.invisible()
                        tpgErrorCopy.visible()
                        tpgErrorCopy.text = getErrorMessage(product.productMapData)
                    } else {
                        labelBelumLengkap.gone()
                        tpgErrorCopy.gone()
                    }
                } else {
                    labelBelumLengkap.visible()
                    tpgErrorCopy.invisible()
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

        private fun handleProductLocationStatus(warehouses: List<SellerCampaignProductList.WarehouseData>) {
            if (warehouses.size.isMoreThanZero()) {

            } else {

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
                this.text = String.format(
                    context.getString(R.string.manage_product_item_campaign_stock_label),
                    campaignStock
                )
            } else {
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

        private fun isProductInfoComplete(productMapData: SellerCampaignProductList.ProductMapData): Boolean {
            return if (productMapData.discountedPrice == 0) {
                false
            } else if (productMapData.discountPercentage == 0) {
                false
            } else if (productMapData.originalCustomStock == 0) {
                false
            } else if (productMapData.customStock == 0) {
                false
            } else productMapData.maxOrder != 0
        }

        private fun getErrorMessage(productMapData: SellerCampaignProductList.ProductMapData): String {
            var errorMsg = ""

            if (productMapData.discountedPrice > productMapData.originalPrice) {
                errorMsg = "Harga campaign Maks. Rp ${productMapData.originalPrice * 0.99}"
                if (productMapData.customStock > productMapData.originalStock) {
                    errorMsg += "dan terjadi kesalahan lainnya"
                } else if (productMapData.discountedPrice < 100) {
                    errorMsg += "dan terjadi kesalahan lainnya"
                } else if (productMapData.customStock < 1) {
                    errorMsg += "dan terjadi kesalahan lainnya"
                } else if (productMapData.maxOrder > productMapData.customStock) {
                    errorMsg += "dan terjadi kesalahan lainnya"
                }
            } else if (productMapData.customStock > productMapData.originalStock) {
                errorMsg = "Stok campaign Maks. ${productMapData.originalStock}"
                if (productMapData.discountedPrice < 100) {
                    errorMsg += "dan terjadi kesalahan lainnya"
                } else if (productMapData.customStock < 1) {
                    errorMsg += "dan terjadi kesalahan lainnya"
                } else if (productMapData.maxOrder > productMapData.customStock) {
                    errorMsg += "dan terjadi kesalahan lainnya"
                }
            } else if (productMapData.discountedPrice < 100) {
                errorMsg = "Harga campaign Min. Rp 100"
                if (productMapData.customStock < 1) {
                    errorMsg += "dan terjadi kesalahan lainnya"
                } else if (productMapData.maxOrder > productMapData.customStock) {
                    errorMsg += "dan terjadi kesalahan lainnya"
                }
            } else if (productMapData.customStock < 1) {
                errorMsg = "Stok campaign Min. 1"
                if (productMapData.maxOrder > productMapData.customStock) {
                    errorMsg += "dan terjadi kesalahan lainnya"
                }
            } else if (productMapData.maxOrder > productMapData.customStock){
                errorMsg = "Maksimum pembelian tidak boleh melebihi stok utama"
            }

            return errorMsg
        }
    }
}