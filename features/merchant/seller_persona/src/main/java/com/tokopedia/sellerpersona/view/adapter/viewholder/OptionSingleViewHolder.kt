package com.tokopedia.sellerpersona.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.databinding.ItemQuestionOptionSingleBinding
import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel

/**
 * Created by @ilhamsuaib on 02/02/23.
 */

class OptionSingleViewHolder(
    private val listener: Listener,
    itemView: View
) : BaseOptionViewHolder<BaseOptionUiModel.QuestionOptionSingleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_question_option_single

        private const val OPTION_VALUE_FORMAT = "%s."
    }

    private val binding by lazy {
        ItemQuestionOptionSingleBinding.bind(itemView)
    }

    override fun bind(element: BaseOptionUiModel.QuestionOptionSingleUiModel) {
        with(binding) {
            tvSpOptionValue.text = String.format(OPTION_VALUE_FORMAT, element.value)
            tvSpOptionTitle.text = element.title

            setBackground(element.isSelected)
            root.setOnClickListener {
                val isSelected = element.isSelected
                element.isSelected = !isSelected
                setBackground(!isSelected)
                listener.onOptionItemSelectedListener(element)
            }
        }
    }

    private fun setBackground(isSelected: Boolean) {
        val background = if (isSelected) {
            R.drawable.sp_bg_question_active
        } else {
            R.drawable.sp_bg_question_inactive
        }
        binding.containerSpOption.setBackgroundResource(background)
    }
}