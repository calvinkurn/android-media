package com.tokopedia.reksadana.domain;

import android.content.Context;

import com.tokopedia.reksadana.source.UploadImageRepositoryImpl;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import okhttp3.ResponseBody;
import rx.Observable;

public class UploadImageUseCase extends UseCase<ResponseBody> {
    public static final String KEY_FILE_NAME = "KEY_FILE_NAME";
    private UploadImageRepository uploadImageRepository;
    private String url;

    public UploadImageUseCase(Context appContext, String url) {
        this.url = url;
    }

    @Override
    public Observable<ResponseBody> createObservable(RequestParams requestParams) {
        uploadImageRepository = new UploadImageRepositoryImpl(url);

        String fileName = requestParams.getString(KEY_FILE_NAME, "");


        return uploadImageRepository.uploadImage(fileName);
    }
}
