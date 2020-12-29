package com.tokopedia.paylater.domain.model

import com.google.gson.annotations.SerializedName


data class PayLaterGetSimulationResponse(
        @SerializedName("paylater_getSimulation")
        val payLaterGetSimulationGateway: PayLaterGetSimulationGateway?
)

data class PayLaterGetSimulationGateway(
        @SerializedName("gateways")
        val payLaterGatewayList: ArrayList<PayLaterSimulationGatewayItem>?
)

data class PayLaterSimulationGatewayItem(
        @SerializedName("gateway_id")
        val gatewayId: Int?,
        @SerializedName("gateway_name")
        val gatewayName: String?,
        @SerializedName("simulation_detail")
        val simulationDetailList: ArrayList<SimulationItemDetail>,
        var installmentMap: HashMap<Int, SimulationItemDetail>
)

data class SimulationItemDetail(
        @SerializedName("installment_per_month_ceil")
        val installmentPerMonth: String?,
        @SerializedName("is_recommended")
        val isRecommended: Boolean?,
        @SerializedName("tenure")
        val tenure: Int?,
        @SerializedName("interest_pct")
        val interestPercent: Float?
)