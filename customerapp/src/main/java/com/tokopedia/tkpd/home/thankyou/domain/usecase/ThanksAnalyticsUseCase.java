package com.tokopedia.tkpd.home.thankyou.domain.usecase;

import android.os.Build;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.tkpd.home.thankyou.data.repository.ThanksAnalyticsRepository;

import rx.Observable;

/**
 * Created by okasurya on 12/4/17.
 */

public class ThanksAnalyticsUseCase extends UseCase<String> {
    private ThanksAnalyticsRepository thanksAnalyticsRepository;

    public ThanksAnalyticsUseCase(ThanksAnalyticsRepository thanksAnalyticsRepository) {
        this.thanksAnalyticsRepository = thanksAnalyticsRepository;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return thanksAnalyticsRepository.sendAnalytics(requestParams);
    }
}
