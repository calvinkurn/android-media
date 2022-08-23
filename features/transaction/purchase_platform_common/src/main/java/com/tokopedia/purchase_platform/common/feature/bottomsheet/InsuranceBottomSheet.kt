package com.tokopedia.purchase_platform.common.feature.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.purchase_platform.common.databinding.LayoutInsuranceBottomSheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class InsuranceBottomSheet {

    private var desc: String = ""
    private var imageRes: Int = 0

    var bottomSheet: BottomSheetUnify? = null
        private set

    fun setDesc(desc: String) {
        this.desc = desc
    }

    fun setImage(@DrawableRes imageRes: Int) {
        this.imageRes = imageRes
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
            val binding = LayoutInsuranceBottomSheetBinding.inflate(LayoutInflater.from(context), null ,false)
            setupChildView(binding)
            setChild(binding.root)
        }
        bottomSheet?.show(fragmentManager, "")
    }

    private fun setupChildView(binding: LayoutInsuranceBottomSheetBinding) {
        binding.tvDesc.text = MethodChecker.fromHtml(desc)
        if (imageRes > 0) {
            binding.ivInsurance.loadImage(imageRes)

        } else {
            binding.ivInsurance.gone()
        }
    }
}