package com.tokopedia.purchase_platform.common.feature.bottomsheet

import com.tokopedia.imageassets.TokopediaImageUrl

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.purchase_platform.common.databinding.LayoutInsuranceBottomSheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class InsuranceBottomSheet {

    companion object {
        private const val IMAGE_URL  = TokopediaImageUrl.IMAGE_URL_SHIPPING_INSURANCE
    }

    private var desc: String = ""
    var bottomSheet: BottomSheetUnify? = null
        private set

    fun setDesc(desc: String) {
        this.desc = desc
    }

    fun show(title: String, context: Context, fragmentManager: FragmentManager) {
        bottomSheet = BottomSheetUnify().apply {
            showCloseIcon = true
            isHideable = true
            isDragable = true
            isSkipCollapseState = true
            overlayClickDismiss = true
            clearContentPadding = true

            setTitle(title)
            val binding = LayoutInsuranceBottomSheetBinding.inflate(LayoutInflater.from(context), null, false)
            setupChildView(binding)
            setChild(binding.root)
        }
        bottomSheet?.show(fragmentManager, "")

    }

    private fun setupChildView(binding: LayoutInsuranceBottomSheetBinding) {
        binding.tvDesc.text = MethodChecker.fromHtml(desc)
        binding.ivInsurance.loadImage(IMAGE_URL)
    }
}