package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycBottomSheetBinding

@SuppressLint("UnifyComponentUsage")
class GotoKycBottomSheetDialog(
    context: Context,
    theme: Int,
    val showCloseIcon: Boolean,
    val content: View
) : BottomSheetDialog(context, theme) {

    private var binding: LayoutGotoKycBottomSheetBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutGotoKycBottomSheetBinding.inflate(LayoutInflater.from(context))
        initView()

        initListener()
    }

    private fun initView() {
        binding?.root?.let {
            it.addView(content)

            binding?.spacing?.showWithCondition(!showCloseIcon)
            binding?.icDismiss?.showWithCondition(showCloseIcon)

            setContentView(it)
        }

        setUpAttribute()
    }

    private fun setUpAttribute() {
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun initListener() {
        binding?.icDismiss?.setOnClickListener {
            dismiss()
        }
    }

}
