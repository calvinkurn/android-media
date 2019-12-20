package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox;

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
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
