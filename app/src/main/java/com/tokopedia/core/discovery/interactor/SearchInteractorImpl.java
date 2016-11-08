package com.tokopedia.core.discovery.interactor;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Toped18 on 7/5/2016.
 */
public class SearchInteractorImpl implements SearchInteractor {

    GetSearchCacheListener listener;
    CompositeSubscription compositeSubscription;

    public SearchInteractorImpl(){

    }

    @Override
    public void deleteItem(String s) {
        if(listener == null){
            Log.i(TAG, "Listener not found");
        }

        Log.i(TAG, "Delete item : " + s);

        compositeSubscription.add(Observable.just(SEARCH_CACHE)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, List<String>>() {
                    @Override
                    public List<String> call(String s) {
                        Log.i(TAG, "Get all cached item");
                        GlobalCacheManager cache = new GlobalCacheManager();
                        Type type = new TypeToken<List<String>>() {}.getType();
                        List<String> storedHistory = CacheUtil.convertStringToListModel(cache.getValueString(s),type);
                        Log.i(TAG, "Get all cache, trying to delete the item");
                        if(!storedHistory.remove(s)){
                            throw new UnsupportedOperationException("No string found in the cache");
                        }
                        storeSearchCache(storedHistory);
                        return storedHistory;
                    }
                })
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        listener.onSuccess(strings);
                    }
                })
        );


    }

    @Override
    public void getSearchCache() {
        if(listener == null){
            Log.i(TAG,"Listener not found");
            return;
        }
        compositeSubscription.add(Observable.just(SEARCH_CACHE)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, List<String>>() {
                    @Override
                    public List<String> call(String s) {
                        GlobalCacheManager cache = new GlobalCacheManager();
                        Type type = new TypeToken<List<String>>() {}.getType();
                        return CacheUtil.convertStringToListModel(cache.getValueString(s),type);
                    }
                })
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(List<String> listSearchCache) {
                        if(listSearchCache != null){
                            Log.i(TAG, "Get The Cache!! " + listSearchCache.toString());
                            listener.onSuccess(listSearchCache);
                        } else {
                            Log.i(TAG, "List Empty, create a new one");
                            List<String> newList = new ArrayList<String>();
                            listener.onSuccess(newList);
                        }

                    }

                }));
    }

    @Override
    public void storeSearchCache(List<String> query) {
        Log.i(TAG,"Storing the cache " + query.toString());
        GlobalCacheManager cache = new GlobalCacheManager();
        cache.setKey(SEARCH_CACHE);
        cache.setValue(CacheUtil.convertListModelToString(query,
                new TypeToken<List<String>>() {
                }.getType()));
        cache.store();
    }

    @Override
    public void clearSearchCache() {
        Log.i(TAG,"Clearing the search cache");
        GlobalCacheManager cache = new GlobalCacheManager();
        cache.delete(SEARCH_CACHE);
    }

    @Override
    public void setCompositeSubscription(CompositeSubscription compositeSubscription) {
        this.compositeSubscription = compositeSubscription;
    }

    @Override
    public void setListener(GetSearchCacheListener listener) {
        this.listener = listener;
    }
}
