package com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.srw.SrwBubbleUiModel
import com.tokopedia.topchat.chatroom.view.custom.SrwFrameLayout
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview

class SrwBubbleViewHolder constructor(
        itemView: View?,
        private val listener: Listener?
) : AbstractViewHolder<SrwBubbleUiModel>(itemView) {

    private val srwLayout: SrwFrameLayout? = itemView?.findViewById(R.id.chat_srw_bubble)

    interface Listener {
        fun trackClickSrwBubbleQuestion(
                products: List<SendablePreview>, question: QuestionUiModel
        )

        fun onClickSrwBubbleQuestion(
                products: List<SendablePreview>, question: QuestionUiModel
        )
    }

    override fun bind(element: SrwBubbleUiModel) {
        setupSrw(element)
    }

    private fun setupSrw(element: SrwBubbleUiModel) {
        srwLayout?.initialize(element.srwHangingState, object : SrwQuestionViewHolder.Listener {
            override fun onClickSrwQuestion(question: QuestionUiModel) {
                listener?.onClickSrwBubbleQuestion(element.products, question)
            }

            override fun trackClickSrwQuestion(question: QuestionUiModel) {
                listener?.trackClickSrwBubbleQuestion(element.products, question)
            }
        })
    }

    companion object {
        val LAYOUT = R.layout.item_chat_srw_bubble
    }
}