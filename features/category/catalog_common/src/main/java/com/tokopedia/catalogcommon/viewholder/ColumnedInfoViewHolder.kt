package com.tokopedia.catalogcommon.viewholder

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemColumnedInfoBinding
import com.tokopedia.catalogcommon.listener.ColumnedInfoListener
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel
import com.tokopedia.catalogcommon.viewholder.columnedinfo.ColumnedInfoListItemAdapter
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.utils.view.binding.viewBinding

class ColumnedInfoViewHolder(
    itemView: View,
    private val columnedInfoListener: ColumnedInfoListener? = null
) : AbstractViewHolder<ColumnedInfoUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_columned_info
    }

    private val binding by viewBinding<WidgetItemColumnedInfoBinding>()

    override fun bind(element: ColumnedInfoUiModel) {
        binding?.apply {
            tfTitle.text = element.sectionTitle
            rvColumnInfo.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            rvColumnInfo.adapter = ColumnedInfoListItemAdapter(element.widgetContent)
            btnSeeMore.isVisible = element.hasMoreData
            btnSeeMore.setOnClickListener {
                columnedInfoListener?.onColumnedInfoSeeMoreClicked(element.sectionTitle, element.fullContent)
            }
            tfTitle.setTextColor(element.widgetTextColor ?: Color.BLACK)
            if (element.darkMode) {
                btnSeeMore.applyColorMode(ColorMode.DARK_MODE)
            } else {
                btnSeeMore.applyColorMode(ColorMode.LIGHT_MODE)
            }
        }
        columnedInfoListener?.onColumnedInfoImpression(element)
    }
}
