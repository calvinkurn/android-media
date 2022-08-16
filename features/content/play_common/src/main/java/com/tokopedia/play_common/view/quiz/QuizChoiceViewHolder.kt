package com.tokopedia.play_common.view.quiz

import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.databinding.ItemQuizOptionBinding

/**
 * @author by astidhiyaa on 08/04/22
 */
open class QuizChoiceViewHolder(
    binding: ItemQuizOptionBinding,
    private val listener: Listener
) : BaseViewHolder(binding.root) {

    private val widget = binding.quizOption

    fun bind(item: QuizChoicesUiModel){
        widget.setupView(item)
        widget.setupLoading(isLoading = item.isLoading)
        widget.setListener(object : QuizChoicesView.Listener{
            override fun onOptionClicked() {
                listener.onClicked(item)
            }
        })
    }

    interface Listener {
        fun onClicked(item: QuizChoicesUiModel)
    }
}