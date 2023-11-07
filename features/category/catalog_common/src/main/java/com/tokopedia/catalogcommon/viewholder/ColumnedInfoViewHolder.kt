package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemColumnedInfoBinding
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel
import com.tokopedia.catalogcommon.viewholder.columnedinfo.ColumnedInfoAdapter
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.utils.view.binding.viewBinding


class ColumnedInfoViewHolder(itemView: View) : AbstractViewHolder<ColumnedInfoUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_columned_info
    }

    private val binding by viewBinding<WidgetItemColumnedInfoBinding>()

    override fun bind(element: ColumnedInfoUiModel) {
        binding?.tfTitle?.text = element.sectionTitle
        binding?.rvColumnInfo?.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        binding?.rvColumnInfo?.adapter = ColumnedInfoAdapter(element.widgetContent)
        binding?.btnSeeMore?.isVisible = element.hasMoreData
    }
}
