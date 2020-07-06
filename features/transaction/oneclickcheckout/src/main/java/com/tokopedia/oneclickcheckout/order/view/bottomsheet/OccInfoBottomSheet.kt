package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.data.get.OnboardingComponentResponse
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_occ_info.view.*

class OccInfoBottomSheet {

    private var bottomSheetUnify: BottomSheetUnify? = null

    fun show(fragment: OrderSummaryPageFragment, body: OnboardingComponentResponse) {
        fragment.fragmentManager?.let {
            bottomSheetUnify = BottomSheetUnify().apply {
                setTitle(body.headerTitle)

                val child = View.inflate(fragment.context, R.layout.bottom_sheet_occ_info, null)
                setupChild(child, body)
                setChild(child)
                show(it, null)
            }
        }
    }

    private fun setupChild(child: View, body: OnboardingComponentResponse) {
        ImageHandler.loadImageFitCenter(child.context, child.iv_body, body.bodyImage)
        child.tv_body.text = MethodChecker.fromHtml(body.bodyMessage)
        child.tv_action.text = body.infoComponent.text
        child.tv_action.setOnClickListener {
            RouteManager.route(child.context, ApplinkConstInternalGlobal.WEBVIEW, body.infoComponent.link)
        }
        child.btn_action.setOnClickListener {
            bottomSheetUnify?.dismiss()
        }
    }
}