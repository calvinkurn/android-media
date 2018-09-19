package com.tokopedia.product.manage.item.common.data.source;

import com.tokopedia.product.manage.item.common.data.source.cloud.UploadImageDataSourceCloud;
import com.tokopedia.product.manage.item.common.data.source.cloud.UploadImageDataSourceCloud;

import java.util.Map;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by zulfikarrahman on 12/19/17.
 */

public class UploadImageDataSource {
    private UploadImageDataSourceCloud uploadImageDataSourceCloud;

    @Inject
    public UploadImageDataSource(UploadImageDataSourceCloud uploadImageDataSourceCloud) {
        this.uploadImageDataSourceCloud = uploadImageDataSourceCloud;
    }

    public Observable<String> uploadImage(Map<String, RequestBody> params, String urlUploadImage) {
        return uploadImageDataSourceCloud.uploadImage(params, urlUploadImage);
    }
}
