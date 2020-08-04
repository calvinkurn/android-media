package com.tokopedia.product.addedit.variant.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.adapter.SelectVariantMainAdapter
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.add_edit_product_select_variant_main_bottom_sheet_content.view.*

class SelectVariantMainBottomSheet(
        private val listener: SelectVariantMainListener
): BottomSheetUnify() {

    companion object {
        const val TAG = "Tag Select Variant Main"
    }

    private var contentView: View? = null
    private var selectAdapter: SelectVariantMainAdapter? = null

    interface SelectVariantMainListener {
        fun onSelectVariantMainFinished(combination: List<Int>)
    }

    init {
        selectAdapter = SelectVariantMainAdapter()
        setBehaviorAsKnob()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        removeContainerPadding()
        addMarginTitle()
    }

    fun setData(items: VariantInputModel?) {
        items?.run {
            selectAdapter?.setData(this)
        }
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }

    private fun setBehaviorAsKnob() {
        showCloseIcon = false
        showKnob = true
    }

    private fun removeContainerPadding() {
        val padding = resources.getDimensionPixelSize(com.tokopedia.product.addedit.R.dimen.tooltip_padding)
        val paddingTop = resources.getDimensionPixelSize(com.tokopedia.product.addedit.R.dimen.tooltip_close_margin)
        bottomSheetWrapper.setPadding(padding, paddingTop, padding, padding)
    }

    private fun addMarginTitle() {
        val topMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
        val horizontalMargin = resources.getDimensionPixelSize(com.tokopedia.product.addedit.R.dimen.tooltip_close_margin)
        (bottomSheetTitle.layoutParams as RelativeLayout.LayoutParams).apply {
            setMargins(horizontalMargin, topMargin, horizontalMargin, 0)
            addRule(RelativeLayout.CENTER_VERTICAL)
        }
    }

    private fun initChildLayout() {
        setTitle(getString(R.string.label_variant_main_select_bottom_sheet_title))
        contentView = View.inflate(context,
                R.layout.add_edit_product_select_variant_main_bottom_sheet_content, null)
        contentView?.recyclerViewVariantMain?.apply {
            setHasFixedSize(true)
            adapter = selectAdapter
            layoutManager = LinearLayoutManager(context)
        }
        contentView?.buttonSave?.setOnClickListener {
            dismiss()
            listener.onSelectVariantMainFinished(selectAdapter?.getSelectedData().orEmpty())
        }
        setChild(contentView)
    }
}