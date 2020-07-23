package com.tokopedia.tkpd.utils;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GQLPing {
    @GET("ping")
    Call<String> pingGQL ();
}
