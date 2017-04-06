
package com.tokopedia.core.network.entity.replacement.opportunitycategorydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SortingType {

    @SerializedName("sortingTypeID")
    @Expose
    private int sortingTypeID;
    @SerializedName("sortingTypeName")
    @Expose
    private String sortingTypeName;

    public int getSortingTypeID() {
        return sortingTypeID;
    }

    public void setSortingTypeID(int sortingTypeID) {
        this.sortingTypeID = sortingTypeID;
    }

    public String getSortingTypeName() {
        return sortingTypeName;
    }

    public void setSortingTypeName(String sortingTypeName) {
        this.sortingTypeName = sortingTypeName;
    }

}
