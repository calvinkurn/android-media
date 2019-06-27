package com.tokopedia.checkout.view.feature.cartlist.subscriber;

import com.tokopedia.checkout.view.feature.cartlist.ICartListPresenter;
import com.tokopedia.checkout.view.feature.cartlist.ICartListView;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

public class GetRecommendationSubscriber extends Subscriber<List<? extends RecommendationWidget>> {

    private ICartListView view;
    private ICartListPresenter presenter;

    public GetRecommendationSubscriber(ICartListView view, ICartListPresenter presenter) {
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (view != null) {
            view.hideItemLoading();
        }
    }

    @Override
    public void onNext(List<? extends RecommendationWidget> recommendationModels) {
        if (view != null) {
            view.hideItemLoading();
            view.renderRecommendation(recommendationModels.get(0).getRecommendationItemList());
        }
    }
}
