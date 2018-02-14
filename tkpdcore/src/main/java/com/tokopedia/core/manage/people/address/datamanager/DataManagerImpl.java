package com.tokopedia.core.manage.people.address.datamanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.manage.people.address.interactor.RetrofitInteractor;
import com.tokopedia.core.manage.people.address.interactor.RetrofitInteractorImpl;
import com.tokopedia.core.manage.people.address.model.GetPeopleAddress;
import com.tokopedia.core.manage.people.address.presenter.ManagePeopleAddressFragmentPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;

import java.lang.reflect.Type;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created on 5/19/16.
 */
public class DataManagerImpl implements DataManager {

    private static final String TAG = DataManagerImpl.class.getSimpleName();

    private final ManagePeopleAddressFragmentPresenter presenter;
    private final RetrofitInteractorImpl retrofit;

    public DataManagerImpl(ManagePeopleAddressFragmentPresenter presenter) {
        this.presenter = presenter;
        this.retrofit = new RetrofitInteractorImpl();
    }

    private void requestData(@NonNull Context context,
                             @NonNull Map<String, String> params,
                             @NonNull RetrofitInteractor.GetPeopleAddressListener listener) {
        retrofit.getPeopleAddress(context, params, listener);
    }

    @Override
    public void disconnectNetworkConnection(Context context) {
        retrofit.unsubscribe();
    }

    @Override
    public void initAddressList(final Context context, final Map<String, String> params) {
        requestData(context, params, new RetrofitInteractor.GetPeopleAddressListener() {
            @Override
            public void onPreExecute(Context context, Map<String, String> params) {
                presenter.setAllowConnection(false);
                presenter.setBeforeInitAddressList();

                if (isUseFilter(params)) {
                    getCacheAddressList(generateCacheKey(context, params));
                }
            }

            @Override
            public void onSuccess(Context context,
                                  Map<String, String> params,
                                  GetPeopleAddress data) {
                if (isUseFilter(params)) {
                    setCacheAddressList(generateCacheKey(context, params), data);
                }

                presenter.setOnSuccessInitAddressList(data);
                presenter.setAllowConnection(true);
                presenter.setOnRequestSuccess();
            }

            @Override
            public void onTimeOut(NetworkErrorHelper.RetryClickedListener clickedListener) {
                presenter.setOnRequestTimeOut(clickedListener);
            }

            @Override
            public void onError(String message, NetworkErrorHelper.RetryClickedListener clickedListener) {
                presenter.setOnRequestError(message, clickedListener);
                presenter.setAllowConnection(true);
                presenter.setOnRequestSuccess();
            }

            @Override
            public void onNullData() {
                if (isUseFilter(params)) {
                    dropCacheAddressList(generateCacheKey(context, params));
                }
                presenter.setOnResponseNull();
                presenter.setAllowConnection(true);
                presenter.setOnRequestSuccess();
            }

            @Override
            public void onComplete() {
                presenter.finishRequest();
            }
        });
    }

    private void dropCacheAddressList(String cacheKey) {
        Observable.just(cacheKey)
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String cacheKey) {
                        // initialize local variable CacheManager
                        GlobalCacheManager cacheManager = new GlobalCacheManager();

                        // delete value
                        cacheManager.delete(cacheKey);
                        return true;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Log.e(TAG, "drop cache success");
                    }
                });
    }

    private boolean isUseFilter(Map<String, String> params) {
        return params.get(NetworkParam.PARAM_ORDER_BY).equals("1") && params.get(NetworkParam.PARAM_QUERY).isEmpty();
    }

    private void setCacheAddressList(String cacheKey, GetPeopleAddress data) {
        Observable.zip(
                Observable.just(cacheKey),
                Observable.just(data),
                new Func2<String, GetPeopleAddress, Boolean>() {
                    @Override
                    public Boolean call(String cacheKey, GetPeopleAddress cacheData) {
                        // initialize local variable CacheManager
                        GlobalCacheManager cacheManager = new GlobalCacheManager();

                        // initialize class you want to be converted from string
                        Type type = new TypeToken<GetPeopleAddress>() {}.getType();

                        // set value
                        cacheManager.setKey(cacheKey);
                        cacheManager.setValue(CacheUtil.convertModelToString(cacheData, type));
                        cacheManager.setCacheDuration(5000);
                        cacheManager.store();

                        return true;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Log.e(TAG, "storing cache success");
                    }
                });
    }

    private void getCacheAddressList(String cacheKey) {
        Observable.just(cacheKey)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, GetPeopleAddress>() {
                    @Override
                    public GetPeopleAddress call(String cacheKey) {
                        // initialize local variable CacheManager
                        GlobalCacheManager cacheManager = new GlobalCacheManager();

                        // initialize class you want to be converted from string
                        Type type = new TypeToken<GetPeopleAddress>() {}.getType();

                        // get json string which already cached
                        String jsonCachedString = cacheManager.getValueString(cacheKey);

                        return CacheUtil.convertStringToModel(jsonCachedString, type);
                    }
                })
                .subscribe(new Subscriber<GetPeopleAddress>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        presenter.onErrorGetCache();
                    }

                    @Override
                    public void onNext(GetPeopleAddress getPeopleAddress) {
                        if (getPeopleAddress != null) {
                            presenter.onSuccessGetCache(getPeopleAddress);
                        }
                    }
                });
    }

    private String generateCacheKey(Context context, Map<String, String> params) {
        String key = GetPeopleAddress.class.getSimpleName()
                + ":user_id=" + SessionHandler.getLoginID(context)
                + "&page=" + params.get(NetworkParam.PARAM_PAGE)
                + "&query=" + params.get(NetworkParam.PARAM_QUERY)
                + "&order_by" + params.get(NetworkParam.PARAM_ORDER_BY);
        Log.d(TAG, key);
        return key;
    }

    @Override
    public void loadMoreAddressList(final Context context, final Map<String, String> params) {
        requestData(context, params, new RetrofitInteractor.GetPeopleAddressListener() {
            @Override
            public void onPreExecute(Context context, Map<String, String> params) {
                presenter.setAllowConnection(false);
                presenter.setBeforeLoadMoreData();
            }

            @Override
            public void onSuccess(Context context, Map<String, String> params, GetPeopleAddress data) {
                presenter.setOnSuccessLoadMoreData(data);
                presenter.setAllowConnection(true);
                presenter.setOnRequestSuccess();
            }

            @Override
            public void onTimeOut(NetworkErrorHelper.RetryClickedListener clickedListener) {
                presenter.setOnRequestTimeOut(clickedListener);
            }

            @Override
            public void onError(String message, NetworkErrorHelper.RetryClickedListener clickedListener) {
                presenter.setOnRequestError(message, clickedListener);
                presenter.setAllowConnection(true);
                presenter.setOnRequestSuccess();
            }

            @Override
            public void onNullData() {
                presenter.setOnResponseNull();
                presenter.setAllowConnection(true);
                presenter.setOnRequestSuccess();
            }

            @Override
            public void onComplete() {
                presenter.finishRequest();
            }
        });
    }
}
