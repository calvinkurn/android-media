package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetFilterAdapter
import com.tokopedia.sellerhomecommon.presentation.model.WidgetFilterUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.shc_widget_filter_bottom_sheet.view.*

/**
 * Created By @ilhamsuaib on 06/11/20
 */

class WidgetFilterBottomSheet : BottomSheetUnify(), WidgetFilterAdapter.Listener {

    companion object {
        const val POST_FILTER_TAG = "PostFilterBottomSheet"
        const val TABLE_FILTER_TAG = "TableFilterBottomSheet"
        fun newInstance(): WidgetFilterBottomSheet {
            return WidgetFilterBottomSheet().apply {
                showCloseIcon = false
                isDragable = true
                showKnob = true
                isHideable = true
            }
        }
    }

    private var widgetFilterAdapter: WidgetFilterAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onItemClick(item: WidgetFilterUiModel) {
        widgetFilterAdapter?.getItems()?.forEach {
            it.isSelected = it == item
        }
        widgetFilterAdapter?.notifyDataSetChanged()
    }

    fun init(
            context: Context,
            @StringRes titleRes: Int,
            widgetFilters: List<WidgetFilterUiModel>,
            onItemClick: (WidgetFilterUiModel) -> Unit
    ): WidgetFilterBottomSheet {
        if (widgetFilterAdapter == null) {
            widgetFilterAdapter = WidgetFilterAdapter(widgetFilters, this)
        }

        setTitle(context.getString(titleRes))
        val inflater = LayoutInflater.from(context)
        val child = inflater.inflate(R.layout.shc_widget_filter_bottom_sheet, LinearLayout(context), false)
        with(child) {
            rvShcWidgetFilter.layoutManager = LinearLayoutManager(context)
            rvShcWidgetFilter.adapter = widgetFilterAdapter
            btnShcWidgetFilterApply.setOnClickListener {
                val selected = widgetFilters.find { it.isSelected }
                selected?.let {
                    onItemClick(it)
                }
                dismiss()
            }
        }
        setChild(child)
        setShowListener {
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }

        return this
    }

}