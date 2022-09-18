package com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailInformationBinding
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemManageProductNonvariantBinding
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse.DiscountSetup
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.ProductCriteria
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult

class ManageProductNonVariantAdapter: RecyclerView.Adapter<ManageProductNonVariantAdapter.CriteriaViewHolder>() {

    private var data: List<ReservedProduct.Product> = emptyList()
    private var listener: ManageProductNonVariantAdapterListener? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val binding = StfsItemManageProductNonvariantBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CriteriaViewHolder(binding, listener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    fun setDataList(newData: List<ReservedProduct.Product>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    fun setListener(listener: ManageProductNonVariantAdapterListener) {
        this.listener = listener
    }

    inner class CriteriaViewHolder(
        private val binding: StfsItemManageProductNonvariantBinding,
        private val listener: ManageProductNonVariantAdapterListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        private fun Number?.toStringOrEmpty(): String {
            return this?.toString().orEmpty()
        }

        private fun LayoutCampaignManageProductDetailInformationBinding.triggerListener(
            criteria: ProductCriteria,
            discount: DiscountSetup?
        ) {
            discount?.let {
                val errorColorRes = com.tokopedia.unifyprinciples.R.color.Unify_RN500
                val normalColorRes = com.tokopedia.unifyprinciples.R.color.Unify_NN600
                val validationResult = listener?.onDataInputChanged(criteria, it)
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

        fun bind(item: ReservedProduct.Product) {
            binding.mainLayout.apply {
                val discount = item.warehouses.firstOrNull()?.discountSetup
                val criteria = item.productCriteria
                periodSection.gone()
                tickerPriceError.gone()
                textFieldPriceDiscountNominal.editText.setText(discount?.price?.toStringOrEmpty())
                textFieldPriceDiscountPercentage.editText.setText(discount?.discount?.toStringOrEmpty())
                quantityEditor.editText.setText(discount?.stock?.orZero().toString())
                textQuantityEditorTitle.text = root.context.getString(R.string.manageproductnonvar_stock_title)
                textQuantityEditorSubTitle.text = root.context.getString(R.string.manageproductnonvar_stock_subtitle, criteria.minCustomStock, criteria.maxCustomStock)
                setupListener(criteria, discount)
            }
        }
    }

    interface ManageProductNonVariantAdapterListener {
        fun onDataInputChanged(criteria: ProductCriteria, discountSetup: DiscountSetup): ValidationResult
    }
}