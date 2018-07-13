package com.tokopedia.feedplus.view.subscriber;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.data.pojo.FollowKolQuery;
import com.tokopedia.feedplus.domain.model.FollowKolDomain;
import com.tokopedia.feedplus.domain.usecase.FollowKolPostGqlUseCase;
import com.tokopedia.feedplus.domain.usecase.FollowKolPostUseCase;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

/**
 * @author by nisie on 11/3/17.
 */

public class FollowUnfollowKolGqlSubscriber extends Subscriber<GraphqlResponse> {
    protected final FeedPlus.View view;
    protected final FeedPlus.View.Kol kolListener;
    protected final int rowNumber;
    protected final int id;
    protected final int status;


    public FollowUnfollowKolGqlSubscriber(int id, int status, int rowNumber, FeedPlus.View view,
                                          FeedPlus.View.Kol kolListener) {
        this.view = view;
        this.kolListener = kolListener;
        this.rowNumber = rowNumber;
        this.id = id;
        this.status = status;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.finishLoadingProgress();
        kolListener.onErrorFollowKol(ErrorHandler.getErrorMessage(e), id, status, rowNumber);
    }

    @Override
    public void onNext(GraphqlResponse response) {
        view.finishLoadingProgress();
        FollowKolQuery query = response.getData(FollowKolQuery.class);
        if (query.getData() != null) {
            FollowKolDomain followKolDomain = new FollowKolDomain(query.getData().getData().getStatus());
            if (followKolDomain.getStatus() == FollowKolPostGqlUseCase.SUCCESS_STATUS)
                kolListener.onSuccessFollowUnfollowKol(rowNumber);
            else {
                kolListener.onErrorFollowKol(MainApplication.getAppContext().getString(R.string
                        .default_request_error_unknown), id, status, rowNumber);
            }
        } else {
            kolListener.onErrorFollowKol(ErrorHandler.getErrorMessage(new Throwable()), id, status, rowNumber);
        }
    }
}
