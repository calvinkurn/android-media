package com.tokopedia.core.database.manager;

import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.CacheDuration;
import com.tokopedia.core.database.DBOperation;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.CategoryDB_Table;

import java.util.List;

/**
 * Created by ricoharisin on 5/4/16.
 */
public class CategoryDatabaseManager implements DBOperation<CategoryDB> {

    List<CategoryDB> category;
    private static final String KEY_EXPIRED_CACHE = "category_expired";
    public static final String KEY_STORAGE_NAME = "category_storage";
    private static final String TAG = "CATEGORY CACHE MANAGER";

    public void setCategory(List<CategoryDB> category) {
        this.category = category;
    }

    @Override
    public void store() {

        DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
        database.beginTransaction();
        try {
            for (int i = 0; i < category.size(); i++) {
                category.get(i).save();
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), KEY_STORAGE_NAME);
        cache.putLong(KEY_EXPIRED_CACHE, System.currentTimeMillis());
        cache.applyEditor();

    }

    @Override
    public void delete(String key) {

    }

    @Override
    public void deleteAll() {
        new Delete().from(CategoryDB.class).execute();
    }

    @Override
    public boolean isExpired(long time) {
        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), KEY_STORAGE_NAME);
        long cacheTime = cache.getLong(KEY_EXPIRED_CACHE, 0);
        if (cacheTime == 0) return true;
        long diffTime = (time - cacheTime);

        Log.i(TAG, "DIFF TIME: "+diffTime);

        if (diffTime > CacheDuration.onDay(7)) return true;


        return false;
    }

    @Override
    public CategoryDB getData(String key) {
        return null;
    }

    @Override
    public List<CategoryDB> getDataList(String key) {
        return null;
    }


    @Override
    public String getValueString(String key) {
        return null;
    }

    @Override
    public <Z> Z getConvertObjData(String key, Class<Z> clazz) {
        return null;
    }


}
