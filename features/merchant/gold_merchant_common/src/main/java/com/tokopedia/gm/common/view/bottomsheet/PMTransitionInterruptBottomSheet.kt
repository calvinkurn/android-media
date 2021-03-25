package com.tokopedia.gm.common.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.gm.common.R
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_gmc_pm_transition_interrupt.view.*

/**
 * Created By @ilhamsuaib on 20/03/21
 */

class PMTransitionInterruptBottomSheet : BottomSheetUnify() {

    companion object {
        const val TAG = "SahPowerMerchantBottomSheet"

        fun getInstance(fm: FragmentManager): PMTransitionInterruptBottomSheet {
            return (fm.findFragmentByTag(TAG) as? PMTransitionInterruptBottomSheet) ?: PMTransitionInterruptBottomSheet().apply {
                clearContentPadding = true
                showHeader = false
                showCloseIcon = false
            }
        }
    }

    private var data: PowerMerchantInterruptUiModel? = null
    private var childView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, com.tokopedia.unifycomponents.R.style.UnifyBottomSheetNotOverlapStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        val view = inflater.inflate(R.layout.bottom_sheet_gmc_pm_transition_interrupt, container, false)
        childView = view
        setChild(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
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

    private fun setupView() = childView?.run {
        if (data == null) {
            dismiss()
            return@run
        }

        data?.let { data ->
            tvSahPmTitle.text = context.getString(R.string.gmc_power_merchant_bottom_sheet_title, data.pmNewUpdateDateFmt)
            slvSahPmShopLevel.show(data.shopLevel)
            tvSahPmShopScore.text = data.shopScore.toString()
            pmgSahPmCard.show(data)

            setShopScoreTextColor()
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