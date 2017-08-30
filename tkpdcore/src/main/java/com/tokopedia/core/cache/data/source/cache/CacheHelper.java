package com.tokopedia.core.cache.data.source.cache;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiData_Table;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist_Table;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by normansyahputa on 8/9/17.
 */

public class CacheHelper {
    private static final String TAG = "CacheHelper";


    public CacheHelper() {

    }

    public Observable<Boolean> addWhiteListData(CacheApiWhitelist cacheApiWhitelist) {
        try {
            if (cacheApiWhitelist == null) {
                return Observable.just(false);
            }

            cacheApiWhitelist.save();

            return Observable.just(true);
        } catch (Exception e) {
            return Observable.just(false);
        }
    }

    public CacheApiWhitelist from(String host, String path, long expiredTime) {
        CacheApiWhitelist cacheApiWhitelist = new CacheApiWhitelist();
        cacheApiWhitelist.setHost(host);
        cacheApiWhitelist.setPath(path);
        cacheApiWhitelist.setExpiredTime(expiredTime);

        return cacheApiWhitelist;
    }

    public static CacheApiWhiteListDomain from2(String host, String path, long expiredTime){
        CacheApiWhiteListDomain cacheApiWhitelist = new CacheApiWhiteListDomain();
        cacheApiWhitelist.setHost(host.replace("https://", "").replace(".com/", ".com"));
        cacheApiWhitelist.setPath(path);
        cacheApiWhitelist.setExpireTime(expiredTime);

        return cacheApiWhitelist;
    }

    public boolean queryFrom(String host, String path) {
        return queryFromRaw(host, path) != null;
    }

    public CacheApiWhitelist queryFromRaw(String host, String path) {
        return new Select()
                .from(CacheApiWhitelist.class)
                .where(CacheApiWhitelist_Table.host.like("%" + host + "%"))
                .and(CacheApiWhitelist_Table.path.like("%" + path + "%"))
                .querySingle();
    }

    public CacheApiData queryDataFrom(String host, String path, String param) {
        Where<CacheApiData> and = new Select()
                .from(CacheApiData.class)
                .where(CacheApiData_Table.host.like("%" + host + "%"))
                .and(CacheApiData_Table.path.like("%" + path + "%"))
                .and(CacheApiData_Table.requestParam.like("%" + param + "%"));
        Log.d(TAG, "queryDataFrom : "+and
                .toString());
        return and.querySingle();
    }

    public List<CacheApiData> queryDataFrom(String host, String path) {
        Where<CacheApiData> and = new Select()
                .from(CacheApiData.class)
                .where(CacheApiData_Table.host.like("%" + host + "%"))
                .and(CacheApiData_Table.path.like("%" + path + "%"));
        Log.d(TAG, "queryDataFrom : " + and
                .toString());
        return and.queryList();
    }

    public void clearTimeout() {
        long currentTime = System.currentTimeMillis() / 1000L;
        List<CacheApiData> cacheApiDatas = new Select().from(CacheApiData.class).where(CacheApiData_Table.expiredDate.lessThan(currentTime)).queryList();
        if (cacheApiDatas != null) {
            for (int i = 0; i < cacheApiDatas.size(); i++) {
                cacheApiDatas.get(i).delete();
            }
        }
    }
}
