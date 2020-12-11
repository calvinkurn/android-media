package com.tokopedia.paylater.presentation.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.paylater.R
import com.tokopedia.unifyprinciples.Typography

class InstallmentViewTableContent(val context: Context, val layoutParams: ViewGroup.LayoutParams) {

    fun getLayout() = R.layout.paylater_simulation_table_content

    fun initUI(priceText: String, showBackground: Boolean, isRecommendedInstallment: Boolean): View {
        val installmentView = LayoutInflater.from(context).inflate(getLayout(), null)
        installmentView.layoutParams = layoutParams
        if (showBackground)
            installmentView.setBackgroundColor(ContextCompat.getColor(context, R.color.Unify_N50))
        val tvInstallmentPrice = installmentView.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tvInstallmentPrice)
        tvInstallmentPrice.text = priceText
        if(isRecommendedInstallment) tvInstallmentPrice.setWeight(Typography.BOLD)
        return installmentView
    }
}