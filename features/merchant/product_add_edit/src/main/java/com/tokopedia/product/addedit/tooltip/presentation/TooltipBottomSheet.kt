package com.tokopedia.product.addedit.tooltip.presentation

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
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDp
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tooltip.adapter.TooltipTypeFactory
import com.tokopedia.product.addedit.tooltip.model.TooltipModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_list.*
import kotlinx.android.synthetic.main.bottom_sheet_list.view.*

class TooltipBottomSheet : BottomSheetUnify() {

    private var contentView: View? = null
    private var isDividerVisible: Boolean = false
    private var listAdapter: BaseListAdapter<TooltipModel, TooltipTypeFactory>? = null
    private var urlImage: String? = null

    init {
        listAdapter = BaseListAdapter(TooltipTypeFactory())
        isKeyboardOverlap = false
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
        changeCloseButtonSize()
        removeContainerPadding()
        addMarginCloseButton()
        setupBannerImage()
    }

    private fun setupBannerImage() {
        urlImage?.apply {
            bannerTooltip.loadImage(this)
            bannerTooltip.show()
        }
    }

    private fun changeCloseButtonSize() {
        val fontSize = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.fontSize_lvl5).toDp()
        bottomSheetTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
        context?.also { context ->
            bottomSheetClose.apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bottomsheet_close))
                layoutParams.apply {
                    width = context.resources.getDimension(com.tokopedia.product.addedit.R.dimen.tooltip_close_size).toInt()
                    height = context.resources.getDimension(com.tokopedia.product.addedit.R.dimen.tooltip_close_size).toInt()
                }
            }
        }
    }

    private fun removeContainerPadding() {
        val padding = resources.getDimensionPixelSize(com.tokopedia.product.addedit.R.dimen.tooltip_padding)
        val paddingTop = resources.getDimensionPixelSize(com.tokopedia.product.addedit.R.dimen.tooltip_padding_top)
        bottomSheetWrapper.setPadding(padding, paddingTop, padding, padding)
    }

    private fun addMarginCloseButton() {
        val topMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
        val horizontalMargin = resources.getDimensionPixelSize(com.tokopedia.product.addedit.R.dimen.tooltip_close_margin)
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
                            paddingLeft = context.resources.getDimension(com.tokopedia.product.addedit.R.dimen.tooltip_divider_padding_left).toInt(),
                            paddingTop = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1).toInt(),
                            paddingBottom = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1).toInt()))
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

    fun setBannerImage(url: String) {
        urlImage = url
    }

}