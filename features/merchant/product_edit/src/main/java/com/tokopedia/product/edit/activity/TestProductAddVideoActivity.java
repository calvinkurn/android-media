package com.tokopedia.product.edit.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.product.edit.model.VideoRecommendationData;
import com.tokopedia.product.edit.presenter.GetVideoRecommendationPresenter;

import java.util.List;

/**
 * Created by hendry on 25/06/18.
 */

public class TestProductAddVideoActivity extends BaseSimpleActivity implements GetVideoRecommendationPresenter.GetVideoRecommendationView {

    private GetVideoRecommendationPresenter getVideoRecommendationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GraphqlClient.init(getApplicationContext());
        getVideoRecommendationPresenter = new GetVideoRecommendationPresenter();
        getVideoRecommendationPresenter.attachView(this);
        getVideoRecommendationPresenter.getVideoRecommendation("iphone", 3);
    }

    @Override
    public void onSuccessGetVideoRecommendation(List<VideoRecommendationData> videoRecommendationDataList) {
        //TODO update UI
    }

    @Override
    public void onErrorGetVideoRecommendation() {
        //TODO update UI
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
