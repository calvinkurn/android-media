package com.tokopedia.product.addedit.optionpicker

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.view.toDp
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.optionpicker.adapter.OptionTypeFactory
import com.tokopedia.product.addedit.optionpicker.model.OptionModel
import com.tokopedia.product.addedit.tooltip.presentation.TooltipDividerItemDecoration
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_list.view.*

class OptionPicker: BottomSheetUnify(), OptionTypeFactory.OnItemClickListener {
    private var selectedPosition: Int = -1
    private var listener: ((selectedText: String, selectedPosition: Int) -> Unit)? = null
    private var contentView: View? = null
    private var isDividerVisible: Boolean = false
    private var listAdapter: BaseListAdapter<OptionModel, OptionTypeFactory>? = null

    init {
        val optionTypeFactory = OptionTypeFactory()
        optionTypeFactory.setOnItemClickListener(this)
        listAdapter = BaseListAdapter(optionTypeFactory)
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onClick(selectedText: String, selectedPosition: Int) {
        dismiss()
        listener?.invoke(selectedText, selectedPosition)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        changeCloseButtonSize()
        removeContainerPadding()
        addMarginCloseButton()
    }

    private fun changeCloseButtonSize() {
        val fontSize = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.fontSize_lvl5).toDp()
        bottomSheetTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
        context?.also { context ->
            bottomSheetClose.apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bottomsheet_close))
                layoutParams.apply {
                    width = context.resources.getDimension(R.dimen.tooltip_close_size).toInt()
                    height = context.resources.getDimension(R.dimen.tooltip_close_size).toInt()
                }
            }
        }
    }

    private fun removeContainerPadding() {
        val padding = resources.getDimensionPixelSize(R.dimen.tooltip_padding)
        val paddingTop = resources.getDimensionPixelSize(R.dimen.tooltip_padding_top)
        bottomSheetWrapper.setPadding(padding, paddingTop, padding, padding)
    }

    private fun addMarginCloseButton() {
        val topMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
        val horizontalMargin = resources.getDimensionPixelSize(R.dimen.tooltip_close_margin)
        (bottomSheetClose.layoutParams as RelativeLayout.LayoutParams).apply {
            setMargins(horizontalMargin, topMargin, horizontalMargin, 0)
            addRule(RelativeLayout.CENTER_VERTICAL)
        }
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.bottom_sheet_list, null)
        contentView?.rvList?.apply {
            setHasFixedSize(true)
            adapter = listAdapter
            if (isDividerVisible) {
                ContextCompat.getDrawable(context, R.drawable.tooltip_divider)?.also {
                    addItemDecoration(TooltipDividerItemDecoration(
                            drawable = it,
                            drawOnLastItem = false,
                            paddingLeft = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2).toInt(),
                            paddingTop = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).toInt(),
                            paddingBottom = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).toInt()))
                }
            }
            layoutManager = LinearLayoutManager(context)
        }
        setChild(contentView)
    }

    fun setItemMenuList(data: List<String>) {
        data.forEachIndexed { index, it ->
            listAdapter?.addElement(OptionModel(it, index == selectedPosition))
        }
    }

    fun setSelectedPosition(selectedPosition: Int){
        this.selectedPosition = selectedPosition
    }

    fun setOnItemClickListener(listener: (selectedText: String, selectedPosition: Int) -> Unit){
        this.listener = listener
    }

    fun notifyDataSetChanged(){
        listAdapter?.notifyDataSetChanged()
    }

    fun setDividerVisible(visible: Boolean) {
        isDividerVisible = visible
    }
}