package com.tokopedia.tkpd.tkpdreputation.domain.interactor;

import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import rx.Observable;

/**
 * @author by nisie on 9/29/17.
 */

public class GetLikeDislikeReviewUseCase extends UseCase<GetLikeDislikeReviewDomain> {

    private static final String PARAM_REVIEW_IDS = "review_ids";
    private static final String PARAM_USER_ID = "user_id";

    private final ReputationRepository reputationRepository;


    public GetLikeDislikeReviewUseCase(ReputationRepository reputationRepository) {
        super();
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<GetLikeDislikeReviewDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.getLikeDislikeReview(requestParams);
    }

    public static RequestParams getParam(String reviewIds, String userId) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_REVIEW_IDS, reviewIds);
        params.putString(PARAM_USER_ID, userId);
        return params;
    }

}
