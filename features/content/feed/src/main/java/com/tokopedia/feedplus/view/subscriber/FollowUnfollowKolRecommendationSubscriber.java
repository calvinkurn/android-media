package com.tokopedia.feedplus.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kol.feature.post.data.pojo.FollowKolQuery;
import com.tokopedia.kol.feature.post.domain.model.FollowKolDomain;
import com.tokopedia.kol.feature.post.domain.usecase.FollowKolPostGqlUseCase;

/**
 * @author by nisie on 11/6/17.
 */

public class FollowUnfollowKolRecommendationSubscriber extends FollowUnfollowKolSubscriber {

    private final int position;

    public FollowUnfollowKolRecommendationSubscriber(int id, int status,
                                                     int rowNumber,
                                                     int position,
                                                     FeedPlus.View view,
                                                     FeedPlus.View.Kol kolListener) {
        super(id, status, rowNumber, view, kolListener);
        this.position = position;
    }

    @Override
    public void onNext(GraphqlResponse response) {
        FollowKolQuery query = response.getData(FollowKolQuery.class);
        if (query.getData() != null) {
            FollowKolDomain followKolDomain = new FollowKolDomain(query.getData().getData().getStatus());
            if (followKolDomain.getStatus() == FollowKolPostGqlUseCase.SUCCESS_STATUS) {
                kolListener.onSuccessFollowKolFromRecommendation(
                        rowNumber,
                        position,
                        status == FollowKolPostGqlUseCase.PARAM_FOLLOW
                );
            } else if (status == FollowKolPostGqlUseCase.PARAM_FOLLOW) {
                kolListener.onErrorFollowKol(view.getContext().getString(R.string
                        .failed_to_follow), id, status, rowNumber);
            } else {
                kolListener.onErrorFollowKol(view.getContext().getString(R.string
                        .failed_to_unfollow), id, status, rowNumber);
            }
        } else {
            kolListener.onErrorFollowKol(ErrorHandler.getErrorMessage(view.getContext(), new
                            Throwable()), id,
                    status, rowNumber);
        }
    }
}
