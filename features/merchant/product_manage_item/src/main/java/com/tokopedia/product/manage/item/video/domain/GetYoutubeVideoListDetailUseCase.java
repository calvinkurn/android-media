package com.tokopedia.product.manage.item.video.domain;

import android.content.Context;

import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailRxUseCase;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by hendry on 26/06/18.
 */

public class GetYoutubeVideoListDetailUseCase extends UseCase<List<Map<Type, RestResponse>>> {
    public static final String ID_LIST = "id_list";

    private GetYoutubeVideoDetailRxUseCase getYoutubeVideoDetailUseCase;

    public GetYoutubeVideoListDetailUseCase(Context context) {
        this.getYoutubeVideoDetailUseCase = new GetYoutubeVideoDetailRxUseCase(context);
    }

    @Override
    public Observable<List<Map<Type, RestResponse>>> createObservable(RequestParams requestParams) {
        return Observable.from(getVideoIdList(requestParams))
                .concatMap((Func1<String, Observable<Map<Type, RestResponse>>>) videoId -> getYoutubeVideoDetailUseCase.createObservable(
                        GetYoutubeVideoDetailRxUseCase.Companion.generateRequestParam(videoId)))
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
