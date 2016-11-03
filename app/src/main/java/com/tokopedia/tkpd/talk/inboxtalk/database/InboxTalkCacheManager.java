package com.tokopedia.tkpd.talk.inboxtalk.database;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.tkpd.database.model.InboxTalkModelDB;
import com.tokopedia.tkpd.database.model.InboxTalkModelDB_Table;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stevenfredian on 4/11/16.
 */
public class InboxTalkCacheManager {

    private static String TAG = "CacheInboxTalk";
    private String nav;
    private String json;
    private long expiredTime;

    public InboxTalkCacheManager(){

    }

    public InboxTalkCacheManager setCacheDuration(int duration) {
        Log.i(TAG, "Storing expired time: " + (System.currentTimeMillis() + (duration * 1000)));
        this.expiredTime = System.currentTimeMillis()+(duration*1000);

        return this;
    }

    public void setNav(String nav, String filter) {
        this.nav = nav+filter;
    }

    public InboxTalkCacheManager setResult(JSONObject result) {
        this.json = String.valueOf(result);
        return this;
    }

    public void saveCache() {
        InboxTalkModelDB cache = new InboxTalkModelDB();
        cache.nav = this.nav;
        cache.json = this.json;
        Log.i(TAG, "Store expired time: "+this.expiredTime);
        cache.expiredTime = this.expiredTime;
        cache.save();
    }

    private Boolean isCacheExpired(long expiredTime) {
        if (expiredTime == 0) return false;
        Log.i(TAG, "Cache expired time: "+expiredTime);
        Log.i(TAG, "Cache current time: " + System.currentTimeMillis());
        return expiredTime < System.currentTimeMillis();
    }

    public InboxTalkCacheManager getCache(String NAV) throws RuntimeException {
        InboxTalkModelDB cache = new Select().from(InboxTalkModelDB.class)
                .where(InboxTalkModelDB_Table.nav.is(NAV))
                .querySingle();

        if (isCacheExpired(cache.expiredTime)) {
            throw new RuntimeException("Cache is expired");
        }
        else {
            this.json = cache.json;
            return this;
        }
    }

    public JSONObject getJson() throws JSONException {
        return new JSONObject(this.json);
    }

    public static void ClearCache(){
        new Delete().from(InboxTalkModelDB.class).execute();
    }
}
