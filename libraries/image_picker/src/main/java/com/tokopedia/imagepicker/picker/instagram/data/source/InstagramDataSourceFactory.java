package com.tokopedia.imagepicker.picker.instagram.data.source;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.imagepicker.picker.instagram.data.source.InstagramDataSource;
import com.tokopedia.imagepicker.picker.instagram.data.source.cache.InstagramDataSourceLocal;
import com.tokopedia.imagepicker.picker.instagram.data.source.cloud.InstagramApi;
import com.tokopedia.imagepicker.picker.instagram.data.source.cloud.InstagramDataSourceCloud;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 5/4/18.
 */

public class InstagramDataSourceFactory {

    private LocalCacheHandler localCacheHandler;
    private InstagramApi instagramApi;

    @Inject
    public InstagramDataSourceFactory(LocalCacheHandler localCacheHandler, InstagramApi instagramApi) {
        this.localCacheHandler = localCacheHandler;
        this.instagramApi = instagramApi;
    }

    public InstagramDataSourceLocal createDataSourceInstagramLocal() {
        return new InstagramDataSourceLocal(localCacheHandler);
    }

    public InstagramDataSourceCloud createDataSourceInstagramCloud() {
        return new InstagramDataSourceCloud(instagramApi);
    }

    public InstagramDataSource createDataSourceInstagram(String code) {
        if(TextUtils.isEmpty(code)){
            return createDataSourceInstagramLocal();
        }else{
            return createDataSourceInstagramCloud();
        }
    }
}
