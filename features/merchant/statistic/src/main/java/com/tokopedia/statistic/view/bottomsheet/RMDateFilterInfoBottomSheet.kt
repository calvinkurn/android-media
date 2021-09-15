package com.tokopedia.statistic.view.bottomsheet

import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.statistic.R
import kotlinx.android.synthetic.main.bottomsheet_stc_rm_date_filter_info.view.*

/**
 * Created By @ilhamsuaib on 18/08/21
 */

class RMDateFilterInfoBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "RMDateFilterInfoBottomSheet"
        fun newInstance() = RMDateFilterInfoBottomSheet()
    }

    override fun getResLayout(): Int = R.layout.bottomsheet_stc_rm_date_filter_info

    override fun setupView() = childView?.run {
        setTitle(context.getString(R.string.stc_change_date_range))
        tvStcUpgradeToPM.setOnClickListener {
            openPowerMerchantPage()
            dismiss()
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun openPowerMerchantPage() {
        activity?.let {
            RouteManager.route(it, ApplinkConst.POWER_MERCHANT_SUBSCRIBE)
        }
    }
}