package com.tokopedia.tkpd.home.database;

import android.support.annotation.Nullable;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tokopedia.core.database.DbFlowOperation;
import com.tokopedia.core.database.model.HomeCategoryMenuModelDb;
import com.tokopedia.core.network.entity.homeMenu.CategoryMenuModel;

import java.util.List;

/**
 * @author kulomady on 11/12/16
 */
public class HomeCategoryMenuDbManager implements DbFlowOperation<HomeCategoryMenuModelDb> {

    private static final String TAG = HomeCategoryMenuDbManager.class.getSimpleName();

    @Override
    public void store() {

    }

    @Override
    public void store(HomeCategoryMenuModelDb data) {

    }

    public void store(String content) {
        HomeCategoryMenuModelDb homeCategoryMenuModelDb = new HomeCategoryMenuModelDb();
        homeCategoryMenuModelDb.setId(1);
        homeCategoryMenuModelDb.setContentHomeCategory(content);
        homeCategoryMenuModelDb.setLastUpdated(System.currentTimeMillis());
        homeCategoryMenuModelDb.save();
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public void deleteAll() {
        Delete.tables(HomeCategoryMenuModelDb.class);
    }

    @Override
    public boolean isExpired(final long currentTime) {
        try {
            long lastUpdated = dataQueryHomeCategoryMenu().getLastUpdated();
            return isEmptyDataQueryHomeCategoryMenu() || isMoreThanOneHour(currentTime, lastUpdated);
        } catch (Exception e) {
            Log.e(TAG, "isExpired: ", e);
            return true;
        }
    }



    @Nullable
    @Override
    public HomeCategoryMenuModelDb getData(String key) {
        return null;
    }

    @Override
    public List<HomeCategoryMenuModelDb> getDataList(String key) {
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

    public List<CategoryMenuModel> getDataHomeCategoryMenu() {
//        new HomeCategoryMenuMapper(dataQueryHomeCategoryMenu()).convert();
//        return convertDataFromDbToListCategoryMenu(dataQueryHomeCategoryMenu());
        return new HomeCategoryMenuMapper(dataQueryHomeCategoryMenu()).convert();

    }

    private HomeCategoryMenuModelDb dataQueryHomeCategoryMenu() {
        return SQLite.select()
                .from(HomeCategoryMenuModelDb.class)
                .querySingle();
    }

    private boolean isMoreThanOneHour(long currentTime, long oldTime) {
        long oneHour = 1000 * 60 * 60;
        return currentTime - oldTime >= oneHour;
    }

    private boolean isEmptyDataQueryHomeCategoryMenu() {
        return dataQueryHomeCategoryMenu() == null;
    }

}