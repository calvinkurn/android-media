package com.tokopedia.notifications.inApp.ruleEngine.storage.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "elapsed_time")
public class ElapsedTime {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;
    @ColumnInfo(name = "elt")
    public long elapsedTime;
}
