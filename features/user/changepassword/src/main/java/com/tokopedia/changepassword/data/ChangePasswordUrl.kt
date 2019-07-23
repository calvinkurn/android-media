package com.tokopedia.changepassword.data

import com.tokopedia.url.TokopediaUrl

/**
 * @author by nisie on 7/25/18.
 */
class ChangePasswordUrl {


    companion object {

        var BASE_URL: String = TokopediaUrl.getInstance().ACCOUNTS
        const val PATH_CHANGE_PASSWORD: String = "api/v1/change-password"
    }

}