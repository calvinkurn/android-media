package com.tokopedia.play_common.view.quiz

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.R
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState

/**
 * @author by astidhiyaa on 08/04/22
 */
class QuizChoiceViewHolder(
    itemView: View,
    private val listener: Listener
) : BaseViewHolder(itemView) {

    private val widget = itemView.findViewById<QuizChoicesView>(R.id.quiz_option)

    fun bind(item: QuizChoicesUiModel){
        widget.setupView(item)

        if (item.type is PlayQuizOptionState.Default){
            widget.setOnClickListener {
                listener.onClicked(item)
            }
        }
    }

    interface Listener {
        fun onClicked(item: QuizChoicesUiModel)
    }
}