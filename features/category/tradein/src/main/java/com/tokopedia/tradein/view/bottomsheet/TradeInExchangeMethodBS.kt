package com.tokopedia.tradein.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tradein.R
import com.tokopedia.tradein.TradeInAnalytics
import com.tokopedia.tradein.model.TradeInDetailModel.GetTradeInDetail.LogisticOption
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography

class TradeInExchangeMethodBS : BottomSheetUnify() {
    private var contentView: View? = null
    private var onLogisticSelected: OnLogisticSelected? = null
    var tradeInAnalytics: TradeInAnalytics? = null

    companion object {
        private const val LOGISTIC_OPTIONS = "LOGISTIC_OPTIONS"
        private const val LOGISTIC_MESSAGE = "LOGISTIC_MESSAGE"
        private const val IS_3PL_SELECTED = "IS_3PL_SELECTED"

        fun newInstance(
            logisticOption: ArrayList<LogisticOption>,
            is3PLSelected: Boolean,
            logisticMessage : String
        ): TradeInExchangeMethodBS {
            return TradeInExchangeMethodBS().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(LOGISTIC_OPTIONS, logisticOption)
                    putBoolean(IS_3PL_SELECTED, is3PLSelected)
                    putString(LOGISTIC_MESSAGE, logisticMessage)
                }
            }
        }
    }

    fun setOnLogisticSelected(onLogisticSelected : OnLogisticSelected){
        this.onLogisticSelected = onLogisticSelected
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
            arguments?.getString(LOGISTIC_MESSAGE, getString(R.string.tradein_exchange_ticker))?.apply {
                findViewById<Ticker>(R.id.ticker_address_info).setTextDescription(this)
            }
            arguments?.getParcelableArrayList<LogisticOption>(LOGISTIC_OPTIONS)?.let {
                if(it.firstOrNull()?.is3PL == false) {
                    tradeInAnalytics?.impressionExchangeMethod(it[0].isAvailable, it[0].estimatedPriceFmt, it[1].isAvailable, it[1].estimatedPriceFmt)
                } else {
                    tradeInAnalytics?.impressionExchangeMethod(it[1].isAvailable, it[1].estimatedPriceFmt, it[0].isAvailable, it[0].estimatedPriceFmt)
                }
                for (logistic in it) {
                    if (!logistic.is3PL) {
                        isNot3PL(logistic, this, it)
                    } else {
                        is3PL(logistic, this, it)
                    }
                }
            }
        }
        setChild(contentView)
    }

    private fun is3PL(
        logistic: LogisticOption,
        view: View,
        it: java.util.ArrayList<LogisticOption>
    ) {
        view.apply {
            logistic.isAvailable.let { available ->
                arguments?.getBoolean(IS_3PL_SELECTED, false)?.let { is3PLSelected->
                    findViewById<IconUnify>(R.id.tradein_p3_tick)?.let { icon ->
                        when {
                            !available -> icon.hide()
                            is3PLSelected -> icon.show()
                            else -> icon.hide()
                        }
                    }
                }
                if(available) {
                    findViewById<View>(R.id.tradein_p3_view).setOnClickListener { click->
                        if(it.firstOrNull()?.is3PL == false) {
                            tradeInAnalytics?.clickExchangeMethods(true, it[0].estimatedPriceFmt, it[1].estimatedPriceFmt)
                        } else {
                            tradeInAnalytics?.clickExchangeMethods(false, it[1].estimatedPriceFmt,  it[0].estimatedPriceFmt)
                        }
                        onLogisticSelected?.onLogisticSelected(true)
                        dismiss()
                    }
                }
                findViewById<Typography>(R.id.tradein_p3_price).let { typography ->
                    typography.text =
                        if (logistic.isDiagnosed)
                            logistic.diagnosticPriceFmt
                        else
                            logistic.estimatedPriceFmt
                    typography.setTextColor(
                        MethodChecker.getColor(
                            context,
                            when {
                                !available -> com.tokopedia.unifyprinciples.R.color.Unify_NN400
                                logistic.isDiagnosed -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
                                else -> com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
                            }
                        )
                    )
                }
                findViewById<Typography>(R.id.tradein_p3_info).text = logistic.subtitle
                setTextColour(findViewById(R.id.tradein_p3_info), available)
                findViewById<Typography>(R.id.tradein_p3).text = logistic.title
                setTextColour(findViewById(R.id.tradein_p3), available)
            }
        }
    }

    private fun isNot3PL(
        logistic: LogisticOption,
        view: View,
        logisticsOptions: java.util.ArrayList<LogisticOption>
    ) {
        view.apply {
            logistic.isAvailable.let { available ->
                arguments?.getBoolean(IS_3PL_SELECTED, false)?.let { is3PLSelected ->
                    findViewById<IconUnify>(R.id.tradein_p1_tick)?.let { icon ->
                        when {
                            !available -> icon.hide()
                            is3PLSelected -> icon.hide()
                            else -> icon.show()
                        }
                    }
                }
                if (available) {
                    findViewById<View>(R.id.tradein_p1_view).setOnClickListener { click ->
                        if (logisticsOptions.firstOrNull()?.is3PL == false) {
                            tradeInAnalytics?.clickExchangeMethods(
                                false,
                                logisticsOptions[0].estimatedPriceFmt,
                                logisticsOptions[1].estimatedPriceFmt
                            )
                        } else {
                            tradeInAnalytics?.clickExchangeMethods(
                                true,
                                logisticsOptions[1].estimatedPriceFmt,
                                logisticsOptions[0].estimatedPriceFmt
                            )
                        }
                        onLogisticSelected?.onLogisticSelected(false)
                        dismiss()
                    }
                }
                findViewById<Typography>(R.id.tradein_p1_price).let { typography ->
                    typography.text =
                        if (logistic.isDiagnosed)
                            logistic.diagnosticPriceFmt
                        else
                            logistic.estimatedPriceFmt
                    typography.setTextColor(
                        MethodChecker.getColor(
                            context,
                            when {
                                !available -> com.tokopedia.unifyprinciples.R.color.Unify_NN400
                                logistic.isDiagnosed -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
                                else -> com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
                            }
                        )
                    )
                }
                findViewById<Typography>(R.id.tradein_p1_info).text = logistic.subtitle
                setTextColour(findViewById(R.id.tradein_p1_info), available)
                findViewById<Typography>(R.id.tradein_p1).text = logistic.title
                setTextColour(findViewById(R.id.tradein_p1), available)
            }
        }
    }

    private fun setTextColour(typography: Typography?, available: Boolean) {
        typography?.setTextColor(MethodChecker.getColor(
            context,
            when {
                !available -> com.tokopedia.unifyprinciples.R.color.Unify_NN400
                else -> com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
            }
        ))
    }

    interface OnLogisticSelected {
        fun onLogisticSelected(is3Pl : Boolean)
    }

}