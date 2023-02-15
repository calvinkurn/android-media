package com.tokopedia.logisticcart.scheduledelivery.view.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.logisticcart.databinding.ViewholderChooseTimeBinding
import com.tokopedia.logisticcart.scheduledelivery.utils.DividerType
import com.tokopedia.logisticcart.scheduledelivery.utils.ScheduleSlotListener
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.ChooseTimeUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx

class ChooseTimeViewHolder(
    private val viewBinding: ViewholderChooseTimeBinding,
    private val listener: ScheduleSlotListener
) : AbstractViewHolder<ChooseTimeUiModel>(viewBinding.root) {

    override fun bind(element: ChooseTimeUiModel) {
        setView(element)
        bindValue(element)
        setState(element)
        setDivider(element.divider)
        setSelected(element)
    }

    private fun setSelected(element: ChooseTimeUiModel) {
        if (element.isSelected) {
            viewBinding.iconCheck.show()
        } else {
            viewBinding.iconCheck.gone()
        }
    }

    private fun setView(element: ChooseTimeUiModel) {
        /* set visibility */
        viewBinding.tvTitle.visibility = if (element.title.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        viewBinding.tvDescription.visibility = if (element.content.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        viewBinding.tvNote.visibility = if (element.note.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        /* set dynamic margin */
        if (element.note.isEmpty()) {
            val params = viewBinding.tvDescription.layoutParams as ViewGroup.MarginLayoutParams
            viewBinding.tvDescription.layoutParams = params.apply {
                topMargin = 22.toPx()
            }
        } else {
            val params = viewBinding.tvDescription.layoutParams as ViewGroup.MarginLayoutParams
            viewBinding.tvDescription.layoutParams = params.apply {
                topMargin = 12.toPx()
            }
        }
    }

    private fun bindValue(element: ChooseTimeUiModel) {
        viewBinding.tvTitle.text = element.title
        viewBinding.tvDescription.text =
            HtmlLinkHelper(itemView.context, element.content).spannedString
        viewBinding.tvNote.text = element.note
    }

    /** set state of view either enabled or disabled
     * if enabled, set click listener
     * otherwise, disable click
     */
    private fun setState(element: ChooseTimeUiModel) {
        if (element.isEnabled) {
            viewBinding.tvDescription.setTextColor(
                MethodChecker.getColor(
                    viewBinding.root.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950
                )
            )

            viewBinding.root.setOnClickListener {
                listener.onClickTimeListener(element)
            }
        } else {
            viewBinding.tvDescription.setTextColor(
                MethodChecker.getColor(
                    viewBinding.root.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
            viewBinding.root.setOnClickListener(null)
        }
    }

    private fun setDivider(dividerType: DividerType) {
        when (dividerType) {
            DividerType.THIN -> {
                viewBinding.divider.visibility = View.VISIBLE
                val params = viewBinding.divider.layoutParams
                params.height = DIVIDER_THIN_HEIGHT
                (params as ViewGroup.MarginLayoutParams).setMargins(
                    16.toPx(),
                    params.topMargin,
                    16.toPx(),
                    params.bottomMargin
                )
                viewBinding.divider.layoutParams = params
            }
            DividerType.THICK -> {
                viewBinding.divider.visibility = View.VISIBLE
                val params = viewBinding.divider.layoutParams
                params.height = DIVIDER_THICK_HEIGHT
                (params as ViewGroup.MarginLayoutParams).setMargins(
                    0,
                    params.topMargin,
                    0,
                    params.bottomMargin
                )
                viewBinding.divider.layoutParams = params
            }
            else -> {
                viewBinding.divider.visibility = View.GONE
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = com.tokopedia.logisticcart.R.layout.viewholder_choose_time
        private const val DIVIDER_THIN_HEIGHT = 3
        private const val DIVIDER_THICK_HEIGHT = 16
    }
}
