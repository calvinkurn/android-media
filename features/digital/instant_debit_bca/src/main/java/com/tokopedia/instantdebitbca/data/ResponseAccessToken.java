package com.tokopedia.instantdebitbca.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 22/03/19.
 */
public class ResponseAccessToken {
    @SerializedName("token")
    @Expose
    private TokenEntity tokenBca;

    public TokenEntity getTokenBca() {
        return tokenBca;
    }
}
