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
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse.DiscountSetup
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.ProductCriteria
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.NumberTextInputUtil
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian.adapter.EditTextWatcher
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.ticker.Ticker

open class ManageProductNonVariantBaseViewHolder(
    view: View,
    private val listener: ManageProductNonVariantAdapterListener?
): RecyclerView.ViewHolder(view) {

    // For Tracker Purposes
    private var listenerTrackerOfNominalDiscount: TextWatcher? = null
    private var listenerTrackerOfPercentDiscount: TextWatcher? = null

    private var isEditing = false
    private var listenerNumberFormatDiscountNominal: TextWatcher? = null
    private var listenerNumberFormatDiscountPercent: TextWatcher? = null

    private fun Number?.toStringOrEmpty() =
        if (this == null || this.toLong() == Int.ZERO.toLong()) "" else getNumberFormatted()

    private fun TextFieldUnify2.setTextIfNotFocus(
        removeTracker: () -> Unit,
        text: String,
        addTracker: () -> Unit
    ) {
        if (!editText.isFocused) {
            removeTracker()
            editText.setText(text)
            addTracker()
        }
    }

    private fun LayoutCampaignManageProductDetailInformationBinding.addTrackerListenerNominal() {
        textFieldPriceDiscountNominal.editText.addTextChangedListener(
            listenerTrackerOfNominalDiscount
        )
    }

    private fun LayoutCampaignManageProductDetailInformationBinding.addTrackerListenerPercent() {
        textFieldPriceDiscountPercentage.editText.addTextChangedListener(
            listenerTrackerOfPercentDiscount
        )
    }

    private fun LayoutCampaignManageProductDetailInformationBinding.removeTrackerListenerNominal() {
        textFieldPriceDiscountNominal.editText.removeTextChangedListener(
            listenerTrackerOfNominalDiscount
        )
    }

    private fun LayoutCampaignManageProductDetailInformationBinding.removeTrackerListenerPercent() {
        textFieldPriceDiscountPercentage.editText.removeTextChangedListener(
            listenerTrackerOfPercentDiscount
        )
    }

    private fun LayoutCampaignManageProductDetailInformationBinding.triggerListener(
        criteria: ProductCriteria,
        discount: DiscountSetup?
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
        criteria: ProductCriteria
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

    private fun applyListeners(binding: LayoutCampaignManageProductDetailInformationBinding) {
        binding.apply {
            listenerNumberFormatDiscountNominal = NumberTextInputUtil.setNumberTextChangeListener(textFieldPriceDiscountNominal)
            listenerNumberFormatDiscountPercent = NumberTextInputUtil.setNumberTextChangeListener(textFieldPriceDiscountPercentage)
            listenerTrackerOfNominalDiscount = EditTextWatcher { listener?.trackOnClickPrice(it) }
            listenerTrackerOfPercentDiscount = EditTextWatcher { listener?.trackOnClickPercent(it) }
            addTrackerListenerNominal()
            addTrackerListenerPercent()
            textFieldPriceDiscountNominal.editText.addTextChangedListener(listenerNumberFormatDiscountNominal)
            textFieldPriceDiscountPercentage.editText.addTextChangedListener(listenerNumberFormatDiscountPercent)
        }
    }

    private fun removeListeners(binding: LayoutCampaignManageProductDetailInformationBinding) {
        binding.apply {
            removeTrackerListenerNominal()
            removeTrackerListenerPercent()
            textFieldPriceDiscountNominal.editText.removeTextChangedListener(listenerNumberFormatDiscountNominal)
            textFieldPriceDiscountPercentage.editText.removeTextChangedListener(listenerNumberFormatDiscountPercent)
        }
    }

    protected fun LayoutCampaignManageProductDetailInformationBinding.setupInputListener(
        criteria: ProductCriteria,
        discount: DiscountSetup?
    ) {
        textFieldPriceDiscountNominal.editText.afterTextChanged {
            if (isEditing) return@afterTextChanged
            discount?.price = it.digitsOnly()
            textFieldPriceDiscountPercentage.setTextIfNotFocus(
                removeTracker = { removeTrackerListenerPercent() },
                text = listener?.calculatePercent(it.digitsOnly(), absoluteAdapterPosition).orEmpty(),
                addTracker = { addTrackerListenerPercent() }
            )
            triggerListener(criteria, discount)
        }
        textFieldPriceDiscountPercentage.editText.afterTextChanged {
            if (isEditing) return@afterTextChanged
            discount?.discount = it.digitsOnly().toInt()
            textFieldPriceDiscountNominal.setTextIfNotFocus(
                removeTracker = { removeTrackerListenerNominal() },
                text = listener?.calculatePrice(it.digitsOnly(), absoluteAdapterPosition).orEmpty(),
                addTracker = { addTrackerListenerNominal() }
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
        criteria: ProductCriteria,
        discount: DiscountSetup?
    ) {
        removeListeners(this)
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
        if (textFieldPriceDiscountNominal.editText.text.isNotEmpty() ||
            textFieldPriceDiscountPercentage.editText.text.isNotEmpty()) {
            triggerListener(criteria, discount)
        }
        isEditing = false
        applyListeners(this)
    }

    protected fun LayoutCampaignManageProductDetailInformationBinding.setTicker(context: Context) {
        tickerPriceError.visible()
        tickerPriceError.setTextDescription(String.format(context.getString(R.string.stfs_text_ticker_warning)))
        tickerPriceError.requestLayout()
    }

    protected fun LayoutCampaignManageProductDetailParentBinding.setupIneligibleLocation(
        warehouse: Warehouse,
        product: Product
    ) {
        if (warehouse.isDisabled) {
            groupVariantNotEligibleErrorMessage.visible()
            textParentErrorMessage.visible()
            textParentErrorMessage.text = root.context.getString(R.string.stfs_warning_location_not_in_criteria)
            textCheckDetail.visible()
            textCheckDetail.setOnClickListener { listener?.showDetailCriteria(adapterPosition, warehouse, product) }
            warehouse.isToggleOn = false
            switcherToggleParent.disable()
            switcherToggleParent.isChecked = false
        } else {
            groupVariantNotEligibleErrorMessage.gone()
            switcherToggleParent.isChecked = warehouse.isToggleOn
        }
    }
}
