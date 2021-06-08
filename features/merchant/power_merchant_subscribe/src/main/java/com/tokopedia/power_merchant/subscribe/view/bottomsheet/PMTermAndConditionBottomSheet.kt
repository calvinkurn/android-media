package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import androidx.fragment.app.FragmentManager
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.webview.BaseSessionWebViewFragment
import kotlinx.android.synthetic.main.bottom_sheet_pm_term_and_condition.view.*

/**
 * Created By @ilhamsuaib on 25/05/21
 */

class PMTermAndConditionBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "PMTermAndConditionBottomSheet"
        fun newInstance(): PMTermAndConditionBottomSheet = PMTermAndConditionBottomSheet().apply {
            isFullpage = true
            clearContentPadding = true
        }
    }

    override fun getChildResLayout(): Int = R.layout.bottom_sheet_pm_term_and_condition

    override fun setupView() = childView?.run {
        showFragment()
        btnPmTncOke.setOnClickListener {
            dismiss()
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun showFragment() {
        val webViewFragment = BaseSessionWebViewFragment.newInstance(Constant.Url.POWER_MERCHANT_TERMS_AND_CONDITION)
        val fm = childFragmentManager
        if (fm.isStateSaved || webViewFragment.isAdded) return

        childFragmentManager.beginTransaction()
                .replace(R.id.containerPmTnC, webViewFragment)
                .commit()
    }
}