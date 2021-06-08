package com.tokopedia.flight.common.constant_kotlin

import com.tokopedia.url.TokopediaUrl

/**
 * @author by furqan on 08/06/2021
 */
object FlightUrl {

    private const val FLIGHT_PATH_V11 = "travel/v1.1/flight/"
    private const val FLIGHT_CANCELLATION_PATH_V11 = FLIGHT_PATH_V11 + "cancel/"
    const val FLIGHT_CANCELLATION_UPLOAD = FLIGHT_CANCELLATION_PATH_V11 + "upload"
    private const val CONTACT_US_FLIGHT_HOME_PREFIX = "contact-us?pid=97&flag_app=1&device=android&utm_source=android"
    val BASE_URL = TokopediaUrl.getInstance().API
    val WEB_DOMAIN = TokopediaUrl.getInstance().WEB
    val CONTACT_US_FLIGHT = WEB_DOMAIN + CONTACT_US_FLIGHT_HOME_PREFIX

    const val FLIGHT_HOMEPAGE_HELP_URL = "https://www.tokopedia.com/help/article/pertanyaan-seputar-tiket-pesawat"
    const val FLIGHT_PASSENGER_HELP_URL = "https://www.tokopedia.com/help/article/saya-tidak-dapat-mengisi-nama-lengkap-penumpang-sesuai-dengan-kartu-identitas-pada-kolom-nama-penumpang-apa-yang-harus-saya-lakukan"
    const val FLIGHT_PROMO_APPLINK = "tokopedia://promoNative?menuID=4&categoryID=615"

}