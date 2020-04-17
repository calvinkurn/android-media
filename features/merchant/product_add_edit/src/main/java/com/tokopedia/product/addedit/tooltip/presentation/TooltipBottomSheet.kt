package com.tokopedia.product.addedit.tooltip.presentation

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.view.toDp
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tooltip.adapter.TooltipTypeFactory
import com.tokopedia.product.addedit.tooltip.model.TooltipModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_list.view.*

class TooltipBottomSheet : BottomSheetUnify() {

    private var contentView: View? = null
    private var isDividerVisible: Boolean = false
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
        changeCloseButtonSize()
        removeContainerPadding()
        addMarginCloseButton()
    }

    private fun changeCloseButtonSize() {
        val fontSize = resources.getDimension(R.dimen.fontSize_lvl5).toDp()
        bottomSheetTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
        bottomSheetClose.drawable?.apply {
            val bitmap = (this as BitmapDrawable).bitmap
            val drawableSize = resources.getDimensionPixelSize(R.dimen.tooltip_close_size)
            val scaled = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, drawableSize, drawableSize, true))
            bottomSheetClose.setImageDrawable(scaled)
        }
    }

    private fun removeContainerPadding() {
        val padding = resources.getDimensionPixelSize(R.dimen.tooltip_padding)
        val paddingTop = resources.getDimensionPixelSize(R.dimen.tooltip_padding_top)
        bottomSheetWrapper.setPadding(padding, paddingTop, padding, padding)
    }

    private fun addMarginCloseButton() {
        val topMargin = resources.getDimensionPixelSize(R.dimen.spacing_lvl3)
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
                    addItemDecoration(ImageDividerItemDecoration(
                            drawable = it,
                            drawOnLastItem = false,
                            paddingLeft = context.resources.getDimension(R.dimen.tooltip_divider_padding_left).toInt(),
                            paddingTop = context.resources.getDimension(R.dimen.layout_lvl1).toInt(),
                            paddingBottom = context.resources.getDimension(R.dimen.layout_lvl1).toInt()))
                }
            }
            layoutManager = LinearLayoutManager(context)
        }
        setChild(contentView)
    }

    fun setItemMenuList(data: List<Visitable<*>>) {
        listAdapter?.setElement(data)
    }

    fun notifyDataSetChanged() {
        listAdapter?.notifyDataSetChanged()
    }

    fun setDividerVisible(visible: Boolean) {
        isDividerVisible = visible
    }

    companion object {
        const val TAG = "Tag list"
    }
}