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

class PMFinalInterruptBottomSheet : BottomSheetUnify() {

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

    private var childView: View? = null
    private var data: PowerMerchantInterruptUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, com.tokopedia.unifycomponents.R.style.UnifyBottomSheetNotOverlapStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
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

    private fun setupView() = childView?.run {
        data?.let { data ->
            if (data.shopScore < data.shopScoreThreshold) {
                cardContainerGmcPotentialGrade.setBackgroundResource(R.drawable.bg_gmc_pm_final_card_risk)
            } else {
                cardContainerGmcPotentialGrade.setBackgroundResource(R.drawable.bg_gmc_pm_final_card)
            }

            tvGmcPmFinalTitle.text = context.getString(R.string.gmc_power_merchant_bottom_sheet_title, data.pmNewUpdateDateFmt)
        }
    }

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        val view = inflater.inflate(R.layout.bottom_sheet_gmc_pm_final_interrupt, container, false)
        childView = view
        setChild(view)
    }
}