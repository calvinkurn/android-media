package com.tokopedia.navigation.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.GlobalNavConstant;
import com.tokopedia.navigation.data.entity.RecomendationEntity;
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase;
import com.tokopedia.navigation.domain.GetRecomendationUseCase;
import com.tokopedia.navigation.domain.model.Recomendation;
import com.tokopedia.navigation.domain.subscriber.InboxSubscriber;
import com.tokopedia.navigation.presentation.view.InboxView;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by meta on 25/07/18.
 */
public class InboxPresenter extends BaseDaggerPresenter{

    private InboxView inboxView;

    private final GetDrawerNotificationUseCase getNotificationUseCase;
    private final GetRecomendationUseCase getRecomendationUseCase;

    @Inject InboxPresenter(GetDrawerNotificationUseCase getNotificationUseCase, GetRecomendationUseCase recomendationUseCase) {
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
        getRecomData(1);
    }

    public void getRecomData(int page) {
        if(this.inboxView == null)
            return;
        getRecomendationUseCase.execute(getRecomendationUseCase.getRecomParams(page),
                new Subscriber<List<Recomendation>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Recomendation> recomendations) {
                inboxView.onRenderRecomInbox(recomendations);
            }
        });
    }

    public void onResume() {
        this.getInboxData();
    }

    public void onDestroy() {
        this.getNotificationUseCase.unsubscribe();
        this.inboxView = null;
    }
}
