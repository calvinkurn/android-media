package com.tokopedia.logout.data

import com.tokopedia.config.url.TokopediaUrl

/**
 * @author by nisie on 5/30/18.
 */
class LogoutUrl {

    companion object {
        var BASE_URL: String = TokopediaUrl.getInstance().WS
        const val PATH_LOGOUT: String = "v4/session/logout.pl"
    }
}