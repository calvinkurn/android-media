package com.tokopedia.sessioncommon.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 10/11/17.
 * Used by LoginService.java (InstrumentationAuthHelper)
 */
class TokenViewModel {
    /**
     * access_token : 2YotnFZFEjr1zCsicMWpAA
     * token_type : example
     * expires_in : 3600
     * refresh_token : tGzv3JOkF0XG5Qx2TlKWIA
     * error : access_denied
     * error_description : The resource owner or authorization server denied the request.
     */
    @SerializedName("access_token")
    @Expose
    var accessToken = ""

    @SerializedName("token_type")
    @Expose
    var tokenType = ""

    @SerializedName("expires_in")
    @Expose
    var expiresIn = 0

    @SerializedName("refresh_token")
    @Expose
    var refreshToken = ""

    @SerializedName("scope")
    @Expose
    var scope = ""
}
