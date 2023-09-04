package com.tokopedia.flight.homepage.presentation.util

import com.tokopedia.common_digital.common.presentation.model.DigitalDppoConsent
import com.tokopedia.flight.homepage.presentation.model.FlightDppoConsentModel
import com.tokopedia.kotlin.extensions.view.ZERO

object FlightHomepageMapper {

    fun mapDppoConsentToFlightModel(data: DigitalDppoConsent): FlightDppoConsentModel {
        return FlightDppoConsentModel(
            description = data.persoData.items.getOrNull(Int.ZERO)?.title ?: ""
        )
    }
}
