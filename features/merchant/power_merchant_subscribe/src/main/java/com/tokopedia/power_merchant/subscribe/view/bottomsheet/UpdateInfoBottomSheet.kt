package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import kotlinx.android.synthetic.main.bottom_sheet_pm_update_info.view.*

/**
 * Created By @ilhamsuaib on 09/05/21
 */

class UpdateInfoBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "PmUpdateInfoBottomSheet"
        fun createInstance(): UpdateInfoBottomSheet {
            return UpdateInfoBottomSheet()
        }
    }

    override fun getChildResLayout(): Int = R.layout.bottom_sheet_pm_update_info

    override fun setupView() = childView?.run {
        btnPmUpdateInfoBottomSheet.setOnClickListener {
            setOnCtaClicked()
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun setOnCtaClicked() {
        RouteManager.route(context, Constant.Url.PM_PRO_BENEFIT_PACKAGE_EDU)
        dismiss()
    }
}