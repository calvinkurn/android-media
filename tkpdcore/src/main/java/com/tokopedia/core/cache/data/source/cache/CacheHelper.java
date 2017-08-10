package com.tokopedia.core.cache.data.source.cache;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiData_Table;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist_Table;

import rx.Observable;

/**
 * Created by normansyahputa on 8/9/17.
 */

public class CacheHelper {


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
        cacheApiWhitelist.host = host;
        cacheApiWhitelist.path = path;
        cacheApiWhitelist.expiredTime = expiredTime;

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
        return new Select()
                .from(CacheApiData.class)
                .where(CacheApiData_Table.host.like("%" + host + "%"))
                .and(CacheApiData_Table.path.like("%" + path + "%"))
                .and(CacheApiData_Table.requestParam.like("%" + param + "%"))
                .querySingle();
    }
}
