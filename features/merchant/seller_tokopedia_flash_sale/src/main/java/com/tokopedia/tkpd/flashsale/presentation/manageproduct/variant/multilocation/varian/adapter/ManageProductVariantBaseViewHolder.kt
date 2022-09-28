package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailInformationBinding
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.unifycomponents.TextFieldUnify2

open class ManageProductVariantBaseViewHolder(
    view: View,
    private val listener: ManageProductVariantAdapterListener?
): RecyclerView.ViewHolder(view) {

    fun Number?.toStringOrEmpty() =
        if (this == null || this.toLong() == Int.ZERO.toLong()) "" else toString()

    fun TextFieldUnify2.setTextIfNotFocus(text: String) {
        if (!editText.isFocused) {
            editText.setText(text)
        }
    }

    private fun LayoutCampaignManageProductDetailInformationBinding.triggerListener(
        criteria: ReservedProduct.Product.ProductCriteria,
        discount: ReservedProduct.Product.Warehouse.DiscountSetup?
    ) {
        discount?.let {
            val validationResult = listener?.onDataInputChanged(adapterPosition, criteria, it)
            textFieldPriceDiscountNominal.isInputError = validationResult?.isPriceError == true
            textFieldPriceDiscountPercentage.isInputError = validationResult?.isPricePercentError == true
            textQuantityEditorSubTitle.setTextColor(setColorText(root.context, validationResult?.isStockError == true))
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
        discount: ReservedProduct.Product.Warehouse.DiscountSetup?,
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
        textFieldPriceDiscountNominal.editText.setText(discount?.price.orZero().getNumberFormatted())
        textFieldPriceDiscountPercentage.editText.setText(discount?.discount.toStringOrEmpty())
        quantityEditor.editText.setText(discount?.stock?.orZero().toString())
        textQuantityEditorTitle.text = root.context.getString(R.string.manageproductnonvar_stock_title)
        val validationResult =
            discount?.let { listener?.onDataInputChanged(adapterPosition, criteria, it) }
        textQuantityEditorSubTitle.setTextColor(setColorText(root.context, validationResult?.isStockError == true))


        textFieldPriceDiscountNominal.editText.setModeToNumberDelimitedInput()
        textFieldPriceDiscountPercentage.editText.setModeToNumberDelimitedInput()
        setupInitialFieldMessage(criteria)
    }

    private fun setColorText(context : Context, isError : Boolean) : Int{
        val errorColorRes = com.tokopedia.unifyprinciples.R.color.Unify_RN500
        val normalColorRes = com.tokopedia.unifyprinciples.R.color.Unify_NN600
        return MethodChecker.getColor(context,
            if (isError) {
                errorColorRes
            } else {
                normalColorRes
            }
        )
    }
}