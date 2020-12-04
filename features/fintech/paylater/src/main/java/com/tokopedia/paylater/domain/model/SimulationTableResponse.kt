package com.tokopedia.paylater.domain.model

data class SimulationTableResponse(
        val partnerUrl: String,
        val installmentData: ArrayList<String>
)