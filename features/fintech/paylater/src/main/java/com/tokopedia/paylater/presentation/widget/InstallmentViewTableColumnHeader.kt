package com.tokopedia.paylater.presentation.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.paylater.R

class InstallmentViewTableColumnHeader(val context: Context, val layoutParams: ViewGroup.LayoutParams) {

    fun getLayout() = R.layout.paylater_simulation_table_column_header

    fun initUI(position: Int): View {
        val installmentColumnHeader = LayoutInflater.from(context).inflate(getLayout(), null)
        installmentColumnHeader.layoutParams = layoutParams
        if (position == 0) installmentColumnHeader.findViewById<TextView>(R.id.tableHeader).text = context.getString(R.string.paylater_column_header_one_month)
        else installmentColumnHeader.findViewById<TextView>(R.id.tableHeader).text = "Cicil ${3 * position}x"
        return installmentColumnHeader
    }
}