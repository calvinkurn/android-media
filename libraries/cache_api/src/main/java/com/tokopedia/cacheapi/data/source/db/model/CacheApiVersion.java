package com.tokopedia.cacheapi.data.source.db.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.cacheapi.data.source.db.DbFlowDatabase;

/**
 * Created by normansyahputa on 8/9/17.
 */
@Table(database = DbFlowDatabase.class, updateConflict = ConflictAction.REPLACE, insertConflict = ConflictAction.REPLACE)
public class CacheApiVersion extends BaseModel {

    @PrimaryKey
    @Column
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
