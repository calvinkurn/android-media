package com.tokopedia.product.edit.domain;

import android.content.Context;

import com.tokopedia.networklib.data.ObservableFactory;
import com.tokopedia.networklib.data.model.RequestType;
import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponse;
import com.tokopedia.product.edit.model.youtube.YoutubeVideoModel;
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
import rx.functions.Func1;

/**
 * Created by hendry on 26/06/18.
 */

public class GetYoutubeVideoListDetailUseCase extends UseCase<List<Map<Type, RestResponse>>> {
    public static final String ID_LIST = "id_list";

    private GetYoutubeVideoDetailUseCase getYoutubeVideoDetailUseCase;

    public GetYoutubeVideoListDetailUseCase(Context context) {
        this.getYoutubeVideoDetailUseCase = new GetYoutubeVideoDetailUseCase(context);
    }

    @Override
    public Observable<List<Map<Type, RestResponse>>> createObservable(RequestParams requestParams) {
        return Observable.from(getVideoIdList(requestParams))
                .concatMap((Func1<String, Observable<Map<Type, RestResponse>>>) videoId -> getYoutubeVideoDetailUseCase.createObservable(
                        GetYoutubeVideoDetailUseCase.generateRequestParam(videoId)))
                .toList();
    }


    public static RequestParams generateRequestParam(ArrayList<String> videoIdList){
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(ID_LIST, videoIdList);
        return requestParams;
    }

    @SuppressWarnings("unchecked")
    private static ArrayList<String> getVideoIdList(RequestParams requestParams){
        return (ArrayList<String>) requestParams.getObject(ID_LIST);
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        getYoutubeVideoDetailUseCase.unsubscribe();
    }
}
