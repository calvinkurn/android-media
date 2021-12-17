package com.tokopedia.settingbank.view.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.tokopedia.settingbank.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheets_privacy_policy.*


class BankPrivacyPolicyBottomSheet : BottomSheetUnify() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setDefaultParams()
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initBottomSheet() {
        context?.run {
            val child = LayoutInflater.from(this)
                .inflate(R.layout.bottom_sheets_privacy_policy, ConstraintLayout(this), false)
            setTitle(TITLE)
            setChild(child)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        privacyWebview.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }
        context?.let{
            privacyWebview.loadUrl(it.getString(R.string.sbank_privacy_url))
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
