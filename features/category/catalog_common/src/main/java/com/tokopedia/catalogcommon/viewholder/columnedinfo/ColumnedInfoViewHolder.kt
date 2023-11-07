package com.tokopedia.catalogcommon.viewholder.columnedinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemColumnedInfoListItemBinding
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.utils.view.binding.viewBinding

class ColumnedInfoViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
            .inflate(R.layout.widget_item_columned_info_list_item, parent, false)
    }

    private val binding: WidgetItemColumnedInfoListItemBinding? by viewBinding()

    fun bind(item: Pair<String, String>, displayDivider: Boolean) {
        binding?.apply {
            tfTitle.text = item.first
            tfValue.text = item.second
            divItem.isVisible = displayDivider
        }
    }
}
