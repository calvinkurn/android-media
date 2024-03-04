package com.tokopedia.sessioncommon.network

import java.io.IOException

/**
 * @author by nisie on 10/22/18.
 */
class TokenErrorException(val error: String, val errorDescription: String, errorKey: String) : IOException("$errorDescription ( $errorKey )")
