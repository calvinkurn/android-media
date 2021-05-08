package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import androidx.fragment.app.FragmentManager
import com.tokopedia.power_merchant.subscribe.R
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
        //todo : open seller edu
        dismiss()
    }
}