package com.tokopedia.filter.newdynamicfilter.database;

import java.io.Serializable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class FilterDBModel implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "filter_id")
    @NonNull
    private String filterId;

    @ColumnInfo(name = "filter_data")
    private String filterData;

    public FilterDBModel(String filterId, String filterData) {
        this.filterId = filterId;
        this.filterData = filterData;
    }

    public String getFilterId() {
        return filterId;
    }

    public String getFilterData() {
        return filterData;
    }
}
