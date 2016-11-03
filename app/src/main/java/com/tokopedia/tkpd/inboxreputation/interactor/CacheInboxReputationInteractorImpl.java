package com.tokopedia.tkpd.inboxreputation.interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.tkpd.database.CacheUtil;
import com.tokopedia.tkpd.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.inboxreputation.model.inboxreputation.InboxReputation;
import com.tokopedia.tkpd.inboxreputation.model.inboxreputationdetail.InboxReputationDetail;
import com.tokopedia.tkpd.inboxreputation.model.param.ActReviewPass;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Nisie on 21/01/16.
 */
public class CacheInboxReputationInteractorImpl implements CacheInboxReputationInteractor {

    private static String TAG = "CacheInboxReputation";
    private static final String CACHE_INBOX_REPUTATION = "CACHE_INBOX_REPUTATION";
    private static final String CACHE_INBOX_REPUTATION_FORM = "CACHE_INBOX_REPUTATION_FORM";


    @Override
    public void getInboxReputationCache(String nav, final GetInboxReputationCacheListener listener) {
        Observable.just(CACHE_INBOX_REPUTATION + "_" + nav)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, InboxReputation>() {
                    @Override
                    public InboxReputation call(String s) {
                        GlobalCacheManager cache = new GlobalCacheManager();
                        return convertToInboxReputation(cache.getValueString(s));
                    }
                })
                .subscribe(new Subscriber<InboxReputation>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(InboxReputation inboxReputation) {
                        Log.i(TAG, "Get The Cache!! " + inboxReputation.toString());
                        listener.onSuccess(inboxReputation);
                    }

                });
    }

    @Override
    public void setInboxReputationCache(String nav, InboxReputation inboxReputation) {

        GlobalCacheManager cache = new GlobalCacheManager();
        cache.setKey(CACHE_INBOX_REPUTATION + "_" + nav);
        cache.setValue(CacheUtil.convertModelToString(inboxReputation,
                new TypeToken<InboxReputation>() {
                }.getType()));
        cache.setCacheDuration(300);
        cache.store();
        Log.i(TAG, "End of storing the cache.....");

    }

    @Override
    public void getInboxReputationDetailCache(String reviewId,
                                              final GetInboxReputationDetailCacheListener listener) {

        Observable.just(reviewId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, InboxReputationDetail>() {
                    @Override
                    public InboxReputationDetail call(String reviewId) {
                        InboxReputationCacheManager cache = new InboxReputationCacheManager();
                        return convertToInboxReputationDetail(cache.getCache(reviewId));
                    }
                })
                .subscribe(new Subscriber<InboxReputationDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(InboxReputationDetail InboxReputationDetail) {
                        Log.i(TAG, "Get The Cache!! " + InboxReputationDetail.toString());
                        listener.onSuccess(InboxReputationDetail);
                    }

                });
    }

    private boolean isCacheExpired(long expiredTime) {
        Log.i(TAG, "Cache expired time: " + expiredTime);
        Log.i(TAG, "Cache current time: " + System.currentTimeMillis());
        return expiredTime < System.currentTimeMillis();
    }


    @Override
    public void setInboxReputationDetailCache(String id, InboxReputationDetail inboxReputationDetail) {

        InboxReputationCacheManager cache = new InboxReputationCacheManager();
        cache.setReviewId(id);
        cache.setData(CacheUtil.convertModelToString(inboxReputationDetail,
                new TypeToken<InboxReputationDetail>() {
                }.getType()));
        cache.setCacheDuration(300);
        Observable.just(cache)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<InboxReputationCacheManager, InboxReputationCacheManager>() {
                    @Override
                    public InboxReputationCacheManager call(InboxReputationCacheManager inboxReputationCacheManager) {
                        inboxReputationCacheManager.save();
                        return inboxReputationCacheManager;
                    }
                })
                .subscribe(new Subscriber<InboxReputationCacheManager>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(InboxReputationCacheManager productDetailCacheManager) {

                    }
                });

    }

    @Override
    public void getInboxReputationFormCache(final GetInboxReputationFormCacheListener listener) {

        Observable.just(CACHE_INBOX_REPUTATION_FORM)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, ActReviewPass>() {
                    @Override
                    public ActReviewPass call(String s) {
                        GlobalCacheManager cache = new GlobalCacheManager();
                        return convertToInboxReputationForm(cache.getValueString(CACHE_INBOX_REPUTATION_FORM));
                    }
                })
                .subscribe(new Subscriber<ActReviewPass>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(ActReviewPass reviewPass) {
                        Log.i(TAG, "Get The Cache!! " + reviewPass.toString());
                        listener.onSuccess(reviewPass);
                    }

                });
    }

    @Override
    public void setInboxReputationFormCache(String reviewId, ActReviewPass reviewPass) {

        GlobalCacheManager cache = new GlobalCacheManager();
        cache.setKey(CACHE_INBOX_REPUTATION_FORM);
        cache.setValue(CacheUtil.convertModelToString(reviewPass,
                new TypeToken<ActReviewPass>() {
                }.getType()));
        cache.setCacheDuration(300);
        cache.store();
        Log.i(TAG, "End of storing the cache.....");

    }

    @Override
    public void deleteCache() {
        GlobalCacheManager cache = new GlobalCacheManager();
        cache.delete(CACHE_INBOX_REPUTATION);
        cache.delete(CACHE_INBOX_REPUTATION_FORM);
    }

    public InboxReputationDetail convertToInboxReputationDetail(String response) {
        if (response != null) {
            return new Gson().fromJson(response, InboxReputationDetail.class);
        } else {
            throw new RuntimeException("Cache doesn't exist");
        }
    }

    private InboxReputation convertToInboxReputation(String response) {
        if (response != null) {
            return new Gson().fromJson(response, InboxReputation.class);
        } else {
            throw new RuntimeException("Cache doesn't exist");
        }
    }

    private ActReviewPass convertToInboxReputationForm(String response) {
        if (response != null) {
            return new Gson().fromJson(response, ActReviewPass.class);
        } else {
            throw new RuntimeException("Cache doesn't exist");
        }
    }
}
