package com.tokopedia.feedplus.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kolcommon.data.pojo.FollowKolDomain;
import com.tokopedia.kolcommon.data.pojo.follow.FollowKolQuery;
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase;

import rx.Subscriber;

/**
 * @author by nisie on 11/3/17.
 */

public class FollowUnfollowKolSubscriber extends Subscriber<GraphqlResponse> {
    protected final FeedPlus.View view;
    protected final FeedPlus.View.Kol kolListener;
    protected final int rowNumber;
    protected final int id;
    protected final int status;


    public FollowUnfollowKolSubscriber(int id, int status, int rowNumber, FeedPlus.View view,
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
        kolListener.onErrorFollowKol(ErrorHandler.getErrorMessage(view.getContext(), e), id,
                status, rowNumber);
    }

    @Override
    public void onNext(GraphqlResponse response) {
        FollowKolQuery query = response.getData(FollowKolQuery.class);
        if (query.getData() != null) {
            FollowKolDomain followKolDomain = new FollowKolDomain(query.getData().getData().getStatus());
            if (followKolDomain.getStatus() == FollowKolPostGqlUseCase.SUCCESS_STATUS)
                kolListener.onSuccessFollowUnfollowKol(rowNumber);
            else {
                kolListener.onErrorFollowKol(view.getContext().getString(R.string
                        .default_request_error_unknown), id, status, rowNumber);
            }
        } else {
            kolListener.onErrorFollowKol(ErrorHandler.getErrorMessage(view.getContext(), new
                            Throwable()), id,
                    status, rowNumber);
        }
    }
}
