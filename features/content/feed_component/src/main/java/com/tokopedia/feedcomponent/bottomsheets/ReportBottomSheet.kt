package com.tokopedia.feedcomponent.bottomsheets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_report.*

class ReportBottomSheet : BottomSheetUnify() {
    private var onReportOptionsClick: OnReportOptionsClick? = null

    companion object {
        fun newInstance(context: Context): ReportBottomSheet {
            return ReportBottomSheet().apply {
                this.onReportOptionsClick = context as? OnReportOptionsClick
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = View.inflate(context, R.layout.bottomsheet_report, null)
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        report_subtext1.setOnClickListener {
            onReportOptionsClick?.onOption1()
        }
        report_subtext1_icon.setOnClickListener {
            onReportOptionsClick?.onOption1()
        }
        report_subtext2.setOnClickListener {
            onReportOptionsClick?.onOption2()
        }
        report_subtext2_icon.setOnClickListener {
            onReportOptionsClick?.onOption2()
        }
    }

    interface OnReportOptionsClick {
        fun onOption1()
        fun onOption2()
    }
}
