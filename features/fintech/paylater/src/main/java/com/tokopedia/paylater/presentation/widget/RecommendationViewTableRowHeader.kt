package com.tokopedia.paylater.presentation.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.PayLaterSimulationGatewayItem

class RecommendationViewTableRowHeader(val context: Context, val layoutParams: ViewGroup.LayoutParams) {

    fun getLayout() = R.layout.paylater_simulation_table_row_header

    fun initUI(simulationDataItem: PayLaterSimulationGatewayItem): View {
        val recommendationView = LayoutInflater.from(context).inflate(getLayout(), null)
        recommendationView.layoutParams = layoutParams
        recommendationView.background = ContextCompat.getDrawable(context, R.drawable.ic_paylater_green_border)
        val ivPayLaterPartner = recommendationView.findViewById<ImageView>(R.id.ivPaylaterPartner)
        var partnerLogo = "https://ecs7.tokopedia.net/assets-fintech-frontend/pdp/kredivo/kredivo.png"

        if (simulationDataItem.imgLightUrl.isNullOrEmpty()) {
            partnerLogo = simulationDataItem.imgLightUrl ?: ""
        }
        ImageHandler.loadImage(context,
                ivPayLaterPartner,
                partnerLogo,
                R.drawable.ic_loading_image)
        return recommendationView
    }
}