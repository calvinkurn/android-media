package com.tokopedia.logisticcart.scheduledelivery.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.logisticcart.databinding.ViewholderTitleSectionBinding
import com.tokopedia.logisticcart.scheduledelivery.utils.ScheduleSlotListener
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.TitleSectionUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.logisticcart.R as logisticcartR

class TitleSectionViewHolder(
    private val viewBinding: ViewholderTitleSectionBinding,
    private val listener: ScheduleSlotListener
) : AbstractViewHolder<TitleSectionUiModel>(viewBinding.root) {

    override fun bind(element: TitleSectionUiModel) {
        if (element.title.isNotEmpty()) {
            viewBinding.tvTitle.text = element.title
            viewBinding.tvTitle.visibility = View.VISIBLE
        } else {
            viewBinding.tvTitle.visibility = View.GONE
        }

        if (element.content.isNotEmpty()) {
            viewBinding.tvDescription.text =
                HtmlLinkHelper(viewBinding.root.context, element.content).spannedString
            viewBinding.tvDescription.visibility = View.VISIBLE
        } else {
            viewBinding.tvDescription.visibility = View.GONE
        }

        if (element.icon != NO_ICON) {
            viewBinding.iconTitle.setImage(element.icon)
            viewBinding.iconTitle.setOnClickListener {
                listener.onClickInfoListener()
            }
            viewBinding.iconTitle.visibility = View.VISIBLE
        } else {
            viewBinding.iconTitle.visibility = View.GONE
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = logisticcartR.layout.viewholder_title_section
        private const val NO_ICON = -1
    }
}
