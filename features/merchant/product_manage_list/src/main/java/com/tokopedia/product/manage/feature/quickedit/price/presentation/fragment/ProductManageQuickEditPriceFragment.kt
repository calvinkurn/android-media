package com.tokopedia.product.manage.feature.quickedit.price.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.DialogFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.fragment_quick_edit_price.*
import java.text.NumberFormat
import java.util.*

class ProductManageQuickEditPriceFragment : BottomSheetUnify() {

    companion object {
        private const val PRODUCT_PRICE = "price"
        private const val MAX_PRICE = 100000000
        private const val MIN_PRICE = 100
        fun createInstance(context: Context, price: String) : ProductManageQuickEditPriceFragment {
            return ProductManageQuickEditPriceFragment().apply{
                val view = View.inflate(context, R.layout.fragment_quick_edit_price,null)
                setChild(view)
                setTitle(context.resources.getString(R.string.product_manage_menu_set_price))
                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                arguments =  Bundle().apply{
                    putString(PRODUCT_PRICE, price)
                }
            }
        }
    }

    var editPriceSuccess = false
    var price = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            price = it.getString(PRODUCT_PRICE) ?: ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(price)
    }

    private fun initView(currentPrice: String) {
        context?.let {
            quick_edit_price.prependText(it.resources.getString(R.string.product_manage_quick_edit_currency))
        }
        quick_edit_price.apply {
            textFieldInput.setText(formatText(currentPrice))
            setFirstIcon(com.tokopedia.unifyicon.R.drawable.ic_system_action_close_normal_24)
            setInputType(InputType.TYPE_CLASS_NUMBER)
            getFirstIcon().setOnClickListener {
                quick_edit_price.textFieldInput.text.clear()
                hideError()
            }
            textFieldInput.addTextChangedListener( object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    //No op
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    price = s.toString()
                    hideError()
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
        quick_edit_price.requestFocus()
        quick_edit_save_button.setOnClickListener {
            when {
                isPriceTooLow() -> showErrorPriceTooLow()
                isPriceTooHigh() -> showErrorPriceTooHigh()
                else -> {
                    editPriceSuccess = true
                    super.dismiss()
                }
            }
        }
    }

    private fun formatText(textToFormat: String): String {
        return NumberFormat.getNumberInstance(Locale.US).format(textToFormat.toIntOrZero()).toString().replace(",",".")
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
        quick_edit_price.setError(true)
        context?.getString(R.string.product_manage_quick_edit_min_price_error)?.let { quick_edit_price.setMessage(it) }
    }

    private fun showErrorPriceTooHigh() {
        quick_edit_price.setError(true)
        context?.getString(R.string.product_manage_quick_edit_max_price_error)?.let { quick_edit_price.setMessage(it) }
    }

    private fun hideError() {
        quick_edit_price.setError(false)
        quick_edit_price.setMessage("")
    }

}