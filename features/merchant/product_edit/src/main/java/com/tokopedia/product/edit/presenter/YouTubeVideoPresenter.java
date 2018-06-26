package com.tokopedia.product.edit.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.networklib.data.model.RestResponse;
import com.tokopedia.product.edit.R;
import com.tokopedia.product.edit.domain.GetYoutubeVideoDetailUseCase;
import com.tokopedia.product.edit.domain.GetYoutubeVideoListDetailUseCase;
import com.tokopedia.product.edit.model.videorecommendation.VideoRecommendationData;
import com.tokopedia.product.edit.model.videorecommendation.VideoRecommendationResult;
import com.tokopedia.product.edit.model.youtube.YoutubeVideoModel;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by hendry on 25/06/18.
 */

public class YouTubeVideoPresenter extends BaseDaggerPresenter<YouTubeVideoPresenter.GetVideoRecommendationView> {

    public static final String QUERY = "query";
    public static final String SIZE = "size";
    private final GraphqlUseCase graphqlUseCase;
    private GetYoutubeVideoListDetailUseCase getYoutubeVideoListDetailUseCase;

    public interface GetVideoRecommendationView extends CustomerView {
        Context getContext();

        void onSuccessGetVideoRecommendation(List<VideoRecommendationData> videoRecommendationDataList);

        void onErrorGetVideoRecommendation(Throwable e);

        void onSuccessGetVideoDetailList(ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList);

        void onErrorGetVideoDetailList(Throwable e);
    }

    public YouTubeVideoPresenter() {
        graphqlUseCase = new GraphqlUseCase();
    }

    public void getVideoRecommendation(String query, int size) {

        Map<String, Object> variables = new HashMap<>();
        variables.put(QUERY, query);
        variables.put(SIZE, size);

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getContext().getResources(),
                R.raw.gql_video_recommendation), VideoRecommendationResult.class, variables);

        graphqlUseCase.setRequest(graphqlRequest);

        graphqlUseCase.execute(RequestParams.EMPTY, new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().onErrorGetVideoRecommendation(e);
                }
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                if (isViewAttached()) {
                    VideoRecommendationResult data = objects.getData(VideoRecommendationResult.class);
                    getView().onSuccessGetVideoRecommendation(data.getVideoSearch().getData());
                }
            }
        });
    }

    private void initYouTubeVideoDetailListUseCase() {
        if (getYoutubeVideoListDetailUseCase == null) {
            getYoutubeVideoListDetailUseCase = new GetYoutubeVideoListDetailUseCase(getView().getContext());
        }
    }

    private YoutubeVideoModel convertToModel(Map<Type, RestResponse> typeRestResponseMap) {
        RestResponse restResponse = typeRestResponseMap.get(YoutubeVideoModel.class);
        return restResponse.getData();
    }

    public void getVideoDetail(ArrayList<String> youtubeVideoIdList) {
        initYouTubeVideoDetailListUseCase();
        getYoutubeVideoListDetailUseCase.execute(GetYoutubeVideoListDetailUseCase.generateRequestParam(youtubeVideoIdList),
                new Subscriber<List<Map<Type, RestResponse>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().onErrorGetVideoDetailList(e);
                        }
                    }

                    @Override
                    public void onNext(List<Map<Type, RestResponse>> maps) {
                        if (isViewAttached()) {
                            ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = new ArrayList<>();
                            for (Map<Type, RestResponse> map: maps){
                                youtubeVideoModelArrayList.add(convertToModel(map));
                            }
                            getView().onSuccessGetVideoDetailList(youtubeVideoModelArrayList);
                        }
                    }
                });
    }

    // TODO remove
    // just for test
    private List<VideoRecommendationData> getMockUpData() {
        Type type = new TypeToken<VideoRecommendationResult>() {
        }.getType();
        // get json string which already cached
        String jsonCachedString = "{\"videosearch\":{\"data\":[{\"id\":\"4Y7bqswecUA\",\"title\":\"tas hp\"},{\"id\":\"1dO58V-KWmI\",\"title\":\"Tas Ransel Keren\"},{\"id\":\"zFDM0IvFd98\",\"title\":\"Tas Armany  grosir tas tajur blogspot com\"}],\"error\":null}}";
        VideoRecommendationResult videoRecommendationResult = CacheUtil.convertStringToModel(jsonCachedString, type);
        return videoRecommendationResult.getVideoSearch().getData();
    }

    @Override
    public void detachView() {
        super.detachView();
        graphqlUseCase.unsubscribe();
        getYoutubeVideoListDetailUseCase.unsubscribe();
    }
}
