package com.tokopedia.feedcomponent.util.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.feedcomponent.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_comment_del_report.*

class ReportDeleteBS : BottomSheetUnify(){
    private var contentView: View? = null
    var onDeleteClick: (() -> Unit)? = null
    var onReportClick: (() -> Unit)? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.bottomsheet_comment_del_report, null)
        setChild(contentView)
      //  setTitle(name)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        context?.let {
            report_comment?.setOnClickListener {
                onReportClick?.invoke()
                dismiss()
            }
            delete_comment?.setOnClickListener {
                onDeleteClick?.invoke()
                dismiss()
            }
        }
    }

    fun show(
            fragmentManager: FragmentManager) {
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
    }
    companion object {
        private const val TOPADS_BOTTOM_SHEET_ACTION_TAG = "ACTION_FILTER_BOTTOM_SHEET_TAG"
        fun newInstance(): ReportDeleteBS = ReportDeleteBS()
    }
}