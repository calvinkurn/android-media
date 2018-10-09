package com.tokopedia.tkpd.tkpdreputation.uploadimage.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.GenerateHostDomain;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.UploadImageDomain;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * @author by nisie on 9/5/17.
 */

public interface ImageUploadRepository {

    Observable<GenerateHostDomain> generateHost(RequestParams parameters);

    Observable<UploadImageDomain> uploadImage(String url,
                                              Map<String, RequestBody> params,
                                              RequestBody imageFile);
}
