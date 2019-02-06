package com.tokopedia.flight.search.data.db.mapper

import com.tokopedia.flight.airline.data.cloud.model.AirlineData
import com.tokopedia.flight_dbflow.FlightAirlineDB

/**
 * Created by Rizky on 25/10/18.
 */
class FlightAirlineDataMapper {

    fun map(airlineData: AirlineData) : FlightAirlineDB {
        return FlightAirlineDB(airlineData.id, airlineData.attributes.fullName, airlineData.attributes.shortName,
                airlineData.attributes.logo, if (airlineData.attributes.isMandatoryDob) 1 else 0,
                if (airlineData.attributes.isMandatoryRefundAttachment) 1 else 0)
    }

}