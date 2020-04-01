package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.OnboardingComponentResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageFragment
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
        child.tv_body.text = getHtmlFormat(body.bodyMessage)
        child.tv_action.text = body.infoComponent.text
        child.tv_action.setOnClickListener {
            RouteManager.route(child.context, ApplinkConstInternalGlobal.WEBVIEW, body.infoComponent.link)
        }
        child.btn_action.setOnClickListener {
            bottomSheetUnify?.dismiss()
        }
    }

    private fun getHtmlFormat(text: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(text)
        }
    }
}