package com.tokopedia.product.addedit.detail.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifyprinciples.Typography

class TitleCorrectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    enum class Mode {
        NORMAL, WARNING, ERROR
    }

    private var tvTitle: Typography = itemView.findViewById(R.id.tv_title)
    private var viewUnderline: DividerUnify = itemView.findViewById(R.id.view_underline)

    fun bindData(productName: String, mode: Mode) {
        val solidRedColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_R600)
        val redColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_R600_20)
        val yellowColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Y300_20)
        val blackColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
        tvTitle.text = productName
        viewUnderline.show()

        when (mode) {
            Mode.WARNING -> {
                tvTitle.setWeight(Typography.BOLD)
                tvTitle.setTextColor(blackColor)
                viewUnderline.setBackgroundColor(yellowColor)
            }
            Mode.ERROR -> {
                tvTitle.setWeight(Typography.BOLD)
                tvTitle.setTextColor(solidRedColor)
                viewUnderline.setBackgroundColor(redColor)
            }
            else -> {
                tvTitle.setWeight(Typography.REGULAR)
                tvTitle.setTextColor(blackColor)
                viewUnderline.gone()
            }
        }
    }
}