package com.tokopedia.talk.feature.reply.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.common.constants.TalkConstants.COMMENT_ID
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnReplyBottomSheetClickedListener
import com.tokopedia.talk.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.widget_talk_report_bottom_sheet.*

class TalkReplyReportBottomSheet : BottomSheetUnify() {

    companion object {
        const val ALLOW_REPORT = "allow_report"
        const val ALLOW_DELETE = "allow_delete"
        const val ALLOW_EDIT = "allow_edit"

        fun createInstance(context: Context,
                           commentId: String,
                           onReplyBottomSheetClickedListener: OnReplyBottomSheetClickedListener,
                           allowReport: Boolean,
                           allowDelete: Boolean,
                           allowEdit: Boolean
        ) : TalkReplyReportBottomSheet = TalkReplyReportBottomSheet().apply {
                arguments = Bundle()
                arguments?.let {
                    it.putString(COMMENT_ID, commentId)
                    it.putBoolean(ALLOW_REPORT, allowReport)
                    it.putBoolean(ALLOW_DELETE, allowDelete)
                    it.putBoolean(ALLOW_EDIT, allowEdit)
                }
                this.onReplyBottomSheetClickedListener = onReplyBottomSheetClickedListener
                val view = View.inflate(context, R.layout.widget_talk_report_bottom_sheet,null)
                setChild(view)
            }
    }

    private var commentId = ""
    private var allowReport = false
    private var allowDelete = false
    private var allowEdit = false
    private var onReplyBottomSheetClickedListener: OnReplyBottomSheetClickedListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getDataFromArguments()
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        showReportWithCondition()
        showDeleteWithCondition()
        showEditProductWithCondition()
    }

    private fun getDataFromArguments() {
        arguments?.let {
            commentId = it.getString(COMMENT_ID, "")
            allowReport = it.getBoolean(ALLOW_REPORT)
            allowDelete = it.getBoolean(ALLOW_DELETE)
            allowEdit = it.getBoolean(ALLOW_EDIT)
        }
    }

    private fun showReportWithCondition() {
        if(allowReport) {
            talkReplyReport.setOnClickListener {
                onReplyBottomSheetClickedListener?.onReportOptionClicked(commentId)
                this.dismiss()
            }
            talkReplyReport.visibility = View.VISIBLE
        }
    }

    private fun showDeleteWithCondition() {
        if(allowDelete) {
            if(commentId.isNotBlank()) {
                talkReplyDelete.text = getString(R.string.delete_answer_bottom_sheet)
            } else {
                talkReplyDelete.text = getString(R.string.delete_question_bottom_sheet)
            }
            talkReplyDelete.setOnClickListener {
                onReplyBottomSheetClickedListener?.onDeleteOptionClicked(commentId)
                this.dismiss()
            }
            talkReplyDelete.visibility = View.VISIBLE
        }
    }

    private fun showEditProductWithCondition() {
        if(allowEdit) {
            talkReplyReport.apply {
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