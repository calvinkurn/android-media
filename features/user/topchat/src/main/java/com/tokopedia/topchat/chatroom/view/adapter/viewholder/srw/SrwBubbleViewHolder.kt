package com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.srw.SrwBubbleUiModel
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.getOppositeMargin
import com.tokopedia.topchat.chatroom.view.custom.SrwFrameLayout
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview

class SrwBubbleViewHolder constructor(
    itemView: View?,
    private val listener: Listener?,
    private val adapterListener: AdapterListener?,
) : AbstractViewHolder<SrwBubbleUiModel>(itemView) {

    private val srwLayout: SrwFrameLayout? = itemView?.findViewById(R.id.chat_srw_bubble)
    private val topMarginOpposite: Float = getOppositeMargin(itemView?.context)

    interface Listener {
        fun trackClickSrwBubbleQuestion(
            products: List<SendablePreview>, question: QuestionUiModel
        )

        fun onClickSrwBubbleQuestion(
            products: List<SendablePreview>, question: QuestionUiModel
        )
    }

    object Signal {
        const val EXPANDED = 1
        const val COLLAPSED = 2
        const val UPDATE_TOP_MARGIN = 3
    }

    override fun bind(element: SrwBubbleUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads.first()) {
            Signal.COLLAPSED,
            Signal.EXPANDED -> bindState(element)
            Signal.UPDATE_TOP_MARGIN -> bindTopMargin(element)
        }
    }

    override fun bind(element: SrwBubbleUiModel) {
        setupSrw(element)
        bindPreviousSRWPreviewState(element)
        bindState(element)
        bindTopMargin(element)
    }

    private fun bindTopMargin(element: SrwBubbleUiModel) {
        val lp = srwLayout?.layoutParams as? ViewGroup.MarginLayoutParams ?: return
        if (adapterListener?.isOpposite(adapterPosition, true) == true) {
            lp.topMargin = topMarginOpposite.toInt()
        } else {
            lp.topMargin = 0
        }
        srwLayout.layoutParams = lp
    }

    private fun bindPreviousSRWPreviewState(element: SrwBubbleUiModel) {
        element.initializeWithSrwPreview()
    }

    private fun bindState(element: SrwBubbleUiModel) {
        srwLayout?.isExpanded = element.isExpanded
    }

    private fun setupSrw(element: SrwBubbleUiModel) {
        srwLayout?.initialize(element.srwPreviewState, object : SrwQuestionViewHolder.Listener {
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