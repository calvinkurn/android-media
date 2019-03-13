package com.tokopedia.imageuploader.domain;


import com.google.gson.Gson;
import com.tokopedia.imageuploader.domain.model.GenerateHostDomainModel;
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel;
import com.tokopedia.imageuploader.utils.ImageUploaderUtils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 12/19/17.
 */

public class UploadImageUseCase<T> extends UseCase<ImageUploadDomainModel<T>> {

    private static final String PARAM_PATH_UPLOAD = "PATH_UPLOAD";
    private static final String HTTPS = "https://";
    private static final String PARAM_BODY = "PARAM_BODY";
    private static final String PARAM_SERVER_ID = "server_id";
    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_DEVICE_TIME = "device_time";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private final UserSessionInterface userSession;

    private UploadImageRepository uploadImageRepository;
    private GenerateHostRepository generateHostRepository;
    private Gson gson;
    private Class<T> imageUploadResultModel;
    private ImageUploaderUtils imageUploaderUtils;

    public UploadImageUseCase(UploadImageRepository uploadImageRepository,
                              GenerateHostRepository generateHostRepository,
                              Gson gson,
                              UserSessionInterface userSession,
                              Class<T> imageUploadResultModel,
                              ImageUploaderUtils imageUploaderUtils) {
        this.uploadImageRepository = uploadImageRepository;
        this.generateHostRepository = generateHostRepository;
        this.gson = gson;
        this.userSession = userSession;
        this.imageUploadResultModel = imageUploadResultModel;
        this.imageUploaderUtils = imageUploaderUtils;
    }

    @Override
    public Observable<ImageUploadDomainModel<T>> createObservable(final RequestParams requestParams) {
        return generateHostRepository.generateHost()
                .flatMap(new Func1<GenerateHostDomainModel, Observable<ImageUploadDomainModel<T>>>() {
                    @Override
                    public Observable<ImageUploadDomainModel<T>> call(final GenerateHostDomainModel generateHostDomainModel) {
                        return uploadImageRepository
                                .uploadImage(
                                        getParamsUploadImage(
                                                String.valueOf(generateHostDomainModel.getServerId()),
                                                (Map<String, RequestBody>) requestParams.getObject(PARAM_BODY)
                                        ),
                                        generateUploadUrl(requestParams.getString(PARAM_PATH_UPLOAD, ""), generateHostDomainModel.getUrl()))
                                .map(new Func1<String, ImageUploadDomainModel<T>>() {
                                    @Override
                                    public ImageUploadDomainModel<T> call(String s) {
                                        ImageUploadDomainModel<T> imageUploadDomainModel = new ImageUploadDomainModel<T>(imageUploadResultModel);
                                        T dataResultImageUpload = gson.fromJson(s, imageUploadDomainModel.getType());
                                        imageUploadDomainModel.setDataResultImageUpload(dataResultImageUpload);
                                        imageUploadDomainModel.setServerId(String.valueOf(generateHostDomainModel.getServerId()));
                                        imageUploadDomainModel.setUrl(generateHostDomainModel.getUrl());
                                        return imageUploadDomainModel;
                                    }
                                });
                    }
                });
    }

    private Map<String, RequestBody> getParamsUploadImage(String serverIdUpload, Map<String, RequestBody> maps) {
        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), serverIdUpload);
        maps.put(PARAM_SERVER_ID, serverId);
        return maps;
    }

    private String generateUploadUrl(String pathUpload, String urlUpload) {
        return HTTPS + urlUpload + pathUpload;
    }

    public RequestParams createRequestParam(String pathFile, String pathUpload, String attachmentTypeKeyValue, Map<String, RequestBody> paramsUploadImage) {
        File file = new File(pathFile);
        RequestParams requestParams = RequestParams.create();
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userSession.getUserId());
        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"), userSession.getDeviceId());
        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"), imageUploaderUtils.getHash());
        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"), imageUploaderUtils.getDeviceTime());
        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody osType = RequestBody.create(MediaType.parse("text/plain"), "1");
        paramsUploadImage.put(PARAM_USER_ID, userId);
        paramsUploadImage.put(PARAM_DEVICE_ID, deviceId);
        paramsUploadImage.put(PARAM_HASH, hash);
        paramsUploadImage.put(PARAM_DEVICE_TIME, deviceTime);
        paramsUploadImage.put(attachmentTypeKeyValue, fileToUpload);
        paramsUploadImage.put(PARAM_OS_TYPE, osType);
        requestParams.putString(PARAM_PATH_UPLOAD, pathUpload);
        requestParams.putObject(PARAM_BODY, paramsUploadImage);
        return requestParams;
    }
}
