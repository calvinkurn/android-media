package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;

import rx.Observable;

/**
 * @author by nisie on 9/20/17.
 */

public class GetCacheInboxReputationUseCase extends UseCase<InboxReputationDomain> {

    protected ReputationRepository reputationRepository;

    public GetCacheInboxReputationUseCase(ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread,
                                          ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<InboxReputationDomain> createObservable(final RequestParams requestParams) {
        return reputationRepository.getInboxReputationFromLocal(requestParams);
    }
}
