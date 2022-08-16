package com.tokopedia.play_common.view.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play_common.R
import com.tokopedia.play_common.databinding.ItemQuizOptionBinding
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel

/**
 * @author by astidhiyaa on 08/04/22
 */
class QuizListCompleteAdapterDelegate(
    listener: QuizChoiceViewHolder.Listener
) : TypedAdapterDelegate<QuizChoicesUiModel, QuizChoicesUiModel, QuizChoiceViewHolder>(R.layout.item_quiz_option),
    QuizChoiceViewHolder.Listener by listener {

    override fun onBindViewHolder(item: QuizChoicesUiModel, holder: QuizChoiceViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): QuizChoiceViewHolder {
        val view = ItemQuizOptionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return QuizChoiceViewHolder(view, this)
    }
}