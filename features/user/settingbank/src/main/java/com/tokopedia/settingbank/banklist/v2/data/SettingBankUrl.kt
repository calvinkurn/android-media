package com.tokopedia.settingbank.banklist.v2.data

import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl

class SettingBankUrl {

    companion object {
        var BASE_URL: String = when(TokopediaUrl.getInstance().TYPE){
            Env.STAGING -> "http://10.255.13.187:9000"
            else-> "http://www.tokopedia.com"
        }
        const val PATH_POST_DOCUMENT: String = "/bank-account/document/upload"
    }

}

