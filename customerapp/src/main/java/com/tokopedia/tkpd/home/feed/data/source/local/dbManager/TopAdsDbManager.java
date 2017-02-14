package com.tokopedia.tkpd.home.feed.data.source.local.dbManager;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tokopedia.core.base.data.DbManagerOperation;
import com.tokopedia.core.database.model.DbTopAds;

import retrofit2.Response;
import rx.Observable;

/**
 * @author Kulomady on 1/3/17.
 */

public class TopAdsDbManager
        implements DbManagerOperation<DbTopAds, Observable<Response<String>>> {

    @Override
    public void store(DbTopAds data) {
        data.save();
    }

    @Override
    public void delete() {
        Delete.tables(DbTopAds.class);
    }

    @Override
    public boolean isExpired(long currentTime) {
        try {
            long lastUpdated = getTable().getLastUpdated();
            return isQueryDataEmpty() || isMoreThanFifteenMinute(currentTime, lastUpdated);
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public DbTopAds getTable() {
        return SQLite.select()
                .from(DbTopAds.class)
                .querySingle();
    }

    @Override
    public Observable<Response<String>> getData() {
        String contentTopAds = getTable().getContentTopAds();
        if (contentTopAds != null) {
            return Observable.just(Response.success(contentTopAds));
        } else {
            return Observable.empty();
        }

    }

    @Override
    public boolean isQueryDataEmpty() {
        return getTable() == null;
    }

    private boolean isMoreThanFifteenMinute(long currentTime, long oldTime) {
        long fifteenMinutes = 1000 * 60 * 15;
        return currentTime - oldTime >= fifteenMinutes;
    }

}
