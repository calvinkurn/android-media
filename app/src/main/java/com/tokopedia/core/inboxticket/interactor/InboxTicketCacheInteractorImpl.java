package com.tokopedia.core.inboxticket.interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.inboxticket.model.inboxticket.InboxTicket;
import com.tokopedia.core.inboxticket.model.inboxticketdetail.InboxTicketDetail;
import com.tokopedia.core.inboxticket.util.InboxTicketCacheManager;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Nisie on 4/19/16.
 */
public class InboxTicketCacheInteractorImpl implements InboxTicketCacheInteractor {
    private static final String TAG = InboxTicketCacheInteractorImpl.class.getSimpleName();
    private static final String INBOX_TICKET_CACHE = "INBOX_TICKET_CACHE";
    private GlobalCacheManager cacheManager;

    public InboxTicketCacheInteractorImpl() {
        this.cacheManager = new GlobalCacheManager();
    }

    @Override
    public void getInboxTicketCache(final GetInboxTicketCacheListener listener) {
        Observable.just(INBOX_TICKET_CACHE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<String, InboxTicket>() {
                    @Override
                    public InboxTicket call(String s) {

                        return CacheUtil.convertStringToModel(cacheManager.getValueString(s),
                                new TypeToken<InboxTicket>() {
                                }.getType());
                    }
                })
                .subscribe(new Subscriber<InboxTicket>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(InboxTicket result) {
                        try {
                            listener.onSuccess(result);
                        } catch (Exception e) {
                            listener.onError(e);
                        }
                    }
                });
    }

    @Override
    public void setInboxTicketCache(InboxTicket result) {
        Observable.just(result)
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<InboxTicket, Boolean>() {
                    @Override
                    public Boolean call(InboxTicket inboxTicket) {
                        cacheManager.setKey(INBOX_TICKET_CACHE)
                                .setCacheDuration(300)
                                .setValue(CacheUtil.convertModelToString(inboxTicket,
                                        new TypeToken<InboxTicket>() {
                                        }.getType()))
                                .store();
                        return true;
                    }
                })
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }

                });
    }

    @Override
    public void getInboxTicketDetailCache(String ticketId, final GetInboxTicketDetailCacheListener listener) {
        Observable.just(ticketId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, InboxTicketDetail>() {
                    @Override
                    public InboxTicketDetail call(String ticketId) {
                        InboxTicketCacheManager cache = new InboxTicketCacheManager();
                        return convertToInboxTicketDetail(cache.getCache(ticketId));
                    }
                })
                .subscribe(new Subscriber<InboxTicketDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(InboxTicketDetail inboxTicketDetail) {
                        Log.i(TAG, "Get The Cache!! " + inboxTicketDetail.toString());
                        listener.onSuccess(inboxTicketDetail);
                    }

                });
    }

    private InboxTicketDetail convertToInboxTicketDetail(String cache) {
        if (cache != null) {
            return new Gson().fromJson(cache, InboxTicketDetail.class);
        } else {
            throw new RuntimeException("Cache doesn't exist");
        }
    }

    @Override
    public void setInboxTicketDetailCache(String ticketId, InboxTicketDetail result) {
        InboxTicketCacheManager cache = new InboxTicketCacheManager();
        cache.setTicketId(ticketId);
        cache.setData(CacheUtil.convertModelToString(result,
                new TypeToken<InboxTicketDetail>() {
                }.getType()));
        cache.setCacheDuration(300);
        Observable.just(cache)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<InboxTicketCacheManager, InboxTicketCacheManager>() {
                    @Override
                    public InboxTicketCacheManager call(InboxTicketCacheManager inboxTicketCacheManager) {
                        inboxTicketCacheManager.save();
                        return inboxTicketCacheManager;
                    }
                })
                .subscribe(new Subscriber<InboxTicketCacheManager>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(InboxTicketCacheManager inboxTicketCacheManager) {

                    }
                });
    }

    @Override
    public void deleteCache() {
        cacheManager.delete(INBOX_TICKET_CACHE);
    }
}
