package com.tokopedia.imagepicker.picker.instagram.data.source.cache;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.imagepicker.picker.instagram.data.source.exception.ShouldLoginInstagramException;
import com.tokopedia.imagepicker.picker.instagram.data.source.InstagramDataSource;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 5/4/18.
 */

public class InstagramDataSourceLocal implements InstagramDataSource {

    public static final String ACCESS_TOKEN_INSTAGRAM = "ACCESS_TOKEN_INSTAGRAM";
    private LocalCacheHandler localCacheHandler;

    public InstagramDataSourceLocal(LocalCacheHandler localCacheHandler) {
        this.localCacheHandler = localCacheHandler;
    }

    public Observable<String> getAccessToken(String code) {
        return Observable.just(code).flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                String accessToken =  localCacheHandler.getString(ACCESS_TOKEN_INSTAGRAM, "");
                if(TextUtils.isEmpty(accessToken)){
                    throw new ShouldLoginInstagramException();
                }else {
                    return Observable.just(accessToken);
                }
            }
        });
    }

    public Observable<String> saveAccessToken(String saveAccessToken) {
        localCacheHandler.putString(ACCESS_TOKEN_INSTAGRAM, saveAccessToken);
        localCacheHandler.applyEditor();
        return Observable.just(saveAccessToken);
    }
}
