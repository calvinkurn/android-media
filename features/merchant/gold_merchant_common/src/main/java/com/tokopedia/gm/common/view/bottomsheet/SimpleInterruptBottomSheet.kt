package com.tokopedia.gm.common.view.bottomsheet

import androidx.fragment.app.FragmentManager
import com.tokopedia.gm.common.R

/**
 * Created By @ilhamsuaib on 08/04/21
 */

class SimpleInterruptBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "GmcSimpleInterruptBottomSheet"

        fun createInstance(): SimpleInterruptBottomSheet {
            return SimpleInterruptBottomSheet()
        }
    }

    override fun getResLayout(): Int = R.layout.bottom_sheet_gmc_pm_simple_interrupt

    override fun setupView() = childView?.run {

    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}