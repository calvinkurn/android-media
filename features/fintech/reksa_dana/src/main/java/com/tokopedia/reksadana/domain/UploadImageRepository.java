package com.tokopedia.reksadana.domain;

import android.content.Context;

import com.tokopedia.reksadana.view.data.submit.Data;
import com.tokopedia.usecase.RequestParams;

import okhttp3.ResponseBody;
import rx.Observable;

public interface UploadImageRepository {

    Observable<ResponseBody> uploadImage(String fileName);
}
