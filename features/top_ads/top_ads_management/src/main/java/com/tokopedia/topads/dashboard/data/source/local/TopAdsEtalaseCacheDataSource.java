package com.tokopedia.topads.dashboard.data.source.local;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.topads.dashboard.data.source.TopAdsEtalaseDataSource;
import com.tokopedia.topads.dashboard.data.model.data.Etalase;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
/**
 * Created by hendry on 2/20/17.
 */
public class TopAdsEtalaseCacheDataSource implements TopAdsEtalaseDataSource {

    private static final long EXPIRED_TIME = TimeUnit.MINUTES.toMillis(60);
    public TopAdsEtalaseCacheDataSource() {

    }

    public Observable<List<Etalase>> getEtalaseList(String shopId, String userId, String deviceId) {

        try {
            String jsonString = PersistentCacheManager.instance.getString(TkpdCache.Key.ETALASE_ADD_PROD);
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
        String jsonString = CacheUtil.convertListModelToString(
                etalaseList,
                new TypeToken<List<Etalase>>() {
                }.getType());
        PersistentCacheManager.instance.put(TkpdCache.Key.ETALASE_ADD_PROD, jsonString, EXPIRED_TIME);
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
        String value;
        try {
            value = PersistentCacheManager.instance.getString(TkpdCache.Key.ETALASE_ADD_PROD);
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
        PersistentCacheManager.instance.delete(TkpdCache.Key.ETALASE_ADD_PROD);
    }



}
