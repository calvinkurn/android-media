package com.tokopedia.topads.common.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.topads.common.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

private const val LEARN_MORE_LINK = "https://seller.tokopedia.com/edu/iklan-manual-baru/"

class BidInfoBottomSheet : BottomSheetUnify() {

    private var learnMore: Typography? = null

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

        learnMore = contentView.findViewById(R.id.txtLearnMore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle(resources.getString(com.tokopedia.topads.common.R.string.autobid_otomatis_title))

        learnMore?.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, LEARN_MORE_LINK)
        }

    }

}