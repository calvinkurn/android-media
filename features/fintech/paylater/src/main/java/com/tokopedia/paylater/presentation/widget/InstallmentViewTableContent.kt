package com.tokopedia.paylater.presentation.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.paylater.R
import com.tokopedia.paylater.data.mapper.EmptyInstallment
import com.tokopedia.paylater.data.mapper.PayLaterSimulationResponseMapper
import com.tokopedia.paylater.data.mapper.PayLaterSimulationTenureType
import com.tokopedia.paylater.domain.model.SimulationItemDetail
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil

class InstallmentViewTableContent(val context: Context, val layoutParams: ViewGroup.LayoutParams) {

    fun getLayout() = R.layout.paylater_simulation_table_content

    fun initUI(installmentMap: HashMap<PayLaterSimulationTenureType, SimulationItemDetail>, tenureList: Array<Int>, row: Int, col: Int): View {
        val installmentView = LayoutInflater.from(context).inflate(getLayout(), null)
        installmentView.layoutParams = layoutParams
        if (showBackground(row))
            installmentView.setBackgroundColor(ContextCompat.getColor(context, R.color.Unify_N50))
        val tvInstallmentPrice = installmentView.findViewById<Typography>(R.id.tvInstallmentPrice)

        tvInstallmentPrice.text = getInstallmentText(tenureList[col], installmentMap)
        if (isRecommendedInstallment(row)) tvInstallmentPrice.setWeight(Typography.BOLD)
        return installmentView
    }

    private fun isRecommendedInstallment(row: Int): Boolean {
        return row == 1
    }

    private fun showBackground(row: Int): Boolean {
        return row % 2 == 0
    }

    private fun getInstallmentText(tenure: Int, installmentMap: HashMap<PayLaterSimulationTenureType, SimulationItemDetail>): String {
        val tenureType = PayLaterSimulationResponseMapper.getSimulationTenureType(tenure)
        return if (tenureType != EmptyInstallment
                && installmentMap.containsKey(tenureType)
                && installmentMap[tenureType]?.installmentPerMonth?.isNotEmpty() == true) {

            val priceString = installmentMap[tenureType]?.installmentPerMonth?.toIntOrNull() ?: -1
            val isInterestZeroPercent = installmentMap[tenureType]?.interestPercent == 0f
            if (priceString != -1 && isInterestZeroPercent) "${CurrencyFormatUtil.convertPriceValueToIdrFormat(priceString, false)}*"
            else if (priceString != -1) CurrencyFormatUtil.convertPriceValueToIdrFormat(priceString, false)
            else "-"
        } else "-"
    }
}