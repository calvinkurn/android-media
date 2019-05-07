package com.tokopedia.sessioncommon.network

import android.util.Base64

/**
 * @author by nisie on 07/05/19.
 */
class BasicTokenGenerator{

    companion object{
        val TOKEN_TYPE = "Basic"
    }

    fun createToken() : String{
        val clientID = "7ea919182ff"
        val clientSecret = "b36cbf904d14bbf90e7f25431595a364"
        val encodeString = "$clientID:$clientSecret"

        val asB64 = Base64.encodeToString(encodeString.toByteArray(), Base64.NO_WRAP)
        return asB64
    }
}