package com.tokopedia.product.addedit.variant.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.MAX_LENGTH_STOCK_INPUT
import com.tokopedia.product.addedit.common.util.getText
import com.tokopedia.product.addedit.common.util.getTextBigIntegerOrZero
import com.tokopedia.product.addedit.common.util.setModeToNumberInput
import com.tokopedia.product.addedit.tracking.ProductAddVariantDetailTracking
import com.tokopedia.product.addedit.tracking.ProductEditVariantDetailTracking
import com.tokopedia.product.addedit.variant.presentation.model.MultipleVariantEditInputModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker

class MultipleVariantEditInputBottomSheet(
        private var enableEditSku: Boolean = false,
        private var enableEditPrice: Boolean = false,
        private val couldShowMultiLocationTicker: Boolean = false,
        private val multipleVariantEditInputListener: MultipleVariantEditListener? = null
): BottomSheetUnify() {

    companion object {
        const val TAG = "Tag Multiple Variant Edit Input"
    }

    private var contentView: View? = null
    private var tfuStock: TextFieldUnify? = null
    private var tfuSku: TextFieldUnify? = null
    private var tfuPrice: TextFieldUnify? = null
    private var tfuWeight: TextFieldUnify? = null
    private var buttonApply: UnifyButton? = null

    private var trackerShopId = ""
    private var trackerIsEditMode = false

    init {
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        clearContentPadding = true
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addMarginCloseButton()
    }

    fun setTrackerShopId(shopId: String) {
        trackerShopId = shopId
    }

    fun setTrackerIsEditMode(isEditMode: Boolean) {
        trackerIsEditMode = isEditMode
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }

    private fun addMarginCloseButton() {
        val topMargin = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3).orZero()
        val horizontalMargin = context?.resources?.getDimensionPixelSize(R.dimen.tooltip_close_margin).orZero()
        (bottomSheetClose.layoutParams as RelativeLayout.LayoutParams).apply {
            setMargins(0, topMargin, horizontalMargin, 0)
            addRule(RelativeLayout.CENTER_VERTICAL)
        }
    }

    private fun initChildLayout() {
        setTitle(getString(com.tokopedia.product.addedit.R.string.label_variant_multiple_input_bottom_sheet_title))
        overlayClickDismiss = false

        contentView = View.inflate(context,
                R.layout.add_edit_product_multiple_variant_edit_input_bottom_sheet_content, null)
        tfuStock = contentView?.findViewById(R.id.tfuStock)
        tfuSku = contentView?.findViewById(R.id.tfuSku)
        tfuPrice = contentView?.findViewById(R.id.tfuPrice)
        tfuWeight = contentView?.findViewById(R.id.tfuWeight)
        buttonApply = contentView?.findViewById(R.id.buttonApply)

        tfuPrice?.setFieldVisibility(enableEditPrice, tfuStock)
        tfuPrice.setModeToNumberInput()
        tfuPrice?.textFieldInput?.afterTextChanged {
            if (enableEditPrice) {
                validatePrice()
            }
        }

        tfuStock.setModeToNumberInput(MAX_LENGTH_STOCK_INPUT)
        tfuStock?.textFieldInput?.afterTextChanged {
            validateStock()
        }

        tfuWeight.setModeToNumberInput()
        tfuWeight?.textFieldInput?.afterTextChanged {
            validateWeight()
        }

        tfuSku?.setFieldVisibility(enableEditSku, tfuWeight)
        tfuSku?.textFieldInput?.afterTextChanged {
            updateSubmitButtonInput()
        }

        buttonApply?.setOnClickListener {
            submitInput()
            updateSubmitButtonInput()
            sendTrackerTrackManageAllPriceData()
        }

        contentView?.findViewById<Ticker>(R.id.ticker_multiple_variant_multi_location)?.let { multiLocationTicker ->
            multiLocationTicker.showWithCondition(couldShowMultiLocationTicker)
            multiLocationTicker.setTextDescription(context?.resources?.getString(R.string.ticker_edit_variant_main_location).toString())
        }
        setChild(contentView)
    }

    private fun sendTrackerTrackManageAllPriceData() {
        val price = tfuPrice.getText()
        val stock = tfuStock.getText()
        val sku = tfuSku.getText()

        if (trackerIsEditMode) {
            if (price.isNotEmpty())
                ProductEditVariantDetailTracking.trackManageAllPrice(price, trackerShopId)
            if (stock.isNotEmpty())
                ProductEditVariantDetailTracking.trackManageAllStock(stock, trackerShopId)
            if (sku.isNotEmpty())
                ProductEditVariantDetailTracking.trackManageAllSku(sku, trackerShopId)
        } else {
            if (price.isNotEmpty())
                ProductAddVariantDetailTracking.trackManageAllPrice(price, trackerShopId)
            if (stock.isNotEmpty())
                ProductAddVariantDetailTracking.trackManageAllStock(stock, trackerShopId)
            if (sku.isNotEmpty())
                ProductAddVariantDetailTracking.trackManageAllSku(sku, trackerShopId)
        }
    }

    private fun validatePrice() {
        val inputText = tfuPrice.getTextBigIntegerOrZero()
        val errorMessage = if (tfuPrice.getText().isNotEmpty()) {
            multipleVariantEditInputListener?.onMultipleEditInputValidatePrice(inputText).orEmpty()
        } else { "" }
        val isErrorValidating = errorMessage.isNotEmpty()

        tfuPrice?.setMessage(errorMessage)
        tfuPrice?.setError(isErrorValidating)
        updateSubmitButtonInput()
    }

    private fun validateStock() {
        val inputText = tfuStock.getTextBigIntegerOrZero()
        val errorMessage = if (tfuStock.getText().isNotEmpty()) {
            multipleVariantEditInputListener?.onMultipleEditInputValidateStock(inputText).orEmpty()
        } else { "" }
        val isErrorValidating = errorMessage.isNotEmpty()

        tfuStock?.setMessage(errorMessage)
        tfuStock?.setError(isErrorValidating)
        updateSubmitButtonInput()
    }

    private fun validateWeight() {
        val inputText = tfuWeight.getTextBigIntegerOrZero()
        val errorMessage = if (tfuWeight.getText().isNotEmpty()) {
            multipleVariantEditInputListener?.onMultipleEditInputValidateWeight(inputText).orEmpty()
        } else { "" }
        val isErrorValidating = errorMessage.isNotEmpty()

        tfuWeight?.setMessage(errorMessage)
        tfuWeight?.setError(isErrorValidating)
        updateSubmitButtonInput()
    }

    private fun updateSubmitButtonInput() {
        val isPriceError = tfuPrice?.isTextFieldError.orTrue()
        val isStockError = tfuStock?.isTextFieldError.orTrue()
        val isWeightError = tfuWeight?.isTextFieldError.orTrue()

        buttonApply?.isEnabled = !isPriceError && !isStockError && !isWeightError
    }

    private fun submitInput() {
        contentView?.apply {
            val price = tfuPrice.getText().replace(".", "")
            val stock = tfuStock.getText().replace(".", "")
            val weight = tfuWeight.getText().replace(".", "")
            val sku = tfuSku.getText()
            val inputData = MultipleVariantEditInputModel(
                price = price,
                stock = stock,
                sku = sku,
                weight = weight
            )
            multipleVariantEditInputListener?.onMultipleEditInputFinished(inputData)
        }
        dismiss()
    }

    private fun TextFieldUnify.setFieldVisibility(isVisible: Boolean, nearView: View?) {
        this.isVisible = isVisible
        nearView?.updateLayoutParams<ConstraintLayout.LayoutParams> {
            startToStart = if (isVisible) R.id.guideline else R.id.guidelineStartMargin
        }
    }
}

