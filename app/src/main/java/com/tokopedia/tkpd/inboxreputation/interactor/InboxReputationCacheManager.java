package com.tokopedia.tkpd.inboxreputation.interactor;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.tkpd.database.model.InboxReputationDetailModelDB;
import com.tokopedia.tkpd.database.model.InboxReputationDetailModelDB_Table;

/**
 * Created by Nisie on 3/17/16.
 */
public class InboxReputationCacheManager {

    private String reviewId;
    private String data;
    private long expiredTime = 0;
    private static String TAG = "InboxReputationCacheManager";

    public InboxReputationCacheManager() {

    }

    public InboxReputationCacheManager setReviewId(String reviewId) {
        this.reviewId = reviewId;
        return this;
    }

    public InboxReputationCacheManager setData(String data) {
        this.data = data;
        return this;
    }

    public InboxReputationCacheManager setCacheDuration(int duration) {
        Log.i(TAG, "Storing expired time: " + (System.currentTimeMillis() + (duration * 1000)));
        this.expiredTime = System.currentTimeMillis() + (duration * 1000);
        return this;
    }

    public void save() {
        InboxReputationDetailModelDB cache = new InboxReputationDetailModelDB();
        cache.id = reviewId;
        cache.data = data;
        cache.expiredTime = this.expiredTime;
        cache.save();
    }

    public String getCache(String reviewId) throws RuntimeException {
        try {
            InboxReputationDetailModelDB cache = new Select().from(InboxReputationDetailModelDB.class)
                    .where(InboxReputationDetailModelDB_Table.id.is(reviewId))
                    .querySingle();

            if (isCacheExpired(cache.expiredTime)) {
                throw new RuntimeException("Cache is expired");
            } else {
                return cache.data;
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("Cache is expired");
        }
    }

    private Boolean isCacheExpired(long expiredTime) {
        if (expiredTime == 0) return false;
        Log.i(TAG, "Cache expired time: " + expiredTime);
        Log.i(TAG, "Cache current time: " + System.currentTimeMillis());
        return expiredTime < System.currentTimeMillis();
    }

    public void delete(String key) {
        new Delete().from(InboxReputationDetailModelDB.class).where(InboxReputationDetailModelDB_Table.id.is(key)).execute();
    }

    public void deleteAll() {
        new Delete().from(InboxReputationDetailModelDB.class).execute();
    }
}