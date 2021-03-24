package com.tokopedia.purchase_platform.common.feature.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.purchase_platform.common.databinding.LayoutGeneralBottomSheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class GeneralBottomSheet {

    private var title: String = ""
    private var desc: String = ""
    private var buttonText: String = ""
    private var iconRes: Int = 0
    private var buttonOnClickListener: (BottomSheetUnify) -> Unit = {}

    var bottomSheet: BottomSheetUnify? = null
        private set

    fun setTitle(title: String) {
        this.title = title
    }

    fun setDesc(desc: String) {
        this.desc = desc
    }

    fun setButtonText(buttonText: String) {
        this.buttonText = buttonText
    }

    fun setButtonOnClickListener(onClickListener: (BottomSheetUnify) -> Unit) {
        this.buttonOnClickListener = onClickListener
    }

    fun setIcon(@DrawableRes iconRes: Int) {
        this.iconRes = iconRes
    }

    fun show(context: Context, fragmentManager: FragmentManager) {
        bottomSheet = BottomSheetUnify().apply {
            showCloseIcon = false
            showHeader = false
            showKnob = true
            isHideable = true
            isDragable = true
            isSkipCollapseState = true
            overlayClickDismiss = true
            clearContentPadding = true
            val binding = LayoutGeneralBottomSheetBinding.inflate(LayoutInflater.from(context), null ,false)
            setupChildView(binding)
            setChild(binding.root)
        }
        bottomSheet?.show(fragmentManager, "")
    }

    private fun setupChildView(binding: LayoutGeneralBottomSheetBinding) {
        binding.tvTitle.text = title
        binding.tvDesc.text = desc
        if (iconRes > 0) {
            binding.ivIcon.loadImageDrawable(iconRes)
        } else {
            binding.ivIcon.gone()
        }
        binding.btnAction.text = buttonText
        binding.btnAction.setOnClickListener {
            val currentBottomSheet = bottomSheet
            if (currentBottomSheet != null) {
                buttonOnClickListener.invoke(currentBottomSheet)
            }
        }
    }
}