package com.tokopedia.tkpd.thankyou.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.tkpd.thankyou.data.repository.ThanksTrackerRepository;

import rx.Observable;

/**
 * Created by okasurya on 12/4/17.
 */

public class ThankYouPageTrackerUseCase extends UseCase<Boolean> {
    private ThanksTrackerRepository thanksTrackerRepository;

    public ThankYouPageTrackerUseCase(ThanksTrackerRepository thanksTrackerRepository) {
        this.thanksTrackerRepository = thanksTrackerRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return thanksTrackerRepository.sendTracker(requestParams);
    }
}
