package com.tokopedia.topads.common.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.topads.common.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_common_keyword_info_sheet.*


class TipSheetKeywordList : BottomSheetUnify() {

    private var contentView: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        setCloseClickListener {
            dismiss()
        }
        btn_submit?.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun createInstance(): TipSheetKeywordList {
            return TipSheetKeywordList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.topads_common_keyword_info_sheet, null)
        setChild(contentView)
        context?.getString(R.string.topads_common_keyword_info_sheet_title)?.let { setTitle(it) }
    }

}
