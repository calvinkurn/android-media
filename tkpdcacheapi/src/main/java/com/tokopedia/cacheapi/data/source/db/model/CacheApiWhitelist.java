package com.tokopedia.cacheapi.data.source.db.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.UniqueGroup;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.cacheapi.data.source.db.DbFlowDatabase;

/**
 * Created by normansyahputa on 8/9/17.
 */
@Table(database = DbFlowDatabase.class,
        uniqueColumnGroups = {@UniqueGroup(groupNumber = 1, uniqueConflict = ConflictAction.ABORT)})
public class CacheApiWhitelist extends BaseModel {
    @PrimaryKey
    @Column
    private String host;

    @PrimaryKey
    @Column
    private String path;

    @Column
    private long expiredTime;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    @Override
    public String toString() {
        return host + path;
    }
}
