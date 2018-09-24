package com.tokopedia.updateinactivephone.usecase;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneURL;
import com.tokopedia.updateinactivephone.data.repository.UploadImageRepositoryImpl;
import com.tokopedia.updateinactivephone.model.request.UploadImageModel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.ID;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.IMAGE_UPLOAD_URL;
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
        String uploadUrl = "https://" + requestParams.getString(IMAGE_UPLOAD_URL, "") + "/upload/attachment";

        return uploadImageRepository.uploadImage(uploadUrl,
                generateRequestBody(requestParams),
                getUploadImageFile(requestParams)
        );
    }

    private Map<String, String> generateRequestBody(com.tokopedia.core.base.domain.RequestParams requestParams) {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put(USERID, requestParams.getString(USERID, ""));
        requestBodyMap.put(ID,
                requestParams.getString(USERID, ""));
        requestBodyMap.put(SERVER_ID, requestParams.getString(SERVER_ID, "49"));
        requestBodyMap.put(TOKEN, requestParams.getString(TOKEN, ""));
        requestBodyMap.put(RESOLUTION, requestParams.getString(RESOLUTION, "215"));

        return requestBodyMap;
    }

    private RequestBody getUploadImageFile(com.tokopedia.core.base.domain.RequestParams requestParams) {
        File file = null;
        try {
            file = ImageUploadHandler.writeImageToTkpdPath(
                    ImageUploadHandler.compressImage(requestParams.getString(PARAM_FILE_TO_UPLOAD, ""))
            );
        } catch (Exception e) {
            throw new RuntimeException(MainApplication.getAppContext().getString(R.string.error_upload_image));
        }
        return RequestBody.create(MediaType.parse("image/*"),
                file);
    }
}
