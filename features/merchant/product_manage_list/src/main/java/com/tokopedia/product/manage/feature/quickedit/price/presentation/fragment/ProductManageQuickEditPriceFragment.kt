package com.tokopedia.product.manage.feature.quickedit.price.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.product.manage.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.feature.quickedit.common.constant.EditProductConstant.MAXIMUM_PRICE_LENGTH
import com.tokopedia.product.manage.feature.quickedit.common.constant.EditProductConstant.MINIMUM_PRICE
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.CurrencyIdrTextWatcher
import kotlinx.android.synthetic.main.fragment_quick_edit_price.*

class ProductManageQuickEditPriceFragment(private val onFinishedListener: OnFinishedListener,
                                          private var product: ProductViewModel) : BottomSheetUnify() {

    companion object {
        fun createInstance(context: Context, product: ProductViewModel, onFinishedListener: OnFinishedListener) : ProductManageQuickEditPriceFragment {
            return ProductManageQuickEditPriceFragment(onFinishedListener, product).apply{
                val view = View.inflate(context, R.layout.fragment_quick_edit_price,null)
                setChild(view)
                setTitle(context.resources.getString(R.string.product_manage_menu_set_price))
                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        product.price?.let { initView(it) }
    }

    private fun initView(currentPrice: String) {
        context?.let {
            quickEditPriceTextField.prependText(it.resources.getString(R.string.product_manage_quick_edit_currency))
        }
        quickEditPriceTextField.apply {
            textFieldInput.filters = arrayOf(InputFilter.LengthFilter(MAXIMUM_PRICE_LENGTH))
            textFieldInput.setText(CurrencyFormatHelper.removeCurrencyPrefix(CurrencyFormatHelper.convertToRupiah(currentPrice)))
            setFirstIcon(com.tokopedia.unifyicon.R.drawable.ic_system_action_close_normal_24)
            setInputType(InputType.TYPE_CLASS_NUMBER)
            getFirstIcon().setOnClickListener {
                quickEditPriceTextField.textFieldInput.text.clear()
            }
            textFieldInput.setOnEditorActionListener { _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    product = product.copy(price = textFieldInput.text.toString())
                    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(textFieldInput.windowToken, 0)
                }
                true
            }
            val idrTextWatcher = CurrencyIdrTextWatcher(this.textFieldInput)
            textFieldInput.addTextChangedListener(idrTextWatcher)
            textFieldInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val input = textFieldInput.text.toString()
                    val price = CurrencyFormatHelper.convertRupiahToInt(input)

                    if(price < MINIMUM_PRICE) {
                        showErrorPriceTooLow()
                    } else {
                        hideError()
                    }
                }

            })
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
            ProductManageTracking.eventEditPriceSave(product.id)
        }
    }

    private fun isPriceTooLow(): Boolean {
        if(product.price.toIntOrZero() < MINIMUM_PRICE) return true
        return false
    }

    private fun showErrorPriceTooLow() {
        quickEditPriceTextField.setError(true)
        context?.getString(R.string.product_manage_quick_edit_min_price_error)?.let { quickEditPriceTextField.setMessage(it) }
    }

    private fun hideError() {
        quickEditPriceTextField.setError(false)
        quickEditPriceTextField.setMessage("")
    }

    private fun isPriceValid() {
        product = product.copy(price = CurrencyFormatHelper.convertRupiahToInt(quickEditPriceTextField.textFieldInput.text.toString()).toString())
        when {
            isPriceTooLow() -> {
                showErrorPriceTooLow()
                return
            }
            else -> {
                onFinishedListener.onFinishEditPrice(product)
                super.dismiss()
            }
        }
    }

    interface OnFinishedListener {
        fun onFinishEditPrice(product: ProductViewModel)
    }

}