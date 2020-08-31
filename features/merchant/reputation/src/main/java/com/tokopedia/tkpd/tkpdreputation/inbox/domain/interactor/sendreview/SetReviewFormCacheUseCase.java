package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview;

import android.util.Log;

import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.sendreview.SendReviewPass;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * @author by nisie on 9/4/17.
 */

public class SetReviewFormCacheUseCase extends UseCase<Boolean> {

    private static final String PARAM_SEND_REVIEW_PASS = "PARAM_SEND_REVIEW_PASS";

    private static final String TAG = SetReviewFormCacheUseCase.class.getSimpleName();
    private PersistentCacheManager persistentCacheManager;

    public SetReviewFormCacheUseCase(PersistentCacheManager persistentCacheManager) {
        super();
        this.persistentCacheManager = persistentCacheManager;
    }

    @Override
    public Observable<Boolean> createObservable(final RequestParams requestParams) {
        return Observable.just(true)
                .flatMap(aBoolean -> {
                    persistentCacheManager.put(
                            GetSendReviewFormUseCase.CACHE_INBOX_REPUTATION_FORM,
                            requestParams.getObject(PARAM_SEND_REVIEW_PASS),
                            TimeUnit.DAYS.toMillis(7)
                    );
                    Log.i(TAG, "End of storing the cache.....");
                    return Observable.just(true);
                });
    }

    public static RequestParams getParam(SendReviewPass sendReviewPass) {
        RequestParams params = RequestParams.create();
        params.putObject(PARAM_SEND_REVIEW_PASS, sendReviewPass);
        return params;
    }
}
