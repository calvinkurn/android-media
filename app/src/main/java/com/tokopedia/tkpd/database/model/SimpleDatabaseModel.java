package com.tokopedia.tkpd.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.tkpd.database.DbFlowDatabase;

/**
 * Created by ricoharisin on 11/23/15.
 */

@Table(database = DbFlowDatabase.class, primaryKeyConflict = ConflictAction.REPLACE)
public class SimpleDatabaseModel extends BaseModel {

    @PrimaryKey
    @Column
    public String key;

    @Column
    public String value;

    @Column
    public long expiredTime = 0;
}