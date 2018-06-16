package com.tokopedia.reksadana.source;

import android.content.Context;

import com.tokopedia.reksadana.domain.UploadImageFactory;
import com.tokopedia.reksadana.domain.UploadImageRepository;

import okhttp3.ResponseBody;
import rx.Observable;

public class UploadImageRepositoryImpl implements UploadImageRepository {
    UploadImageFactory factory;
    String mUrl;
    public UploadImageRepositoryImpl(String url) {
        factory = new UploadImageFactory(url);
        mUrl = url;
    }

    @Override
    public Observable<ResponseBody> uploadImage(String fileName) {
        return factory.createDataSource().uploadImage(mUrl, fileName);
    }
}
