package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox;

import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import rx.Observable;

/**
 * @author by nisie on 9/20/17.
 */

public class GetCacheInboxReputationUseCase extends UseCase<InboxReputationDomain> {

    private final ReputationRepository reputationRepository;

    public GetCacheInboxReputationUseCase(ReputationRepository reputationRepository) {
        super();
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<InboxReputationDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.getInboxReputationFromLocal(requestParams);
    }
}
