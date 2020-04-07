package com.tokopedia.sellerhome.view.bottomsheet.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.bottomsheet.adapter.BottomSheetAdapterTypeFactory
import com.tokopedia.sellerhome.view.bottomsheet.model.BaseBottomSheetUiModel
import com.tokopedia.sellerhome.view.bottomsheet.model.BottomSheetContentUiModel
import com.tokopedia.sellerhome.view.bottomsheet.model.BottomSheetListItemUiModel
import com.tokopedia.sellerhome.view.model.TooltipUiModel
import kotlinx.android.synthetic.main.sah_bottom_sheet_content.view.*

class SellerHomeBottomSheetContent : LinearLayout {

    private var adapter: BaseListAdapter<BaseBottomSheetUiModel, BottomSheetAdapterTypeFactory>? = null

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

        if (null == adapter)
            adapter = BaseListAdapter(BottomSheetAdapterTypeFactory())

        rvBottomSheetContent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SellerHomeBottomSheetContent.adapter
            val divider = context.getResDrawable(R.drawable.sah_tooltip_item_divider)
            addItemDecoration(SellerHomeTooltipItemDivider(divider ?: return))
        }
    }

    fun setTooltipData(tooltip: TooltipUiModel) {
        with(tooltip) {

            adapter?.data?.clear()

            if (content.isNotEmpty()) {
                adapter?.data?.add(BottomSheetContentUiModel(content))
            }

            if (!list.isNullOrEmpty()) {
                adapter?.data?.addAll(list.map { item -> BottomSheetListItemUiModel(item.title, item.description) })
            }
        }

        rvBottomSheetContent.post {
            adapter?.notifyDataSetChanged()
        }
    }
}