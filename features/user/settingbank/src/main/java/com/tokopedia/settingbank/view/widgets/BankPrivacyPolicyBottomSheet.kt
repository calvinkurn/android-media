package com.tokopedia.settingbank.view.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.databinding.BottomSheetsPrivacyPolicyBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class BankPrivacyPolicyBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomSheetsPrivacyPolicyBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setDefaultParams()
        initBottomSheet(container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initBottomSheet(container: ViewGroup?) {
        context?.run {
            binding = BottomSheetsPrivacyPolicyBinding.inflate(LayoutInflater.from(context), container, false)
            setTitle(TITLE)
            setChild(binding?.root)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.privacyWebview?.settings?.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }
        context?.let {
            binding?.privacyWebview?.loadUrl(it.getString(R.string.sbank_privacy_url))
        }
    }

    private fun setDefaultParams() {
        isFullpage = true
        isDragable = false
        isHideable = false
    }

    companion object {
        private const val TITLE = "Kebijakan Privasi"
        private const val TAG = "BankPrivacyBottomSheet"
        fun showBankPrivacyBottomSheet(activity: FragmentActivity?) {
            activity?.let {
                if (!activity.isFinishing) {
                    val bottomSheet = BankPrivacyPolicyBottomSheet()
                    bottomSheet.show(activity.supportFragmentManager, TAG)
                }
            }
        }
    }
}
