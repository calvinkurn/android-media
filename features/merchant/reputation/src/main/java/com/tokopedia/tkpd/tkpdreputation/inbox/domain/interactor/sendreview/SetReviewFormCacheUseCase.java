package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.sendreview.SendReviewPass;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 9/4/17.
 */

public class SetReviewFormCacheUseCase extends UseCase<Boolean> {

    private static final String PARAM_SEND_REVIEW_PASS = "PARAM_SEND_REVIEW_PASS";

    private static final String TAG = SetReviewFormCacheUseCase.class.getSimpleName();
    GlobalCacheManager globalCacheManager;

    public SetReviewFormCacheUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     GlobalCacheManager globalCacheManager) {
        super(threadExecutor, postExecutionThread);
        this.globalCacheManager = globalCacheManager;
    }

    @Override
    public Observable<Boolean> createObservable(final RequestParams requestParams) {
        return Observable.just(true)
                .flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {

                        globalCacheManager.setKey(GetSendReviewFormUseCase.CACHE_INBOX_REPUTATION_FORM);
                        globalCacheManager.setValue(CacheUtil.convertModelToString((SendReviewPass) requestParams.getObject
                                        (PARAM_SEND_REVIEW_PASS),
                                new TypeToken<SendReviewPass>() {
                                }.getType()));
                        globalCacheManager.setCacheDuration(300);
                        globalCacheManager.store();
                        Log.i(TAG, "End of storing the cache.....");
                        return Observable.just(true);
                    }
                });
    }

    public static RequestParams getParam(SendReviewPass sendReviewPass) {
        RequestParams params = RequestParams.create();
        params.putObject(PARAM_SEND_REVIEW_PASS, sendReviewPass);
        return params;
    }
}
