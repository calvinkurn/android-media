package com.tokopedia.topads.edit.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.topads.edit.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class BidInfoBottomSheet(private val isAutomatic: Boolean) : BottomSheetUnify() {

    private var description: Typography? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val contentView = View.inflate(context, R.layout.layout_topads_edit_bid_info, null)
        setChild(contentView)
        showKnob = true
        isHideable = true
        showCloseIcon = false

        context?.run {
            contentView.setPadding(
                0, 0, 0,
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4))
        }

        description = contentView.findViewById(R.id.txtDescription)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAutomatic) {
            setTitle(resources.getString(R.string.autobid_otomatis_title))
            description?.text =
                resources.getString(com.tokopedia.topads.common.R.string.autobid_otomatis_desc)
        } else {
            setTitle(resources.getString(R.string.autobid_manual_title))
            description?.text =
                resources.getString(com.tokopedia.topads.common.R.string.autobid_manual_desc)
        }

    }

}