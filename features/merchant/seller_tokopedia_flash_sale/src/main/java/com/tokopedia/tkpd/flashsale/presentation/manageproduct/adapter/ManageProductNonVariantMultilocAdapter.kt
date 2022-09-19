package com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailInformationBinding
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailLocationItemBinding
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.ProductCriteria
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse.DiscountSetup

class ManageProductNonVariantMultilocAdapter: RecyclerView.Adapter<ManageProductNonVariantMultilocAdapter.CriteriaViewHolder>() {

    private var product: ReservedProduct.Product? = null
    private var listener: ManageProductNonVariantAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val binding = LayoutCampaignManageProductDetailLocationItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CriteriaViewHolder(binding, listener)
    }

    override fun getItemCount() = product?.warehouses?.size.orZero()

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        product?.let {
            it.warehouses.getOrNull(position)?.let { selectedWarehouse ->
                holder.bind(it, selectedWarehouse)
            }
        }
    }

    fun setDataList(newData: ReservedProduct.Product) {
        product = newData
        notifyItemRangeChanged(Int.ZERO, newData.warehouses.size)
    }

    fun setListener(listener: ManageProductNonVariantAdapterListener) {
        this.listener = listener
    }

    inner class CriteriaViewHolder(
        private val binding: LayoutCampaignManageProductDetailLocationItemBinding,
        private val listener: ManageProductNonVariantAdapterListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        private fun LayoutCampaignManageProductDetailInformationBinding.triggerListener(
            criteria: ProductCriteria,
            discount: DiscountSetup?
        ) {
            discount?.let {
                val errorColorRes = com.tokopedia.unifyprinciples.R.color.Unify_RN500
                val normalColorRes = com.tokopedia.unifyprinciples.R.color.Unify_NN600
                val validationResult = listener?.onDataInputChanged(adapterPosition, criteria, it)
                textFieldPriceDiscountNominal.isInputError = validationResult?.isPriceError == true
                textFieldPriceDiscountPercentage.isInputError = validationResult?.isPricePercentError == true
                textQuantityEditorSubTitle.setTextColor(MethodChecker.getColor(root.context,
                    if (validationResult?.isStockError == true) {
                        errorColorRes
                    } else {
                        normalColorRes
                    }
                ))
            }
        }

        private fun LayoutCampaignManageProductDetailInformationBinding.setupListener(
            criteria: ProductCriteria,
            discount: DiscountSetup?
        ) {
            textFieldPriceDiscountNominal.editText.afterTextChanged {
                discount?.price = it.toLongOrZero()
                triggerListener(criteria, discount)
            }
            textFieldPriceDiscountPercentage.editText.afterTextChanged {
                discount?.discount = it.toIntSafely()
                triggerListener(criteria, discount)
            }
            quantityEditor.editText.afterTextChanged {
                discount?.stock = it.toLongOrZero()
                triggerListener(criteria, discount)
            }
        }

        fun bind(
            product: ReservedProduct.Product,
            selectedWarehouse: ReservedProduct.Product.Warehouse,
        ) {
            binding.containerLayoutProductParent.apply {
                textParentErrorMessage.gone()
                imageParentError.gone()
                textParentTitle.text = selectedWarehouse.name
                textParentOriginalPrice.text = selectedWarehouse.price.getCurrencyFormatted()
                textParentTotalStock.text = root.context.getString(R.string.manageproductnonvar_stock_total_format, selectedWarehouse.stock)
                switcherToggleParent.isChecked = selectedWarehouse.isToggleOn
                switcherToggleParent.setOnClickListener {
                    selectedWarehouse.isToggleOn = switcherToggleParent.isChecked
                    binding.containerProductChild.isVisible = selectedWarehouse.isToggleOn
                }
            }
            binding.containerProductChild.isVisible = selectedWarehouse.isToggleOn
            binding.containerLayoutProductInformation.apply {
                val discount = selectedWarehouse.discountSetup
                val criteria = product.productCriteria
                periodSection.gone()
                tickerPriceError.gone()
                textFieldPriceDiscountNominal.editText.setText(discount.price.toString())
                textFieldPriceDiscountPercentage.editText.setText(discount.discount.toString())
                quantityEditor.editText.setText(discount.stock.toString())
                textQuantityEditorTitle.text = root.context.getString(R.string.manageproductnonvar_stock_title)
                textQuantityEditorSubTitle.text = root.context.getString(R.string.manageproductnonvar_stock_subtitle, criteria.minCustomStock, criteria.maxCustomStock)
                setupListener(criteria, discount)
            }
        }
    }
}