package com.tokopedia.updateinactivephone.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.updateinactivephone.model.request.UploadHostModel;
import com.tokopedia.updateinactivephone.model.request.UploadImageModel;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

public interface UploadImageRepository {

    Observable<UploadImageModel> uploadImage(String url,
                                             Map<String, String> params,
                                             RequestBody imageFile);

    Observable<UploadHostModel> getUploadHost(TKPDMapParam<String, Object> parameters);

}
