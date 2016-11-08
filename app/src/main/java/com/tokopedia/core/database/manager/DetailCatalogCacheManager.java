package com.tokopedia.core.database.manager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.core.catalog.model.CatalogDetailData;
import com.tokopedia.core.database.DbFlowOperation;
import com.tokopedia.core.database.model.CatalogDetailModelDB;
import com.tokopedia.core.database.model.CatalogDetailModelDB_Table;

import java.util.List;

/**
 * @author anggaprasetiyo on 10/18/16.
 */

public class DetailCatalogCacheManager implements DbFlowOperation<CatalogDetailModelDB> {
    @Override
    public void store() {

    }

    @Override
    public void store(CatalogDetailModelDB data) {
        data.save();
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public boolean isExpired(long time) {
        return false;
    }

    @Override
    public CatalogDetailModelDB getData(String key) {
        return null;
    }

    @Override
    public List<CatalogDetailModelDB> getDataList(String key) {
        return null;
    }

    @Override
    public String getValueString(String key) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Z> Z getConvertObjData(String key, Class<Z> clazz) throws JsonSyntaxException {
        try {
            CatalogDetailModelDB cache = new Select().from(CatalogDetailModelDB.class)
                    .where(CatalogDetailModelDB_Table.detailCatalogId.is(key))
                    .querySingle();
            if (cache != null) {
                return cache.getExpiredTime() < System.currentTimeMillis() ? null
                        : (Z) new Gson().fromJson(cache.getDetailCatalogData(), CatalogDetailData.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
        //  if (cache == null) throw new RuntimeException("Tidak ada");

    }
}
