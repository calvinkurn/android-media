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
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.product.addedit.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.bottom_sheet_list.view.*

class TooltipBottomSheet <T, F : AdapterTypeFactory> (typeFactory: F) : BottomSheetUnify() {

    private var contentView: View? = null
    private var isDividerVisible: Boolean = false
    private var listAdapter: BaseListAdapter<T, F>? = null

    init {
        listAdapter = BaseListAdapter(typeFactory)
        setFullPage(false)
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
        bottomSheetWrapper.setPadding(0, 24.toPx(), 0, 0)
    }

    private fun addMarginCloseButton() {
        val horizontalMargin = 16.toPx()
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