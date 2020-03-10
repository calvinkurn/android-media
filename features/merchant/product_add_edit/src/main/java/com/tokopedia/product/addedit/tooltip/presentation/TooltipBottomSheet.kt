package com.tokopedia.product.addedit.tooltip.presentation

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tooltip.adapter.TooltipTypeFactory
import com.tokopedia.product.addedit.tooltip.model.TooltipModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_list.view.*
import kotlin.math.roundToInt

class TooltipBottomSheet: BottomSheetUnify() {

    private var contentView: View? = null
    private var isDividerVisible: Boolean = false
    private var listAdapter: BaseListAdapter<TooltipModel, TooltipTypeFactory>? = null

    init {
        listAdapter = BaseListAdapter(TooltipTypeFactory())
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
        changeCloseButtonColour()
        removeContainerPadding()
        addMarginCloseButton()
    }

    private fun changeCloseButtonColour() {
        context?.let { ctx ->
            val color = ContextCompat.getColor(ctx, R.color.Neutral_N400)
            bottomSheetClose.drawable?.apply {
                mutate()
                setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }

    private fun removeContainerPadding() {
        val paddingTop = resources.getDimension(R.dimen.tooltip_padding).toPx().roundToInt()
        val padding = resources.getDimension(R.dimen.tooltip_padding_top).toPx().roundToInt()
        bottomSheetWrapper.setPadding(padding, paddingTop, padding, padding)
    }

    private fun addMarginCloseButton() {
        val horizontalMargin =
                resources.getDimension(R.dimen.bottom_sheet_margin_close).toPx().roundToInt()
        (bottomSheetClose.layoutParams as RelativeLayout.LayoutParams).apply {
            setMargins(horizontalMargin, 0, horizontalMargin, 0)
            addRule(RelativeLayout.CENTER_VERTICAL)
        }
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.bottom_sheet_list, null)
        contentView?.rvList?.apply {
            setHasFixedSize(true)
            adapter = listAdapter
            if (isDividerVisible) addItemDecoration(DividerItemDecoration(context))
            layoutManager = LinearLayoutManager(context)
        }
        setChild(contentView)
    }

    fun setItemMenuList(data: List<Visitable<*>>) {
        listAdapter?.setElement(data)
    }

    fun notifyDataSetChanged(){
        listAdapter?.notifyDataSetChanged()
    }

    fun setDividerVisible(visible: Boolean) {
        isDividerVisible = visible
    }

    companion object {
        const val TAG = "Tag list"
    }
}