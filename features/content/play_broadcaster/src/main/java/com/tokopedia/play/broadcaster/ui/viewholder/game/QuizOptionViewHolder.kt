package com.tokopedia.play.broadcaster.ui.viewholder.game

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ItemQuizOptionListBinding
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel

/**
 * Created By : Jonathan Darwin on April 04, 2022
 */
class QuizOptionViewHolder private constructor(
    private val binding: ItemQuizOptionListBinding,
    private val listener: Listener,
) : BaseViewHolder(binding.root) {

    fun bind(item: QuizFormDataUiModel.Option) {
        binding.root.apply {
            order = item.order
            text = item.text
            textChoice = item.getTextChoice()
            textHint = if(item.isMandatory) getString(R.string.play_bro_quiz_hint_text, item.order + 1)
                        else getString(R.string.play_bro_quiz_hint_add_new_option)
            maxLength = item.maxLength
            isCorrect = item.isSelected
            isEditable = item.isEditable

            setOnCheckedListener { listener.onOptionChecked(item.order) }
            setOnTextChanged { order, text -> listener.onTextChanged(order, text) }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener,
        ) = QuizOptionViewHolder(
            ItemQuizOptionListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener,
        )
    }

    interface Listener {
        fun onOptionChecked(order: Int)
        fun onTextChanged(order: Int, text: String)
    }
}