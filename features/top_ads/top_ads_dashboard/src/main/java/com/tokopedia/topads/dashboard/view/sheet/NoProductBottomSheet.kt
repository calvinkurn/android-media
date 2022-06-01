package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class NoProductBottomSheet : BottomSheetUnify() {

    companion object {
        fun newInstance() = NoProductBottomSheet()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val contentView = View.inflate(context, R.layout.topads_dash_no_products_sheet_layout, null)
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageUnify>(R.id.imageEmpty)
            ?.setImageDrawable(context?.getResDrawable(R.drawable.topads_dash_grey_circle))
        view.findViewById<Typography>(R.id.addProductBtn)?.setOnClickListener {
            RouteManager.route(context, ApplinkConst.PRODUCT_MANAGE)
        }
    }
}