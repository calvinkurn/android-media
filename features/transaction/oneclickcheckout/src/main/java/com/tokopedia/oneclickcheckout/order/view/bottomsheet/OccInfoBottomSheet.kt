package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.view.LayoutInflater
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.oneclickcheckout.databinding.BottomSheetOccInfoBinding
import com.tokopedia.oneclickcheckout.order.data.get.OnboardingComponentResponse
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.unifycomponents.BottomSheetUnify

class OccInfoBottomSheet {

    private var bottomSheetUnify: BottomSheetUnify? = null

    fun show(fragment: OrderSummaryPageFragment, body: OnboardingComponentResponse) {
        fragment.parentFragmentManager.let {
            bottomSheetUnify = BottomSheetUnify().apply {
                setTitle(body.headerTitle)

                val binding = BottomSheetOccInfoBinding.inflate(LayoutInflater.from(fragment.context))
                setupChild(binding, body)
                setChild(binding.root)
                show(it, null)
            }
        }
    }

    private fun setupChild(binding: BottomSheetOccInfoBinding, body: OnboardingComponentResponse) {
        ImageHandler.loadImageFitCenter(binding.root.context, binding.ivBody, body.bodyImage)
        binding.tvBody.text = MethodChecker.fromHtml(body.bodyMessage)
        binding.tvAction.apply {
            text = body.infoComponent.text
            setOnClickListener {
                RouteManager.route(binding.root.context, ApplinkConstInternalGlobal.WEBVIEW, body.infoComponent.link)
            }
        }
        binding.btnAction.setOnClickListener {
            bottomSheetUnify?.dismiss()
        }
    }
}