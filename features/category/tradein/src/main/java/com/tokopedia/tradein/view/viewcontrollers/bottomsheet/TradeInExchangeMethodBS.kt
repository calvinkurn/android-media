package com.tokopedia.tradein.view.viewcontrollers.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.TradeInDetailModel.GetTradeInDetailData.LogisticOption
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class TradeInExchangeMethodBS : BottomSheetUnify() {
    private var contentView: View? = null

    companion object {
        private const val LOGISTIC_OPTIONS = "LOGISTIC_OPTIONS"
        private const val IS_3PL_SELECTED = "IS_3PL_SELECTED"

        fun newInstance(
            logisticOption: ArrayList<LogisticOption>,
            is3PLSelected: Boolean
        ): TradeInExchangeMethodBS {
            return TradeInExchangeMethodBS().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(LOGISTIC_OPTIONS, logisticOption)
                    putBoolean(IS_3PL_SELECTED, is3PLSelected)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun init() {
        showCloseIcon = true
        showKnob = false
        setTitle(getString(com.tokopedia.tradein.R.string.tradein_pilih_method))
        contentView = View.inflate(
            context,
            R.layout.tradein_exchange_method_bottomsheet, null
        )
        contentView?.apply {
            arguments?.getBoolean(IS_3PL_SELECTED, false)?.let {
                findViewById<IconUnify>(R.id.tradein_p1_tick)?.let { icon ->
                    if (it) icon.hide() else icon.show()
                }
                findViewById<IconUnify>(R.id.tradein_p3_tick)?.let { icon ->
                    if (it) icon.show() else icon.hide()
                }
            }
            arguments?.getParcelableArrayList<LogisticOption>(LOGISTIC_OPTIONS)?.let {
                for (logistic in it) {
                    if (logistic.is3PL) {
                        findViewById<Typography>(R.id.tradein_p1_price).let { typography ->
                            typography.text =
                                if (logistic.isDiagnosed)
                                    logistic.diagnosticPriceFmt
                                else
                                    logistic.estimationPriceFmt
                            typography.setTextColor(
                                MethodChecker.getColor(
                                    context,
                                    if (logistic.isDiagnosed)
                                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                                    else
                                        com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                                )
                            )
                        }
                        findViewById<Typography>(R.id.tradein_p1_info).text = logistic.subTitle
                        findViewById<Typography>(R.id.tradein_p1).text = logistic.title
                    } else {
                        findViewById<Typography>(R.id.tradein_p3_price).let { typography ->
                            typography.text =
                                if (logistic.isDiagnosed)
                                    logistic.diagnosticPriceFmt
                                else
                                    logistic.estimationPriceFmt
                            typography.setTextColor(
                                MethodChecker.getColor(
                                    context,
                                    if (logistic.isDiagnosed)
                                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                                    else
                                        com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                                )
                            )
                        }
                        findViewById<Typography>(R.id.tradein_p3_info).text = logistic.subTitle
                        findViewById<Typography>(R.id.tradein_p3).text = logistic.title

                    }
                }
            }
        }
        setChild(contentView)
    }

}