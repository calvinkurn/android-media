package com.tokopedia.product.addedit.tooltip.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.customview.TabletAdaptiveBottomSheet
import com.tokopedia.product.addedit.tooltip.adapter.TooltipTypeFactory
import com.tokopedia.product.addedit.tooltip.model.TooltipModel

class TooltipBottomSheet : TabletAdaptiveBottomSheet() {

    private var contentView: View? = null
    private var isDividerVisible: Boolean = false
    private var listAdapter: BaseListAdapter<TooltipModel, TooltipTypeFactory>? = null
    private var urlImage: String? = null
    private var bannerTooltip: AppCompatImageView? = null
    private var rvList: RecyclerView? = null

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
        removeContainerPadding()
        setupBannerImage()
        super.onActivityCreated(savedInstanceState)
    }

    private fun setupBannerImage() {
        urlImage?.apply {
            bannerTooltip?.loadImage(this)
            bannerTooltip?.show()
        }
    }

    private fun removeContainerPadding() {
        val padding = resources.getDimensionPixelSize(R.dimen.tooltip_padding)
        val paddingTop = resources.getDimensionPixelSize(R.dimen.tooltip_padding_top)
        val closeButtonMargin = resources.getDimensionPixelSize(R.dimen.tooltip_close_margin)
        bottomSheetWrapper.setPadding(padding, paddingTop, padding, padding)
        bottomSheetClose.setMargin(closeButtonMargin, Int.ZERO, closeButtonMargin, Int.ZERO)
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.bottom_sheet_list, null)
        bannerTooltip = contentView?.findViewById(R.id.bannerTooltip)
        rvList = contentView?.findViewById(R.id.rvList)
        rvList?.apply {
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