package com.tokopedia.browse.homepage.data.source.cache;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.browse.homepage.data.entity.DigitalBrowseMarketplaceData;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by furqan on 13/09/18.
 */

public class DigitalBrowseServiceCacheSource {

    private static long DIGITAL_BROWSE_SERVICE_CACHE_TIMEOUT = TimeUnit.DAYS.toSeconds(30);
    private static String DIGITAL_BROWSE_SERVICE_CACHE_KEY = "DIGITAL_BROWSE_SERVICE_CACHE_KEY";

    private CacheManager cacheManager;

    @Inject
    public DigitalBrowseServiceCacheSource(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Observable<DigitalBrowseMarketplaceData> getCache() {
        String jsonString = cacheManager.get(DIGITAL_BROWSE_SERVICE_CACHE_KEY);

        Type type = new TypeToken<DigitalBrowseMarketplaceData>() {
        }.getType();
        DigitalBrowseMarketplaceData data = CacheUtil.convertStringToModel(jsonString, type);
        return Observable.just(data);
    }

    public void saveCache(DigitalBrowseMarketplaceData orderEntity) {
        cacheManager.delete(DIGITAL_BROWSE_SERVICE_CACHE_KEY);

        Type type = new TypeToken<DigitalBrowseMarketplaceData>() {
        }.getType();

        cacheManager.save(DIGITAL_BROWSE_SERVICE_CACHE_KEY,
                CacheUtil.convertModelToString(orderEntity, type),
                DIGITAL_BROWSE_SERVICE_CACHE_TIMEOUT);
    }
}