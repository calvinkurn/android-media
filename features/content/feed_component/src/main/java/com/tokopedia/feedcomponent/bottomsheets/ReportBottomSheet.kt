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
        fun newInstance(postId: Int, context: OnReportOptionsClick): ReportBottomSheet {
            return ReportBottomSheet().apply {
                this.onReportOptionsClick = context
                this.contentId = postId
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = View.inflate(context, R.layout.bottomsheet_report, null)
        setChild(contentView)
        setTitle(getString(R.string.feed_report_comment))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun sendReport() {
        getReason()
        setFinalView()
        onReportOptionsClick?.onOption1(reasonType, reasonDesc)
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
            4 -> {
                reasonType = getString(R.string.feed_common_reason_type_others)
                reasonDesc = getString(R.string.feed_common_reason_desc_others)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        report_subtext1?.setOnClickListener {
            isClicked = 1
            sendReport()
        }
        report_subtext1_icon?.setOnClickListener {
            isClicked = 1
            sendReport()

        }
        report_subtext2?.setOnClickListener {
            isClicked = 2
            sendReport()

        }
        report_subtext2_icon?.setOnClickListener {
            isClicked = 2
            sendReport()

        }
        report_subtext3?.setOnClickListener {
            isClicked = 3
            sendReport()

        }
        report_subtext3_icon?.setOnClickListener {
            isClicked = 3
            sendReport()

        }
        report_subtext4?.setOnClickListener {
            isClicked = 4
            sendReport()

        }
        report_subtext4_icon?.setOnClickListener {
            isClicked = 4
            sendReport()
        }
    }

    private fun setFinalView() {
        layout1?.gone()
        layout2?.gone()
        layout3.visible()
    }

    interface OnReportOptionsClick {
        fun onOption1(reasonType: String, reasonDesc: String)
    }
}
