package com.tokopedia.pdpsimulation.paylater.presentation.simulation.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterSimulationGatewayItem
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

class NoRecommendationViewTableRowHeader(val context: Context, val layoutParams: ViewGroup.LayoutParams) {

    private fun getLayout() = R.layout.paylater_simulation_table_row_header

    fun initUI(simulationDataItem: PayLaterSimulationGatewayItem, showBackGround: Boolean): View {
        val rowHeaderNoRecom = LayoutInflater.from(context).inflate(getLayout(), null)
        rowHeaderNoRecom.layoutParams = layoutParams
        val ivPayLaterPartner = rowHeaderNoRecom.findViewById<ImageView>(R.id.ivPaylaterPartner)
        val imageUrl: String?
        if (context.isDarkMode())
            imageUrl = simulationDataItem.imgDarkUrl
        else imageUrl = simulationDataItem.imgLightUrl
        if (!imageUrl.isNullOrEmpty())
            ivPayLaterPartner.loadImage(imageUrl)

        if (showBackGround)
            rowHeaderNoRecom.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N50))
        else rowHeaderNoRecom.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))

        val recomBadge = rowHeaderNoRecom.findViewById<ImageView>(R.id.ivRecommendationBadge)
        recomBadge.gone()
        return rowHeaderNoRecom
    }
}