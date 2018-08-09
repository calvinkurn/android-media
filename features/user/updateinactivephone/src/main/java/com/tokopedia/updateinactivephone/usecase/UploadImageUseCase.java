package com.tokopedia.updateinactivephone.usecase;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.data.repository.UploadImageRepositoryImpl;
import com.tokopedia.updateinactivephone.model.request.UploadImageModel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.ID;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.PARAM_DEVICE_ID;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.PARAM_FILE_TO_UPLOAD;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.RESOLUTION;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.SERVER_ID;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.TOKEN;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.USERID;

public class UploadImageUseCase extends UseCase<UploadImageModel> {

    private UploadImageRepositoryImpl uploadImageRepository;

    public UploadImageUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              UploadImageRepositoryImpl uploadImageRepository) {
        super(threadExecutor, postExecutionThread);
        this.uploadImageRepository = uploadImageRepository;
    }

    @Override
    public Observable<UploadImageModel> createObservable(com.tokopedia.core.base.domain.RequestParams requestParams) {
        String url = "https://up-staging.tokopedia.net/upload/attachment";

        return uploadImageRepository.uploadImage(url,
                generateRequestBody(requestParams),
                getUploadImageFile(requestParams)
        );
    }

    private Map<String, RequestBody> generateRequestBody(com.tokopedia.core.base.domain.RequestParams requestParams) {
        Map<String, String> paramsMap = AuthUtil.generateParams(
                requestParams.getString(USERID,
                        SessionHandler.getLoginID(MainApplication.getAppContext())),
                requestParams.getString(PARAM_DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())),
                new HashMap<String, String>()
        );

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(USERID,
                        paramsMap.get(USERID)));
        /*RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_DEVICE_ID,
                        paramsMap.get(PARAM_DEVICE_ID)));*/

        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"),
                String.valueOf(requestParams.getString(SERVER_ID, "")));
        /*RequestBody imageId = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_IMAGE_ID, ""));*/
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(TOKEN, ""
                ));

        RequestBody resolution = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(RESOLUTION, ""
                ));

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put(USERID, userId);
        requestBodyMap.put(ID, userId);
//        requestBodyMap.put(PARAM_DEVICE_ID, deviceId);
        requestBodyMap.put(SERVER_ID, serverId);
//        requestBodyMap.put(PARAM_IMAGE_ID, imageId);
//        requestBodyMap.put(PARAM_FILE_TO_UPLOAD, imageId);
        requestBodyMap.put(TOKEN, token);
        requestBodyMap.put(RESOLUTION, resolution);

        return requestBodyMap;
    }

    private RequestBody getUploadImageFile(com.tokopedia.core.base.domain.RequestParams requestParams) {
        File file = null;
        try {
            file = ImageUploadHandler.writeImageToTkpdPath(
                    ImageUploadHandler.compressImage(requestParams.getString(PARAM_FILE_TO_UPLOAD, ""))
            );
        } catch (IOException e) {
            throw new RuntimeException(MainApplication.getAppContext().getString(R.string.error_upload_image));
        }
        return RequestBody.create(MediaType.parse("image/*"),
                file);
    }
}
