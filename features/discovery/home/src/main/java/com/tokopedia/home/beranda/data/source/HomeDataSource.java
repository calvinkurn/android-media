package com.tokopedia.home.beranda.data.source;

import com.tokopedia.home.common.HomeAceApi;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeDataSource {
    private HomeAceApi homeAceApi;

    public HomeDataSource(HomeAceApi homeAceApi) {
        this.homeAceApi = homeAceApi;
    }

    public Observable<Response<String>> sendGeolocationInfo() {
        return homeAceApi.sendGeolocationInfo();
    }

}
