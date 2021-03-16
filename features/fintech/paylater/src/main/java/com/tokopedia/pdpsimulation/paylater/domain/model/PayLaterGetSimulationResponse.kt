package com.tokopedia.pdpsimulation.paylater.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.pdpsimulation.paylater.mapper.PayLaterSimulationTenureType


data class PayLaterGetSimulationResponse(
        @SerializedName("paylater_getSimulation")
        val payLaterGetSimulationGateway: PayLaterGetSimulationGateway?,
)

data class PayLaterGetSimulationGateway(
        @SerializedName("gateways")
        val payLaterGatewayList: ArrayList<PayLaterSimulationGatewayItem>?,
)

data class PayLaterSimulationGatewayItem(
        @SerializedName("gateway_id")
        val gatewayId: Int?,
        @SerializedName("gateway_name")
        val gatewayName: String?,
        @SerializedName("img_light_url")
        val imgLightUrl: String?,
        @SerializedName("img_dark_url")
        val imgDarkUrl: String?,
        @SerializedName("simulation_detail")
        val simulationDetailList: ArrayList<SimulationItemDetail>?,
        // To have a map of tenure to installment item aiding in setting in table
        var installmentMap: HashMap<PayLaterSimulationTenureType, SimulationItemDetail>,
        var isRecommended: Boolean,
)

data class SimulationItemDetail(
        @SerializedName("installment_per_month_ceil")
        val installmentPerMonth: String?,
        @SerializedName("is_recommended")
        val isRecommended: Boolean?,
        @SerializedName("tenure")
        val tenure: Int?,
        @SerializedName("interest_pct")
        val interestPercent: Float?,
)