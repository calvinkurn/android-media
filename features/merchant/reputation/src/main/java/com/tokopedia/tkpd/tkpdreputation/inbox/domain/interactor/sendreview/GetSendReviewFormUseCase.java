package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview;

import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.sendreview.SendReviewPass;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author by nisie on 9/4/17.
 */

public class GetSendReviewFormUseCase extends UseCase<SendReviewPass> {

    public static final String CACHE_INBOX_REPUTATION_FORM = "CACHE_INBOX_REPUTATION_FORM";
    private PersistentCacheManager persistentCacheManager;

    public GetSendReviewFormUseCase(PersistentCacheManager persistentCacheManager) {
        super();
        this.persistentCacheManager = persistentCacheManager;
    }

    @Override
    public Observable<SendReviewPass> createObservable(RequestParams requestParams) {
        return Observable.just(CACHE_INBOX_REPUTATION_FORM)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(s -> {
                    SendReviewPass sendReviewPass = persistentCacheManager.get(CACHE_INBOX_REPUTATION_FORM, SendReviewPass.class);
                    if (sendReviewPass != null) {
                        return sendReviewPass;
                    } else {
                        throw new RuntimeException("Cache doesn't exist");
                    }
                });
    }
}
