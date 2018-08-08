package com.tokopedia.product.edit.common.data.repository;

import com.tokopedia.product.edit.common.data.source.UploadImageDataSource;
import com.tokopedia.product.edit.common.domain.UploadImageRepository;
import com.tokopedia.product.edit.common.domain.UploadImageRepository;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by zulfikarrahman on 12/19/17.
 */

public class UploadImageRepositoryImpl implements UploadImageRepository {

    private final UploadImageDataSource uploadImageDataSource;

    public UploadImageRepositoryImpl(UploadImageDataSource uploadImageDataSource) {
        this.uploadImageDataSource = uploadImageDataSource;
    }

    @Override
    public Observable<String> uploadImage(Map<String, RequestBody> params, String urlUploadImage) {
        return uploadImageDataSource.uploadImage(params, urlUploadImage);
    }
}
