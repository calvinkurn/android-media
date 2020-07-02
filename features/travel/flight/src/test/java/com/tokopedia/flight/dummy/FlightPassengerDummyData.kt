package com.tokopedia.flight.dummy

import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.usecase.coroutines.Success

/**
 * @author by furqan on 24/06/2020
 */
val DUMMY_PASSENGER_DATA = arrayListOf<TravelContactListModel.Contact>(
        TravelContactListModel.Contact(
                "dummy UUID",
                "dummy Type",
                "dummy Title ID",
                "dummy title",
                "dummy short title",
                "Passenger First",
                "Passenger Last",
                "",
                "male",
                "2020-11-11",
                "ID",
                62,
                "8989898989",
                "email@email.com",
                arrayListOf()
        ),
        TravelContactListModel.Contact(
                "dummy UUID",
                "dummy Type",
                "dummy Title ID",
                "dummy title",
                "dummy short title",
                "Passenger First",
                "Passenger Last",
                "Passenger Full Name",
                "male",
                "2020-11-11",
                "ID",
                62,
                "8989898989",
                "email@email.com",
                arrayListOf()
        )
)

val DUMMY_NATIONALITY_SUCCESS = Success(TravelCountryPhoneCode("ID", "Indonesia", 62))