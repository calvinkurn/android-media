package com.tokopedia.catalogcommon.viewholder.columnedinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemColumnedInfoBottomsheetItemBinding
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.utils.view.binding.viewBinding

class ColumnedInfoBottomSheetViewHolder(
    itemView: View,
    private val showAccordion: Boolean
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val ROTATED_DEGREE = 180f
        private const val NONROTATED_DEGREE = 0f

        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
            .inflate(R.layout.widget_item_columned_info_bottomsheet_item, parent, false)
    }

    private val binding: WidgetItemColumnedInfoBottomsheetItemBinding? by viewBinding()
    private var isExtended = true

    fun bind(item: ColumnedInfoUiModel.ColumnData) {
        binding?.apply {
            headerColumnInfo.isVisible = showAccordion
            tfTitle.text = item.title
            icChevron.setOnClickListener {
                isExtended = !isExtended
                updateChevronIcon()
                rvColumnedInfo.isVisible = isExtended
            }
            rvColumnedInfo.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            rvColumnedInfo.adapter = ColumnedInfoListItemAdapter(item)
        }
    }

    private fun updateChevronIcon() {
        binding?.icChevron?.rotation = if (isExtended) NONROTATED_DEGREE else ROTATED_DEGREE
    }
}
