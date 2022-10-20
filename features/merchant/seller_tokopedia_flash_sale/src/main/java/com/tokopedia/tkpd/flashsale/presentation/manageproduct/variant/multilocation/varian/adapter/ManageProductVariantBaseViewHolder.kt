package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian.adapter

import android.content.Context
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailInformationBinding
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailParentBinding
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.NumberTextInputUtil

open class ManageProductVariantBaseViewHolder(
    view: View,
    private val listener: ManageProductVariantAdapterListener?
) : RecyclerView.ViewHolder(view) {

    private var listenerOfEditTextDiscountNominal: TextWatcher? = null
    private var listenerOfEditTextDiscountPercent: TextWatcher? = null
    private var listenerQty: TextWatcher? = null
    private var listenerNumberFormatDiscountNominal: TextWatcher? = null
    private var listenerNumberFormatDiscountPercent: TextWatcher? = null

    //For Tracker Purpose
    private var listenerTrackerOfNominalDiscount: TextWatcher? = null
    private var listenerTrackerOfPercentDiscount: TextWatcher? = null

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

    private fun LayoutCampaignManageProductDetailInformationBinding.triggerListener(
        criteria: ReservedProduct.Product.ProductCriteria,
        discount: ReservedProduct.Product.Warehouse.DiscountSetup?
    ) {
        discount?.let {
            val validationResult = listener?.onDataInputChanged(absoluteAdapterPosition, criteria, it)
            val isPriceError = validationResult?.isPriceError == true
            val isPricePercentError = validationResult?.isPricePercentError == true
            this.textFieldPriceDiscountNominal.isInputError = isPriceError
            this.textFieldPriceDiscountPercentage.isInputError = isPricePercentError
            this.textQuantityEditorSubTitle.setTextColor(
                setColorText(
                    root.context,
                    validationResult?.isStockError == true
                )
            )
            this.textFieldPriceDiscountNominal.setMessage(validationResult?.priceMessage.orEmpty())
            this.textFieldPriceDiscountPercentage.setMessage(validationResult?.pricePercentMessage.orEmpty())
            this.tickerPriceError.setTypeOfTicker(isPriceError || isPricePercentError)
        }
    }

    private fun LayoutCampaignManageProductDetailInformationBinding.setupInitialFieldMessage(
        criteria: ReservedProduct.Product.ProductCriteria
    ) {
        this.textQuantityEditorSubTitle.text = root.context.getString(
            R.string.manageproductnonvar_stock_subtitle,
            criteria.minCustomStock,
            criteria.maxCustomStock
        )
        this.textFieldPriceDiscountNominal.setMessage(
            root.context.getString(
                R.string.manageproductnonvar_range_message_format,
                criteria.minFinalPrice.getCurrencyFormatted(),
                criteria.maxFinalPrice.getCurrencyFormatted()
            )
        )
        this.textFieldPriceDiscountPercentage.setMessage(
            root.context.getString(
                R.string.manageproductnonvar_range_message_format,
                criteria.minDiscount.getPercentFormatted(),
                criteria.maxDiscount.getPercentFormatted()
            )
        )
    }

    protected fun LayoutCampaignManageProductDetailInformationBinding.setupListener(
        criteria: ReservedProduct.Product.ProductCriteria,
        discount: ReservedProduct.Product.Warehouse.DiscountSetup?,
    ) {

        initialListerForTracker()
        setupListenerForTracker()

        listenerNumberFormatDiscountNominal =
            NumberTextInputUtil.setNumberTextChangeListener(textFieldPriceDiscountNominal)
        listenerNumberFormatDiscountPercent =
            NumberTextInputUtil.setNumberTextChangeListener(textFieldPriceDiscountPercentage)

        listenerOfEditTextDiscountNominal = EditTextWatcher {
            discount?.price = it.digitsOnly()
            this.textFieldPriceDiscountPercentage.setTextIfNotFocus(
                removeTracker = { removeTrackerListenerPercent() },
                text = listener?.calculatePercent(it.digitsOnly(), absoluteAdapterPosition).orEmpty(),
                addTracker = { addTrackerListenerPercent() }
            )
            triggerListener(criteria, discount)
        }
        this.textFieldPriceDiscountNominal.editText.addTextChangedListener(
            listenerOfEditTextDiscountNominal
        )
        this.textFieldPriceDiscountNominal.editText.addTextChangedListener(
            listenerNumberFormatDiscountNominal
        )

        listenerOfEditTextDiscountPercent = EditTextWatcher {
            discount?.discount = it.digitsOnly().toInt()
            this.textFieldPriceDiscountNominal.setTextIfNotFocus(
                removeTracker = { removeTrackerListenerNominal() },
                text = listener?.calculatePrice(it.digitsOnly(), absoluteAdapterPosition).toLongOrZero()
                    .getNumberFormatted(),
                addTracker = { this.addTrackerListenerNominal() }
            )
            triggerListener(criteria, discount)
        }
        this.textFieldPriceDiscountPercentage.editText.addTextChangedListener(
            listenerOfEditTextDiscountPercent
        )
        this.textFieldPriceDiscountPercentage.editText.addTextChangedListener(
            listenerNumberFormatDiscountPercent
        )

        listenerQty = EditTextWatcher {
            discount?.stock = it.digitsOnly()
            triggerListener(criteria, discount)
        }
        this.quantityEditor.editText.addTextChangedListener(listenerQty)
    }

    protected fun LayoutCampaignManageProductDetailInformationBinding.clearListener() {
        this.textFieldPriceDiscountNominal.editText.removeTextChangedListener(
            listenerOfEditTextDiscountNominal
        )
        this.textFieldPriceDiscountPercentage.editText.removeTextChangedListener(
            listenerOfEditTextDiscountPercent
        )
        this.textFieldPriceDiscountNominal.editText.removeTextChangedListener(
            listenerNumberFormatDiscountNominal
        )
        this.textFieldPriceDiscountPercentage.editText.removeTextChangedListener(
            listenerNumberFormatDiscountPercent
        )

        removeTrackerListenerNominal()
        removeTrackerListenerPercent()

        this.textFieldPriceDiscountPercentage.editText.removeTextChangedListener(
            listenerTrackerOfPercentDiscount
        )
        this.quantityEditor.editText.removeTextChangedListener(listenerQty)
    }

    protected fun LayoutCampaignManageProductDetailInformationBinding.setupInputField(
        criteria: ReservedProduct.Product.ProductCriteria,
        discount: ReservedProduct.Product.Warehouse.DiscountSetup?
    ) {
        this.periodSection.gone()
        this.tickerPriceError.gone()
        this.textFieldPriceDiscountNominal.editText.setText(
            discount?.price.orZero().getNumberFormatted()
        )
        this.textFieldPriceDiscountPercentage.editText.setText(
            discount?.discount.orZero().toString()
        )
        this.quantityEditor.editText.setText(discount?.stock?.orZero().toString())
        this.textQuantityEditorTitle.text =
            root.context.getString(R.string.manageproductnonvar_stock_title)
        val validationResult =
            discount?.let { listener?.validationItem(criteria, it) }
        val isPriceError = validationResult?.isPriceError == true
        val isPricePercentError = validationResult?.isPricePercentError == true
        this.textFieldPriceDiscountNominal.isInputError = isPriceError
        this.textFieldPriceDiscountPercentage.isInputError = isPricePercentError
        this.textQuantityEditorSubTitle.setTextColor(
            setColorText(
                root.context,
                validationResult?.isStockError == true
            )
        )
        this.tickerPriceError.setTypeOfTicker(isPriceError || isPricePercentError)

        setupInitialFieldMessage(criteria)
    }

    protected fun LayoutCampaignManageProductDetailParentBinding.setIsVariantOnCriteria(
        selectedWarehouse: ReservedProduct.Product.Warehouse,
    ) {

        val isVariantIneligible = selectedWarehouse.isDisabled

        if (isVariantIneligible) {
            groupVariantNotEligibleErrorMessage.visible()
            this.textParentErrorMessage.visible()
            this.textParentErrorMessage.text =
                root.context.getString(R.string.stfs_warning_location_not_in_criteria)
            this.textCheckDetail.visible()
            this.textCheckDetail.setOnClickListener {
                listener?.showDetailCriteria(
                    selectedWarehouse
                )
            }
            selectedWarehouse.isToggleOn = false
            this.switcherToggleParent.isEnabled = false
        } else {
            groupVariantNotEligibleErrorMessage.gone()
            this.textParentErrorMessage.gone()
            this.textCheckDetail.gone()
        }

        switcherToggleParent.isChecked =
            if (isVariantIneligible) false else selectedWarehouse.isToggleOn

    }

    protected fun LayoutCampaignManageProductDetailInformationBinding.setTicker(context: Context) {
        this.tickerPriceError.visible()
        this.tickerPriceError.setTextDescription(
            String.format(
                context.getString(
                    R.string.stfs_text_ticker_warning
                )
            )
        )
    }

    private fun setColorText(context: Context, isError: Boolean): Int {
        val errorColorRes = com.tokopedia.unifyprinciples.R.color.Unify_RN500
        val normalColorRes = com.tokopedia.unifyprinciples.R.color.Unify_NN600
        return MethodChecker.getColor(
            context,
            if (isError) {
                errorColorRes
            } else {
                normalColorRes
            }
        )
    }

    private fun Ticker.setTypeOfTicker(isFieldError: Boolean) {
        this.tickerType = if (isFieldError) Ticker.TYPE_WARNING else Ticker.TYPE_ANNOUNCEMENT

    }

    private fun initialListerForTracker() {
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

}
