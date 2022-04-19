package com.tokopedia.flight.common.data.model

import com.tokopedia.network.exception.MessageErrorException

/**
 * @author by furqan on 09/06/2021
 */
class FlightException(
        message: String,
        val errorList: List<FlightError> = arrayListOf())
    : MessageErrorException(message)