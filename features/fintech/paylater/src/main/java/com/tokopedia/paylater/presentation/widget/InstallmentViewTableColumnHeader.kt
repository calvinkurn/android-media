package com.tokopedia.paylater.presentation.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.SimulationItemDetail

class InstallmentViewTableColumnHeader(val context: Context, val layoutParams: ViewGroup.LayoutParams) {

    fun getLayout() = R.layout.paylater_simulation_table_column_header

    fun initUI(installmentMap: HashMap<Int, SimulationItemDetail>, position: Int): View {
        val installmentColumnHeader = LayoutInflater.from(context).inflate(getLayout(), null)
        installmentColumnHeader.layoutParams = layoutParams
        if (isOfferApplied(installmentMap, position)) {
            installmentColumnHeader.findViewById<TextView>(R.id.offerLabel).visible()
        } else {
            installmentColumnHeader.findViewById<TextView>(R.id.offerLabel).gone()
            installmentColumnHeader.findViewById<TextView>(R.id.tableHeader).text = "Cicil ${3 * position}x"
        }
        return installmentColumnHeader
    }

    private fun isOfferApplied(installmentMap: HashMap<Int, SimulationItemDetail>, position: Int): Boolean {
        if (position == 0 && installmentMap.containsKey(1)) {
            val interestRate = installmentMap[1]?.interestPercent
            return interestRate == 0.0f
        }
        return false
    }
}