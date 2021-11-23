package com.tokopedia.settingbank.view.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.tokopedia.settingbank.R
import com.tokopedia.unifycomponents.BottomSheetUnify


class BankPrivacyPolicyBottomSheet : BottomSheetUnify() {


    val MIME_TYPE = "text/html"
    val ENCODING = "utf-8"

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


    private fun setDefaultParams() {
        isFullpage = true
        isDragable = true
        isHideable = true
    }

    companion object {
        private val TITLE = "Kebijakan Privasi"
        private val TAG = "BankPrivacyBottomSheet"
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
