package com.tokopedia.talk.feature.reply.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.R
import com.tokopedia.talk.common.constants.TalkConstants.COMMENT_ID
import com.tokopedia.talk.databinding.WidgetTalkReportBottomSheetBinding
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnReplyBottomSheetClickedListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoCleared

class TalkReplyReportBottomSheet : BottomSheetUnify() {

    companion object {
        const val ALLOW_REPORT = "allow_report"
        const val ALLOW_DELETE = "allow_delete"
        const val ALLOW_EDIT = "allow_edit"
        const val ALLOW_BLOCK = "allow_block"

        fun createInstance(
            context: Context,
            commentId: String,
            onReplyBottomSheetClickedListener: OnReplyBottomSheetClickedListener,
            allowReport: Boolean,
            allowDelete: Boolean,
            allowEdit: Boolean,
            allowBlock: Boolean
        ): TalkReplyReportBottomSheet = TalkReplyReportBottomSheet().apply {
            arguments = Bundle()
            arguments?.let {
                it.putString(COMMENT_ID, commentId)
                it.putBoolean(ALLOW_REPORT, allowReport)
                it.putBoolean(ALLOW_DELETE, allowDelete)
                it.putBoolean(ALLOW_EDIT, allowEdit)
                it.putBoolean(ALLOW_BLOCK, allowBlock)
            }
            this.onReplyBottomSheetClickedListener = onReplyBottomSheetClickedListener
            val view = View.inflate(context, R.layout.widget_talk_report_bottom_sheet, null)
            binding = WidgetTalkReportBottomSheetBinding.bind(view)
            setChild(view)
        }
    }

    private var commentId = ""
    private var allowReport = false
    private var allowDelete = false
    private var allowEdit = false
    private var allowBlock = false
    private var onReplyBottomSheetClickedListener: OnReplyBottomSheetClickedListener? = null

    private var binding: WidgetTalkReportBottomSheetBinding by autoCleared<WidgetTalkReportBottomSheetBinding> {  }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getDataFromArguments()
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        showReportWithCondition()
        showBlockWithCondition()
        showDeleteWithCondition()
        showEditProductWithCondition()
    }

    private fun getDataFromArguments() {
        arguments?.let {
            commentId = it.getString(COMMENT_ID, "")
            allowReport = it.getBoolean(ALLOW_REPORT)
            allowDelete = it.getBoolean(ALLOW_DELETE)
            allowEdit = it.getBoolean(ALLOW_EDIT)
            allowBlock = it.getBoolean(ALLOW_BLOCK)
        }
    }

    private fun showReportWithCondition() {
        if (allowReport) {
            binding.talkReplyReport.setOnClickListener {
                onReplyBottomSheetClickedListener?.onReportOptionClicked(commentId)
                this.dismiss()
            }
            binding.talkReplyReport.show()
            binding.dividerTalkReplyReport.show()
        }
    }

    private fun showBlockWithCondition() {
        if (allowBlock) {
            binding.talkReplyBlock.setOnClickListener {
                onReplyBottomSheetClickedListener?.onBlockOptionClicked()
                this.dismiss()
            }
            binding.talkReplyBlock.show()
            binding.dividerTalkReplyBlock.show()
        }
    }

    private fun showDeleteWithCondition() {
        if (allowDelete) {
            with(binding) {
                if (commentId.isNotBlank()) {
                    talkReplyDelete.text = getString(R.string.delete_answer_bottom_sheet)
                } else {
                    talkReplyDelete.text = getString(R.string.delete_question_bottom_sheet)
                }
                talkReplyDelete.setOnClickListener {
                    onReplyBottomSheetClickedListener?.onDeleteOptionClicked(commentId)
                    this@TalkReplyReportBottomSheet.dismiss()
                }
                talkReplyDelete.show()
            }
        }
    }

    private fun showEditProductWithCondition() {
        if (allowEdit) {
            binding.talkReplyReport.apply {
                text = getString(R.string.edit_product_bottom_sheet)
                setOnClickListener {
                    onReplyBottomSheetClickedListener?.onEditProductOptionClicked()
                    this@TalkReplyReportBottomSheet.dismiss()
                }
                show()
            }
        }
    }

}
