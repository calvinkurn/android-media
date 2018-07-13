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

/**
 * @author by nisie on 11/6/17.
 */

public class FollowUnfollowKolRecommendationGqlSubscriber extends FollowUnfollowKolGqlSubscriber {

    private final int position;

    public FollowUnfollowKolRecommendationGqlSubscriber(int id, int status,
                                                        int rowNumber,
                                                        int position,
                                                        FeedPlus.View view,
                                                        FeedPlus.View.Kol kolListener) {
        super(id, status, rowNumber, view, kolListener);
        this.position = position;
    }

    @Override
    public void onNext(GraphqlResponse response) {
        view.finishLoadingProgress();
        FollowKolQuery query = response.getData(FollowKolQuery.class);
        if (query.getData() != null) {
            FollowKolDomain followKolDomain = new FollowKolDomain(query.getData().getData().getStatus());
            if (followKolDomain.getStatus() == FollowKolPostUseCase.SUCCESS_STATUS
                    && status == FollowKolPostUseCase.PARAM_FOLLOW)
                kolListener.onSuccessFollowKolFromRecommendation(rowNumber, position);
            else if (followKolDomain.getStatus() == FollowKolPostUseCase.SUCCESS_STATUS
                    && status == FollowKolPostUseCase.PARAM_UNFOLLOW){
                kolListener.onSuccessUnfollowKolFromRecommendation(rowNumber, position);
            }
            else if(status == FollowKolPostUseCase.PARAM_FOLLOW){
                kolListener.onErrorFollowKol(MainApplication.getAppContext().getString(R.string
                        .failed_to_follow), id, status, rowNumber);
            }else{
                kolListener.onErrorFollowKol(MainApplication.getAppContext().getString(R.string
                        .failed_to_unfollow), id, status, rowNumber);
            }
        } else {
            kolListener.onErrorFollowKol(ErrorHandler.getErrorMessage(new Throwable()), id, status, rowNumber);
        }
    }
}
