package com.tokopedia.product.addedit.variant.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.getText
import com.tokopedia.product.addedit.common.util.getTextBigIntegerOrZero
import com.tokopedia.product.addedit.common.util.getTextIntOrZero
import com.tokopedia.product.addedit.common.util.setModeToNumberInput
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_PRICE_LIMIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_PRODUCT_PRICE_LIMIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.variant.presentation.model.MultipleVariantEditInputModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.add_edit_product_multiple_variant_edit_input_bottom_sheet_content.*
import kotlinx.android.synthetic.main.add_edit_product_multiple_variant_edit_input_bottom_sheet_content.view.*
import kotlinx.android.synthetic.main.add_edit_product_multiple_variant_edit_input_bottom_sheet_content.view.buttonApply
import kotlinx.android.synthetic.main.fragment_add_edit_product_description.*

class MultipleVariantEditInputBottomSheet(
        private val multipleVariantEditInputListener: MultipleVariantEditInputListener
): BottomSheetUnify() {

    companion object {
        const val TAG = "Tag Multiple Variant Edit Input"
    }

    private var contentView: View? = null
    private var isPriceError = false
    private var isStockError = false

    interface MultipleVariantEditInputListener {
        fun onMultipleEditInputFinished(multipleVariantEditInputModel: MultipleVariantEditInputModel)
    }

    init {
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addMarginCloseButton()
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }

    private fun addMarginCloseButton() {
        val topMargin = resources.getDimensionPixelSize(R.dimen.spacing_lvl3)
        val horizontalMargin = resources.getDimensionPixelSize(R.dimen.tooltip_close_margin)
        (bottomSheetClose.layoutParams as RelativeLayout.LayoutParams).apply {
            setMargins(0, topMargin, horizontalMargin, 0)
            addRule(RelativeLayout.CENTER_VERTICAL)
        }
    }

    private fun initChildLayout() {
        setTitle(getString(R.string.label_variant_multiple_input_bottom_sheet_title))
        overlayClickDismiss = false
        contentView = View.inflate(context,
                R.layout.add_edit_product_multiple_variant_edit_input_bottom_sheet_content, null)
        contentView?.tfuPrice.setModeToNumberInput()
        contentView?.tfuPrice?.textFieldInput?.afterTextChanged {
            validatePrice()
        }
        contentView?.tfuStock?.textFieldInput?.afterTextChanged {
            validateStock()
            updateSubmitButtonInput()
        }
        contentView?.buttonApply?.setOnClickListener {
            submitInput()
            updateSubmitButtonInput()
        }
        setChild(contentView)
    }

    private fun validatePrice() {
        val inputText = contentView?.tfuPrice.getTextBigIntegerOrZero()
        when {
            inputText >= MAX_PRODUCT_PRICE_LIMIT.toBigInteger() -> {
                contentView?.tfuPrice?.setMessage(getString(R.string.error_product_price_exceeding_max_limit))
                contentView?.tfuPrice?.setError(true)
            }
            inputText <= MIN_PRODUCT_PRICE_LIMIT.toBigInteger() -> {
                contentView?.tfuPrice?.setMessage(getString(R.string.error_product_price_less_than_min_limit))
                contentView?.tfuPrice?.setError(true)
            }
            else -> {
                contentView?.tfuPrice?.setError(false)
                contentView?.tfuPrice?.setMessage("")
            }
        }
        isPriceError = contentView?.tfuPrice?.isTextFieldError ?: false
    }

    private fun validateStock() {
        val inputText = contentView?.tfuStock.getTextBigIntegerOrZero()
        when {
            inputText >= MAX_PRODUCT_STOCK_LIMIT.toBigInteger() -> {
                contentView?.tfuStock?.setMessage(getString(R.string.error_available_stock_quantity_exceeding_max_limit))
                contentView?.tfuStock?.setError(true)
            }
            inputText <= MIN_PRODUCT_STOCK_LIMIT.toBigInteger() -> {
                contentView?.tfuStock?.setMessage(getString(R.string.error_minimum_stock_quantity_is_one))
                contentView?.tfuStock?.setError(true)
            }
            else -> {
                contentView?.tfuStock?.setError(false)
                contentView?.tfuStock?.setMessage("")
            }
        }
        isStockError = contentView?.tfuStock?.isTextFieldError ?: false
    }

    private fun updateSubmitButtonInput() {
        contentView?.buttonApply?.isEnabled = !isPriceError && !isStockError
    }

    private fun submitInput() {
        validatePrice()
        validateStock()
        if (!isPriceError && !isStockError) {
            contentView?.apply {
                val price = tfuPrice.getTextBigIntegerOrZero()
                val stock = tfuStock.getTextIntOrZero()
                val sku = tfuSku.getText()
                val inputData = MultipleVariantEditInputModel(
                        price = price,
                        stock = stock,
                        sku = sku
                )
                multipleVariantEditInputListener.onMultipleEditInputFinished(inputData)
            }
            dismiss()
        }
    }
}