package com.tokopedia.product.manage.feature.quickedit.price.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.fragment_quick_edit_price.*
import java.text.NumberFormat
import java.util.*

class ProductManageQuickEditPriceFragment : BottomSheetUnify() {

    companion object {
        fun createInstance(context: Context) : ProductManageQuickEditPriceFragment {
            return ProductManageQuickEditPriceFragment().apply{
                val view = View.inflate(context, R.layout.fragment_quick_edit_price,null)
                setChild(view)
                setTitle(context.resources.getString(R.string.product_manage_menu_set_price))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView("16234")
    }

    private fun initView(currentPrice: String) {
        context?.let {
            quick_edit_price.prependText(it.resources.getString(R.string.quick_edit_currency))
        }
        quick_edit_price.apply {
            textFieldInput.setText(formatText(currentPrice))
            setFirstIcon(com.tokopedia.unifyicon.R.drawable.ic_system_action_close_normal_24)
            setInputType(InputType.TYPE_CLASS_NUMBER)
            getFirstIcon().setOnClickListener {
                quick_edit_price.textFieldInput.text.clear()
            }
            textFieldInput.addTextChangedListener( object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    // No Op
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    //No op
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val formattedText = formatText(s as String)
                    textFieldInput.setText(formattedText)
                }
            })
        }
        quick_edit_save_button.setOnClickListener {
            super.dismiss()
        }
        quick_edit_price.requestFocus()
    }

    private fun formatText(textToFormat: String): String {
        return NumberFormat.getNumberInstance(Locale.US).format(textToFormat.toIntOrZero()).toString().replace(",",".")
    }
}