package com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter

import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailInformationBinding
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailParentBinding
import com.tokopedia.campaign.utils.extension.disable
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian.adapter.EditTextWatcher
import com.tokopedia.unifycomponents.TextFieldUnify2

open class ManageProductNonVariantBaseViewHolder(
    view: View,
    private val listener: ManageProductNonVariantAdapterListener?
): RecyclerView.ViewHolder(view) {

    // For Tracker Purposes
    private var listenerTrackerOfNominalDiscount: TextWatcher? = null
    private var listenerTrackerOfPercentDiscount: TextWatcher? = null

    private fun Number?.toStringOrEmpty() =
        if (this == null || this.toLong() == Int.ZERO.toLong()) "" else toString()

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

    private fun initListenerForTracker() {
        listenerTrackerOfNominalDiscount = EditTextWatcher {
            listener?.trackOnClickPrice(it)
        }

        listenerTrackerOfPercentDiscount = EditTextWatcher {
            listener?.trackOnClickPercent(it)
        }
    }

    private fun LayoutCampaignManageProductDetailInformationBinding.setupListenerForTracker() {
        addTrackerListenerNominal()
        addTrackerListenerPercent()
    }

    private fun LayoutCampaignManageProductDetailInformationBinding.addTrackerListenerNominal() {
        this.textFieldPriceDiscountNominal.editText.addTextChangedListener(
            listenerTrackerOfNominalDiscount
        )
    }

    private fun LayoutCampaignManageProductDetailInformationBinding.addTrackerListenerPercent() {
        this.textFieldPriceDiscountPercentage.editText.addTextChangedListener(
            listenerTrackerOfPercentDiscount
        )
    }

    private fun LayoutCampaignManageProductDetailInformationBinding.removeTrackerListenerNominal() {
        this.textFieldPriceDiscountNominal.editText.removeTextChangedListener(
            listenerTrackerOfNominalDiscount
        )
    }

    private fun LayoutCampaignManageProductDetailInformationBinding.removeTrackerListenerPercent() {
        this.textFieldPriceDiscountPercentage.editText.removeTextChangedListener(
            listenerTrackerOfPercentDiscount
        )
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
        initListenerForTracker()
        setupListenerForTracker()

        textFieldPriceDiscountNominal.editText.afterTextChanged {
            discount?.price = it.digitsOnly()
            textFieldPriceDiscountPercentage.setTextIfNotFocus(
                text = listener?.calculatePercent(it.digitsOnly(), adapterPosition).orEmpty(),
                removeTracker = { removeTrackerListenerNominal() },
                addTracker = { addTrackerListenerNominal() }
            )
            triggerListener(criteria, discount)
        }
        textFieldPriceDiscountPercentage.editText.afterTextChanged {
            discount?.discount = it.digitsOnly().toInt()
            textFieldPriceDiscountNominal.setTextIfNotFocus(
                text = listener?.calculatePrice(it.digitsOnly(), adapterPosition).orEmpty(),
                removeTracker = { removeTrackerListenerPercent() },
                addTracker = { addTrackerListenerPercent() }
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
        quantityEditor.minValue = criteria.minCustomStock
        quantityEditor.maxValue = criteria.maxCustomStock
        textQuantityEditorTitle.text = root.context.getString(R.string.manageproductnonvar_stock_title)

        textFieldPriceDiscountNominal.editText.setModeToNumberDelimitedInput()
        textFieldPriceDiscountPercentage.editText.setModeToNumberDelimitedInput()
        setupInitialFieldMessage(criteria)
    }

    protected fun LayoutCampaignManageProductDetailParentBinding.setupIneligibleLocation(
        warehouse: ReservedProduct.Product.Warehouse,
        product: ReservedProduct.Product
    ) {
        if (warehouse.isDisabled) {
            textParentErrorMessage.visible()
            textParentErrorMessage.text = root.context.getString(R.string.stfs_warning_location_not_in_criteria)
            tvCheckDetail.visible()
            tvCheckDetail.setOnClickListener { listener?.showDetailCriteria(adapterPosition, warehouse, product) }
            warehouse.isToggleOn = false
            switcherToggleParent.disable()
            switcherToggleParent.isChecked = false
        } else {
            switcherToggleParent.isChecked = warehouse.isToggleOn
        }
    }
}
