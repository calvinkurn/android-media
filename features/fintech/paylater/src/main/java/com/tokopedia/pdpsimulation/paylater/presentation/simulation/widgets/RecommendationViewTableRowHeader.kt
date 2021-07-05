package com.tokopedia.pdpsimulation.paylater.presentation.simulation.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterSimulationGatewayItem
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

class RecommendationViewTableRowHeader(val context: Context, val layoutParams: ViewGroup.LayoutParams) {

    private fun getLayout() = R.layout.paylater_simulation_table_row_header

    fun initUI(simulationDataItem: PayLaterSimulationGatewayItem, showBackGround: Boolean): View {
        val recommendationView = LayoutInflater.from(context).inflate(getLayout(), null)
        recommendationView.layoutParams = layoutParams
        val ivPayLaterPartner = recommendationView.findViewById<ImageView>(R.id.ivPaylaterPartner)
        val imageUrl: String?
        if (context.isDarkMode())
            imageUrl = simulationDataItem.imgDarkUrl
        else imageUrl = simulationDataItem.imgLightUrl

        if (!imageUrl.isNullOrEmpty())
            ivPayLaterPartner.loadImage(imageUrl)

        if (showBackGround)
            recommendationView.background = ContextCompat.getDrawable(context, R.drawable.ic_paylater_bg_grey_green_border)
        else recommendationView.background = ContextCompat.getDrawable(context, R.drawable.ic_paylater_bg_white_green_border)
        return recommendationView
    }
}