package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.R
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_report.*

class ReportBottomSheet : BottomSheetUnify() {
    private var onReportOptionsClick: OnReportOptionsClick? = null
    private var isClicked = 0
    private var reasonType: String = ""
    private var reasonDesc: String = ""
    private var contentId: Int = 0

    companion object {
        private const val SPAM = 1
        private const val ABUSE = 2
        private const val INAPPROPRIATE = 3
        fun newInstance(postId: Int, context: OnReportOptionsClick): ReportBottomSheet {
            return ReportBottomSheet().apply {
                this.onReportOptionsClick = context
                this.contentId = postId
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = View.inflate(context, R.layout.bottomsheet_report, null)
        setChild(contentView)
        setTitle(getString(R.string.feed_report_comment))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun sendReport() {
        getReason()
        onReportOptionsClick?.onReportAction(reasonType, reasonDesc)
    }

    private fun getReason() {
        when (isClicked) {
            1 -> {
                reasonType = getString(R.string.feed_common_reason_type_spam)
                reasonDesc = getString(R.string.feed_common_reason_desc_spam)
            }
            2 -> {
                reasonType = getString(R.string.feed_common_reason_type_abuse)
                reasonDesc = getString(R.string.feed_common_reason_desc_abuse)
            }
            3 -> {
                reasonType = getString(R.string.feed_common_reason_type_inappropriate)
                reasonDesc = getString(R.string.feed_common_reason_desc_inappropriate)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        report_subtext1?.setOnClickListener {
            setSpamCase()
        }
        report_subtext1_icon?.setOnClickListener {
            setSpamCase()
        }
        report_subtext2?.setOnClickListener {
            setAbuseCase()
        }
        report_subtext2_icon?.setOnClickListener {
            setAbuseCase()
        }
        report_subtext3?.setOnClickListener {
            setInappropriateCase()
        }
        report_subtext3_icon?.setOnClickListener {
            setInappropriateCase()
        }
    }

    private fun setSpamCase() {
        isClicked = SPAM
        sendReport()
    }

    private fun setAbuseCase() {
        isClicked = ABUSE
        sendReport()
    }

    private fun setInappropriateCase() {
        isClicked = INAPPROPRIATE
        sendReport()
    }

     fun setFinalView() {
        layout1?.gone()
        layout2?.gone()
        layout3.visible()
    }

    interface OnReportOptionsClick {
        fun onReportAction(reasonType: String, reasonDesc: String)
    }
}
