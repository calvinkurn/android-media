package com.tokopedia.product.edit.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.networklib.util.RestClient;
import com.tokopedia.product.edit.model.videorecommendation.VideoRecommendationData;
import com.tokopedia.product.edit.model.youtube.YoutubeVideoModel;
import com.tokopedia.product.edit.presenter.YouTubeVideoPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 25/06/18.
 */

public class TestProductAddVideoActivity extends BaseSimpleActivity implements YouTubeVideoPresenter.GetVideoRecommendationView {

    private YouTubeVideoPresenter youTubeVideoPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GraphqlClient.init(getApplicationContext());
        RestClient.init(getApplicationContext());

        youTubeVideoPresenter = new YouTubeVideoPresenter();
        youTubeVideoPresenter.attachView(this);
        youTubeVideoPresenter.getVideoRecommendation("iphone", 3);

        ArrayList<String> videoIdList = new ArrayList<>();
        videoIdList.add("ruUq2F72oao");
        videoIdList.add("qifuYKVi23I");
        youTubeVideoPresenter.getVideoDetail(videoIdList);
    }

    @Override
    public void onSuccessGetVideoRecommendation(List<VideoRecommendationData> videoRecommendationDataList) {
        //TODO update UI
    }

    @Override
    public void onErrorGetVideoRecommendation(Throwable throwable) {
        //TODO update UI
    }

    @Override
    public void onSuccessGetVideoDetailList(ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList) {
        //TODO update UI
    }

    @Override
    public void onErrorGetVideoDetailList(Throwable e) {
        //TODO update UI
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public Context getContextView() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubeVideoPresenter.detachView();
    }
}
