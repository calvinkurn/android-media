package com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailInformationBinding
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.unifycomponents.TextFieldUnify2

open class ManageProductNonVariantBaseViewHolder(
    view: View,
    private val listener: ManageProductNonVariantAdapterListener?
): RecyclerView.ViewHolder(view) {

    private fun Number?.toStringOrEmpty() = if (this == null || this == Int.ZERO) "" else toString()

    private fun TextFieldUnify2.setTextIfNotFocus(text: String) {
        if (!editText.isFocused) {
            editText.setText(text)
        }
    }

    private fun LayoutCampaignManageProductDetailInformationBinding.triggerListener(
        criteria: ReservedProduct.Product.ProductCriteria,
        discount: ReservedProduct.Product.Warehouse.DiscountSetup?
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
            textFieldPriceDiscountNominal.setMessage(validationResult?.priceMessage.orEmpty())
            textFieldPriceDiscountPercentage.setMessage(validationResult?.pricePercentMessage.orEmpty())
        }
    }

    private fun LayoutCampaignManageProductDetailInformationBinding.setupInitialFieldMessage(
        criteria: ReservedProduct.Product.ProductCriteria
    ) {
        textQuantityEditorSubTitle.text = root.context.getString(R.string.manageproductnonvar_stock_subtitle, criteria.minCustomStock, criteria.maxCustomStock)
        textFieldPriceDiscountNominal.setMessage(root.context.getString(
            R.string.manageproductnonvar_range_message_format,
            criteria.minFinalPrice.getCurrencyFormatted(),
            criteria.maxFinalPrice.getCurrencyFormatted()
        ))
        textFieldPriceDiscountPercentage.setMessage(root.context.getString(
            R.string.manageproductnonvar_range_message_format,
            criteria.minDiscount.getPercentFormatted(),
            criteria.maxDiscount.getPercentFormatted()
        ))
    }

    protected fun LayoutCampaignManageProductDetailInformationBinding.setupListener(
        criteria: ReservedProduct.Product.ProductCriteria,
        discount: ReservedProduct.Product.Warehouse.DiscountSetup?
    ) {
        textFieldPriceDiscountNominal.editText.afterTextChanged {
            discount?.price = it.digitsOnly()
            textFieldPriceDiscountPercentage.setTextIfNotFocus(
                listener?.calculatePercent(it.digitsOnly(), adapterPosition).orEmpty()
            )
            triggerListener(criteria, discount)
        }
        textFieldPriceDiscountPercentage.editText.afterTextChanged {
            discount?.discount = it.digitsOnly().toInt()
            textFieldPriceDiscountNominal.setTextIfNotFocus(
                listener?.calculatePrice(it.digitsOnly(), adapterPosition).orEmpty()
            )
            triggerListener(criteria, discount)
        }
        quantityEditor.editText.afterTextChanged {
            discount?.stock = it.digitsOnly()
            triggerListener(criteria, discount)
        }
    }

    protected fun LayoutCampaignManageProductDetailInformationBinding.setupInputField(
        criteria: ReservedProduct.Product.ProductCriteria,
        discount: ReservedProduct.Product.Warehouse.DiscountSetup?
    ) {
        periodSection.gone()
        tickerPriceError.gone()
        textFieldPriceDiscountNominal.editText.setText(discount?.price.toStringOrEmpty())
        textFieldPriceDiscountPercentage.editText.setText(discount?.discount.toStringOrEmpty())
        quantityEditor.editText.setText(discount?.stock?.orZero().toString())
        textQuantityEditorTitle.text = root.context.getString(R.string.manageproductnonvar_stock_title)

        textFieldPriceDiscountNominal.editText.setModeToNumberDelimitedInput()
        textFieldPriceDiscountPercentage.editText.setModeToNumberDelimitedInput()
        setupInitialFieldMessage(criteria)
    }
}