package com.tokopedia.sellerpersona.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.databinding.ItemQuestionOptionMultipleBinding
import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel

/**
 * Created by @ilhamsuaib on 02/02/23.
 */

class OptionMultipleViewHolder(
    private val listener: Listener,
    itemView: View
) : BaseOptionViewHolder<BaseOptionUiModel.QuestionOptionMultipleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_question_option_multiple
    }

    private val binding by lazy {
        ItemQuestionOptionMultipleBinding.bind(itemView)
    }

    override fun bind(element: BaseOptionUiModel.QuestionOptionMultipleUiModel) {
        with(binding) {
            cbSpMultipleOption.isChecked = element.isSelected
            tvSpMultipleOption.text = element.title

            cbSpMultipleOption.setOnCheckedChangeListener { _, isChecked ->
                element.isSelected = isChecked
                listener.onOptionItemSelectedListener(element)
            }
            root.setOnClickListener {
                cbSpMultipleOption.isChecked = !cbSpMultipleOption.isChecked
            }
        }
    }
}