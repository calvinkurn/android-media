package com.tokopedia.network.interceptor.akamai

import java.io.IOException

class AkamaiErrorException(message: String): IOException(message)