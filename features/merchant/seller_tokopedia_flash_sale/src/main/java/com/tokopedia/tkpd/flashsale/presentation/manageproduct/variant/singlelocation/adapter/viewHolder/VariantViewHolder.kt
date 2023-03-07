package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailVariantItemBinding
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter.ManageProductVariantListener
import com.tokopedia.unifycomponents.TextFieldUnify2

class VariantViewHolder(
    private val binding: LayoutCampaignManageProductDetailVariantItemBinding,
    private val listener: ManageProductVariantListener?
) : RecyclerView.ViewHolder(binding.root) {

    private fun Number?.toStringOrEmpty() =
        if (this == null || this.toLong() == Int.ZERO.toLong()) "" else toString()

    private fun TextFieldUnify2.setTextIfNotFocus(text: String) {
        if (!editText.isFocused) {
            editText.setText(text)
        }
    }

    fun bind(
        product: ReservedProduct.Product,
        selectedChildProduct: ReservedProduct.Product.ChildProduct
    ) {
        val discount = selectedChildProduct.warehouses.firstOrNull()?.discountSetup
        val criteria = product.productCriteria
        binding.containerLayoutProductParent.apply {
            textParentTitle.text = selectedChildProduct.name
            textParentOriginalPrice.text =
                selectedChildProduct.price.price.getCurrencyFormatted()
            textParentTotalStock.text = root.context.getString(
                R.string.manageproductnonvar_stock_total_format,
                selectedChildProduct.stock
            )
            switcherToggleParent.apply {
                isChecked = selectedChildProduct.isToggleOn
                setOnClickListener {
                    selectedChildProduct.isToggleOn = switcherToggleParent.isChecked
                    binding.containerProductChild.isVisible = selectedChildProduct.isToggleOn
                    listener?.onToggleSwitch(adapterPosition, switcherToggleParent.isChecked)
                    discount?.let { disc ->
                        listener?.onDataInputChanged(
                            adapterPosition,
                            product, disc
                        )
                    }
                }
                isEnabled = !selectedChildProduct.isDisabled
            }
            if (selectedChildProduct.isDisabled) {
                groupVariantNotEligibleErrorMessage.visible()
            } else {
                groupVariantNotEligibleErrorMessage.gone()
            }
            textCheckDetail.setOnClickListener {
                listener?.onIneligibleProductWithSingleWarehouseClicked(
                    absoluteAdapterPosition,
                    selectedChildProduct,
                    product.productCriteria
                )
            }
            binding.containerProductChild.isVisible = selectedChildProduct.isToggleOn
            binding.containerLayoutProductInformation.apply {
                periodSection.gone()
                tickerPriceError.gone()
                textFieldPriceDiscountNominal.editText.setText(discount?.price.toStringOrEmpty())
                textFieldPriceDiscountPercentage.editText.setText(discount?.discount.toStringOrEmpty())
                quantityEditor.minValue = criteria.minCustomStock
                quantityEditor.maxValue = criteria.maxCustomStock
                quantityEditor.editText.setText(discount?.stock.orZero().toString())
                textQuantityEditorTitle.text =
                    root.context.getString(R.string.manageproductnonvar_stock_title)
                textFieldPriceDiscountNominal.editText.setModeToNumberDelimitedInput()
                textFieldPriceDiscountPercentage.editText.setModeToNumberDelimitedInput()
                textQuantityEditorSubTitle.text = root.context.getString(
                    R.string.manageproductnonvar_stock_subtitle,
                    criteria.minCustomStock,
                    criteria.maxCustomStock
                )
                textFieldPriceDiscountNominal.setMessage(
                    root.context.getString(
                        R.string.manageproductnonvar_range_message_format,
                        criteria.minFinalPrice.getCurrencyFormatted(),
                        criteria.maxFinalPrice.getCurrencyFormatted()
                    )
                )
                textFieldPriceDiscountPercentage.setMessage(
                    root.context.getString(
                        R.string.manageproductnonvar_range_message_format,
                        criteria.minDiscount.getPercentFormatted(),
                        criteria.maxDiscount.getPercentFormatted()
                    )
                )

                if (textFieldPriceDiscountPercentage.editText.text.isNotEmpty() ||
                    textFieldPriceDiscountNominal.editText.text.isNotEmpty()) {
                    triggerListener(product, discount)
                }

                textFieldPriceDiscountNominal.textInputLayout.editText?.afterTextChanged {
                    discount?.price = it.digitsOnly()
                    val discountPercent =
                        listener?.calculatePercent(it.digitsOnly(), adapterPosition).orEmpty()
                    textFieldPriceDiscountPercentage.setTextIfNotFocus(discountPercent)
                    listener?.onDiscountChange(
                        adapterPosition,
                        it.digitsOnly(),
                        discountPercent.toIntOrZero()
                    )
                    triggerListener(product, discount)
                }

                textFieldPriceDiscountPercentage.editText.afterTextChanged {
                    discount?.discount = it.digitsOnly().toInt()
                    val discountPrice =
                        listener?.calculatePrice(it.digitsOnly(), adapterPosition).orEmpty()
                    textFieldPriceDiscountNominal.setTextIfNotFocus(discountPrice)
                    listener?.onDiscountChange(
                        adapterPosition,
                        discountPrice.toLong(),
                        it.digitsOnly().toInt()
                    )
                    triggerListener(product, discount)
                }

                quantityEditor.apply {
                    maxValue = criteria.maxCustomStock
                    minValue = criteria.minCustomStock
                    editText.afterTextChanged {
                        discount?.stock = it.digitsOnly()
                        discount?.stock?.let { disc -> listener?.onStockChange(adapterPosition, disc) }
                        triggerListener(product, discount)
                    }
                }
            }
        }
    }

    private fun triggerListener(
        product: ReservedProduct.Product,
        discount: ReservedProduct.Product.Warehouse.DiscountSetup?
    ) {
        discount?.let {
            val errorColorRes = com.tokopedia.unifyprinciples.R.color.Unify_RN500
            val normalColorRes = com.tokopedia.unifyprinciples.R.color.Unify_NN600
            val validationResult = listener?.onDataInputChanged(adapterPosition, product, it)
            binding.containerLayoutProductInformation.apply {
                textFieldPriceDiscountNominal.isInputError =
                    validationResult?.isPriceError == true
                textFieldPriceDiscountPercentage.isInputError =
                    validationResult?.isPricePercentError == true
                textQuantityEditorSubTitle.setTextColor(
                    MethodChecker.getColor(
                        root.context,
                        if (validationResult?.isStockError == true) {
                            errorColorRes
                        } else {
                            normalColorRes
                        }
                    )
                )
                textFieldPriceDiscountNominal.setMessage(validationResult?.priceMessage.orEmpty())
                textFieldPriceDiscountPercentage.setMessage(validationResult?.pricePercentMessage.orEmpty())
            }
        }
    }
}
