package com.tokopedia.kol.feature.postdetail.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kol.R;
import com.tokopedia.kolcommon.data.pojo.follow.FollowKolQuery;
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase;
import com.tokopedia.kol.feature.post.domain.model.FollowKolDomain;
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;

import rx.Subscriber;

/**
 * @author by milhamj on 01/08/18.
 */

public class FollowUnfollowDetailSubscriber extends Subscriber<GraphqlResponse> {
    private final KolPostDetailContract.View view;
    private final int rowNumber;
    private final int id;
    private final int status;

    public FollowUnfollowDetailSubscriber(KolPostDetailContract.View view, int rowNumber, int id,
                                          int status) {
        this.view = view;
        this.rowNumber = rowNumber;
        this.id = id;
        this.status = status;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onErrorFollowKol(
                ErrorHandler.getErrorMessage(view.getContext(), e), id, status, rowNumber
        );
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        FollowKolQuery query = graphqlResponse.getData(FollowKolQuery.class);
        if (query.getData() != null) {
            FollowKolDomain followKolDomain = new FollowKolDomain(query.getData().getData().getStatus());
            if (followKolDomain.getStatus() == FollowKolPostGqlUseCase.SUCCESS_STATUS) {
                view.onSuccessFollowUnfollowKol(rowNumber);
            } else {
                String errorMessage = view.getContext().getApplicationContext().getString(
                        com.tokopedia.abstraction.R.string.default_request_error_unknown
                );
                view.onErrorFollowKol(errorMessage, id, status, rowNumber);
            }
        } else {
            view.onErrorFollowKol(
                    ErrorHandler.getErrorMessage(view.getContext(), new Throwable()),
                    id, status, rowNumber
            );
        }
    }
}
