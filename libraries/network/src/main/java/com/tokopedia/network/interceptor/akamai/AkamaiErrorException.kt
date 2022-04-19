package com.tokopedia.network.interceptor.akamai

import java.io.IOException

/*
   * 21 August 2020
   * Duplicated from Akamai Bot Interceptor Library for composite network lib to accommodate Refresh Token usecase only
 */

class AkamaiErrorException(message: String): IOException(message)