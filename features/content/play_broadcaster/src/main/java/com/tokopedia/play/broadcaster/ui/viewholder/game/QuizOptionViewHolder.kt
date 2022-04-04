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
            text = item.text
            textChoice = item.getTextChoice()
            textHint = getString(R.string.play_bro_quiz_hint_text, item.order + 1)
            maxLength = item.maxLength
            isCorrect = item.isSelected
            isEditable = item.isEditable

            /** TODO: Set maxLength */

            setOnClickListener { listener.onOptionChecked(item.order) }
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
    }
}