package com.tokopedia.pdpsimulation.paylater.presentation.simulation.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.pdpsimulation.R
import com.tokopedia.unifyprinciples.Typography

class InstallmentViewTableColumnHeader(val context: Context, val layoutParams: ViewGroup.LayoutParams) {

    fun getLayout() = R.layout.paylater_simulation_table_content

    fun initUI(position: Int): View {
        val installmentColumnHeader = LayoutInflater.from(context).inflate(getLayout(), null)
        installmentColumnHeader.layoutParams = layoutParams
        installmentColumnHeader.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N50))
        val headerTextView = installmentColumnHeader.findViewById<Typography>(R.id.tvContent)
        headerTextView.setType(Typography.HEADING_5)
        headerTextView.setWeight(Typography.BOLD)
        if (position == 0) headerTextView.text = context.getString(R.string.pay_later_column_header_one_month)
        else headerTextView.text = "Cicil ${3 * position}x"
        return installmentColumnHeader
    }
}