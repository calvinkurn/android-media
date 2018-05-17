package com.tokopedia.imagepicker.picker.instagram.data;

import com.tokopedia.imagepicker.picker.instagram.data.source.InstagramDataSourceFactory;
import com.tokopedia.imagepicker.picker.instagram.data.model.ResponseListMediaInstagram;
import com.tokopedia.imagepicker.picker.instagram.domain.InstagramRepository;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 5/4/18.
 */

public class InstagramRepositoryImpl implements InstagramRepository {

    private InstagramDataSourceFactory instagramDataSourceFactory;

    public InstagramRepositoryImpl(InstagramDataSourceFactory instagramDataSourceFactory) {
        this.instagramDataSourceFactory = instagramDataSourceFactory;
    }

    @Override
    public Observable<String> getAccessToken(String code) {
        return instagramDataSourceFactory.createDataSourceInstagram(code).getAccessToken(code);
    }

    @Override
    public Observable<ResponseListMediaInstagram> getListMedia(final String token, final String nextMaxId, final String countPerPage) {
        return instagramDataSourceFactory.createDataSourceInstagramLocal().getInstagramCookies().flatMap(new Func1<String, Observable<ResponseListMediaInstagram>>() {
            @Override
            public Observable<ResponseListMediaInstagram> call(String cookie) {
                return instagramDataSourceFactory.createDataSourceInstagramCloud().getListMedia(cookie, token, nextMaxId, countPerPage);
            }
        });

    }

    @Override
    public Observable<String> saveAccessToken(String saveAccessToken) {
        return instagramDataSourceFactory.createDataSourceInstagramLocal().saveAccessToken(saveAccessToken);
    }

    @Override
    public Observable<String> saveCookies(String cookies) {
        return instagramDataSourceFactory.createDataSourceInstagramLocal().saveInstagramCookies(cookies);
    }
}
