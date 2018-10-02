package com.tokopedia.affiliate.feature.createpost.domain.usecase;

import com.tokopedia.affiliate.feature.createpost.data.pojo.uploadimage.UploadImageData;
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by milhamj on 10/1/18.
 */
public class UploadMultipleImageUseCase extends UseCase<List<String>> {
    private static final String PARAM_URL_LIST = "url_list";

    private final UploadImageUseCase<UploadImageData> uploadImageUseCase;

    @Inject
    UploadMultipleImageUseCase(UploadImageUseCase<UploadImageData> uploadImageUseCase) {
        this.uploadImageUseCase = uploadImageUseCase;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<List<String>> createObservable(RequestParams requestParams) {
        return Observable.from((List<String>) requestParams.getObject(PARAM_URL_LIST))
                .flatMap(uploadSingleImage())
                .toList();
    }

    private Func1<String, Observable<String>> uploadSingleImage() {
        return s -> {
            if (CreatePostViewModel.urlIsFile(s)) {
                return uploadImageUseCase.createObservable(RequestParams.create()).map(mapToUrl());
            } else {
                return Observable.just(s);
            }
        };
    }

    private Func1<ImageUploadDomainModel<UploadImageData>, String> mapToUrl() {
        return uploadDomainModel -> uploadDomainModel.getDataResultImageUpload().getPicSrc();
    }

    public static RequestParams createRequestParams(List<String> urlList) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_URL_LIST, urlList);
        return requestParams;
    }

}
