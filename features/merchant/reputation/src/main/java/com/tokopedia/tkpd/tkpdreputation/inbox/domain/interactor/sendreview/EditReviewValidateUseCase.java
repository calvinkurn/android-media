package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview;

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author by nisie on 9/12/17.
 */

public class EditReviewValidateUseCase extends SendReviewValidateUseCase {

    public EditReviewValidateUseCase(ReputationRepository reputationRepository) {
        super(reputationRepository);
    }

    @Override
    public Observable<SendReviewValidateDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.editReviewValidation(requestParams);
    }
}
