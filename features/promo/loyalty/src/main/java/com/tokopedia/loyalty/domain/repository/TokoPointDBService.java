package com.tokopedia.loyalty.domain.repository;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.cachemanager.PersistentCacheManager;
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

    public static final String KEY_TOKOPOINT_DRAWER_DATA = "KEY_TOKOPOINT_DRAWER_DATA";

    private final Gson gson;

    @Inject
    public TokoPointDBService(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Observable<HachikoDrawerDataResponse> getPointDrawer() {
        return Observable.just(KEY_TOKOPOINT_DRAWER_DATA)
                .map(new Func1<String, HachikoDrawerDataResponse>() {
                    @Override
                    public HachikoDrawerDataResponse call(String s) {
                        try {
                            String cacheStr = PersistentCacheManager.instance.getString(s);
                            if (cacheStr != null && !cacheStr.isEmpty()) {
                                HachikoDrawerDataResponse tokoPointDrawerDataResponse =
                                        gson.fromJson(cacheStr, HachikoDrawerDataResponse.class);

                                if (tokoPointDrawerDataResponse.getGqlTokoPointDrawerDataResponse().getGqlTokoPointPopupNotif() != null &&
                                        !TextUtils.isEmpty(tokoPointDrawerDataResponse.getGqlTokoPointDrawerDataResponse().getGqlTokoPointPopupNotif().getTitle())) {
                                    PersistentCacheManager.instance.delete(KEY_TOKOPOINT_DRAWER_DATA);
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
                    PersistentCacheManager.instance.put(KEY_TOKOPOINT_DRAWER_DATA, gson.toJson(tokoPointDrawerDataResponse), 60_000);
                }
            }
        });
    }
}
