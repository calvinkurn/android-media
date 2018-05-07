package com.tokopedia.imagepicker.picker.instagram.data;

import com.tokopedia.imagepicker.picker.instagram.data.source.InstagramDataSourceFactory;
import com.tokopedia.imagepicker.picker.instagram.data.model.ResponseListMediaInstagram;
import com.tokopedia.imagepicker.picker.instagram.domain.InstagramRepository;

import rx.Observable;

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
    public Observable<ResponseListMediaInstagram> getListMedia(String token, String nextMaxId, String countPerPage) {
        return instagramDataSourceFactory.createDataSourceInstagramCloud().getListMedia(token, nextMaxId, countPerPage);
    }

    @Override
    public Observable<String> saveAccessToken(String saveAccessToken) {
        return instagramDataSourceFactory.createDataSourceInstagramLocal().saveAccessToken(saveAccessToken);
    }
}
