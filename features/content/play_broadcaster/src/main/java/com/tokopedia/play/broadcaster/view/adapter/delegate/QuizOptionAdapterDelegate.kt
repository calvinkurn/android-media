package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.game.QuizOptionViewHolder
import com.tokopedia.play_common.R

/**
 * Created By : Jonathan Darwin on April 04, 2022
 */
class QuizOptionAdapterDelegate(
    private val listener: QuizOptionViewHolder.Listener,
) : TypedAdapterDelegate<QuizFormDataUiModel.Option, QuizFormDataUiModel.Option, QuizOptionViewHolder>(
    R.layout.view_play_empty){

    override fun onBindViewHolder(item: QuizFormDataUiModel.Option, holder: QuizOptionViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): QuizOptionViewHolder {
        return QuizOptionViewHolder.create(parent, listener)
    }
}