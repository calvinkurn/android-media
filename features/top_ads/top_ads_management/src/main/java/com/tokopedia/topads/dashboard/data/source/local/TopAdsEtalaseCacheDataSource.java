package com.tokopedia.topads.dashboard.data.source.local;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.topads.dashboard.data.source.TopAdsEtalaseDataSource;
import com.tokopedia.topads.dashboard.data.model.data.Etalase;

import java.util.List;

import rx.Observable;
/**
 * Created by hendry on 2/20/17.
 */
public class TopAdsEtalaseCacheDataSource implements TopAdsEtalaseDataSource {
    public TopAdsEtalaseCacheDataSource() {

    }

    public Observable<List<Etalase>> getEtalaseList(String shopId) {
        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        try {
            String jsonString = globalCacheManager.getValueString(TkpdCache.ETALASE_ADD_PROD);
            List<Etalase> etalaseList = CacheUtil.convertStringToListModel(jsonString,
                    new TypeToken<List<Etalase>>() {
                    }.getType());
            return Observable.just(etalaseList);
        }
        catch (RuntimeException e) {
            return null;
        }
        /*List<DataEtalaseDB> dataEtalaseDBList = new Select().from(DataEtalaseDB.class).queryList();

        return Observable.from(dataEtalaseDBList)
                .map(new Func1<DataEtalaseDB, Etalase>() {
                    @Override
                    public Etalase call(DataEtalaseDB dataEtalaseDB) {
                        return new Etalase(dataEtalaseDB);
                    }
                })
                .toList();*/
    }

    public static void saveEtalaseListToCache(List<Etalase> etalaseList) {
        GlobalCacheManager manager = new GlobalCacheManager();
        String jsonString = CacheUtil.convertListModelToString(
                etalaseList,
                new TypeToken<List<Etalase>>() {
                }.getType());
        manager.setKey(TkpdCache.ETALASE_ADD_PROD);
        manager.setValue(jsonString);
        manager.setCacheDuration(60);
        manager.store();
        /*final DatabaseWrapper database = FlowManager.getDatabase(TkpdSellerDatabase.NAME).getWritableDatabase();
        database.beginTransaction();
        try {
            DataEtalaseDB dataEtalaseDB = new DataEtalaseDB();
            for (int i = 0, sizei = etalaseList.size(); i < sizei; i++) {
                Etalase etalase = etalaseList.get(i);
                dataEtalaseDB.etalaseId = etalase.getEtalaseId();
                dataEtalaseDB.etalaseName = etalase.getEtalaseName();
                dataEtalaseDB.etalaseNumProduct = etalase.getEtalaseNumProduct();
                dataEtalaseDB.etalaseTotalProduct = etalase.getEtalaseTotalProduct();
                dataEtalaseDB.save();
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }*/
    }

    public static boolean isCacheExpired(){
        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        String value;
        try {
            value = globalCacheManager.getValueString(TkpdCache.ETALASE_ADD_PROD);
        }
        catch (RuntimeException e) {
            return true;
        }
        if (null == value) {
            return true;
        }
        return false;
    }

    public static void setExpired(){
        clearCache();
    }

    public static void clearCache(){
        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        globalCacheManager.delete(TkpdCache.ETALASE_ADD_PROD);
    }



}
