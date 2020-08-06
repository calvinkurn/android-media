package com.tokopedia.tkpd.utils

import retrofit2.Call
import retrofit2.http.GET

interface GQLPing {
    @GET("ping")
    fun pingGQL(): Call<String?>?
}