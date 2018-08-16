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
import com.tokopedia.common.network.data.source.cloud.api.RestApi;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Interceptor;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class UploadAWSUseCase extends RestRequestSupportInterceptorUseCase  {

    private final Context context;
    private final Interceptor interceptor;
    private String filePath;
    private String challengeId;
    private RestApi mApi;
    private UploadFingerprints uploadFingerprints;

    @Inject
    public UploadAWSUseCase(IndiAuthInterceptor interceptor,@ApplicationContext Context context) {
        super(interceptor, context);
        this.context = context;
        this.interceptor = interceptor;
    }


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public void setUploadFingerprints(UploadFingerprints uploadFingerprints) {
        this.uploadFingerprints = uploadFingerprints;
    }

    @Override
    protected List<RestRequest> buildRequest() {
        return null;
    }

    @Override
    public Observable<Map<Type, RestResponse>> createObservable(RequestParams requestParams) {
        return createObservableGetNextPart(uploadFingerprints);
    }

    public Observable<Map<Type, RestResponse>> createObservableUploadAWS(UploadFingerprints fingerprints) {
        if(fingerprints.getPartsCompleted() != fingerprints.getTotalParts()) {
            return buildAWSUploadRequest(fingerprints).flatMap(new Func1<Response<String>, Observable<Map<Type, RestResponse>>>() {
                @Override
                public Observable<Map<Type, RestResponse>> call(Response<String> stringResponse) {
                    return createObservableGetNextPart(fingerprints);
                }
            });
        }else {
            Map<Type, RestResponse> results = new HashMap<>();
                RestResponse response = new RestResponse("Upload Success", 200, false);
                results.put(String.class, response);
                return Observable.just(results);

        }

    }


    public Observable<Response<String>> buildAWSUploadRequest(UploadFingerprints fingerprints) {
        List<RestRequest> tempRequest = new ArrayList<>();
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("Authorization", fingerprints.getAuthorization());
        headers.put("x-amz-date", fingerprints.getAuthorizationDate());
        headers.put("Content-Type", "multipart/form-data; charset=UTF-8");
        this.mApi = NetworkClient.getApiInterfaceWithNoInterceptor(context);
        return  mApi.putMultipart(fingerprints.getUploadUrl(), Utils.generateImageRequestBodySlice(filePath,fingerprints.getBlockStart(),fingerprints.getBlockEnd()),headers);
    }

    public Observable<Map<Type, RestResponse>> createObservableGetNextPart(UploadFingerprints fingerprints) {
        return ObservableFactory.create(getNextPartRequest(fingerprints.getManifestId()), Arrays.asList(interceptor), context).doOnNext(new Action1<Map<Type, RestResponse>>() {
            @Override
            public void call(Map<Type, RestResponse> restResponse) {
            }
        }).flatMap(new Func1<Map<Type, RestResponse>, Observable<Map<Type, RestResponse>>>() {
            @Override
            public Observable<Map<Type, RestResponse>> call(Map<Type, RestResponse> restResponse) {
                RestResponse res1 = restResponse.get(UploadFingerprints.class);
                UploadFingerprints fingerprints = res1.getData();
                return createObservableUploadAWS(fingerprints);
            };
        });

    }

    public List<RestRequest> getNextPartRequest(String  manifestID) {
        List<RestRequest> tempRequest = new ArrayList<>();
        HashMap<String, Object> request = new HashMap<>();
        request.put("manifest_id",manifestID);
        RestRequest restRequest1 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + String.format(ChallengesUrl.PRIVATE.Upload.CHALLENGE_GET_NEXT_PART,challengeId), UploadFingerprints.class)
                .setRequestType(RequestType.GET).setQueryParams(request) .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }


}
