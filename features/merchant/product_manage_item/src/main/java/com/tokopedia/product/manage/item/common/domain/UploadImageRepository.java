package com.tokopedia.product.manage.item.common.domain;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by zulfikarrahman on 12/19/17.
 */

public interface UploadImageRepository {
    Observable<String> uploadImage(Map<String, RequestBody> params, String urlUploadImage);
}
