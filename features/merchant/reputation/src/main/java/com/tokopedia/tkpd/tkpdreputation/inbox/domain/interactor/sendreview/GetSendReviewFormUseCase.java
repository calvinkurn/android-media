package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.sendreview.SendReviewPass;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author by nisie on 9/4/17.
 */

public class GetSendReviewFormUseCase extends UseCase<SendReviewPass> {

    public static final String CACHE_INBOX_REPUTATION_FORM = "CACHE_INBOX_REPUTATION_FORM";
    GlobalCacheManager globalCacheManager;

    public GetSendReviewFormUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    GlobalCacheManager globalCacheManager) {
        super(threadExecutor, postExecutionThread);
        this.globalCacheManager = globalCacheManager;
    }

    @Override
    public Observable<SendReviewPass> createObservable(RequestParams requestParams) {
        return Observable.just(CACHE_INBOX_REPUTATION_FORM)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, SendReviewPass>() {
                    @Override
                    public SendReviewPass call(String s) {
                        GlobalCacheManager cache = new GlobalCacheManager();
                        return convertToInboxReputationForm(
                                cache.getValueString(CACHE_INBOX_REPUTATION_FORM));
                    }
                });
    }


    private SendReviewPass convertToInboxReputationForm(String response) {
        if (response != null) {
            return new Gson().fromJson(response, SendReviewPass.class);
        } else {
            throw new RuntimeException("Cache doesn't exist");
        }
    }
}
