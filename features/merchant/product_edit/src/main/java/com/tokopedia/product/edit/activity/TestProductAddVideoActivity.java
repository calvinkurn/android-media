package com.tokopedia.product.edit.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.product.edit.listener.ProductAddVideoRecommendationView;
import com.tokopedia.product.edit.model.VideoRecommendationData;
import com.tokopedia.product.edit.presenter.ProductAddVideoRecommendationPresenter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hendry on 25/06/18.
 */

public class TestProductAddVideoActivity extends BaseSimpleActivity implements ProductAddVideoRecommendationView {

    private ProductAddVideoRecommendationPresenter getVideoRecommendationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GraphqlClient.init(getApplicationContext());
        getVideoRecommendationPresenter = new ProductAddVideoRecommendationPresenter();
        getVideoRecommendationPresenter.attachView(this);
        getVideoRecommendationPresenter.getVideoRecommendation("iphone", 3);
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
    protected Fragment getNewFragment() {
        return null;
    }

    @NotNull
    @Override
    public Context getContextView() {
        return this;
    }
}
