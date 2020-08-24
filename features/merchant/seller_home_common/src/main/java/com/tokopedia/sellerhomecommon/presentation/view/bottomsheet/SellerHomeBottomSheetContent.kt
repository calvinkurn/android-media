package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.adapter.BottomSheetAdapterTypeFactory
import com.tokopedia.sellerhomecommon.presentation.model.BaseBottomSheetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BottomSheetContentUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BottomSheetListItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import kotlinx.android.synthetic.main.shc_bottom_sheet_content.view.*

/**
 * Created By @ilhamsuaib on 27/05/20
 */

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
        View.inflate(context, R.layout.shc_bottom_sheet_content, this)

        if (null == adapter)
            adapter = BaseListAdapter(BottomSheetAdapterTypeFactory())

        rvBottomSheetContent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SellerHomeBottomSheetContent.adapter
            val divider = context.getResDrawable(R.drawable.shc_tooltip_item_divider)
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

    class SellerHomeTooltipItemDivider(private val mDivider: Drawable) : RecyclerView.ItemDecoration() {

        override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val dividerLeft = parent.paddingLeft
            val dividerRight = parent.width - parent.paddingRight

            val childCount = parent.childCount
            for (i in 0..childCount - 2) {
                val child = parent.getChildAt(i)

                val params = child.layoutParams as RecyclerView.LayoutParams

                val dividerTop = child.bottom + params.bottomMargin
                val dividerBottom = dividerTop + mDivider.intrinsicHeight

                mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                mDivider.draw(canvas)
            }
        }
    }
}