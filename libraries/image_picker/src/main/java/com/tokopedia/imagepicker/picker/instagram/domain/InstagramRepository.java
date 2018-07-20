package com.tokopedia.imagepicker.picker.instagram.domain;

import com.tokopedia.imagepicker.picker.instagram.data.model.ResponseListMediaInstagram;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/3/18.
 */

public interface InstagramRepository {
    Observable<String> getAccessToken(String code);

    Observable<ResponseListMediaInstagram> getListMedia(String token, String nextMaxId, String countPerPage);

    Observable<String> saveAccessToken(String saveAccessToken);

    Observable<String> saveCookies(String cookies);
}
