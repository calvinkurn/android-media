package com.tokopedia.logout.data

/**
 * @author by nisie on 5/30/18.
 */
class LogoutUrl {

    companion object {
        var BASE_URL: String = "https://ws.tokopedia.com/"
        const val PATH_LOGOUT: String = "v4/session/logout.pl"
    }
}