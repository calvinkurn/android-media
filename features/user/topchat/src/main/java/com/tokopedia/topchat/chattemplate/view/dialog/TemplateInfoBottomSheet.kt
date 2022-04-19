package com.tokopedia.topchat.chattemplate.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.topchat.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class TemplateInfoBottomSheet : BottomSheetUnify() {

    private var desc: Typography? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTitle()
        initDesc()
    }

    private fun initChildLayout() {
        val view = View.inflate(context, R.layout.bs_chat_template_info, null).also {
            initViewBinding(it)
        }
        setChild(view)
    }

    private fun initViewBinding(view: View) {
        desc = view.findViewById(R.id.bs_chat_desc)
    }

    private fun initTitle() {
        val title = context?.getString(R.string.title_info_list_template) ?: return
        setTitle(title)
    }

    private fun initDesc() {
        val desc = context?.getString(R.string.body_info_list_template) ?: return
        this.desc?.text = desc
    }
}