package com.tokopedia.statistic.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.statistic.R
import com.tokopedia.statistic.databinding.BottomsheetStcRmDateFilterInfoBinding

/**
 * Created By @ilhamsuaib on 18/08/21
 */

class RMDateFilterInfoBottomSheet : BaseBottomSheet<BottomsheetStcRmDateFilterInfoBinding>() {

    companion object {
        private const val TAG = "RMDateFilterInfoBottomSheet"
        fun newInstance() = RMDateFilterInfoBottomSheet()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetStcRmDateFilterInfoBinding.inflate(inflater).apply {
            setChild(root)
            setTitle(root.context.getString(R.string.stc_change_date_range))
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setupView() = binding?.run {
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