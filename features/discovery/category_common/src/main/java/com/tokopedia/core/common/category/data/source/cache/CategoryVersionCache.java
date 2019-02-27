package com.tokopedia.core.common.category.data.source.cache;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class CategoryVersionCache {
    private static final String CATEGORY_HADES_VERSION = "STORED_CATEGORY_HADES_VERSION";
    private static final String VERSION_NUMBER = "VERSION_NUMBER";
    private static final int MINUTES = 60;
    private final Context context;

    @Inject
    public CategoryVersionCache(@ApplicationContext Context context) {
        this.context = context;
    }

    public Long getCategoryVersion() {
        return new LocalCacheHandler(context, CATEGORY_HADES_VERSION).getLong(VERSION_NUMBER);
    }

    public void storeVersion(Long apiVersion) {
        LocalCacheHandler localCache = new LocalCacheHandler(context, CATEGORY_HADES_VERSION);
        localCache.putLong(VERSION_NUMBER, apiVersion);
        localCache.applyEditor();
    }

    public boolean isNeedCategoryVersionCheck() {
        return new LocalCacheHandler(context, CATEGORY_HADES_VERSION).isExpired();
    }

    public void storeNeedCategoryVersionCheck(Integer interval) {
        new LocalCacheHandler(context, CATEGORY_HADES_VERSION).setExpire(interval * MINUTES);
    }

    public void clearCategoryTimer() {
        LocalCacheHandler.clearCache(context, CATEGORY_HADES_VERSION);
    }
}
