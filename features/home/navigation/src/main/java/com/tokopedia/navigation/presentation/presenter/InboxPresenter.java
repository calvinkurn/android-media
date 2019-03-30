package com.tokopedia.navigation.presentation.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.GlobalNavConstant;
import com.tokopedia.navigation.data.entity.RecomendationEntity;
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase;
import com.tokopedia.navigation.domain.GetRecomendationUseCase;
import com.tokopedia.navigation.domain.model.RecomTitle;
import com.tokopedia.navigation.domain.model.Recomendation;
import com.tokopedia.navigation.domain.subscriber.InboxSubscriber;
import com.tokopedia.navigation.presentation.view.InboxView;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by meta on 25/07/18.
 */
public class InboxPresenter extends BaseDaggerPresenter {

    private InboxView inboxView;

    private final GetDrawerNotificationUseCase getNotificationUseCase;
    private final GetRecomendationUseCase getRecomendationUseCase;

    @Inject
    InboxPresenter(GetDrawerNotificationUseCase getNotificationUseCase, GetRecomendationUseCase recomendationUseCase) {
        this.getNotificationUseCase = getNotificationUseCase;
        this.getRecomendationUseCase = recomendationUseCase;
    }

    public void setView(InboxView inboxView) {
        this.inboxView = inboxView;
    }

    public void getInboxData() {
        if (this.inboxView == null)
            return;

        this.inboxView.onStartLoading();

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GlobalNavConstant.QUERY,
                GraphqlHelper.loadRawString(this.inboxView.getContext().getResources(), R.raw.query_notification));
        getNotificationUseCase.execute(requestParams, new InboxSubscriber(this.inboxView));
    }

    public void getFirstRecomData(){
        if (this.inboxView == null)
            return;
        getRecomendationUseCase.execute(getRecomendationUseCase.getRecomParams(0),
                new Subscriber<RecomendationEntity.RecomendationData>() {
                    @Override
                    public void onStart() {
                        inboxView.showLoadMoreLoading();
                    }
                    @Override
                    public void onCompleted() {
                        inboxView.hideLoadMoreLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(RecomendationEntity.RecomendationData recomendationData) {
                        List<Visitable> visitables = new ArrayList<>();
                        visitables.add(new RecomTitle(recomendationData.getTitle()));
                        visitables.addAll(getRecomendations(recomendationData));
                        inboxView.hideLoadMoreLoading();
                        inboxView.onRenderRecomInbox(visitables);
                    }
                });
    }

    public void getRecomData(int page) {
        if (this.inboxView == null)
            return;
        getRecomendationUseCase.execute(getRecomendationUseCase.getRecomParams(page),
                new Subscriber<RecomendationEntity.RecomendationData>() {
                    @Override
                    public void onStart() {
                        inboxView.showLoadMoreLoading();
                    }

                    @Override
                    public void onCompleted() {
                        inboxView.hideLoadMoreLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(RecomendationEntity.RecomendationData recomendationData) {
                        inboxView.hideLoadMoreLoading();
                        inboxView.onRenderRecomInbox(getRecomendations(recomendationData));
                    }
                });
    }

    @NonNull
    private List<Visitable> getRecomendations(RecomendationEntity.RecomendationData recomendationData) {
        List<Visitable> recomendationList = new ArrayList<>();
        for (RecomendationEntity.Recommendation r : recomendationData.getRecommendation()) {
            Recomendation recomendation = new Recomendation();
            recomendation.setImageUrl(r.getImageUrl());
            recomendation.setCategoryBreadcrumbs(r.getCategoryBreadcrumbs());
            recomendation.setClickUrl(r.getClickUrl());
            recomendation.setPrice(r.getPrice());
            recomendation.setPriceNumber(r.getPriceInt());
            recomendation.setProductId(r.getId());
            recomendation.setDepartementId(r.getDepartmentId());
            recomendation.setProductName(r.getName());
            recomendation.setRecommendationType(r.getRecommendationType());
            recomendation.setTopAds(r.isIsTopads());
            recomendation.setTrackerImageUrl(r.getTrackerImageUrl());
            recomendationList.add(recomendation);
        }
        return recomendationList;
    }

    public void onResume() {
        this.getInboxData();
    }

    public void onDestroy() {
        this.getNotificationUseCase.unsubscribe();
        this.inboxView = null;
    }
}
