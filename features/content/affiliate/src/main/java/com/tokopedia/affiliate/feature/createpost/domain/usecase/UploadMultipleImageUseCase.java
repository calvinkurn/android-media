package com.tokopedia.affiliate.feature.createpost.domain.usecase;

import com.tokopedia.affiliate.feature.createpost.data.pojo.uploadimage.UploadImageResponse;
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by milhamj on 10/1/18.
 */
public class UploadMultipleImageUseCase extends UseCase<List<String>> {
    private static final String PARAM_URL_LIST = "url_list";
    private static final String PARAM_ID = "id";
    private static final String PARAM_WEB_SERVICE = "web_service";
    private static final String PARAM_RESOLUTION = "param_resolution";
    private static final String DEFAULT_WEB_SERVICE = "1";
    private static final String DEFAULT_UPLOAD_PATH = "/upload/attachment";
    private static final String DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg";
    private static final String DEFAULT_RESOLUTION = "100-square";
    private static final String RESOLUTION_500 = "500";
    private static final String TEXT_PLAIN = "text/plain";

    private final UploadImageUseCase<UploadImageResponse> uploadImageUseCase;
    private final UserSession userSession;

    @Inject
    UploadMultipleImageUseCase(UploadImageUseCase<UploadImageResponse> uploadImageUseCase,
                               UserSession userSession) {
        this.uploadImageUseCase = uploadImageUseCase;
        this.userSession = userSession;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<List<String>> createObservable(RequestParams requestParams) {
        return Observable.from((List<String>) requestParams.getObject(PARAM_URL_LIST))
                .flatMap(uploadSingleImage())
                .toList();
    }

    private Func1<String, Observable<String>> uploadSingleImage() {
        return url -> {
            if (CreatePostViewModel.urlIsFile(url)) {
                return uploadImageUseCase.createObservable(createUploadParams(url)).map(mapToUrl());
            } else {
                return Observable.just(url);
            }
        };
    }

    private Func1<ImageUploadDomainModel<UploadImageResponse>, String> mapToUrl() {
        return uploadDomainModel -> {
            String imageUrl = uploadDomainModel.getDataResultImageUpload().getData().getPicSrc();
            if (imageUrl != null && imageUrl.contains(DEFAULT_RESOLUTION)) {
                imageUrl = imageUrl.replaceFirst(DEFAULT_RESOLUTION, RESOLUTION_500);
            }
            return imageUrl;
        };
    }

    private RequestParams createUploadParams(String fileLocation) {
        Map<String, RequestBody> maps = new HashMap<>();
        RequestBody webService = RequestBody.create(
                MediaType.parse(TEXT_PLAIN),
                DEFAULT_WEB_SERVICE
        );
        RequestBody resolution = RequestBody.create(
                MediaType.parse(TEXT_PLAIN),
                RESOLUTION_500
        );
        RequestBody id = RequestBody.create(
                MediaType.parse(TEXT_PLAIN),
                userSession.getUserId() + UUID.randomUUID() + System.currentTimeMillis()
        );
        maps.put(PARAM_WEB_SERVICE, webService);
        maps.put(PARAM_ID, id);
        maps.put(PARAM_RESOLUTION, resolution);
        return uploadImageUseCase.createRequestParam(
                fileLocation,
                DEFAULT_UPLOAD_PATH,
                DEFAULT_UPLOAD_TYPE,
                maps
        );
    }

    public static RequestParams createRequestParams(List<String> urlList) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_URL_LIST, urlList);
        return requestParams;
    }
}
