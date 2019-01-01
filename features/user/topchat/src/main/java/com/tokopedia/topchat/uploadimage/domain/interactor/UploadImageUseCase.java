package com.tokopedia.topchat.uploadimage.domain.interactor;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.topchat.common.util.ImageUploadHandlerChat;
import com.tokopedia.topchat.uploadimage.data.repository.ImageUploadRepository;
import com.tokopedia.topchat.uploadimage.domain.model.UploadImageDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * @author by nisie on 9/5/17.
 */

public class UploadImageUseCase extends UseCase<UploadImageDomain> {

    public static final String PARAM_GENERATED_HOST = "PARAM_GENERATED_HOST";
    public static final String PARAM_FILE_TO_UPLOAD = "PARAM_FILE_TO_UPLOAD";

    public static final String PARAM_IMAGE_ID = "id";
    private static final String PARAM_WEB_SERVICE = "web_service";

    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";

    public static final String PARAM_SERVER_ID = "server_id";
    public static final String PARAM_TOKEN = "token";
    public static final String PARAM_URL = "url";

    private static final String HTTP = "http://";
    private static final String UPLOAD_ATTACHMENT = "/upload/attachment";
    private final UserSessionInterface userSession;

    private ImageUploadRepository imageUploadRepository;

    @Inject
    public UploadImageUseCase(ImageUploadRepository imageUploadRepository, UserSessionInterface
            userSession) {
        super();
        this.imageUploadRepository = imageUploadRepository;
        this.userSession = userSession;
    }

    @Override
    public Observable<UploadImageDomain> createObservable(RequestParams requestParams) {
        return imageUploadRepository.uploadImage(generateUrl(requestParams),
                generateRequestBody(requestParams),
                generateImage(requestParams)
        );
    }

    private String generateUrl(RequestParams requestParams) {
        return HTTP
                + requestParams.getString(PARAM_GENERATED_HOST, "")
                + UPLOAD_ATTACHMENT;
    }

    private Map<String, RequestBody> generateRequestBody(RequestParams requestParams) {
        //TODO REPLACE
        Map<String, String> paramsMap = AuthUtil.generateParams(
                requestParams.getString(PARAM_USER_ID,
                        userSession.getUserId()),
                requestParams.getString(PARAM_DEVICE_ID,
                        userSession.getDeviceId()),
                new HashMap<String, String>()
        );

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_USER_ID,
                        paramsMap.get(PARAM_USER_ID)));
        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_DEVICE_ID,
                        paramsMap.get(PARAM_DEVICE_ID)));
        RequestBody osType = RequestBody.create(MediaType.parse("text/plain"),
                paramsMap.get(PARAM_OS_TYPE));

        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"),
                paramsMap.get(PARAM_HASH));
        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"),
                paramsMap.get(PARAM_TIMESTAMP));
        RequestBody imageId = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_IMAGE_ID, ""));
        RequestBody webservice = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_WEB_SERVICE, "1"
                ));
        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"),
                String.valueOf(requestParams.getString(PARAM_SERVER_ID, "")));

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put(PARAM_USER_ID, userId);
        requestBodyMap.put(PARAM_DEVICE_ID, deviceId);
        requestBodyMap.put(PARAM_OS_TYPE, osType);
        requestBodyMap.put(PARAM_HASH, hash);
        requestBodyMap.put(PARAM_TIMESTAMP, deviceTime);
        requestBodyMap.put(PARAM_IMAGE_ID, imageId);
        requestBodyMap.put(PARAM_WEB_SERVICE, webservice);
        requestBodyMap.put(PARAM_SERVER_ID, serverId);

        return requestBodyMap;
    }

    private RequestBody generateImage(RequestParams requestParams) {
        File file = null;
        try {
            byte[] temp = ImageUploadHandlerChat.compressImage(requestParams.getString(PARAM_FILE_TO_UPLOAD, ""));
            if (ImageUploadHandlerChat.checkSizeOverLimit(temp.length, 5)) {
                throw new MessageErrorException("Gambar melebihi 5MB");
            }

            file = ImageUploadHandlerChat.writeImageToTkpdPath(temp);

        } catch (IOException e) {
            throw new RuntimeException("Gagal mengunggah gambar");
        }
        return RequestBody.create(MediaType.parse("image/*"),
                file);
    }

    public static RequestParams getParam(RequestParams requestParams, String uploadHost,
                                         String imageId, String fileLoc, String serverId) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_GENERATED_HOST, uploadHost);
        params.putString(PARAM_FILE_TO_UPLOAD, fileLoc);
        params.putString(PARAM_SERVER_ID, serverId);
        String userId = requestParams.getString(PARAM_USER_ID, "");
        params.putString(UploadImageUseCase.PARAM_USER_ID, userId);
        params.putString(UploadImageUseCase.PARAM_IMAGE_ID, String.format("%s%s", userId, imageId));
        params.putString(UploadImageUseCase.PARAM_DEVICE_ID,
                requestParams.getString(PARAM_DEVICE_ID, ""));
        params.putString(UploadImageUseCase.PARAM_WEB_SERVICE, "1");
        params.putString(UploadImageUseCase.PARAM_IMAGE_ID, imageId);
        return params;
    }
}
