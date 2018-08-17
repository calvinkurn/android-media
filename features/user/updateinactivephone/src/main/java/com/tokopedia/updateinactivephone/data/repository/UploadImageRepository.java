package com.tokopedia.updateinactivephone.data.repository;

import com.tokopedia.updateinactivephone.model.request.UploadImageModel;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

public interface UploadImageRepository {

    Observable<UploadImageModel> uploadImage(String url,
                                             Map<String, String> params,
                                             RequestBody imageFile);

}
