package com.tokopedia.gm.common.view.bottomsheet

import androidx.fragment.app.FragmentManager
import com.tokopedia.gm.common.R
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import com.tokopedia.kotlin.extensions.view.getResColor
import kotlinx.android.synthetic.main.bottom_sheet_gmc_pm_transition_interrupt.view.*

/**
 * Created By @ilhamsuaib on 20/03/21
 */

class PMTransitionInterruptBottomSheet : BaseBottomSheet() {

    companion object {
        const val TAG = "GmcPowerMerchantBottomSheet"

        fun getInstance(fm: FragmentManager): PMTransitionInterruptBottomSheet {
            return (fm.findFragmentByTag(TAG) as? PMTransitionInterruptBottomSheet)
                    ?: PMTransitionInterruptBottomSheet().apply {
                        clearContentPadding = true
                        showHeader = false
                        showCloseIcon = false
                        overlayClickDismiss = false
                    }
        }
    }

    private var ctaClickCallback: (() -> Unit)? = null
    private var data: PowerMerchantInterruptUiModel? = null

    override fun getResLayout(): Int = R.layout.bottom_sheet_gmc_pm_transition_interrupt

    override fun setupView() = childView?.run {
        if (data == null) {
            dismiss()
            return@run
        }

        data?.let { data ->
            tvSahPmTitle.text = context.getString(R.string.gmc_power_merchant_bottom_sheet_title, PMConstant.TRANSITION_PERIOD_START_DATE)
            slvSahPmShopLevel.show(data.shopLevel)
            pmgSahPmCard.show(data)

            val shopScore = if (data.shopScore <= 0) "-" else data.shopScore.toString()
            tvSahPmShopScore.text = shopScore

            setShopScoreTextColor()
        }

        btnGmcCheckShopPerformance.setOnClickListener {
            ctaClickCallback?.invoke()
            dismiss()
        }
    }

    fun setOnCtaClickListener(callback: () -> Unit): PMTransitionInterruptBottomSheet {
        ctaClickCallback = callback
        return this
    }

    fun setData(data: PowerMerchantInterruptUiModel): PMTransitionInterruptBottomSheet {
        this.data = data
        return this
    }

    fun show(fm: FragmentManager) {
        data?.let {
            show(fm, TAG)
        }
    }

    private fun setShopScoreTextColor() = childView?.run {
        data?.let {
            val textColor = if (it.shopScore >= it.shopScoreThreshold) {
                context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N700)
            } else {
                context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_R600)
            }
            tvSahPmShopScore.setTextColor(textColor)
        }
    }
}