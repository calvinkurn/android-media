package com.tokopedia.play.view.quiz

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter

/**
 * @author by astidhiyaa on 08/04/22
 */
class QuizListAdapter(
    listener: QuizChoiceViewHolder.Listener
) : BaseDiffUtilAdapter<QuizChoicesUiModel>() {

    init {
        delegatesManager
            .addDelegate(QuizListCompleteAdapterDelegate(listener))
    }

    override fun areItemsTheSame(
        oldItem: QuizChoicesUiModel,
        newItem: QuizChoicesUiModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: QuizChoicesUiModel,
        newItem: QuizChoicesUiModel
    ): Boolean {
        return oldItem == newItem
    }
}