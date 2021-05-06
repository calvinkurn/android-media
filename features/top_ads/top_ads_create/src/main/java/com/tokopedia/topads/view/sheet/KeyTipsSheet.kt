package com.tokopedia.topads.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topads.create.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_create_key_sheet_layout.*

class KeyTipsSheet : BottomSheetUnify() {
    private var contentView: View? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.topads_create_key_sheet_layout, null)
        setChild(contentView)
        showKnob = true
        isHideable = true
        isDragable = true
        showCloseIcon = false
        context?.getString(R.string.topads_empty_tip_memilih_kata_kunci_title)?.let { setTitle(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        desc1?.text = MethodChecker.fromHtml(getString(R.string.topads_create_tip_sheet_desc1))
        desc2?.text = MethodChecker.fromHtml(getString(R.string.topads_create_tip_sheet_desc2))
        desc3?.text = MethodChecker.fromHtml(getString(R.string.topads_create_tip_sheet_desc3))
        desc4?.text = MethodChecker.fromHtml(getString(R.string.topads_create_tip_sheet_desc4))
    }

    companion object {
        fun createInstance(): KeyTipsSheet {
            return KeyTipsSheet()
        }
    }

}