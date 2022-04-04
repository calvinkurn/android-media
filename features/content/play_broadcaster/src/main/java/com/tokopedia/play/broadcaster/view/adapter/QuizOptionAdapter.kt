package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.game.QuizOptionViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.QuizOptionAdapterDelegate

/**
 * Created By : Jonathan Darwin on April 04, 2022
 */
class QuizOptionAdapter(
    listener: QuizOptionViewHolder.Listener,
) : BaseDiffUtilAdapter<QuizFormDataUiModel.Option>() {

    init {
        delegatesManager
            .addDelegate(QuizOptionAdapterDelegate(listener))
    }

    override fun areItemsTheSame(
        oldItem: QuizFormDataUiModel.Option,
        newItem: QuizFormDataUiModel.Option
    ): Boolean {
        return oldItem.order == newItem.order
    }

    override fun areContentsTheSame(
        oldItem: QuizFormDataUiModel.Option,
        newItem: QuizFormDataUiModel.Option
    ): Boolean {
        return oldItem == newItem
    }
}