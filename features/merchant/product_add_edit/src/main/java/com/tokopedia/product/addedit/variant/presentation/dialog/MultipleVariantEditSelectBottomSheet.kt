package com.tokopedia.product.addedit.variant.presentation.dialog

import android.os.Bundle
import android.util.LayoutDirection
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tooltip.adapter.TooltipTypeFactory
import com.tokopedia.product.addedit.tooltip.model.TooltipModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.add_edit_product_multiple_variant_edit_input_bottom_sheet_content.view.*

class MultipleVariantEditSelectBottomSheet : BottomSheetUnify() {

    private var contentView: View? = null
    private var listAdapter: BaseListAdapter<TooltipModel, TooltipTypeFactory>? = null

    init {
        listAdapter = BaseListAdapter(TooltipTypeFactory())
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        removeContainerPadding()
    }

    private fun removeContainerPadding() {
        val padding = resources.getDimensionPixelSize(R.dimen.tooltip_padding)
        val paddingTop = resources.getDimensionPixelSize(R.dimen.tooltip_padding_top)
        bottomSheetWrapper.setPadding(padding, paddingTop, padding, padding)
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.add_edit_product_multiple_variant_edit_input_bottom_sheet_content, null)
        contentView?.recyclerViewVariantCheck?.apply {
            setHasFixedSize(true)
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
        }
        contentView?.checkboxSelectAll?.layoutDirection = LayoutDirection.RTL
        setChild(contentView)
    }

    fun notifyDataSetChanged() {
        listAdapter?.notifyDataSetChanged()
    }

    companion object {
        const val TAG = "Tag list"
    }
}