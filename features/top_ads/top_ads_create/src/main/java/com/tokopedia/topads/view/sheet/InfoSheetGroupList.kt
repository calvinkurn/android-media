package com.tokopedia.topads.view.sheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.create.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_create_fragment_group_sheet_info.*
import kotlinx.android.synthetic.main.topads_create_fragment_group_sheet_info.view.*

/**
 * Author errysuprayogi on 07,May,2019
 */
class InfoSheetGroupList : BottomSheetUnify() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var contentView = View.inflate(context, R.layout.topads_create_fragment_group_sheet_info, null)
        setChild(contentView)
        setTitle(getString(R.string.apa_itu_group_iklan))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.ic1.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_ic_checklist))
        view.ic2.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_ic_checklist))
        view.ic3.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_ic_checklist))
    }

    companion object {

        fun newInstance(): InfoSheetGroupList {
            return InfoSheetGroupList()
        }
    }
}
