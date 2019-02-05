package com.tokopedia.core.gcm.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.CoreLegacyDbFlowDatabase;

/**
 * Created by alvarisi on 2/22/17.
 */
@Table(database = CoreLegacyDbFlowDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class DbPushNotification extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    private String category;

    @Column
    private String response;

    @Column
    private String customIndex;

    @Column
    private String serverId;

    public DbPushNotification() {
    }


    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return this.response;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }

    public String getCustomIndex() {
        return customIndex;
    }

    public void setCustomIndex(String customIndex) {
        this.customIndex = customIndex;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
