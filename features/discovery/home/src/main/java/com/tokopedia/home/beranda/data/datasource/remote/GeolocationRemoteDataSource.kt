package com.tokopedia.home.beranda.data.datasource.remote

import com.tokopedia.home.common.HomeAceApi
import retrofit2.Response
import rx.Observable

class GeolocationRemoteDataSource (private val homeAceApi: HomeAceApi){
    fun sendGeolocationInfo(): Observable<Response<String>> {
        return homeAceApi.sendGeolocationInfo()
    }
}