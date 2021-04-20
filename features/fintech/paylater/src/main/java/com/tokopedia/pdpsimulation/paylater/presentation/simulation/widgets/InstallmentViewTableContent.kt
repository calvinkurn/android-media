package com.tokopedia.pdpsimulation.paylater.presentation.simulation.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.SimulationItemDetail
import com.tokopedia.pdpsimulation.paylater.mapper.EmptyInstallment
import com.tokopedia.pdpsimulation.paylater.mapper.PayLaterSimulationResponseMapper
import com.tokopedia.pdpsimulation.paylater.mapper.PayLaterSimulationTenureType
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil

class InstallmentViewTableContent(val context: Context, private val layoutParams: ViewGroup.LayoutParams) {

    private fun getLayout() = R.layout.paylater_simulation_table_content

    fun initUI(installmentMap: HashMap<PayLaterSimulationTenureType, SimulationItemDetail>, tenureList: Array<Int>, isRecommendedOption: Boolean, col: Int): View {
        val installmentView = LayoutInflater.from(context).inflate(getLayout(), null)
        installmentView.layoutParams = layoutParams
        val tvInstallmentPrice = installmentView.findViewById<Typography>(R.id.tvContent)
        tvInstallmentPrice.text = getInstallmentText(tenureList[col], installmentMap)
        if (isRecommendedOption) tvInstallmentPrice.setWeight(Typography.BOLD)
        return installmentView
    }

    private fun getInstallmentText(tenure: Int, installmentMap: HashMap<PayLaterSimulationTenureType, SimulationItemDetail>): String {
        val tenureType = PayLaterSimulationResponseMapper.getSimulationTenureType(tenure)
        return if (tenureType != EmptyInstallment
                && installmentMap.containsKey(tenureType)
                && installmentMap[tenureType]?.installmentPerMonth?.isNotEmpty() == true) {

            val priceString = installmentMap[tenureType]?.installmentPerMonth?.toIntOrNull() ?: 0
            val isInterestZeroPercent = installmentMap[tenureType]?.interestPercent == 0f
            if (priceString != 0 && isInterestZeroPercent) "${CurrencyFormatUtil.convertPriceValueToIdrFormat(priceString, false)}*"
            else if (priceString != 0) CurrencyFormatUtil.convertPriceValueToIdrFormat(priceString, false)
            else "-"
        } else "-"
    }
}