package com.tokopedia.navigation.presentation.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.navigation.GlobalNavConstant;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase;
import com.tokopedia.navigation.domain.model.RecomTitle;
import com.tokopedia.navigation.domain.model.Recomendation;
import com.tokopedia.navigation.domain.subscriber.InboxSubscriber;
import com.tokopedia.navigation.presentation.view.InboxView;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;
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
    private final GetRecommendationUseCase getRecommendationUseCase;

    public static final String X_SOURCE_RECOM_WIDGET = "recom_widget";
    public static final String INBOX_PAGE = "inbox";

    @Inject
    InboxPresenter(GetDrawerNotificationUseCase getNotificationUseCase, GetRecommendationUseCase recommendationUseCase) {
        this.getNotificationUseCase = getNotificationUseCase;
        this.getRecommendationUseCase = recommendationUseCase;
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
        getRecommendationUseCase.execute(getRecommendationUseCase.getRecomParams(0,
                X_SOURCE_RECOM_WIDGET,
                INBOX_PAGE,
                new ArrayList<>()),
                new Subscriber<List<? extends RecommendationWidget>>() {
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
                        inboxView.hideLoadMoreLoading();
                    }

                    @Override
                    public void onNext(List<? extends RecommendationWidget> recommendationWidgets) {
                        List<Visitable> visitables = new ArrayList<>();
                        RecommendationWidget recommendationWidget = recommendationWidgets.get(0);
                        visitables.add(new RecomTitle(recommendationWidget.getTitle()));
                        visitables.addAll(getRecommendationVisitables(recommendationWidget));
                        inboxView.hideLoadMoreLoading();
                        inboxView.onRenderRecomInbox(visitables);
                    }
                });
    }

    public void getRecomData(int page) {
        if (this.inboxView == null)
            return;
        getRecommendationUseCase.execute(getRecommendationUseCase.getRecomParams(
                page,
                X_SOURCE_RECOM_WIDGET,
                INBOX_PAGE,
                new ArrayList<>()),
                new Subscriber<List<? extends RecommendationWidget>>() {
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
                        inboxView.hideLoadMoreLoading();
                    }

                    @Override
                    public void onNext(List<? extends RecommendationWidget> recommendationWidgets) {
                        inboxView.hideLoadMoreLoading();
                        RecommendationWidget recommendationWidget = recommendationWidgets.get(0);
                        inboxView.onRenderRecomInbox(getRecommendationVisitables(recommendationWidget));
                    }
                });
    }

    @NonNull
    private List<Visitable> getRecommendationVisitables(RecommendationWidget recommendationWidget) {
        List<Visitable> recomendationList = new ArrayList<>();
        for (RecommendationItem item : recommendationWidget.getRecommendationItemList()) {
            recomendationList.add(new Recomendation(item));
        }
        return recomendationList;
    }

    public void onResume() {
        this.getInboxData();
    }

    public void onDestroy() {
        this.getRecommendationUseCase.unsubscribe();
        this.getNotificationUseCase.unsubscribe();
        this.inboxView = null;
    }
}
