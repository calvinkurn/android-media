package com.tokopedia.hotel.common.data

/**
 * @author by jessica on 2020-03-18
 */

class HotelErrorException(val errorCode: Int, override val message: String) : Exception(message)