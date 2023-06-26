package com.tokopedia.common_digital.common.presentation.bottomsheet

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.unifycomponents.BottomSheetUnify
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common_digital.databinding.BottomsheetRechargeGeneralDppoConsentBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.common_digital.R

class DigitalDppoConsentBottomSheet(private val description: String): BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomsheetRechargeGeneralDppoConsentBinding>()

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        initConsentContent()
        setTitle(getString(R.string.digital_common_dppo_consent))
        setChild(binding?.root)
        setCloseClickListener {
            dismiss()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        binding = BottomsheetRechargeGeneralDppoConsentBinding.inflate(LayoutInflater.from(context))
    }

    private fun initConsentContent() {
        binding?.dppoConsentDescription?.text = MethodChecker.fromHtml(description)
        binding?.dppoConsentDescription?.movementMethod = LinkMovementMethod.getInstance()
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG_GENERAL_CONSENT_BOTTOM_SHEET)
    }

    companion object {
        private const val TAG_GENERAL_CONSENT_BOTTOM_SHEET = "GENERAL_CONSENT_BOTTOM_SHEET"
    }
}

