package com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter

import android.content.Context
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailInformationBinding
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailParentBinding
import com.tokopedia.campaign.utils.extension.disable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.digitsOnly
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.kotlin.extensions.view.getPercentFormatted
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.NumberTextInputUtil
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.ticker.Ticker

open class ManageProductNonVariantBaseViewHolder(
    view: View,
    private val listener: ManageProductNonVariantAdapterListener?
): RecyclerView.ViewHolder(view) {

    private var isEditing = false
    private var listenerNumberFormatDiscountNominal : TextWatcher? = null
    private var listenerNumberFormatDiscountPercent : TextWatcher? = null

    private fun Number?.toStringOrEmpty() =
        if (this == null || this.toLong() == Int.ZERO.toLong()) "" else getNumberFormatted()

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
            val isDiscountInvalid = validationResult?.isPriceError == true || validationResult?.isPricePercentError == true
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
            tickerPriceError.tickerType = if (isDiscountInvalid) Ticker.TYPE_WARNING else Ticker.TYPE_ANNOUNCEMENT
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
            if (isEditing) return@afterTextChanged
            discount?.price = it.digitsOnly()
            textFieldPriceDiscountPercentage.setTextIfNotFocus(
                listener?.calculatePercent(it.digitsOnly(), adapterPosition).orEmpty()
            )
            triggerListener(criteria, discount)
        }
        textFieldPriceDiscountPercentage.editText.afterTextChanged {
            if (isEditing) return@afterTextChanged
            discount?.discount = it.digitsOnly().toInt()
            textFieldPriceDiscountNominal.setTextIfNotFocus(
                listener?.calculatePrice(it.digitsOnly(), adapterPosition).orEmpty()
            )
            triggerListener(criteria, discount)
        }
        quantityEditor.editText.afterTextChanged {
            if (isEditing) return@afterTextChanged
            discount?.stock = it.digitsOnly()
            triggerListener(criteria, discount)
        }
    }

    protected fun LayoutCampaignManageProductDetailInformationBinding.setupInputField(
        criteria: ReservedProduct.Product.ProductCriteria,
        discount: ReservedProduct.Product.Warehouse.DiscountSetup?
    ) {
        textFieldPriceDiscountNominal.editText.removeTextChangedListener(listenerNumberFormatDiscountNominal)
        textFieldPriceDiscountPercentage.editText.removeTextChangedListener(listenerNumberFormatDiscountPercent)
        isEditing = true
        periodSection.gone()
        tickerPriceError.gone()
        textFieldPriceDiscountNominal.editText.setText(discount?.price.toStringOrEmpty())
        textFieldPriceDiscountPercentage.editText.setText(discount?.discount.toStringOrEmpty())
        quantityEditor.editText.setText(discount?.stock?.orZero().toString())
        quantityEditor.minValue = criteria.minCustomStock
        quantityEditor.maxValue = criteria.maxCustomStock
        textQuantityEditorTitle.text = root.context.getString(R.string.manageproductnonvar_stock_title)
        setupInitialFieldMessage(criteria)
        isEditing = false
        listenerNumberFormatDiscountNominal = NumberTextInputUtil.setNumberTextChangeListener(textFieldPriceDiscountNominal)
        listenerNumberFormatDiscountPercent = NumberTextInputUtil.setNumberTextChangeListener(textFieldPriceDiscountPercentage)
        textFieldPriceDiscountNominal.editText.addTextChangedListener(listenerNumberFormatDiscountNominal)
        textFieldPriceDiscountPercentage.editText.addTextChangedListener(listenerNumberFormatDiscountPercent)
    }

    protected fun LayoutCampaignManageProductDetailInformationBinding.setTicker(context: Context) {
        tickerPriceError.visible()
        tickerPriceError.setTextDescription(String.format(context.getString(R.string.stfs_text_ticker_warning)))
        tickerPriceError.requestLayout()
    }

    protected fun LayoutCampaignManageProductDetailParentBinding.setupIneligibleLocation(
        warehouse: ReservedProduct.Product.Warehouse
    ) {
        if (warehouse.isDisabled) {
            groupVariantNotEligibleErrorMessage.visible()
            textParentErrorMessage.visible()
            textParentErrorMessage.text = root.context.getString(R.string.stfs_warning_location_not_in_criteria)
            textCheckDetail.visible()
            textCheckDetail.setOnClickListener { listener?.showDetailCriteria(adapterPosition) }
            warehouse.isToggleOn = false
            switcherToggleParent.disable()
            switcherToggleParent.isChecked = false
        } else {
            groupVariantNotEligibleErrorMessage.gone()
            switcherToggleParent.isChecked = warehouse.isToggleOn
        }
    }
}
