package com.tokopedia.talk.feature.reply.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.talk.common.TalkConstants.COMMENT_ID
import com.tokopedia.talk.common.TalkConstants.TALK_ID
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.widget_talk_report_bottom_sheet.*

class TalkReplyReportBottomSheet : BottomSheetUnify() {

    companion object {

        fun createInstance(context: Context, talkId: Int, commentId: Int, onReportClickedListener: OnReportClickedListener) : TalkReplyReportBottomSheet {
            return TalkReplyReportBottomSheet().apply{
                arguments = Bundle()
                arguments?.let {
                    it.putInt(TALK_ID, talkId)
                    it.putInt(COMMENT_ID, commentId)
                }
                this.onReportClickedListener = onReportClickedListener
                val view = View.inflate(context, R.layout.widget_talk_report_bottom_sheet,null)
                setChild(view)
            }
        }
    }

    private var talkId = 0
    private var commentId = 0
    private var onReportClickedListener: OnReportClickedListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getDataFromArguments()
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        talkReplyReport.setOnClickListener {
            onReportClickedListener?.onReportOptionClicked(talkId, commentId)
        }
    }

    private fun getDataFromArguments() {
        arguments?.let {
            talkId = it.getInt(TALK_ID)
            commentId = it.getInt(COMMENT_ID)
        }
    }

}