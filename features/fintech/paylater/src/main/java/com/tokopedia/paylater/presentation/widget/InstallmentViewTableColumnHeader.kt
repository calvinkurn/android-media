package com.tokopedia.paylater.presentation.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.paylater.R

class InstallmentViewTableColumnHeader(val context: Context, val layoutParams: ViewGroup.LayoutParams) {

    fun getLayout() = R.layout.paylater_simulation_table_column_header

    fun initUI(isOfferApplied: Boolean): View {
        val installmentColumnHeader = LayoutInflater.from(context).inflate(getLayout(), null)
        installmentColumnHeader.layoutParams = layoutParams
        if (!isOfferApplied) {
            installmentColumnHeader.findViewById<TextView>(R.id.offerLabel).gone()
            installmentColumnHeader.findViewById<TextView>(R.id.tableHeader).text = "Cicil 3x"
        }
        return installmentColumnHeader
    }
}