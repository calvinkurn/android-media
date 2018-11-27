package com.tokopedia.digital.common.data.source;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.widget.data.entity.status.StatusEntity;
import com.tokopedia.digital.widget.view.model.mapper.StatusMapper;
import com.tokopedia.digital.widget.view.model.status.Status;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 19/01/18.
 */

public class StatusDataSource {

    private final static String KEY_STATUS_CURRENT = "RECHARGE_STATUS_CURRENT";
    private final static String KEY_CATEGORY_LIST = "RECHARGE_CATEGORY_LIST";

    private DigitalEndpointService digitalEndpointService;
    private CacheManager cacheManager;
    private StatusMapper statusMapper;

    public StatusDataSource(DigitalEndpointService digitalEndpointService,
                            CacheManager globalCacheManager,
                            StatusMapper statusMapper) {
        this.digitalEndpointService = digitalEndpointService;
        this.cacheManager = globalCacheManager;
        this.statusMapper = statusMapper;
    }

    public Observable<Status> getStatus() {
        return digitalEndpointService.getApi().getStatus()
                .map(getFuncTransformStatusEntity())
                .map(new Func1<StatusEntity, StatusEntity>() {
                    @Override
                    public StatusEntity call(StatusEntity status) {
                        String currentStatusString = cacheManager.get(KEY_STATUS_CURRENT);
                        String statusString = CacheUtil.convertModelToString(status,
                                new TypeToken<StatusEntity>() {
                                }.getType());
                        if (currentStatusString != null && !currentStatusString.equals(statusString)) {
                            cacheManager.delete(KEY_CATEGORY_LIST);

                            saveStatusToCache(statusString);
                        } else if (currentStatusString == null) {
                            saveStatusToCache(statusString);
                        }
                        return status;
                    }
                })
                .map(statusMapper);
    }

    private void saveStatusToCache(String statusString) {
        cacheManager.save(
                KEY_STATUS_CURRENT,
                statusString,
                0
        );
    }

    @NonNull
    private Func1<Response<TkpdDigitalResponse>, StatusEntity> getFuncTransformStatusEntity() {
        return new Func1<Response<TkpdDigitalResponse>, StatusEntity>() {
            @Override
            public StatusEntity call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                return tkpdDigitalResponseResponse.body().convertDataObj(StatusEntity.class);
            }
        };
    }

}
