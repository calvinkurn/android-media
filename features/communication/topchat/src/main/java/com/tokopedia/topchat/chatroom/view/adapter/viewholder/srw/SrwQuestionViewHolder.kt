package com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.databinding.ItemTopchatSrwQuestionBinding
import com.tokopedia.utils.view.binding.viewBinding

class SrwQuestionViewHolder constructor(
        itemView: View?,
        private val listener: Listener?
) : AbstractViewHolder<QuestionUiModel>(itemView) {

    interface Listener {
        fun onClickSrwQuestion(question: QuestionUiModel)
        fun trackClickSrwQuestion(question: QuestionUiModel)
    }

    private val binding: ItemTopchatSrwQuestionBinding? by viewBinding()

    override fun bind(element: QuestionUiModel) {
        bindTitle(element)
        bindClick(element)
        bindLabel(element)
    }

    private fun bindTitle(element: QuestionUiModel) {
        binding?.tpSrwTitle?.text = element.content
    }

    private fun bindClick(element: QuestionUiModel) {
        itemView.setOnClickListener {
            listener?.trackClickSrwQuestion(element)
            listener?.onClickSrwQuestion(element)
        }
    }

    private fun bindLabel(element: QuestionUiModel) {
        if (element.label.isEmpty()) {
            binding?.labelSrwQuestion?.hide()
        } else {
            binding?.labelSrwQuestion?.let {
                it.setLabel(element.label)
                it.show()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_srw_question
    }
}