package com.tokopedia.tkpd.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.tkpd.database.DbFlowDatabase;

/**
 * Created on 4/9/16.
 */
@Table(database = DbFlowDatabase.class, primaryKeyConflict = ConflictAction.REPLACE)
public class InboxResCenterFilterDB  extends BaseModel {

    @PrimaryKey
    @Column
    public String inboxTabID;

    @Column
    public String inboxTabName;

    @Column
    public int filterStatus;

    @Column
    public String filterStatusText;

    @Column
    public int filterRead;

    @Column
    public String filterReadText;

    @Column
    public int filterTime;

    @Column
    public String filterTimeText;
}