package com.tokopedia.challenges.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.data.IndiAuthInterceptor;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.model.upload.UploadFingerprints;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.data.ObservableFactory;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.usecase.RequestParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class IntializeMultiPartUseCase extends RestRequestSupportInterceptorUseCase {

    private static final String FILE_NAME = "file_name";
    private static final String FILE_SIZE = "file_size";
    private static final String IMAGE_TITLE = "title";
    private static final String IMAGE_DESCRIPTION = "description";
    private static final String BLOB_DATA = "blob_data";
    private final IndiAuthInterceptor interceptor;
    HashMap<String, Object> requestBodyMap = new HashMap<>();
    private String challengeID;
    Context context;
    String imagePath;



    public void setCHALLENGE_ID(String challengeID) {
        this.challengeID = challengeID;
    }

    @Inject
    public IntializeMultiPartUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
        this.context = context;
        this.interceptor = interceptor;
    }


    @Override
    public Observable<Map<Type, RestResponse>> createObservable(RequestParams requestParams) {
        return super.createObservable(requestParams).flatMap(new Func1<Map<Type, RestResponse>, Observable<Map<Type, RestResponse>>>() {
            @Override
            public Observable<Map<Type, RestResponse>> call(Map<Type, RestResponse> restResponse) {
                RestResponse res1 = restResponse.get(UploadFingerprints.class);
                UploadFingerprints fingerprints = res1.getData();
                UploadAWSUseCase uploadAWSUseCase = new UploadAWSUseCase(interceptor,context,imagePath,challengeID);
                return uploadAWSUseCase.createObservableUploadAWS(fingerprints);
            }
        });
    }




    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();

        RestRequest restRequest1 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + String.format(ChallengesUrl.PRIVATE.Upload.CHALLENGE_INTIALIZE_MULTIPART,challengeID), UploadFingerprints.class)
                .setRequestType(RequestType.POST_MULTIPART).setBody(requestBodyMap) .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }



    public void generateRequestParams(String title, String description, String imagePath) {
        this.imagePath = imagePath;
       requestBodyMap.put(IMAGE_TITLE, Utils.generateRequestPlainTextBody(title));
        requestBodyMap.put(IMAGE_DESCRIPTION, Utils.generateRequestPlainTextBody(description));
        File imageFile = new File(imagePath);
        requestBodyMap.put(FILE_NAME, Utils.generateRequestPlainTextBody(imageFile.getName()));
        requestBodyMap.put(FILE_SIZE, Utils.generateRequestPlainTextBody(String.valueOf(imageFile.length())));
        requestBodyMap.put(BLOB_DATA, Utils.generateRequestBlobBody(Utils.get10KBFile(imagePath)));
    }


}
