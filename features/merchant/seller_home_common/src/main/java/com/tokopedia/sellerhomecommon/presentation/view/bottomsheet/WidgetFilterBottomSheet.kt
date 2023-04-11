package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcWidgetFilterBottomSheetBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetFilterAdapter
import com.tokopedia.sellerhomecommon.presentation.model.WidgetFilterUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

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
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.ShcDialogStyle)
    }

    @SuppressLint("NotifyDataSetChanged")
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
        val childBinding = ShcWidgetFilterBottomSheetBinding.inflate(
            LayoutInflater.from(context), null, false
        )
        with(childBinding) {
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
        setChild(childBinding.root)
        setShowListener {
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }

        return this
    }
}
