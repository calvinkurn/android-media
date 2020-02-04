package com.tokopedia.sellerhome.view.bottomsheet.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.bottomsheet.adapter.BottomSheetAdapterTypeFactory
import com.tokopedia.sellerhome.view.bottomsheet.model.BaseBottomSheetUiModel
import com.tokopedia.sellerhome.view.bottomsheet.model.BottomSheetContentUiModel
import com.tokopedia.sellerhome.view.bottomsheet.model.BottomSheetListItemUiModel
import com.tokopedia.sellerhome.view.model.TooltipUiModel
import kotlinx.android.synthetic.main.sah_bottom_sheet_content.view.*

class SellerHomeBottomSheetContent : LinearLayout {

    private var tooltip: TooltipUiModel? = null
    private lateinit var adapter: BaseListAdapter<BaseBottomSheetUiModel, BottomSheetAdapterTypeFactory>

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.sah_bottom_sheet_content, this)

        adapter = BaseListAdapter(BottomSheetAdapterTypeFactory())

        rv_bottom_sheet_content.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SellerHomeBottomSheetContent.adapter
            val divider = ContextCompat.getDrawable(context, R.drawable.sah_tooltip_item_divider)
            divider?.apply {
                addItemDecoration(SellerHomeTooltipItemDivider(this))
            }
        }
    }

    fun setTooltipData(tooltip: TooltipUiModel) {
        this.tooltip = tooltip
        with(tooltip) {
            val items = mutableListOf<BaseBottomSheetUiModel>()

            if (content.isNotEmpty()) {
                items.add(BottomSheetContentUiModel(content))
            }

            if (!list.isNullOrEmpty()) {
                items.addAll(list.map { item -> BottomSheetListItemUiModel(item.title, item.description) })
            }

            adapter.data.clear()
            adapter.data.addAll(items)

            adapter.notifyDataSetChanged()
        }
    }
}