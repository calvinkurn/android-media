package com.tokopedia.paylater.presentation.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.SimulationItemDetail
import com.tokopedia.unifyprinciples.Typography

class InstallmentViewTableContent(val context: Context, val layoutParams: ViewGroup.LayoutParams) {

    fun getLayout() = R.layout.paylater_simulation_table_content

    fun initUI(installmentMap: HashMap<Int, SimulationItemDetail>, tenureList: Array<Int>, row: Int, col: Int): View {
        val installmentView = LayoutInflater.from(context).inflate(getLayout(), null)
        installmentView.layoutParams = layoutParams
        if (showBackground(row))
            installmentView.setBackgroundColor(ContextCompat.getColor(context, R.color.Unify_N50))
        val tvInstallmentPrice = installmentView.findViewById<Typography>(R.id.tvInstallmentPrice)

        tvInstallmentPrice.text = getInstallmentText(tenureList[col], installmentMap)
        if(isRecommendedInstallment(row)) tvInstallmentPrice.setWeight(Typography.BOLD)
        return installmentView
    }

    private fun isRecommendedInstallment(row: Int): Boolean {
        return row == 1
    }

    private fun showBackground(row : Int): Boolean {
        return row % 2 == 0
    }

    private fun getInstallmentText(tenure: Int, installmentMap: HashMap<Int, SimulationItemDetail>): String {
        return if (installmentMap.containsKey(tenure)) {
            "Rp${installmentMap[tenure]?.installmentPerMonth}"
        }  else "-"
    }
}