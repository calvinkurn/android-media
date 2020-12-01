package com.tokopedia.product.addedit.variant.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.getText
import com.tokopedia.product.addedit.common.util.getTextBigIntegerOrZero
import com.tokopedia.product.addedit.common.util.setModeToNumberInput
import com.tokopedia.product.addedit.tracking.ProductAddVariantDetailTracking
import com.tokopedia.product.addedit.tracking.ProductEditVariantDetailTracking
import com.tokopedia.product.addedit.variant.presentation.model.MultipleVariantEditInputModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.add_edit_product_multiple_variant_edit_input_bottom_sheet_content.view.*
import java.math.BigInteger

class MultipleVariantEditInputBottomSheet(
        private var enableEditSku: Boolean = false,
        private var enableEditPrice: Boolean = false,
        private val multipleVariantEditInputListener: MultipleVariantEditInputListener? = null
): BottomSheetUnify() {

    companion object {
        const val TAG = "Tag Multiple Variant Edit Input"
    }

    private var contentView: View? = null
    private var isPriceError = false
    private var isStockError = false
    private var trackerShopId = ""
    private var trackerIsEditMode = false

    private var multiLocationTicker: Ticker? = null

    // TODO: Change these dummy values to user session values
    private val isShopOwner = false
    private val isShopAdmin = true
    private val isMultiLocation = false
    private val canManageProduct = true
    private val canManageStock = false

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

        contentView?.findViewById<Ticker>(R.id.ticker_multiple_variant_multi_location)?.run {
            val couldShowTicker = (isShopOwner || isShopAdmin) && isMultiLocation && canManageProduct
            if (couldShowTicker) {
                if (canManageStock) {
                    setTextDescription(context?.getString(R.string.ticker_edit_variant_main_location).orEmpty())
                } else {
                    setTextDescription(context?.getString(R.string.ticker_edit_variant_cant_edit_stock).orEmpty())
                }
                show()
            } else {
                hide()
            }
        }

        contentView?.tfuSku?.visibility = if (enableEditSku) View.VISIBLE else View.GONE
        contentView?.tfuPrice?.visibility = if (enableEditPrice) View.VISIBLE else View.GONE
        contentView?.tfuPrice.setModeToNumberInput()
        contentView?.tfuPrice?.textFieldInput?.afterTextChanged {
            if (enableEditPrice) {
                validatePrice()
                updateSubmitButtonInput()
            }
        }
        if (canManageStock || isShopOwner) {
            contentView?.tfuStock.setModeToNumberInput()
            contentView?.tfuStock?.textFieldInput?.afterTextChanged {
                validateStock()
                updateSubmitButtonInput()
            }
        } else {
            contentView?.tfuStock?.gone()
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
        val price = contentView?.tfuPrice.getText()
        val stock = contentView?.tfuStock.getText()
        val sku = contentView?.tfuSku.getText()

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
        if (contentView?.tfuPrice.getText().isNotEmpty()) {
            val inputText = contentView?.tfuPrice.getTextBigIntegerOrZero()
            val errorMessage = multipleVariantEditInputListener?.onMultipleEditInputValidatePrice(inputText).orEmpty()
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
            val errorMessage = multipleVariantEditInputListener?.onMultipleEditInputValidateStock(inputText).orEmpty()
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
                multipleVariantEditInputListener?.onMultipleEditInputFinished(inputData)
            }
            dismiss()
        }
    }
}