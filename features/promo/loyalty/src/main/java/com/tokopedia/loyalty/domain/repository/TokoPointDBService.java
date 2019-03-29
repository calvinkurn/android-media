package com.tokopedia.loyalty.domain.repository;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.loyalty.domain.entity.response.GqlTokoPointDrawerDataResponse;
import com.tokopedia.loyalty.domain.entity.response.HachikoDrawerDataResponse;
import com.tokopedia.loyalty.domain.exception.TokoPointDBServiceException;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 07/12/17.
 */

public class TokoPointDBService implements ITokoPointDBService {

    private final CacheManager globalCacheManager;
    private final Gson gson;

    @Inject
    public TokoPointDBService(CacheManager globalCacheManager, Gson gson) {
        this.globalCacheManager = globalCacheManager;
        this.gson = gson;
    }

    @Override
    public Observable<HachikoDrawerDataResponse> getPointDrawer() {
        return Observable.just(TkpdCache.Key.KEY_TOKOPOINT_DRAWER_DATA)
                .map(new Func1<String, HachikoDrawerDataResponse>() {
                    @Override
                    public HachikoDrawerDataResponse call(String s) {
                        try {
                            String cacheStr = globalCacheManager.get(s);
                            if (cacheStr != null && !cacheStr.isEmpty()) {
                                HachikoDrawerDataResponse tokoPointDrawerDataResponse =
                                        gson.fromJson(cacheStr, HachikoDrawerDataResponse.class);

                                if (tokoPointDrawerDataResponse.getGqlTokoPointDrawerDataResponse().getGqlTokoPointPopupNotif() != null &&
                                        !TextUtils.isEmpty(tokoPointDrawerDataResponse.getGqlTokoPointDrawerDataResponse().getGqlTokoPointPopupNotif().getTitle())) {
                                    globalCacheManager.delete(TkpdCache.Key.KEY_TOKOPOINT_DRAWER_DATA);
                                    throw new TokoPointDBServiceException("cant pull from db, cause data has notif flag active");
                                }

                                return tokoPointDrawerDataResponse;
                            } else {
                                throw new TokoPointDBServiceException("Cache null or not completed data");
                            }
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                            throw new TokoPointDBServiceException("Cache not found or expired");
                        }

                    }
                });
    }

    @Override
    public Observable<HachikoDrawerDataResponse> storePointDrawer(
            HachikoDrawerDataResponse tokoPointDrawerDataResponse
    ) {
        return Observable.just(tokoPointDrawerDataResponse).doOnNext(new Action1<HachikoDrawerDataResponse>() {
            @Override
            public void call(HachikoDrawerDataResponse tokoPointDrawerDataResponse) {

                if (tokoPointDrawerDataResponse.getGqlTokoPointDrawerDataResponse().getGqlTokoPointPopupNotif() == null ||
                        (tokoPointDrawerDataResponse.getGqlTokoPointDrawerDataResponse().getGqlTokoPointPopupNotif() != null &&
                                TextUtils.isEmpty(tokoPointDrawerDataResponse.getGqlTokoPointDrawerDataResponse().getGqlTokoPointPopupNotif().getTitle()))) {
                    globalCacheManager.save(TkpdCache.Key.KEY_TOKOPOINT_DRAWER_DATA, gson.toJson(tokoPointDrawerDataResponse), 60);
                }
            }
        });
//                .map(new Func1<TokoPointDrawerDataResponse, TokoPointDrawerDataResponse>() {
//                    @Override
//                    public TokoPointDrawerDataResponse call(
//                            TokoPointDrawerDataResponse tokoPointDrawerDataResponse
//                    ) {
//                        if (tokoPointDrawerDataResponse.getHasNotif() != 1) {
//                            globalCacheManager.setCacheDuration(60);
//                            globalCacheManager.setKey(TkpdCache.Key.KEY_TOKOPOINT_DRAWER_DATA);
//                            globalCacheManager.setValue(gson.toJson(tokoPointDrawerDataResponse));
//                            globalCacheManager.store();
//                        }
//                        return tokoPointDrawerDataResponse;
//                    }
//                });
    }
}
