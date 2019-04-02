package com.tokopedia.product.manage.item.video.domain;

import android.content.Context;

import com.tokopedia.common.network.data.ObservableFactory;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.product.manage.item.video.domain.model.youtube.YoutubeVideoModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import rx.Observable;

/**
 * Created by hendry on 26/06/18.
 */

public class GetYoutubeVideoDetailUseCase extends UseCase<Map<Type, RestResponse>> {
    public static final String YOUTUBE_API_KEY = "AIzaSyADrnEdJGwsVM1Z6uWWnWAgZZf1sSfnIVQ";
    public static final String YOUTUBE_LINK = "https://www.googleapis.com/youtube/v3/videos";
    public static final String PART = "part";
    public static final String ID = "id";
    public static final String KEY = "key";
    public static final String SNIPPET_CONTENT_DETAILS = "snippet,contentDetails";

    private Context context;

    public GetYoutubeVideoDetailUseCase(Context context) {
        this.context = context;
    }

    @Override
    public Observable<Map<Type, RestResponse>> createObservable(RequestParams requestParams) {
        ArrayList<Interceptor> interceptorArrayList = new ArrayList<>();
        interceptorArrayList.add(new HttpLoggingInterceptor());
        return ObservableFactory.create(
                getRequests(getVideoId(requestParams)),
                interceptorArrayList,
                context);
    }

    private List<RestRequest> getRequests(String videoId) {
        return new ArrayList<>(buildRequest(videoId));
    }

    private List<RestRequest> buildRequest(String videoId) {
        List<RestRequest> tempRequest = new ArrayList<>();

        RestRequest restRequest1 = new RestRequest.Builder(YOUTUBE_LINK, YoutubeVideoModel.class)
                .setQueryParams(generateParam(videoId))
                .setRequestType(RequestType.GET)
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;

    }

    private Map<String, Object> generateParam(String videoId) {
        Map<String, Object> param = new HashMap<>();
        param.put(PART, SNIPPET_CONTENT_DETAILS);
        param.put(ID, videoId);
        param.put(KEY, YOUTUBE_API_KEY);
        return param;
    }

    public static RequestParams generateRequestParam(String videoId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ID, videoId);
        return requestParams;
    }

    private static String getVideoId(RequestParams requestParams){
        return requestParams.getString(ID, "");
    }
}
