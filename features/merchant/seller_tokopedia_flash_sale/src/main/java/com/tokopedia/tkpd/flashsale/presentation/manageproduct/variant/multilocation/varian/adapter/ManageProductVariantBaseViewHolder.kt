package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian.adapter

import android.content.Context
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailInformationBinding
import com.tokopedia.campaign.utils.constant.LocaleConstant
import com.tokopedia.campaign.utils.textwatcher.NumberThousandSeparatorTextWatcher
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.ticker.Ticker
import java.text.DecimalFormat
import java.text.NumberFormat

open class ManageProductVariantBaseViewHolder(
    view: View,
    private val listener: ManageProductVariantAdapterListener?
): RecyclerView.ViewHolder(view) {

    companion object {
        const val NUMBER_PATTERN = "#,###,###"
    }

    var listenerOfEditTextDiscountNominal : TextWatcher? = null
    var listenerOfEditTextDiscountPercent : TextWatcher? = null
    var listenerQty : TextWatcher? = null
    var listenerNumberFormatDiscountNominal : TextWatcher? = null
    var listenerNumberFormatDiscountPercent : TextWatcher? = null

    private fun Number?.toStringOrEmpty() =
        if (this == null || this.toLong() == Int.ZERO.toLong()) "" else toString()

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
            val validationResult = listener?.onDataInputChanged(adapterPosition, criteria, it)
            this.textFieldPriceDiscountNominal.isInputError = validationResult?.isPriceError == true
            this.textFieldPriceDiscountPercentage.isInputError = validationResult?.isPricePercentError == true
            this.textQuantityEditorSubTitle.setTextColor(setColorText(root.context, validationResult?.isStockError == true))
            this.textFieldPriceDiscountNominal.setMessage(validationResult?.priceMessage.orEmpty())
            this.textFieldPriceDiscountPercentage.setMessage(validationResult?.pricePercentMessage.orEmpty())
        }
    }

    private fun LayoutCampaignManageProductDetailInformationBinding.setupInitialFieldMessage(
        criteria: ReservedProduct.Product.ProductCriteria
    ) {
        this.textQuantityEditorSubTitle.text = root.context.getString(R.string.manageproductnonvar_stock_subtitle, criteria.minCustomStock, criteria.maxCustomStock)
        this.textFieldPriceDiscountNominal.setMessage(root.context.getString(
            R.string.manageproductnonvar_range_message_format,
            criteria.minFinalPrice.getCurrencyFormatted(),
            criteria.maxFinalPrice.getCurrencyFormatted()
        ))
        this.textFieldPriceDiscountPercentage.setMessage(root.context.getString(
            R.string.manageproductnonvar_range_message_format,
            criteria.minDiscount.getPercentFormatted(),
            criteria.maxDiscount.getPercentFormatted()
        ))
    }

    protected fun LayoutCampaignManageProductDetailInformationBinding.setupListener(
        criteria: ReservedProduct.Product.ProductCriteria,
        discount: ReservedProduct.Product.Warehouse.DiscountSetup?,
    ) {

        listenerNumberFormatDiscountNominal = setNumberTextChangeListener(textFieldPriceDiscountNominal)
        listenerNumberFormatDiscountPercent = setNumberTextChangeListener(textFieldPriceDiscountPercentage)

        listenerOfEditTextDiscountNominal = EditTextWatcher{
            discount?.price = it.digitsOnly()
            this.textFieldPriceDiscountPercentage.setTextIfNotFocus(
                listener?.calculatePercent(it.digitsOnly(), adapterPosition).orEmpty()
            )
            triggerListener(criteria, discount)
        }
        this.textFieldPriceDiscountNominal.editText.addTextChangedListener(listenerOfEditTextDiscountNominal)
        this.textFieldPriceDiscountNominal.editText.addTextChangedListener(listenerNumberFormatDiscountNominal)

        listenerOfEditTextDiscountPercent = EditTextWatcher{
            discount?.discount = it.digitsOnly().toInt()
            this.textFieldPriceDiscountNominal.setTextIfNotFocus(
                listener?.calculatePrice(it.digitsOnly(), adapterPosition).toLongOrZero().getNumberFormatted()
            )
            triggerListener(criteria, discount)
        }
        this.textFieldPriceDiscountPercentage.editText.addTextChangedListener(listenerOfEditTextDiscountPercent)
        this.textFieldPriceDiscountPercentage.editText.addTextChangedListener(listenerNumberFormatDiscountPercent)

        listenerQty = EditTextWatcher{
            discount?.stock = it.digitsOnly()
            triggerListener(criteria, discount)
        }
        this.quantityEditor.editText.addTextChangedListener(listenerQty)
    }

    protected fun LayoutCampaignManageProductDetailInformationBinding.clearListener() {
        this.textFieldPriceDiscountNominal.editText.removeTextChangedListener(listenerOfEditTextDiscountNominal)
        this.textFieldPriceDiscountPercentage.editText.removeTextChangedListener(listenerOfEditTextDiscountPercent)
        this.textFieldPriceDiscountNominal.editText.removeTextChangedListener(listenerNumberFormatDiscountNominal)
        this.textFieldPriceDiscountPercentage.editText.removeTextChangedListener(listenerNumberFormatDiscountPercent)
        this.quantityEditor.editText.removeTextChangedListener(listenerQty)
    }

    protected fun LayoutCampaignManageProductDetailInformationBinding.setupInputField(
        criteria: ReservedProduct.Product.ProductCriteria,
        discount: ReservedProduct.Product.Warehouse.DiscountSetup?
    ) {
        this.periodSection.gone()
        this.tickerPriceError.gone()
        this.textFieldPriceDiscountNominal.editText.setText(discount?.price.orZero().getNumberFormatted())
        this.textFieldPriceDiscountPercentage.editText.setText(discount?.discount.toStringOrEmpty())
        this.quantityEditor.editText.setText(discount?.stock?.orZero().toString())
        this.textQuantityEditorTitle.text = root.context.getString(R.string.manageproductnonvar_stock_title)
        val validationResult =
            discount?.let { listener?.validationItem(criteria, it) }
        this.textFieldPriceDiscountNominal.isInputError = validationResult?.isPriceError == true
        this.textFieldPriceDiscountPercentage.isInputError = validationResult?.isPricePercentError == true
        this.textQuantityEditorSubTitle.setTextColor(setColorText(root.context, validationResult?.isStockError == true))

        setupInitialFieldMessage(criteria)
    }

    protected fun LayoutCampaignManageProductDetailInformationBinding.setTicker(context: Context) {
        this.tickerPriceError.visible()
        this.tickerPriceError.tickerType = Ticker.TYPE_ANNOUNCEMENT
        this.tickerPriceError.setTextDescription(
            String.format(
                context.getString(
                    R.string.stfs_text_ticker_warning
                )
            )
        )
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

    private fun setNumberTextChangeListener(editText : TextFieldUnify2): TextWatcher {
        val numberFormatter = NumberFormat.getInstance(LocaleConstant.INDONESIA) as DecimalFormat
        numberFormatter.applyPattern(NUMBER_PATTERN)
        return NumberThousandSeparatorTextWatcher(
            editText.editText, numberFormatter
        ) { _, formatNumber ->
            editText.editText.setText(formatNumber)
            editText.editText.setSelection(
                editText.editText.text?.length.orZero()
            )
        }
    }
}