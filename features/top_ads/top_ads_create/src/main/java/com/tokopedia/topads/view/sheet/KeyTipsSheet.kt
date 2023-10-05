package com.tokopedia.topads.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topads.create.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class KeyTipsSheet : BottomSheetUnify() {

    private var desc1: Typography? = null
    private var desc2: Typography? = null
    private var desc3: Typography? = null
    private var desc4: Typography? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val contentView = View.inflate(context, R.layout.topads_create_key_sheet_layout, null)
        setChild(contentView)
        showKnob = true
        isHideable = true
        isDragable = true
        showCloseIcon = false
        context?.getString(com.tokopedia.topads.common.R.string.topads_empty_tip_memilih_kata_kunci_title)?.let { setTitle(it) }
        desc1 = contentView.findViewById(R.id.desc1)
        desc2 = contentView.findViewById(R.id.desc2)
        desc3 = contentView.findViewById(R.id.desc3)
        desc4 = contentView.findViewById(R.id.desc4)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        desc1?.text = MethodChecker.fromHtml(getString(R.string.topads_create_tip_sheet_desc1))
        desc2?.text = MethodChecker.fromHtml(getString(R.string.topads_create_tip_sheet_desc2))
        desc3?.text = MethodChecker.fromHtml(getString(R.string.topads_create_tip_sheet_desc3))
        desc4?.text = MethodChecker.fromHtml(getString(R.string.topads_create_tip_sheet_desc4))
    }
}
