package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview;

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewSubmitDomain;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author by nisie on 9/12/17.
 */

public class EditReviewSubmitUseCase extends SendReviewSubmitUseCase {

    public EditReviewSubmitUseCase(ReputationRepository reputationRepository) {
        super(reputationRepository);
    }

    @Override
    public Observable<SendReviewSubmitDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.editReviewSubmit(requestParams);
    }
}
