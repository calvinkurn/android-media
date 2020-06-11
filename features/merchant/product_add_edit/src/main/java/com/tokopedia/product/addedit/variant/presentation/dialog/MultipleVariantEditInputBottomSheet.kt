package com.tokopedia.product.addedit.variant.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.tokopedia.product.addedit.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.add_edit_product_multiple_variant_edit_input_bottom_sheet_content.view.*

class MultipleVariantEditInputBottomSheet : BottomSheetUnify() {

    private var contentView: View? = null

    init {
        setCloseClickListener {
            dismiss()
        }
        setTitle("Atur sekaligus di sini")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addMarginCloseButton()
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
        contentView = View.inflate(context,
                R.layout.add_edit_product_multiple_variant_edit_input_bottom_sheet_content, null)
        contentView?.buttonApply?.setOnClickListener {
            dismiss()
        }
        setChild(contentView)
    }

    companion object {
        const val TAG = "Tag Multiple Variant Edit Input"
    }
}