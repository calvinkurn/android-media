package com.tokopedia.notifications.inApp.ruleEngine.storage.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "elapsed_time")
public class ElapsedTime {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "elt")
    public long elapsedTime;
}
