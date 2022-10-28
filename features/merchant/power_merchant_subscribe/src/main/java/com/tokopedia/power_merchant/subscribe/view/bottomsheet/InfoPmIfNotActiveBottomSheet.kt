package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.BottomSheetPmIfNotActiveInfoBinding

class InfoPmIfNotActiveBottomSheet :
    BaseBottomSheet<BottomSheetPmIfNotActiveInfoBinding>() {

    companion object {
        private const val TAG = "PMFeeServiceBottomSheet"
        fun createInstance(): InfoPmIfNotActiveBottomSheet {
            return InfoPmIfNotActiveBottomSheet().apply {
                showKnob = true
                showCloseIcon = false
            }
        }
    }

    override fun bind(view: View) = BottomSheetPmIfNotActiveInfoBinding.bind(view)

    override fun getChildResLayout(): Int = R.layout.bottom_sheet_pm_if_not_active_info

    override fun setupView() = binding?.run {
        val title = context?.getString(R.string.pm_active_cta_if_pm_not_active).orEmpty()
        setTitle(title)
    }

    fun show(fm: FragmentManager) {
        if (fm.isStateSaved && isVisible) return
        show(fm, TAG)
    }
}