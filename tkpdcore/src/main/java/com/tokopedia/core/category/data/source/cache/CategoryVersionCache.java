package com.tokopedia.core.category.data.source.cache;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.database.manager.CategoryDatabaseManager;

/**
 * Created by sebastianuskh on 3/8/17.
 */
@Deprecated
public class CategoryVersionCache {
    public static final String STORED_CATEGORY_HADES_VERSION = "STORED_CATEGORY_HADES_VERSION";
    public static final String VERSION_NUMBER = "VERSION_NUMBER";
    public static final int MINUTES = 60;
    private final Context context;

    public CategoryVersionCache(Context context) {
        this.context = context;
    }

    public Long getCategoryVersion() {
        return new LocalCacheHandler(context, STORED_CATEGORY_HADES_VERSION).getLong(VERSION_NUMBER);
    }

    public void storeVersion(Long apiVersion) {
        LocalCacheHandler localCache = new LocalCacheHandler(context, STORED_CATEGORY_HADES_VERSION);
        localCache.putLong(VERSION_NUMBER, apiVersion);
        localCache.applyEditor();
    }

    public boolean isNeedCategoryVersionCheck() {
        return new LocalCacheHandler(context, STORED_CATEGORY_HADES_VERSION).isExpired();
    }

    public void storeNeedCategoryVersionCheck(Integer interval) {
        new LocalCacheHandler(context, STORED_CATEGORY_HADES_VERSION).setExpire(interval * MINUTES);
    }

    public void clearCategoryTimer() {
        LocalCacheHandler.clearCache(context, CategoryDatabaseManager.KEY_STORAGE_NAME);
    }
}
