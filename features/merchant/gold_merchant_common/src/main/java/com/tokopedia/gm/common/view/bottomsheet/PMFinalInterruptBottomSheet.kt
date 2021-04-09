package com.tokopedia.gm.common.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.gm.common.R
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_gmc_pm_final_interrupt.view.*

/**
 * Created By @ilhamsuaib on 24/03/21
 */

class PMFinalInterruptBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "GmcPMFinalInterruptBottomSheet"
        fun getInstance(fm: FragmentManager): PMFinalInterruptBottomSheet {
            return (fm.findFragmentByTag(TAG) as? PMFinalInterruptBottomSheet)
                    ?: PMFinalInterruptBottomSheet().apply {
                        clearContentPadding = true
                        showCloseIcon = false
                        showHeader = false
                        isDragable = true
                        showKnob = true
                        isHideable = true
                        isSkipCollapseState = true
                    }
        }
    }

    private var data: PowerMerchantInterruptUiModel? = null

    override fun getResLayout(): Int = R.layout.bottom_sheet_gmc_pm_final_interrupt

    override fun setupView() = childView?.run {
        data?.let { data ->
            if (data.shopScore < data.shopScoreThreshold) {
                cardContainerGmcPotentialGrade.setBackgroundResource(R.drawable.bg_gmc_pm_final_card_risk)
            } else {
                cardContainerGmcPotentialGrade.setBackgroundResource(R.drawable.bg_gmc_pm_final_card)
            }

            tvGmcPmFinalTitle.text = context.getString(R.string.gmc_power_merchant_bottom_sheet_title, data.pmNewUpdateDateFmt)
        }
    }

    fun setData(data: PowerMerchantInterruptUiModel): PMFinalInterruptBottomSheet {
        this.data = data
        return this
    }

    fun show(fm: FragmentManager) {
        data?.let {
            show(fm, TAG)
        }
    }
}