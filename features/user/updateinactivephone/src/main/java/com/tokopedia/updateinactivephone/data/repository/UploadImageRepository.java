package com.tokopedia.updateinactivephone.data.repository;

import com.tokoepdia.updateinactivephone.model.response.ResponseUploadImage;
import com.tokopedia.updateinactivephone.model.request.UploadImageModel;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

public interface UploadImageRepository {

    Observable<UploadImageModel> uploadImage(String url,
                                             Map<String, RequestBody> params,
                                             RequestBody imageFile);


}
