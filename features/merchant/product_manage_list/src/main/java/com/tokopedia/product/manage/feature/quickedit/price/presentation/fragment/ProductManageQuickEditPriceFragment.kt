package com.tokopedia.product.manage.feature.quickedit.price.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.utils.ProductManageTracking
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.CurrencyIdrTextWatcher
import kotlinx.android.synthetic.main.fragment_quick_edit_price.*

class ProductManageQuickEditPriceFragment : BottomSheetUnify() {

    companion object {
        private const val PRODUCT_ID = "id"
        private const val PRODUCT_PRICE = "price"
        private const val MAX_PRICE = 100000000
        private const val MIN_PRICE = 100
        private const val MAXIMUM_STRING_LENGTH = 11
        fun createInstance(context: Context, price: String, id: String) : ProductManageQuickEditPriceFragment {
            return ProductManageQuickEditPriceFragment().apply{
                val view = View.inflate(context, R.layout.fragment_quick_edit_price,null)
                setChild(view)
                setTitle(context.resources.getString(R.string.product_manage_menu_set_price))
                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                arguments =  Bundle().apply{
                    putString(PRODUCT_ID, id)
                    putString(PRODUCT_PRICE, price)
                }
            }
        }
    }

    var editPriceSuccess = false
    var id = ""
    var price = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            price = it.getString(PRODUCT_PRICE) ?: ""
            id = it.getString(PRODUCT_ID) ?: ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(price)
    }

    private fun initView(currentPrice: String) {
        context?.let {
            quickEditPriceTextField.prependText(it.resources.getString(R.string.product_manage_quick_edit_currency))
        }
        quickEditPriceTextField.apply {
            textFieldInput.filters = arrayOf(InputFilter.LengthFilter(MAXIMUM_STRING_LENGTH))
            textFieldInput.setText(CurrencyFormatHelper.removeCurrencyPrefix(CurrencyFormatHelper.convertToRupiah(currentPrice)))
            setFirstIcon(com.tokopedia.unifyicon.R.drawable.ic_system_action_close_normal_24)
            setInputType(InputType.TYPE_CLASS_NUMBER)
            getFirstIcon().setOnClickListener {
                quickEditPriceTextField.textFieldInput.text.clear()
                hideError()
            }
            textFieldInput.setOnEditorActionListener { _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    price = textFieldInput.text.toString()
                    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(textFieldInput.windowToken, 0)
                }
                true
            }
            val idrTextWatcher = CurrencyIdrTextWatcher(this.textFieldInput)
            textFieldInput.addTextChangedListener(idrTextWatcher)
            setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    activity.let {
                        KeyboardHandler.showSoftKeyboard(it)
                    }
                } else {
                    activity.let {
                        KeyboardHandler.hideSoftKeyboard(it)
                    }
                }
            }
        }
        quickEditPriceTextField.requestFocus()
        quickEditPriceSaveButton.setOnClickListener {
            isPriceValid()
            ProductManageTracking.eventEditPriceSave(id)
        }
    }

    private fun isPriceTooLow(): Boolean {
        if(price.toIntOrZero() < MIN_PRICE) return true
        return false
    }

    private fun isPriceTooHigh(): Boolean {
        if(price.toIntOrZero() > MAX_PRICE) return true
        return false
    }

    private fun showErrorPriceTooLow() {
        quickEditPriceTextField.setError(true)
        context?.getString(R.string.product_manage_quick_edit_min_price_error)?.let { quickEditPriceTextField.setMessage(it) }
    }

    private fun showErrorPriceTooHigh() {
        quickEditPriceTextField.setError(true)
        context?.getString(R.string.product_manage_quick_edit_max_price_error)?.let { quickEditPriceTextField.setMessage(it) }
    }

    private fun hideError() {
        quickEditPriceTextField.setError(false)
        quickEditPriceTextField.setMessage("")
    }

    private fun isPriceValid() {
        price = CurrencyFormatHelper.convertRupiahToInt(quickEditPriceTextField.textFieldInput.text.toString()).toString()
        when {
            isPriceTooLow() -> {
                showErrorPriceTooLow()
                return
            }
            isPriceTooHigh() -> {
                showErrorPriceTooHigh()
                return
            }
            else -> {
                editPriceSuccess = true
                super.dismiss()
            }
        }
    }

}