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
import com.tokopedia.product.addedit.common.util.setModeToNumberInput
import com.tokopedia.product.addedit.tracking.ProductAddVariantDetailTracking
import com.tokopedia.product.addedit.tracking.ProductEditVariantDetailTracking
import com.tokopedia.product.addedit.variant.presentation.model.MultipleVariantEditInputModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.add_edit_product_multiple_variant_edit_input_bottom_sheet_content.view.*
import java.math.BigInteger

class MultipleVariantEditInputBottomSheet(
        private var enableEditSku: Boolean,
        private var enableEditPrice: Boolean,
        private val multipleVariantEditInputListener: MultipleVariantEditInputListener
): BottomSheetUnify() {

    companion object {
        const val TAG = "Tag Multiple Variant Edit Input"
    }

    private var contentView: View? = null
    private var isPriceError = false
    private var isStockError = false
    private var trackerShopId = ""
    private var trackerIsEditMode = false

    interface MultipleVariantEditInputListener {
        fun onMultipleEditInputFinished(multipleVariantEditInputModel: MultipleVariantEditInputModel)
        fun onMultipleEditInputValidatePrice(price: BigInteger): String
        fun onMultipleEditInputValidateStock(stock: BigInteger): String
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
        val topMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
        val horizontalMargin = resources.getDimensionPixelSize(R.dimen.tooltip_close_margin)
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
        contentView?.tfuSku?.visibility = if (enableEditSku) View.VISIBLE else View.GONE
        contentView?.tfuPrice?.visibility = if (enableEditPrice) View.VISIBLE else View.GONE
        contentView?.tfuPrice.setModeToNumberInput()
        contentView?.tfuPrice?.textFieldInput?.afterTextChanged {
            if (enableEditPrice) {
                validatePrice()
                updateSubmitButtonInput()
            }
        }
        contentView?.tfuStock.setModeToNumberInput()
        contentView?.tfuStock?.textFieldInput?.afterTextChanged {
            validateStock()
            updateSubmitButtonInput()
        }
        contentView?.tfuSku?.textFieldInput?.afterTextChanged {
            updateSubmitButtonInput()
        }
        contentView?.buttonApply?.setOnClickListener {
            submitInput()
            updateSubmitButtonInput()
            sendTrackerTrackManageAllPriceData()
        }
        setChild(contentView)
    }

    private fun sendTrackerTrackManageAllPriceData() {
        val price = contentView?.tfuPrice.getTextBigIntegerOrZero().toString()
        val stock = contentView?.tfuStock.getText()
        val sku = contentView?.tfuSku.getText()

        if (trackerIsEditMode) {
            ProductEditVariantDetailTracking.trackManageAllPrice(price, trackerShopId)
            ProductEditVariantDetailTracking.trackManageAllStock(stock, trackerShopId)
            ProductEditVariantDetailTracking.trackManageAllSku(sku, trackerShopId)
        } else {
            ProductAddVariantDetailTracking.trackManageAllPrice(price, trackerShopId)
            ProductAddVariantDetailTracking.trackManageAllStock(stock, trackerShopId)
            ProductAddVariantDetailTracking.trackManageAllSku(sku, trackerShopId)
        }
    }

    private fun validatePrice() {
        if (contentView?.tfuPrice.getText().isNotEmpty()) {
            val inputText = contentView?.tfuPrice.getTextBigIntegerOrZero()
            val errorMessage = multipleVariantEditInputListener.onMultipleEditInputValidatePrice(inputText)
            val isErrorValidating = errorMessage.isNotEmpty()

            contentView?.tfuPrice?.setMessage(errorMessage)
            contentView?.tfuPrice?.setError(isErrorValidating)
            isPriceError = isErrorValidating
        } else {
            // ignore validation if field is empty
            contentView?.tfuPrice?.setMessage("")
            contentView?.tfuPrice?.setError(false)
            isPriceError = false
        }
    }

    private fun validateStock() {
        if (contentView?.tfuStock.getText().isNotEmpty()) {
            val inputText = contentView?.tfuStock.getTextBigIntegerOrZero()
            val errorMessage = multipleVariantEditInputListener.onMultipleEditInputValidateStock(inputText)
            val isErrorValidating = errorMessage.isNotEmpty()

            contentView?.tfuStock?.setMessage(errorMessage)
            contentView?.tfuStock?.setError(isErrorValidating)
            isStockError = isErrorValidating
        } else {
            // ignore validation if field is empty
            contentView?.tfuStock?.setMessage("")
            contentView?.tfuStock?.setError(false)
            isStockError = false
        }
    }

    private fun updateSubmitButtonInput() {
        val isPriceEmpty = contentView?.tfuPrice.getText().isEmpty()
        val isStockEmpty = contentView?.tfuStock.getText().isEmpty()
        val isSkuEmpty = contentView?.tfuSku.getText().isEmpty()

        contentView?.buttonApply?.isEnabled = if (isPriceEmpty && isStockEmpty && isSkuEmpty) {
            false
        } else {
            !isPriceError && !isStockError
        }
    }

    private fun submitInput() {
        if (!isPriceError && !isStockError) {
            contentView?.apply {
                val price = tfuPrice.getText().replace(".", "")
                val stock = tfuStock.getText().replace(".", "")
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